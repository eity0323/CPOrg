package com.sien.cphonegap.model.beans;

import java.io.Serializable;


/**
 * js调用java的webactivity页面传的参数
 * @author sien
 *
 */
public class H5Params implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8104761171764027697L;
	private String title;
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
