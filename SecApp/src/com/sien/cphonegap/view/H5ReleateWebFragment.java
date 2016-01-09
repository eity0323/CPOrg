package com.sien.cphonegap.view;

import java.util.concurrent.ExecutorService;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.apache.cordova.engine.SystemWebViewEngine;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sien.cphonegap.R;
import com.sien.cphonegap.utils.phonegap.JavaScriptUtils;
import com.sien.cphonegap.utils.phonegap.plugin.IPluginAction;
import com.sien.cphonegap.view.widgets.LoadingDialog;

public class H5ReleateWebFragment extends Fragment implements OnClickListener, CordovaInterface, IPluginAction {
	private CordovaWebView cordovaWebView;
	private SystemWebView systemWebView;
	private View rootView;

	protected LoadingDialog loadingDialog;
	private LinearLayout layout_error;
	private RelativeLayout layout_web_head;
	private ImageButton btn_web_back;
	private TextView title_web_view;
	private TextView tv_error;

	private Context mcontext;
	/** 加载url **/
	private String preUrl = "file:///android_asset/www/main.html";//http://vr.weilian.cn";//file:///android_asset/www/index.html";

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mcontext = new CordovaContext(getActivity(), this);
		LayoutInflater localInflater = inflater.cloneInContext(mcontext);
		rootView = localInflater.inflate(R.layout.basic_activity_webview_navigation, container, false);

		initViews();

		return rootView;
	}

	private void initViews() {
		layout_web_head = (RelativeLayout) rootView.findViewById(R.id.layout_web_head);
		btn_web_back = (ImageButton) rootView.findViewById(R.id.btn_web_back);
		btn_web_back.setOnClickListener(this);
		title_web_view = (TextView) rootView.findViewById(R.id.title_web_view);

		layout_error = (LinearLayout) rootView.findViewById(R.id.layout_error);
		layout_error.setOnClickListener(this);
		tv_error = (TextView) rootView.findViewById(R.id.tv_error);

		// preUrl = getIntent().getStringExtra("preUrl");
		layout_web_head.setVisibility(View.VISIBLE);

		initWebView();
	}

	private void initWebView() {
		systemWebView = (SystemWebView) rootView.findViewById(R.id.cordova_webview);
		String defalutUA = systemWebView.getSettings().getUserAgentString();
		systemWebView.getSettings().setUserAgentString(defalutUA + "Weilian_Android");
		systemWebView.setVerticalScrollBarEnabled(false);

		// -------------------------cp.add用于h5定位
		WebSettings webSettings = systemWebView.getSettings();
		// webview支持js脚本
		webSettings.setJavaScriptEnabled(true);
		// 启用数据库
		webSettings.setDatabaseEnabled(true);
		// 设置定位的数据库路径
		String dir = this.getActivity().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setGeolocationDatabasePath(dir);
		// 启用地理定位
		webSettings.setGeolocationEnabled(true);
		// 开启DomStorage缓存
		webSettings.setDomStorageEnabled(true);
		// -----------------------------------------end

		// ----------------------------------------cp.add 用户h5获取cookie
		webSettings.setAllowFileAccess(true);
		// 如果访问的页面中有Javascript，则webview必须设置支持Javascript
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(true);
		// syncCookie(this, preUrl);
		// ------------------------------------------end
		ConfigXmlParser parser = new ConfigXmlParser();
		parser.parse(getActivity());// 这里会解析res/xml/config.xml配置文件
		SystemWebViewEngine systemEngine = new SystemWebViewEngine(systemWebView);
		cordovaWebView = new CordovaWebViewImpl(systemEngine);// 创建一个cordovawebview

		SystemWebViewClient systemWebviewClient = new SystemWebViewClient(systemEngine) {
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
				// super.onReceivedError(view, errorCode, description,
				// failingUrl);
			}

		};

		SystemWebChromeClient systemWebchromeClient = new SystemWebChromeClient(systemEngine) {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (!TextUtils.isEmpty(title)) {
					if (!TextUtils.isEmpty(title) && title.length() > 10) {
						title = title.substring(0, 10) + "...";
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

		cordovaWebView.init(new CordovaInterfaceImpl(getActivity()), parser.getPluginEntries(), parser.getPreferences());// 初始化

		// 加载url
		loadPage(preUrl);
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

		if (!isNetworkConnected(getActivity())) {
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
    public void onDestroyView() {
        if (systemWebView != null) {
        	systemWebView.destroy();
        }  
    	super.onDestroyView();
    }
    
	/**java调用js方法*/
	private void doJava2Js(){
		JavaScriptUtils.getInstance().sendCmd("{'action':'showalert','data':'i am java params'}");
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

	/** 设置内部title */
	private void innerTitle(final String title) {
		title_web_view.setText(title);
	}

	private class CordovaContext extends ContextWrapper implements CordovaInterface {
		CordovaInterface cordova;

		public CordovaContext(Context base, CordovaInterface cordova) {
			super(base);
			this.cordova = cordova;
		}

		public void startActivityForResult(CordovaPlugin command, Intent intent, int requestCode) {
			cordova.startActivityForResult(command, intent, requestCode);
		}

		public void setActivityResultCallback(CordovaPlugin plugin) {
			cordova.setActivityResultCallback(plugin);
		}

		public Activity getActivity() {
			return cordova.getActivity();
		}

		public Object onMessage(String id, Object data) {
			return cordova.onMessage(id, data);
		}

		public ExecutorService getThreadPool() {
			return cordova.getThreadPool();
		}

		@Override
		public boolean hasPermission(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void requestPermission(CordovaPlugin arg0, int arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void requestPermissions(CordovaPlugin arg0, int arg1, String[] arg2) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public ExecutorService getThreadPool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPermission(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object onMessage(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestPermission(CordovaPlugin arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestPermissions(CordovaPlugin arg0, int arg1, String[] arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivityResultCallback(CordovaPlugin arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivityForResult(CordovaPlugin arg0, Intent arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_web_back:
//			doReBack();
			doJava2Js();
			break;

		case R.id.layout_error:
			layout_error.setVisibility(View.GONE);
			systemWebView.reload();
			break;
		}
	}
	
	/**执行返回事件（如果h5页面能返回则返回上一个url，不能返回则退出activity）*/
	private void doReBack(){
		if (systemWebView.canGoBack()) {
			systemWebView.goBack();
		} else {
			getActivity().finish();
		}
	}

	@Override
	public boolean back() {
		doReBack();
		return true;
	}

	@Override
	public void showTitleBar(String title, boolean showNativeTitleBar) {
		layout_web_head.setVisibility(View.VISIBLE);
		title_web_view.setText(title);
	}

	@Override
	public void hideTitleBar() {
		layout_web_head.setVisibility(View.GONE);
	}

	@Override
	public SystemWebView getSystemWebView() {
		return systemWebView;
	}
}
