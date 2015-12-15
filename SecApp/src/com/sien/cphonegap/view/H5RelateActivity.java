package com.sien.cphonegap.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sien.cphonegap.R;
import com.sien.cphonegap.model.beans.H5RelateBean;
import com.sien.cphonegap.presenter.H5RelatePresenter;
import com.sien.cphonegap.view.adapter.H5RelateAdapter;
import com.sien.cphonegap.view.interfaces.IH5RelateAction;
import com.sien.cphonegap.view.widgets.PullRefreshLayout;
import com.sien.cphonegap.view.widgets.PullRefreshLayout.OnRefreshListener;
import com.sien.cphonegap.view.widgets.SearchMenueCustom;

/**
 * H5相关页面基类
 * 
 * @author sien
 * 
 */
public class H5RelateActivity extends Activity implements IH5RelateAction {

	private final String H5_RELATE_CACHE_ICON = "h5_relate_cache_icon";

	private H5RelatePresenter helper;

	private ListView listview;// 列表
	private PullRefreshLayout pullRefreshLayout; // 下拉刷新对象
	private SearchMenueCustom listHeader;// 列表头
	private H5RelateAdapter adapter;// 适配器

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_view_h5_relate_fragment_listview);
		
		helper = new H5RelatePresenter(this, getPageClassName());
		initView();
	}

	private String getPageClassName(){
		return "CloudCareFragment";
	}

	@Override
	public void onDestroy() {
		if (helper != null)
			helper.destory();

		super.onDestroy();
	}

	private void initView() {
		initLayout();
		initEvents();

		initial();
		
		loadDataList();
	}

	private void initLayout() {
		pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.h5_relate_pullrefresh);
		listview = (ListView) findViewById(R.id.h5_relate_listview);

		listHeader = new SearchMenueCustom(this);
		listHeader.setActivity(this);
		listHeader.getSearchEt().setVisibility(View.GONE);
		listHeader.getSearchTv().setVisibility(View.VISIBLE);
		listHeader.getSearchTv().setText("输入关键字...");
		listHeader.getMenueBt().setVisibility(View.GONE);
		listHeader.setmOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
				int index = position;
				
				if(helper != null){
					H5RelateBean fvo = helper.getMenuDataSource().get(index);
					go2DetailActivity(fvo);
				}
			}
		});

	}

	private void initEvents() {
		listHeader.setOnClickListener(clickListener);
		pullRefreshLayout.setOnRefreshListener(refreshListener);
		listview.setOnItemClickListener(itemClickListener);
	}

	private void initial() {
		if (helper != null) {
			// 将listview传给下拉组件，解决listview上拉与下拉刷新组件手势冲突问题
			pullRefreshLayout.setTargetAbsListView(listview);

			adapter = new H5RelateAdapter(this, helper.getListDataSource());
			listview.addHeaderView(listHeader);
			listview.setAdapter(adapter);
		}
	}

	/* 加载页面数据 */
	protected void loadDataList() {
		if (helper != null) {
			helper.loadDataList(getPageClassName());
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == listHeader) {
				go2SearchActivity();
			}
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			//因为加了一个list header,所以index需要-1
			int index = position -1 ;
			if(index < 0 || index >= adapter.getCount())	return;
			
			H5RelateBean fvo = adapter.getDatasource().get(index);
			// 调整至详情页面
			if (fvo.isCheckLogin()) {
				go2DetailActivity(fvo);
			} else {
				go2DetailActivity(fvo);
			}
		}
	};

	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if (helper != null)
				helper.loadDataList(getPageClassName());
		}
	};

	/* 跳转至详情页面 */
	private void go2DetailActivity(H5RelateBean data) {
		if (data != null) {
			if (TextUtils.isEmpty(data.getLink())) {
				// Toast.makeText(getActivity(), "没有跳转地址",
				// Toast.LENGTH_SHORT).show();
			} else {
				if (data.isInnerLink()) {
					Intent innerIntent = new Intent();
					innerIntent.setClassName(this, data.getLink());
					startActivity(innerIntent);
				} else {
					Intent preWebIntent2 = new Intent(this, H5RelateWebActivity.class);
					preWebIntent2.putExtra("webtitle", data.getTitle());
					preWebIntent2.putExtra("preUrl", "http://www.baidu.com");
					preWebIntent2.putExtra("url", "http://www.baidu.com");
					if("值得看".equals(data.getTitle())){
						preWebIntent2.putExtra("needDefaultTitle", true);
					}
					startActivity(preWebIntent2);
				}
			}
		}
	}

	/** 跳转登录界面 */
	private void go2LoginActivity() {
//		Intent intent = new Intent();
//		intent.setClass(this, LoginActivity.class);
//		startActivity(intent);
	}

	/* 跳转至搜索页面 */
	private void go2SearchActivity() {

	}

	@Override
	public void updateListView(List<H5RelateBean> data) {
		refreshEnd();

		toggleListViewVisible();

		if (data != null) {
			adapter.setDataSource(data);
		}
	}

	@Override
	public void updateMenuView(List<H5RelateBean> data) {
		if (data != null && data.size() > 0) {
			listHeader.getMenueBt().setVisibility(View.VISIBLE);
			
			List<String> list = new ArrayList<String>();
			for (H5RelateBean item : data) {
				list.add(item.getTitle());
			}
			listHeader.setMenuDatas(list);
		}
	}

	/** 切换listview可见性 */
	private void toggleListViewVisible() {
		if (helper == null)
			return;
	}

	/** 结束刷新 */
	private void refreshEnd() {
		pullRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				pullRefreshLayout.setRefreshing(false);
			}
		}, 200);
	}
}
