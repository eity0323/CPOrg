package com.sien.cpshoot;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button button;

	/**
	 * Called when the activity is first created.
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main1);
		this.button = (Button) this.findViewById(R.id.my_button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takeScreenShot1(v);
			}
		});

	}

	private void takeScreenShot1(View v) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINESE);
		String fname = "/sdcard/" + sdf.format(new Date()) + ".png";
		View view = v.getRootView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		if (bitmap != null) {
			System.out.println("bitmap got!");
			try {
				FileOutputStream out = new FileOutputStream(fname);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				System.out.println("file" + fname + "output done.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("bitmap is NULL!");
		}
	}
}
