package com.sien.cporg.model.impl;

import android.content.Context;

import com.sien.cporg.control.OrganizeManager;
import com.sien.cporg.model.interfaces.IOrgStructureModelLevel;

/**
 * 组织架构数据处理
 * 
 * @author sien
 * 
 */
public class OrgStructureModelLevel implements IOrgStructureModelLevel {
	public OrgStructureModelLevel(){
		
	}

	@Override
	public void getOrganizeData(Context context, String sessionId, String departId, String loginJid, boolean fromCache) {
		OrganizeManager.getInstance().getDetailData(context,departId);
	}
}
