package com.sien.cporg.control;

import java.io.InputStream;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cporg.utils.log.NLog;

/**
 * 组织架构基础数据管理
 * 
 * @author sien
 * 
 */
public abstract class BaseOrgManager {

	private String defaultConfigFile = "organize.json"; // 文件名称（v1.2测试环境）
	
	/**解析数据*/
	protected abstract boolean parseLoadData(String jsonStr);
	
	/** 返回结果数据数据 */
	protected abstract void resultDatas();

	protected String getDefaultConfigFile() {
		return defaultConfigFile;
	}

	/* 读取本地json数据 */
	protected void initData(final Context context) {
		Thread temp = new Thread(new Runnable() {

			@Override
			public void run() {
				doLoad(context);

				// 第一次获取数据时读取本地文件并显示数据
				resultDatas();
			}
		});
		temp.start();
	}
	
	/* 加载本地数据 (默认从网络加载，加载失败则获取本地配置)*/
	private synchronized void doLoad(Context context) {
		String jsonStr = readLocalJson(context,getDefaultConfigFile());//readNetJson(context);//

		if (TextUtils.isEmpty(jsonStr)) {
			NLog.d("BaseOrgManager", "BaseOrgManager json string is empty!");
			return;
		}
		
		parseLoadData(jsonStr);
	}

	/* 加载本地json配置文件 */
	private String readLocalJson(Context context, String fileName) {
		String resultString = "";
		try {
			InputStream inputStream = context.getResources().getAssets().open(fileName);
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			resultString = new String(buffer, "utf-8");
		} catch (Exception e) {
			NLog.d("BaseOrgManager", "BaseOrgManager read file failed!");
		}
		return resultString;
	}
	
//	/*加载网络json配置文件*/
//	private String readNetJson(Context context){
//		String result;
//		if (NetworkUtils.isNetworkAvailable(context)) {			//有网络
//			SyncHttpClient httpManager = SyncHttpClient.getInstance(context);
//			try {
//				result = httpManager.get(Constants.H5_CONFIG_URL);
//				if (!TextUtils.isEmpty(result)) {			//网络获取
//					Log4j.debug("--------------------read h5 relate json from network");
//					return result;
//				}
//					
//			} catch (HttpException e) {
//				e.printStackTrace();
//			}
//			return readLocalJson(context,getDefaultConfigFile());	//读默认配置
//		} else {
//			return readLocalJson(context,getDefaultConfigFile());	//读默认配置
//		}
//	}
	
}
