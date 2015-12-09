package com.sien.cporg.utils.response;

import java.util.List;

import com.sien.cporg.model.beans.Employee;

/**
 * 组织架构部门数据请求返回类
 * 
 * @author sien
 * 
 */
public class EmployeeResponse extends BaseResponse {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6616932432232786764L;

	private List<Employee> data;

	public List<Employee> getData() {
		return data;
	}

	public void setData(List<Employee> data) {
		this.data = data;
	}

}
