package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.SubSystemChangeAuditBase;
import fmss.action.entity.UBaseConfigChangeDO;

public class UBaseConfigChangeType extends AbstractChangeType {

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "��¼��������Ϊ��");
	    UBaseConfigChangeDO ubcc = (UBaseConfigChangeDO) entity;
		if(ubcc == null)
			return "ϵͳ�����ڶ�Ӧ����ϵͳ";
		return "��ϵͳ��[" + (ubcc.getSystemCname() != null ? ubcc.getSystemCname()
				: "�޷��������") + "]�޸���[" + ubcc.getChangeUser() + "]";
	}
//��ϸ��Ϣ�������ˣ�
	public String getDetailURL() {
		UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
		if(o == null)
			return "";
		String action = "showOwnUBaseCfgChanges.action?roleId=" + o.getSystemId();
		return "showDetail('" + action + "',650,450)";
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		return (String) AuditBase.TYPE_DESC_MAP.get(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY);
	}

	public void transform() {
		// TODO Auto-generated method stub

	}
//���ͨ��
	public String getApproveURL() {
		UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
		if(o == null)
			return "";
		String action = "approveUbaseCfgChanges.action?roleId=" + o.getSystemId();
		return action;
	}
//��˲�ͨ��
	public String getRejectURL() {
		UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
		if(o == null)
			return "";
		String action = "rejectUBaseCfgChanges.action?roleId=" + o.getSystemId();
		return action;
	}
//״̬����
	public String getAuditResult() {
		try {
			UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
			Assert.notNull(o, "��ϵͳ��Ϣ����Ϊ��");
			Assert.notNull(login, "��½��������Ϊ��");
			String sql = "select distinct audit_status from "
					+ SubSystemChangeAuditBase.CFG_BAK_TABLE
					+ " where system_id=? and (change_user=? or audit_user=?) ";
			List list = dao.find(sql, new Object[] { o.getSystemId(),
					login.getUserId(),login.getUserId() });
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
//����cancelSubSystemChanges
	public String getCancelURL() {
		if (StringUtils.isNotEmpty(cancelURL)) {
			return cancelURL;
		}
		UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ SubSystemChangeAuditBase.CFG_BAK_TABLE
				+ " where audit_status not in(?,?,?) and system_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getSystemId(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelUbaseCfgChanges.action?roleId=" + o.getSystemId();
		}
		return cancelURL;
	}
//�鿴��ť
	public String getOwnDetailURL() {
		UBaseConfigChangeDO o = (UBaseConfigChangeDO) entity;
		if(o == null)
			return "";
		String action = "showOwnUBaseCfgChanges.action?roleId=" + o.getSystemId();
		return "showDetail('" + action + "',650,450)";
		//showOwnUBaseCfgChanges
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}

}
