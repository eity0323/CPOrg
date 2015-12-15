package com.sien.cphonegap.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cphonegap.model.beans.H5RelateData;
import com.sien.cphonegap.model.beans.H5RelateEntity;
import com.sien.cphonegap.model.beans.H5RelateEntity.SubEntity;
import com.sien.cphonegap.utils.events.H5RelateEvents;
import com.sien.cphonegap.utils.events.H5RelateEvents.loadH5RelateEvent;
import com.sien.cphonegap.utils.log.NLog;
import com.sien.cphonegap.utils.parser.JsonMananger;
import com.sien.cphonegap.utils.requests.BaseRequestData;
import com.sien.cphonegap.utils.response.H5RelateResponse;

import de.greenrobot.event.EventBus;

/**
 * h5相关数据管理
 * 
 * @author sien
 * 
 */
public class H5RelateDataManager {

	private static H5RelateDataManager instance = null;

	private List<H5RelateData> datasource = null; // 静态页面数据源

	private static boolean loaded = false; // 是否已经加载过配置

	public static H5RelateDataManager getInstance() {
		if (instance == null) {
			synchronized (H5RelateDataManager.class) {
				if (instance == null) {
					instance = new H5RelateDataManager();
				}
			}
		}
		return instance;
	}

//	/**
//	 * 初始化读取数据
//	 * 
//	 * @param context
//	 *            应用上下文
//	 * @param tag
//	 *            页面数据标签（类名）
//	 * 
//	 * <br>
//	 * @注 需要订阅onEventAsync(loadH5RelateEvent event)事件
//	 * */
//	public synchronized void init(Context context, String tag,final BaseRequestData request) {
//		if (context == null) {
//			NLog.d("","H5RelateDataManager context can not be null");
//			EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(loadH5RelateEvent.STATUS_FAIL, null));
//			return;
//		}
//
//		if (TextUtils.isEmpty(tag)) {
//			NLog.d("","H5RelateDataManager tag can not be null");
//			EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(loadH5RelateEvent.STATUS_FAIL, null));
//			return;
//		}
//
//		if (!loaded || datasource == null) {
//			loaded = true;
//			initData(context, tag, true,request);
//		}
//	}

	/**
	 * 根据标签获取页面数据
	 * 
	 * @param context
	 *            应用上下文
	 * @param tag
	 *            页面数据标签（类名）
	 * 
	 * <br>
	 * @注 需要订阅onEventAsync(loadH5RelateEvent event)事件
	 */
	public void getDataByTag(final Context context, final String tag,final BaseRequestData request) {
		if (context == null) {
			NLog.d("","H5RelateDataManager context can not be null");
			EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(loadH5RelateEvent.STATUS_FAIL, null));
			return;
		}

		if (TextUtils.isEmpty(tag)) {
			EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(loadH5RelateEvent.STATUS_FAIL, null));
			return;
		}

		if (datasource == null) { // 初始读取配置文件
			initData(context, tag, false,request);
		} else { // 从缓存中读取配置信息
			getSignTagData(tag);
		}
	}

	/* 读取本地json数据 */
	private void initData(final Context context, final String tag, final boolean isInited,final BaseRequestData request) {
		Thread temp = new Thread(new Runnable() {

			@Override
			public void run() {
				doLoad(context,request);

				// 当用户没有调用初始化读取本地文件时，第一次获取数据时读取本地文件并显示数据
				if (!isInited) {
					getSignTagData(tag);
				}
			}
		});
		temp.start();
	}

	/** 获取指定标签的数据 */
	private void getSignTagData(String tag) {
		if (TextUtils.isEmpty(tag)) {
			EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(loadH5RelateEvent.STATUS_FAIL, null));
			return;
		}

		H5RelateData data = null;
		int status = loadH5RelateEvent.STATUS_FAIL;

		if (datasource != null) {
			for (H5RelateData item : datasource) {
				if (item.getPageTag().equals(tag)) {
					data = item;
					status = loadH5RelateEvent.STATUS_SUCCESS;
					break;
				}
			}
		}

		EventBus.getDefault().post(new H5RelateEvents.loadH5RelateEvent(status, data));
	}

	/* 加载本地数据 (默认从网络加载，加载失败则获取本地配置)*/
	private synchronized void doLoad(Context context,BaseRequestData request) {
		if (!loaded || datasource == null) {
			String jsonStr = request.requestData(context);

			if (TextUtils.isEmpty(jsonStr)) {
				NLog.d("","H5RelateDataManager json string is empty!");
				loaded = false;
				return;
			}

			try {
				H5RelateResponse response = JsonMananger.jsonToBean(jsonStr, H5RelateResponse.class);
				if (response != null) {
					datasource = formatH5RelateData2List(response.getData());
				}

			} catch (Exception e) {
				e.printStackTrace();
				loaded = false;
			}
		}
	}
	
	/**格式化h5配置数据*/
	private List<H5RelateData> formatH5RelateData2List(List<H5RelateEntity> entities){
		//TODO 将请求的数据格式化为页面使用的数据
		List<H5RelateData> tempList = new ArrayList<H5RelateData>();
		
		H5RelateData obj = null;
		String str = "";
		if(entities != null){
			for(H5RelateEntity item:entities){
				List<SubEntity> list = item.getItemList();
				str = item.getItemTitle();
				if(list != null){
					for(SubEntity subitem:list){
						obj = new H5RelateData();
						obj.setListData(subitem.getSubItemList());
						obj.setMenuData(subitem.getMenuData());
						obj.setPageTag(subitem.getPageTag());
						obj.setShowMenu(subitem.isShowMenu());
						obj.setSubType(subitem.getSubItemTitle());
						obj.setType(str);
						
						tempList.add(obj);
					}
				}
			}
		}
		return tempList;
	}
	
}
