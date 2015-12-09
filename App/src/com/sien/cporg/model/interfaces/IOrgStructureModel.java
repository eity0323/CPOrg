package com.sien.cporg.model.interfaces;

import android.content.Context;

/**
 * 组织架构数据接口
 * 
 * @author sien
 * 
 */
public interface IOrgStructureModel {
	/**
	 * 获取部门数据
	 * 
	 * @param context
	 *            应用上下文
	 * @param requestCode
	 *            请求码
	 * @param sessionId
	 *            事务id
	 * @param loginJid
	 *            当前登录用户jid
	 * @param fromCache
	 *            是否需要启用缓存
	 * 
	 * <br>
	 * @注 需要订阅处理IMAPPEvents.loadDepartmentEvent事件
	 */
	public void getDepartmentData(Context context, String sessionId, String loginJid, boolean fromCache);
	
	/**
	 * 根据部门获取员工
	 * 
	 * @param context
	 *            应用上下文
	 * @param requestCode
	 *            请求码
	 * @param departId
	 *            部门id
	 * @param sessionId
	 *            事务id
	 * @param loginJid
	 *            当前登录用户jid
	 * @param fromCache
	 *            是否启用缓存
	 * 
	 * <br>
	 * @注 需要订阅处理IMAPPEvents.loadEmployeesEvent事件
	 */
	public void getEmployeesData(Context context, String departId, String sessionId, String loginJid, boolean fromCache);
}
