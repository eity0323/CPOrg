package com.sien.cporg.presenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.Employee;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.model.beans.Organize;
import com.sien.cporg.model.impl.OrgStructureModelEx;
import com.sien.cporg.model.interfaces.IOrgStructureModelEx;
import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.cache.LruCacheManager;
import com.sien.cporg.utils.clone.CloneUtils;
import com.sien.cporg.utils.events.IMAPPEvents;
import com.sien.cporg.utils.events.IMAPPEvents.LoadOrganizeEvent;
import com.sien.cporg.view.OrgStructureActivity;
import com.sien.cporg.view.interfaces.IOrgStructureActionEx;

/**
 * 组织架构管理类
 * 
 * @author sien
 * 
 */
public class OrgStructurePresenterEx extends IMBasePresenter {

	private static final int MSG_UPDATE_DEPARTMENT = 0x0c1;//部门
	private static final int MSG_UPDATE_NODATA = 0x0c2;//没有数据

	private IOrgStructureActionEx impl = null;
	private IOrgStructureModelEx imodel = null;

	private List<Employee> initSelectedContactors = new ArrayList<Employee>();
	private OrgNode organizes; // 组织架构根节点
	private OrgNode targetNode; // 根据指点节点id查到的目标节点
	private OrgNode curNode; // 当前节点

	private String loginJid = "";
	private String sessionId;
	private String organiseRootName = "组织架构";
	private int checkMode = OrgStructureActivity.NO_CHOOSE_MODE;

	public OrgStructurePresenterEx(Context context) {
		super();
		this.mcontext = context;
		impl = (IOrgStructureActionEx) context;

		updateMessageHander = new InnerHandler(this);
		
		imodel = new OrgStructureModelEx();

		initial();
	}

	public OrgNode getOrganizes() {
		return organizes;
	}

	public OrgNode getTargetNode() {
		return targetNode;
	}

	public String getOrganiseRootName() {
		return organiseRootName;
	}

	public List<Employee> getInitSelectedContactors() {
		return initSelectedContactors;
	}

	/** 设置参数 */
	public void setParams(String organiseRootName, int checkMode,String sessionId) {
		this.checkMode = checkMode;
		this.organiseRootName = organiseRootName;
		this.sessionId = sessionId;
	}

	private void initial() {
		// 初始化选中用户
		List<Employee> list = (List<Employee>) LruCacheManager.getInstance().get(Params.CHOOSE_CONTACOTOR_CACHE_KEY);
		if (list != null) {
			initSelectedContactors.clear();
			initSelectedContactors.addAll(list);
		}
	}

	/** 加载部门数据 */
	public void loadDepartmentData(OrgNode node,boolean fromCache) {
		curNode = node;
		String departId = node == null?"":node.getDepId();
		if(imodel != null){
			imodel.getOrganizeData(mcontext, sessionId,departId, loginJid, fromCache);
		}
	}

	public void onEventAsync(LoadOrganizeEvent event) {
		if (event != null) {
			int status = event.getStatus();
			if (status == IMAPPEvents.LoadOrganizeEvent.STATUS_SUCCESS) {
				Organize list = event.getData();

				if (updateMessageHander != null) {
					Message msg = new Message();
					msg.what = MSG_UPDATE_DEPARTMENT;
					msg.obj = list;
					updateMessageHander.sendMessage(msg);
				}
			} else {
				if (updateMessageHander != null) {
					Message msg = new Message();
					msg.what = MSG_UPDATE_NODATA;
					updateMessageHander.sendMessage(msg);
				}
			}
		}
	}

	private void showToast(String str) {
		if (impl == null)
			return;

		impl.showToast(str);
	}

	/** 部门树数据处理 */
	private void departmentHander(Organize data) {
		if (impl == null)
			return;

		impl.refreshEnd();

		if(curNode == null){		//获取一级数据
			impl.updateOrganizeLayout(null,null,data);
		}else{						//获取其他级别数据
			// 查找到员工所属部门的节点
			findParentOrgNode(organizes, curNode.getDepId());

			boolean needCheckSelected = false;
			if (checkMode != OrgStructureActivity.NO_CHOOSE_MODE && initSelectedContactors.size() > 0) { // 选择模式下，且初始选中用户不为空时需要检测用户的初始选中状态
				needCheckSelected = true;
			}

			OrgNode node;
			String name = "";
			//成员
			List<Employee> empData = data.getEmployees();
			for (Employee employee : empData) {
				node = new OrgNode();
				name = "";

				name = employee.getNick();
				if (TextUtils.isEmpty(name)) {
					name = employee.getName();
				}
				if (TextUtils.isEmpty(name)) {
					name = employee.getUserName();
				}

				// 检测用户的初始选中状态
				if (needCheckSelected) {
					node.setChecked(checkContactorSelectStatus(""+employee.getUserId()));
				}

				node.setParent(targetNode);
				node.setEmployee(employee);

				targetNode.add(node);
			}
			
			//部门
			for(Department departItem:data.getDepartment()){
				node = new OrgNode();
				if(!TextUtils.isEmpty(departItem.getName())){
					node.setDepName(departItem.getName());
				}
				
				if(departItem.getDepartmentId() > 0)
				node.setDepId(""+departItem.getDepartmentId());
				node.setParent(targetNode);
				
				Employee depEmployee = new Employee();
				depEmployee.setName(departItem.getName());
				node.setEmployee(depEmployee);
				targetNode.add(node);
			}

			impl.updateOrganizeLayout(organizes, targetNode,null);
		}
	}


	/** 检测用户的初始选中状态 */
	private boolean checkContactorSelectStatus(String userJid) {
		boolean res = false;
		for (Employee cvo : initSelectedContactors) {
			if (userJid.equals(cvo.getUserId())) {
				res = true;
				break;
			}
		}
		return res;
	}

	private void hideInnerDialog() {
		if (impl == null)
			return;

		impl.hideInnerDialog();
	}

	/** 获取新选中的联系人（排除初始选中的用户） */
	public List<Employee> getSelectedContactors(List<OrgNode> selNodes) {
		List<Employee> list = new ArrayList<Employee>();

		if (selNodes != null) {
			Employee cvo;
			for (OrgNode node : selNodes) {
				if (node.isLeaf()) {
					cvo = node.getEmployee();

					if (cvo != null && !(""+cvo.getUserId()).equals(loginJid)) {
						list.add(cvo);
					}
				}
			}
		}
		
		// 删除初始数据中重复的记录
		List<Employee> tempInitDatas;
		try {
			tempInitDatas = (List<Employee>) CloneUtils.deepClone(initSelectedContactors);
		} catch (Exception e) {
			e.printStackTrace();
			tempInitDatas = new ArrayList<Employee>();
			for (Employee outitem : initSelectedContactors) {
				tempInitDatas.add(outitem);
			}
		}
		for (Employee initem : list) {
			for (Employee outitem : tempInitDatas) {
				if ((""+initem.getUserId()).equals((""+outitem.getUserId()))) {
					tempInitDatas.remove(outitem);
					break;
				}
			}
		}

		// 添加初始数据中非重复的记录
		for (Employee outitem : tempInitDatas) {
			list.add(outitem);
		}

		return list;
	}

	/** 查找指定id的节点 */
	private boolean findParentOrgNode(OrgNode pnode, String nodeid) {
		String curnodeid = pnode.getDepId();

		if (curnodeid != null && nodeid.equals(curnodeid)) { // 若为末级节点，则直接返回
			targetNode = pnode;
			return true;
		}

		List<OrgNode> list = pnode.getChildren();
		if (list != null && list.size() > 0) {
			for (OrgNode item : list) {
				boolean res = findParentOrgNode(item, nodeid);
				if (res) {
					return true;
				}
			}
		}

		return false;
	}
	
	/** 根生成树的根节点 */
	public OrgNode generateRoot(){
		OrgNode rootNode = new OrgNode();
		rootNode.setDepId("0");
		if(TextUtils.isEmpty(organiseRootName)){
			organiseRootName = "";
		}
		rootNode.setDepName(organiseRootName);
		
		Employee cvo = new Employee();
		cvo.setName(organiseRootName);
		rootNode.setEmployee(cvo);
		
		organizes = rootNode;
		
		return rootNode;
	}

//	/** 根据数据源生成数据树 */
//	public OrgNode generateTree(Department department) {
//		OrgNode rootNode = new OrgNode();
//		rootNode.setCDptId("0");
//
//		organiseRootName = department.getName();
//		rootNode.setCName(organiseRootName);
//		rootNode.setCDptId(Long.toString(department.getDepartmentId()));
//		ContactorVO cvo = new ContactorVO();
//		cvo.name = organiseRootName;
//		rootNode.setContactor(cvo);
//
//		List<Department> list = department.getSubDptList();
//		if (list != null && list.size() > 0) {
//			generatorNode(rootNode, list);
//		}
//
//		organizes = rootNode;
//		return rootNode;
//	}

	/** 生成树节点 */
	public void generatorNode(OrgNode node, Organize department) {
		OrgNode childNode;
		Employee cvo;
		//成员
		for (Employee item : department.getEmployees()) {
			childNode = new OrgNode();
			childNode.setParent(node);
			childNode.setDepName(item.getName());
			childNode.setExpanded(false);

			cvo = new Employee();
			cvo.setName(item.getName());
			childNode.setEmployee(cvo);

			node.add(childNode);
		}
		
		//部门
		for (Department item : department.getDepartment()) {
			childNode = new OrgNode();
			childNode.setParent(node);
			childNode.setDepName(item.getName());
			childNode.setDepId(Long.toString(item.getDepartmentId()));
			childNode.setExpanded(false);

			cvo = new Employee();
			cvo.setName(item.getName());
			childNode.setEmployee(cvo);

			node.add(childNode);
		}
	}

	/** 初始化树节点 */
	public OrgNode initTree() {
		Employee cvo;

		// 创建根节点
		OrgNode root = new OrgNode();
		root.setDepId("0");
		root.setDepName(organiseRootName);
		cvo = new Employee();
		cvo.setName(organiseRootName);
		root.setEmployee(cvo);

		organizes = root;
		return root;
	}

	@Override
	protected void handleMessageFunc(BasePresenter helper, Message msg) {
		OrgStructurePresenterEx theActivity = (OrgStructurePresenterEx) helper;
		if (theActivity == null)
			return;

		if (msg.what == MSG_UPDATE_DEPARTMENT) {
			Organize list = (Organize) msg.obj;
			if (list != null) {
				theActivity.hideInnerDialog();
				theActivity.departmentHander(list);
			}
		}else if(msg.what == MSG_UPDATE_NODATA){
			theActivity.showToast("请求失败，请稍后再试");
		}
	}

}
