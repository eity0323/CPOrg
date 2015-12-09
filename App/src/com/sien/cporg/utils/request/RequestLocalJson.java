package com.sien.cporg.utils.request;

import java.io.InputStream;

import com.sien.cporg.utils.log.NLog;

import android.content.Context;

/**
 * 请求本地json数据
 * @author sien
 *
 */
public class RequestLocalJson implements IRequestData {

	@Override
	public String requestData(Context context, String url) {
		String resultString = "";
		try {
			InputStream inputStream = context.getResources().getAssets().open(url);
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			resultString = new String(buffer, "utf-8");
		} catch (Exception e) {
			NLog.d("BaseOrgManager", "BaseOrgManager read file failed!");
		}
		return resultString;
	}

}
