/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午03:50:23
 * @描述: [UAuthRoleDO]角色表
 */
public class UAuthRoleDO extends BaseDO implements Serializable{

	private String roleId;// 角色编号 ROLE_ID
	private String roleName;// 角色名称 ROLE_NAME
	private Date startDate = new Date(new java.util.Date().getTime());// 开始日期
	private Date endDate = new Date(new java.util.Date().getTime());// 结束日期
	private Date createTime = new Date(new java.util.Date().getTime());// 创建时间
																		// CREATE_TIME
	private String description;// 描述 DESCRIPTION
	private String enabled;// 启用标识
	private String systemId;// 系统编号 SYSTEM_ID
	private List authRoleRes;// 角色资源
	private String isHead;//是否仅总行使用
	private String systemCname;



	public String getSystemCname() {
		return systemCname;
	}

	public void setSystemCname(String systemCname) {
		this.systemCname = systemCname;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

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
	 * <p>方法名称: getRoleName|描述:取得角色名称 </p>
	 * @return
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 * <p>方法名称: setRoleName|描述:设置角色名称 </p>
	 * @param roleName
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	/**
	 * <p>方法名称: getStartDate|描述:取得开始日期 </p>
	 * @return
	 */
	public Date getStartDate(){
		return startDate;
	}

	/**
	 * <p>方法名称: setStartDate|描述:设置开始日期 </p>
	 * @param startDate
	 */
	public void setStartDate(Date startDate){
		this.startDate = startDate;
	}

	/**
	 * <p>方法名称: getEndDate|描述:取得结束日期 </p>
	 * @return
	 */
	public Date getEndDate(){
		return endDate;
	}

	/**
	 * <p>方法名称: setEndDate|描述:设置结束日期 </p>
	 * @param endDate
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}

	/**
	 * <p>方法名称: getCreateTime|描述: 取得创建时间 </p>
	 * @return
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 * <p>方法名称: setCreateTime|描述: 设置创建时间</p>
	 * @param createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	/**
	 * <p>方法名称: getDescription|描述:取得描述信息 </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>方法名称: setDescription|描述: 设置描述信息</p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * <p>方法名称: getEnabled|描述:取得启用标识 </p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>方法名称: setEnabled|描述:设置启用标识 </p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
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

	/**
	 * <p>方法名称: getAuthRoleRes|描述:取得角色资源信息 </p>
	 * @return
	 */
	public List getAuthRoleRes(){
		return authRoleRes;
	}

	/**
	 * <p>方法名称: setAuthRoleRes|描述:设置角色资源信息 </p>
	 * @param authRoleRes
	 */
	public void setAuthRoleRes(List authRoleRes){
		this.authRoleRes = authRoleRes;
	}
}
