package com.sien.cphonegap.utils.images;

import java.io.File;

import com.sien.cphonegap.utils.net.CommonUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * [从本地选择图片以及拍照工具类，完美适配2.0-5.0版本]
 * 
 * @author huxinwu
 * @version 1.0
 * @date 2015-1-7
 * 
 **/
public class PhotoUtils {
	
	private static final String tag = PhotoUtils.class.getSimpleName();
	
	/** 拍照成功后返回 **/ 
	public static final int INTENT_TAKE = 16;
	/** 拍照成功后返回 **/ 
	public static final int INTENT_SELECT = 8;
	
	public static final String CROP_FILE_NAME = "temp.jpg";
	
	private static OnPhotoResultListener onPhotoResultListener;
	
	/**
	 * 构建uri
	 * @param activity
	 * @return
	 */
	private static Uri buildUri(Activity activity) {
		if(CommonUtils.checkSDCard()){
			return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(CROP_FILE_NAME).build();
		}else{
			return Uri.fromFile(activity.getCacheDir()).buildUpon().appendPath(CROP_FILE_NAME).build();
		}
    }
	
	/**
	 * 拍照
	 * @param uri
	 * @return
	 */
	public static void takePicture(Activity activity, OnPhotoResultListener onPhotoResultListener) {
		try {
			PhotoUtils.onPhotoResultListener = onPhotoResultListener;
			
			//每次选择图片吧之前的图片删除
			clearFile(buildUri(activity));
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);  
			intent.putExtra(MediaStore.EXTRA_OUTPUT, buildUri(activity));
			activity.startActivityForResult(intent, INTENT_TAKE);
			
		} catch (Exception e) {
			e.printStackTrace();
			onPhotoResultListener.onPhotoCancel("sorry, fail.");
		}
    }
	 
	/***
	 * 选择一张图片
	 * 图片类型，这里是image/*，当然也可以设置限制
	 * 如：image/jpeg等
	 * @param activity Activity
	 */
	@SuppressLint("InlinedApi")
	public static void selectPicture(Activity activity, OnPhotoResultListener onPhotoResultListener) {
		try {
			PhotoUtils.onPhotoResultListener = onPhotoResultListener;
			
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			activity.startActivityForResult(intent, INTENT_SELECT);
			
		} catch (Exception e) {
			e.printStackTrace();
			onPhotoResultListener.onPhotoCancel("sorry, fail.");
		}
	}
	
	
	/**
	 * 返回结果处理
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		
		if(onPhotoResultListener == null){
			Log.e(tag, "onPhotoResultListener is not null");
			return;
		}
		
		switch (requestCode) {
			case INTENT_TAKE:
				onPhotoResultListener.onPhotoResult(buildUri(activity).getPath());
				break;
				
			case INTENT_SELECT:
				 if (resultCode == Activity.RESULT_OK && null != data){
					 Uri selectedImage = data.getData();
		             String[] filePathColumns = new String[] {MediaStore.Images.Media.DATA};
		             Cursor c = activity.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
		             c.moveToFirst();
		             int columnIndex = c.getColumnIndex(filePathColumns[0]);
		             String mPicturePath = c.getString(columnIndex);
		             c.close();
					onPhotoResultListener.onPhotoResult(mPicturePath);
				}else{
					onPhotoResultListener.onPhotoCancel("sorry, fail.");
				}
				break;
		}
	}
	
	/**
	 * 删除文件
	 * @param uri
	 * @return
	 */
	public static boolean clearFile(Uri uri) {
        if (uri == null){
        	return false;
        }

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result){
            	Log.i(tag, "Cached crop file cleared.");
            }else{
            	Log.e(tag, "Failed to clear cached crop file.");
            }
            return result;
        } else {
        	Log.w(tag, "Trying to clear cached crop file but it does not exist.");
        }
        
        return false;
    }
	 
	/**
	 * [回调监听类]
	 * 
	 * @author huxinwu
	 * @version 1.0
	 * @date 2015-1-7
	 * 
	 **/
	public interface OnPhotoResultListener{
		public void onPhotoResult(String imgpath);
		public void onPhotoCancel(String message);
	}
}
