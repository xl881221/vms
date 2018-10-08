package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: jiazhimin
 * @����: 2011-04-25
 * @����: [VcrmsSystemRelaDO]������
 */
public class VBaseUserEmailDO extends BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String userId;
	private String userEname;
	private String systemId;
	private String emailAddr;
	
	public String getUserEname() {
		return userEname;
	}
	public void setUserEname(String userEname) {
		this.userEname = userEname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	
	
}
