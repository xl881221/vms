package fmss.action.base;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.UserChangeDO;
import fmss.common.util.Constants;
import fmss.common.util.HexUtils;

public class UserBaseAuditBase extends AuditBase {

	public static final String BAK_TABLE = "u_base_user_change";

	public static final String MAIN_TABLE = "u_base_user";

	public static final String AUTH_TABLE = "u_auth_object";

	public void auditThis(Object o) {
		UserChangeDO info = (UserChangeDO) o;
		dao.update("update " + BAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where id=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
						info.getAuditStatus(), info.getId() });
	}

	public void flush2Prime(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		UserChangeDO info = (UserChangeDO) o;
		Object obj = dao.find(BAK_TABLE, info.getId());
		Assert.notNull(obj);
		Map map = (Map) obj;
		String sql = "select count(*) from " + BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED + " and id=?";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { info.getId() });
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
				// if edit
				if (CHANGE_TYPE_USER_MODIFY.equals(info.getChangeStatus())) {
					// 因为密码有非空约束，密码为空的，特殊处理一下
					if (StringUtils.isEmpty((String) map.get("password"))) {
						map.put("password", HexUtils.shaHex(getDefaultPWD(),
								map.get("user_id").toString()));
					}
					dao.update(MAIN_TABLE, obj, getValueFields(),
							getColumnFields(), " where user_id='"
									+ map.get("user_id").toString() + "'");
				}

				// if add
				if (CHANGE_TYPE_USER_ADD.equals(info.getChangeStatus())) {
					// nested transaction here
					dao
							.update(
									"insert into "
											+ AUTH_TABLE
											+ "(object_id,object_name,object_type) values(?,?,'USER')",
									new Object[] { map.get("user_id"),
											map.get("user_cname") });
					dao
							.save(MAIN_TABLE, o, getValueFields(),
									getColumnFields());
				}

				// if delete
				if (CHANGE_TYPE_USER_DELETE.equals(info.getChangeStatus())) {
					if (!"1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC))) {
						// 清除用户历史密码表
						dao
								.update(
										"delete from u_base_his_user_pwd where user_id=?",
										new Object[] { info.getUserId() });
						// 清除系统管理员管理员表
						dao
								.update(
										"delete from u_auth_system_admin where user_id=?",
										new Object[] { info.getUserId() });
						// 角色用户表
						dao.update(
								"delete from u_auth_role_user where user_id=?",
								new Object[] { info.getUserId() });
						// 系统日志
						dao.update(
								"delete from u_base_sys_log where user_id=?",
								new Object[] { info.getUserId() });
						// 用户表
						dao.update("delete from " + MAIN_TABLE
								+ " where user_id=?", new Object[] { info
								.getUserId() });
						// 权限主体表
						dao.update("delete from " + AUTH_TABLE
								+ " where object_id=?", new Object[] { info
								.getUserId() });
					} else {
						// 用户表
						dao.update("update " + MAIN_TABLE
								+ " set is_delete=? where user_id=?",
								new Object[] { "1", info.getUserId() });
					}
				}
				transactionManager.commit(status);
			} catch (RuntimeException e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		String sql = "select count(*) from " + BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and user_id=?";
		UserChangeDO user = (UserChangeDO) o;
		int count = dao.findForInt(sql, new Object[] { user.getUserId() });
		if (count > 0)
			throw new AuditException("存在未审核的用户修改信息:" + user.getUserId());
		dao.save(BAK_TABLE, o, getValueFields(), getColumnFields());
	}

	private String getDefaultPWD() {
		String defPwd = "000000";
		String strPwd = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_INITPWD);
		if (!"".equals(strPwd)) {
			defPwd = strPwd;
		}
		return defPwd;
	}

}
