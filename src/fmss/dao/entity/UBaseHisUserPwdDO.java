/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:02:29
 * @描述: [UBaseHisUserPwdDO]用户历史密码表
 */
public class UBaseHisUserPwdDO extends BaseDO implements Serializable{

	private Long id;// 编号
	private String userId;// 用户编号 U_BASE_USER.USER_ID
	private String password;// 密码 password
	private Date modifyTime;// 修改密码时间 MODIFY_TIME

	/**
	 * <p>方法名称: getId|描述:取得编号信息 </p>
	 * @return
	 */
	public Long getId(){
		return id;
	}

	/**
	 * <p>方法名称: setId|描述:设置编号信息 </p>
	 * @param id
	 */
	public void setId(Long id){
		this.id = id;
	}

	/**
	 * <p>方法名称: getUserId|描述:取得用户编号 </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>方法名称: setUserId|描述:设置用户编号 </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * <p>方法名称: getPassword|描述:取得密码信息 </p>
	 * @return
	 */
	public String getPassword(){
		return password;
	}

	/**
	 * <p>方法名称: setPassword|描述:设置密码信息 </p>
	 * @param password
	 */
	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * <p>方法名称: getModifyTime|描述:取得修改密码时间 </p>
	 * @return
	 */
	public Date getModifyTime(){
		return modifyTime;
	}

	/**
	 * <p>方法名称: setModifyTime|描述:设置修改密码时间 </p>
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}
}
