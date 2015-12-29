package com.sien.cphonegap.view;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sien.cphonegap.R;
import com.sien.cphonegap.utils.images.PhotoUtils;
import com.sien.cphonegap.utils.phonegap.JavaScriptUtils;
import com.sien.cphonegap.utils.phonegap.plugin.IPluginAction;
import com.sien.cphonegap.view.widgets.LoadingDialog;

/**
 * h5页面
 * @author sien
 *
 */
public class H5RelateWebActivity extends Activity implements OnClickListener, IPluginAction{
    private CordovaWebView cordovaWebView;
    private SystemWebView systemWebView;
    public final ArrayBlockingQueue<String> onPageFinishedUrl = new ArrayBlockingQueue<String>(5);
    
    protected LoadingDialog loadingDialog;
	private LinearLayout layout_error;
	private RelativeLayout layout_web_head;
	private ImageButton btn_web_back;
	private TextView title_web_view;
	private TextView tv_error;

	/** 加载url **/
	private String preUrl = "file:///android_asset/www/index.html";

    protected CordovaInterfaceImpl cordovaInterface = new CordovaInterfaceImpl(this) {
        @Override
        public Object onMessage(String id, Object data) {
            if ("onPageFinished".equals(id)) {
                onPageFinishedUrl.add((String) data);
            }
            return super.onMessage(id, data);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_activity_webview_navigation);


        initViews();
    }
    
    private void initViews(){
		layout_web_head = (RelativeLayout) findViewById(R.id.layout_web_head);
		btn_web_back = (ImageButton) findViewById(R.id.btn_web_back);
		btn_web_back.setOnClickListener(this);
		title_web_view = (TextView) findViewById(R.id.title_web_view);

		layout_error = (LinearLayout) findViewById(R.id.layout_error);
		layout_error.setOnClickListener(this);
		tv_error = (TextView) findViewById(R.id.tv_error);
		
		//preUrl = getIntent().getStringExtra("preUrl");
		layout_web_head.setVisibility(View.VISIBLE);
		
		initWebView();
    }
    
    private void initWebView(){
        systemWebView = (SystemWebView)findViewById(R.id.cordova_webview);
        
        String defalutUA = systemWebView.getSettings().getUserAgentString();
        systemWebView.getSettings().setUserAgentString(defalutUA + "Weilian_Android");
        systemWebView.setVerticalScrollBarEnabled(false);
        systemWebView.setOnLongClickListener(new WebView.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
        
        //-------------------------cp.add用于h5定位
  		WebSettings webSettings = systemWebView.getSettings();
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
		
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);//这里会解析res/xml/config.xml配置文件
        SystemWebViewEngine systemEngine = new SystemWebViewEngine(systemWebView);
        cordovaWebView = new CordovaWebViewImpl(systemEngine);//创建一个cordovawebview
        
        SystemWebViewClient systemWebviewClient = new SystemWebViewClient(systemEngine){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		// 解决二级链接不能点击问题
        		return false;
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
				//super.onReceivedError(view, errorCode, description, failingUrl);
			}
        	
        };
        
        SystemWebChromeClient systemWebchromeClient = new SystemWebChromeClient(systemEngine){
        	@Override
        	public void onReceivedTitle(WebView view, String title) {
        		if(!TextUtils.isEmpty(title)){
					if(!TextUtils.isEmpty(title) && title.length() > 10){
						title = title.substring(0,10) + "...";
					}
					innerTitle(title);
 				}
        		super.onReceivedTitle(view, title);
        	}
        	
        	@Override
        	public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        		callback.invoke(origin, true, false);  
        		super.onGeolocationPermissionsShowPrompt(origin, callback);
        	}
        };

        systemWebView.setWebChromeClient(systemWebchromeClient);
        systemWebView.setWebViewClient(systemWebviewClient);
        
        cordovaWebView.init(new CordovaInterfaceImpl(this), parser.getPluginEntries(), parser.getPreferences());//初始化
        
        // 加载url
 		loadPage(preUrl);
    }

    public CordovaWebView getCordovaWebView() {
        return cordovaWebView;
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
			systemWebView.loadUrl(preUrl);
		}
	}

	@Override
	public void showTitleBar(final String title, boolean showFlag) {
		// cp.add 加载网页之后修改标题
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				layout_web_head.setVisibility(View.VISIBLE);
				title_web_view.setText(title);
			}
		});
	}

	@Override
	public boolean back() {
		runOnUiThread(new Runnable() {
			public void run() {
				doReBack();
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
			doReBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**执行返回事件（如果h5页面能返回则返回上一个url，不能返回则退出activity）*/
	private void doReBack(){
		if (systemWebView.canGoBack()) {
			systemWebView.goBack();
		} else {
			finish();
		}
	}
	
	/**java调用js方法*/
	private void doJava2Js(){
		JavaScriptUtils.getInstance().sendCmd("{'action':'showalert','data':'i am java params'}");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_web_back:
			doReBack();
			break;

		case R.id.layout_error:
			layout_error.setVisibility(View.GONE);
			systemWebView.reload();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if(systemWebView != null){
			systemWebView.destroy();
		}
		super.onDestroy();
	}
	
	@Override
	public SystemWebView getSystemWebView() {
		return systemWebView;
	}

	public void setSystemWebView(SystemWebView systemWebView) {
		this.systemWebView = systemWebView;
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
