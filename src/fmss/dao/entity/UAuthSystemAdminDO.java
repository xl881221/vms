/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����03:55:03
 * @����: [UAuthSystemAdminDO]��ϵͳ����Ա��
 */
public class UAuthSystemAdminDO extends BaseDO implements Serializable{

	private String userId;// �û���� U_BASE_USER.USER_ID
	private String systemId;// ϵͳ��� U_BASE_CONFIG.SYSTEM_ID

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
}
