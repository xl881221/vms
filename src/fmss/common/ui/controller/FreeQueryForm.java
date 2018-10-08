/**
 * 
 */
package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;





/**
 * 自由查询对象
 * @author liuhaibo
 *
 */
public class FreeQueryForm {
	
	private String id;
	private String schemeCode;
	private String schemeType;
	private String schemeName;
	private String purviewDes;
	private String subjectId;
	private String purviewId;
	private String subjectDes;
	private String schemeDes;
	private String unit;
	private String sql;
	private List conditionList = new ArrayList();
	private List selectedColumnSet = new ArrayList();
	private List selectedColumn = new ArrayList();
	private Operation operation;
	
	private	String	schemeDate;//方案创建日期
	
	private	String	factTableId;//事实表ID
	
	private	String	factTableName;//事实表名
	
	private	String	factTableDes;//事实表描述
	
	private	String	schemeCreator;//方案创建者
	
	private boolean isNullForm;
	
	private String previousPage;

	/**
	 * 访问域为部分时对应的用户
	 */
	private String purviewUserList;
	/**
	 * 访问域为部分时是否设置对象的标识
	 */
	private String partUserPurview;
	
	private List graphChartList= new ArrayList();
	
	private Map dimsTablesMap;


	private List selectedFactRelation; // list in FreeQuerySelFRForm Object map
	// to selectedFactRelationJson


	private List allColumn;



	private List selectedColumnSetC;

	private String allColumnJson;

	private String selectedFactRelationJson;

	private String selectedColumnJson;

	private String selectedColumnSetJson;

	private String selectedColumnSetCJson;
	
	private boolean canSave=true;

	/*
	 * 小数位
	 */
	private String decimalDigits;

	private String relClms;
	
	public List getGraphChartList() {
		return graphChartList;
	}
	public void setGraphChartList(List graphChartList) {
		this.graphChartList = graphChartList;
	}
	public List getSelectedColumn() {
		return selectedColumn;
	}
	public void setSelectedColumn(List selectedColumn) {
		this.selectedColumn = selectedColumn;
	}
	public List getSelectedColumnSet() {
		return selectedColumnSet;
	}
	public void setSelectedColumnSet(List selectedColumnSet) {
		this.selectedColumnSet = selectedColumnSet;
	}
	public List getConditionList() {
		List selectColumns = getCurrentClmListIsSelect();
		List resultL = new ArrayList();
		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if (freeQueryClmForm.getIsCondtion().booleanValue()) {
				resultL.add(freeQueryClmForm);
			}
		}
		return resultL;
	}
	/**
	 * 获取当前使用的字段列表 方法说明:<br>
	 * 创建时间: 2008-12-24 下午03:52:43<br>
	 */
	public List getCurrentClmListIsSelect() {
		if (selectedColumnSet != null) {
			if (!selectedColumnSet.isEmpty()) {
				return selectedColumnSet;
			} else {
				return selectedColumn;
			}
		}
		return new LinkedList();
	}
	public void setConditionList(List conditionList) {
		this.conditionList = conditionList;
	}
	public String getSchemeName() {
		return schemeName;
	}
	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	public String getPurviewDes() {
		return purviewDes;
	}
	public void setPurviewDes(String purviewDes) {
		this.purviewDes = purviewDes;
	}
	public String getSubjectDes() {
		return subjectDes;
	}
	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}
	public String getSchemeDes() {
		return schemeDes;
	}
	public void setSchemeDes(String schemeDes) {
		this.schemeDes = schemeDes;
	}
	public String getUnit() {
		if(null == unit || "".equals(unit)){
			return "10000";
		}
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getPurviewId() {
		return purviewId;
	}
	public void setPurviewId(String purviewId) {
		this.purviewId = purviewId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getSchemeType() {
		return schemeType;
	}
	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}
	public String getSchemeDate() {
		return schemeDate;
	}
	public void setSchemeDate(String schemeDate) {
		this.schemeDate = schemeDate;
	}
	public String getFactTableId() {
		return factTableId;
	}
	public void setFactTableId(String factTableId) {
		this.factTableId = factTableId;
	}
	public String getFactTableName() {
		return factTableName;
	}
	public void setFactTableName(String factTableName) {
		this.factTableName = factTableName;
	}
	public String getFactTableDes() {
		return factTableDes;
	}
	public void setFactTableDes(String factTableDes) {
		this.factTableDes = factTableDes;
	}
	public String getSchemeCreator() {
		return schemeCreator;
	}
	public void setSchemeCreator(String schemeCreator) {
		this.schemeCreator = schemeCreator;
	}
	public boolean isNullForm() {
		return isNullForm;
	}
	public void setNullForm(boolean isNullForm) {
		this.isNullForm = isNullForm;
	}
	public String getPreviousPage() {
		return previousPage;
	}
	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}
	public String getPurviewUserList() {
		return purviewUserList;
	}
	public void setPurviewUserList(String purviewUserList) {
		this.purviewUserList = purviewUserList;
	}
	public String getPartUserPurview() {
		return partUserPurview;
	}
	public void setPartUserPurview(String partUserPurview) {
		this.partUserPurview = partUserPurview;
	}
	public Map getDimsTablesMap() {
		return dimsTablesMap;
	}
	public void setDimsTablesMap(Map dimsTablesMap) {
		this.dimsTablesMap = dimsTablesMap;
	}
	public List getSelectedFactRelation() {
		return selectedFactRelation;
	}
	public void setSelectedFactRelation(List selectedFactRelation) {
		this.selectedFactRelation = selectedFactRelation;
	}
	public List getAllColumn() {
		return allColumn;
	}
	public void setAllColumn(List allColumn) {
		this.allColumn = allColumn;
	}
	public List getSelectedColumnSetC() {
		return selectedColumnSetC;
	}
	public void setSelectedColumnSetC(List selectedColumnSetC) {
		this.selectedColumnSetC = selectedColumnSetC;
	}
	public String getAllColumnJson() {
		return allColumnJson;
	}
	public void setAllColumnJson(String allColumnJson) {
		this.allColumnJson = allColumnJson;
	}
	public String getSelectedFactRelationJson() {
		return selectedFactRelationJson;
	}
	public void setSelectedFactRelationJson(String selectedFactRelationJson) {
		this.selectedFactRelationJson = selectedFactRelationJson;
	}
	public String getSelectedColumnJson() {
		return selectedColumnJson;
	}
	public void setSelectedColumnJson(String selectedColumnJson) {
		this.selectedColumnJson = selectedColumnJson;
	}
	public String getSelectedColumnSetJson() {
		return selectedColumnSetJson;
	}
	public void setSelectedColumnSetJson(String selectedColumnSetJson) {
		this.selectedColumnSetJson = selectedColumnSetJson;
	}
	public String getSelectedColumnSetCJson() {
		return selectedColumnSetCJson;
	}
	public void setSelectedColumnSetCJson(String selectedColumnSetCJson) {
		this.selectedColumnSetCJson = selectedColumnSetCJson;
	}
	public boolean isCanSave() {
		return canSave;
	}
	public void setCanSave(boolean canSave) {
		this.canSave = canSave;
	}
	public String getRelClms() {
		return relClms;
	}
	public void setRelClms(String relClms) {
		this.relClms = relClms;
	}
	public String getDecimalDigits() {
		if(null == decimalDigits || "".equals(decimalDigits))
			return "4";
		return decimalDigits;
	}
	public void setDecimalDigits(String decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	
	public Object clone() throws CloneNotSupportedException{
		FreeQueryForm o=null;
		o=(FreeQueryForm) super.clone();
		return o;
	}
	
	/**
	 * 获取所选择的字段列表 未选择返回null
	 * 
	 * @param orderedList
	 * @param noorderedList
	 * @return
	 */
	public boolean getIsSelectColumn() {
		if (selectedColumn.isEmpty() && selectedColumnSetC.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 返回当前字段列表
	 * 
	 * @param form
	 * @return
	 */
	public List generateAllClmList() {
		boolean isSelClm = getIsSelectColumn();
		if (!isSelClm) {
			// 如果为选择任何查询字段 那么查询所有维表及事实表
			return allColumn;
		} else {
			return getCurrentClmListIsSelect();
		}
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	/**
	 * 方法说明:在FORM 模型中添加自定义列<br>
	 * 创建时间: 2009-2-9 下午01:13:38<br>
	 */
	public void setCustomColumn(List currentClmListIsSelect, ColumnPostion cp,
			String customColumnMetaKey, String customColumnMetaName,
			String customColumnMetaType) {
		FreeQueryClmForm fqc = new FreeQueryClmForm();
		if (cp.isEnd()) {

			fqc.setTableClmName(customColumnMetaKey);
			fqc.setTableClmDes(customColumnMetaName);
			fqc.setTableClmType(customColumnMetaType);
			fqc.setCustom(true);
			fqc.setEditor(true);
		}
		currentClmListIsSelect.add(fqc);
	}
	
	public void setAllClmNotEdit() {
		List list = generateAllClmList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			freeQueryClmForm.setEditor(false);
		}

	}

}
