package com.sien.cporg.model.beans;

import java.util.List;

public class Organize extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 307863862394067301L;

	private List<Department> department;
	private List<Employee> employees;
	public List<Department> getDepartment() {
		return department;
	}
	public void setDepartment(List<Department> department) {
		this.department = department;
	}
	public List<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	
}
