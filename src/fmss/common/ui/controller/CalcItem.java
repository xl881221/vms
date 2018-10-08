package fmss.common.ui.controller;


/**
 * 类说明: 存储计算项<br> 创建时间: 2009-2-10 上午11:02:44<br>
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class CalcItem{

	public CalcItem(String key, String dataType, String dataRange){
		this.key = key;
		this.dataType = dataType;
		this.dataRange = dataRange;
	}

	private String key; // 项目标识
	private String dataType; // 项目数据类型
	private String dataRange; // 项目作用域
	private String keyValueFlm; // 项目对应的取值公式

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getDataType(){
		return dataType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public String getDataRange(){
		return dataRange;
	}

	public void setDataRange(String dataRange){
		this.dataRange = dataRange;
	}

	public String toString(){
		return this.key + "|" + this.dataType + " ]";
	}

	public String getKeyValueFlm(){
		if(AnalyseCst.NUMBER.equals(dataType)
				&& AnalyseCst.ROW.equals(dataRange)){
			return FormulaUtil.parseBsGetNRow(key);
		}else if(AnalyseCst.STRING.equals(dataType)
				&& AnalyseCst.ROW.equals(dataRange)){
			return FormulaUtil.parseBsGetSRow(key);
		}
		return null;
	}

	public void setKeyValueFlm(String keyValueFlm){
		this.keyValueFlm = keyValueFlm;
	}
}
