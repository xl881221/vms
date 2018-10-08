package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 一个单元的值 可能的原始类型:数值，字符串，日期
 * 
 * @author litao
 * 
 */
public abstract class CellValue implements Comparable, Cloneable {
	
	private boolean isSumCell = false;
	
	public static final String CELL_VALUE_TYPE_INTEGER = "Integer";

	public static final String CELL_VALUE_TYPE_NUMERIC = "Numeric";

	public static final String CELL_VALUE_TYPE_STRING = "String";

	public List conditionList = new ArrayList();
	/**
	 * 存储当前CELL中的公式信息 仅仅当该列为自定义列时候才出现
	 */
	private String flmMsg = "";
	
	public String getFlmMsg() {
		return flmMsg;
	}


	public void setFlmMsg(String flmMsg) {
		this.flmMsg = flmMsg;
	}


	public boolean isExist() {
		return this != null;
	}
	//标识 此CELL 是否是客户化的
	private boolean isCustomCell = false;
	//标识 此CELL 当前是否可以被编辑
	private boolean isEditor = false;
	
	public boolean isEditor() {
		return isEditor;
	}


	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}


	public String getConditionKey() {
		return FreeQueryUtil.bulidKeyByThisList(conditionList);
	}


	/**
	 * Object param must be Double/Integer/Long/String. TODO use more OO 's
	 * method.
	 * 
	 * @param obj
	 * @return CellValue
	 * @author xieli
	 * @throws FormulaDefineException
	 */
	public static CellValue getDynaTypeCellValue(Object o)
			throws Exception {
		CellValue cv = null;
		if (o instanceof Double) {
			if (Double.isInfinite(((Double) o).doubleValue())) {
				// must at once terminate the thread.
				// throw new RuntimeException("公式发生除零错误.请检查公式计算值.");
				return new NumericValue(0);
			}
			if (Double.isNaN((((Double) o).doubleValue()))) {
				// must at once terminate the thread.
				// throw new RuntimeException("报表数据错误.请检查公式设置是否错误,或手输入的值有错误.");
				return new NumericValue(0);
			}
			cv = new NumericValue(((Double) o).doubleValue());
		} else if (o instanceof Integer) {
			// will all use the NumericValue for the DB's design.
			cv = new NumericValue(((Integer) o).intValue());
		} else if (o instanceof String) {
			cv = new StringValue((String) o);
		} else if (o instanceof Long) {
			cv = new NumericValue(((Long) o).longValue());
		} else if (o instanceof Boolean) {
			throw new Exception("公式计算结果返回布尔值是非法的,请检查当前计算点公式");
		} else {
			throw new Exception(
					"Object type is unsuported by CellValue.getDyanaTypeCellValue()");
		}
		return cv;
	}

	protected String TYPE_NAME;

	private boolean alreadyBeRead = false;

	private String editableType = "";

	private String excelCellFormatStr = "General";

	private String originTypeName = TYPE_NAME;

	private Map refrenceMap = null;

	protected String colKey;
	
	private int unitValue = 1;

	public abstract Date getDateValue() throws DataTypeException;

	public String getEditableType() {
		return editableType;
	}

	public String getExcelCellFormatStr() {
		return excelCellFormatStr;
	}

	public abstract int getIntegerValue() throws DataTypeException;

	public abstract double getNumericValue() throws DataTypeException;

	public String getOriginTypeName() {
		return originTypeName;
	}

	public Map getRefrenceMap() {
		return refrenceMap;
	}

	public abstract String getStringValue() throws DataTypeException;

	public abstract boolean isNull();
	
	public final String getTypeName() {
		return TYPE_NAME;
	}

	/**
	 * transfer all cellValue to NumericValue Type for calculate.
	 * 
	 * @return double
	 * @throws DataTypeException
	 */
	public double getUniformNumericValue() throws DataTypeException {
		// double rstNumeric = 0;
		// TODO refactor use subclass's method .JVM 's polymophysm machanism.
		String cellTypeName = getTypeName();// getOriginTypeName();//getTypeName();
		// TODO USE THE number replace the string .
		if (cellTypeName.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_NUMERIC)) {
			return getNumericValue();
			// Double.parseDouble(String.valueOf(getNumericValue()));
		}

		if (cellTypeName.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_INTEGER)) {
			return getIntegerValue();
			// Double.parseDouble(String.valueOf(getIntegerValue()));
		}

		if (cellTypeName.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_STRING)
				&& !StringUtil.isBlank(getValue().toString().trim())
				&& !StringUtil.isAlpha((getValue().toString()))
				&& !getValue().toString().equals("-")) {
			try {
				return Double.parseDouble(getStringValue());
			} catch (NumberFormatException e) {
				throw new RuntimeException("数据区有非数据类的非法字符" + e.getMessage());
			}

		} else if (cellTypeName
				.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_STRING)
				&& (getValue().toString().equalsIgnoreCase("null") || StringUtil
						.isBlank(getValue().toString().trim()))
				|| getValue().toString().equals("-")) {
			return 0;
		} else {
			throw new DataTypeException("double or int ", cellTypeName);
		}
		// return rstNumeric;
	}

	/**
	 * transfer all cellValue to StringValue Type.
	 * 
	 * @return StringValue
	 * @throws DataTypeException
	 */
	public StringValue getUniformStringValue() {
		StringValue sv = null;
		String cellTypeName = getTypeName();
		try {
			if (cellTypeName
					.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_INTEGER)) {
				sv = new StringValue(String.valueOf(getIntegerValue()));
			}
			if (cellTypeName
					.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_NUMERIC)) {
				sv = new StringValue(StringUtil.doubleToString(
						getNumericValue(), 6));
			}
			if (cellTypeName.equalsIgnoreCase(CellValue.CELL_VALUE_TYPE_STRING)) {
				sv = new StringValue(getStringValue());
			}
		} catch (DataTypeException e) {
			sv = new StringValue("0");
		}
		return sv;

	}

	public int getUnitValue() {
		return unitValue;
	}

	public abstract Object getValue();

	public abstract String getValueByString();

	public/* abstract */String getValueByString4View() {
		return getValueByString();
	}

	/**
	 * 为了处理合并单元格中包含数据的情况
	 * 
	 * @return
	 */
	public boolean isAlreadyBeRead() {
		return alreadyBeRead;
	}

	public void setAlreadyBeRead(boolean alreadyBeRead) {
		this.alreadyBeRead = alreadyBeRead;
	}

	public abstract void setDateValue(Date value) throws DataTypeException;

	public CellValue setEditableType(String editType) {
		this.editableType = editType;
		return this;
	}

	public void setExcelCellFormatStr(String excelCellFormatStr) {
		if (excelCellFormatStr == null) {
			this.excelCellFormatStr = "General";
		} else {
			this.excelCellFormatStr = excelCellFormatStr.trim();
		}
	}

	public abstract void setIntegerValue(int value) throws DataTypeException;
	
	public abstract boolean equals(Object o);
	
	public abstract void setNumericValue(double value) throws DataTypeException;

	public CellValue setOriginTypeName(String originTypeName) {
		this.originTypeName = originTypeName;
		return this;
	}

	public CellValue setRefrenceMap(Map refrenceMap) {
		this.refrenceMap = refrenceMap;
		return this;
	}

	public abstract void setStringValue(String value) throws DataTypeException;

	public CellValue setUnitValue(int unitValue) {
		this.unitValue = unitValue;
		return this;
	}

	public String toString() {
		return "[" + this.getValue() + "-" + this.TYPE_NAME + " ]" ;
		//return " value is " + this.getValue() + " type is" + this.TYPE_NAME;
	}

	public String toValueString() {
		return this.getValue().toString();
	}
	
	public String getColKey() {
		return colKey;
	}

	public void setColKey(String colKey) {
		this.colKey = colKey;
	}

	public boolean isSumCell() {
		return isSumCell;
	}

	public void setSumCell(boolean isSumCell) {
		this.isSumCell = isSumCell;
	}

	public List getConditionList() {
		return conditionList;
	}

	public void setConditionList(List conditionList) {
		this.conditionList = conditionList;
	}


	public boolean isCustomCell() {
		return isCustomCell;
	}


	public void setCustomCell(boolean isCustomCell) {
		this.isCustomCell = isCustomCell;
	}


}
