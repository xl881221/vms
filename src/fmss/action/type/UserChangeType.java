package fmss.action.type;

import java.text.SimpleDateFormat;

import org.springframework.util.Assert;

import fmss.action.base.UserBaseAuditBase;
import fmss.action.entity.UserChangeDO;

public abstract class UserChangeType extends AbstractChangeType {

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "��¼��������Ϊ��");
		UserChangeDO user = (UserChangeDO) entity;
		return "�û���[" + (user.getUserCname() != null ? user.getUserCname()
				: "�޷���ȡ�û���") + "]�޸���[" + user.getChangeUser() + "]";
	}

	public String getSubmitDate() {
		String submitDate = "";
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user, "�û���������Ϊ��");
		if (user.getChangeTime() != null)
			submitDate = new SimpleDateFormat(SHORT_DATE_FORMAT).format(user
					.getChangeTime());
		return submitDate;
	}

	public Boolean getCanAudit() {
		if (super.canAudit != null) {
			return super.canAudit;
		}
		try {
			UserChangeDO user = (UserChangeDO) entity;
			Assert.notNull(user, "�û���������Ϊ��");
			Assert.notNull(login, "��¼��������Ϊ��");
			String sql = "select count(*) from " + UserBaseAuditBase.BAK_TABLE
					+ " where user_id=? and id=? and change_user!=?";
			int count = dao.findForInt(sql, new Object[] { user.getUserId(),
					user.getId(), login.getUserId() });
			if (count > 0) {
				super.canAudit = new Boolean(true);
			} else {
				super.canAudit = new Boolean(false);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return super.canAudit;
	}

}
