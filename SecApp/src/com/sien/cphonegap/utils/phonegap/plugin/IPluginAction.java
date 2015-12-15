package com.sien.cphonegap.utils.phonegap.plugin;

import org.apache.cordova.CordovaWebView;

/**
 * h5提供的接口
 * @author sien
 *
 */
public interface IPluginAction {
	/**返回模式（用于某些页面做特殊处理）*/
	public void setBackModel(String backModel);
	
	public String getBackModel();
	
	public CordovaWebView getAppView();
	
	public void setAppView(CordovaWebView appView);
	
	/**
	 * 返回方法（js插件方法）
	 * @param model 貌似没用到??
	 * @return
	 */
	public boolean back(final String model);
	
	/**
	 * 页面返回方法 （js插件方法）
	 * @return
	 */
	public boolean back();
	
	/**
	 * 设置标题（js插件方法）
	 * @param title, title有值为子页面，显示webActivity的头部；title没值为tab页面，显示tab的头部
	 */
	public void setTitle(final String title);
	
	/**
	 * 显示头部（js插件方法）
	 * @param title  title有值为子页面，显示webActivity的头部；title没值为tab页面，显示tab的头部
	 * @param showFlag true显示普通title，false显示tab栏
	 * @param model 二级栏目类型（便于页面做特殊处理）
	 */
	public void showhead(final String title, final boolean showFlag, final String model);
	
	/**
	 * 隐藏原生title
	 */
	public void hideTitleBar();
}
