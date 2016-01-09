package com.sien.cphonegap.view;

import org.apache.cordova.engine.SystemWebView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sien.cphonegap.R;
import com.sien.cphonegap.utils.phonegap.plugin.IPluginAction;

public class ComponentActivity extends FragmentActivity implements IPluginAction{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.component_activity);
		
		initView();
	}
	
	private void initView(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		H5ReleateWebFragment hf = new H5ReleateWebFragment();
		ft.add(R.id.web_fragment, hf);
		ft.commit();
	}

	@Override
	public boolean back() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showTitleBar(String title, boolean showNativeTitleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideTitleBar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SystemWebView getSystemWebView() {
		// TODO Auto-generated method stub
		return null;
	}
}
