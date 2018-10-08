package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> ��Ȩ����:(C)2003-2010  </p>
 * @����: GongRunLin
 * @����: 2009-7-24 ����15:10:00
 * @����: ͨ�淴��
 */
public class UBaseNoticeFeedBackDO extends BaseDO implements Serializable{

	public static final long serialVersionUID = 1L;
	private Long id; // ����
	private Long noticeId; // ���
	private String userId; // �û����
	private String userEName; // �û���¼��
	private String userCName; // �û�������
	private String content; // ��������
	private Date feedTime; // ����ʱ��s

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getNoticeId(){
		return noticeId;
	}

	public void setNoticeId(Long noticeId){
		this.noticeId = noticeId;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserEName(){
		return userEName;
	}

	public void setUserEName(String userEName){
		this.userEName = userEName;
	}

	public String getUserCName(){
		return userCName;
	}

	public void setUserCName(String userCName){
		this.userCName = userCName;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Date getFeedTime(){
		return feedTime;
	}

	public void setFeedTime(Date feedTime){
		this.feedTime = feedTime;
	}

	public String getFeedTime2(){
		if(feedTime != null)
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(feedTime);
		return null;
	}
}
