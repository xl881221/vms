package fmss.action.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import fmss.action.entity.UBaseConfigChangeDO;
import fmss.common.util.ArrayUtil;

public class SubSystemChangeAuditBase extends AuditBase {
	
	public static final String CFG_BAK_TABLE = "u_base_config_change";

	public static final String CFG_MAIN_TABLE = "u_base_config";
	
	private static String[] baseCfgAttributeFields = new String[] { "systemId", "systemEname", "systemNickEname", "systemCname",
		"systemNickCname", "dbUrl", "menuName", "menuTable", "menuOrderNum", "menuImgSrc", "linkTarget", "linkSiteUrl", "unitLoginUrl",
		"display", "resDbType", "resDbUserId","resDbServerPort","resDbServerIp", "resDbSid", "resDbPassword", "enabled", "menuRes", "linkSiteInnerUrl", "unitLoginInnerUrl"};
	
	private static String[] appendAttributeFields = new String[] { "changeUser", "changeTime", "auditUser",
		"auditTime", "auditStatus", "id", "changeStatus" };
	
	public static String[] fullBaseCfgAttributeFields = ArrayUtil
	.concat(baseCfgAttributeFields, appendAttributeFields);
	
	private static String[] baseCfgColumnFields = new String[] { "SYSTEM_ID", "SYSTEM_ENAME", "SYSTEM_NICK_ENAME", "SYSTEM_CNAME",
		"SYSTEM_NICK_CNAME", "DB_URL", "MENU_NAME", "MENU_TABLE", "MENU_ORDER_NUM", "MENU_IMG_SRC", "LINK_TARGET", "LINK_SITE_URL", "UNIT_LOGIN_URL",
		"DISPLAY", "RES_DB_TYPE", "RES_DB_USER_ID","RES_DB_SERVER_PORT","RES_DB_SERVER_IP", "RES_DB_SID", "RES_DB_PASSWORD", "ENABLED", "MENURES", "LINK_SITE_INNER_URL", "UNIT_LOGIN_INNER_URL"};
	
	private static String[] appendColumnFields = new String[] { "CHANGE_USER", "CHANGE_TIME", "AUDIT_USER",
		"AUDIT_TIME", "AUDIT_STATUS", "ID", "CHANGE_STATUS" };
	
	public static String[] fullBaseCfgColumnFields = ArrayUtil.concat(baseCfgColumnFields, appendColumnFields);


	public void auditThis(Object o) {
		UBaseConfigChangeDO info = (UBaseConfigChangeDO) o;
		dao.update("update " + CFG_BAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where system_id=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
						info.getAuditStatus(), info.getSystemId() });
	}

	public void flush2Prime(Object o) {
		UBaseConfigChangeDO info = (UBaseConfigChangeDO) o;
		String sql = "select count(*) from " + CFG_BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and system_id=? ";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { info.getSystemId() });
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
				String sql2 = "update "
						+ CFG_MAIN_TABLE
						+ " set SYSTEM_ENAME=?,SYSTEM_NICK_ENAME=?,SYSTEM_CNAME=?,SYSTEM_NICK_CNAME=?,DISPLAY=?"
						+ ",ENABLED=?,MENU_ORDER_NUM=?,UNIT_LOGIN_URL=?,LINK_SITE_URL=?,MENU_IMG_SRC=?,DB_URL=?,LINK_SITE_INNER_URL=?,UNIT_LOGIN_INNER_URL=? where SYSTEM_ID=?";
				dao.update(sql2, new Object[] { info.getSystemEname(),
						info.getSystemNickEname(), info.getSystemCname(),
						info.getSystemNickCname(), info.getDisplay(),
						info.getEnabled(), info.getMenuOrderNum(),
						info.getUnitLoginUrl(), info.getLinkSiteUrl(),
						info.getMenuImgSrc(),info.getDbUrl(),info.getLinkSiteInnerUrl(),info.getUnitLoginInnerUrl(),info.getSystemId() });
			} catch (RuntimeException e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		
//		Assert.isTrue(getColumnFields().length == getValueFields().length);
//		String sql = "select count(*) from " + CFG_BAK_TABLE
//				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
//				+ " and param_id=?";
//		UBaseConfigChangeDO user = (UBaseConfigChangeDO) o;
//		int count = dao.findForInt(sql, new Object[] { user.getUserId() });
//		if (count > 0)
//			throw new AuditException("存在未审核的系统参数信息:" + user.getUserId());
//		dao.save(CFG_BAK_TABLE, o, getValueFields(), getColumnFields());
	}

}
