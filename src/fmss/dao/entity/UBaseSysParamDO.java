package fmss.dao.entity;

import java.io.Serializable;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-7-10 下午02:00:49
 * @描述: [UBaseSysParamDO]请在此简要描述类的功能
 */
public class UBaseSysParamDO extends BaseDO implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** 参数id */
	private Integer paramId;

	/** 子系统id */
	private String systemId;

	/** 参数类别 */
	private String type;

	/** 参数类别说明 */
	private String typeDesc;

	/** 参数项英文 */
	private String itemEname;

	/** 参数项中文 */
	private String itemCname;

	/** 参数项值 */
	private String selectedValue;

	/** 参数项值列表 */
	private String valueList;

	/** 是否可修改 */
	private String isMofify;

	/** 是否可见 */
	private String isVisible;
	
	/** 排序字段 */
	private Integer orderNum;

	/**
	 * @return paramId
	 */
	public Integer getParamId(){
		return paramId;
	}

	/**
	 * @param paramId 要设置的 paramId
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
	 * @param systemId 要设置的 systemId
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
	 * @param type 要设置的 type
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
	 * @param typeDesc 要设置的 typeDesc
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
	 * @param itemEname 要设置的 itemEname
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
	 * @param itemCname 要设置的 itemCname
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
	 * @param selectedValue 要设置的 selectedValue
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
	 * @param valueList 要设置的 valueList
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
	 * @param isMofify 要设置的 isMofify
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
	 * @param isVisible 要设置的 isVisible
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

