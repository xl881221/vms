package fmss.common.ui.controller;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;



public class StringValue extends CellValue {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StringValue.class);

	public final static String THE_TOTAL_TEXT = "¡ïºÏ¼Æ¡ï";

	private String value;

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StringValue other = (StringValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public StringValue(String value) {
		this();
		if (value == null || "null".equals(value)) {
			value = "-";
		}
		this.value = value;
		TYPE_NAME = CellValue.CELL_VALUE_TYPE_STRING;
		setOriginTypeName(TYPE_NAME);
	}

	public StringValue(String value,String colKey) {
		this();
		if (value == null || "null".equals(value)) {
			value = "-";
		}
		this.value = value;
		this.colKey = colKey;
		TYPE_NAME = CellValue.CELL_VALUE_TYPE_STRING;
		setOriginTypeName(TYPE_NAME);
	}
	
	public StringValue() {
		TYPE_NAME = "String";
	}

	public Date getDateValue() throws DataTypeException {
		throw new DataTypeException("Date", TYPE_NAME);
	}

	public double getNumericValue() {
		try {
			return Double.parseDouble(getStringValue());
		} catch (NumberFormatException e) {
			return 0;
		}
		// throw new DataTypeException("Numeric", TYPE_NAME);
	}

	public String getStringValue() {
		return value;
	}

	public Object getValue() {
		return value;
	}

	
	
	public void setDateValue(Date value) throws DataTypeException {
		throw new DataTypeException("Date", TYPE_NAME);
	}

	public void setNumericValue(double value) throws DataTypeException {
		throw new DataTypeException("Numeric", TYPE_NAME);

	}

	public void setStringValue(String value) {
		this.value = value;
	}

	public String toString() {
		return "¡º" + value + "-" + this.TYPE_NAME + " ¡»" ;
		// return value + " NowType:" + this.TYPE_NAME + " InitType: " +
		// getOriginTypeName();
	}

	public String getValueByString() {
		return value;
	}

	public int getIntegerValue() throws DataTypeException {
		throw new DataTypeException("Integer", TYPE_NAME);
	}

	public void setIntegerValue(int value) throws DataTypeException {
		throw new DataTypeException("Integer", TYPE_NAME);

	}

	public int compareTo(Object o) {
		return ((String) this.getValue()).compareTo((String)((CellValue) o).getValue());
		// return ((String) ((CellValue)
		// o).getValue()).compareTo(this.getValue());
	}

	public String getValueByString4View() {
		if (this.value == null) {
			return "";
		} else if (this.value.startsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)
				&& this.value.endsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)) {
			return StringUtil.replaceStr(this.value, AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN, "");
		} else {
			return super.getValueByString4View();
		}
	}

	public Map getRefrenceMap() {
		if (this.value == null) {
			return null;
		} else if (this.value.startsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)
				&& this.value.endsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)) {
			return null;
		} else {
			return super.getRefrenceMap();
		}
	}

	public String getEditableType() {
		if (this.value == null) {
			return "N";
		} else if (this.value.startsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)
				&& this.value.endsWith(AnalyseCst.CRMS_SPECIAL_SPLIT_SIGN)) {
			return "N";
		} else {
			return super.getEditableType();
		}
	}

	public Object clone() throws CloneNotSupportedException {
		// super.clone();
		StringValue sv = new StringValue(this.value);
		try {
			BeanUtils.copyProperties(sv, this);
		} catch (IllegalAccessException e) {
			logger.error("clone()", e); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			logger.error("clone()", e); //$NON-NLS-1$
		}
		return sv;
	}

	public boolean isNull() {
		
		if(StringUtils.isBlank(value) || "-".equals(value)){
			return true;
		}
		return false;
	}

}
