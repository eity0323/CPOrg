package com.sien.cporg.model.interfaces;

import android.content.Context;

/**
 * 组织架构数据接口
 * 
 * @author sien
 * 
 */
public interface IOrgStructureModelLevel {
	/**
	 * 获取组织架构数据
	 * 
	 * @param context
	 *            应用上下文
	 * @param requestCode
	 *            请求码
	 * @param sessionId
	 *            事务id
	 * @param departId
	 *            部门id
	 * @param loginJid
	 *            当前登录用户jid
	 * @param fromCache
	 *            是否需要启用缓存
	 * 
	 * <br>
	 * @注 需要订阅处理IMAPPEvents.loadDepartmentEvent事件
	 */
	public void getOrganizeData(Context context, String sessionId,String departId, String loginJid, boolean fromCache);
}
