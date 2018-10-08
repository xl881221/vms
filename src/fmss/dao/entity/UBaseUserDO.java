/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:04:53
 * @����: [UBaseUserDO]�û���
 */
public class UBaseUserDO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userId;// �û���� USER_ID
	private String userEname;// �û�Ӣ���� USER_ENAME
	private String userCname;// �û������� USER_CNAME
	private String password;// �û����� PASSWORD
	private String instId;// ������� U_BASE_INST.INST_ID
	private String instName;// ������� U_BASE_INST.INST_ID
	private UBaseInstDO ubaseInst;
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
	private Date startDate = new Date(new java.util.Date().getTime());// ��������
	// START_DATE
	private Date endDate = new Date(2049,12,31);// ��ֹ����
	// END_DATE
	private Date createTime = new Date(new java.util.Date().getTime());// ����ʱ��
	// CREATE_TIME
	private String description;// ���� DESCRIPTION
	private String enabled;// ���ñ�ʶ ENABLED
	private String startDateStr;// ��ʼ�����ַ���
	private String endDateStr;// ���������ַ���
	private String createTimeStr;// ����ʱ���ַ���
	private UBaseInstDO baseInst;// ������Ϣ
    private Set uauthRole = new HashSet();
    private String isDelete;// �Ƿ�ɾ��
    private String isList ;//�Ƿ����ڰ�����
    
    private Date lastLoginDate;// ����¼����
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * <p>��������: getUserId|����:ȡ���û���� </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>��������: setUserId|����:�����û���� </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * <p>��������: getUserEname|����:ȡ���û�Ӣ������ </p>
	 * @return
	 */
	public String getUserEname(){
		return userEname;
	}

	/**
	 * <p>��������: setUserEname|����:�����û�Ӣ������ </p>
	 * @param userEname
	 */
	public void setUserEname(String userEname){
		this.userEname = userEname;
	}

	/**
	 * <p>��������: getUserCname|����:ȡ���û��������� </p>
	 * @return
	 */
	public String getUserCname(){
		return userCname;
	}

	/**
	 * <p>��������: setUserCname|����:�����û��������� </p>
	 * @param userCname
	 */
	public void setUserCname(String userCname){
		this.userCname = userCname;
	}

	/**
	 * <p>��������: getPassword|����: ȡ���û�������Ϣ</p>
	 * @return
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * <p>��������: setPassword|����:�����û�������Ϣ </p>
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * <p>��������: getInstId|����: ȡ���û������������</p>
	 * @return
	 */
	public String getInstId(){
		return instId;
	}

	/**
	 * <p>��������: setInstId|����:�����û������������ </p>
	 * @param instId
	 */
	public void setInstId(String instId){
		this.instId = instId;
	}

	/**
	 * <p>��������: getInstName|����: ȡ���û�������������</p>
	 * @return
	 */
	public String getInstName() {
		return instName;
	}

	/**
	 * <p>��������: setInstName|����:�����û�������������</p>
	 * @param instName
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * <p>��������: getUbaseInst|����:ȡ���û�����������Ϣ </p>
	 * @return
	 */
	public UBaseInstDO getUbaseInst(){
		return ubaseInst;
	}

	/**
	 * <p>��������: setUbaseInst|����:�����û�����������Ϣ </p>
	 * @param ubaseInst
	 */
	public void setUbaseInst(UBaseInstDO ubaseInst){
		this.ubaseInst = ubaseInst;
	}

	/**
	 * <p>��������: getDepartId|����: ȡ���û����ڲ�����Ϣ</p>
	 * @return
	 */
	public String getDepartId(){
		return departId;
	}

	/**
	 * <p>��������: setDepartId|����:�����û����ڲ�����Ϣ </p>
	 * @param departId
	 */
	public void setDepartId(String departId){
		this.departId = departId;
	}

	/**
	 * <p>��������: getTel|����:ȡ�õ绰���� </p>
	 * @return
	 */
	public String getTel(){
		return tel;
	}

	/**
	 * <p>��������: setTel|����:���õ绰���� </p>
	 * @param tel
	 */
	public void setTel(String tel){
		this.tel = tel;
	}

	/**
	 * <p>��������: getMobile|����:ȡ���ֻ����� </p>
	 * @return
	 */
	public String getMobile(){
		return mobile;
	}

	/**
	 * <p>��������: setMobile|����:�����ֻ����� </p>
	 * @param mobile
	 */
	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	/**
	 * <p>��������: getAddress|����:ȡ���û���ַ��Ϣ </p>
	 * @return
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * <p>��������: setAddress|����:�����û���ַ��Ϣ</p>
	 * @param address
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * <p>��������: getEmail|����: ȡ���û�EMAIL��Ϣ</p>
	 * @return
	 */
	public String getEmail(){
		return email;
	}

	/**
	 * <p>��������: setEmail|����:�����û�EMAIL��Ϣ </p>
	 * @param email
	 */
	public void setEmail(String email){
		this.email = email;
	}

	/**
	 * <p>��������: getLastModifyDate|����:ȡ������޸����� </p>
	 * @return
	 */
	public Date getLastModifyDate(){
		return lastModifyDate;
	}

	/**
	 * <p>��������: setLastModifyDate|����:��������޸����� </p>
	 * @param lastModifyDate
	 */
	public void setLastModifyDate(Date lastModifyDate){
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * <p>��������: getIsFirstLogin|����:�Ƿ�Ϊ�״ε�¼ </p>
	 * @return
	 */
	public String getIsFirstLogin(){
		return isFirstLogin;
	}

	/**
	 * <p>��������: setIsFirstLogin|����: ����Ϊ�״ε�¼</p>
	 * @param isFirstLogin
	 */
	public void setIsFirstLogin(String isFirstLogin){
		this.isFirstLogin = isFirstLogin;
	}

	/**
	 * <p>��������: getWrongPwdCount|����:ȡ������������ </p>
	 * @return
	 */
	public String getWrongPwdCount(){
		return wrongPwdCount;
	}

	/**
	 * <p>��������: setWrongPwdCount|����:�������������� </p>
	 * @param wrongPwdCount
	 */
	public void setWrongPwdCount(String wrongPwdCount){
		this.wrongPwdCount = wrongPwdCount;
	}

	/**
	 * <p>��������: getWrongPwdDate|����:ȡ�����һ����������������� </p>
	 * @return
	 */
	public Date getWrongPwdDate(){
		return wrongPwdDate;
	}

	/**
	 * <p>��������: setWrongPwdDate|����:�������һ����������������� </p>
	 * @param wrongPwdDate
	 */
	public void setWrongPwdDate(Date wrongPwdDate){
		this.wrongPwdDate = wrongPwdDate;
	}

	/**
	 * <p>��������: getIsUserLocked|����: �Ƿ������û�</p>
	 * @return
	 */
	public String getIsUserLocked(){
		return isUserLocked;
	}

	/**
	 * <p>��������: setIsUserLocked|����: �����û�</p>
	 * @param isUserLocked
	 */
	public void setIsUserLocked(String isUserLocked){
		this.isUserLocked = isUserLocked;
	}

	/**
	 * <p>��������: getUserLockedReson|����:ȡ�������û�ԭ�� </p>
	 * @return
	 */
	public String getUserLockedReson(){
		return userLockedReson;
	}

	/**
	 * <p>��������: setUserLockedReson|����:���������û�ԭ�� </p>
	 * @param userLockedReson
	 */
	public void setUserLockedReson(String userLockedReson){
		this.userLockedReson = userLockedReson;
	}

	/**
	 * <p>��������: getStartDate|����: ȡ�ÿ�ʼ����</p>
	 * @return
	 */
	public Date getStartDate(){
		return startDate;
	}

	/**
	 * <p>��������: setStartDate|����:���ÿ�ʼ���� </p>
	 * @param startDate
	 */
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	/**
	 * <p>��������: getEndDate|����:ȡ�ý������� </p>
	 * @return
	 */
	public Date getEndDate(){
		return endDate;
	}

	/**
	 * <p>��������: setEndDate|����:���ý������� </p>
	 * @param endDate
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	/**
	 * <p>��������: getCreateTime|����:ȡ�ô���ʱ�� </p>
	 * @return
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * <p>��������: setCreateTime|����:���ô���ʱ�� </p>
	 * @param createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	/**
	 * <p>��������: getDescription|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>��������: setDescription|����:����������Ϣ </p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * <p>��������: getEnabled|����: �Ƿ�����</p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>��������: setEnabled|����:���� </p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
	}

	/**
	 * <p>��������: getStartDateStr|����: ȡ�ÿ�ʼ�����ַ���</p>
	 * @return
	 */
	public String getStartDateStr(){
		return startDateStr;
	}

	/**
	 * <p>��������: setStartDateStr|����: ���ÿ�ʼ�����ַ���</p>
	 * @param startDateStr
	 */
	public void setStartDateStr(String startDateStr){
		this.startDateStr = startDateStr;
	}

	/**
	 * <p>��������: getEndDateStr|����:ȡ�ý��������ַ��� </p>
	 * @return
	 */
	public String getEndDateStr(){
		return endDateStr;
	}

	/**
	 * <p>��������: setEndDateStr|����: ���ý��������ַ���</p>
	 * @param endDateStr
	 */
	public void setEndDateStr(String endDateStr){
		this.endDateStr = endDateStr;
	}

	/**
	 * <p>��������: getCreateTimeStr|����: ȡ�ô���ʱ���ַ���</p>
	 * @return
	 */
	public String getCreateTimeStr(){
		return createTimeStr;
	}

	/**
	 * <p>��������: setCreateTimeStr|����:���ô���ʱ���ַ��� </p>
	 * @param createTimeStr
	 */
	public void setCreateTimeStr(String createTimeStr){
		this.createTimeStr = createTimeStr;
	}

	/**
	 * <p>��������: getBaseInst|����: ȡ�û�����Ϣ</p>
	 * @return
	 */
	public UBaseInstDO getBaseInst(){
		return baseInst;
	}

	/**
	 * <p>��������: setBaseInst|����:���û�����Ϣ </p>
	 * @param baseInst
	 */
	public void setBaseInst(UBaseInstDO baseInst){
		this.baseInst = baseInst;
	}

	/**
	 * <p>��������: getStartDateString|����: ��ʽ��ʼ������ʽΪ��yyyy-MM-dd</p>
	 * @return
	 */
	public String getStartDateString(){
		if(startDate != null){
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); // ��ʽ����ǰϵͳ����
			return dateFm.format(startDate);
		}else{
			return "";
		}
	}

	/**
	 * <p>��������: getEndDateString|����:��ʽ����������ʽΪ��yyyy-MM-dd </p>
	 * @return
	 */
	public String getEndDateString(){
		if(endDate != null){
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); // ��ʽ����ǰϵͳ����
			return dateFm.format(endDate);
		}else{
			return "";
		}
	}

	public Set getUauthRole() {
		return uauthRole;
	}

	public void setUauthRole(Set uauthRole) {
		this.uauthRole = uauthRole;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public UBaseUserDO(String userId, String userCname) {
		super();
		this.userId = userId;
		this.userCname = userCname;
	}

	public UBaseUserDO() {
		super();
	}

	public String getIsList() {
		return isList;
	}

	public void setIsList(String isList) {
		this.isList = isList;
	}

}
