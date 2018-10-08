package fmss.action.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.services.AuthRoleService;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


public abstract class AuditBase {

	protected transient Log logger = LogFactory.getLog(getClass());

	protected static JdbcDaoAccessor dao = (JdbcDaoAccessor) SpringContextUtils.getBean("jdbcDaoAccessor");

	protected static CacheManager cacheManager = (CacheManager) SpringContextUtils.getBean("cacheManager");

	public static final int AUDIT_STATUS_NOADUITED = 1;// 未审
	public static final int AUDIT_STATUS_APPROVED = 2;// 通过
	public static final int AUDIT_STATUS_REJECTED = 3;// 不通过
	public static final int AUDIT_STATUS_CANCEL = 4;// 废弃
	public static final int AUDIT_STATUS_HASAUDIT = 5;// 已审

	public static final int ROLE_RES_CHANGE_STATUS_DELETE = 3;// 删除
	public static final int ROLE_RES_CHANGE_STATUS_ADD = 2;// 增加
	public static final int ROLE_RES_CHANGE_STATUS_NORMAL = 1;// 正常
	public static final int ROLE_RES_CHANGE_STATUS_ABANDON = 4;// 废弃

	public static final int USER_ROLE_CHANGE_STATUS_DELETE = 3;// 删除
	public static final int USER_ROLE_CHANGE_STATUS_ADD = 2;// 增加
	public static final int USER_ROLE_CHANGE_STATUS_NORMAL = 1;// 正常
	public static final int USER_ROLE_CHANGE_STATUS_ABANDON = 4;// 废弃

	public static final Long CHANGE_TYPE_USER_ADD = new Long(1);// 用户新增
	public static final Long CHANGE_TYPE_USER_MODIFY = new Long(2);// 用户修改
	public static final Long CHANGE_TYPE_USER2ROLE = new Long(3);// 用户配角色
	public static final Long CHANGE_TYPE_ROLE2USER = new Long(4);// 角色配用户
	public static final Long CHANGE_TYPE_ROLE2RESOURCE = new Long(5);// 角色配资源
	public static final Long CHANGE_TYPE_USER_DELETE = new Long(6);// 用户删除
	public static final Long CHANGE_TYPE_INST_ADD = new Long(7);//    机构新增
	public static final Long CHANGE_TYPE_INST_MODIFY = new Long(8);// 机构修改
	public static final Long CHANGE_TYPE_INST_DELETE = new Long(9);// 机构删除
	public static final Long CHANGE_TYPE_SYS_PARAM_MODIFY = new Long(10);// 系统参数修改
	public static final Long CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY = new Long(11);// 子系统配置参数修改添加
	public static final Long CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE = new Long(12);// 子系统配置管理员删除
	public static final Long CHANGE_TYPE_SUB_SYSTEM_MODIFY = new Long(13);// 子系统修改
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_ADD = new Long(14);// 节假日类型新增
	public static final Long CHANGE_TYPE_HOLIDAY_ADD = new Long(16);// 节假日新增
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_DELETE = new Long(15);// 节假日类型删除
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_MODIFY = new Long(18);// 节假日类型修改
	public static final Long CHANGE_TYPE_HOLIDAY_MODIFY = new Long(19);// 节假日修改
	public static final Long CHANGE_TYPE_HOLIDAY_DELETE = new Long(17);// 节假日删除

	public static final Map USER_ROLE_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map OWN_USER_ROLE_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map ROLE_RES_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map OWN_ROLE_RES_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map AUDIT_STATUS_MAP = new LinkedHashMap();
	public static final Map TYPE_DESC_MAP = new LinkedHashMap();
	static {
		USER_ROLE_TYPE_DESC_MAP.put(new Long(0), "");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_NORMAL), "未变");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ADD), "新增");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_DELETE), "删除");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ABANDON), "撤回");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(0), "");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_NORMAL), "已有");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ADD), "已新增");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_DELETE), "已删除");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ABANDON), "撤回");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(0), "");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL), "未变");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ADD), "新增");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE), "删除");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ABANDON), "撤回");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(0), "");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL), "已有");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ADD), "已新增");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE), "已删除");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ABANDON), "撤回");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_NOADUITED), "未审");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_APPROVED), "通过");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_REJECTED), "不通过");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_CANCEL), "撤回");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_HASAUDIT), "已审");
		TYPE_DESC_MAP.put(new Long(0), "全部");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_ADD, "用户新增");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_MODIFY, "用户修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_DELETE, "用户删除");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER2ROLE, "用户角色修改");
		// TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_ROLE2USER, "用户角色修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_ROLE2RESOURCE, "角色资源修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_INST_MODIFY, "机构修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY, "系统参数修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY, "子系统管理员修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY, "子系统修改");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD, "节假日变更");
		//TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY, "节假日修改");
		//TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE, "节假日删除");
	}

	/**
	 * 审核id生成器类型
	 */
	public static final String AUDIT = "AUD";

	protected String[] columnFields;

	protected String[] valueFields;

	protected LoginDO login;

	/**
	 * 审核
	 * 
	 * @param id
	 */
	public void audit(Object id) {
		if (id instanceof Integer || id instanceof Long || id instanceof String) {
			flush2Prime(id);
		} else {
			throw new AuditException("invaid id");
		}
	}

	/**
	 * 修改保存
	 * 
	 * @param obj
	 */
	public void save(Object o) {
		onSave(o);
	}

	public abstract void auditThis(Object o);

	public abstract void flush2Prime(Object o);

	public abstract void onSave(Object o);

	public String[] getColumnFields() {
		return columnFields;
	}

	public void setColumnFields(String[] columnFields) {
		this.columnFields = columnFields;
	}

	public String[] getValueFields() {
		return valueFields;
	}

	public void setValueFields(String[] valueFields) {
		this.valueFields = valueFields;
	}

	public static JdbcDaoAccessor getDao() {
		return dao;
	}

	public void setLogin(LoginDO login) {
		this.login = login;
	}

	/**
	 * 限定范围为当前登录用户拥有的子系统对应的全量角色
	 */
	public static String appendHaveSystemAuthsRangeSQL(LoginDO args0) {
		Assert.notNull(args0, "登录参数不能为空");
		UBaseUserDO u = new UBaseUserDO();
		u.setUserId(args0.getUserId());
		AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
		Assert.notNull(authRoleService, "authRoleService不能为空");
		List sysList = authRoleService.getBaseConfig(u);
		List l = new ArrayList();
		if (CollectionUtils.isNotEmpty(sysList)) {
			for (Iterator iterator = sysList.iterator(); iterator.hasNext();) {
				UBaseConfigDO o = (UBaseConfigDO) iterator.next();
				l.add(o.getSystemId());
			}
		}
		String rangeSQL = " and role_id in(select role_id from u_auth_role where system_id in ('"
				+ StringUtils.join(l.iterator(), "','") + "'))";
		if (LogFactory.getLog(AuditBase.class).isDebugEnabled())
			LogFactory.getLog(AuditBase.class).debug("rangeSQL is :" + rangeSQL);
		return l.size() > 0 ? rangeSQL : "";
	}
	
	/**
	 * 限定范围为当前登录用户拥有的子系统对应的全量角色
	 */
	public static String appendHaveSystemAuthsRangeHQL(LoginDO args0) {
		Assert.notNull(args0, "登录参数不能为空");
		UBaseUserDO u = new UBaseUserDO();
		u.setUserId(args0.getUserId());
		AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
		Assert.notNull(authRoleService, "authRoleService不能为空");
		List sysList = authRoleService.getBaseConfig(u);
		List l = new ArrayList();
		if (CollectionUtils.isNotEmpty(sysList)) {
			for (Iterator iterator = sysList.iterator(); iterator.hasNext();) {
				UBaseConfigDO o = (UBaseConfigDO) iterator.next();
				l.add(o.getSystemId());
			}
		}
		String rangeSQL = " and roleId in(select roleId from UAuthRoleDO where systemId in ('"
				+ StringUtils.join(l.iterator(), "','") + "'))";
		if (LogFactory.getLog(AuditBase.class).isDebugEnabled())
			LogFactory.getLog(AuditBase.class).debug("rangeHQL is :" + rangeSQL);
		return l.size() > 0 ? rangeSQL : "";
	}

}
