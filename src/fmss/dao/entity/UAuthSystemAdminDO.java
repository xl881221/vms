/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午03:55:03
 * @描述: [UAuthSystemAdminDO]子系统管理员表
 */
public class UAuthSystemAdminDO extends BaseDO implements Serializable{

	private String userId;// 用户编号 U_BASE_USER.USER_ID
	private String systemId;// 系统编号 U_BASE_CONFIG.SYSTEM_ID

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
	 * <p>方法名称: getSystemId|描述:取得系统编号 </p>
	 * @return
	 */
	public String getSystemId(){
		return systemId;
	}

	/**
	 * <p>方法名称: setSystemId|描述:设置系统编号 </p>
	 * @param systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}
}
