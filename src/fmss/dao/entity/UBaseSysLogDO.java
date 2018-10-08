/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:04:25
 * @描述: [UBaseSysLogDO]系统日志表
 */
public class UBaseSysLogDO extends BaseDO implements Serializable{

	private long logId;// LOG_ID 日志编号
	private String userId;// USER_ID 用户编号
	private String userEname;// USER_ENAME 用户登录名
	private String userCname;// USER_CNAME 用户中文
	private String instId;// INST_ID 机构编号
	private String instCname;// INST_CNAME 机构名称
	private String menuId;// MENU_ID 栏目编号
	private String menuName;// MENU_NAME 栏目名称
	private String ip;// IP 用户IP
	private String browse;// BROWSE 用户浏览器
	private String logType;// LOG_TYPE 日志类型--00001--用户登录日志
	private Timestamp execTime;// EXEC_TIME 执行时间
	private String description;// DESCRIPTION 描述
	private String status;// 操作状态
	private String systemId;// 子系统编号
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * <p>方法名称: getLogId|描述:取得日志编号 </p>
	 * @return
	 */
	public long getLogId(){
		return logId;
	}

	/**
	 * <p>方法名称: setLogId|描述:设置日志编号 </p>
	 * @param logId
	 */
	public void setLogId(long logId){
		this.logId = logId;
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
	 * <p>方法名称: getUserEname|描述:取得用户英文名称 </p>
	 * @return
	 */
	public String getUserEname(){
		return userEname;
	}

	/**
	 * <p>方法名称: setUserEname|描述:设置用户英文名称 </p>
	 * @param userEname
	 */
	public void setUserEname(String userEname){
		this.userEname = userEname;
	}

	/**
	 * <p>方法名称: getUserCname|描述:取得用户中文名称</p>
	 * @return
	 */
	public String getUserCname(){
		return userCname;
	}

	/**
	 * <p>方法名称: setUserCname|描述:取得用户英文名称 </p>
	 * @param userCname
	 */
	public void setUserCname(String userCname){
		this.userCname = userCname;
	}

	/**
	 * <p>方法名称: getInstId|描述: 取得机构编号</p>
	 * @return
	 */
	public String getInstId(){
		return instId;
	}

	/**
	 * <p>方法名称: setInstId|描述:设置机构编号 </p>
	 * @param instId
	 */
	public void setInstId(String instId){
		this.instId = instId;
	}

	/**
	 * <p>方法名称: getInstCname|描述:取得机构中文名称 </p>
	 * @return
	 */
	public String getInstCname(){
		return instCname;
	}

	/**
	 * <p>方法名称: setInstCname|描述:设置机构中文名称 </p>
	 * @param instCname
	 */
	public void setInstCname(String instCname){
		this.instCname = instCname;
	}

	/**
	 * <p>方法名称: getMenuId|描述:取得栏目编号 </p>
	 * @return
	 */
	public String getMenuId(){
		return menuId;
	}

	/**
	 * <p>方法名称: setMenuId|描述:设置栏目编号 </p>
	 * @param menuId
	 */
	public void setMenuId(String menuId){
		this.menuId = menuId;
	}

	/**
	 * <p>方法名称: getMenuName|描述:取得栏目名称 </p>
	 * @return
	 */
	public String getMenuName(){
		return menuName;
	}

	/**
	 * <p>方法名称: setMenuName|描述:设置栏目名称 </p>
	 * @param menuName
	 */
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	/**
	 * <p>方法名称: getIp|描述: 取得IP信息</p>
	 * @return
	 */
	public String getIp(){
		return ip;
	}

	/**
	 * <p>方法名称: setIp|描述:设置IP </p>
	 * @param ip
	 */
	public void setIp(String ip){
		this.ip = ip;
	}

	/**
	 * <p>方法名称: getBrowse|描述: 取得浏览器信息</p>
	 * @return
	 */
	public String getBrowse(){
		return browse;
	}

	/**
	 * <p>方法名称: setBrowse|描述:设置浏览器信息 </p>
	 * @param browse
	 */
	public void setBrowse(String browse){
		this.browse = browse;
	}

	/**
	 * <p>方法名称: getLogType|描述:取得日志类型 </p>
	 * @return
	 */
	public String getLogType(){
		return logType;
	}

	/**
	 * <p>方法名称: setLogType|描述:设置日志类型 </p>
	 * @param logType
	 */
	public void setLogType(String logType){
		this.logType = logType;
	}

	/**
	 * <p>方法名称: getExecTime|描述:取得执行时间 </p>
	 * @return
	 */
	public Timestamp getExecTime(){
		return execTime;
	}

	/**
	 * <p>方法名称: setExecTime|描述:设置执行时间 </p>
	 * @param execTime
	 */
	public void setExecTime(Timestamp execTime){
		this.execTime = execTime;
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

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
}
