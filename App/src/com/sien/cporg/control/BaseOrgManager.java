package com.sien.cporg.control;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.request.IRequestData;

/**
 * 组织架构基础数据管理
 * 
 * @author sien
 * 
 */
public abstract class BaseOrgManager {

	private String defaultConfigFile = Params.ORG_CONFIG_FILE; // 文件名称（v1.2测试环境）
	
	private String defaultUrl = Params.ORG_CONFIG_URL;
	
	/**解析数据*/
	protected abstract void parseLoadData(String jsonStr);
	
	/** 返回结果数据数据 */
	protected abstract void resultDatas();
	

	protected String getDefaultConfigFile() {
		return defaultConfigFile;
	}
	
	protected String getDefaultUrl(){
		return defaultUrl;
	}

	/* 读取本地json数据 */
	protected void initData(final Context context,final IRequestData request) {
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
	
	/* 加载本地数据 (默认从网络加载，加载失败则获取本地配置)*/
	private synchronized void doLoad(Context context,IRequestData request) {
		String jsonStr = request.requestData(context,getDefaultUrl());

		if (TextUtils.isEmpty(jsonStr)) {
			NLog.d("BaseOrgManager", "BaseOrgManager json string is empty!");
		}
		
		parseLoadData(jsonStr);
	}
	
}
