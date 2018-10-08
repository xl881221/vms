/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午03:54:15
 * @描述:[UAuthRoleUserDO]角色表
 */
public class UAuthRoleUserDO extends BaseDO implements Serializable{

	private String roleId;// 角色编号 U_AUTH_ROLE.ROLE_ID
	private String userId;// 描述 U_BASE_USER.USER_ID

	/**
	 * <p>方法名称: getRoleId|描述:取得角色编号 </p>
	 * @return
	 */
	public String getRoleId(){
		return roleId;
	}

	/**
	 * <p>方法名称: setRoleId|描述:设置角色编号 </p>
	 * @param roleId
	 */
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	/**
	 * <p>方法名称: getUserId|描述:取得描述信息 </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>方法名称: setUserId|描述:设置描述信息 </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
}
