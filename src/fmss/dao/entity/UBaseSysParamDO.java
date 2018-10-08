package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-7-10 ����02:00:49
 * @����: [UBaseSysParamDO]���ڴ˼�Ҫ������Ĺ���
 */
public class UBaseSysParamDO extends BaseDO implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** ����id */
	private Integer paramId;

	/** ��ϵͳid */
	private String systemId;

	/** ������� */
	private String type;

	/** �������˵�� */
	private String typeDesc;

	/** ������Ӣ�� */
	private String itemEname;

	/** ���������� */
	private String itemCname;

	/** ������ֵ */
	private String selectedValue;

	/** ������ֵ�б� */
	private String valueList;

	/** �Ƿ���޸� */
	private String isMofify;

	/** �Ƿ�ɼ� */
	private String isVisible;
	
	/** �����ֶ� */
	private Integer orderNum;

	/**
	 * @return paramId
	 */
	public Integer getParamId(){
		return paramId;
	}

	/**
	 * @param paramId Ҫ���õ� paramId
	 */
	public void setParamId(Integer paramId){
		this.paramId = paramId;
	}

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
	 * @return type
	 */
	public String getType(){
		return type;
	}

	/**
	 * @param type Ҫ���õ� type
	 */
	public void setType(String type){
		this.type = type;
	}

	/**
	 * @return typeDesc
	 */
	public String getTypeDesc(){
		return typeDesc;
	}

	/**
	 * @param typeDesc Ҫ���õ� typeDesc
	 */
	public void setTypeDesc(String typeDesc){
		this.typeDesc = typeDesc;
	}

	/**
	 * @return itemEname
	 */
	public String getItemEname(){
		return itemEname;
	}

	/**
	 * @param itemEname Ҫ���õ� itemEname
	 */
	public void setItemEname(String itemEname){
		this.itemEname = itemEname;
	}

	/**
	 * @return itemCname
	 */
	public String getItemCname(){
		return itemCname;
	}

	/**
	 * @param itemCname Ҫ���õ� itemCname
	 */
	public void setItemCname(String itemCname){
		this.itemCname = itemCname;
	}

	/**
	 * @return selectedValue
	 */
	public String getSelectedValue(){
		return selectedValue;
	}

	/**
	 * @param selectedValue Ҫ���õ� selectedValue
	 */
	public void setSelectedValue(String selectedValue){
		this.selectedValue = selectedValue;
	}

	/**
	 * @return valueList
	 */
	public String getValueList(){
		return valueList;
	}

	/**
	 * @param valueList Ҫ���õ� valueList
	 */
	public void setValueList(String valueList){
		this.valueList = valueList;
	}

	/**
	 * @return isMofify
	 */
	public String getIsMofify(){
		return isMofify;
	}

	/**
	 * @param isMofify Ҫ���õ� isMofify
	 */
	public void setIsMofify(String isMofify){
		this.isMofify = isMofify;
	}

	/**
	 * @return isVisible
	 */
	public String getIsVisible(){
		return isVisible;
	}

	/**
	 * @param isVisible Ҫ���õ� isVisible
	 */
	public void setIsVisible(String isVisible){
		this.isVisible = isVisible;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	
}

