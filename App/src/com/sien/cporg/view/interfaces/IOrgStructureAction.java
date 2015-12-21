package com.sien.cporg.view.interfaces;

import java.util.List;

import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.OrgNode;

/**
 * 组织架构ui接口
 * 
 * @author sien
 * 
 */
public interface IOrgStructureAction {

	/**更新部门数据*/
	public void updateDepartmentLayout(List<Department> data);

	/**更新员工数据*/
	public void updateEmployeeLayout(OrgNode rootNode, OrgNode targetNode);

	public void refreshEnd();

	/**隐藏加载框*/
	public void hideInnerDialog();
	
	/**显示加载框*/
	public void showToast(String str);
	
	/**没有成员的时候还需要更新部门*/
	public void refreshDempart(int index);
}
