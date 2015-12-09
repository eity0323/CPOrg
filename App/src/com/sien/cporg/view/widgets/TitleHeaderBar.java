package com.sien.cporg.view.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sien.cporg.R;
import com.sien.cporg.utils.keyboard.SoftKeyboardUtils;

/**
 * 普通标题头部的实现： 左侧返回 中部标题 右侧文字
 * 
 * tip: 默认是显示左边返回键并实现了返回事件。  如果想隐藏返回键，请调用enableBackKey(false). 
 * 如果要更换左边按钮的图标或者文字，请用getLeftTextView获取Textview来设置图片和文字
 * 设置图片使用setLeftViewImageRes(); 不是用图片使用mLeftTextView.setCompoundDrawables(drawable, null, null, null);
 * @author wei.yi 2015.05.02
 */
public class TitleHeaderBar extends RelativeLayout {

	private TextView mTitleTextView;
	private TextView mRightTextView;
	private TextView mLeftTextView;

	public TitleHeaderBar(Context context) {
		this(context, null);
	}

	public TitleHeaderBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleHeaderBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(getLayoutId(), this);
		mLeftTextView = (TextView) findViewById(R.id.tv_title_bar_left);
		mTitleTextView = (TextView) findViewById(R.id.tv_title_bar_title);
		mRightTextView = (TextView) findViewById(R.id.tv_title_bar_right);
		enableBackKey(true);
	}

	protected int getLayoutId() {
		return R.layout.base_header_bar_title;
	}

	public TextView getLeftTextView() {
		return mLeftTextView;
	}

	public TextView getTitleTextView() {
		return mTitleTextView;
	}

	public TextView getRightTextView() {
		return mRightTextView;
	}

	public void setTitleText(String titleTxt) {
		if (titleTxt != null)
			mTitleTextView.setText(titleTxt.trim());
	}

	public CharSequence getText() {
		if (null != mTitleTextView) {
			return mTitleTextView.getText();
		}
		return "";
	}

	public void setLeftViewImageRes(int res) {
		Drawable drawable = getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mLeftTextView.setCompoundDrawables(drawable, null, null, null);
	}

	public void setRightViewImageRes(int res) {
		mRightTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0);
	}

	/** 设置是否使用返回键， true 使用， false 不使用 */
	public void enableBackKey(boolean enable) {
		if (enable) {
			findViewById(R.id.ly_title_bar_left).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(getContext() != null){
						SoftKeyboardUtils.hideKeyboard((Activity)getContext());
						((Activity) getContext()).onBackPressed();
					}
				}
			});
		} else {
			findViewById(R.id.ly_title_bar_left).setVisibility(GONE);
		}
	}

	public void setRightOnClickListener(OnClickListener l) {
		findViewById(R.id.ly_title_bar_right).setOnClickListener(l);
	}

	public void setLeftOnClickListener(OnClickListener l) {
		findViewById(R.id.ly_title_bar_left).setOnClickListener(l);
	}

	public void setBackgroundColor(int colorId){
		findViewById(R.id.rl_title).setBackgroundColor(colorId);
	}

}