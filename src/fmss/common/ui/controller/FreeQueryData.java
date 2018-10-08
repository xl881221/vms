package fmss.common.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 类说明:报表数据 //由行列组成的数据存储结构<br>
 * 创建时间: 2008-9-2 下午01:39:03<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryData extends QueryData implements Cloneable, Serializable {

	private int startIndex = 0;

	private int endIndex;

	private boolean isDisplayTotal;

	private static final long serialVersionUID = 7991552226614088458L;

	private List rows = new LinkedList();// 报表行信息定义及数据

	private List colsMetas = new ArrayList();// 报表列信息定义

	private Map columnMetasMap = new HashMap();

	private Map totalValueMap = new HashMap();
	
	private Map groupModelMap = new HashMap();
	
	public Map getGroupModelMap() {
		return groupModelMap;
	}

	public void setGroupModelMap(Map groupModelMap) {
		this.groupModelMap = groupModelMap;
	}

	public Map getColumnMetasMap() {
		return columnMetasMap;
	}

	public void setColumnMetasMap(Map columnMetasMap) {
		this.columnMetasMap = columnMetasMap;
	}

	// 加入cell
	public void addRow(Row row) {
		if (row == null) {
			return;
		}
		// 获取当前行的columnMeta 信息

		rows.add(row);
	}

	public void clearRow(){
		rows.clear();
	}
	
	public void addColMeta(ColMeta colMeta) {
		if (colMeta == null)
			return;
		colsMetas.add(colMeta);
	}

	/**
	 * 得到要显示的列数
	 * 
	 * @return
	 */
	public int getColCountDisplay() {
		int count = 0;
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			boolean isDisplay = colMeta.getIsDisplay().booleanValue();
			if (isDisplay)
				count++;
		}
		return count;
	}

	/**
	 * 根据列名取出整列数据
	 * 
	 * @param key
	 * @return
	 */
	public List getColValueList(String key) {

		List colValueList = new ArrayList();
		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			List data = row.getRowData();
			for (Iterator iterator2 = data.iterator(); iterator2.hasNext();) {
				CellValue cellValue = (CellValue) iterator2.next();
				if (key.equals(cellValue.getColKey())) {
					colValueList.add(cellValue);
				}
			}
		}
		return colValueList;
	}

	public void effectGroupArea() {
		for (Iterator iterator = groupModelMap.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			FreeQueryGroupModel fq = (FreeQueryGroupModel) groupModelMap.get(key);
			Row row = (Row)totalValueMap.get(key);
			fq.setTotalRow(row);
			int startRow = fq.getStartRow() + 1;
			int endRow = startRow + fq.getTotalCount() - 1;
			fq.setModStartRow(startRow);
			fq.setModEndRow(endRow);
		}
	}
	
	
	public FreeQueryData getDataByColumnValue(String column, String value)
			throws DataTypeException {
		FreeQueryData qd = new FreeQueryData();
		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			if (findByCondtion(column, value, row)) {
				qd.addRow(row);
			}
		}
		qd.setColsMetas(this.colsMetas);
		return qd;
	}

	/**
	 * 根据指定的参数值获取目标列的数据
	 * 
	 * @param paramMap
	 *            {key : columnMetaName value: value}
	 * @param targetColumn
	 * @return
	 * @throws DataTypeException
	 */
	public List getColumnByCondtionTarget(Map paramMap, String targetColumn)
			throws DataTypeException {
		FreeQueryData qd = getDataByParams(paramMap);
		return qd.getColValueList(targetColumn);
	}

	public FreeQueryData getDataByParams(Map paramMap) throws DataTypeException {
		FreeQueryData qd = new FreeQueryData();
		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			Set set = paramMap.keySet();
			boolean select = false;
			for (Iterator iterator1 = set.iterator(); iterator1.hasNext();) {
				String column = (String) iterator1.next();
				if (findByCondtion(column, (String) paramMap.get(column), row)) {
					select = true;
				} else {
					select = false;
					break;
				}
			}
			if (select) {
				qd.addRow(row);
			}
		}
		qd.setColsMetas(this.colsMetas);
		return qd;
	}

	public FreeQueryData getDataByColumnValue(String column, String value,
			String column1, String value1) throws DataTypeException {
		FreeQueryData qd = new FreeQueryData();
		for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			if (findByCondtion(column, value, row)
					&& findByCondtion(column1, value1, row)) {
				qd.addRow(row);
			}
		}
		qd.setColsMetas(this.colsMetas);
		return qd;
	}

	public boolean findByCondtion(String column, String value, Row row)
			throws DataTypeException {
		Map paramsMap = new HashMap();
		for (int i = 0; i < colsMetas.size(); i++) {
			ColMeta colMeta = (ColMeta) colsMetas.get(i);
			if (column.equals(colMeta.getKey())) {
				paramsMap.put(new Integer(i), value);
			}
		}
		return row.findByCondtion(paramsMap);
	}

	public Set getColGroupSet(String colName) {
		Set set = new HashSet();
		set.addAll(getColValueList(colName));
		return set;
	}



	public void setColsMetas(List colsMetas) {
		this.colsMetas = colsMetas;
	}

	public List getColsMetas() {
		return colsMetas;
	}

	/**
	 * 获取当前colmeta所有数据项列
	 * 
	 * @return
	 */
	public List getColsMetasIsNumber() {
		List list = new LinkedList();
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			if (colMeta.isNumber()) {
				list.add(colMeta);
			}
		}
		return list;
	}

	/**
	 * 获取当前colmeta所有描述项列
	 * 
	 * @return
	 */
	public List getColsMetasIsDes() {
		List list = new LinkedList();
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			if (!colMeta.isNumber()) {
				list.add(colMeta);
			}
		}
		return list;
	}

	public List getRows() {
		return rows;
	}

	public Row getRowObject(int rowNum) {
		for (int i = 1; i <= rows.size(); i++) {
			Row row = (Row) rows.get(i-1);
			if(rowNum == i){
				return row;
			}			
		}
		return null;
	}
	
	/**
	 * 当前QueryData 是否需要设置条件
	 * 
	 * @return
	 */
	public boolean hasCondition() {
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			boolean isDisplay = colMeta.getIsCondtion().booleanValue();
			if (isDisplay)
				return true;
		}
		return false;
	}

	public ColMeta getColMetaByKey(String key) {
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			if (key.equals(colMeta.getKey())) {
				return colMeta;
			}
		}
		return null;
	}

	/**
	 * sort x,y 's regular order to output.avoid Map 's confused order. only for
	 * calcEngine 's debug by xieli
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer();
		// TODO friendly show . list.get(0) --no 1 element.
		for (Iterator iterator = this.rows.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			sb.append(row.toString());
			sb.append("\n");
		}
		return sb.toString();
	}




	public int getEndIndex() {
		return rows.size();
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public boolean isDisplayTotal() {
		return isDisplayTotal;
	}

	public void setDisplayTotal(boolean isDisplayTotal) {
		this.isDisplayTotal = isDisplayTotal;
	}

	public ColMeta getMetaByPostion(int metaPostion) {
		for (int i = 0; i < colsMetas.size(); i++) {
			ColMeta colMeta = (ColMeta) colsMetas.get(i);
			if (metaPostion == colMeta.getPosition()) {
				return colMeta;
			}
		}
		return null;
	}
	
	public int getMaxColumnPostion(){
		List temp = new ArrayList();
		for (Iterator iterator = colsMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			temp.add(new Integer(colMeta.getPosition()));
		}
		Integer num = (Integer)Collections.max(temp);
		return num.intValue();
	}
	
	public void setCustomMeta(ColMeta colMeta){
		colsMetas.add(colMeta);
		columnMetasMap.put(colMeta.getKey(), colMeta);
	}
	
	public void addCustomColumn(String metaKey,String metaType,String model,int columnPostion,List data){
		if(AnalyseCst.CUSTOM_MODEL_BLANK.equals(model)){
			Object value;
			if("number".equals(metaType)){
				value = new Double(0.00);
			}else{
				value = new String("");
			}
			for (Iterator iterator = this.rows.iterator(); iterator.hasNext();) {
				Row row = (Row) iterator.next();
				row.addCustomCell(metaKey, metaType, columnPostion, value);
			}
			
			for (Iterator iterator = groupModelMap.values().iterator(); iterator.hasNext();) {
				FreeQueryGroupModel fq = (FreeQueryGroupModel)iterator.next();
				
				Row totalRow = fq.getTotalRow();
				totalRow.addCustomCell(metaKey, metaType, columnPostion, value);
			}
		}
	}
	
	public ColMeta getMetaByKey(String metaKey) {
		for (int i = 0; i < colsMetas.size(); i++) {
			ColMeta colMeta = (ColMeta) colsMetas.get(i);
			if (metaKey.equals(colMeta.getKey())) {
				return colMeta;
			}
		}
		return null;
	}
	public Map getTotalValueMap() {
		return totalValueMap;
	}

	public void setTotalValueMap(Map totalValueMap) {
		this.totalValueMap = totalValueMap;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
}
