package com.sien.cporg.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sien.cporg.R;
import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.Employee;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.presenter.OrgStructurePresenter;
import com.sien.cporg.view.adapter.MultiChooseOrgAdapter;
import com.sien.cporg.view.interfaces.IOrgStructureAction;
import com.sien.cporg.view.widgets.LoadingDialog;
import com.sien.cporg.view.widgets.PullRefreshLayout;
import com.sien.cporg.view.widgets.PullRefreshLayout.OnRefreshListener;
import com.sien.cporg.view.widgets.TitleHeaderBar;

import de.greenrobot.event.EventBus;

/**
 * 组织架构（checkable false显示模式，true选择模式）
 * 
 * @author sien
 * 
 */
public class OrgStructureActivity extends Activity implements IOrgStructureAction {
	
	public static int NO_CHOOSE_MODE = 0;//非选择模式(默认)
	public static int SINGLE_CHOOSE_MODE = 1;//单选
	public static int MULTI_CHOOSE_MODE = 2;//多选
	private int chooseMode = MULTI_CHOOSE_MODE;//选择模式

	private ListView listview;
	private TitleHeaderBar titleBar;
	private TextView rightBtn;
	private PullRefreshLayout refreshLayout;
	private View listHeader;
	private MultiChooseOrgAdapter adapter;

	private OrgNode curNode; // 当前节点
	private OrgNode rootNode;

	private boolean inited = false;
	private String loginJid;
	private String organiseRootName = "组织架构";
	
	
	private OrgStructurePresenter helper = null;
	private boolean fromCache = true;
	
	protected LoadingDialog loadingDialog;
	
	// 默认存放图片的路径
	public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "CPOrg" + File.separator + "Images"
			+ File.separator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.im_activity_orgstructure);
		
		helper = new OrgStructurePresenter(this);
		fromCache = true;
		
		initImageLoader();
		initView();
	}
	
	/** 初始化imageLoader */
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).build();

		File cacheDir = new File(DEFAULT_SAVE_IMAGE_PATH);
		ImageLoaderConfiguration imageconfig = new ImageLoaderConfiguration.Builder(this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200).diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).defaultDisplayImageOptions(options).build();

		ImageLoader.getInstance().init(imageconfig);
	}

	@Override
	protected void onDestroy() {
		if(helper != null)	helper.destory();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!inited) {
			initial();
			inited = true;
		}
	}

	private void initView() {
		initLayout();
		initEvent();
	}

	private void initLayout() {
		listview = (ListView) findViewById(R.id.im_orgstructure_listview);
		titleBar = (TitleHeaderBar) findViewById(R.id.titleBar);
		rightBtn = titleBar.getRightTextView();
		rightBtn.setMinWidth(100);
		refreshLayout = (PullRefreshLayout) findViewById(R.id.im_orgstructure_swiperefresh_Layout);

		listHeader = getLayoutInflater().inflate(R.layout.im_view_listview_searchbar, null);
	}

	private void initEvent() {
		listHeader.setOnClickListener(clickListener);
		rightBtn.setOnClickListener(clickListener);
		refreshLayout.setOnRefreshListener(refreshListener);
	}

	private void initial() {
//		chooseMode = getIntent().getBooleanExtra("checkable", false);
		
		if(helper != null)	helper.setParams(organiseRootName,chooseMode,"");

		titleBar.setTitleText(organiseRootName);

		if (chooseMode == MULTI_CHOOSE_MODE) { // 选择模式下，需要显示确定选择人数按钮
			rightBtn.setVisibility(View.VISIBLE);

			String str = "确定";
			if(helper != null){
				str += helper.getInitSelectedContactors().size() > 0 ? "(" + helper.getInitSelectedContactors().size() + ")" : "";
			}
			rightBtn.setText(str);
		} else if(chooseMode == NO_CHOOSE_MODE) {
			rightBtn.setVisibility(View.GONE);
		}else if(chooseMode == SINGLE_CHOOSE_MODE){
			//TODO 单选模式
		}else{
			rightBtn.setVisibility(View.GONE);
		}

		if(helper != null){
			rootNode = helper.initTree();
			adapter = new MultiChooseOrgAdapter(this, rootNode);
			// 设置整个树是否显示复选框
//			adapter.setCheckBox(chooseMode);
			// 设置展开和折叠时图标
			adapter.setExpandedCollapsedIcon(R.drawable.im_skin_icon_tree_open, R.drawable.im_skin_icon_tree_close);
			// 设置默认展开级别
			adapter.setExpandLevel(2);
			
//			//选择模式监听点击事件
//			if(chooseMode != NO_CHOOSE_MODE){
//				adapter.setItemCheckedListener(new ItemCheckedListener() {
//					@Override
//					public boolean onClick(String userJid) {
//						updateBtnCount();
//						return memberCheck(userJid);
//					}
//				});
//			}
		}

		listview.addHeaderView(listHeader);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(itemClickListener);

		// 加载组织架构部门数据
		if(helper != null){
			showLoadDialog("加载中...");
			helper.loadDepartmentData(true);
		}
	}

	/** 下拉刷新结束 */
	@Override
	public void refreshEnd() {
		refreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshLayout.setRefreshing(false);
			}
		}, 200);
	}

	/** 刷新选中用户按钮人数 */
	private void updateBtnCount() {
		List<OrgNode> selNodes = caculateSelectedNodes();//adapter.getSeletedNodes();
		if(helper == null)	return;
		
		List<Employee> list = helper.getSelectedContactors(selNodes);

		int count = list.size();

		if (count > 0) {
			String str = "确定" + "(" + count + ")";
			rightBtn.setVisibility(View.VISIBLE);
			rightBtn.setText(str);
		} else {
			rightBtn.setVisibility(View.GONE);
		}
	}

	private OnRefreshListener refreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			if(helper != null){
				fromCache = false;
				helper.loadDepartmentData(false);
			}
			
		}
	};

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(helper == null)	return;
			
			int index = position - 1;//减去listheader的索引数
			//cp.add
			if(index < 0) index = 0;
			
			curNode = (OrgNode) adapter.getItem(index);

			if (curNode.isLeaf()) { // 叶子节点
				if (curNode.getDepId() == null) { // 成员

					Employee vo = curNode.getEmployee();
					if (vo != null) {
						// 选择模式不进入详情页面，显示模式则跳转至详情页面
						if (chooseMode == MULTI_CHOOSE_MODE) {
							if((""+vo.getUserId()).equals(loginJid)){//不能选择自己
								Toast.makeText(OrgStructureActivity.this, "请不要选择自己",Toast.LENGTH_SHORT).show();
								return;
							}
							//已经是成员的用户不能取消
							boolean ismembered = memberCheck(""+vo.getUserId());
							if(ismembered)	return;
							//end
							
							boolean tempstatus = curNode.isChecked();
							curNode.setChecked(!tempstatus);
							
							adapter.notifyDataSetChanged();
							
							updateBtnCount();
							
						} else if(chooseMode == SINGLE_CHOOSE_MODE) {
							//TODO 单选模式
						}else{

							go2ContactorDetailActivity((""+vo.getUserId()), false);
						}
					}
				} else { // 末级部门
					showLoadDialog("加载中...");
					if(helper != null)	helper.loadEmployeesData(curNode,fromCache);
				}
			} else { // 部门
				adapter.ExpandOrCollapse(index);
			}
		}
	};
	
	/**检测用户是否已经为成员*/
	private boolean memberCheck(String memberJid){
		boolean ismembered = false;
		List<Employee> templist = helper.getInitSelectedContactors();
		for(Employee item :templist){
			if(memberJid.equals((""+item.getUserId()))){
				ismembered = true;
				break;
			}
		}
		
		return ismembered;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == listHeader) {
				go2LocalSearchActivity();
			} else if (v == rightBtn) {
				// 获取新的选中用户
				if(helper == null)	return;
				
				List<OrgNode> selNodes = caculateSelectedNodes();//adapter.getSeletedNodes();
				List<Employee> list = helper.getSelectedContactors(selNodes);
				EventBus.getDefault().post(list);

				finish();
			}
		}
	};
	
	private List<OrgNode> caculateSelectedNodes(){
		List<OrgNode> nodes = new ArrayList<OrgNode>();
		caculateNode(nodes,rootNode);
		return nodes;
	}
	
	private void caculateNode(List<OrgNode> target,OrgNode rnode){
		//添加选中的成员
		if (rnode.isLeaf() && rnode.getDepId() == null && rnode.isChecked()) {
			target.add(rnode);
		}
		
		//递归获取选中成员
		List<OrgNode> list = rnode.getChildren();
		if (list != null && list.size() > 0) {
			for (OrgNode item : list) {
				caculateNode(target,item);
			}
		}
	}

	/** 跳转至联系人详情页面 */
	private void go2ContactorDetailActivity(String userJid, boolean isFriend) {
//		Intent t = new Intent();
//		t.setClass(this, ContactorDetailActivity.class);
//		t.putExtra("showPosition", true);
//		t.putExtra("userJid", userJid);
//		t.putExtra("isFriend", false);
//		startActivity(t);
	}

	/** 跳转至本地搜索页面 */
	private void go2LocalSearchActivity() {
//		Intent t = new Intent();
//		t.setClass(this, FilterFriendActvity.class);
//		t.putExtra("searchFrom", "outer");
//		startActivity(t);
	}

	@Override
	public void showToast(String str) {
		Toast.makeText(this, str,Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void hideInnerDialog(){
		hideLoadDialog();
	}

	@Override
	public void updateDepartmentLayout(List<Department> data){
		if (data == null || data.size() <= 0) {
			return;
		}
		if(helper != null){
			//初始化根节点（因为数据中未包含根节点信息，需要客户端生成树节点）
			OrgNode rootNode = helper.generateRoot();
			
			//根据数据初始化树节点
			helper.generatorNode(rootNode,data);
//			for (int i = 0; i < data.size(); i++) {
//				Department depart = data.get(i);
//				rootNode = helper.generateTree(depart);
//			}
	
			if(rootNode != null){
				adapter.setDataSource(rootNode);
				adapter.setExpandLevel(1);
			}
			
			organiseRootName = helper.getOrganiseRootName();
		}

		// 设置组织架构名称
		titleBar.setTitleText(organiseRootName);
	}

	@Override
	public void updateEmployeeLayout(OrgNode organize,OrgNode targetNode){
		rootNode = organize;
		adapter.setDataSource(organize);

		//cp.add 获取数据之后，其他节点收起，只展开指定节点，
		adapter.ExpandOrCollapseNode(targetNode);
	}
	
	protected void showLoadDialog(String message) {
		if (null == loadingDialog) {
			loadingDialog = new LoadingDialog(this);
		}
		loadingDialog.setMessage(message);
		loadingDialog.show();
	}

	protected void hideLoadDialog() {
		if (null != loadingDialog) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}
}
