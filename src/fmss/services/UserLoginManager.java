package fmss.services;

import org.apache.commons.lang.StringUtils;

import fmss.common.util.LoginUtil;

public class UserLoginManager {
	private LoginService loginService;

	public void setLoginService(LoginService a) {
		loginService = a;
	}

	public String getDetailUserStatus(String isUserLocked, String userLockedReson) {
		if (StringUtils.isEmpty(isUserLocked)) {
			return (String) LoginUtil.LOCK_USER_STATUS.get(LoginUtil.LOCK_NORMAL);
		}
		if (LoginUtil.LOCK_LOCKED.equals(isUserLocked)) {
			if (LoginUtil.LOCK_REASON_PWD_INVALID_COUNTS.equals(userLockedReson))
				return (String) LoginUtil.getDetailLockReason(userLockedReson, String.valueOf(loginService
						.getPWDInvalidCounts()));
			if (LoginUtil.LOCK_REASON_USER_OVERDUE.equals(userLockedReson))
				return (String) LoginUtil.getDetailLockReason(userLockedReson, String.valueOf(loginService
						.getUserOverdueDays()));
			if (LoginUtil.LOCK_REASON_BY_USER.equals(userLockedReson))
				return (String) LoginUtil.getDetailLockReason(userLockedReson, null);
			return (String) LoginUtil.LOCK_REASON_DESC.get(userLockedReson);
		}
		return (String) LoginUtil.LOCK_USER_STATUS.get(isUserLocked);
	}

}
