package fmss.common.ui.controller;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明: 用于标识列显示属性<br>
 * 创建时间: 2009-2-16 下午02:55:44<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ColumnDisplay {
	
	private String columnKey;
	private String columnType;
	private String columnBgColor;
	private String columnDisplayType;
	
	private String ruleStr; //存储显示规则的字符串
	
	private List ruleList;


	public List getRuleList() {

		return ruleList;
		
	}

	public void setRuleList(List ruleList) {
		this.ruleList = ruleList;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnBgColor() {
		return columnBgColor;
	}

	public void setColumnBgColor(String columnBgColor) {
		this.columnBgColor = columnBgColor;
	}

	public String getColumnDisplayType() {
		if (StringUtils.isBlank(columnDisplayType)
				&& AnalyseCst.NUMBER.equals(this.columnType)) {
			return AnalyseCst.DOUBLE;
		}
		if (StringUtils.isBlank(columnDisplayType)
				&& AnalyseCst.STRING.equals(this.columnType)) {
			return AnalyseCst.CHAR;
		}

		return columnDisplayType;
	}

	public void setColumnDisplayType(String columnDisplayType) {
		this.columnDisplayType = columnDisplayType;
	}
	
	public boolean isDisplayImage(){
		
		if(ruleList!=null){
			if(ruleList.size()>0){
				return true;
			}
		}
		return false;
	}
	
	public String getImage(CellValue cv){
		for (Iterator iterator = ruleList.iterator(); iterator.hasNext();) {
			Rule rule = (Rule) iterator.next();
			if(rule.isEq(cv)){
				return rule.getImg();
			}
		}
		return "";
	}

	public String getRuleStr() {
		return ruleStr;
	}

	public void setRuleStr(String ruleStr) {
		this.ruleStr = ruleStr;
	}

}
