package com.sien.cphonegap.utils.phonegap.plugin;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.sien.cphonegap.utils.log.NLog;
import com.sien.cphonegap.utils.parser.JsonMananger;

/**
 * 自定义插件
 * 
 * @author sien
 * 
 */
public class CPlugin extends CordovaPlugin {

	private final String tag = CPlugin.class.getSimpleName();

	// cp.modify
	private IPluginAction impl;
	private Context mcontext;

	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

		impl = (IPluginAction) cordova.getActivity();
		mcontext = cordova.getActivity();

		NLog.e("CPlugin", "action:" + action + "args:" + args.toString());

		// web页面跳转
		if ("callPhone".equals(action)) {
			String phone = args.getString(0);
			if (!TextUtils.isEmpty(phone)) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(phone));
				mcontext.startActivity(intent);
			}
			return true;
		}

		// 设置标题
		if ("setTitle".equals(action)) {
			String title = args.getString(0);
			impl.setTitle(title);
			return true;
		}

		// 显示头部
		if ("showhead".equals(action)) {
			String title = args.getString(0);
			boolean showFlag = args.getBoolean(1);
			String model = args.getString(2);
			impl.showhead(title, showFlag, model);
			return true;
		}

		// 获取经纬度
		if ("getLocation".equals(action)) {
			try {
				Map<String, String> loc = new HashMap<String, String>();
				loc.put("lat", "22.8007953997");
				loc.put("lon", "108.2906566832");
				loc.put("city", "南宁市");
				callbackContext.success(JsonMananger.beanToJson(loc));
			} catch (Exception e) {
				e.printStackTrace();
				callbackContext.error("");
			}
			return true;
		}

		// 返回方法
		if ("back".equals(action)) {
			String model = args.getString(0);
			NLog.e(tag, model);
			impl.back(model);
			return true;
		}

		// 隐藏键盘
		if ("hideSoftInput".equals(action)) {
			try {
				InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(impl.getAppView().getWindowToken(), 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		// 系统浏览器
		if ("openBrowser".equals(action)) {
			String url = args.getString(0);
			if (!TextUtils.isEmpty(url)) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(url);
				intent.setData(content_url);
				// intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
				mcontext.startActivity(intent);
			}
			return true;
		}

		// 隐藏标题
		if ("hideTitleBar".equals(action)) {
			impl.hideTitleBar();
			return true;
		}

		return false;
	}
}
