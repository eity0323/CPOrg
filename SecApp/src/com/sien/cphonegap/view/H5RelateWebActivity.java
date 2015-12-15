package com.sien.cphonegap.view;

import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sien.cphonegap.R;
import com.sien.cphonegap.utils.events.H5RelateEvents.refreshH5RelateEvent;
import com.sien.cphonegap.utils.images.PhotoUtils;
import com.sien.cphonegap.utils.phonegap.JavaScriptUtils;
import com.sien.cphonegap.utils.phonegap.plugin.IPluginAction;
import com.sien.cphonegap.view.widgets.LoadingDialog;

import de.greenrobot.event.EventBus;

/**
 * [h5相关页面跳转]
 * 
 * @author sien
 * @version 1.0
 * 
 **/
public class H5RelateWebActivity extends CordovaActivity implements OnClickListener, IPluginAction {

	protected LoadingDialog loadingDialog;
	private LinearLayout layout_error;
	private RelativeLayout layout_web_head;
	private ImageButton btn_web_back;
	private TextView title_web_view;
	private TextView tv_error;

	/** 特殊处理模块 **/
	private String backModel;
	/** 是否有翌 **/
	private boolean yiVisiable = false;
	/** 加载url **/
	private String preUrl = "file:///android_asset/www/index.html";
	
	private boolean needDefaultTitle = false;//是否需要默认标题（跳转广西环保的页面需要添加）

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_activity_webview_navigation);
		
		needDefaultTitle = getIntent().getBooleanExtra("needDefaultTitle", false);

		layout_web_head = (RelativeLayout) findViewById(R.id.layout_web_head);
		btn_web_back = (ImageButton) findViewById(R.id.btn_web_back);
		btn_web_back.setOnClickListener(this);
		title_web_view = (TextView) findViewById(R.id.title_web_view);

		layout_error = (LinearLayout) findViewById(R.id.layout_error);
		layout_error.setOnClickListener(this);
		tv_error = (TextView) findViewById(R.id.tv_error);
		appView = (CordovaWebView) findViewById(R.id.cordova_webview);
		String defalutUA = appView.getSettings().getUserAgentString();
		appView.getSettings().setUserAgentString(defalutUA + "Weilian_Android");
		appView.setVerticalScrollBarEnabled(false);
		appView.setOnLongClickListener(new WebView.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		
		//-------------------------cp.add用于h5定位
		WebSettings webSettings = appView.getSettings();
		//webview支持js脚本
		webSettings.setJavaScriptEnabled(true);
		//启用数据库  
		webSettings.setDatabaseEnabled(true);    
		//设置定位的数据库路径  
		String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
		webSettings.setGeolocationDatabasePath(dir);   
		//启用地理定位  
		webSettings.setGeolocationEnabled(true);  
		//开启DomStorage缓存
		webSettings.setDomStorageEnabled(true);
		//-----------------------------------------end

		// 通知客户端app加载当前网页时的各种时机状态
		appView.setWebViewClient(new CordovaWebViewClient(this, appView) {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String weburl) {
				return super.shouldOverrideUrlLoading(view, weburl);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				layout_error.setEnabled(true);
				layout_error.setVisibility(View.VISIBLE);
				tv_error.setText("页面加载失败，点击重新加载");
			}
		});

		appView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				
				if(!TextUtils.isEmpty(title)){
//					title_web_view.setText(title);
					
					//cp.add 跳转广西环保的页面，所有标题不抓取页面title，统一设置为“广西环保”
					if(!needDefaultTitle){
						if(!TextUtils.isEmpty(title) && title.length() > 10){
							title = title.substring(0,10) + "...";
						}
						innerTitle(title);
					}else{
						innerTitle("环保资讯");
					}
				}
			}
			
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
				callback.invoke(origin, true, false);  
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});

		// 页面传参
		yiVisiable = getIntent().getBooleanExtra("yiVisiable", false);
		preUrl = getIntent().getStringExtra("preUrl");

		layout_web_head.setVisibility(View.VISIBLE);

		// 加载url
		loadPage(preUrl);
	}
	
	/**设置内部title*/
	private void innerTitle(final String title){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				title_web_view.setText(title);
			}
		});
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	private boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 加载页面
	 * 
	 * @param url
	 */
	private void loadPage(final String url) {
		if (TextUtils.isEmpty(url) || url.indexOf("blank") > -1) {
			return;
		}

		if (!isNetworkConnected(this)) {
			layout_error.setEnabled(false);
			layout_error.setVisibility(View.VISIBLE);
			tv_error.setText("您的网络有问题啦，请检查...");
			return;
		}

		if (!TextUtils.isEmpty(url)) {
			appView.loadUrl(url);
		}
	}

	@Override
	public void showhead(final String title, final boolean showFlag, final String model) {
		// cp.add 加载网页之后修改标题
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				layout_web_head.setVisibility(View.VISIBLE);
				if(!needDefaultTitle){
					title_web_view.setText(title);
				}
			}
		});
	}

	@Override
	public void setTitle(final String title) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				layout_web_head.setVisibility(View.VISIBLE);
				if(!needDefaultTitle){
					title_web_view.setText(title);
				}
			}
		});
	}

	@Override
	public boolean back() {
		return back(null);
	}

	@Override
	public boolean back(final String model) {
		runOnUiThread(new Runnable() {
			public void run() {
				// 正常流程
				if (appView.canGoBack()) {
					appView.goBack();
					
					if(needDefaultTitle){
						innerTitle("环保资讯");
					}
					
//					h5RefreshMethodDelay();
					
				} else {
					finish();
				}
			}
		});
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 选择图片 or 拍照
		PhotoUtils.onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			
			if (appView.canGoBack()) {
				appView.goBack();
				
				if(needDefaultTitle){
					innerTitle("环保资讯");
				}
				
//				h5RefreshMethodDelay();
				return true;
			} else {
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_web_back:
			back();
			break;

		case R.id.layout_error:
			layout_error.setVisibility(View.GONE);
			appView.reload();
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public CordovaWebView getAppView() {
		return appView;
	}

	@Override
	public void setAppView(CordovaWebView appView) {
		this.appView = appView;
	}

	@Override
	public String getBackModel() {
		return backModel;
	}

	@Override
	public void setBackModel(String backModel) {
		this.backModel = backModel;
	}

	@Override
	public void hideTitleBar() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				layout_web_head.setVisibility(View.GONE);
			}
		});
	}
}
