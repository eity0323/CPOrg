package com.sien.cphonegap.utils.phonegap;

import org.apache.cordova.CordovaWebView;

/**
 * html5交互管理类
 * 
 * @author sien
 * 
 */
public class JavaScriptUtils {

	public static void sendJavascript(CordovaWebView webview, String jsMethod) {
		sendJavascripts(webview, jsMethod, new String());
	}

	public static void sendJavascript(final CordovaWebView webview, String jsMethod, String jsonstr) {
		final StringBuffer sb = new StringBuffer(jsMethod);
		sb.append("(");
		if (jsonstr != null) {
			sb.append(jsonstr);
		}
		sb.append(")");
		webview.postDelayed(new Runnable() {
			@Override
			public void run() {
				webview.sendJavascript(sb.toString());
			}
		}, 500);
	}

	public static void sendJavascripts(CordovaWebView webview, String jsMethod, String... params) {
		StringBuffer sb = new StringBuffer(jsMethod);
		sb.append("(");
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				sb.append("'" + params[i] + "'");
				if (i < params.length - 1) {
					sb.append(",");
				}
			}
		}
		sb.append(")");
		webview.sendJavascript(sb.toString());
	}
}
