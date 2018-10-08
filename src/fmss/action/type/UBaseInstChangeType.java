package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.InstBaseAuditBase;
import fmss.action.entity.UBaseInstChangeDO;

public class UBaseInstChangeType extends AbstractChangeType {

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "登录参数不能为空");
		UBaseInstChangeDO instc = (UBaseInstChangeDO) entity;
		if(instc == null)
			return "系统不存在对应的机构";
		return "机构名[" + (instc.getInstName() != null ? instc.getInstName()
				: "无法获取机构名") + "]修改人[" + instc.getChangeUser() + "]";
	}
//详细信息（变更审核）
	public String getDetailURL() {

		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if(o == null)
			return "";
		String action = "showModifyUBaseInstChanges.action?roleId=" + o.getInstId();
		return "showDetail('" + action + "',650,550)";
	
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if (o.getChangeStatus().intValue() == 7) {
			return "机构新增";
		}
		if (o.getChangeStatus().intValue() == 8) {
			return (String) AuditBase.TYPE_DESC_MAP
					.get(AuditBase.CHANGE_TYPE_INST_MODIFY);
		}
		return null ;
	}

	public void transform() {
		// TODO Auto-generated method stub

	}
//审核通过
	public String getApproveURL() {
		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if(o == null)
			return "";
		String action = "approveInstChanges.action?roleId=" + o.getInstId();
		return action;
	}
//审核不通过
	public String getRejectURL() {
		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if(o == null)
			return "";
		String action = "rejectInstBaseChanges.action?roleId=" + o.getInstId();
		return action;
	}
//状态参数
	public String getAuditResult() {
		try {
			UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
			Assert.notNull(o, "机构参数不能为空");
			Assert.notNull(login, "登陆参数不能为空");
			String sql = "select distinct audit_status from "
					+ InstBaseAuditBase.IBAK_TABLE
					+ " where inst_id=? and (change_user=? or audit_user =? )";
			List list = dao.find(sql, new Object[] { o.getInstId(),
					login.getUserId(),login.getUserId()});
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
			if (auditStatus.indexOf(String
					.valueOf(AuditBase.AUDIT_STATUS_APPROVED)) > -1)
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_APPROVED));
			if (auditStatus.indexOf(String
					.valueOf(AuditBase.AUDIT_STATUS_REJECTED)) > -1)
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_REJECTED));
			if (auditStatus.indexOf(String
					.valueOf(AuditBase.AUDIT_STATUS_CANCEL)) > -1)
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_CANCEL));
			if (auditStatus.indexOf(String
					.valueOf(AuditBase.AUDIT_STATUS_HASAUDIT)) > -1)
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_HASAUDIT));
			return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_NOADUITED));
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}
//撤销showResourceChanges
	public String getCancelURL() {
		if (StringUtils.isNotEmpty(cancelURL)) {
			return cancelURL;
		}
		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ InstBaseAuditBase.IBAK_TABLE
				+ " where audit_status not in(?,?,?) and inst_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getInstId(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelUBaseInstChanges.action?roleId=" + o.getInstId();
		}
		return cancelURL;
	}
//查看按钮
	public String getOwnDetailURL() {

		UBaseInstChangeDO o = (UBaseInstChangeDO) entity;
		if(o == null)
			return "";
		String action = "showModifyUBaseInstChanges.action?roleId=" + o.getInstId();
		return "showDetail('" + action + "',650,550)";
	
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}

}
