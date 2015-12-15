package com.sien.cphonegap.presenter;

import de.greenrobot.event.EventBus;

/**
 * UI管理基类(注册了eventbus事件派发机制)
 * 
 * @author sien
 * 
 * @注
 * 继承自该类的子类，在页面销毁时都需调用父类的destory来取消订阅事件、回收变量
 * 
 */
public class IMBasePresenter extends BasePresenter{

	private boolean eventBusInited = false;

	public IMBasePresenter() {
		super();
		init();
	}

	protected void init() {
		if (!eventBusInited) {
			EventBus.getDefault().register(this);
			eventBusInited = true;
		}
	}

	public void destory() {
		if (updateMessageHander != null) {
			updateMessageHander.removeCallbacksAndMessages(null);
			updateMessageHander = null;
		}

		if (eventBusInited) {
			EventBus.getDefault().unregister(this);
			eventBusInited = false;
		}
		
		remove();
	}

}
