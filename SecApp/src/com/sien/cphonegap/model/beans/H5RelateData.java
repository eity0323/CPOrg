package com.sien.cphonegap.model.beans;

import java.io.Serializable;
import java.util.List;

/**
 * h5相关页面配置实体
 * 
 * @author sien
 * 
 */
public class H5RelateData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392040840929491913L;

	private String pageTag;// 页面名称（用于标识具体页面）

	private String type;// 一级栏目类型
	private String subType;// 二级栏目类型

	private boolean showMenu; // 是否显示菜单

	private List<H5RelateBean> listData;// 页面列表数据源
	private List<H5RelateBean> menuData;// 页面菜单数据源

	public String getPageTag() {
		return pageTag;
	}

	public void setPageTag(String pageTag) {
		this.pageTag = pageTag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}

	public List<H5RelateBean> getListData() {
		return listData;
	}

	public void setListData(List<H5RelateBean> listData) {
		this.listData = listData;
	}

	public List<H5RelateBean> getMenuData() {
		return menuData;
	}

	public void setMenuData(List<H5RelateBean> menuData) {
		this.menuData = menuData;
	}

}
