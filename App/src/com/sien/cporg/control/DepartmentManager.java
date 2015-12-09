package com.sien.cporg.control;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cporg.model.beans.Department;
import com.sien.cporg.utils.events.IMAPPEvents;
import com.sien.cporg.utils.events.IMAPPEvents.LoadDepartmentEvent;
import com.sien.cporg.utils.events.IMAPPEvents.LoadEmployeeEvent;
import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.parser.JsonMananger;
import com.sien.cporg.utils.response.DepartmentResponse;

import de.greenrobot.event.EventBus;

/**
 * 部门数据管理
 * 
 * @author sien
 * 
 */
public class DepartmentManager extends BaseOrgManager{

	private static DepartmentManager instance = null;
	
	private List<Department> datasource = null; // 静态页面数据源
	
	private static boolean loaded = false; // 是否已经加载过配置
	
	public static DepartmentManager getInstance() {
		if (instance == null) {
			synchronized (DepartmentManager.class) {
				if (instance == null) {
					instance = new DepartmentManager();
				}
			}
		}
		return instance;
	}
	
	@Override
	protected String getDefaultConfigFile() {
		return "organize.json";
	}

	@Override
	protected boolean parseLoadData(String jsonStr) {
		if(TextUtils.isEmpty(jsonStr)){
			loaded = false;
		}
		
		boolean dataLoaded = false;
		try {
			DepartmentResponse response = JsonMananger.jsonToBean(jsonStr, DepartmentResponse.class);
			if (response != null) {
				datasource = response.getData();
				
				dataLoaded = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dataLoaded;
	}

	@Override
	protected void resultDatas() {
		int status = LoadDepartmentEvent.STATUS_FAIL;

		if (datasource != null) {
			status = LoadEmployeeEvent.STATUS_SUCCESS;
			EventBus.getDefault().post(new IMAPPEvents.LoadDepartmentEvent(status, datasource));
			return;
		}

		EventBus.getDefault().post(new IMAPPEvents.LoadDepartmentEvent(status, datasource));
	}
	
	/**
	 * 获取数据
	 * 
	 * @param context
	 *            应用上下文
	 * @param tag
	 *            页面数据标签（类名）
	 * 
	 * <br>
	 * @注 需要订阅onEventAsync(LoadEmployeeEvent event)事件
	 */
	public void getDetailData(final Context context) {
		if (context == null) {
			NLog.d("EmployeeManager", "EmployeeManager context can not be null");
			EventBus.getDefault().post(new IMAPPEvents.LoadEmployeeEvent(LoadEmployeeEvent.STATUS_FAIL, null));
			return;
		}

		if (!loaded) { // 初始读取配置文件
			loaded = true;
			initData(context);
		} else { // 从缓存中读取配置信息
			resultDatas();
		}
	}
}
