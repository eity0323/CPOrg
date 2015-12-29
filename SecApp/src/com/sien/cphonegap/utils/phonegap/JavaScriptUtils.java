package com.sien.cphonegap.utils.phonegap;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

/**
 * java调用js方法
 * @author sien
 *
 */
public class JavaScriptUtils {
	public static CallbackContext mCallbackContext;
	private static JavaScriptUtils instance;
	
	public static JavaScriptUtils getInstance(){
		if(instance == null){
			synchronized (JavaScriptUtils.class) {
				if(instance == null){
					instance = new JavaScriptUtils();
				}
			}
		}
		return instance;
	}
	
    /**测试java调用js*/
	public void sendCmd(String cmd) {
		if (mCallbackContext != null) {
			PluginResult dataResult = new PluginResult(PluginResult.Status.OK, cmd);
			dataResult.setKeepCallback(true);// 非常重要
			mCallbackContext.sendPluginResult(dataResult);
		}
	}

	public void setCallbackContext(CallbackContext callbackContext) {
		mCallbackContext = callbackContext;
	}
}
