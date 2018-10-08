package fmss.common.ui.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * ��˵��:<br>
 * ����ʱ��: 2008-9-2 ����04:57:52<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ColMeta implements Comparable{
	
	public ColMeta(String key,String name,int position){
		this.key = key;
		this.name = name;
		this.position = position;
	}
	/**
	 * �Ƿ��� �Զ���� meta
	 */
	private boolean isCustom = false;
	/**
	 * �Ƿ��� �ɱ༭�� 
	 */	
	private boolean isEditor = false;
	/**
	 * ������
	 */
	private String key;
	/**
	 * ������
	 */
	private String name;
	/**
	 * ��λ��
	 */
	private int position;
	/**
	 * ���Ƿ����
	 * @return
	 */
	private Boolean isGroup = new Boolean(false);

	private boolean isNumber;
	/**
	 * ���Ƿ���ʾ
	 */
	private Boolean isDisplay = new Boolean(true);
	/**
	 * ���Ƿ���Ϊ����
	 */
	private Boolean isCondtion = new Boolean(false);
	
	private Boolean isSumItemByGroup;
	/**
	 * ����
	 */
	private String tableClmRule;
	/**
	 * ��Ϊ����ʱ ���������ֵ
	 */
	private String coditionValue="";
	//�����洢��α�� ����ʹ�÷���ʱ���õ�
	private String coditionValueCode="";
	/**
	 * ��Ϊ����ʱ ���������ֵ
	 */
	private List tableClmValues;
	/**
	 * �ֶ�����
	 */
	private int groupCount;
	/**
	 * �Ƿ�Ӧ��GROUP BY �Ӿ�
	 */
	private Boolean groupBy;
	/**
	 * �ϼƺ���
	 */	
	private String func;
	
	private String flmMsg = "";//�洢 ����Ӧ�õĹ�ʽ��Ϣ
	
	private String getValueFlm;//�洢 ���е�ȡֵ��ʽ
	
	private String tableClmType; //string number date

	private ColumnDisplay columnDisplay;
	
	private String unitType;
	
	/**
	 * ��ʶ�����Ƿ����ù��Զ�������
	 */
	private boolean isCustomAttr = false;
	
	public boolean isCustomAttr() {
		return isCustomAttr;
	}
	public void setCustomAttr(boolean isCustomAttr) {
		this.isCustomAttr = isCustomAttr;
	}
	public String getTableClmRule() {
		return tableClmRule;
	}
	public void setTableClmRule(String tableClmRule) {
		this.tableClmRule = tableClmRule;
	}
	public List getTableClmValues() {
		return tableClmValues;
	}
	public void setTableClmValues(List tableClmValues) {
		this.tableClmValues = tableClmValues;
	}
	public String getTableClmType() {
		return tableClmType;
	}
	public void setTableClmType(String tableClmType) {
		this.tableClmType = tableClmType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Boolean getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
	public Boolean getIsCondtion() {
		return isCondtion;
	}
	public void setIsCondtion(Boolean isCondtion) {
		this.isCondtion = isCondtion;
	}
	public int getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	public int compareTo(Object o) {
		return (new Integer(this.getGroupCount())).compareTo(new Integer(((ColMeta) o).getGroupCount()));
	}
	public String getCoditionValue() {
		return coditionValue;
	}
	public void setCoditionValue(String coditionValue) {
		this.coditionValue = coditionValue;
	}
	public String getCoditionValueCode() {
		return coditionValueCode;
	}
	public void setCoditionValueCode(String coditionValueCode) {
		this.coditionValueCode = coditionValueCode;
	}
	public boolean isNumber() {
		return "number".equals(tableClmType)?true:false;
	}
	public void setNumber(boolean isNumber) {
		this.isNumber = isNumber;
	}
	public Boolean getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(Boolean groupBy) {
		this.groupBy = groupBy;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public Boolean getIsSumItemByGroup() {
		if(isGroup.booleanValue()){
			return isSumItemByGroup;
		}else{
			return new Boolean(false);
		}
	}
	public void setIsSumItemByGroup(Boolean isSumItemByGroup) {
		this.isSumItemByGroup = isSumItemByGroup;
	}

	public boolean isCustom() {
		return isCustom;
	}
	public void setCustom(boolean isCustom) {
		this.isCustom = isCustom;
	}
	public boolean isEditor() {
		return isEditor;
	}
	public void setEditor(boolean isEditor) {
		this.isEditor = isEditor;
	}
	public String getFlmMsg() {
		return flmMsg;
	}
	public void setFlmMsg(String flmMsg) {
		this.flmMsg = flmMsg;
	}
	public ColumnDisplay getColumnDisplay() {

		return columnDisplay;
	}
	public void setColumnDisplay(ColumnDisplay columnDisplay) {
		this.columnDisplay = columnDisplay;
	}
	public String getGetValueFlm() {
		return getValueFlm;
	}
	public void setGetValueFlm(String getValueFlm) {
		this.getValueFlm = getValueFlm;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

}
