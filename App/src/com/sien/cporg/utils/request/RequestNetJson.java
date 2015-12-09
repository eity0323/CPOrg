package com.sien.cporg.utils.request;

import java.io.IOException;

import com.sien.cporg.utils.log.NLog;
import com.sien.cporg.utils.net.NetworkUtils;
import com.sien.cporg.utils.net.OkHttpUtil;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.text.TextUtils;

/**
 * 获取网络json数据
 * @author sien
 *
 */
public class RequestNetJson implements IRequestData {

	@Override
	public String requestData(Context context, String url) {
		String result = "";
		if (NetworkUtils.isNetworkAvailable(context)) {			//有网络
			try {
				Request request = new Request.Builder()
	            .url(url)
	            .build();
				Response execute = OkHttpUtil.execute(request);
				result = execute.body().string();
				
				if (!TextUtils.isEmpty(result)) {			//网络获取
					NLog.d("--------------------read json from network");
					return result;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
