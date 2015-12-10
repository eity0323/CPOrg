package com.sien.cporg.control;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.request.BaseRequestData;

/**
 * 组织架构基础数据管理
 * 
 * @author sien
 * 
 */
public abstract class BaseOrgManager {
	/**解析数据*/
	protected abstract void parseLoadData(String result);
	
	/** 返回结果数据数据 */
	protected abstract void resultDatas();

	/**
	 *  读取本地json数据 
	 *  */
	protected void initData(final Context context,final BaseRequestData request) {
		Thread temp = new Thread(new Runnable() {

			@Override
			public void run() {
				doLoad(context,request);

				// 返回获取数据
				resultDatas();
			}
		});
		temp.start();
	}
	
	/**
	 *  加载本地数据 (默认从网络加载，加载失败则获取本地配置)
	 *  */
	private synchronized void doLoad(Context context,BaseRequestData request) {
		String result = request.requestData(context);

		if (TextUtils.isEmpty(result)) {
			NLog.d("BaseOrgManager", "BaseOrgManager result string is empty!");
		}
		
		parseLoadData(result);
	}
	
}
