/*
 * 
 */
package fmss.common.ui;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:11:45
 * @����: [InstGridTreeNode]���ڴ˼�Ҫ������Ĺ���
 */
public class InstGridTreeNode extends GridTreeNode{

	private String instId;// ������� INST_ID varchar2(20)
	private String instName;// �������� INST_NAME varchar2(30)
	private String instSmpName;// ������� INST_SMP_NAME varchar2(20)
	private String parentInstId;// �ϼ����� PARENT_INST_ID varchar2(20)
	private Integer instLayer;// �������� INST_LAYER INT
	private String address;// ������ַ ADDRESS varchar2(100)
	private String zip;// �����ʱ� ZIP varchar2(6)
	private String tel;// �����绰 TEL varchar2(27)
	private String fax;// �������� FAX varchar2(20)
	private String isBussiness;// �Ƿ�ҵ����� IS_BUSSINESS VARCHAR2(5)
	private Integer orderNum;// ����ֵ ORDER_NUM INT
	private String description;// ���� DESCRIPTION VARCHAR2(100)
	private String startDate;// �������� START_DATE DATE
	private String endDate;// ��ֹ���� END_DATE DATE
	private String createTime;// ����ʱ�� CREATE_TIME DATE
	private String enabled;// ���ñ�ʶ ENABLED VARCHAR2(5)

	/**
	 * <p>��������: getInstId|����:ȡ�û������ </p>
	 * @return
	 */
	public String getInstId(){
		return instId;
	}

	/**
	 * <p>��������: setInstId|����:���û������ </p>
	 * @param instId
	 */
	public void setInstId(String instId){
		this.instId = instId;
	}

	/**
	 * <p>��������: getInstName|����:ȡ�û������� </p>
	 * @return
	 */
	public String getInstName(){
		return instName;
	}

	/**
	 * <p>��������: setInstName|����:���û������� </p>
	 * @param instName
	 */
	public void setInstName(String instName){
		this.instName = instName;
	}

	/**
	 * <p>��������: getInstSmpName|����:ȡ�û������ </p>
	 * @return
	 */
	public String getInstSmpName(){
		return instSmpName;
	}

	/**
	 * <p>��������: setInstSmpName|����:���û������ </p>
	 * @param instSmpName
	 */
	public void setInstSmpName(String instSmpName){
		this.instSmpName = instSmpName;
	}

	/**
	 * <p>��������: getParentInstId|����:ȡ���ϼ�����ID </p>
	 * @return
	 */
	public String getParentInstId(){
		return parentInstId;
	}

	/**
	 * <p>��������: setParentInstId|����:�����ϼ�����ID </p>
	 * @param parentInstId
	 */
	public void setParentInstId(String parentInstId){
		this.parentInstId = parentInstId;
	}

	/**
	 * <p>��������: getInstLayer|����:ȡ�û������� </p>
	 * @return
	 */
	public Integer getInstLayer(){
		return instLayer;
	}

	/**
	 * <p>��������: setInstLayer|����:���û������� </p>
	 * @param instLayer
	 */
	public void setInstLayer(Integer instLayer){
		this.instLayer = instLayer;
	}

	/**
	 * <p>��������: getAddress|����:ȡ�û������ڵ�ַ </p>
	 * @return
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * <p>��������: setAddress|����:���û������ڵ�ַ </p>
	 * @param address
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * <p>��������: getZip|����:ȡ���������� </p>
	 * @return
	 */
	public String getZip(){
		return zip;
	}

	/**
	 * <p>��������: setZip|����:������������ </p>
	 * @param zip
	 */
	public void setZip(String zip){
		this.zip = zip;
	}

	/**
	 * <p>��������: getTel|����:ȡ�õ绰���� </p>
	 * @return
	 */
	public String getTel(){
		return tel;
	}

	/**
	 * <p>��������: setTel|����:���õ绰���� </p>
	 * @param tel
	 */
	public void setTel(String tel){
		this.tel = tel;
	}

	/**
	 * <p>��������: getFax|����:ȡ�ô������ </p>
	 * @return
	 */
	public String getFax(){
		return fax;
	}

	/**
	 * <p>��������: setFax|����:���ô������ </p>
	 * @param fax
	 */
	public void setFax(String fax){
		this.fax = fax;
	}

	/**
	 * <p>��������: getIsBussiness|����:�ж��Ƿ�ҵ����� </p>
	 * @return
	 */
	public String getIsBussiness(){
		return isBussiness;
	}

	/**
	 * <p>��������: setIsBussiness|����:�����Ƿ�Ϊҵ����� </p>
	 * @param isBussiness
	 */
	public void setIsBussiness(String isBussiness){
		this.isBussiness = isBussiness;
	}

	/**
	 * <p>��������: getOrderNum|����:ȡ��������� </p>
	 * @return
	 */
	public Integer getOrderNum(){
		return orderNum;
	}

	/**
	 * <p>��������: setOrderNum|����:����������� </p>
	 * @param orderNum
	 */
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}

	/**
	 * <p>��������: getDescription|����:ȡ��������Ϣ </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>��������: setDescription|����:����������Ϣ </p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * <p>��������: getStartDate|����:ȡ�ÿ�ʼ���� </p>
	 * @return
	 */
	public String getStartDate(){
		return startDate;
	}

	/**
	 * <p>��������: setStartDate|����:���ÿ�ʼ���� </p>
	 * @param startDate
	 */
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	/**
	 * <p>��������: getEndDate|����:ȡ�ý������� </p>
	 * @return
	 */
	public String getEndDate(){
		return endDate;
	}

	/**
	 * <p>��������: setEndDate|����:���ý������� </p>
	 * @param endDate
	 */
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	/**
	 * <p>��������: getCreateTime|����:ȡ�ô���ʱ�� </p>
	 * @return
	 */
	public String getCreateTime(){
		return createTime;
	}

	/**
	 * <p>��������: setCreateTime|����:���ô���ʱ�� </p>
	 * @param createTime
	 */
	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	/**
	 * <p>��������: getEnabled|����: �ж��Ƿ����ô˻���</p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>��������: setEnabled|����: ���û����Ƿ�����</p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
	}
}