package com.sien.cporg.utils.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardUtils {

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				View v = activity.getCurrentFocus();
				if(v != null)
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
	}

	/**
	 * 显示软键盘
	 * 
	 * @param activity
	 */
	public static void showKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (!imm.isActive()) {
				imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

}
