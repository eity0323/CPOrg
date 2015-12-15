package com.sien.cphonegap.view.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sien.cphonegap.R;

/**
 * 弹出的menu
 * @author wei.yi
 *
 */
public class MenuePopupWindow extends PopupWindow{
	private List<String> datas = new ArrayList<String>();
	private MenuAdapter menuadapter;
	private int w ;

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
		if(menuadapter!=null){
			menuadapter.setDatas(datas);
			menuadapter.notifyDataSetChanged();
		}
	}

	public MenuePopupWindow(final Context context, List<String> datas, final OnItemClickListener listener){
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View conentView = inflater.inflate(R.layout.demo_menue_popupwindow, null);
        NoScrollerListView menueLv = (NoScrollerListView) conentView.findViewById(R.id.menuLv);
        menuadapter = new MenuAdapter(context);
        menuadapter.setDatas(datas);
        menueLv.setAdapter(menuadapter);
        menueLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(listener!=null){
					listener.onItemClick(parent, view, position, id);
				}
				dismiss();
			}
		});
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int h = wm.getDefaultDisplay().getHeight();  
        w = wm.getDefaultDisplay().getWidth();  
        
        // 设置SelectPicPopupWindow的View  
        this.setContentView(conentView);  
        // 设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(w / 3);  
        // 设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        // 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw);  
        // 设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimationPreview);  
        
	}
	
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, -this.getWidth()+parent.getWidth(), 0);  
        } else {  
            this.dismiss();  
        }  
    } 
	
	private class MenuAdapter extends BaseAdapter{
		private List<String> datas = new ArrayList<String>();
		private Context mContext;
		public List<String> getDatas() {
			return datas;
		}
		public void setDatas(List<String> datas) {
			if(datas!=null){
				this.datas = datas;
			}
		}

		public MenuAdapter(Context context){
			this.mContext = context;
		}
		
		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.demo_menu_dialog_item_layout, null);
			}
			TextView titleTv = (TextView) convertView.findViewById(R.id.menu_item_title);
			String title = datas.get(position);
			titleTv.setText(title);
			return convertView;
		}
		
	}
}
