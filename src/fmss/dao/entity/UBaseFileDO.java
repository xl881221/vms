package fmss.dao.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class UBaseFileDO {
	
	private Long fileId;
	private UBaseFolderDO baseFolder;
	private String refId;
	private String fileName;
	private Long fileSize;
	private double fileSizeKB;
	private Date uploadTime;
	private String status;
	private String createUserId;
	private String createUserName;
	
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
	public UBaseFolderDO getBaseFolder() {
		return baseFolder;
	}
	public void setBaseFolder(UBaseFolderDO baseFolder) {
		this.baseFolder = baseFolder;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public double getFileSizeKB() {
		if(fileSize!=null){
			double f = 1.0*fileSize.longValue()/1024;
			BigDecimal bg = new BigDecimal(f);
			f=bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f;
		}
		return 0;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setFileSizeKB(double fileSizeKB) {
		this.fileSizeKB = fileSizeKB;
	}
}
