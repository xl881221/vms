/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: liuyi
 * @����: 2011-6-17 ����04:03:04
 * @����: [UBaseInstDO]�û����߱�
 */
public class UBaseOnlineDO extends BaseDO implements Serializable{

	private  String loginId ;//��½ID
	private  String userId ;
	private  Timestamp loginTime ;//��½ʱ��
	private  Timestamp kickoutTime ;//���߳�ʱ��
	private  String addr ;//��½IP
	private  String status ;//״̬
	private  UBaseUserDO baseUser;
	private  String instId;
	

	public UBaseOnlineDO(){
		
	}
	
    public UBaseOnlineDO(String loginId,String userId,String addr){
		this.loginId=loginId;
		this.userId=userId;
		this.loginTime=new Timestamp(System.currentTimeMillis());
		this.addr=addr;
		this.status="1";
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public java.sql.Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(java.sql.Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public UBaseUserDO getBaseUser() {
		return baseUser;
	}

	public void setBaseUser(UBaseUserDO baseUser) {
		this.baseUser = baseUser;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}



	public Timestamp getKickoutTime() {
		return kickoutTime;
	}

	public void setKickoutTime(Timestamp kickoutTime) {
		this.kickoutTime = kickoutTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	
}
