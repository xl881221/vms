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
 * @����: ͨ��鿴��־
 */
public class UBaseNoticeLogDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // ����
	private Long noticeId; // ���
	private String userEName; // �û���¼��
	private String userCName; // �û�������
	private Date viewTime; // �鿴ʱ��
	private String ip; // �û�IP��ַ

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

	public String getUserEName() {
		return userEName;
	}

	public void setUserEName(String userEName) {
		this.userEName = userEName;
	}

	public String getUserCName() {
		return userCName;
	}

	public void setUserCName(String userCName) {
		this.userCName = userCName;
	}

	public Date getViewTime() {
		return viewTime;
	}

	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}

	public String getViewTime2() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(viewTime);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
