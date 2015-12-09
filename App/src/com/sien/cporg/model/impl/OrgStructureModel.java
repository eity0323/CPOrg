package com.sien.cporg.model.impl;

import android.content.Context;

import com.sien.cporg.control.DepartmentManager;
import com.sien.cporg.control.EmployeeManager;
import com.sien.cporg.model.interfaces.IOrgStructureModel;

/**
 * 组织架构数据处理
 * 
 * @author sien
 * 
 */
public class OrgStructureModel implements IOrgStructureModel {
	public OrgStructureModel(){
		
	}

	@Override
	public void getDepartmentData(Context context, String sessionId, String loginJid, boolean fromCache){
		DepartmentManager.getInstance().getDetailData(context);
	}

	@Override
	public void getEmployeesData(Context context, String departId, String sessionId, String loginJid, boolean fromCache){
		double ram = Math.random();
		if(ram > 0.5f){
			EmployeeManager.getInstance().getDetailData(context,"employee1.json");
		}else{
			EmployeeManager.getInstance().getDetailData(context,"employee.json");
		}
	}
}
