package com.sien.cphonegap.utils.phonegap.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.engine.SystemWebView;


/**
 * h5提供的接口
 * @author sien
 *
 */
public interface IPluginAction {
	/**
	 * 页面返回方法 （js插件方法）
	 * @return
	 */
	public boolean back();
	
	/**
	 * 显示头部（js插件方法）
	 * @param title  h5页面标题名称，当showNativeTitleBar为false时忽略该值，为true时title为空显示tab标题栏，title不为空显示原生标题栏读取h5标题内容
	 * @param showNativeTitleBar 是否显示原生标题栏，true显示原生标题栏，false显示h5标题栏
	 */
	public void showTitleBar(String title, boolean showNativeTitleBar);
	
	/**
	 * 隐藏原生title
	 */
	public void hideTitleBar();
	
	/**获取webview对象*/
	public SystemWebView getSystemWebView();
}
