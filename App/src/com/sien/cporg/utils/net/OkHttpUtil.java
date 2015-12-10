package com.sien.cporg.utils.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 网络请求工具类
 * @author sien
 *
 */
public class OkHttpUtil {
	private static final OkHttpClient mOkHttpClient = new OkHttpClient();
	private static Handler mDelivery = new Handler(Looper.getMainLooper());
    static{
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }
    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
             
            @Override
            public void onResponse(Response arg0) throws IOException {
                 
            }
             
            @Override
            public void onFailure(Request arg0, IOException arg1) {
                 
            }
        });
    }
    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    private static final String CHARSET_NAME = "UTF-8";
//    /**
//     * 这里使用了HttpClinet的API。只是为了方便
//     * @param params
//     * @return
//     */
//    public static String formatParams(List<BasicNameValuePair> params){
//        return URLEncodedUtils.format(params, CHARSET_NAME);
//    }
//    /**
//     * 为HttpGet 的 url 方便的添加多个name value 参数。
//     * @param url
//     * @param params
//     * @return
//     */
//    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
//        return url + "?" + formatParams(params);
//    }
    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }
    
    
//    //cp.add
//    public static void displayImage(final ImageView view,final String url,final int errorResId) throws IOException
//    {
//    	view.setTag(url);
//    	//cp.add---------加载本地图片
//    	if(url.startsWith("drawable://")){
//    		 setErrorResId(view,url, errorResId);
//    		return;
//    	}
//    	//end------------end
//    	
//    	final Request request = new Request.Builder()
//        .url(url)
//        .build();
//		Call call = mOkHttpClient.newCall(request);
//		call.enqueue(new Callback()
//		{
//		    @Override
//		    public void onFailure(Request request, IOException e)
//		    {
//		        setErrorResId(view,url, errorResId);
//		    }
//		
//		    @Override
//		    public void onResponse(Response response)
//		    {
//		        InputStream is = null;
//		        try
//		        {
//		            is = response.body().byteStream();
//		            ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
//		            ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
//		            int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
//		            try
//		            {
//		                is.reset();
//		            } catch (IOException e)
//		            {
//		                 Request request = new Request.Builder()
//		                .url(url)
//		                .build();
//				        Call call = mOkHttpClient.newCall(request);
//				        response = call.execute();
//		                is = response.body().byteStream();
//		            }
//		
//		            BitmapFactory.Options ops = new BitmapFactory.Options();
//		            ops.inJustDecodeBounds = false;
//		            ops.inSampleSize = inSampleSize;
//		            final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
//		            mDelivery.post(new Runnable()
//		            {
//		                @Override
//		                public void run()
//		                {
//		                	if(view.getTag().equals(url))
//		                		view.setImageBitmap(bm);
//		                }
//		            });
//		        } catch (Exception e)
//		        {
//		            setErrorResId(view,url, errorResId);
//		
//		        } finally
//		        {
//		            if (is != null) try
//		            {
//		                is.close();
//		            } catch (IOException e)
//		            {
//		                e.printStackTrace();
//		            }
//		        }
//		    }
//		});
//    }
//    
//    private static void setErrorResId(final ImageView view,final String url, final int errorResId)
//    {
//        mDelivery.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//            	if(view.getTag().equals(url))
//            		view.setImageResource(errorResId);
//            }
//        });
//    }
}
