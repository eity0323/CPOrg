package com.sien.cporg.utils.response;

import com.sien.cporg.model.beans.Organize;

/**
 * 组织架构部门数据请求返回类
 * 
 * @author sien
 * 
 */
public class OrganizeResponse extends BaseResponse {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6616932432232786764L;

	private Organize data;

	public Organize getData() {
		return data;
	}

	public void setData(Organize data) {
		this.data = data;
	}

}
