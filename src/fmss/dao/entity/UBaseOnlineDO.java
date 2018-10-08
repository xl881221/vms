/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: liuyi
 * @日期: 2011-6-17 下午04:03:04
 * @描述: [UBaseInstDO]用户在线表
 */
public class UBaseOnlineDO extends BaseDO implements Serializable{

	private  String loginId ;//登陆ID
	private  String userId ;
	private  Timestamp loginTime ;//登陆时间
	private  Timestamp kickoutTime ;//被踢出时间
	private  String addr ;//登陆IP
	private  String status ;//状态
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
