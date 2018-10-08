package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 类说明:<br>
 * 创建时间: 2008-9-2 下午04:57:45<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class Row {

	private boolean isTotalRow;

	private String sumColKey; // 当前的合计列

	private CellValue sumCellValue;
	
	private List rowData = new ArrayList();
	
	private int rowNumber;
	
	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Row(List rowData, boolean isTotalRow) {
		this.isTotalRow = isTotalRow;
		this.rowData = rowData;
	}

	public List getRowData() {
		return rowData;
	}

	public void setRowData(List rowData) {
		this.rowData = rowData;
	}

	/**
	 * 当前行的数据列数
	 * 
	 * @return
	 */
	public int getCount() {
		return rowData.size();
	}

	public boolean contains(CellValue cv) {
		return rowData.contains(cv);
	}

	public List getAfterByKey(String key){
		List dataList = new ArrayList();
		if(getPostionByKey(key) !=-1){
			boolean isAfter = false;
			for (Iterator iterator = rowData.iterator(); iterator.hasNext();) {
				CellValue cv = (CellValue) iterator.next();
				if (key.equals(cv.getColKey())) {
					isAfter = true;
				}else if(isAfter){
					dataList.add(cv);
				}
			}
		}else{
			dataList.addAll(rowData);
		}
		return dataList;
	}
	
	public CellValue getCvByTarget(String target) {
		for (Iterator iterator = rowData.iterator(); iterator.hasNext();) {
			try {

				CellValue cv = (CellValue) iterator.next();
				if (target.equals(cv.getColKey())) {
					return cv;
				}

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List getCvThisAndBeforeList(String key) {
		List beforeList = new ArrayList();
		int postion = getPostionByKey(key);
		if(postion !=-1){
			int index = 0;
			while(index <= postion){
				CellValue cv = (CellValue)rowData.get(index);
				beforeList.add(cv);
				index ++;
			}
		}
		return beforeList;
	}
	
	public void sumRowData(List dataList) throws DataTypeException{
		List newList = new ArrayList();
		for (int i = 0; i < rowData.size(); i++) {
			CellValue cv = (CellValue) rowData.get(i);
			CellValue newCv = sumRow(dataList,cv);
			if(newCv!=null){
				newList.add(newCv);
			}else{
				newList.add(cv);
			}
		}
		this.setRowData(newList);
	}
	
	public CellValue sumRow(List dataList,CellValue oldCv) throws DataTypeException{
		for (int i = 0; i < dataList.size(); i++) {
			CellValue cv = (CellValue) dataList.get(i);
			if(oldCv.getColKey().equals(cv.getColKey())){
				CellValue newCv = null;
				if (cv instanceof NumericValue && oldCv instanceof NumericValue) {
					double d = cv.getNumericValue() + oldCv.getNumericValue();
					newCv = new NumericValue(d, cv.getColKey());
				} else if (cv instanceof StringValue) {
					if (isAfterSumColKey(cv.getColKey())) {
						newCv = new StringValue("&nbsp;", cv.getColKey());
					} else {
						newCv = oldCv;
					}
				}
				newCv.setCustomCell(cv.isCustomCell());
				newCv.setEditor(cv.isEditor());
				newCv.setFlmMsg(cv.getFlmMsg());
				return newCv;
			}
		}
		return null;
	}

	/**
	 * 根据参数中 key 值判断当前列是否在合计列之后 方法说明:<br>
	 * 创建时间: 2009-1-6 下午03:10:15<br>
	 */
	public boolean isAfterSumColKey(String key){
		if (isTotalRow && sumColKey != null) {
			int currentIndex = getPostionByKey(key);
			int sumIndex = getPostionByKey(sumColKey);
			return currentIndex > sumIndex && sumIndex != -1;
		} else {
			return false;
		}
	}

	public int getPostionByKey(String key) {
		for (int i = 0; i < rowData.size(); i++) {
			CellValue cv = (CellValue) rowData.get(i);
			if (key.equals(cv.getColKey())) {
				return i;
			}
		}
		return -1;
	}

	public boolean findByCondtion(Map map) throws DataTypeException {
		Set set = map.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Integer postion = (Integer) iterator.next();
			CellValue cellValue = (CellValue) rowData.get(postion.intValue());
			String value = (String) map.get(postion);
			if (cellValue instanceof StringValue) {
				if (value.equals(cellValue.getStringValue())) {
					return true;
				}
			} else if (cellValue instanceof NumericValue) {
				if (value.equals(String.valueOf(cellValue.getNumericValue()))) {
					return true;
				}
			} else {
				throw new DataTypeException("", "未知的数据类型!");
			}
		}
		return false;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer();
		for (Iterator iterator = rowData.iterator(); iterator.hasNext();) {
			CellValue cellValue = (CellValue) iterator.next();
			sbf.append(cellValue.toString());
		}
		return sbf.toString();
	}

	public boolean isTotalRow() {
		return isTotalRow;
	}

	public void setTotalRow(boolean isTotalRow) {
		this.isTotalRow = isTotalRow;
	}

	public String getSumColKey() {
		return sumColKey;
	}

	public void setSumColKey(String sumColKey) {
		this.sumColKey = sumColKey;
	}

	public CellValue getSumCellValue() {
		return sumCellValue;
	}

	public CellValue getSumCell() {
		for (Iterator iterator = rowData.iterator(); iterator.hasNext();) {
			CellValue cellValue = (CellValue) iterator.next();
			if(cellValue.isSumCell()){
				return cellValue;
			}
		}
		return new StringValue("合计");
	}	
	
	public void setSumCellValue(CellValue sumCellValue) {
		this.sumCellValue = sumCellValue;
	}

	
	public void addCustomCell(String key,String type,int postion,Object cellData){
		CellValue customCv;
		if("number".equals(type)){
			customCv = new NumericValue(((Double)cellData).doubleValue(),key);
		}else{
			customCv = new StringValue(((String)cellData),key);
		}
		customCv.setCustomCell(true);
		customCv.setEditor(true);
		rowData.add(postion-1, customCv);
	}
}
