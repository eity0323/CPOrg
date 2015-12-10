package com.sien.cporg.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.cporg.R;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.image.DisplayImageOptionsUtil;
import com.sien.cporg.view.OrgStructureActivity;
import com.sien.cporg.view.widgets.CircleImageView;

/**
 * 组织架构适配器
 * 
 * @author sien
 * 
 */
public class OrgStructureAdapter extends BaseAdapter {

	private Context con;
	private LayoutInflater lif;
	private List<OrgNode> allsCache = new ArrayList<OrgNode>();
	private List<OrgNode> alls = new ArrayList<OrgNode>();
	private OrgStructureAdapter oThis = this;
	private int hasCheckBox = OrgStructureActivity.NO_CHOOSE_MODE;// 是否拥有复选框
	private int expandedIcon = -1;
	private int collapsedIcon = -1;

	private ItemCheckedListener checkListener;

	/**
	 * TreeAdapter构造函数
	 * 
	 * @param context
	 * @param rootNode
	 *            根节点
	 */
	public OrgStructureAdapter(Context context, OrgNode rootNode) {
		this.con = context;
		this.lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}

	public void setItemCheckedListener(ItemCheckedListener listener) {
		this.checkListener = listener;
	}

	public void setDataSource(OrgNode rootNode) {
		allsCache.clear();
		alls.clear();
		addNode(rootNode);

		notifyDataSetChanged();
	}

	private void addNode(OrgNode node) {
		alls.add(node);
		allsCache.add(node);
		if (node.isLeaf())
			return;
		for (int i = 0; i < node.getChildren().size(); i++) {
			addNode(node.getChildren().get(i));
		}
	}

	// 复选框联动
	private void checkNode(OrgNode node, boolean isChecked) {
		node.setChecked(isChecked);
		for (int i = 0; i < node.getChildren().size(); i++) {
			checkNode(node.getChildren().get(i), isChecked);
		}
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

	// 控制节点的展开和折叠
	private void filterNode() {
		alls.clear();
		for (int i = 0; i < allsCache.size(); i++) {
			OrgNode n = allsCache.get(i);
			if (!n.isParentCollapsed() || n.isRoot()) {
				alls.add(n);
			}
		}
	}

	/**
	 * 设置是否拥有复选框
	 * 
	 * @param hasCheckBox
	 */
	public void setCheckBox(int hasCheckBox) {
		this.hasCheckBox = hasCheckBox;
	}

	/**
	 * 设置展开和折叠状态图标
	 * 
	 * @param expandedIcon
	 *            展开时图标
	 * @param collapsedIcon
	 *            折叠时图标
	 */
	public void setExpandedCollapsedIcon(int expandedIcon, int collapsedIcon) {
		this.expandedIcon = expandedIcon;
		this.collapsedIcon = collapsedIcon;
	}

	/**
	 * 设置展开级别
	 * 
	 * @param level
	 */
	public void setExpandLevel(int level) {
		alls.clear();
		for (int i = 0; i < allsCache.size(); i++) {
			OrgNode n = allsCache.get(i);
			if (n.getLevel() <= level) {
				if (n.getLevel() < level) {// 上层都设置展开状态
					n.setExpanded(true);
				} else {// 最后一层都设置折叠状态
					n.setExpanded(false);
				}
				alls.add(n);
			}
		}
		this.notifyDataSetChanged();
	}

	/**
	 * 控制节点的展开和收缩
	 * 
	 * @param position
	 */
	public void ExpandOrCollapse(int position) {
		OrgNode n = alls.get(position);
		if (n != null) {
			if (!n.isLeaf()) {
				n.setExpanded(!n.isExpanded());
				filterNode();
				this.notifyDataSetChanged();
			}
		}
	}

	/**
	 * cp.add
	 * 收缩其他节点，展开指定节点(因为子节点是动态读取数据的，position设置不准确，需要根据数据重新计算一遍position)
	 * 
	 * @param node
	 */
	public void ExpandOrCollapseNode(OrgNode node) {
		// 遍历关闭所有子节点，展开一层节点（屏蔽----展开当前层级时，不关闭之前的层级）
		// for(int i = 0; i < alls.size(); i++){
		// OrgNode n = alls.get(i);
		// if (n.getLevel() < 1 ) {// 上层都设置展开状态
		// n.setExpanded(true);
		// } else {// 最后一层都设置折叠状态
		// n.setExpanded(false);
		// }
		// filterNode();
		// }

		// 遍历当前节点的父节点，设置展开
		List<OrgNode> subnodes;
		for (int i = 0; i < alls.size(); i++) {
			OrgNode n = alls.get(i);
			subnodes = n.getChildren();
			for (int j = 0; j < subnodes.size(); j++) {
				OrgNode subn = subnodes.get(j);
				if ((TextUtils.isEmpty(subn.getDepId()) || TextUtils.isEmpty(node.getDepId()) || subn.getDepId().equals(node.getDepId()))) {
					if (!TextUtils.isEmpty(subn.getDepName()) && !TextUtils.isEmpty(node.getDepName()) && subn.getDepName().equals(node.getDepName())) {
						n.setExpanded(true);
						filterNode();
						break;
					}
				}
			}
		}

		// 遍历当前节点，设置展开
		for (int i = 0; i < alls.size(); i++) {
			OrgNode n = alls.get(i);
			if (node.getDepName().equals(n.getDepName())
					&& (TextUtils.isEmpty(n.getDepId()) || TextUtils.isEmpty(node.getDepId()) || n.getDepId().equals(node.getDepId()))) {
				n.setExpanded(true);
				filterNode();
				break;
			}
		}

		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return alls.size();
	}

	@Override
	public Object getItem(int position) {
		return alls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = this.lif.inflate(R.layout.im_view_orgstructure_item_ex, null);
			holder = new ViewHolder();
			holder.chbSelect = (ImageView) view.findViewById(R.id.chbSelect);

//			// 复选框单击事件
//			holder.chbSelect.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					OrgNode n = (OrgNode) v.getTag();
//					
//					boolean checkStatus = n.isChecked();
//					String tempjid = "";
//					if (checkListener != null) {
//						ContactorVO cvo = n.getContactor();
//						if (cvo != null) {
//							tempjid = cvo.userJid;
//						}
//
//						boolean status = checkListener.onClick(tempjid);
//						
//						if (status) {
//							checkStatus = true;
//							((ImageView) v).setImageResource(R.drawable.im_skin_icon_checkbox_press);
//						}
//					}
//					
//					checkNode(n, checkStatus);
//					oThis.notifyDataSetChanged();
//				}
//
//			});
			holder.ivIcon = (CircleImageView) view.findViewById(R.id.ivIcon);
			holder.tvText = (TextView) view.findViewById(R.id.tvText);
			holder.ivExEc = (ImageView) view.findViewById(R.id.ivExEc);
			holder.rlLayout = (RelativeLayout) view.findViewById(R.id.rlLayout);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		// 得到当前节点
		final OrgNode n = alls.get(position);

		if (position == 0) { // 根节点
			// 显示文本
			holder.tvText.setText(n.getEmployee().getName());
			holder.tvText.setTextColor(con.getResources().getColor(R.color.im_theme_font_color));

			holder.ivIcon.setVisibility(View.GONE);
			holder.rlLayout.setBackgroundColor(0x00ffffff);

			holder.ivExEc.setVisibility(View.VISIBLE);
			holder.ivExEc.setImageResource(R.drawable.im_skin_icon_tree_icon);

			holder.rlLayout.setPadding(3, 3, 3, 3);
		} else {
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
						if (n.hasCheckBox() && hasCheckBox != OrgStructureActivity.NO_CHOOSE_MODE) {
							holder.chbSelect.setVisibility(View.VISIBLE);
						} else {
							holder.chbSelect.setVisibility(View.GONE);
						}

						// 是叶节点 不显示展开和折叠状态图标
						holder.ivExEc.setVisibility(View.GONE);
						holder.ivIcon.setVisibility(View.VISIBLE);
						holder.rlLayout.setBackgroundColor(0x00eeeeee);

						String avatar = n.getEmployee().getPhoto();
						if(TextUtils.isEmpty(avatar)){
							avatar = "drawable://" + R.drawable.basic_skin_icon_default_avatar_small;
						}else{
							avatar = Params.USER_HEADIMG_ROOT_URL + avatar;
						}
						
						ImageLoader.getInstance().displayImage(avatar, holder.ivIcon, DisplayImageOptionsUtil.getUserAvatarSmallDisplayIO());

					} else { // 部门
						holder.chbSelect.setVisibility(View.GONE);

						holder.rlLayout.setBackgroundColor(0xffeeeeee);
						holder.ivIcon.setVisibility(View.GONE);
						// 单击时控制子节点展开和折叠,状态图标改变
						holder.ivExEc.setVisibility(View.VISIBLE);

						if (collapsedIcon != -1)
							holder.ivExEc.setImageResource(collapsedIcon);
					}

				} else { // 部门
					holder.chbSelect.setVisibility(View.GONE);

					holder.rlLayout.setBackgroundColor(0xffeeeeee);
					holder.ivIcon.setVisibility(View.GONE);
					// 单击时控制子节点展开和折叠,状态图标改变
					holder.ivExEc.setVisibility(View.VISIBLE);
					if (n.isExpanded()) {
						if (expandedIcon != -1)
							holder.ivExEc.setImageResource(expandedIcon);
					} else {
						if (collapsedIcon != -1)
							holder.ivExEc.setImageResource(collapsedIcon);
					}

				}
				// 控制缩进
				// view.setPadding(35*n.getLevel(), 3,3, 3);
				holder.rlLayout.setPadding(35 * n.getLevel(), 3, 3, 3);
			}
		}

		return view;
	}

	/**
	 * 
	 * 列表项控件集合
	 * 
	 */
	public class ViewHolder {
		public ImageView chbSelect;// 选中与否
		private CircleImageView ivIcon;// 图标
		private TextView tvText;// 文本〉〉〉
		private ImageView ivExEc;// 展开或折叠标记">"或"v"
		private RelativeLayout rlLayout;
	}

	public interface ItemCheckedListener {
		public boolean onClick(String userJid);
	}
}
