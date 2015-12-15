package com.sien.cporg.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.cporg.R;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.image.DisplayImageOptionsUtil;
import com.sien.cporg.view.widgets.CircleImageView;

/**
 * 多选组织架构适配器
 * @author sien
 *
 */
public class MultiChooseOrgAdapter extends BaseOrgStructureAdapter {

	public MultiChooseOrgAdapter(Context context, OrgNode rootNode) {
		super(context, rootNode);
	}
	
	/**
	 * 获得选中节点
	 * 
	 * @return
	 */
	public List<OrgNode> getSeletedNodes() {
		List<OrgNode> nodes = new ArrayList<OrgNode>();
		for (int i = 0; i < allsCache.size(); i++) {
			OrgNode n = allsCache.get(i);
			if (n.isChecked()) {
				nodes.add(n);
			}
		}
		return nodes;
	}

	@Override
	protected View createViewWithViewHolder(int position, View view, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if(view.findViewById(R.id.ivIcon) == null){
			ViewStub vs = (ViewStub) view.findViewById(viewStubResId);
			vs.setLayoutResource(R.layout.view_multi_choose_org_item);
			View tempv = vs.inflate();
			
			holder.ivIcon = (CircleImageView) tempv.findViewById(R.id.ivIcon);
			holder.tvText = (TextView) tempv.findViewById(R.id.tvText);
			holder.chbSelect = (ImageView)tempv.findViewById(R.id.chbSelect);
		}
		
		view.setTag(holder);
		return view;
	}

	@Override
	protected void bindView(int position, BaseViewHolder mholder, OrgNode n) {
		ViewHolder holder = (ViewHolder)mholder;
		if (position == 0) { // 根节点
			// 显示文本
			holder.tvText.setText(n.getEmployee().getName());
			holder.tvText.setTextColor(con.getResources().getColor(R.color.im_theme_font_color));

			holder.ivIcon.setVisibility(View.GONE);
		}else{
			if (n != null) {
				holder.chbSelect.setTag(n);
				if(n.isChecked()){
					holder.chbSelect.setImageResource(R.drawable.im_skin_icon_checkbox_press);
				}else{
					holder.chbSelect.setImageResource(R.drawable.im_skin_icon_checkbox_normal);
				}
				
				// 显示文本
				holder.tvText.setText(n.getEmployee().getName());
				holder.tvText.setTextColor(con.getResources().getColor(R.color.im_theme_font_sub_color));
				
				if (n.isLeaf()) {
					if (n.getDepId() == null) { // 成员
						// 是否显示复选框
						if (n.hasCheckBox()) {
							holder.chbSelect.setVisibility(View.VISIBLE);
						} else {
							holder.chbSelect.setVisibility(View.GONE);
						}
						
						// 是叶节点 不显示展开和折叠状态图标
						holder.ivIcon.setVisibility(View.VISIBLE);
						String avatar = n.getEmployee().getPhoto();
						if(TextUtils.isEmpty(avatar)){
							avatar = "drawable://" + R.drawable.basic_skin_icon_default_avatar_small;
						}else{
							avatar = Params.USER_HEADIMG_ROOT_URL + avatar;
						}
						ImageLoader.getInstance().displayImage(avatar, holder.ivIcon, DisplayImageOptionsUtil.getUserAvatarSmallDisplayIO());
					}else{						//部门
						holder.chbSelect.setVisibility(View.GONE);
						holder.ivIcon.setVisibility(View.GONE);
					}
				}else{
					holder.chbSelect.setVisibility(View.GONE);
					holder.ivIcon.setVisibility(View.GONE);
				}
			}
		}
	}

	/**
	 * 
	 * 列表项控件集合
	 * 
	 */
	public class ViewHolder extends BaseViewHolder {
		private ImageView chbSelect;// 选中与否
		private CircleImageView ivIcon;// 图标
		private TextView tvText;// 文本〉〉〉
	}
}
