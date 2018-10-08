package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.UserRoleAuditBase;
import fmss.dao.entity.UBaseUserDO;

public class User2RoleChangeType extends AbstractChangeType {

	public String getContentDesc() {
		UBaseUserDO user = (UBaseUserDO) entity;
		Assert.notNull(user, "用户参数不能为空");
		return "用户名[" + (user.getUserCname() != null ? user.getUserCname()
				: "无法获取用户名") + "]";
	}

	public String getDetailURL() {
		UBaseUserDO o = (UBaseUserDO) entity;
		Assert.notNull(o, "对象不能为空");
		String action = "showRoleChanges.action?userId=" + o.getUserId();
		return "showDetail('" + action + "',500,450)";
	}

	public String getSubmitDate() {
		UBaseUserDO user = (UBaseUserDO) entity;
		Assert.notNull(user, "用户参数不能为空");
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		return (String) AuditBase.TYPE_DESC_MAP
				.get(AuditBase.CHANGE_TYPE_USER2ROLE);
	}

	public void transform() {
		// TODO Auto-generated method stub

	}

	public String getApproveURL() {
		UBaseUserDO o = (UBaseUserDO) entity;
		Assert.notNull(o, "用户参数不能为空");
		String action = "approveRoleChanges.action?userId=" + o.getUserId();
		return action;
	}

	public String getRejectURL() {
		UBaseUserDO o = (UBaseUserDO) entity;
		Assert.notNull(o, "用户参数不能为空");
		String action = "rejectRoleChanges.action?userId=" + o.getUserId();
		return action;
	}

	public String getAuditResult() {
		try {
			UBaseUserDO o = (UBaseUserDO) entity;
			Assert.notNull(o, "用户参数不能为空");
			Assert.notNull(login, "登陆参数不能为空");
			String sql = "select distinct audit_status from "
					+ UserRoleAuditBase.BAK_TABLE
					+ " where user_id=? and change_user=?";
			List list = dao.find(sql, new Object[] { o.getUserId(),
					login.getUserId() });
			// with current user parameter
			String auditStatus = "";
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Map row = (Map) iterator.next();
				auditStatus += row.get("audit_status").toString() + ",";
			}
			if (auditStatus.indexOf(String
					.valueOf(AuditBase.AUDIT_STATUS_NOADUITED)) > -1)
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_NOADUITED));
			return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
					AuditBase.AUDIT_STATUS_HASAUDIT));
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}

	public String getCancelURL() {
		UBaseUserDO o = (UBaseUserDO) entity;
		Assert.notNull(o, "对象不能为空");
		String sql = "select count(audit_status) from "
				+ UserRoleAuditBase.BAK_TABLE
				+ " where audit_status not in(?,?,?) and user_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getUserId(),
				login.getUserId() });
		if (count > 0) {
			String action = "cancelRoleChanges.action?userId=" + o.getUserId();
			return action;
		}
		return null;
	}

	public String getOwnDetailURL() {
		UBaseUserDO o = (UBaseUserDO) entity;
		Assert.notNull(o, "对象不能为空");
		String action = "showOwnRoleChanges.action?userId=" + o.getUserId();
		return "showDetail('" + action + "',540,520)";
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}
}
