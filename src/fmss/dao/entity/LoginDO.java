package fmss.dao.entity;

import java.util.Date;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����11:53:28
 * @����: [LoginDO]��¼��Ϣ
 */
public class LoginDO {

	private String userId;// �û���� USER_ID
	private String userEname;// �û�Ӣ���� USER_ENAME
	private String userCname;// �û������� USER_CNAME
	private String password;// �û����� PASSWORD
	private String instId;// ������� U_BASE_INST.INST_ID
	private String instCname;// ��������
	private String departId;// ���ű��
	private String tel;// �����绰 TEL
	private String mobile;// �ֻ��� MOBILE
	private String address;// ��ַ ADDRESS
	private String email;// ���� EMAIL
	private Date lastModifyDate;// ����޸��������� LAST_MODIFY_DATE
	private String isFirstLogin;// �Ƿ�Ϊ�״ε�¼ IS_FIRST_LOGIN
	private String wrongPwdCount;// �������������� WRONG_PWD_COUNT
	private Date wrongPwdDate;// ���һ����������������� WRONG_PWD_DATE
	private String isUserLocked;// �Ƿ������û� IS_USER_LOCKED
	private String userLockedReson;// ����ԭ�� USER_LOCKED_RESON
	private Date startDate;// �������� START_DATE
	private Date endDate;// ��ֹ���� END_DATE
	private Date createTime;// ����ʱ�� CREATE_TIME
	private String description;// ���� DESCRIPTION
	private String enabled;// ���ñ�ʶ ENABLED
	private String loginId;// ��¼ID
	private String browser;// �������Ϣ
	private String menuId;// �˵�ID
	private String menuName;// �˵�����
	private String ip; // ��¼IP��Ϣ
	private String language;
	private String isList;
	private String instIsHead;//�Ƿ���������
	public String getInstIsHead() {
		return instIsHead;
	}

	public void setInstIsHead(String instIsHead) {
		this.instIsHead = instIsHead;
	}

	/**
	 * <p>
	 * ��������: getUserId|����:ȡ���û����
	 * </p>
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * <p>
	 * ��������: setUserId|����:�����û����
	 * </p>
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * <p>
	 * ��������: getUserEname|����:ȡ���û�Ӣ������
	 * </p>
	 * 
	 * @return
	 */
	public String getUserEname() {
		return userEname;
	}

	/**
	 * <p>
	 * ��������: setUserEname|����:�����û�Ӣ������
	 * </p>
	 * 
	 * @param userEname
	 */
	public void setUserEname(String userEname) {
		this.userEname = userEname;
	}

	/**
	 * <p>
	 * ��������: getUserCname|����:ȡ���û���������
	 * </p>
	 * 
	 * @return
	 */
	public String getUserCname() {
		return userCname;
	}

	/**
	 * <p>
	 * ��������: setUserCname|����:�����û���������
	 * </p>
	 * 
	 * @param userCname
	 */
	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	/**
	 * <p>
	 * ��������: getPassword|����: ȡ���û�������Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * <p>
	 * ��������: setPassword|����:�����û�������Ϣ
	 * </p>
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * <p>
	 * ��������: getInstId|����: ȡ���û������������
	 * </p>
	 * 
	 * @return
	 */
	public String getInstId() {
		return instId;
	}

	/**
	 * <p>
	 * ��������: setInstId|����:�����û������������
	 * </p>
	 * 
	 * @param instId
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * ��������: getDepartId|����: ȡ���û����ڲ�����Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 * <p>
	 * ��������: setDepartId|����:�����û����ڲ�����Ϣ
	 * </p>
	 * 
	 * @param departId
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}

	/**
	 * <p>
	 * ��������: getTel|����:ȡ�õ绰����
	 * </p>
	 * 
	 * @return
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * <p>
	 * ��������: setTel|����:���õ绰����
	 * </p>
	 * 
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * <p>
	 * ��������: getMobile|����:ȡ���ֻ�����
	 * </p>
	 * 
	 * @return
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * <p>
	 * ��������: setMobile|����:�����ֻ�����
	 * </p>
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * <p>
	 * ��������: getAddress|����:ȡ���û���ַ��Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * <p>
	 * ��������: setAddress|����:�����û���ַ��Ϣ
	 * </p>
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * <p>
	 * ��������: getEmail|����: ȡ���û�EMAIL��Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <p>
	 * ��������: setEmail|����:�����û�EMAIL��Ϣ
	 * </p>
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * <p>
	 * ��������: getLastModifyDate|����:ȡ������޸�����
	 * </p>
	 * 
	 * @return
	 */
	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * <p>
	 * ��������: setLastModifyDate|����:��������޸�����
	 * </p>
	 * 
	 * @param lastModifyDate
	 */
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * <p>
	 * ��������: getIsFirstLogin|����:�Ƿ�Ϊ�״ε�¼
	 * </p>
	 * 
	 * @return
	 */
	public String getIsFirstLogin() {
		return isFirstLogin;
	}

	/**
	 * <p>
	 * ��������: setIsFirstLogin|����: ����Ϊ�״ε�¼
	 * </p>
	 * 
	 * @param isFirstLogin
	 */
	public void setIsFirstLogin(String isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	/**
	 * <p>
	 * ��������: getWrongPwdCount|����:ȡ������������
	 * </p>
	 * 
	 * @return
	 */
	public String getWrongPwdCount() {
		return wrongPwdCount;
	}

	/**
	 * <p>
	 * ��������: setWrongPwdCount|����:��������������
	 * </p>
	 * 
	 * @param wrongPwdCount
	 */
	public void setWrongPwdCount(String wrongPwdCount) {
		this.wrongPwdCount = wrongPwdCount;
	}

	/**
	 * <p>
	 * ��������: getWrongPwdDate|����:ȡ�����һ�����������������
	 * </p>
	 * 
	 * @return
	 */
	public Date getWrongPwdDate() {
		return wrongPwdDate;
	}

	/**
	 * <p>
	 * ��������: setWrongPwdDate|����:�������һ�����������������
	 * </p>
	 * 
	 * @param wrongPwdDate
	 */
	public void setWrongPwdDate(Date wrongPwdDate) {
		this.wrongPwdDate = wrongPwdDate;
	}

	/**
	 * <p>
	 * ��������: getIsUserLocked|����: �Ƿ������û�
	 * </p>
	 * 
	 * @return
	 */
	public String getIsUserLocked() {
		return isUserLocked;
	}

	/**
	 * <p>
	 * ��������: setIsUserLocked|����: �����û�
	 * </p>
	 * 
	 * @param isUserLocked
	 */
	public void setIsUserLocked(String isUserLocked) {
		this.isUserLocked = isUserLocked;
	}

	/**
	 * <p>
	 * ��������: getUserLockedReson|����:ȡ�������û�ԭ��
	 * </p>
	 * 
	 * @return
	 */
	public String getUserLockedReson() {
		return userLockedReson;
	}

	/**
	 * <p>
	 * ��������: setUserLockedReson|����:���������û�ԭ��
	 * </p>
	 * 
	 * @param userLockedReson
	 */
	public void setUserLockedReson(String userLockedReson) {
		this.userLockedReson = userLockedReson;
	}

	/**
	 * <p>
	 * ��������: getStartDate|����: ȡ�ÿ�ʼ����
	 * </p>
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <p>
	 * ��������: setStartDate|����:���ÿ�ʼ����
	 * </p>
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * ��������: getEndDate|����:ȡ�ý�������
	 * </p>
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * <p>
	 * ��������: setEndDate|����:���ý�������
	 * </p>
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>
	 * ��������: getCreateTime|����:ȡ�ô���ʱ��
	 * </p>
	 * 
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * <p>
	 * ��������: setCreateTime|����:���ô���ʱ��
	 * </p>
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * <p>
	 * ��������: getDescription|����:ȡ��������Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * ��������: setDescription|����:����������Ϣ
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
	 * ��������: getEnabled|����: �Ƿ�����
	 * </p>
	 * 
	 * @return
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * <p>
	 * ��������: setEnabled|����:����
	 * </p>
	 * 
	 * @param enabled
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/**
	 * <p>
	 * ��������: getInstCname|����: ȡ�û�����������
	 * </p>
	 * 
	 * @return
	 */
	public String getInstCname() {
		return instCname;
	}

	/**
	 * <p>
	 * ��������: setInstCname|����:���û�����������
	 * </p>
	 * 
	 * @param instCname
	 */
	public void setInstCname(String instCname) {
		this.instCname = instCname;
	}

	/**
	 * <p>
	 * ��������: getLoginId|����:ȡ�õ�¼ID
	 * </p>
	 * 
	 * @return
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * <p>
	 * ��������: setLoginId|����: ���õ�¼ID
	 * </p>
	 * 
	 * @param loginId
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * <p>
	 * ��������: getBrowser|����: ȡ���������Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * <p>
	 * ��������: setBrowser|����:�����������Ϣ
	 * </p>
	 * 
	 * @param browser
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * <p>
	 * ��������: getMenuId|����:ȡ�ò˵�ID
	 * </p>
	 * 
	 * @return
	 */
	public String getMenuId() {
		return menuId;
	}

	/**
	 * <p>
	 * ��������: setMenuId|����:���ò˵�ID
	 * </p>
	 * 
	 * @param menuId
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/**
	 * <p>
	 * ��������: getMenuName|����:ȡ�ò˵�����
	 * </p>
	 * 
	 * @return
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * <p>
	 * ��������: setMenuName|����:���ò˵�����
	 * </p>
	 * 
	 * @param menuName
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * <p>
	 * ��������: getIp|����:ȡ��IP��Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * <p>
	 * ��������: setIp|����:����IP��Ϣ
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
