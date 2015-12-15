package com.sien.cphonegap.view.interfaces;

import java.util.List;

import com.sien.cphonegap.model.beans.H5RelateBean;

/**
 * 视点-空间
 * 
 * @author sien
 * 
 */
public interface IH5RelateAction {
	/** 获取页面数据，更新列表 */
	public void updateListView(List<H5RelateBean> data);

	/** 获取页面数据，更新菜单 */
	public void updateMenuView(List<H5RelateBean> data);
}
