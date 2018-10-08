/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����10:21:59
 * @����: [UBaseConfig]��ϵͳ������Ϣ��
 */
public class UBaseConfigDO extends BaseDO implements Serializable{

	private String systemId; // SYSTEM_ID ϵͳ���
	private String systemEname; // SYSTEM_ENAME ϵͳӢ������
	private String systemNickEname;// SYSTEM_NICK_ENAME ϵͳӢ�ļ��
	private String systemCname; // SYSTEM_CNAME ϵͳ������
	private String systemNickCname;// SYSTEM_NICK_CNAME ϵͳ���ļ��
	//private String dbDriverClass; // DB_TYPE �˵����ݿ�����
	private String dbUrl; // DB_SERVER_IP �˵�������IP
	//private String dbUserId; // DB_USER_ID �˵����ݿ��û���
	//private String dbPassword; // DB_PASSWORD �˵����ݿ�����
	private String menuName; // MENU_NAME ϵͳ�˵�����
	private String menuTable; // MENU_TABLE ϵͳ�˵���ű�
	private String menuOrderNum; // MENU_ORDER_NUM ϵͳ�˵����
	private String menuImgSrc; // MENU_IMG_SRC ϵͳ�˵�ͼ��
	private String linkTarget; // LINK_TARGET ϵͳ�˵����ӷ�ʽ
	private String linkSiteUrl; // LINK_SITE_URL ϵͳ�������ӵ�ַ
	private String linkSiteInnerUrl; // LINK_SITE_INNER_URL ϵͳ�������ӵ�ַ
	private String unitLoginUrl; // UNIT_LOGIN_URL ϵͳ�˵�ͳһ��¼���ӵ�ַ
	private String unitLoginInnerUrl; // UNIT_LOGIN_INNER_URL ϵͳ�˵�ͳһ��¼���ӵ�ַ
	private String display; // DISPLAY �Ƿ���ʾ
	private String resDbType; // RES_DB_TYPE ��Դ���ݿ�����
	private String resDbUserId; // RES_DB_USER_ID ��Դ���ݿ��û���
	private String resDbServerPort;// RES_DB_SERVER_PORT ��Դ�������˿�
	private String resDbServerIp; // RES_DB_SERVER_IP ��Դ������IP
	private String resDbSid; // RES_DB_SID ��Դ���ݿ��ʶ
	private String resDbPassword; // RES_DB_PASSWORD ��Դ���ݿ�����
	private String enabled; // ENABLED �Ƿ����
	private String menuRes; //�˵���Դ

	public String getMenuRes() {
		return menuRes;
	}

	public void setMenuRes(String menuRes) {
		this.menuRes = menuRes;
	}

	/**
	 * <p>��������: getSystemId|����: ȡ��ϵͳ���</p>
	 * @return
	 */
	public String getSystemId(){
		return systemId;
	}

	/**
	 * <p>��������: setSystemId|����:����ϵͳ��� </p>
	 * @param systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	/**
	 * <p>��������: getSystemEname|����: ȡ��ϵͳӢ������</p>
	 * @return
	 */
	public String getSystemEname(){
		return systemEname;
	}

	/**
	 * <p>��������: setSystemEname|����: ����ϵͳӢ������</p>
	 * @param systemEname
	 */
	public void setSystemEname(String systemEname){
		this.systemEname = systemEname;
	}

	/**
	 * <p>��������: getSystemNickEname|����:ȡ��ϵͳӢ�ļ�� </p>
	 * @return
	 */
	public String getSystemNickEname(){
		return systemNickEname;
	}

	/**
	 * <p>��������: setSystemNickEname|����:����ϵͳӢ�ļ�� </p>
	 * @param systemNickEname
	 */
	public void setSystemNickEname(String systemNickEname){
		this.systemNickEname = systemNickEname;
	}

	/**
	 * <p>��������: getSystemCname|����: ȡ��ϵͳ��������</p>
	 * @return
	 */
	public String getSystemCname(){
		return systemCname;
	}

	/**
	 * <p>��������: setSystemCname|����:����ϵͳ�������� </p>
	 * @param systemCname
	 */
	public void setSystemCname(String systemCname){
		this.systemCname = systemCname;
	}

	/**
	 * <p>��������: getSystemNickCname|����:ȡ��ϵͳ���ļ�� </p>
	 * @return
	 */
	public String getSystemNickCname(){
		return systemNickCname;
	}

	/**
	 * <p>��������: setSystemNickCname|����:����ϵͳ���ļ�� </p>
	 * @param systemNickCname
	 */
	public void setSystemNickCname(String systemNickCname){
		this.systemNickCname = systemNickCname;
	}

	
	/**
	 * <p>��������: getDbServerIp|����:ȡ�ò˵�������IP </p>
	 * @return
	 */
	public String getDbUrl(){
		return dbUrl;
	}

	/**
	 * <p>��������: setDbServerIp|����:���ò˵�������IP </p>
	 * @param dbServerIp
	 */
	public void setDbUrl(String dbUrl){
		this.dbUrl = dbUrl;
	}



	/**
	 * <p>��������: getMenuName|����: ȡ��ϵͳ�˵�����</p>
	 * @return
	 */
	public String getMenuName(){
		return menuName;
	}

	/**
	 * <p>��������: setMenuName|����:����ϵͳ�˵����� </p>
	 * @param menuName
	 */
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	/**
	 * <p>��������: getMenuTable|����:ȡ��ϵͳ�˵���ű� </p>
	 * @return
	 */
	public String getMenuTable(){
		return menuTable;
	}

	/**
	 * <p>��������: setMenuTable|����:����ϵͳ�˵���ű� </p>
	 * @param menuTable
	 */
	public void setMenuTable(String menuTable){
		this.menuTable = menuTable;
	}

	/**
	 * <p>��������: getMenuOrderNum|����:ȡ��ϵͳ�˵���� </p>
	 * @return
	 */
	public String getMenuOrderNum(){
		return menuOrderNum;
	}

	/**
	 * <p>��������: setMenuOrderNum|����:����ϵͳ�˵���� </p>
	 * @param menuOrderNum
	 */
	public void setMenuOrderNum(String menuOrderNum){
		this.menuOrderNum = menuOrderNum;
	}

	/**
	 * <p>��������: getMenuImgSrc|����:ȡ��ϵͳ�˵�ͼ�� </p>
	 * @return
	 */
	public String getMenuImgSrc(){
		return menuImgSrc;
	}

	/**
	 * <p>��������: setMenuImgSrc|����:����ϵͳ�˵�ͼ�� </p>
	 * @param menuImgSrc
	 */
	public void setMenuImgSrc(String menuImgSrc){
		this.menuImgSrc = menuImgSrc;
	}

	/**
	 * <p>��������: getLinkTarget|����:ȡ��ϵͳ�˵����ӷ�ʽ </p>
	 * @return
	 */
	public String getLinkTarget(){
		return linkTarget;
	}

	/**
	 * <p>��������: setLinkTarget|����:����ϵͳ�˵����ӷ�ʽ </p>
	 * @param linkTarget
	 */
	public void setLinkTarget(String linkTarget){
		this.linkTarget = linkTarget;
	}

	/**
	 * <p>��������: getLinkSiteUrl|����:ȡ��ϵͳ�˵����ӵ�ַ </p>
	 * @return
	 */
	public String getLinkSiteUrl(){
		return linkSiteUrl;
	}

	/**
	 * <p>��������: setLinkSiteUrl|����:����ϵͳ�˵����ӵ�ַ </p>
	 * @param linkSiteUrl
	 */
	public void setLinkSiteUrl(String linkSiteUrl){
		this.linkSiteUrl = linkSiteUrl;
	}

	/**
	 * <p>��������: getUnitLoginUrl|����: ȡ��ϵͳ�˵�ͳһ��¼���ӵ�ַ</p>
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
	 * <p>��������: setUnitLoginUrl|����:����ϵͳ�˵�ͳһ��¼���ӵ�ַ </p>
	 * @param unitLoginUrl
	 */
	public void setUnitLoginUrl(String unitLoginUrl){
		this.unitLoginUrl = unitLoginUrl;
	}

	/**
	 * <p>��������: getDisplay|����:ȡ���Ƿ���ʾ </p>
	 * @return
	 */
	public String getDisplay(){
		return display;
	}

	/**
	 * <p>��������: setDisplay|����:�����Ƿ���ʾ </p>
	 * @param display
	 */
	public void setDisplay(String display){
		this.display = display;
	}

	/**
	 * <p>��������: getResDbType|����:ȡ����Դ���ݿ����� </p>
	 * @return
	 */
	public String getResDbType(){
		return resDbType;
	}

	/**
	 * <p>��������: setResDbType|����:������Դ���ݿ����� </p>
	 * @param resDbType
	 */
	public void setResDbType(String resDbType){
		this.resDbType = resDbType;
	}

	/**
	 * <p>��������: getResDbUserId|����:ȡ����Դ���ݿ��û��� </p>
	 * @return
	 */
	public String getResDbUserId(){
		return resDbUserId;
	}

	/**
	 * <p>��������: setResDbUserId|����:������Դ���ݿ��û��� </p>
	 * @param resDbUserId
	 */
	public void setResDbUserId(String resDbUserId){
		this.resDbUserId = resDbUserId;
	}

	/**
	 * <p>��������: getResDbServerPort|����:ȡ����Դ�������˿� </p>
	 * @return
	 */
	public String getResDbServerPort(){
		return resDbServerPort;
	}

	/**
	 * <p>��������: setResDbServerPort|����:������Դ�������˿� </p>
	 * @param resDbServerPort
	 */
	public void setResDbServerPort(String resDbServerPort){
		this.resDbServerPort = resDbServerPort;
	}

	/**
	 * <p>��������: getResDbServerIp|����:ȡ����Դ������IP </p>
	 * @return
	 */
	public String getResDbServerIp(){
		return resDbServerIp;
	}

	/**
	 * <p>��������: setResDbServerIp|����:������Դ������IP </p>
	 * @param resDbServerIp
	 */
	public void setResDbServerIp(String resDbServerIp){
		this.resDbServerIp = resDbServerIp;
	}

	/**
	 * <p>��������: getResDbSid|����:ȡ����Դ���ݿ��ʶ </p>
	 * @return
	 */
	public String getResDbSid(){
		return resDbSid;
	}

	/**
	 * <p>��������: setResDbSid|����:������Դ���ݿ��ʶ </p>
	 * @param resDbSid
	 */
	public void setResDbSid(String resDbSid){
		this.resDbSid = resDbSid;
	}

	/**
	 * <p>��������: getResDbPassword|����:ȡ����Դ���ݿ����� </p>
	 * @return
	 */
	public String getResDbPassword(){
		return resDbPassword;
	}

	/**
	 * <p>��������: setResDbPassword|����: ������Դ���ݿ�����</p>
	 * @param resDbPassword
	 */
	public void setResDbPassword(String resDbPassword){
		this.resDbPassword = resDbPassword;
	}

	/**
	 * <p>��������: getEnabled|����:ȡ��ϵͳ�Ƿ���ñ�ʶ�� </p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>��������: setEnabled|����:����ϵͳ�Ƿ���ñ�ʶ�� </p>
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
