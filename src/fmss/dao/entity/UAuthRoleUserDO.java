/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����03:54:15
 * @����:[UAuthRoleUserDO]��ɫ��
 */
public class UAuthRoleUserDO extends BaseDO implements Serializable{

	private String roleId;// ��ɫ��� U_AUTH_ROLE.ROLE_ID
	private String userId;// ���� U_BASE_USER.USER_ID

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
	 * <p>��������: getUserId|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>��������: setUserId|����:����������Ϣ </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
}
