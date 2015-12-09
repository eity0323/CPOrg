package com.sien.cporg.model.beans;

import java.sql.Timestamp;
import java.util.List;

public class Department extends BaseModel {
	private static final long serialVersionUID = 3616342432232786764L;

	private long departmentId;// 部门ID
	private long parentId;// 上级部门ID
	private String enterpriseCode;// 企业编码
	private String name;// 部门名称
	private String abbrName;// 别名
	private String order;// 顺序
	private String level;// 层级
	private Timestamp createTime;// 创建时间
	private Timestamp lastUpdateTime;// 修改时间
	private List<Department> subDptList;//子级部门
	
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbrName() {
		return abbrName;
	}
	public void setAbbrName(String abbrName) {
		this.abbrName = abbrName;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public List<Department> getSubDptList() {
		return subDptList;
	}
	public void setSubDptList(List<Department> subDptList) {
		this.subDptList = subDptList;
	}
	
}
