package com.sien.cphonegap.utils.requests;

import android.content.Context;

public abstract class BaseRequestData {
	public String url = "";
	public abstract String requestData(Context context);
}
