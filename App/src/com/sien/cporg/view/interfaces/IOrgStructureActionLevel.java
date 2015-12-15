package com.sien.cporg.view.interfaces;

import java.util.List;

import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.model.beans.Organize;

/**
 * 组织架构ui接口
 * 
 * @author sien
 * 
 */
public interface IOrgStructureActionLevel {

	/**更新部门数据*/
	public void updateOrganizeLayout(OrgNode node, OrgNode targetNode,Organize data);

	public void refreshEnd();

	/**隐藏加载框*/
	public void hideInnerDialog();
	
	/**显示加载框*/
	public void showToast(String str);
}
