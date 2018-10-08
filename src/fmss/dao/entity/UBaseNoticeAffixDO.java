package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: GongRunLin
 * @日期: 2009-7-24 下午15:10:00
 * @描述: 通告附件
 */
public class UBaseNoticeAffixDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // 主键
	private Long noticeId; // 外键
	private String filePath; // 文件路径
	private String fileName; // 文件名称
	private Long fileSize; // 文件大小
	private String uploadUser; // 上传人
	private Date uploadTime; // 上传时间
	private String description; // 文件描述

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public String getUploadTime2() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uploadTime);
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
