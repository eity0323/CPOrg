package com.sien.cporg.utils.response;

import com.sien.cporg.model.beans.BaseModel;

import android.text.TextUtils;

/**
 * [A brief description]
 * 
 * @author devin.hu
 * @version 1.0
 * @date 2013-9-30
 **/
public class BaseResponse extends BaseModel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5616901114632786764L;

	private String status;// 状态代码

	private String message;// 返回信息

	private String page_no;// 页码
	private String page_size;// 每页记录数
	private String row_count;// 记录总数

	private String errorCode;

	private String errormsg;

	public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}

	public String getPage_size() {
		return page_size;
	}

	public void setPage_size(String page_size) {
		this.page_size = page_size;
	}

	public String getRow_count() {
		return row_count;
	}

	public void setRow_count(String row_count) {
		this.row_count = row_count;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	/**
	 * 只适合共轴后台接口
	 * @return
	 */
	public boolean isGZSuccess(){
		if(!TextUtils.isEmpty(status) && "1".equals(status)){
			return true;
		}
		return false;
	}
}
