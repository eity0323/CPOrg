package com.sien.cphonegap.view.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sien.cphonegap.R;
import com.sien.cphonegap.model.beans.H5RelateBean;

/**
 * h5相关内容适配器
 * 
 * @author sien
 * 
 */
public class H5RelateAdapter extends BaseAdapter {

	private List<H5RelateBean> datasource;
	private Context mcontext;
	
	private String packname;
	
	private boolean showLarge = false;//翌起玩显示大图片，翌起来显示小图片
	
//	private DisplayImageOptions options;

	public H5RelateAdapter(Context context, List<H5RelateBean> data) {
		this.mcontext = context;
		this.datasource = data;
		packname = mcontext.getPackageName();
		
//		options = new DisplayImageOptions.Builder()
//		.showImageForEmptyUri(R.drawable.im_aio_image_fail_round)//设置图片Uri为空或是错误的时候显示的图片 
//		.showImageOnFail(R.drawable.im_aio_image_fail_round)//设置图片加载/解码过程中错误时候显示的图片
//		.cacheInMemory(true)//设置下载的图片是否缓存在内存中
//		.cacheOnDisk(false)// 设置下载的资源是否缓存在SD卡中
//		.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
//		.build();
	}

	public void setShowLarge(boolean showLarge) {
		this.showLarge = showLarge;
	}

	public List<H5RelateBean> getDatasource() {
		return datasource;
	}

	public void setDataSource(List<H5RelateBean> data) {
		this.datasource = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datasource.size();
	}

	@Override
	public Object getItem(int position) {
		return datasource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			if(showLarge){
				convertView = LayoutInflater.from(mcontext).inflate(R.layout.view_h5_relate_item_layout_listview_large, null);
			}else{
				convertView = LayoutInflater.from(mcontext).inflate(R.layout.view_h5_relate_item_layout_listview, null);
			}
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.h5_relate_item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.h5_relate_item_title);
			holder.content = (TextView) convertView.findViewById(R.id.h5_relate_item_content);
			holder.line = convertView.findViewById(R.id.h5_relate_item_line);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final H5RelateBean items = datasource.get(position);

		holder.title.setText(items.getTitle());
		holder.content.setText(items.getContext());

		//本地数据是读取的本地图片，本地图片缺失，暂时屏蔽图片显示代码
//		String avatar = items.getIcon();
//		int avatarId = R.drawable.im_aio_image_fail_round;
//		if (!TextUtils.isEmpty(avatar) && avatar.indexOf("http") < 0) {
//			avatarId = mcontext.getResources().getIdentifier(avatar, "drawable", packname);
//		}
//		holder.icon.setImageResource(avatarId);

		if (position == this.getCount() - 1) {
			holder.line.setVisibility(View.GONE);
		} else {
			holder.line.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	static class ViewHolder {
		ImageView icon;// 图标
		TextView title;// 标题
		TextView content;// 内容
		View line;	
	}

}
