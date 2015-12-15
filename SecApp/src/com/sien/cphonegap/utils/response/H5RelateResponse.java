package com.sien.cphonegap.utils.response;

import java.io.Serializable;
import java.util.List;

import com.sien.cphonegap.model.beans.H5RelateEntity;

public class H5RelateResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2235834619833521842L;
	private List<H5RelateEntity> data;

	public List<H5RelateEntity> getData() {
		return data;
	}

	public void setData(List<H5RelateEntity> data) {
		this.data = data;
	}
	
}
