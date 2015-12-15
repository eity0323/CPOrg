package com.sien.cphonegap.view.widgets;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sien.cphonegap.R;

/**
 * 搜索＋menu 搜索view默认显示的是EditText控件，如果需要显示TextView的自己获取控件控制显示和隐藏 默认实现menu弹出
 * 不需要menu自己隐藏 需要menu的话 请设置activity和menuDatas
 * 
 * @author wei.yi
 * 
 */
public class SearchMenueCustom extends LinearLayout implements OnClickListener {

	private EditText searchEt;
	private TextView searchTv;
	private ImageView menueBt;
	private MenuePopupWindow mMenuePopupWindow;
	private OnItemClickListener mOnItemClickListener;
	private Activity activity;
	private List<String> menuDatas;

	public TextView getSearchTv() {
		return searchTv;
	}

	public void setMenuDatas(List<String> menuDatas) {
		this.menuDatas = menuDatas;
		if (mMenuePopupWindow != null) {
			mMenuePopupWindow.setDatas(menuDatas);
		}
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	public EditText getSearchEt() {
		return searchEt;
	}

	public ImageView getMenueBt() {
		return menueBt;
	}

	public SearchMenueCustom(Context context) {
		super(context);
		init(context);
	}

	public SearchMenueCustom(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_searchmenue, this, true);
		searchEt = (EditText) view.findViewById(R.id.searchEt);
		searchTv = (TextView) view.findViewById(R.id.searchTv);
		menueBt = (ImageView) view.findViewById(R.id.menueBt);
		menueBt.setOnClickListener(this);
	}

	/**
	 * 隐藏menu
	 */
	public void dismissMenu() {
		if (mMenuePopupWindow != null && mMenuePopupWindow.isShowing()) {
			mMenuePopupWindow.dismiss();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.menueBt:
			if (null != menuDatas && menuDatas.size() > 0) {
				if (mMenuePopupWindow == null) {
					mMenuePopupWindow = new MenuePopupWindow(SearchMenueCustom.this.getContext(), menuDatas, mOnItemClickListener);
				}
				mMenuePopupWindow.showPopupWindow(menueBt);
			}
			break;

		default:
			break;
		}
	}

	public void setEditHint(String hint) {
		if (null != searchEt) {
			if (TextUtils.isEmpty(hint)) {
				hint = "";
			}
			searchEt.setHint(hint);
		}
	}

	public void setEditTextEnable(boolean enable) {
		if (null != searchEt) {
			searchEt.setEnabled(enable);
		}
	}

}
