package com.sien.cphonegap.utils.net;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 通用工具类
 * 
 * @author suneee012
 * 
 */
public class CommonUtils {

	/**
	 * 判断SDCard是否存在,并可写
	 * 
	 * @return
	 */
	public static boolean checkSDCard(){
		String  flag = Environment.getExternalStorageState();
		if(android.os.Environment.MEDIA_MOUNTED.equals(flag)){
			return true;
		}
		return false;
	}
	
	/**
	 * 得到唯一设备id
	 * @param context
	 * @return
	 */
	public static String getUUID(Context context){
		StringBuilder uuid = new StringBuilder("Android");
		StringBuilder deviceId = new StringBuilder();
		try {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId.append(telManager.getSubscriberId());
			
			deviceId.append(telManager.getDeviceId());
			
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			deviceId.append(info.getMacAddress());
			
			String androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			deviceId.append(androidID);
			
			uuid.append(MD5Utils.encrypt(deviceId.toString()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return uuid.toString();
	}
	
	/**
	 * 判断当前页面是否处于激活状态
	 * @param context
	 * @param activityClassName
	 * @return
	 */
	public static boolean isTopActivity(Context context, String activityClassName) {
		try {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
			if (tasksInfo.size() > 0) {
				String topActivityName = tasksInfo.get(0).topActivity.getClassName();
				if (topActivityName.contains(activityClassName)) {
					return true;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 验证手机号码是否正确
	 * 仅判断了长度是否11位
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNumber(String mobiles) {
		/*Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		boolean match = m.matches();
		if (!match) {
			if (mobiles.indexOf("00") == 0 || mobiles.indexOf("+86") == 0) {
				match = true;
			}
		}*/
		boolean flag = false;
		if(!TextUtils.isEmpty(mobiles)){
			if(mobiles.trim().length() == 11){
				flag = true;
			}
		}
		return flag;
	}
}
