package com.sien.cporg.presenter;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * UI管理基类
 * 
 * @author sien
 * 
 */
public class BasePresenter {
	protected Context mcontext = null;

	public BasePresenter() {

	}
	
	public void remove(){
		mcontext = null;
	}

	/** 用于当前页面处于激活状态时，及时更新界面 */
	protected void handleMessageFunc(BasePresenter helper, Message msg) {

	}

	/** 用于当前页面处于激活状态时，及时更新界面 */
	protected static class InnerHandler extends Handler {
		private final WeakReference<BasePresenter> helper;

		public InnerHandler(BasePresenter activity) {
			helper = new WeakReference<BasePresenter>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			BasePresenter theActivity = helper.get();
			if (theActivity == null)
				return;

			theActivity.handleMessageFunc(theActivity, msg);
		}
	}

	protected InnerHandler updateMessageHander = null;
}
