package com.sien.cporg.utils.response;

import java.util.List;

import com.sien.cporg.model.beans.Department;

/**
 * 组织架构部门数据请求返回类
 * 
 * @author sien
 * 
 */
public class DepartmentResponse extends BaseResponse {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6616932432232786764L;

	private List<Department> data;

	public List<Department> getData() {
		return data;
	}

	public void setData(List<Department> data) {
		this.data = data;
	}

}
