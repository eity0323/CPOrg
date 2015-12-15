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

import com.sien.cporg.R;
import com.sien.cporg.model.beans.OrgNode;

/**
 * 组织架构基类适配器
 * 
 * @author sien
 * 
 */
public abstract class BaseOrgStructureAdapter extends BaseAdapter {

	protected Context con;
	protected LayoutInflater lif;
	
	protected List<OrgNode> allsCache = new ArrayList<OrgNode>();
	private List<OrgNode> alls = new ArrayList<OrgNode>();
	private int expandedIcon = -1;
	private int collapsedIcon = -1;
	
	protected int viewStubResId = R.id.org_content_layout;//viewstub id
	
	//曾经背景色
	private int empItemBgColor = 0xffffffff;//成员背景色
	private int depItemBgColor = 0xffffffff;//部门背景色
	private int rootItemBgColor = 0xffffffff;//根节点背景色
	
	//层级间间距
	private int levelPaddingLeft = 50;//35;
	private int levelPaddingTop=3,levelPaddingBottom=3,levelPaddingRight = 3;

	/**
	 * TreeAdapter构造函数
	 * 
	 * @param context
	 * @param rootNode
	 *            根节点
	 */
	public BaseOrgStructureAdapter(Context context, OrgNode rootNode) {
		this.con = context;
		this.lif = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addNode(rootNode);
	}

	
	public int getEmpItemBgColor() {
		return empItemBgColor;
	}

	public void setEmpItemBgColor(int empItemBgColor) {
		this.empItemBgColor = empItemBgColor;
	}

	public int getDepItemBgColor() {
		return depItemBgColor;
	}

	public void setDepItemBgColor(int depItemBgColor) {
		this.depItemBgColor = depItemBgColor;
	}

	public int getRootItemBgColor() {
		return rootItemBgColor;
	}

	public void setRootItemBgColor(int rootItemBgColor) {
		this.rootItemBgColor = rootItemBgColor;
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
	
	protected abstract View createViewWithViewHolder(int position, View view, ViewGroup parent);
	protected abstract void bindView(int position,BaseViewHolder holder,OrgNode n);

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		BaseViewHolder holder = null;
		if (view == null) {
			view = this.lif.inflate(R.layout.view_base_orgstructure_item, null);
			createViewWithViewHolder(position,view,parent);
			
			holder = (BaseViewHolder) view.getTag();
			holder.execicon = (ImageView) view.findViewById(R.id.org_execicon);
			holder.container = (RelativeLayout) view.findViewById(R.id.org_item_container);
		} else {
			holder = (BaseViewHolder) view.getTag();
		}

		// 得到当前节点
		final OrgNode n = alls.get(position);
		
		// 显示内容
		bindView(position,holder,n);

		// 背景 + 节点展开图标
		if (position == 0) { // 根节点
			holder.execicon.setVisibility(View.VISIBLE);
			holder.execicon.setImageResource(R.drawable.im_skin_icon_tree_icon);
			holder.container.setBackgroundColor(rootItemBgColor);
			holder.container.setPadding(levelPaddingRight, levelPaddingTop, levelPaddingRight, levelPaddingBottom);
		} else {
			if (n != null) {
				if (n.isLeaf()) {
					if (n.getDepId() == null) { // 成员
						// 是叶节点 不显示展开和折叠状态图标
						holder.execicon.setVisibility(View.INVISIBLE);
						holder.container.setBackgroundColor(empItemBgColor);
						
					} else { // 部门
						holder.container.setBackgroundColor(depItemBgColor);
						// 单击时控制子节点展开和折叠,状态图标改变
						holder.execicon.setVisibility(View.VISIBLE);
						if (collapsedIcon != -1)
							holder.execicon.setImageResource(collapsedIcon);
					}
				} else { // 部门
					holder.container.setBackgroundColor(depItemBgColor);
					// 单击时控制子节点展开和折叠,状态图标改变
					holder.execicon.setVisibility(View.VISIBLE);
					if (n.isExpanded()) {
						if (expandedIcon != -1)
							holder.execicon.setImageResource(expandedIcon);
					} else {
						if (collapsedIcon != -1)
							holder.execicon.setImageResource(collapsedIcon);
					}
				}
				// 控制缩进
				holder.container.setPadding(levelPaddingLeft * n.getLevel(), levelPaddingTop, levelPaddingRight, levelPaddingBottom);
			}
		}
		return view;
	}

	/**
	 * 
	 * 列表项控件集合
	 * 
	 */
	public class BaseViewHolder {
		public ImageView execicon;// 展开或折叠标记">"或"v"
		public RelativeLayout container;
	}
}
