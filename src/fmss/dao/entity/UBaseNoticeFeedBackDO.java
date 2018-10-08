package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> 版权所有:(C)2003-2010  </p>
 * @作者: GongRunLin
 * @日期: 2009-7-24 下午15:10:00
 * @描述: 通告反馈
 */
public class UBaseNoticeFeedBackDO extends BaseDO implements Serializable{

	public static final long serialVersionUID = 1L;
	private Long id; // 主键
	private Long noticeId; // 外键
	private String userId; // 用户编号
	private String userEName; // 用户登录名
	private String userCName; // 用户中文名
	private String content; // 反馈内容
	private Date feedTime; // 反馈时间s

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
