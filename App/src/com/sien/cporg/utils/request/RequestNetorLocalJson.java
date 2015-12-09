package com.sien.cporg.utils.request;

import android.content.Context;
import android.text.TextUtils;

public class RequestNetorLocalJson implements IRequestData {

	@Override
	public String requestData(Context context, String url) {
		IRequestData resn = new RequestNetJson();
		String result = resn.requestData(context, url);
		
		if(TextUtils.isEmpty(result)){
			resn = new RequestLocalJson();
			result = resn.requestData(context, url);
		}
		
		return result;
	}
}
