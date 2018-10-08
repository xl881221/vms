/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����03:50:23
 * @����: [UAuthRoleDO]��ɫ��
 */
public class UAuthRoleDO extends BaseDO implements Serializable{

	private String roleId;// ��ɫ��� ROLE_ID
	private String roleName;// ��ɫ���� ROLE_NAME
	private Date startDate = new Date(new java.util.Date().getTime());// ��ʼ����
	private Date endDate = new Date(new java.util.Date().getTime());// ��������
	private Date createTime = new Date(new java.util.Date().getTime());// ����ʱ��
																		// CREATE_TIME
	private String description;// ���� DESCRIPTION
	private String enabled;// ���ñ�ʶ
	private String systemId;// ϵͳ��� SYSTEM_ID
	private List authRoleRes;// ��ɫ��Դ
	private String isHead;//�Ƿ������ʹ��
	private String systemCname;



	public String getSystemCname() {
		return systemCname;
	}

	public void setSystemCname(String systemCname) {
		this.systemCname = systemCname;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	/**
	 * <p>��������: getRoleId|����:ȡ�ý�ɫ��� </p>
	 * @return
	 */
	public String getRoleId(){
		return roleId;
	}

	/**
	 * <p>��������: setRoleId|����:���ý�ɫ��� </p>
	 * @param roleId
	 */
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	/**
	 * <p>��������: getRoleName|����:ȡ�ý�ɫ���� </p>
	 * @return
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 * <p>��������: setRoleName|����:���ý�ɫ���� </p>
	 * @param roleName
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	/**
	 * <p>��������: getStartDate|����:ȡ�ÿ�ʼ���� </p>
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
	 * <p>��������: getCreateTime|����: ȡ�ô���ʱ�� </p>
	 * @return
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * <p>��������: setCreateTime|����: ���ô���ʱ��</p>
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
	 * <p>��������: setDescription|����: ����������Ϣ</p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * <p>��������: getEnabled|����:ȡ�����ñ�ʶ </p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>��������: setEnabled|����:�������ñ�ʶ </p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
	}

	/**
	 * <p>��������: getSystemId|����:ȡ��ϵͳ��� </p>
	 * @return
	 */
	public String getSystemId(){
		return systemId;
	}

	/**
	 * <p>��������: setSystemId|����:����ϵͳ��� </p>
	 * @param systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	/**
	 * <p>��������: getAuthRoleRes|����:ȡ�ý�ɫ��Դ��Ϣ </p>
	 * @return
	 */
	public List getAuthRoleRes(){
		return authRoleRes;
	}

	/**
	 * <p>��������: setAuthRoleRes|����:���ý�ɫ��Դ��Ϣ </p>
	 * @param authRoleRes
	 */
	public void setAuthRoleRes(List authRoleRes){
		this.authRoleRes = authRoleRes;
	}
}
