package com.sien.cporg.view.widgets;

import com.sien.cporg.R;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * 加载提示框
 * @author sien
 *
 */
public class LoadingDialog extends Dialog {

	private TextView dialogLoadingText;
	private View mImageView;

	public LoadingDialog(Context context) {
		super(context, R.style.progress_style);
		setCancelable(true);
		setContentView(R.layout.layout_loading_panel);
		dialogLoadingText = (TextView) findViewById(R.id.dialog_loading_text);
		mImageView = findViewById(R.id.imageview);
	}

	public LoadingDialog(Context context, int resid) {
		this(context);
		dialogLoadingText.setText(resid);
	}

	public LoadingDialog(Context context, String message) {
		this(context);
		if (!TextUtils.isEmpty(message)) {
			dialogLoadingText.setText(message);
		}
	}

	public final void setMessage(String message) {
		if (!TextUtils.isEmpty(message)) {
			dialogLoadingText.setText(message);
		}
	}

	public final void dismiss() {
		super.dismiss();
		mImageView.clearAnimation();
	}

	public final void show() {
		super.show();
		Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.im_loading);
		mImageView.startAnimation(loadAnimation);
	}

	public void canceable(boolean value) {
		setCancelable(value);
	}

}
