package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.RoleResourceAuditBase;
import fmss.dao.entity.UAuthRoleDO;

public class Role2ResourceChangeType extends AbstractChangeType {

	public String getContentDesc() {
		UAuthRoleDO role = (UAuthRoleDO) entity;
		if(role == null)
			return "系统不存在对应的角色";
		return "角色名[" + role.getRoleName() + "]";
	}

	public String getDetailURL() {
		UAuthRoleDO o = (UAuthRoleDO) entity;
		if(o == null)
			return "";
		String action = "showResourceChanges.action?roleId=" + o.getRoleId();
		return "showDetail('" + action + "',650,350)";
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		return (String) AuditBase.TYPE_DESC_MAP.get(AuditBase.CHANGE_TYPE_ROLE2RESOURCE);
	}

	public void transform() {
		// TODO Auto-generated method stub

	}

	public String getApproveURL() {
		UAuthRoleDO o = (UAuthRoleDO) entity;
		if(o == null)
			return "";
		String action = "approveResourceChanges.action?roleId=" + o.getRoleId();
		return action;
	}

	public String getRejectURL() {
		UAuthRoleDO o = (UAuthRoleDO) entity;
		if(o == null)
			return "";
		String action = "rejectResourceChanges.action?roleId=" + o.getRoleId();
		return action;
	}

	public String getAuditResult() {
		try {
			UAuthRoleDO o = (UAuthRoleDO) entity;
			Assert.notNull(o, "角色参数不能为空");
			Assert.notNull(login, "登陆参数不能为空");
			String sql = "select distinct audit_status from "
					+ RoleResourceAuditBase.BAK_TABLE
					+ " where object_id=? and change_user=?";
			List list = dao.find(sql, new Object[] { o.getRoleId(),
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
		if (StringUtils.isNotEmpty(cancelURL)) {
			return cancelURL;
		}
		UAuthRoleDO o = (UAuthRoleDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ RoleResourceAuditBase.BAK_TABLE
				+ " where audit_status not in(?,?,?) and object_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getRoleId(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelResourceChanges.action?roleId=" + o.getRoleId();
		}
		return cancelURL;
	}

	public String getOwnDetailURL() {
		UAuthRoleDO o = (UAuthRoleDO) entity;
		if(o == null)
			return "";
		String action = "showOwnResourceChanges.action?roleId=" + o.getRoleId();
		return "showDetail('" + action + "',800,350)";
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}

}
