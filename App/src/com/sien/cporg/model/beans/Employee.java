package com.sien.cporg.model.beans;

import java.sql.Timestamp;


public class Employee extends BaseModel {

	private static final long serialVersionUID = 4323218566352084645L;

	private long departmentId;//部门ID
	private long employeeId; // 员工ID
	private long userId; //用户ID
	private String userName; // 微链号
	private String name; // 用户姓名
	private String nick; // 昵称
	private String mobile; //手机
	private boolean sex;//性别
	private String email;// 邮箱
	private String photo;//个人图像
	private String account;//帐号
	private Timestamp createTime;//创建时间
	private String post;//职位
	private boolean status;//是否在职
	private Timestamp brithday;//出生日期
	private String nation;//民族
	private boolean marriage;//婚姻状况
	private String degree;//学位
	private Timestamp enterTime;//入职时间
	private Timestamp regTime;//转正时间
	private Timestamp dimissionTime;//离职时间
	
	/*inner extend*/
	private boolean isSelected;
	
	public long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Timestamp getBrithday() {
		return brithday;
	}
	public void setBrithday(Timestamp brithday) {
		this.brithday = brithday;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public boolean isMarriage() {
		return marriage;
	}
	public void setMarriage(boolean marriage) {
		this.marriage = marriage;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public Timestamp getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(Timestamp enterTime) {
		this.enterTime = enterTime;
	}
	public Timestamp getRegTime() {
		return regTime;
	}
	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}
	public Timestamp getDimissionTime() {
		return dimissionTime;
	}
	public void setDimissionTime(Timestamp dimissionTime) {
		this.dimissionTime = dimissionTime;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
