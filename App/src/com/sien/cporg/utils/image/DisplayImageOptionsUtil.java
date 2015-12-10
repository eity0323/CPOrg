package com.sien.cporg.utils.image;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sien.cporg.R;

public class DisplayImageOptionsUtil {

	public static DisplayImageOptions getDefaultDisplayIO() {
		return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}


	public static DisplayImageOptions getUserAvatarSmallDisplayIO() {
		return new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.basic_skin_icon_default_avatar_small)
				.showImageOnFail(R.drawable.basic_skin_icon_default_avatar_small).cacheInMemory(true).cacheOnDisk(true).build();
	}
}
