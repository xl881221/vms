package fmss.common.ui.controller;


/**
 * ��˵��: �洢������<br> ����ʱ��: 2009-2-10 ����11:02:44<br>
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class CalcItem{

	public CalcItem(String key, String dataType, String dataRange){
		this.key = key;
		this.dataType = dataType;
		this.dataRange = dataRange;
	}

	private String key; // ��Ŀ��ʶ
	private String dataType; // ��Ŀ��������
	private String dataRange; // ��Ŀ������
	private String keyValueFlm; // ��Ŀ��Ӧ��ȡֵ��ʽ

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
