/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:02:29
 * @����: [UBaseHisUserPwdDO]�û���ʷ�����
 */
public class UBaseHisUserPwdDO extends BaseDO implements Serializable{

	private Long id;// ���
	private String userId;// �û���� U_BASE_USER.USER_ID
	private String password;// ���� password
	private Date modifyTime;// �޸�����ʱ�� MODIFY_TIME

	/**
	 * <p>��������: getId|����:ȡ�ñ����Ϣ </p>
	 * @return
	 */
	public Long getId(){
		return id;
	}

	/**
	 * <p>��������: setId|����:���ñ����Ϣ </p>
	 * @param id
	 */
	public void setId(Long id){
		this.id = id;
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
	 * <p>��������: getPassword|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * <p>��������: setPassword|����:����������Ϣ </p>
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * <p>��������: getModifyTime|����:ȡ���޸�����ʱ�� </p>
	 * @return
	 */
	public Date getModifyTime(){
		return modifyTime;
	}

	/**
	 * <p>��������: setModifyTime|����:�����޸�����ʱ�� </p>
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
}
