package fmss.dao.entity;

import java.io.Serializable;

public class UserSimultaneousDo extends BaseDO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;//用户ID
	
	private String userCname;//用户姓名
	
	private String instId;//部门
	
	private String email;//邮箱
	
	private String isExistsUprr;//同步状态
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCname() {
		return userCname;
	}

	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsExistsUprr() {
		return isExistsUprr;
	}

	public void setIsExistsUprr(String isExistsUprr) {
		this.isExistsUprr = isExistsUprr;
	} 
	
}
