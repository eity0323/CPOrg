/*
    ShengDao Android Client, JsonMananger
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.sien.cphonegap.utils.parser;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.sien.cphonegap.utils.log.NLog;

/**
 * [JSON解析管理类]
 * 
 **/
public class JsonMananger {

	private static final String tag = JsonMananger.class.getSimpleName();
	static{
		TypeUtils.compatibleWithJavaBean = true;
	}
	
	/**
	 * 将json字符串转换成java对象
	 * @param json
	 * @param cls
	 * @return
	 * @throws HttpException 
	 */
	public static <T> T jsonToBean(String json, Class<T> cls){
        return JSON.parseObject(json, cls);
	}

	/**
	 * 将json字符串转换成java List对象
	 * @param json
	 * @param cls
	 * @return
	 * @throws HttpException 
	 */
	public static <T> List<T> jsonToList(String json, Class<T> cls){
        return JSON.parseArray(json, cls);
	}
	
	/**
	 * 将bean对象转化成json字符串
	 * @param obj
	 * @return
	 * @throws HttpException 
	 */
	public static String beanToJson(Object obj){
		String result = JSON.toJSONString(obj);
		NLog.e(tag, "beanToJson: " + result);
		return result;
	}
	
}
