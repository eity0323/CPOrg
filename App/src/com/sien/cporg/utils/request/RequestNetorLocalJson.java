package com.sien.cporg.utils.request;

import android.content.Context;
import android.text.TextUtils;

public class RequestNetorLocalJson extends BaseRequestData {

	@Override
	public String requestData(Context context) {
		BaseRequestData resn;
		String result = "";
		
		if(url.indexOf("http://") != -1){
			resn = new RequestNetJson();
			resn.url = url;
			result = resn.requestData(context);
		}
		
		if(TextUtils.isEmpty(result)){
			resn = new RequestLocalJson();
			resn.url = this.url;
			result = resn.requestData(context);
		}
		
		return result;
	}
}
