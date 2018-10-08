/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 上午10:21:59
 * @描述: [UBaseConfig]子系统基本信息表
 */
public class UBaseConfigDO extends BaseDO implements Serializable{

	private String systemId; // SYSTEM_ID 系统编号
	private String systemEname; // SYSTEM_ENAME 系统英文名称
	private String systemNickEname;// SYSTEM_NICK_ENAME 系统英文简称
	private String systemCname; // SYSTEM_CNAME 系统中文名
	private String systemNickCname;// SYSTEM_NICK_CNAME 系统中文简称
	//private String dbDriverClass; // DB_TYPE 菜单数据库类型
	private String dbUrl; // DB_SERVER_IP 菜单服务器IP
	//private String dbUserId; // DB_USER_ID 菜单数据库用户名
	//private String dbPassword; // DB_PASSWORD 菜单数据库密码
	private String menuName; // MENU_NAME 系统菜单名称
	private String menuTable; // MENU_TABLE 系统菜单存放表
	private String menuOrderNum; // MENU_ORDER_NUM 系统菜单序号
	private String menuImgSrc; // MENU_IMG_SRC 系统菜单图标
	private String linkTarget; // LINK_TARGET 系统菜单连接方式
	private String linkSiteUrl; // LINK_SITE_URL 系统外网连接地址
	private String linkSiteInnerUrl; // LINK_SITE_INNER_URL 系统内网连接地址
	private String unitLoginUrl; // UNIT_LOGIN_URL 系统菜单统一登录链接地址
	private String unitLoginInnerUrl; // UNIT_LOGIN_INNER_URL 系统菜单统一登录链接地址
	private String display; // DISPLAY 是否显示
	private String resDbType; // RES_DB_TYPE 资源数据库类型
	private String resDbUserId; // RES_DB_USER_ID 资源数据库用户名
	private String resDbServerPort;// RES_DB_SERVER_PORT 资源服务器端口
	private String resDbServerIp; // RES_DB_SERVER_IP 资源服务器IP
	private String resDbSid; // RES_DB_SID 资源数据库标识
	private String resDbPassword; // RES_DB_PASSWORD 资源数据库密码
	private String enabled; // ENABLED 是否可用
	private String menuRes; //菜单来源

	public String getMenuRes() {
		return menuRes;
	}

	public void setMenuRes(String menuRes) {
		this.menuRes = menuRes;
	}

	/**
	 * <p>方法名称: getSystemId|描述: 取得系统编号</p>
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
	 * <p>方法名称: getSystemEname|描述: 取得系统英文名称</p>
	 * @return
	 */
	public String getSystemEname(){
		return systemEname;
	}

	/**
	 * <p>方法名称: setSystemEname|描述: 设置系统英文名称</p>
	 * @param systemEname
	 */
	public void setSystemEname(String systemEname){
		this.systemEname = systemEname;
	}

	/**
	 * <p>方法名称: getSystemNickEname|描述:取得系统英文简称 </p>
	 * @return
	 */
	public String getSystemNickEname(){
		return systemNickEname;
	}

	/**
	 * <p>方法名称: setSystemNickEname|描述:设置系统英文简称 </p>
	 * @param systemNickEname
	 */
	public void setSystemNickEname(String systemNickEname){
		this.systemNickEname = systemNickEname;
	}

	/**
	 * <p>方法名称: getSystemCname|描述: 取得系统中文名称</p>
	 * @return
	 */
	public String getSystemCname(){
		return systemCname;
	}

	/**
	 * <p>方法名称: setSystemCname|描述:设置系统中文名称 </p>
	 * @param systemCname
	 */
	public void setSystemCname(String systemCname){
		this.systemCname = systemCname;
	}

	/**
	 * <p>方法名称: getSystemNickCname|描述:取得系统中文简称 </p>
	 * @return
	 */
	public String getSystemNickCname(){
		return systemNickCname;
	}

	/**
	 * <p>方法名称: setSystemNickCname|描述:设置系统中文简称 </p>
	 * @param systemNickCname
	 */
	public void setSystemNickCname(String systemNickCname){
		this.systemNickCname = systemNickCname;
	}

	
	/**
	 * <p>方法名称: getDbServerIp|描述:取得菜单服务器IP </p>
	 * @return
	 */
	public String getDbUrl(){
		return dbUrl;
	}

	/**
	 * <p>方法名称: setDbServerIp|描述:设置菜单服务器IP </p>
	 * @param dbServerIp
	 */
	public void setDbUrl(String dbUrl){
		this.dbUrl = dbUrl;
	}



	/**
	 * <p>方法名称: getMenuName|描述: 取得系统菜单名称</p>
	 * @return
	 */
	public String getMenuName(){
		return menuName;
	}

	/**
	 * <p>方法名称: setMenuName|描述:设置系统菜单名称 </p>
	 * @param menuName
	 */
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	/**
	 * <p>方法名称: getMenuTable|描述:取得系统菜单存放表 </p>
	 * @return
	 */
	public String getMenuTable(){
		return menuTable;
	}

	/**
	 * <p>方法名称: setMenuTable|描述:设置系统菜单存放表 </p>
	 * @param menuTable
	 */
	public void setMenuTable(String menuTable){
		this.menuTable = menuTable;
	}

	/**
	 * <p>方法名称: getMenuOrderNum|描述:取得系统菜单序号 </p>
	 * @return
	 */
	public String getMenuOrderNum(){
		return menuOrderNum;
	}

	/**
	 * <p>方法名称: setMenuOrderNum|描述:设置系统菜单序号 </p>
	 * @param menuOrderNum
	 */
	public void setMenuOrderNum(String menuOrderNum){
		this.menuOrderNum = menuOrderNum;
	}

	/**
	 * <p>方法名称: getMenuImgSrc|描述:取得系统菜单图标 </p>
	 * @return
	 */
	public String getMenuImgSrc(){
		return menuImgSrc;
	}

	/**
	 * <p>方法名称: setMenuImgSrc|描述:设置系统菜单图标 </p>
	 * @param menuImgSrc
	 */
	public void setMenuImgSrc(String menuImgSrc){
		this.menuImgSrc = menuImgSrc;
	}

	/**
	 * <p>方法名称: getLinkTarget|描述:取得系统菜单连接方式 </p>
	 * @return
	 */
	public String getLinkTarget(){
		return linkTarget;
	}

	/**
	 * <p>方法名称: setLinkTarget|描述:设置系统菜单连接方式 </p>
	 * @param linkTarget
	 */
	public void setLinkTarget(String linkTarget){
		this.linkTarget = linkTarget;
	}

	/**
	 * <p>方法名称: getLinkSiteUrl|描述:取得系统菜单连接地址 </p>
	 * @return
	 */
	public String getLinkSiteUrl(){
		return linkSiteUrl;
	}

	/**
	 * <p>方法名称: setLinkSiteUrl|描述:设置系统菜单连接地址 </p>
	 * @param linkSiteUrl
	 */
	public void setLinkSiteUrl(String linkSiteUrl){
		this.linkSiteUrl = linkSiteUrl;
	}

	/**
	 * <p>方法名称: getUnitLoginUrl|描述: 取得系统菜单统一登录链接地址</p>
	 * @return
	 */
	public String getUnitLoginUrl(){
		unitLoginUrl = unitLoginUrl == null ? "" : unitLoginUrl;
		linkSiteUrl = linkSiteUrl == null ? "" : linkSiteUrl;
		
		if (unitLoginUrl.trim().equals("") || unitLoginUrl.equals("/")) {
			return "/";
		}
		
		if(unitLoginUrl.startsWith("http")){
			return unitLoginUrl;
		}else{
			if(this.linkSiteUrl.endsWith("/")){
				return this.linkSiteUrl + unitLoginUrl;
			}else{
				return this.linkSiteUrl + "/" + unitLoginUrl;
			}
		}
	}

	/**
	 * <p>方法名称: setUnitLoginUrl|描述:设置系统菜单统一登录链接地址 </p>
	 * @param unitLoginUrl
	 */
	public void setUnitLoginUrl(String unitLoginUrl){
		this.unitLoginUrl = unitLoginUrl;
	}

	/**
	 * <p>方法名称: getDisplay|描述:取得是否显示 </p>
	 * @return
	 */
	public String getDisplay(){
		return display;
	}

	/**
	 * <p>方法名称: setDisplay|描述:设置是否显示 </p>
	 * @param display
	 */
	public void setDisplay(String display){
		this.display = display;
	}

	/**
	 * <p>方法名称: getResDbType|描述:取得资源数据库类型 </p>
	 * @return
	 */
	public String getResDbType(){
		return resDbType;
	}

	/**
	 * <p>方法名称: setResDbType|描述:设置资源数据库类型 </p>
	 * @param resDbType
	 */
	public void setResDbType(String resDbType){
		this.resDbType = resDbType;
	}

	/**
	 * <p>方法名称: getResDbUserId|描述:取得资源数据库用户名 </p>
	 * @return
	 */
	public String getResDbUserId(){
		return resDbUserId;
	}

	/**
	 * <p>方法名称: setResDbUserId|描述:设置资源数据库用户名 </p>
	 * @param resDbUserId
	 */
	public void setResDbUserId(String resDbUserId){
		this.resDbUserId = resDbUserId;
	}

	/**
	 * <p>方法名称: getResDbServerPort|描述:取得资源服务器端口 </p>
	 * @return
	 */
	public String getResDbServerPort(){
		return resDbServerPort;
	}

	/**
	 * <p>方法名称: setResDbServerPort|描述:设置资源服务器端口 </p>
	 * @param resDbServerPort
	 */
	public void setResDbServerPort(String resDbServerPort){
		this.resDbServerPort = resDbServerPort;
	}

	/**
	 * <p>方法名称: getResDbServerIp|描述:取得资源服务器IP </p>
	 * @return
	 */
	public String getResDbServerIp(){
		return resDbServerIp;
	}

	/**
	 * <p>方法名称: setResDbServerIp|描述:设置资源服务器IP </p>
	 * @param resDbServerIp
	 */
	public void setResDbServerIp(String resDbServerIp){
		this.resDbServerIp = resDbServerIp;
	}

	/**
	 * <p>方法名称: getResDbSid|描述:取得资源数据库标识 </p>
	 * @return
	 */
	public String getResDbSid(){
		return resDbSid;
	}

	/**
	 * <p>方法名称: setResDbSid|描述:设置资源数据库标识 </p>
	 * @param resDbSid
	 */
	public void setResDbSid(String resDbSid){
		this.resDbSid = resDbSid;
	}

	/**
	 * <p>方法名称: getResDbPassword|描述:取得资源数据库密码 </p>
	 * @return
	 */
	public String getResDbPassword(){
		return resDbPassword;
	}

	/**
	 * <p>方法名称: setResDbPassword|描述: 设置资源数据库密码</p>
	 * @param resDbPassword
	 */
	public void setResDbPassword(String resDbPassword){
		this.resDbPassword = resDbPassword;
	}

	/**
	 * <p>方法名称: getEnabled|描述:取得系统是否可用标识符 </p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>方法名称: setEnabled|描述:设置系统是否可用标识符 </p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
	}

	public String getLinkSiteInnerUrl() {
		return linkSiteInnerUrl;
	}

	public void setLinkSiteInnerUrl(String linkSiteInnerUrl) {
		this.linkSiteInnerUrl = linkSiteInnerUrl;
	}

	public String getUnitLoginInnerUrl() {
		return unitLoginInnerUrl;
	}

	public void setUnitLoginInnerUrl(String unitLoginInnerUrl) {
		this.unitLoginInnerUrl = unitLoginInnerUrl;
	}


}
