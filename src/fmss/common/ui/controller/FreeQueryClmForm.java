package fmss.common.ui.controller;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


/**
 * ��˵��:<br>
 * ����ʱ��: 2008-8-25 ����10:18:49<br>
 * 
 * @author �����<br>
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

	private String flmMsg = "";// �洢 ����Ӧ�õĹ�ʽ��Ϣ

	private String func;

	private String filter;

	private String orderby;

	private String orderType = "";
	// �����洢��ֵ ����ʹ�÷���ʱ���õ�
	private String coditionValue;
	// �����洢��α�� ����ʹ�÷���ʱ���õ�
	private String coditionValueCode;
	/**
	 * ������Ŀ
	 */
	private int groupCount;
	/**
	 * ��ʶ�����Ƿ����ù��Զ�������
	 */
	private boolean isCustomAttr = false;
	/**
	 * ��¼��ǰ���Ƿ�Ϊ �Զ�����
	 */
	private boolean isCustom = false;
	/**
	 * ��¼��ǰ���Ƿ�Ϊ �ɱ༭��
	 */
	private boolean isEditor = false;

	private ColumnDisplay columnDisplay;
	
	/**
	 * ����������ʼֵ
	 */
	private String filterStartValue;
	/**
	 * ������������ֵ
	 */
	private String filterEndValue;
	/**
	 * ��������ö��ֵ
	 */
	private String filterEnumValue;
	/**
	 * �����������ʽֵ
	 */
	private String filterExpressionValue;
	/**
	 * ���������ۺ�����
	 */
	private String filterComTypeValue;
	
	private String getValueFlm;//�洢 ���е�ȡֵ��ʽ
	
	/**
	 * ��ѯ����������
	 */
	private String conditionType = "";
	/**
	 * ����ѯ����������Ϊ���ݿ��ʱ���洢SQL���
	 */
	private String conditionDataTableSql = "";
	/**
	 * ����ѯ����������Ϊ���ݿ��ʱ���洢ֵ����
	 */
	private String conditionDataTableValueColName = "";
	/**
	 * ����ѯ����������Ϊ���ݿ��ʱ���洢��ʾ����
	 */
	private String conditionDataTableDisplayColName = "";
	/**
	 * ����ѯ����������Ϊ�Զ���ö��ʱ���洢ö������
	 */
	private String conditioncustomEnumeratedData = "";
	/**
	 * ��λ����
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
