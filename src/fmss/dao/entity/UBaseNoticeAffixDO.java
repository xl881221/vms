package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: GongRunLin
 * @����: 2009-7-24 ����15:10:00
 * @����: ͨ�渽��
 */
public class UBaseNoticeAffixDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // ����
	private Long noticeId; // ���
	private String filePath; // �ļ�·��
	private String fileName; // �ļ�����
	private Long fileSize; // �ļ���С
	private String uploadUser; // �ϴ���
	private Date uploadTime; // �ϴ�ʱ��
	private String description; // �ļ�����

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
