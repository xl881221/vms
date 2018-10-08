package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: GongRunLin
 * @日期: 2009-7-24 下午15:10:00
 * @描述: 通告信息
 */
public class UBaseNoticeInfoDO extends BaseDO implements Serializable {

	public static final long serialVersionUID = 1L;
	private Long id; // 主键
	private String title; // 通告标题
	private String content; // 通告内容
	private String type; // 通告所属子系统
	private String userId; // 用户编号
	private Date createTime; // 创建时间
	private String status; // 状态
	private List feedBacks; // 反馈信息
	private List logs; // 查看用户日志
	private List affixs; // 附件信息

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
