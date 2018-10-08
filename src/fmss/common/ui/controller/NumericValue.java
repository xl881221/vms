package fmss.common.ui.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;


public class NumericValue extends CellValue {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(NumericValue.class);

	private double value;

	public NumericValue() {
		TYPE_NAME = CELL_VALUE_TYPE_NUMERIC;
	}

	public NumericValue(double value) {
		this();
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			logger.warn("当前计算值发生数据计算异常.产生了非法数.系统将默认返回0.请检查相关公式或取数据运算逻辑有误.");
			this.value = 0;
		} else {
			this.value = value;
		}
		TYPE_NAME = CellValue.CELL_VALUE_TYPE_NUMERIC;
		setOriginTypeName(TYPE_NAME);
	}

	public NumericValue(double value,String colKey) {
		this();
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			logger.warn("当前计算值发生数据计算异常.产生了非法数.系统将默认返回0.请检查相关公式或取数据运算逻辑有误.");
			this.value = 0;
			this.colKey = colKey;
		} else {
			this.value = value;
			this.colKey = colKey;
		}
		TYPE_NAME = CellValue.CELL_VALUE_TYPE_NUMERIC;
		setOriginTypeName(TYPE_NAME);
	}
	
	public Date getDateValue() throws DataTypeException {
		throw new DataTypeException("Date", TYPE_NAME);
	}

	public double getNumericValue() {
		return value;
	}

	public String getStringValue() throws DataTypeException {
		throw new DataTypeException("String", TYPE_NAME);
	}

	public Object getValue() {
		return new Double(value);
	}

	public void setDateValue(Date value) throws DataTypeException {
		throw new DataTypeException("Date", TYPE_NAME);
	}

	public void setNumericValue(double value) {
		if (value == Double.NaN) {
			this.value = 0;
		} else {
			this.value = value;
		}
	}

	public void setStringValue(String value) throws DataTypeException {
		throw new DataTypeException("String", TYPE_NAME);
	}

	public String toString() {
		return "『" + String.valueOf(value) + "-" + this.TYPE_NAME + " 』" ;
		// getOriginTypeName();
		// return value + " NowType:" + this.TYPE_NAME + " InitType: " +
		// getOriginTypeName();
	}

	public String getValueByString() {
		return String.valueOf(value);
	}

	public int getIntegerValue() throws DataTypeException {
		throw new DataTypeException("Integer", TYPE_NAME);
	}

	public void setIntegerValue(int value) throws DataTypeException {
		throw new DataTypeException("Integer", TYPE_NAME);

	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = PRIME * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final NumericValue other = (NumericValue) obj;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}

	public int compareTo(Object o) {
		// return ((Double) this.getValue()).compareTo(((CellValue)
		// o).getValue());
		return ((Double) ((CellValue) o).getValue()).compareTo((Double)this.getValue());

	}

	public Object clone() throws CloneNotSupportedException {
		NumericValue nv = new NumericValue(this.value);
		try {
			BeanUtils.copyProperties(nv, this);
		} catch (IllegalAccessException e) {
			logger.error("clone()", e); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			logger.error("clone()", e); //$NON-NLS-1$
		}
		return nv;
	}
	
	public boolean isNull() {
		
		if(value==0){
			return true;
		}
		return false;
	}
}
