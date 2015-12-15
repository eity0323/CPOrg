package com.sien.cphonegap.model.interfaces;

import com.sien.cphonegap.utils.requests.BaseRequestData;



/**
 * h5相关数据处理接口
 * 
 * @author sien
 * 
 */
public interface IH5RelateModel {
	/**
	 * 根据标签获取页面数据
	 * @param tag 页面标签（类名）
	 * 
	 * <br>
	 * @注
	 * 需要订阅处理EventBus事件 onEventAsync(loadH5RelateEvent event)
	 * 通过event.getItem()拿到H5RelateData data数据，
	 * 处理数据时需要判断是否为当前页面发出的请求，
	 * 通过对data.getPageTag()与当前页面类名做比较来判断是否为当前页面的数据
	 * */
	public void getDataByTag(String tag,BaseRequestData request);
}
