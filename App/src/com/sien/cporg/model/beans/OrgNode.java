package com.sien.cporg.model.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 * */
public class OrgNode {
	private OrgNode parent;// 父节点
	private List<OrgNode> children = new ArrayList<OrgNode>();// 子节点
	private Employee employees;
	
	private boolean isChecked = false;// 是否处于选中状态
	private boolean isExpanded = true;// 是否处于展开状态
	private boolean hasCheckBox = true;// 是否拥有复选框
	
	/*组织架构数据结构*/
	private String CAbbrName;// 部门简称
	private String CDptId;// 部门ID
	private String CLevel;// 部门层级
	private String CName;// 部门全称
	private String CParentId;// 上级部门id
	private String NCount;// 部门中人数
	

	/**
	 * Node构造函数
	 * 
	 * @param text
	 *            节点显示的文字
	 * @param value
	 *            节点的值
	 */
	public OrgNode(Employee employees) {
		this.employees = employees;
	}
	
	public OrgNode() {
		
	}

	/**
	 * 设置父节点
	 * 
	 * @param node
	 */
	public void setParent(OrgNode node) {
		this.parent = node;
	}

	/**
	 * 获得父节点
	 * 
	 * @return
	 */
	public OrgNode getParent() {
		return this.parent;
	}
	
	/**组织架构数据结构*/
	public String getCAbbrName() {
		return CAbbrName;
	}

	public void setCAbbrName(String cAbbrName) {
		CAbbrName = cAbbrName;
	}

	public String getCDptId() {
		return CDptId;
	}

	public void setCDptId(String cDptId) {
		CDptId = cDptId;
	}

	public String getCLevel() {
		return CLevel;
	}

	public void setCLevel(String cLevel) {
		CLevel = cLevel;
	}

	public String getCName() {
		return CName;
	}

	public void setCName(String cName) {
		CName = cName;
	}

	public String getCParentId() {
		return CParentId;
	}

	public void setCParentId(String cParentId) {
		CParentId = cParentId;
	}

	public String getNCount() {
		return NCount;
	}

	public void setNCount(String nCount) {
		NCount = nCount;
	}
	
	/*组织架构成员*/
	public Employee getEmployee() {
		return employees;
	}

	public void setEmployee(Employee employees) {
		this.employees = employees;
	}

	/**
	 * 是否根节点
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return parent == null ? true : false;
	}

	/**
	 * 获得子节点
	 * 
	 * @return
	 */
	public List<OrgNode> getChildren() {
		return this.children;
	}

	/**
	 * 添加子节点
	 * 
	 * @param node
	 */
	public void add(OrgNode node) {
		if (!children.contains(node)) {
			children.add(node);
		}
	}

	/**
	 * 清除所有子节点
	 */
	public void clear() {
		children.clear();
	}

	/**
	 * 删除一个子节点
	 * 
	 * @param node
	 */
	public void remove(OrgNode node) {
		if (!children.contains(node)) {
			children.remove(node);
		}
	}

	/**
	 * 删除指定位置的子节点
	 * 
	 * @param location
	 */
	public void remove(int location) {
		children.remove(location);
	}

	/**
	 * 获得节点的级数,根节点为0
	 * 
	 * @return
	 */
	public int getLevel() {
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置节点选中状态
	 * 
	 * @param isChecked
	 */
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	/**
	 * 获得节点选中状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * 设置是否拥有复选框
	 * 
	 * @param hasCheckBox
	 */
	public void setCheckBox(boolean hasCheckBox) {
		this.hasCheckBox = hasCheckBox;
	}

	/**
	 * 是否拥有复选框
	 * 
	 * @return
	 */
	public boolean hasCheckBox() {
		return hasCheckBox;
	}

	/**
	 * 是否叶节点,即没有子节点的节点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return children.size() < 1 ? true : false;
	}

	/**
	 * 当前节点是否处于展开状态
	 * 
	 * @return
	 */
	public boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * 设置节点展开状态
	 * 
	 * @return
	 */
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	/**
	 * 递归判断父节点是否处于折叠状态,有一个父节点折叠则认为是折叠状态
	 * 
	 * @return
	 */
	public boolean isParentCollapsed() {
		if (parent == null)
			return !isExpanded;
		if (!parent.isExpanded())
			return true;
		return parent.isParentCollapsed();
	}

	/**
	 * 递归判断所给的节点是否当前节点的父节点
	 * 
	 * @param node
	 *            所给节点
	 * @return
	 */
	public boolean isParent(OrgNode node) {
		if (parent == null)
			return false;
		if (node.equals(parent))
			return true;
		return parent.isParent(node);
	}
}
