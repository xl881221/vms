package fmss.common.ui.controller;

import java.io.Serializable;
import java.util.List;

/**
 * 类说明:<br>
 * 创建时间: 2008-8-21 下午01:05:54<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
/**
 * @author yangxufei
 *
 */
public class FreeQuerySelFRForm implements Serializable{
	private static final long serialVersionUID = 7291552226614088158L; 
	
	private String id;
	
	private String factTableName;
	
	private String factTableDes;
	
	private boolean isSetDim;
	
	private List jsonDimTablesStr;

	private List jsonRelTables;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public boolean isSetDim() {
		return isSetDim;
	}

	public void setSetDim(boolean isSetDim) {
		this.isSetDim = isSetDim;
	}

	public List getJsonDimTablesStr() {
		return jsonDimTablesStr;
	}

	public void setJsonDimTablesStr(List jsonDimTablesStr) {
		this.jsonDimTablesStr = jsonDimTablesStr;
	}

	public List getJsonRelTables() {
		return jsonRelTables;
	}

	public void setJsonRelTables(List jsonRelTables) {
		this.jsonRelTables = jsonRelTables;
	}
}
