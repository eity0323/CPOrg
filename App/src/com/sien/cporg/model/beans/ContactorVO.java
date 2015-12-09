package com.sien.cporg.model.beans;

import java.io.Serializable;

/**
 * 联系人实体（用于组织架构）
 * 
 * @author sien
 * 
 */
public class ContactorVO implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9138663813290684429L;
	/** 头像 */
	public String iconUrl;
	/** 昵称（当昵称为空时显示真实姓名，都为空时显示微链号） */
	public String name;
	/** 真实姓名 */
	public String extraName;
	/**备注*/
	public String remarkName;
	/** 是否在线 */
	public boolean isOnline = false;
	/** openfire用户名 */
	public String userJid;
	/** 所属用户（当前登录用户） */
	public String owner;
	/** 手机号 */
	public String mobile;
	/** voip账号 */
	public String voipAccount;
	/** 邮箱 */
	public String email;
	/** 微链号 */
	public String account;
	/** 性别（true 男 false 女） */
	public boolean sex;
	/** 是否为好友 */
	public boolean isFriend = false;
	/** 是否选中 */
	public boolean checkable = false;

	/** 所属群组 */
	public String roomJid;
	/** 群组中的权限 */
	public String affiliation;
	
	/**
	 * 个新签名
	 */
	public String  signature;

	public ContactorVO clone() {
		ContactorVO o = null;
		try {
			o = (ContactorVO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
