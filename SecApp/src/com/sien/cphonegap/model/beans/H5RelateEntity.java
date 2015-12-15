package com.sien.cphonegap.model.beans;

import java.io.Serializable;
import java.util.List;

public class H5RelateEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392040840929491913L;
	
	private String itemTitle;// 一级栏目标题
	
	private List<SubEntity> itemList;//二级栏目

	
	public static class SubEntity implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6957184137048795549L;
		
		private String subItemTitle;// 二级栏目标题
		private String pageTag;// 页面名称（用于标识具体页面）
		private boolean showMenu;// 是否显示菜单
		private List<H5RelateBean> menuData;// 页面菜单数据源
		private List<H5RelateBean> subItemList;// 页面列表数据源

		public String getSubItemTitle() {
			return subItemTitle;
		}

		public void setSubItemTitle(String subItemTitle) {
			this.subItemTitle = subItemTitle;
		}

		public String getPageTag() {
			return pageTag;
		}

		public void setPageTag(String pageTag) {
			this.pageTag = pageTag;
		}

		public boolean isShowMenu() {
			return showMenu;
		}

		public void setShowMenu(boolean showMenu) {
			this.showMenu = showMenu;
		}

		public List<H5RelateBean> getMenuData() {
			return menuData;
		}

		public void setMenuData(List<H5RelateBean> menuData) {
			this.menuData = menuData;
		}

		public List<H5RelateBean> getSubItemList() {
			return subItemList;
		}

		public void setSubItemList(List<H5RelateBean> subItemList) {
			this.subItemList = subItemList;
		}		
	}
	//end

	public String getItemTitle() {
		return itemTitle;
	}


	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}


	public List<SubEntity> getItemList() {
		return itemList;
	}


	public void setItemList(List<SubEntity> itemList) {
		this.itemList = itemList;
	}
	
}
