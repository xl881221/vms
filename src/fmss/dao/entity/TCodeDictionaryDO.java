package fmss.dao.entity;

import java.io.Serializable;

public class TCodeDictionaryDO extends BaseDO implements Serializable{
	private String codeType;
	private String codeValueBank;
	private String codeValueStandardLetter;
	private String codeValueStandardNum;
	private String codeName;
	private String codeTypeDesc;
	private String codeSym;
	private String backupNum;
	private String begindate;	//	启用日期
	private String enddate;		//停用日期
	private String rabValue;	//奥合银行系统值
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCodeValueBank() {
		return codeValueBank;
	}
	public void setCodeValueBank(String codeValueBank) {
		this.codeValueBank = codeValueBank;
	}
	public String getCodeValueStandardLetter() {
		return codeValueStandardLetter;
	}
	public void setCodeValueStandardLetter(String codeValueStandardLetter) {
		this.codeValueStandardLetter = codeValueStandardLetter;
	}
	public String getCodeValueStandardNum() {
		return codeValueStandardNum;
	}
	public void setCodeValueStandardNum(String codeValueStandardNum) {
		this.codeValueStandardNum = codeValueStandardNum;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeTypeDesc() {
		return codeTypeDesc;
	}
	public void setCodeTypeDesc(String codeTypeDesc) {
		this.codeTypeDesc = codeTypeDesc;
	}
	public String getCodeSym() {
		return codeSym;
	}
	public void setCodeSym(String codeSym) {
		this.codeSym = codeSym;
	}
	public String getBackupNum() {
		return backupNum;
	}
	public void setBackupNum(String backupNum) {
		this.backupNum = backupNum;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getRabValue() {
		return rabValue;
	}
	public void setRabValue(String rabValue) {
		this.rabValue = rabValue;
	}
}
