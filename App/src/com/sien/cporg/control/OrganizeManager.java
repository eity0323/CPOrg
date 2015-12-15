package com.sien.cporg.control;

import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cporg.model.beans.Organize;
import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.events.IMAPPEvents;
import com.sien.cporg.utils.events.IMAPPEvents.LoadOrganizeEvent;
import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.parser.JsonMananger;
import com.sien.cporg.utils.request.RequestNetorLocalJson;
import com.sien.cporg.utils.response.OrganizeResponse;

import de.greenrobot.event.EventBus;

/**
 * 组织架构数据管理
 * 
 * @author sien
 * 
 */
public class OrganizeManager extends BaseOrgManager{

	private static OrganizeManager instance = null;
	
	private Organize orgdatas = null; // 静态页面数据源
	private HashMap<String, Organize> datasource = new HashMap<String, Organize>();
	
	private String organizeFile = "organize.json";//加载本地数据
	private String organizeUrl = Params.ORG_CONFIG_URL;//加载网络数据
	private String requestMode = organizeFile;
	
	public static OrganizeManager getInstance() {
		if (instance == null) {
			synchronized (OrganizeManager.class) {
				if (instance == null) {
					instance = new OrganizeManager();
				}
			}
		}
		return instance;
	}

	@Override
	protected void parseLoadData(String result) {
		try {
			OrganizeResponse response = JsonMananger.jsonToBean(result, OrganizeResponse.class);
			if (response != null) {
				orgdatas = response.getData();
				
				datasource.put(requestMode, orgdatas);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void resultDatas() {
		int status = LoadOrganizeEvent.STATUS_FAIL;

		if (orgdatas != null) {
			status = LoadOrganizeEvent.STATUS_SUCCESS;
			EventBus.getDefault().post(new IMAPPEvents.LoadOrganizeEvent(status, orgdatas));
			return;
		}

		EventBus.getDefault().post(new IMAPPEvents.LoadOrganizeEvent(status, null));
	}
	
	/**
	 * 获取数据
	 * 
	 * @param context
	 *            应用上下文
	 * 
	 * <br>
	 * @注 需要订阅onEventAsync(LoadOrganizeEvent event)事件
	 */
	public void getDetailData(final Context context) {
		if (context == null) {
			NLog.d("DepartmentManager", "DepartmentManager context can not be null");
			EventBus.getDefault().post(new IMAPPEvents.LoadOrganizeEvent(LoadOrganizeEvent.STATUS_FAIL, null));
			return;
		}

		if (datasource != null && !datasource.containsKey(requestMode)) { // 初始读取配置文件
			RequestNetorLocalJson req = new RequestNetorLocalJson();
			req.url = requestMode;
			initData(context,req);
		} else { // 从缓存中读取配置信息
			orgdatas = datasource.get(requestMode);
			resultDatas();
		}
	}
	
	/**
	 * 获取数据
	 * 
	 * @param context
	 *            应用上下文
	 * @param departId
	 *            页面数据标签（类名）
	 * 
	 * <br>
	 * @注 需要订阅onEventAsync(LoadOrganizeEvent event)事件
	 */
	public void getDetailData(final Context context,String departId) {
		
		if(!TextUtils.isEmpty(departId)){
			requestMode = "organize1.json";
		}

		getDetailData(context);
	}
	
}
