package com.sien.cporg.presenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.sien.cporg.model.beans.ContactorVO;
import com.sien.cporg.model.beans.Department;
import com.sien.cporg.model.beans.Employee;
import com.sien.cporg.model.beans.OrgNode;
import com.sien.cporg.model.impl.OrgStructureModel;
import com.sien.cporg.model.interfaces.IOrgStructureModel;
import com.sien.cporg.utils.Params;
import com.sien.cporg.utils.cache.LruCacheManager;
import com.sien.cporg.utils.clone.CloneUtils;
import com.sien.cporg.utils.events.IMAPPEvents;
import com.sien.cporg.utils.events.IMAPPEvents.LoadDepartmentEvent;
import com.sien.cporg.utils.events.IMAPPEvents.LoadEmployeeEvent;
import com.sien.cporg.view.interfaces.IOrgStructureAction;

/**
 * 组织架构管理类
 * 
 * @author sien
 * 
 */
public class OrgStructurePresenter extends IMBasePresenter {

	private static final int MSG_UPDATE_DEPARTMENT = 0x0c1;
	private static final int MSG_UPDATE_CONTACTOR = 0x0c2;

	private IOrgStructureAction impl = null;
	private IOrgStructureModel imodel = null;

	private List<ContactorVO> initSelectedContactors = new ArrayList<ContactorVO>();
	private OrgNode organizes; // 组织架构根节点
	private OrgNode targetNode; // 根据指点节点id查到的目标节点
	private OrgNode curNode; // 当前节点

	private String loginJid = "";
	private String sessionId;
	private String organiseRootName = "组织架构";
	private boolean checkMode = false;

	public OrgStructurePresenter(Context context) {
		super();
		this.mcontext = context;
		impl = (IOrgStructureAction) context;

		updateMessageHander = new InnerHandler(this);
		
		imodel = new OrgStructureModel();

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

	public List<ContactorVO> getInitSelectedContactors() {
		return initSelectedContactors;
	}

	/** 设置参数 */
	public void setParams(String organiseRootName, boolean checkMode,String sessionId) {
		this.checkMode = checkMode;
		this.organiseRootName = organiseRootName;
		this.sessionId = sessionId;
	}

	private void initial() {
		// 初始化选中用户
		List<ContactorVO> list = (List<ContactorVO>) LruCacheManager.getInstance().get(Params.CHOOSE_CONTACOTOR_CACHE_KEY);
		if (list != null) {
			initSelectedContactors.clear();
			initSelectedContactors.addAll(list);
		}
	}

	/** 加载部门数据 */
	public void loadDepartmentData(boolean fromCache) {
		if(imodel != null){
			imodel.getDepartmentData(mcontext, sessionId, loginJid, fromCache);
		}
	}

	/** 加载员工数据 */
	public void loadEmployeesData(OrgNode node, boolean fromCache) {
		curNode = node;
		String departId = node.getCDptId();
		if(imodel != null){
			imodel.getEmployeesData(mcontext, departId, sessionId, loginJid, fromCache);
		}
	}

	public void onEventAsync(LoadDepartmentEvent event) {
		if (event != null) {
			int status = event.getStatus();
			if (status == IMAPPEvents.LoadDepartmentEvent.STATUS_SUCCESS) {
				List<Department> list = event.getData();

				if (updateMessageHander != null) {
					Message msg = new Message();
					msg.what = MSG_UPDATE_DEPARTMENT;
					msg.obj = list;
					updateMessageHander.sendMessage(msg);
				}
			} else {
				showToast("请求失败，请稍后再试");
			}
		}
	}

	public void onEventAsync(LoadEmployeeEvent event) {
		if (event != null) {
			int status = event.getStatus();
			if (status == IMAPPEvents.LoadEmployeeEvent.STATUS_SUCCESS) {
				List<Employee> list = event.getData();

				if (updateMessageHander != null) {
					Message msg = new Message();
					msg.what = MSG_UPDATE_CONTACTOR;
					msg.obj = list;
					updateMessageHander.sendMessage(msg);
				}
			} else {
				showToast("请求失败，请稍后再试");
			}
		}
	}

	private void showToast(String str) {
		if (impl == null)
			return;

		impl.showToast(str);
	}

	/** 部门树数据处理 */
	private void departmentHander(List<Department> data) {
		if (impl == null)
			return;

		impl.refreshEnd();

		impl.updateDepartmentLayout(data);
	}

	/** 员工数据处理 */
	private void contactorHander(List<Employee> empData) {
		if (impl == null)
			return;

		if (empData == null || empData.size() <= 0) {
			return;
		}

		// 查找到员工所属部门的节点
		findParentOrgNode(organizes, curNode.getCDptId());

		boolean needCheckSelected = false;
		if (checkMode && initSelectedContactors.size() > 0) { // 选择模式下，且初始选中用户不为空时需要检测用户的初始选中状态
			needCheckSelected = true;
		}

		OrgNode node;
		ContactorVO item;
		String name = "";
		for (Employee employee : empData) {
			name = "";

			name = employee.getNick();
			if (TextUtils.isEmpty(name)) {
				name = employee.getName();
			}
			if (TextUtils.isEmpty(name)) {
				name = employee.getUserName();
			}

			node = new OrgNode();
			node.setCName(name);
			item = new ContactorVO();
			item.name = name;
			item.userJid = ""+employee.getUserId();
			item.isFriend = false;

			String avatarUrl = employee.getPhoto();
			item.iconUrl = avatarUrl;

			// 检测用户的初始选中状态
			if (needCheckSelected) {
				node.setChecked(checkContactorSelectStatus(item.userJid));
			}
			
			item.account = employee.getUserName();
			item.email = employee.getEmail();

			node.setParent(targetNode);
			node.setContactor(item);

			targetNode.add(node);
		}

		impl.updateEmployeeLayout(organizes, targetNode);
	}

	/** 检测用户的初始选中状态 */
	private boolean checkContactorSelectStatus(String userJid) {
		boolean res = false;
		for (ContactorVO cvo : initSelectedContactors) {
			if (userJid.equals(cvo.userJid)) {
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
	public List<ContactorVO> getSelectedContactors(List<OrgNode> selNodes) {
		List<ContactorVO> list = new ArrayList<ContactorVO>();

		if (selNodes != null) {
			ContactorVO cvo;
			for (OrgNode node : selNodes) {
				if (node.isLeaf()) {
					cvo = node.getContactor();

					if (cvo != null && !cvo.userJid.equals(loginJid)) {
						list.add(cvo);
					}
				}
			}
		}
		
		// 删除初始数据中重复的记录
		List<ContactorVO> tempInitDatas;
		try {
			tempInitDatas = (List<ContactorVO>) CloneUtils.deepClone(initSelectedContactors);
		} catch (Exception e) {
			e.printStackTrace();
			tempInitDatas = new ArrayList<ContactorVO>();
			for (ContactorVO outitem : initSelectedContactors) {
				tempInitDatas.add(outitem);
			}
		}
		for (ContactorVO initem : list) {
			for (ContactorVO outitem : tempInitDatas) {
				if (initem.userJid.equals(outitem.userJid)) {
					tempInitDatas.remove(outitem);
					break;
				}
			}
		}

		// 添加初始数据中非重复的记录
		for (ContactorVO outitem : tempInitDatas) {
			list.add(outitem);
		}

		return list;
	}

	/** 查找指定id的节点 */
	private boolean findParentOrgNode(OrgNode pnode, String nodeid) {
		String curnodeid = pnode.getCDptId();

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
		rootNode.setCDptId("0");
		if(TextUtils.isEmpty(organiseRootName)){
			organiseRootName = "";
		}
		rootNode.setCName(organiseRootName);
		
		ContactorVO cvo = new ContactorVO();
		cvo.name = organiseRootName;
		rootNode.setContactor(cvo);
		
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
	public void generatorNode(OrgNode node, List<Department> department) {
		OrgNode childNode;
		ContactorVO cvo;
		for (Department item : department) {
			childNode = new OrgNode();
			childNode.setParent(node);
			childNode.setCName(item.getName());
			childNode.setCDptId(Long.toString(item.getDepartmentId()));
			childNode.setExpanded(false);

			cvo = new ContactorVO();
			cvo.name = item.getName();
			childNode.setContactor(cvo);

			List<Department> list = item.getSubDptList();
			if (list != null && list.size() > 0) {
				generatorNode(childNode, list);
			}

			node.add(childNode);
		}
	}

	/** 初始化树节点 */
	public OrgNode initTree() {
		ContactorVO cvo;

		// 创建根节点
		OrgNode root = new OrgNode();
		root.setCDptId("0");
		root.setCName(organiseRootName);
		cvo = new ContactorVO();
		cvo.name = organiseRootName;
		root.setContactor(cvo);

		organizes = root;
		return root;
	}

	@Override
	protected void handleMessageFunc(BasePresenter helper, Message msg) {
		OrgStructurePresenter theActivity = (OrgStructurePresenter) helper;
		if (theActivity == null)
			return;

		if (msg.what == MSG_UPDATE_DEPARTMENT) {
			List<Department> list = (List<Department>) msg.obj;
			if (list != null) {
				theActivity.hideInnerDialog();
				theActivity.departmentHander(list);
			}
		} else if (msg.what == MSG_UPDATE_CONTACTOR) {
			List<Employee> list = (List<Employee>) msg.obj;
			if (list != null) {
				theActivity.hideInnerDialog();
				theActivity.contactorHander(list);
			}
		}

	}

}
