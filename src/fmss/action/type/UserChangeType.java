package fmss.action.type;

import java.text.SimpleDateFormat;

import org.springframework.util.Assert;

import fmss.action.base.UserBaseAuditBase;
import fmss.action.entity.UserChangeDO;

public abstract class UserChangeType extends AbstractChangeType {

	public String getContentDesc() {
		Assert.notNull(entity);
		Assert.notNull(login, "登录参数不能为空");
		UserChangeDO user = (UserChangeDO) entity;
		return "用户名[" + (user.getUserCname() != null ? user.getUserCname()
				: "无法获取用户名") + "]修改人[" + user.getChangeUser() + "]";
	}

	public String getSubmitDate() {
		String submitDate = "";
		UserChangeDO user = (UserChangeDO) entity;
		Assert.notNull(user, "用户参数不能为空");
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
			Assert.notNull(user, "用户参数不能为空");
			Assert.notNull(login, "登录参数不能为空");
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
