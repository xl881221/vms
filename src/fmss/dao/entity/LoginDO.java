package fmss.dao.entity;

import java.util.Date;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 上午11:53:28
 * @描述: [LoginDO]登录信息
 */
public class LoginDO {

	private String userId;// 用户编号 USER_ID
	private String userEname;// 用户英文名 USER_ENAME
	private String userCname;// 用户中文名 USER_CNAME
	private String password;// 用户密码 PASSWORD
	private String instId;// 机构编号 U_BASE_INST.INST_ID
	private String instCname;// 机构名称
	private String departId;// 部门编号
	private String tel;// 座机电话 TEL
	private String mobile;// 手机号 MOBILE
	private String address;// 地址 ADDRESS
	private String email;// 邮箱 EMAIL
	private Date lastModifyDate;// 最后修改密码日期 LAST_MODIFY_DATE
	private String isFirstLogin;// 是否为首次登录 IS_FIRST_LOGIN
	private String wrongPwdCount;// 输入密码错误次数 WRONG_PWD_COUNT
	private Date wrongPwdDate;// 最后一次输入密码错误日期 WRONG_PWD_DATE
	private String isUserLocked;// 是否锁定用户 IS_USER_LOCKED
	private String userLockedReson;// 锁定原因 USER_LOCKED_RESON
	private Date startDate;// 启用日期 START_DATE
	private Date endDate;// 终止日期 END_DATE
	private Date createTime;// 创建时间 CREATE_TIME
	private String description;// 描述 DESCRIPTION
	private String enabled;// 启用标识 ENABLED
	private String loginId;// 登录ID
	private String browser;// 浏览器信息
	private String menuId;// 菜单ID
	private String menuName;// 菜单名称
	private String ip; // 登录IP信息
	private String language;
	private String isList;
	private String instIsHead;//是否属于总行
	public String getInstIsHead() {
		return instIsHead;
	}

	public void setInstIsHead(String instIsHead) {
		this.instIsHead = instIsHead;
	}

	/**
	 * <p>
	 * 方法名称: getUserId|描述:取得用户编号
	 * </p>
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * <p>
	 * 方法名称: setUserId|描述:设置用户编号
	 * </p>
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * <p>
	 * 方法名称: getUserEname|描述:取得用户英文名称
	 * </p>
	 * 
	 * @return
	 */
	public String getUserEname() {
		return userEname;
	}

	/**
	 * <p>
	 * 方法名称: setUserEname|描述:设置用户英文名称
	 * </p>
	 * 
	 * @param userEname
	 */
	public void setUserEname(String userEname) {
		this.userEname = userEname;
	}

	/**
	 * <p>
	 * 方法名称: getUserCname|描述:取得用户中文名称
	 * </p>
	 * 
	 * @return
	 */
	public String getUserCname() {
		return userCname;
	}

	/**
	 * <p>
	 * 方法名称: setUserCname|描述:设置用户中文名称
	 * </p>
	 * 
	 * @param userCname
	 */
	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	/**
	 * <p>
	 * 方法名称: getPassword|描述: 取得用户密码信息
	 * </p>
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * <p>
	 * 方法名称: setPassword|描述:设置用户密码信息
	 * </p>
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * <p>
	 * 方法名称: getInstId|描述: 取得用户所属机构编号
	 * </p>
	 * 
	 * @return
	 */
	public String getInstId() {
		return instId;
	}

	/**
	 * <p>
	 * 方法名称: setInstId|描述:设置用户所属机构编号
	 * </p>
	 * 
	 * @param instId
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * 方法名称: getDepartId|描述: 取得用户所在部门信息
	 * </p>
	 * 
	 * @return
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 * <p>
	 * 方法名称: setDepartId|描述:设置用户所在部门信息
	 * </p>
	 * 
	 * @param departId
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}

	/**
	 * <p>
	 * 方法名称: getTel|描述:取得电话号码
	 * </p>
	 * 
	 * @return
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * <p>
	 * 方法名称: setTel|描述:设置电话号码
	 * </p>
	 * 
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * <p>
	 * 方法名称: getMobile|描述:取得手机号码
	 * </p>
	 * 
	 * @return
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * <p>
	 * 方法名称: setMobile|描述:设置手机号码
	 * </p>
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * <p>
	 * 方法名称: getAddress|描述:取得用户地址信息
	 * </p>
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * <p>
	 * 方法名称: setAddress|描述:设置用户地址信息
	 * </p>
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * <p>
	 * 方法名称: getEmail|描述: 取得用户EMAIL信息
	 * </p>
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <p>
	 * 方法名称: setEmail|描述:设置用户EMAIL信息
	 * </p>
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * <p>
	 * 方法名称: getLastModifyDate|描述:取得最后修改日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * <p>
	 * 方法名称: setLastModifyDate|描述:设置最后修改日期
	 * </p>
	 * 
	 * @param lastModifyDate
	 */
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * <p>
	 * 方法名称: getIsFirstLogin|描述:是否为首次登录
	 * </p>
	 * 
	 * @return
	 */
	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	/**
	 * <p>
	 * 方法名称: setIsFirstLogin|描述: 设置为首次登录
	 * </p>
	 * 
	 * @param isFirstLogin
	 */
	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	/**
	 * <p>
	 * 方法名称: getWrongPwdCount|描述:取得密码错误次数
	 * </p>
	 * 
	 * @return
	 */
	public String getWrongPwdCount() {
		return wrongPwdCount;
	}

	/**
	 * <p>
	 * 方法名称: setWrongPwdCount|描述:设置密码错误次数
	 * </p>
	 * 
	 * @param wrongPwdCount
	 */
	public void setWrongPwdCount(String wrongPwdCount) {
		this.wrongPwdCount = wrongPwdCount;
	}

	/**
	 * <p>
	 * 方法名称: getWrongPwdDate|描述:取得最后一次输入密码错误日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getWrongPwdDate() {
		return wrongPwdDate;
	}

	/**
	 * <p>
	 * 方法名称: setWrongPwdDate|描述:设置最后一次输入密码错误日期
	 * </p>
	 * 
	 * @param wrongPwdDate
	 */
	public void setWrongPwdDate(Date wrongPwdDate) {
		this.wrongPwdDate = wrongPwdDate;
	}

	/**
	 * <p>
	 * 方法名称: getIsUserLocked|描述: 是否锁定用户
	 * </p>
	 * 
	 * @return
	 */
	public String getIsUserLocked() {
		return isUserLocked;
	}

	/**
	 * <p>
	 * 方法名称: setIsUserLocked|描述: 锁定用户
	 * </p>
	 * 
	 * @param isUserLocked
	 */
	public void setIsUserLocked(String isUserLocked) {
		this.isUserLocked = isUserLocked;
	}

	/**
	 * <p>
	 * 方法名称: getUserLockedReson|描述:取得锁定用户原因
	 * </p>
	 * 
	 * @return
	 */
	public String getUserLockedReson() {
		return userLockedReson;
	}

	/**
	 * <p>
	 * 方法名称: setUserLockedReson|描述:设置锁定用户原因
	 * </p>
	 * 
	 * @param userLockedReson
	 */
	public void setUserLockedReson(String userLockedReson) {
		this.userLockedReson = userLockedReson;
	}

	/**
	 * <p>
	 * 方法名称: getStartDate|描述: 取得开始日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <p>
	 * 方法名称: setStartDate|描述:设置开始日期
	 * </p>
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * 方法名称: getEndDate|描述:取得结束日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * <p>
	 * 方法名称: setEndDate|描述:设置结束日期
	 * </p>
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>
	 * 方法名称: getCreateTime|描述:取得创建时间
	 * </p>
	 * 
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * <p>
	 * 方法名称: setCreateTime|描述:设置创建时间
	 * </p>
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * <p>
	 * 方法名称: getDescription|描述:取得描述信息
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * 方法名称: setDescription|描述:设置描述信息
	 * </p>
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LoginDO addDescription(String description) {
		this.description = (this.description != null ? this.description : "") + description;
		return this;
	}
	
	public LoginDO addDescription(Object description) {
		this.description = (this.description != null ? this.description : "") + description;
		return this;
	}


	/**
	 * <p>
	 * 方法名称: getEnabled|描述: 是否启用
	 * </p>
	 * 
	 * @return
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * <p>
	 * 方法名称: setEnabled|描述:启用
	 * </p>
	 * 
	 * @param enabled
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/**
	 * <p>
	 * 方法名称: getInstCname|描述: 取得机构中文名称
	 * </p>
	 * 
	 * @return
	 */
	public String getInstCname() {
		return instCname;
	}

	/**
	 * <p>
	 * 方法名称: setInstCname|描述:设置机构中文名称
	 * </p>
	 * 
	 * @param instCname
	 */
	public void setInstCname(String instCname) {
		this.instCname = instCname;
	}

	/**
	 * <p>
	 * 方法名称: getLoginId|描述:取得登录ID
	 * </p>
	 * 
	 * @return
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * <p>
	 * 方法名称: setLoginId|描述: 设置登录ID
	 * </p>
	 * 
	 * @param loginId
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * <p>
	 * 方法名称: getBrowser|描述: 取得浏览器信息
	 * </p>
	 * 
	 * @return
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * <p>
	 * 方法名称: setBrowser|描述:设置浏览器信息
	 * </p>
	 * 
	 * @param browser
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * <p>
	 * 方法名称: getMenuId|描述:取得菜单ID
	 * </p>
	 * 
	 * @return
	 */
	public String getMenuId() {
		return menuId;
	}

	/**
	 * <p>
	 * 方法名称: setMenuId|描述:设置菜单ID
	 * </p>
	 * 
	 * @param menuId
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * <p>
	 * 方法名称: getMenuName|描述:取得菜单名称
	 * </p>
	 * 
	 * @return
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * <p>
	 * 方法名称: setMenuName|描述:设置菜单名称
	 * </p>
	 * 
	 * @param menuName
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * <p>
	 * 方法名称: getIp|描述:取得IP信息
	 * </p>
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * <p>
	 * 方法名称: setIp|描述:设置IP信息
	 * </p>
	 * 
	 * @param ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isSuperUser(String user) {
		return "admin".equals(user);
	}
	
	private boolean ldapLoginType;

	public boolean isLdapLoginType() {
		return ldapLoginType;
	}

	public void setLdapLoginType(boolean ldapLoginType) {
		this.ldapLoginType = ldapLoginType;
	}
	
	public boolean getLdapLoginType(){
		return ldapLoginType ;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}



}
