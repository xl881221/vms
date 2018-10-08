package fmss.action.type;

import org.springframework.util.Assert;

import fmss.action.base.AuditBase;
import fmss.action.entity.UserChangeDO;

public class UserDeleteChangeType extends UserChangeType {

	public String getApproveURL() {
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user.getId(), "审核id不能为空");
		String action = "approveUserDelete.action?id="
				+ user.getId().longValue();
		return action;
	}

	public String getAuditResult() {
		Assert.notNull(entity);
		UserChangeDO o = (UserChangeDO) entity;
		return (String) AuditBase.AUDIT_STATUS_MAP.get(o.getAuditStatus());
	}

	public String getCancelURL() {
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user.getId(), "审核id不能为空");
		if (user.getAuditStatus() != null
				&& AuditBase.AUDIT_STATUS_APPROVED != user.getAuditStatus()
						.intValue()
				&& AuditBase.AUDIT_STATUS_REJECTED != user.getAuditStatus()
						.intValue()
				&& AuditBase.AUDIT_STATUS_CANCEL != user.getAuditStatus()
						.intValue()) {
			String action = "cancelUserDelete.action?id="
					+ user.getId().longValue();
			return action;
		}
		return null;
	}

	public String getDetailURL() {
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user.getId(), "审核id不能为空");
		String action = "showUserChanges.action?id=" + user.getId().longValue();
		return "showDetail('" + action + "',500,400)";
	}

	public String getOwnDetailURL() {
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user.getId(), "审核id不能为空");
		String action = "showOwnUserChanges.action?id="
				+ user.getId().longValue();
		return "showDetail('" + action + "',500,400)";
	}

	public String getRejectURL() {
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user.getId(), "审核id不能为空");
		String action = "rejectUserDelete.action?id="
				+ user.getId().longValue();
		return action;
	}

	public String getTypeDesc() {
		return (String) AuditBase.TYPE_DESC_MAP.get(AuditBase.CHANGE_TYPE_USER_DELETE);
	}

	public void transform() {
		// TODO Auto-generated method stub

	}

}
