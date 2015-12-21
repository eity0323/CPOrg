package com.sien.cporg.control;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.sien.cporg.model.beans.Employee;
import com.sien.cporg.utils.events.IMAPPEvents;
import com.sien.cporg.utils.events.IMAPPEvents.LoadEmployeeEvent;
import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.parser.JsonMananger;
import com.sien.cporg.utils.request.RequestNetorLocalJson;
import com.sien.cporg.utils.response.EmployeeResponse;

import de.greenrobot.event.EventBus;

/**
 * 员工数据管理
 * 
 * @author sien
 * 
 */
public class EmployeeManager extends BaseOrgManager{

	private static EmployeeManager instance = null;

	private HashMap<String, List<Employee>> datasource = new HashMap<>(); // 静态页面数据源
	private List<Employee> employees = null;
	
	private String employeeFile = "employee.json";
	private String employeeUrl = "http://58.250.204.31:18880/account_auth_admin/personal-api.getEmployeesByDepartmentId?departmentId=2340&sessionId=b63f29f514d44022a3d3ef07cd043780";
	
	private String requestMode = employeeFile;//请求模式

	public static EmployeeManager getInstance() {
		if (instance == null) {
			synchronized (EmployeeManager.class) {
				if (instance == null) {
					instance = new EmployeeManager();
				}
			}
		}
		return instance;
	}

	@Override
	protected void parseLoadData(String result) {
		try {
			EmployeeResponse response = JsonMananger.jsonToBean(result, EmployeeResponse.class);
			if (response != null) {
				employees = response.getData();
				
				datasource.put(requestMode, employees);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void resultDatas() {
		int status = LoadEmployeeEvent.STATUS_FAIL;

		if (employees != null) {
			status = LoadEmployeeEvent.STATUS_SUCCESS;
			EventBus.getDefault().post(new IMAPPEvents.LoadEmployeeEvent(status, employees));
			return;
		}

		EventBus.getDefault().post(new IMAPPEvents.LoadEmployeeEvent(status, null));
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
		
		if (datasource != null && !datasource.containsKey(requestMode)) { // 初始读取配置文件
			RequestNetorLocalJson req = new RequestNetorLocalJson();
			req.url = requestMode;
			initData(context,req);
		} else { // 从缓存中读取配置信息
			employees = datasource.get(requestMode);
			resultDatas();
		}
	}
	
	public void getDetailData(final Context context,String departmentId) {
		//加载本地数据
//		employeeFile = departmentId;
//		requestMode = employeeFile;
		
		//加载网络数据
		employeeUrl = "http://58.250.204.31:18880/account_auth_admin/personal-api.getEmployeesByDepartmentId?departmentId="+ departmentId +"&sessionId=b63f29f514d44022a3d3ef07cd043780";
		requestMode = employeeUrl;

		getDetailData(context);
	}
}
