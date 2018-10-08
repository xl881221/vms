package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.SubSystemAdminAuditBase;
import fmss.action.entity.SubSystemAdminChangeDO;

public class SubSystemAdminChangeType extends AbstractChangeType {
//���ӹ���Ա����
	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "��¼��������Ϊ��");
	    SubSystemAdminChangeDO sscdo = (SubSystemAdminChangeDO) entity;
		if(sscdo == null)
			return "ϵͳ�����ڶ�Ӧ����ϵͳ";
		return "��ϵͳ��[" + (sscdo.getSystemCname() != null ? sscdo.getSystemCname()
				: "�޷��������") + "]����Ա["+sscdo.getUserId()+"]�޸���[" + sscdo.getChangeUser() + "]";
	}
//��ϸ��Ϣ�������ˣ�
	public String getDetailURL() {
//		SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
//		if(o == null)
//			return "";
//		String action = "showOwnSubSystemChanges.action?roleId=" + o.getSystemId();
//		return "showDetail('" + action + "',650,450)";
		return "";
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
//		return (String) AuditBase.TYPE_DESC_MAP.get(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY);
		return "��������Ա";
	}

	public void transform() {
		// TODO Auto-generated method stub

	}
//���ͨ��
	public String getApproveURL() {
		SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
		if(o == null)
			return "";
		String action = "approveSubSystemAdminChanges.action?roleId=" + o.getUserId()+"&id="+o.getId()+"&systemId="+o.getSystemId();
		return action;
	}
//��˲�ͨ��rejectSubSystemAdminChanges
	public String getRejectURL() {
		SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
		if(o == null)
			return "";
		String action = "rejectSubSystemAdminChanges.action?roleId=" + o.getUserId()+"&id="+o.getId()+"&systemId="+o.getSystemId();
		return action;
	}
//״̬����
	public String getAuditResult() {
		try {
			SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
			Assert.notNull(o, "��ϵͳ��Ϣ����Ϊ��");
			Assert.notNull(login, "��½��������Ϊ��");
			String sql = "select distinct audit_status from "
					+ SubSystemAdminAuditBase.SUB_BAK_TABLE
					+ " where user_id=? and (change_user=? or audit_user=?)";
			List list = dao.find(sql, new Object[] { o.getUserId(),
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
		SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ SubSystemAdminAuditBase.SUB_BAK_TABLE
				+ " where audit_status not in(?,?,?) and user_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getUserId(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelSubSystemAdminChanges.action?roleId=" + o.getUserId()+"&id="+o.getId()+"&systemId="+o.getSystemId();
		}
		return cancelURL;
	}
//�鿴��ť
	public String getOwnDetailURL() {
//		SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) entity;
//		if(o == null)
//			return "";
//		String action = "showOwnSubSystemChanges.action?roleId=" + o.getSystemId();
//		return "showDetail('" + action + "',650,450)";
		return "";
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}

}
