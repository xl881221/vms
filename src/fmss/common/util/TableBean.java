package fmss.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBean {
	private String name;
	private List cols;
	
	
	private Map difcols;//对比后有区别的列信息
	
	
	public TableBean() {
		super();
		this.cols = new ArrayList();
		this.difcols = new HashMap();
	}
	
	public TableBean(String name, List cols) {
		super();
		this.name = name;
		this.cols = cols;
		this.difcols = new HashMap();
	}
	
	public Map getDifcols() {
		return difcols;
	}

	public void setDifcols(Map difcols) {
		this.difcols = difcols;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List getCols() {
		return cols;
	}

	public void setCols(List cols) {
		this.cols = cols;
	}
	
	
	
}
