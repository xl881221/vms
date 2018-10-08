package fmss.common.ui.controller;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明:<br>
 * 创建时间: 2008-9-5 上午11:31:48<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class QueryDataToHtml {
	private static Map map = new HashMap();

	static {
		map.put(new BigDecimal("1"), "元");
		map.put(new BigDecimal("10000"), "万元");
		map.put(new BigDecimal("100000"), "拾万元");
		map.put(new BigDecimal("1000000"), "百万元");
		map.put(new BigDecimal("10000000"), "千万元");
		map.put(new BigDecimal("100000000"), "亿元");
	}
	
	private String currencyUnit;
	private int digit = 4;
	private String getCurrencyUnitName(){
		if(null == currencyUnit || "".equals(currencyUnit))
			return "";
		BigDecimal currencyUnitValue = new BigDecimal(currencyUnit);
		Object obj = map.get(currencyUnitValue);
		if( null == obj )
			return "";
		return String.valueOf( obj );
	}
	private void buildColumnName(ColMeta colMeta, StringWriter sw){
		sw.write(colMeta.getName());
		if("number".equals(colMeta.getTableClmType() ) ){
			if(AnalyseCst.CURRENCY.equals(colMeta.getUnitType())){
				String currencyUnitName = getCurrencyUnitName();
				if( null != currencyUnitName && !"".equals(currencyUnitName)){
					sw.write("（" + currencyUnitName +"）");
				}
			}
		}
	}
	
	public String toHtml(FreeQueryData queryViewData, String dataTitle, String currencyUnit, String decimalDigits)
			throws DataTypeException {
		if (queryViewData == null)
			return "";
		if (queryViewData.getRows().size() < 1)
			return "";
		if( null != currencyUnit && !"".equals(currencyUnit)){
			this.currencyUnit = currencyUnit;
		}
		
		if(null != decimalDigits && !"".equals(decimalDigits)){
			this.digit = new Integer(decimalDigits).intValue();
		}
		
		StringWriter sw = new StringWriter();
		List cols = queryViewData.getColsMetas();
		Map metasMap = queryViewData.getColumnMetasMap();
		int colspanCount = queryViewData.getColCountDisplay();
		sw.write("<table id=\"reportData\" border=0  cellpadding=0 cellspacing=0 onclick=\"javascript:selectCell(event);\">");
		bulidHeadCount(colspanCount, sw);
		int rowNumber = 1;
		buildTitle(colspanCount, dataTitle, sw, rowNumber++);
		// bulid condition
		if (queryViewData.hasCondition()) {
			buildCondition(cols, colspanCount, sw, rowNumber++);
		}
		// bulid head
		int headRow = buildHead(cols, sw, rowNumber++) + 1;
		int leftCol = bulidLeftCol(sw);
		List tempTotalDisplay = new ArrayList();
		for (int i = 0; i < queryViewData.getRows().size(); i++) {
			Row row = (Row) queryViewData.getRows().get(i);
			generateRow(row, metasMap, sw, i, rowNumber++, queryViewData
					.getGroupModelMap(), tempTotalDisplay);

			for (int j = tempTotalDisplay.size() - 1; j >= 0; j--) {
				Row totalRow = (Row) tempTotalDisplay.get(j);
				generateRow(totalRow, metasMap, sw, i, rowNumber++,
						queryViewData.getGroupModelMap(), null);
				tempTotalDisplay.remove(totalRow);
			}

		}

		sw.write("</table>");
		sw.write("<script type=\"text/javascript\">");
		sw.write("var headRow=" + headRow + ",leftCol=" + leftCol);
		sw.write("</script>");
		return sw.toString();
	}

	private void bulidHeadCount(int colspanCount, StringWriter sw) {
		sw.write("<tr>");
		for (int i = 0; i < colspanCount + 1; i++) {
			String temp = i == 0 ? "&nbsp;" : String.valueOf(i);
			sw.write("<td class=\"head_index\">" + temp + "</td>");
		}
		sw.write("</tr>");
	}

	/**
	 * 构建 表条件 方法说明:<br>
	 * 创建时间: 2009-1-9 上午10:01:24<br>
	 */
	private void buildCondition(List cols, int spanCount, StringWriter sw,
			int rowCount) {
		sw.write("<tr>");
		sw.write("<td class=\"head_index\">" + String.valueOf(rowCount)
				+ "</td>");
		sw.write("<th id=\"condition\" colspan=" + String.valueOf(spanCount)
				+ ">");
		for (Iterator iterator = cols.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			if (colMeta.getIsCondtion().booleanValue()) {
				String value = "";
				if (colMeta.getCoditionValueCode() != null) {
					value = colMeta.getCoditionValueCode();
				}
				sw.write(colMeta.getName() + ":<input name=\"filter"
						+ colMeta.getKey() + "\" type=\"text\" value=\""
						+ value + "\"/>");
			}
		}
		sw.write("</th>");
		sw.write("</tr>");
	}

	/**
	 * 方法说明:构建 表 title<br>
	 * 创建时间: 2009-1-9 上午10:01:04<br>
	 */
	public void buildTitle(int colCount, String dataTitle, StringWriter sw,
			int rowCount) {
		if (dataTitle != null) {
			sw.write("<tr>");
			sw.write("<td class=\"head_index\">" + String.valueOf(rowCount)
					+ "</td>");
			sw.write("<th id=\"title\" colspan=" + String.valueOf(colCount)
					+ ">");
			sw.write(dataTitle.toString());
			sw.write("</th>");
			sw.write("</tr>");
		}
	}

	/**
	 * 方法说明:构建head<br>
	 * 
	 * @param colMetas
	 *            包含列信息定义的list,sw 输出流,rowCount 行号 创建时间: 2009-1-9 上午09:57:02<br>
	 */
	public int buildHead(List colMetas, StringWriter sw, int rowCount) {
		sw.write("<tr>");
		sw.write("<td class=\"head_index\">" + String.valueOf(rowCount)
				+ "</td>");
		for (Iterator iterator = colMetas.iterator(); iterator.hasNext();) {
			ColMeta colMeta = (ColMeta) iterator.next();
			String postion = String.valueOf(colMeta.getPosition());
			String type = colMeta.getTableClmType();
			String isEditor = colMeta.isEditor() ? "true" : "false";
			String isCustom = colMeta.isCustom() ? "true" : "false";
			String isGroup = colMeta.getIsGroup().booleanValue() ? "true"
					: "false";
			String isCustomAttr = "false";
			String thClass = "thHead";

			if (colMeta.isCustomAttr()) {
				thClass = thClass + " thAttr";
				isCustomAttr = "true";
			}

			thClass = colMeta.isCustomAttr() ? thClass + " thAttr" : thClass;

			if (colMeta.getIsDisplay().booleanValue()) {
				sw.write("<th class = \"" + thClass + "\" postion=\"" + postion
						+ "\" type=\"" + type + "\" isEdit = \"" + isEditor
						+ "\"" + "key = \"" + colMeta.getKey()
						+ "\" isGroup = \"" + isGroup + "\" isCustom=\""
						+ isCustom + "\" isCustomAttr=\""+isCustomAttr+"\"/>");
				buildColumnName(colMeta, sw);
				//sw.write(colMeta.getName());
				sw.write("</th>");
			}
		}
		sw.write("</tr>");
		return rowCount;
	}

	/**
	 * 方法说明: 构建行html
	 * 
	 * @param row
	 *            行数据对象, metasMap,列信息定义map,sw 输出流,i 行索引 ,rowCount 行号
	 * @author yangxufei 创建时间: 2009-1-9 上午09:55:03<br>
	 * @throws DataTypeException
	 */
	private void generateRow(Row row, Map metasMap, StringWriter sw, int i,
			int rowCount, Map groupCountMap, List tempTotalDisplay)
			throws DataTypeException {
		String rowStyle = row.isTotalRow() ? "font-weight: bold;" : "";
		sw.write("<tr style=\"" + rowStyle + "\">");
		sw.write("<td class=\"head_index\">" + String.valueOf(rowCount)
				+ "</td>");
		boolean isTotalRow = row.isTotalRow();
		String sumCellString = "";
		if (isTotalRow) {
			sumCellString = row.getSumCellValue().toValueString();
		}
		List rowList = row.getRowData();
		for (int j = 0; j < rowList.size(); j++) {
			CellValue cellValue = (CellValue) row.getRowData().get(j);

			String key = cellValue.getColKey();
			ColMeta colMeta = (ColMeta) metasMap.get(key);

			if (colMeta.getIsDisplay().booleanValue()) {

				if (colMeta.getIsGroup().booleanValue() && !row.isTotalRow()) {
					Object object = groupCountMap.get(cellValue
							.getConditionKey());
					if (object != null) {
						FreeQueryGroupModel fqgm = (FreeQueryGroupModel) object;
						int count = fqgm.getTotalCount();
						if (!fqgm.isHasDipsplay()) {
							if (fqgm.isHasChild()) {
								int tempCount = buildChildCount(fqgm
										.getChildSet(), groupCountMap, metasMap);
								count = count + tempCount;
							}
							fqgm.setCurrentStartRow(i);
							fqgm.setHasDipsplay(true);
							generateCol(cellValue, sw, count, colMeta,
									sumCellString);
						}
						int endCount = fqgm.getCurrentStartRow()
								+ fqgm.getTotalCount();
						if (i == endCount - 1) {
							if (colMeta.getIsSumItemByGroup().booleanValue()) {
								tempTotalDisplay.add(fqgm.getTotalRow());
							}
						}
					}
				} else {
					if (!row.isTotalRow()) {
						generateCol(cellValue, sw, colMeta, sumCellString);
					} else {
						if (!isBeforeGroup(colMeta, row, key, cellValue)) {
							generateCol(cellValue, sw, colMeta, sumCellString);
						}
					}

				}
			}
		}
		sw.write("</tr>");
	}

	private int buildChildCount(Set childSet, Map allGroupAreaMap, Map metasMap) {
		int count = 0;
		// List tempCurrentList = new LinkedList(currentList);
		for (Iterator iterator = childSet.iterator(); iterator.hasNext();) {
			// CellValue cv = (CellValue) iterator.next();
			// String cvKey = cv.getColKey();
			// ColMeta cvMeta = (ColMeta)metasMap.get(cvKey);
			// if(cvMeta.getIsSumItemByGroup().booleanValue()){
			// count ++;
			// }
			// tempCurrentList.add(cv);
			// String key = FreeQueryUtil.bulidKeyByThisList(tempCurrentList);
			String key = (String) iterator.next();
			Object object = allGroupAreaMap.get(key);
			if (object != null) {
				FreeQueryGroupModel fqgm = (FreeQueryGroupModel) object;
				Set tempChildSet = fqgm.getChildSet();
				if (fqgm.getThisMeta().getIsSumItemByGroup().booleanValue()
						&& fqgm.getThisMeta().getIsDisplay().booleanValue()) {
					count++;
				}
				if (fqgm.isHasChild()) {
					int tempCount = buildChildCount(tempChildSet,
							allGroupAreaMap, metasMap);
					count = count + tempCount;
				}
			}
		}
		return count;
	}

	private boolean isBeforeGroup(ColMeta colMeta, Row row, String key,
			CellValue cv) {
		if (!row.isAfterSumColKey(key) && !cv.isSumCell()
				&& colMeta.getIsGroup().booleanValue()) {
			return true;
		}
		return false;
	}

	private int bulidLeftCol(StringWriter sw) {
		int leftCol = 1;
		return leftCol;
	}

	/**
	 * 方法说明:构建 html TD 此方法被用于不含合计列的 单元格
	 * 
	 * @param cellValue
	 *            单元格数值 ,sw 输出的 StringWriter, headBorder 表头的border postion 位置
	 *            sumCellString 合计行的合计项
	 * @author yangxufei 创建时间: 2009-1-9 上午09:51:31<br>
	 */
	private void generateCol(CellValue cellValue, StringWriter sw,
			ColMeta colMeta, String sumCellString) throws DataTypeException {
		boolean isSumCell = cellValue.isSumCell();
		String cellTitle = "";
		if (isSumCell) {
			cellTitle = "此行为:[" + sumCellString + "]的合计行";
		}
		if (StringUtil.isNotBlank(colMeta.getFlmMsg())
				&& cellValue.isCustomCell()) {
			cellTitle = cellTitle + "计算公式：" + colMeta.getFlmMsg();
		}
		if (cellValue instanceof NumericValue) {
			cellValue.isNull();
			double pcvd = NumberUtil.round(cellValue.getNumericValue(), 4);
			buildNumericCellHtml(cellValue, sw, 0, colMeta, pcvd, sumCellString);
		} else {
			buildStringCellHtml(cellValue, sw, 0, colMeta, sumCellString);
		}

	}

	/**
	 * 方法说明:构建 html TD 此方法被用于不含合计列的 单元格
	 * 
	 * @param cellValue
	 *            单元格数值 ,sw 输出的 StringWriter,count 合并数量 headBorder 表头的border
	 *            postion 位置 sumCellString 合计行的合计项
	 * @author yangxufei 创建时间: 2009-1-9 上午09:51:31<br>
	 */
	private void generateCol(CellValue cellValue, StringWriter sw, int count,
			ColMeta colMeta, String sumCellString) throws DataTypeException {

		if (cellValue instanceof NumericValue) {
			double pcvd = NumberUtil.round(cellValue.getNumericValue() / 10000,
					4);
			buildNumericCellHtml(cellValue, sw, count, colMeta, pcvd,
					sumCellString);
		} else {
			buildStringCellHtml(cellValue, sw, count, colMeta, sumCellString);
		}

	}

	private void buildStringCellHtml(CellValue cellValue, StringWriter sw,
			int count, ColMeta colMeta, String sumCellString) {
		String className = cellValue.isCustomCell() ? "character-custom"
				: "character";
		String isEditor = cellValue.isEditor() ? "true" : "false";
		String postion = String.valueOf(colMeta.getPosition());
		ColumnDisplay columnDisplay =  colMeta.getColumnDisplay();
		String cellBgStyle = "";
		if (columnDisplay != null) {
			cellBgStyle = columnDisplay.getColumnBgColor() != null ? "background-color:"
					+ columnDisplay.getColumnBgColor()
					: "";
		}
		String title = "";
		if (cellValue.isSumCell()) {
			title = "此行为:[" + sumCellString + "]的合计行";
		}
		if (StringUtil.isNotBlank(colMeta.getFlmMsg()) && cellValue.isCustomCell()) {
			title = title + "计算公式：" + colMeta.getFlmMsg();
		}
		sw.write("<td class=\"" + className + "\" rowspan="
						+ String.valueOf(count) + " postion=\"" + postion
						+ "\" cellType=\"string\" isEdit = \"" + isEditor
						+ "\" title=\"" + title + "\" style = \"" + cellBgStyle
						+ "\">");
		bulidStringCellValue(cellValue, sw, columnDisplay);
		sw.write("</td>");
	}

	private void buildNumericCellHtml(CellValue cellValue, StringWriter sw,
			int count, ColMeta colMeta, double pcvd, String sumCellString) {
		String className = cellValue.isCustomCell() ? "data-custom" : "data";
		String isEditor = cellValue.isCustomCell() ? "true" : "false";
		String postion = String.valueOf(colMeta.getPosition());
		ColumnDisplay columnDisplay = colMeta.getColumnDisplay();
		String cellBgStyle = "";
		if (columnDisplay != null) {
			cellBgStyle = columnDisplay.getColumnBgColor() != null ? "background-color:"
					+ columnDisplay.getColumnBgColor()
					: "";
		}

		String title = "";
		if (cellValue.isSumCell()) {
			title = "此行为:[" + sumCellString + "]的合计行";
		}
		if (StringUtil.isNotBlank(colMeta.getFlmMsg())
				&& cellValue.isCustomCell()) {
			title = title + "计算公式：" + colMeta.getFlmMsg();
		}
		sw.write("<td class=\"" + className + "\" rowspan="
						+ String.valueOf(count) + "  postion=\"" + postion
						+ "\" cellType=\"number\" isEdit = \"" + isEditor
						+ "\" title='" + title + "' style = \"" + cellBgStyle
						+ "\">");
		bulidNumericCellValue(cellValue, sw, pcvd, columnDisplay, colMeta.getUnitType());
		sw.write("</td>");
	}

	private void bulidStringCellValue(CellValue cellValue, StringWriter sw,
			ColumnDisplay columnDisplay) {
		String value = cellValue.getValue().toString();

		if (cellValue.isEditor()) {
			sw
					.write("<input type=\"text\" class=\"character-custom-input\" value="
							+ value + "  />");
		} else {
			sw.write(value);
		}

	}

	private void bulidNumericCellValue(CellValue cellValue, StringWriter sw,
			double pcvd, ColumnDisplay columnDisplay, String unitType ) {
		String value = StringUtil.doubleToStringNotEndWithZero(pcvd, this.digit);
		if( AnalyseCst.CURRENCY.equals( unitType) ){
			//单位换算
			if(null != this.currencyUnit && !"".equals(this.currencyUnit)){
				double dividend = new Double(this.currencyUnit).doubleValue();
				double dValue = pcvd/dividend;
				value = StringUtil.doubleToStringNotEndWithZero(dValue, this.digit);
			}
		}

		if (columnDisplay != null) {
			if (AnalyseCst.PERCENT.equals(columnDisplay.getColumnDisplayType())) {
				value = StringUtil.get_percent(pcvd, 2, "0.00%");
			}
			if (columnDisplay.isDisplayImage()) {
				String imgSrc = columnDisplay.getImage(cellValue);
				String imgTitle = "此处的值为：" + String.valueOf(pcvd);
				if (StringUtils.isNotBlank(imgSrc)) {
					value = "<img src=\"" + imgSrc + "\" title=\"" + imgTitle
							+ "\" />";
				}
			}
		} 

		if (cellValue.isEditor()) {
			sw
					.write("<input type=\"text\" size=\"100\" class=\"data-custom-input\" value="
							+ value + " />");
		} else {
			sw.write(value);
		}

	}
}
