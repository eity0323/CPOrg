package com.sien.cporg.utils.events;

import java.util.List;

import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.Employee;

/**
 * 事件管理类
 * 
 * @author sien
 * 
 */
public class IMAPPEvents {
	/** 加载部门数据 */
	public static class LoadDepartmentEvent {
		public static int STATUS_SUCCESS = 0;
		public static int STATUS_FAIL = 1;

		private int status;
		private List<Department> items;

		public LoadDepartmentEvent(int status, List<Department> data) {
			this.status = status;
			this.items = data;
		}

		public int getStatus() {
			return status;
		}

		public List<Department> getData() {
			return items;
		}
	}

	/** 加载部门员工数据 */
	public static class LoadEmployeeEvent {
		public static int STATUS_SUCCESS = 0;
		public static int STATUS_FAIL = 1;

		private int status;
		private List<Employee> items;

		public LoadEmployeeEvent(int status, List<Employee> data) {
			this.status = status;
			this.items = data;
		}

		public int getStatus() {
			return status;
		}

		public List<Employee> getData() {
			return items;
		}
	}


	/** 搜索企业员工 */
	public static class searchEmployeeEvent {
		public static int STATUS_SUCCESS = 0;
		public static int STATUS_FAIL = 1;

		private int status;
		private List<Employee> items;

		public searchEmployeeEvent(int status, List<Employee> data) {
			this.status = status;
			this.items = data;
		}

		public int getStatus() {
			return status;
		}

		public List<Employee> getData() {
			return items;
		}
	}

}
