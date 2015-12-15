package com.sien.cphonegap.model.impl;

import android.content.Context;

import com.sien.cphonegap.control.H5RelateDataManager;
import com.sien.cphonegap.model.interfaces.IH5RelateModel;
import com.sien.cphonegap.utils.requests.BaseRequestData;

/**
 * h5相关数据处理实现类
 * 
 * @author sien
 * 
 */
public class H5RelateModel implements IH5RelateModel {
	
	private Context mcontext;

	public H5RelateModel(Context context, String tag,BaseRequestData request) {
		mcontext = context;
	}

	@Override
	public void getDataByTag(String tag,BaseRequestData request) {
		H5RelateDataManager.getInstance().getDataByTag(mcontext, tag,request);
	}
	
}
