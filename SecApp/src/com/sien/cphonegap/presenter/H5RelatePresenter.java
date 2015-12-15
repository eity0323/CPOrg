package com.sien.cphonegap.presenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;

import com.sien.cphonegap.model.beans.H5RelateBean;
import com.sien.cphonegap.model.beans.H5RelateData;
import com.sien.cphonegap.model.impl.H5RelateModel;
import com.sien.cphonegap.model.interfaces.IH5RelateModel;
import com.sien.cphonegap.utils.clone.CloneUtils;
import com.sien.cphonegap.utils.events.H5RelateEvents.loadH5RelateEvent;
import com.sien.cphonegap.utils.requests.BaseRequestData;
import com.sien.cphonegap.utils.requests.RequestLocalJson;
import com.sien.cphonegap.view.interfaces.IH5RelateAction;

/**
 * 视点-空间
 * 
 * @author sien
 * 
 */
public class H5RelatePresenter extends IMBasePresenter {

	private final int LOAD_CONFIG_DATA_CODE = 1001;// 加载配置数据请求码

	private IH5RelateAction impl;// ui接口类
	private IH5RelateModel imodel;// 数据处理接口类

	private String mTag; // 页面tag（类名）

	private List<H5RelateBean> listDataSource = new ArrayList<H5RelateBean>();// 列表数据源
	private List<H5RelateBean> menuDataSource = new ArrayList<H5RelateBean>();// 菜单数据源
	
	private BaseRequestData request = new RequestLocalJson();

	public H5RelatePresenter(Context context, String tag) {
		super();
		mcontext = context;

		impl = (IH5RelateAction) context;
		imodel = new H5RelateModel(mcontext, tag,request);

		mTag = tag;

		updateMessageHander = new InnerHandler(this);
	}

	public List<H5RelateBean> getListDataSource() {
		return listDataSource;
	}

	public List<H5RelateBean> getMenuDataSource() {
		return menuDataSource;
	}

	/* 加载列表数据 */
	public void loadDataList(String className) {
		if (imodel != null) {
			imodel.getDataByTag(mTag,request);
		}
	}

	/* 加载数据成功 */
	public void onEventAsync(loadH5RelateEvent event) {
		if (event != null) {
			Message msg = new Message();
			msg.what = LOAD_CONFIG_DATA_CODE;
			msg.obj = event.getItem();
			updateMessageHander.sendMessage(msg);
		}
	}

	/* 更新界面显示 */
	private void _updateViews(H5RelateData data) {
		if (data == null) {
			// 没有数据时，也要调用页面更新方法，结束下拉刷新动作
			if (impl != null) {
				impl.updateListView(null);
			}
			return;
		}

		// 只能更新当前子类界面，不允许更新其他子类界面
		if (!data.getPageTag().equals(mTag))
			return;

		try {
			cacheDatas(CloneUtils.deepClone(data));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (impl != null) {
			impl.updateListView(data.getListData());

			if (data.isShowMenu()) {
				impl.updateMenuView(data.getMenuData());
			}
		}
	}

	/* 缓存数据 */
	private void cacheDatas(Object obj) {
		H5RelateData data = (H5RelateData) obj;
		if (data != null) {
			if (data.getListData() != null) {
				listDataSource.clear();
				listDataSource.addAll(data.getListData());
			}

			if (data.getMenuData() != null) {
				menuDataSource.clear();
				menuDataSource.addAll(data.getMenuData());
			}
		}
	}

	@Override
	protected void handleMessageFunc(BasePresenter helper, Message msg) {
		super.handleMessageFunc(helper, msg);

		H5RelatePresenter theActivity = (H5RelatePresenter) helper;

		if (theActivity == null)
			return;

		// 更新界面
		if (msg.what == LOAD_CONFIG_DATA_CODE) {
			theActivity._updateViews((H5RelateData) msg.obj);
		}
	}

}
