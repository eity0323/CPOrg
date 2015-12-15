package com.sien.cphonegap.utils.requests;

import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

import com.sien.cphonegap.utils.Params;
import com.sien.cphonegap.utils.log.NLog;
import com.sien.cphonegap.utils.net.NetworkUtils;

/**
 * 获取网络json数据
 * @author sien
 *
 */
public class RequestNetJson extends BaseRequestData {
	
	private String defaultUrl = Params.ORG_CONFIG_URL;

	@Override
	public String requestData(Context context) {
		if(TextUtils.isEmpty(url)){
			url = defaultUrl;
		}
		
		String result = "";
//		if (NetworkUtils.isNetworkAvailable(context)) {			//有网络
//			try {
//				Request request = new Request.Builder()
//	            .url(url)
//	            .build();
//				Response execute = OkHttpUtil.execute(request);
//				result = execute.body().string();
//				
//				if (!TextUtils.isEmpty(result)) {			//网络获取
//					NLog.d("--------------------read json from network");
//					return result;
//				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		return result;
	}

}
