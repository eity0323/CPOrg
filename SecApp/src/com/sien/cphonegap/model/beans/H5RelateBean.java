package com.sien.cphonegap.model.beans;

import java.io.Serializable;

/**
 * h5页面相关的数据实体
 * @author sien
 *
 */
public class H5RelateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7603847179577218941L;

	private String icon;// 图片
	private String title;// 标题
	private String context;// 内容
	private String link;// 链接
	private boolean innerLink;// 是否为内部链接
	//add by wei.yi
	private String type;//类型
	private boolean checkLogin = false;//是否需要校验登录状态

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isInnerLink() {
		return innerLink;
	}

	public void setInnerLink(boolean innerLink) {
		this.innerLink = innerLink;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCheckLogin() {
		return checkLogin;
	}

	public void setCheckLogin(boolean checkLogin) {
		this.checkLogin = checkLogin;
	}
	
}
