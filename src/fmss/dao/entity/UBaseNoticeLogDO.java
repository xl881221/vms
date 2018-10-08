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
 * @描述: 通告查看日志
 */
public class UBaseNoticeLogDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // 主键
	private Long noticeId; // 外键
	private String userEName; // 用户登录名
	private String userCName; // 用户中文名
	private Date viewTime; // 查看时间
	private String ip; // 用户IP地址

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
