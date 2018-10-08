package fmss.action.type;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.base.HolidayTypeBaseAuditBase;
import fmss.action.base.InstBaseAuditBase;
import fmss.action.entity.HolidayTypeChangeDO;
import fmss.action.entity.HolidayTypeChangeDO;
import fmss.action.entity.HolidayTypeChangeDO;

public class UBaseHolidayAddType extends AbstractChangeType {
	
	protected String typeDesc;

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "��¼��������Ϊ��");
		HolidayTypeChangeDO holiday = (HolidayTypeChangeDO) entity;
		if(holiday == null)
			return "ϵͳ�����ڶԽڼ���";
		return "�ڼ�������[" + (holiday.getHolidayName() != null ? holiday.getHolidayName()
				: "�޷���ȡ�ڼ�������") + "]�޸���[" + holiday.getChangeUser() + "]";
	}
//��ϸ��Ϣ�������ˣ�
	public String getDetailURL() {
		String changes = "";
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		if(o == null)
			return "";
		if(o.getChangeStatus().intValue() == AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY.intValue()){
			changes = "changeHolidayType";
		}else{
			changes = "changeHoliday";
		}
		String action = "listHolidayByType.action?holidayType=" + o.getHolidayType()+"&readOnly=true&changes="+changes;
		//String action = "showHolidayChanges.action?id=" + o.getHolidayType();
		return "showDetail('" + action + "',650,650)";
	
	}

	public String getSubmitDate() {
		if (changeTime != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT)
					.format(changeTime);
		return submitDate;
	}

	public String getTypeDesc() {
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		if (o.getChangeStatus().intValue() == AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD.intValue()) {
			return "�ڼ�������";
		}else if(o.getChangeStatus().intValue() == AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY.intValue()){
			return "�ڼ����޸�";
		}else if(o.getChangeStatus().intValue() == AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE.intValue()){
			return "�ڼ���ɾ��";
		}
		return null ;
	}

	public void transform() {
		// TODO Auto-generated method stub

	}
//���ͨ��
	public String getApproveURL() {
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		String id="";
		if(o == null)
			return "";
		if(o.getId()!=null){
			id=o.getId().toString();
		}
		String action = "approveHolidayChanges.action?id="+id+"&holidayType="+o.getHolidayType();
		return action;
	}
//��˲�ͨ��
	public String getRejectURL() {
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		if(o == null)
			return "";
		String action = "rejectHolidayChanges.action?id=" + o.getId()+"&holidayType="+o.getHolidayType();
		if(o.getId()==null){
			action = "rejectHolidayChanges.action?holidayType="+o.getHolidayType();
		}
		return action;
	}
//״̬����
	public String getAuditResult() {
		try {
			HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
			Assert.notNull(o, "������������Ϊ��");
			Assert.notNull(login, "��½��������Ϊ��");
			if(o.getAuditStatus()!=null&&o.getAuditStatus().toString().equals("1")){
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_NOADUITED));
			}
			if(o.getAuditStatus()!=null&&o.getAuditStatus().toString().equals("2")){
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_APPROVED));
			}
			if(o.getAuditStatus()!=null&&o.getAuditStatus().toString().equals("3")){
				return (String) AuditBase.AUDIT_STATUS_MAP.get(new Long(
						AuditBase.AUDIT_STATUS_REJECTED));
			}
			String sql = "select distinct audit_status from "
					+ HolidayTypeBaseAuditBase.HTTBAK_TABLE
					+ " where holiday_type=? and (change_user=? or audit_user =? )";
			List list = dao.find(sql, new Object[] { o.getHolidayType(),
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
//����showResourceChanges
	public String getCancelURL() {
		if (StringUtils.isNotEmpty(cancelURL)) {
			return cancelURL;
		}
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		if(o == null)
			return "";
		String sql = "select count(audit_status) from "
				+ InstBaseAuditBase.IBAK_TABLE
				+ " where audit_status not in(?,?,?) and inst_id=? and change_user=?";
		int count = dao.findForInt(sql, new Object[] {
				new Long(AuditBase.AUDIT_STATUS_APPROVED),
				new Long(AuditBase.AUDIT_STATUS_REJECTED),
				new Long(AuditBase.AUDIT_STATUS_CANCEL), o.getHolidayType(),
				login.getUserId() });
		if (count > 0) {
			cancelURL = "cancelHolidayChanges.action?id=" + o.getHolidayType();
		}
		return cancelURL;
	}
//�鿴��ť
	public String getOwnDetailURL() {
		String changes = "";
		HolidayTypeChangeDO o = (HolidayTypeChangeDO) entity;
		if(o == null)
			return "";
		if(o.getChangeStatus().intValue() == AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY.intValue()){
			changes = "changeHolidayType";
		}else{
			changes = "changeHoliday";
		}
		String action = "listHolidayByType.action?holidayType=" + o.getHolidayType()+"&readOnly=true&changes="+changes;
		//String action = "showHolidayChanges.action?id=" + o.getHolidayType();
		return "showDetail('" + action + "',650,650)";
	}

	public Boolean getCanAudit() {
		return new Boolean(true);
	}
	public void setTypeDesc(String typeDesc){
		this.typeDesc=typeDesc;
	}

}
