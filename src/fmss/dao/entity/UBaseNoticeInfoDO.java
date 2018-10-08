package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: GongRunLin
 * @����: 2009-7-24 ����15:10:00
 * @����: ͨ����Ϣ
 */
public class UBaseNoticeInfoDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // ����
	private String title; // ͨ�����
	private String content; // ͨ������
	private String type; // ͨ��������ϵͳ
	private String userId; // �û����
	private Date createTime; // ����ʱ��
	private String status; // ״̬
	private List feedBacks; // ������Ϣ
	private List logs; // �鿴�û���־
	private List affixs; // ������Ϣ

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime2() {
		if(null == createTime){
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List getFeedBacks() {
		return feedBacks;
	}

	public void setFeedBacks(List feedBacks) {
		this.feedBacks = feedBacks;
	}

	public List getLogs() {
		return logs;
	}

	public void setLogs(List logs) {
		this.logs = logs;
	}

	public List getAffixs() {
		return affixs;
	}

	public void setAffixs(List affixs) {
		this.affixs = affixs;
	}
}
