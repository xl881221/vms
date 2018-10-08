package fmss.dao.entity;

import java.util.List;
import fmss.dao.entity.BaseDO;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����12:04:08
 * @����: [MenuDO]�˵�DO
 */
public class MenuDO extends BaseDO implements java.io.Serializable{

	private static final long serialVersionUID = 4308547504611152386L;// ���к�
	private java.lang.String itemcode;// ��Ŀ���
	private java.lang.String itemname;// ��Ŀ����
	private java.lang.String target;// Ŀ��λ��
	private java.lang.String url;// ���ӵ�ַ
	private java.lang.String innerUrl;// ���ӵ�ַ
	private java.lang.String imgsrc;// ͼƬλ��
	private Integer ordernum;// ����˳��
	private java.lang.String display;// ��ʾ��Ϣ
	private java.lang.String enabled;// �Ƿ����
	private List subMenuList = null;// �Ӳ˵���Ϣ
	private String systemId;// ��ϵͳ���
	private String menu_ename;//��ĿӢ������
	
	/**
	 * @return systemId
	 */
	public String getSystemId(){
		return systemId;
	}

	
	/**
	 * @param systemId Ҫ���õ� systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	/**
	 * <p>��������: setItemcode|����:���ò˵���Ŀ���� </p>
	 * @param itemcode
	 */
	public void setItemcode(java.lang.String itemcode){
		this.itemcode = itemcode;
	}

	/**
	 * <p>��������: getItemcode|����: ȡ�ò˵���Ŀ����</p>
	 * @return
	 */
	public java.lang.String getItemcode(){
		return itemcode;
	}

	/**
	 * <p>��������: setItemname|����:���ò˵���Ŀ���� </p>
	 * @param itemname
	 */
	public void setItemname(java.lang.String itemname){
		this.itemname = itemname;
	}

	/**
	 * <p>��������: getItemname|����:ȡ�ò˵���Ŀ���� </p>
	 * @return
	 */
	public java.lang.String getItemname(){
		return itemname;
	}

	/**
	 * <p>��������: setTarget|����:ȡ�ò˵�����λ�� </p>
	 * @param target
	 */
	public void setTarget(java.lang.String target){
		this.target = target;
	}

	/**
	 * <p>��������: getTarget|����: ȡ�ò˵�����λ��</p>
	 * @return
	 */
	public java.lang.String getTarget(){
		return target;
	}

	/**
	 * <p>��������: setUrl|����:���ò˵����ӵ�ַ </p>
	 * @param url
	 */
	public void setUrl(java.lang.String url){
		this.url = url;
	}

	/**
	 * <p>��������: getUrl|����: ȡ�ò˵����ӵ�ַ</p>
	 * @return
	 */
	public java.lang.String getUrl(){
		return url;
	}

	/**
	 * <p>��������: setImgsrc|����:����ͼƬ��ַ </p>
	 * @param imgsrc
	 */
	public void setImgsrc(java.lang.String imgsrc){
		this.imgsrc = imgsrc;
	}

	/**
	 * <p>��������: getImgsrc|����: ȡ��ͼƬ��ַ</p>
	 * @return
	 */
	public java.lang.String getImgsrc(){
		return imgsrc;
	}

	/**
	 * <p>��������: setDisplay|����:���ò˵���ʾ��Ϣ </p>
	 * @param display
	 */
	public void setDisplay(java.lang.String display){
		this.display = display;
	}

	/**
	 * <p>��������: getDisplay|����: ȡ�ò˵���ʾ��Ϣ</p>
	 * @return
	 */
	public java.lang.String getDisplay(){
		return display;
	}

	/**
	 * <p>��������: setEnabled|����: ���ò˵��Ƿ�����</p>
	 * @param enabled
	 */
	public void setEnabled(java.lang.String enabled){
		this.enabled = enabled;
	}

	/**
	 * <p>��������: getEnabled|����: �жϲ˵��Ƿ�����</p>
	 * @return
	 */
	public java.lang.String getEnabled(){
		return enabled;
	}

	/**
	 * <p>��������: getOrdernum|����: ȡ�����</p>
	 * @return
	 */
	public Integer getOrdernum(){
		return ordernum;
	}

	/**
	 * <p>��������: setOrdernum|����:���ò˵���ʾ��� </p>
	 * @param ordernum
	 */
	public void setOrdernum(Integer ordernum){
		this.ordernum = ordernum;
	}

	/**
	 * <p>��������: getSubMenuList|����:ȡ�ò˵��б� </p>
	 * @return
	 */
	public List getSubMenuList(){
		return subMenuList;
	}

	/**
	 * <p>��������: setSubMenuList|����:���ò˵��б� </p>
	 * @param subMenuList
	 */
	public void setSubMenuList(List subMenuList){
		this.subMenuList = subMenuList;
	}


	public String getMenu_ename() {
		return menu_ename;
	}


	public void setMenu_ename(String menu_ename) {
		this.menu_ename = menu_ename;
	}


	public java.lang.String getInnerUrl() {
		return innerUrl;
	}


	public void setInnerUrl(java.lang.String innerUrl) {
		this.innerUrl = innerUrl;
	}
	
}