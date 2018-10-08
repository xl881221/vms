package fmss.common.ui.controller;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明:<br>
 * 创建时间: 2008-8-25 上午10:18:49<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryClmForm implements Comparable, Serializable {
	private static final long serialVersionUID = 7991552226614088158L;
	private String tableId;

	private int colPostion;

	private String tableName;

	private String tableClmName;

	private String tableDialectName;

	private String tableClmDes;

	private String tableClmType;

	private String tableClmValue;

	private String tableClmRule;

	private String tableTypeMark;

	private Boolean isDisplay = new Boolean(true);

	private Boolean isCondtion = new Boolean(false);
	
	private Boolean isCondtionforDate=new Boolean(false);

	private Boolean isSumItemByGroup = new Boolean(false);

	private Boolean isGroup = new Boolean(false);

	private Boolean groupBy = new Boolean(false);

	private String flmMsg = "";// 存储 此列应用的公式信息

	private String func;

	private String filter;

	private String orderby;

	private String orderType = "";
	// 条件存储的值 仅当使用方案时才用到
	private String coditionValue;
	// 条件存储的伪码 仅当使用方案时才用到
	private String coditionValueCode;
	/**
	 * 分组数目
	 */
	private int groupCount;
	/**
	 * 标识此列是否被设置过自定义属性
	 */
	private boolean isCustomAttr = false;
	/**
	 * 记录当前列是否为 自定义列
	 */
	private boolean isCustom = false;
	/**
	 * 记录当前列是否为 可编辑的
	 */
	private boolean isEditor = false;

	private ColumnDisplay columnDisplay;
	
	/**
	 * 过滤条件开始值
	 */
	private String filterStartValue;
	/**
	 * 过滤条件结束值
	 */
	private String filterEndValue;
	/**
	 * 过滤条件枚举值
	 */
	private String filterEnumValue;
	/**
	 * 过滤条件表达式值
	 */
	private String filterExpressionValue;
	/**
	 * 过滤条件聚合类型
	 */
	private String filterComTypeValue;
	
	private String getValueFlm;//存储 此列的取值公式
	
	/**
	 * 查询条件的类型
	 */
	private String conditionType = "";
	/**
	 * 当查询条件的类型为数据库表时，存储SQL语句
	 */
	private String conditionDataTableSql = "";
	/**
	 * 当查询条件的类型为数据库表时，存储值列名
	 */
	private String conditionDataTableValueColName = "";
	/**
	 * 当查询条件的类型为数据库表时，存储显示列名
	 */
	private String conditionDataTableDisplayColName = "";
	/**
	 * 当查询条件的类型为自定义枚举时，存储枚举数据
	 */
	private String conditioncustomEnumeratedData = "";
	/**
	 * 单位类型
	 */
	private String unitType = "";
	
	private CalcItem calcItem;
	
	public String getFilterStartValue() {
		return filterStartValue;
	}

	public void setFilterStartValue(String filterStartValue) {
		this.filterStartValue = filterStartValue;
	}

	public String getFilterEndValue() {
		return filterEndValue;
	}

	public void setFilterEndValue(String filterEndValue) {
		this.filterEndValue = filterEndValue;
	}

	public String getFilterEnumValue() {
		return filterEnumValue;
	}

	public void setFilterEnumValue(String filterEnumValue) {
		this.filterEnumValue = filterEnumValue;
	}

	public String getFilterExpressionValue() {
		return filterExpressionValue;
	}

	public void setFilterExpressionValue(String filterExpressionValue) {
		this.filterExpressionValue = filterExpressionValue;
	}

	public String getFilterComTypeValue() {
		return filterComTypeValue;
	}

	public void setFilterComTypeValue(String filterComTypeValue) {
		this.filterComTypeValue = filterComTypeValue;
	}

	public ColumnDisplay getColumnDisplay() {
		if (columnDisplay == null){
			this.columnDisplay = new ColumnDisplay();
			columnDisplay.setColumnKey(this.tableClmName);
			columnDisplay.setColumnType(this.tableClmType);
		}
		return columnDisplay;
	}

	public void setColumnDisplay(ColumnDisplay columnDisplay) {
		this.columnDisplay = columnDisplay;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public boolean isCustom() {
		return isCustom;
	}

	public void setCustom(boolean isCustom) {
		this.isCustom = isCustom;
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

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getTableTypeMark() {
		return tableTypeMark;
	}

	public void setTableTypeMark(String tableTypeMark) {
		this.tableTypeMark = tableTypeMark;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableClmName() {
		return tableClmName;
	}

	public void setTableClmName(String tableClmName) {
		this.tableClmName = tableClmName;
	}

	public String getTableClmDes() {
		return tableClmDes;
	}

	public void setTableClmDes(String tableClmDes) {
		this.tableClmDes = tableClmDes;
	}

	public String getTableClmType() {
		return tableClmType;
	}

	public void setTableClmType(String tableClmType) {
		this.tableClmType = tableClmType;
	}

	public String getTableClmValue() {
		return tableClmValue;
	}

	public void setTableClmValue(String tableClmValue) {
		this.tableClmValue = tableClmValue;
	}

	public String getTableClmRule() {
		return tableClmRule;
	}

	public void setTableClmRule(String tableClmRule) {
		this.tableClmRule = tableClmRule;
	}

	public String getTableDialectName() {
		try {
			return AnalyzingUtil.getDialect(tableName, this.tableTypeMark);
		} catch (AnalyzingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setTableDialectName(String tableDialectName) {
		this.tableDialectName = tableDialectName;
	}

	public int getGroupCount() {
		if (StringUtils.isNotBlank(orderby)) {
			String[] array = orderby.split(",");
			if (array.length > 1) {
				return Integer.parseInt(array[0]);
			}
		}
		return -1;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public int compareTo(Object o) {
		return (new Integer(this.getGroupCount())).compareTo(new Integer(
				((FreeQueryClmForm) o).getGroupCount()));
	}

	public String getOrderType() {
		String thisType = "";
		if (StringUtils.isNotBlank(orderby)) {
			String[] array = orderby.split(",");
			if (array.length > 1) {
				thisType = array[1];
			}
		}
		return thisType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

	public Boolean getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}

	public String getFunc() {
		if (StringUtils.isEmpty(func)) {
			return "number".equals(tableClmType) ? "sum" : "max";
		}
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public int getColPostion() {
		return colPostion;
	}

	public void setColPostion(int colPostion) {
		this.colPostion = colPostion;
	}

	public boolean isNumber() {
		return AnalyseCst.NUMBER.equals(tableClmType) ? true : false;
	}

	public Boolean getIsSumItemByGroup() {
		return isSumItemByGroup;
	}

	public void setIsSumItemByGroup(Boolean isSumItemByGroup) {
		this.isSumItemByGroup = isSumItemByGroup;
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

	public boolean isCustomAttr() {
		return isCustomAttr;
	}

	public void setCustomAttr(boolean isCustomAttr) {
		this.isCustomAttr = isCustomAttr;
	}

	public String getGetValueFlm() throws AnalyzingException {
		return getCalcItem().getKeyValueFlm();
	}

	public void setGetValueFlm(String getValueFlm) {
		this.getValueFlm = getValueFlm;
	}

	public CalcItem getCalcItem() {
		if (calcItem == null){
			this.calcItem = new CalcItem(this.tableClmName,this.tableClmType,AnalyseCst.ROW);
		}
		return calcItem;
	}

	public void setCalcItem(CalcItem calcItem) {
		this.calcItem = calcItem;
	}

	public Boolean getIsCondtionforDate() {
		return isCondtionforDate;
	}

	public void setIsCondtionforDate(Boolean isCondtionforDate) {
		this.isCondtionforDate = isCondtionforDate;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getConditionDataTableSql() {
		return conditionDataTableSql;
	}

	public void setConditionDataTableSql(String conditionDataTableSql) {
		this.conditionDataTableSql = conditionDataTableSql;
	}

	public String getConditionDataTableValueColName() {
		return conditionDataTableValueColName;
	}

	public void setConditionDataTableValueColName(
			String conditionDataTableValueColName) {
		this.conditionDataTableValueColName = conditionDataTableValueColName;
	}

	public String getConditionDataTableDisplayColName() {
		return conditionDataTableDisplayColName;
	}

	public void setConditionDataTableDisplayColName(
			String conditionDataTableDisplayColName) {
		this.conditionDataTableDisplayColName = conditionDataTableDisplayColName;
	}

	public String getConditioncustomEnumeratedData() {
		return conditioncustomEnumeratedData;
	}

	public void setConditioncustomEnumeratedData(
			String conditioncustomEnumeratedData) {
		this.conditioncustomEnumeratedData = conditioncustomEnumeratedData;
	}

	public String getUnitType() {
		return null == unitType? "" : unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

}
