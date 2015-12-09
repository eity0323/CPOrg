package com.sien.cporg.utils.request;

import android.content.Context;

public abstract class BaseRequestData {
	public String url = "";
	public abstract String requestData(Context context);
}
