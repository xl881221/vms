/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:04:25
 * @����: [UBaseSysLogDO]ϵͳ��־��
 */
public class UBaseSysLogDO extends BaseDO implements Serializable{

	private long logId;// LOG_ID ��־���
	private String userId;// USER_ID �û����
	private String userEname;// USER_ENAME �û���¼��
	private String userCname;// USER_CNAME �û�����
	private String instId;// INST_ID �������
	private String instCname;// INST_CNAME ��������
	private String menuId;// MENU_ID ��Ŀ���
	private String menuName;// MENU_NAME ��Ŀ����
	private String ip;// IP �û�IP
	private String browse;// BROWSE �û������
	private String logType;// LOG_TYPE ��־����--00001--�û���¼��־
	private Timestamp execTime;// EXEC_TIME ִ��ʱ��
	private String description;// DESCRIPTION ����
	private String status;// ����״̬
	private String systemId;// ��ϵͳ���
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * <p>��������: getLogId|����:ȡ����־��� </p>
	 * @return
	 */
	public long getLogId(){
		return logId;
	}

	/**
	 * <p>��������: setLogId|����:������־��� </p>
	 * @param logId
	 */
	public void setLogId(long logId){
		this.logId = logId;
	}

	/**
	 * <p>��������: getUserId|����:ȡ���û���� </p>
	 * @return
	 */
	public String getUserId(){
		return userId;
	}

	/**
	 * <p>��������: setUserId|����:�����û���� </p>
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	 * <p>��������: getUserEname|����:ȡ���û�Ӣ������ </p>
	 * @return
	 */
	public String getUserEname(){
		return userEname;
	}

	/**
	 * <p>��������: setUserEname|����:�����û�Ӣ������ </p>
	 * @param userEname
	 */
	public void setUserEname(String userEname){
		this.userEname = userEname;
	}

	/**
	 * <p>��������: getUserCname|����:ȡ���û���������</p>
	 * @return
	 */
	public String getUserCname(){
		return userCname;
	}

	/**
	 * <p>��������: setUserCname|����:ȡ���û�Ӣ������ </p>
	 * @param userCname
	 */
	public void setUserCname(String userCname){
		this.userCname = userCname;
	}

	/**
	 * <p>��������: getInstId|����: ȡ�û������</p>
	 * @return
	 */
	public String getInstId(){
		return instId;
	}

	/**
	 * <p>��������: setInstId|����:���û������ </p>
	 * @param instId
	 */
	public void setInstId(String instId){
		this.instId = instId;
	}

	/**
	 * <p>��������: getInstCname|����:ȡ�û����������� </p>
	 * @return
	 */
	public String getInstCname(){
		return instCname;
	}

	/**
	 * <p>��������: setInstCname|����:���û����������� </p>
	 * @param instCname
	 */
	public void setInstCname(String instCname){
		this.instCname = instCname;
	}

	/**
	 * <p>��������: getMenuId|����:ȡ����Ŀ��� </p>
	 * @return
	 */
	public String getMenuId(){
		return menuId;
	}

	/**
	 * <p>��������: setMenuId|����:������Ŀ��� </p>
	 * @param menuId
	 */
	public void setMenuId(String menuId){
		this.menuId = menuId;
	}

	/**
	 * <p>��������: getMenuName|����:ȡ����Ŀ���� </p>
	 * @return
	 */
	public String getMenuName(){
		return menuName;
	}

	/**
	 * <p>��������: setMenuName|����:������Ŀ���� </p>
	 * @param menuName
	 */
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	/**
	 * <p>��������: getIp|����: ȡ��IP��Ϣ</p>
	 * @return
	 */
	public String getIp(){
		return ip;
	}

	/**
	 * <p>��������: setIp|����:����IP </p>
	 * @param ip
	 */
	public void setIp(String ip){
		this.ip = ip;
	}

	/**
	 * <p>��������: getBrowse|����: ȡ���������Ϣ</p>
	 * @return
	 */
	public String getBrowse(){
		return browse;
	}

	/**
	 * <p>��������: setBrowse|����:�����������Ϣ </p>
	 * @param browse
	 */
	public void setBrowse(String browse){
		this.browse = browse;
	}

	/**
	 * <p>��������: getLogType|����:ȡ����־���� </p>
	 * @return
	 */
	public String getLogType(){
		return logType;
	}

	/**
	 * <p>��������: setLogType|����:������־���� </p>
	 * @param logType
	 */
	public void setLogType(String logType){
		this.logType = logType;
	}

	/**
	 * <p>��������: getExecTime|����:ȡ��ִ��ʱ�� </p>
	 * @return
	 */
	public Timestamp getExecTime(){
		return execTime;
	}

	/**
	 * <p>��������: setExecTime|����:����ִ��ʱ�� </p>
	 * @param execTime
	 */
	public void setExecTime(Timestamp execTime){
		this.execTime = execTime;
	}

	/**
	 * <p>��������: getDescription|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>��������: setDescription|����: ����������Ϣ</p>
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
