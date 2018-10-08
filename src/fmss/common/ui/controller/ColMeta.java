package fmss.common.ui.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明:<br>
 * 创建时间: 2008-9-2 下午04:57:52<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ColMeta implements Comparable{
	
	public ColMeta(String key,String name,int position){
		this.key = key;
		this.name = name;
		this.position = position;
	}
	/**
	 * 是否是 自定义的 meta
	 */
	private boolean isCustom = false;
	/**
	 * 是否是 可编辑的 
	 */	
	private boolean isEditor = false;
	/**
	 * 行索引
	 */
	private String key;
	/**
	 * 列名称
	 */
	private String name;
	/**
	 * 行位置
	 */
	private int position;
	/**
	 * 列是否分组
	 * @return
	 */
	private Boolean isGroup = new Boolean(false);

	private boolean isNumber;
	/**
	 * 列是否显示
	 */
	private Boolean isDisplay = new Boolean(true);
	/**
	 * 列是否做为条件
	 */
	private Boolean isCondtion = new Boolean(false);
	
	private Boolean isSumItemByGroup;
	/**
	 * 规则
	 */
	private String tableClmRule;
	/**
	 * 做为条件时 允许输入的值
	 */
	private String coditionValue="";
	//条件存储的伪码 仅当使用方案时才用到
	private String coditionValueCode="";
	/**
	 * 做为条件时 允许输入的值
	 */
	private List tableClmValues;
	/**
	 * 字段类型
	 */
	private int groupCount;
	/**
	 * 是否应用GROUP BY 子句
	 */
	private Boolean groupBy;
	/**
	 * 合计函数
	 */	
	private String func;
	
	private String flmMsg = "";//存储 此列应用的公式信息
	
	private String getValueFlm;//存储 此列的取值公式
	
	private String tableClmType; //string number date

	private ColumnDisplay columnDisplay;
	
	private String unitType;
	
	/**
	 * 标识此列是否被设置过自定义属性
	 */
	private boolean isCustomAttr = false;
	
	public boolean isCustomAttr() {
		return isCustomAttr;
	}
	public void setCustomAttr(boolean isCustomAttr) {
		this.isCustomAttr = isCustomAttr;
	}
	public String getTableClmRule() {
		return tableClmRule;
	}
	public void setTableClmRule(String tableClmRule) {
		this.tableClmRule = tableClmRule;
	}
	public List getTableClmValues() {
		return tableClmValues;
	}
	public void setTableClmValues(List tableClmValues) {
		this.tableClmValues = tableClmValues;
	}
	public String getTableClmType() {
		return tableClmType;
	}
	public void setTableClmType(String tableClmType) {
		this.tableClmType = tableClmType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Boolean getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	public Boolean getIsCondtion() {
		return isCondtion;
	}
	public void setIsCondtion(Boolean isCondtion) {
		this.isCondtion = isCondtion;
	}
	public int getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	public int compareTo(Object o) {
		return (new Integer(this.getGroupCount())).compareTo(new Integer(((ColMeta) o).getGroupCount()));
	}
	public String getCoditionValue() {
		return coditionValue;
	}
	public void setCoditionValue(String coditionValue) {
		this.coditionValue = coditionValue;
	}
	public String getCoditionValueCode() {
		return coditionValueCode;
	}
	public void setCoditionValueCode(String coditionValueCode) {
		this.coditionValueCode = coditionValueCode;
	}
	public boolean isNumber() {
		return "number".equals(tableClmType)?true:false;
	}
	public void setNumber(boolean isNumber) {
		this.isNumber = isNumber;
	}
	public Boolean getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public Boolean getIsSumItemByGroup() {
		if(isGroup.booleanValue()){
			return isSumItemByGroup;
		}else{
			return new Boolean(false);
		}
	}
	public void setIsSumItemByGroup(Boolean isSumItemByGroup) {
		this.isSumItemByGroup = isSumItemByGroup;
	}

	public boolean isCustom() {
		return isCustom;
	}
	public void setCustom(boolean isCustom) {
		this.isCustom = isCustom;
	}
	public boolean isEditor() {
		return isEditor;
	}
	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}
	public String getFlmMsg() {
		return flmMsg;
	}
	public void setFlmMsg(String flmMsg) {
		this.flmMsg = flmMsg;
	}
	public ColumnDisplay getColumnDisplay() {

		return columnDisplay;
	}
	public void setColumnDisplay(ColumnDisplay columnDisplay) {
		this.columnDisplay = columnDisplay;
	}
	public String getGetValueFlm() {
		return getValueFlm;
	}
	public void setGetValueFlm(String getValueFlm) {
		this.getValueFlm = getValueFlm;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

}
