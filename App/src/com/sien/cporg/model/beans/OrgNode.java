package com.sien.cporg.model.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 * */
public class OrgNode {
	private OrgNode parent;// 父节点
	private List<OrgNode> children = new ArrayList<OrgNode>();// 子节点
	private Employee employees;//成员
	
	/*inner extend*/
	private boolean isChecked = false;// 是否处于选中状态
	private boolean isExpanded = true;// 是否处于展开状态
	private boolean hasCheckBox = true;// 是否拥有复选框
	private boolean isLoadedEmployee = false;//是否已经加载过成员
	
	
	/*组织架构数据结构*/
	private String depName;// 部门简称
	private String depId;// 部门ID
	private String depLevel;// 部门层级
	private String depFullName;// 部门全称
	private String parentId;// 上级部门id
	private String depMemberCount;// 部门中人数
	

	/**
	 * Node构造函数
	 */
	public OrgNode(Employee employees) {
		this.employees = employees;
	}
	
	public OrgNode() {
		
	}

	public boolean isLoadedEmployee() {
		return isLoadedEmployee;
	}

	public void setLoadedEmployee(boolean isLoadedEmployee) {
		this.isLoadedEmployee = isLoadedEmployee;
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
	
	/**组织架构部门数据结构*/
	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepLevel() {
		return depLevel;
	}

	public void setDepLevel(String depLevel) {
		this.depLevel = depLevel;
	}

	public String getDepFullName() {
		return depFullName;
	}

	public void setDepFullName(String depFullName) {
		this.depFullName = depFullName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDepMemberCount() {
		return depMemberCount;
	}

	public void setDepMemberCount(String depMemberCount) {
		this.depMemberCount = depMemberCount;
	}
	
	/*组织架构成员数据*/
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
