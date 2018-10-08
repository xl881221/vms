package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import fmss.action.base.AuditBase;
import fmss.action.base.SysParamAuditBase;
import fmss.action.entity.UBaseSysParamChangeDO;

public class SysParamChangeType extends AbstractChangeType {

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "登录参数不能为空");
		UBaseSysParamChangeDO usc = (UBaseSysParamChangeDO) entity;
		if(usc == null)
			return "系统不存在对应的参数";
		return "修改了参数[" + (usc.getItemCname() != null ? usc.getItemCname()
				: "无法获参数名") +"]修改值为["+usc.getSelectedValue()+ "]修改人[" + usc.getChangeUser() + "]";
	}
//详细信息（变更审核）
	public String getDetailURL() {
		UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
		if(o == null)
			return "";
		String action = "showOwnSysParamChanges.action?roleId=" + o.getParamId();
		return "showDetail('" + action + "')";
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		return (String) AuditBase.TYPE_DESC_MAP.get(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY);
	}

	public void transform() {
		// TODO Auto-generated method stub

	}
//审核通过
	public String getApproveURL() {
		UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
		if(o == null)
			return "";
		String action = "approveSysParamChanges.action?roleId=" + o.getParamId();
		return action;
	}
//审核不通过
	public String getRejectURL() {
		UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
		if(o == null)
			return "";
		String action = "rejectSysParamChanges.action?roleId=" + o.getParamId();
		return action;
	}
//状态参数
	public String getAuditResult() {
		try {
			UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
			Assert.notNull(o, "系统参数不能为空");
			Assert.notNull(login, "登陆参数不能为空");
			String sql = "select distinct audit_status from "
					+ SysParamAuditBase.SPBAK_TABLE
					+ " where param_id=? and (change_user=? or audit_user= ? )";
			List list = dao.find(sql, new Object[] { String.valueOf(o.getParamId()),
					login.getUserId() ,login.getUserId()});
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
		UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ SysParamAuditBase.SPBAK_TABLE
				+ " where audit_status not in(?,?,?) and param_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getParamId(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelUBaseSysParamChanges.action?roleId=" + o.getParamId();
		}
		return cancelURL;
	}
//查看按钮
	public String getOwnDetailURL() {
//		UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) entity;
//		if(o == null)
//			return "";
//		String action = "showOwnSysParamChanges.action?roleId=" + o.getParamId();
//		return "showDetail('" + action + "')";
		return "";
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}

}
