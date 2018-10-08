package fmss.action.base;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.action.entity.RoleResourceChangeDO;
import fmss.action.entity.SubSystemAdminChangeDO;
import fmss.action.entity.UBaseConfigChangeDO;
import fmss.action.entity.UBaseInstChangeDO;
import fmss.action.entity.UBaseSysParamChangeDO;
import fmss.action.entity.UserChangeDO;
import fmss.action.entity.UserRoleChangeDO;
import fmss.action.type.AbstractChangeType;
import fmss.action.type.Role2ResourceChangeType;
import fmss.action.type.SubSystemAdminChangeType;
import fmss.action.type.SubSystemAdminDeleteChangeType;
import fmss.action.type.SysParamChangeType;
import fmss.action.type.UBaseConfigChangeType;
import fmss.action.type.UBaseInstAddType;
import fmss.action.type.UBaseInstChangeType;
import fmss.action.type.UBaseInstDeleteChangeType;
import fmss.action.type.User2RoleChangeType;
import fmss.action.type.UserAddChangeType;
import fmss.action.type.UserDeleteChangeType;
import fmss.action.type.UserModifyChangeType;
import fmss.common.util.ArrayUtil;
import fmss.common.util.BeanUtil;
import fmss.dao.entity.BaseDO;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UAuthRoleResourceDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.services.AuthRoleService;
import fmss.services.DictionaryService;
import fmss.services.InstService;
import fmss.services.OnlineService;
import fmss.services.ParamConfigService;
import fmss.services.ResourceConfigService;
import fmss.services.UserLoginManager;
import fmss.services.UserService;
import fmss.common.util.Constants;
import fmss.common.util.PaginationList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.dao.config.Dialect;
import fmss.dao.config.DialectFactory;
import fmss.dao.config.MSSQLDialect;


public class HolidayTypeChangingService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8513225009234956892L;

	private static final Log LOG = LogFactory.getLog(HolidayTypeChangingService.class);

	private CacheManager cacheManager; // 缓存

	private UserLoginManager userLoginManager;//

	private UserService userService;
	
	private OnlineService onlineService;

	private AuthRoleService authRoleService;

	private JdbcDaoAccessor jdbcDaoAccessor;
	
	/** 机构信息服务类*/
	private InstService instService;
	
	/** 字典信息服务类*/
	private DictionaryService dicService;
	
	private ParamConfigService paramConfigService ;

	public ParamConfigService getParamConfigService() {
		return paramConfigService;
	}

	public void setParamConfigService(ParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	public DictionaryService getDicService() {
		return dicService;
	}

	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	public InstService getInstService() {
		return instService;
	}

	public void setInstService(InstService instService) {
		this.instService = instService;
	}

	private static String[] baseModifyAttributeFields = new String[] { "userId", "userCname", "instId", "tel",
			"mobile", "address", "email", "startDate", "endDate", "description" };

	private static String[] baseModifyColumnFields = new String[] { "user_id", "USER_CNAME", "INST_ID", "TEL",
			"MOBILE", "ADDRESS", "EMAIL", "START_DATE", "END_DATE", "DESCRIPTION" };

	private static String[] baseSPAttributeFields = new String[] { "password", "lastModifyDate", "wrongPwdCount",
			"wrongPwdDate", "isUserLocked", "userLockedReson", "lastLoginDate", "isFirstLogin" };

	private static String[] baseSPColumnFields = new String[] { "password", "last_modify_date", "wrong_pwd_count",
			"wrong_pwd_date", "is_user_locked", "user_locked_reson", "last_login_date", "is_first_login" };

	private static String[] baseAddAttributeFields = new String[] { "userId", "userEname", "userCname", "password",
			"instId", "tel", "mobile", "address", "email", "isFirstLogin", "isUserLocked", "startDate", "endDate",
			"createTime", "description" };

	private static String[] baseAddColumnFields = new String[] { "USER_ID", "USER_ENAME", "USER_CNAME", "PASSWORD",
			"INST_ID", "TEL", "MOBILE", "ADDRESS", "EMAIL", "IS_FIRST_LOGIN", "IS_USER_LOCKED", "START_DATE",
			"END_DATE", "CREATE_TIME", "DESCRIPTION" };

	private static String[] appendAttributeFields = new String[] { "changeUser", "changeTime", "auditUser",
			"auditTime", "auditStatus", "id", "changeStatus" };

	private static String[] appendColumnFields = new String[] { "change_user", "change_time", "audit_user",
			"audit_time", "audit_status", "id", "change_status" };
	
	private static String[] appendEnAttributeFields=new String[]{"enabled","isList"};
	
	private static String[] appendEnColumnFields=new String[]{"ENABLED","is_list"};

	private static String[] roleAttributeFields = new String[] { "userId", "roleId" };

	private static String[] roleColumnFields = new String[] { "user_id", "role_id" };

	private static String[] resourceAttributeFields = new String[] { "objectId", "resId", "resDetailValue",
			"resDetailName", "systemId" };

	private static String[] resourceColumnFields = new String[] { "object_id", "res_id", "res_detail_value",
			"res_detail_name", "system_id" };

	private static String[] fullResourceAttributeFields = ArrayUtil.concat(resourceAttributeFields,
			appendAttributeFields);

	private static String[] fullResourceColumnFields = ArrayUtil.concat(resourceColumnFields, appendColumnFields);

	private static String[] fullBaseAddAttributeFields = ArrayUtil
			.concat(baseAddAttributeFields, appendAttributeFields);

	private static String[] fullBaseAddColumnFields = ArrayUtil.concat(baseAddColumnFields, appendColumnFields);

	private static String[] changeRemarkAttributeField = new String[] { "changeRemark" };

	private static String[] changeRemarkColumnField = new String[] { "change_remark" };

	private static String[] fullBaseModifyAttributeFields = ArrayUtil.concat(baseModifyAttributeFields,
			appendAttributeFields);

	private static String[] fullBaseModifyColumnFields = ArrayUtil.concat(baseModifyColumnFields, appendColumnFields);

	private static String[] fullBaseModifyAndRemarkSPAttributeFields = ArrayUtil.concat(fullBaseModifyAttributeFields,
			ArrayUtil.concat(baseSPAttributeFields, changeRemarkAttributeField));

	private static String[] fullBaseModifyAndRemarkSPColumnFields = ArrayUtil.concat(fullBaseModifyColumnFields,
			ArrayUtil.concat(baseSPColumnFields, changeRemarkColumnField));

	private static String[] fullBaseDeleteAttributeFields = ArrayUtil.concat(baseAddAttributeFields,
			appendAttributeFields);

	private static String[] fullBaseDeleteColumnFields = ArrayUtil.concat(baseAddColumnFields, appendColumnFields);

	private static String[] fullRoleAttributeFields = ArrayUtil.concat(roleAttributeFields, appendAttributeFields);

	private static String[] fullRoleColumnFields = ArrayUtil.concat(roleColumnFields, appendColumnFields);
	
	private static String[] instAddAttributeFields = new String[] { "instId", "instName", "instSmpName", "parentInstId",
		"instLayer", "address", "zip", "tel", "fax", "isBussiness", "orderNum", "description", "startDate","endDate", "createTime", 
		"enabled","email","inst_region","inst_path","inst_level","is_head" };
	
	
	private static String[] fullInstAddAttributeFields = ArrayUtil.concat(instAddAttributeFields, appendAttributeFields);
	private static String[] instAddColumnFields = new String[] {"INST_ID", "INST_NAME", "INST_SMP_NAME", "PARENT_INST_ID",
		"INST_LAYER", "ADDRESS", "ZIP", "TEL", "FAX", "IS_BUSSINESS", "ORDER_NUM", "DESCRIPTION", "START_DATE","END_DATE", "CREATE_TIME", 
		"ENABLED", "EMAIL","INST_REGION","INST_PATH","INST_LEVEL","IS_HEAD" };
	
	private static String[] fullInstAddColumnFields = ArrayUtil.concat(instAddColumnFields, appendColumnFields);
	private static Dialect dialect;

	/**
	 * 用户信息更改比较
	 * 
	 * @param o1
	 *            new
	 * @param o2
	 *            old
	 * @return 值相同true,不同false
	 */
	private boolean compareBaseAttribute(UBaseUserDO o1, UBaseUserDO o2) {
		for (int i = 0; i < baseModifyAttributeFields.length; i++) {
			String attribute = baseModifyAttributeFields[i];
			Object value1 = BeanUtil.getProperty(o1, attribute);
			Object value2 = BeanUtil.getProperty(o2, attribute);
			if ("".equals(value1)) {
				value1 = null;
			}
			if ("".equals(value2)) {
				value2 = null;
			}
			if (value1 == null || value2 == null) {
				if (value2 != null) {
					return false;
				}
				if (value1 != null) {
					return false;
				}
				if (value2 == null && value1 == null)
					continue;
			}
			if (!value1.equals(value2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 修改
	 */
	public String saveBaseChanges(LoginDO login, UBaseUserDO o1, UBaseUserDO o2) {
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		if (!this.compareBaseAttribute(o1, o2)) {
			UserChangeDO o = new UserChangeDO();
			BeanUtils.copyProperties(o1, o);
			o.setChangeUser(login.getUserId());
			o.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
			long id = idGenerator.getNextKey();
			o.setId(new Long(id));
			o.setChangeStatus(AuditBase.CHANGE_TYPE_USER_MODIFY);
			o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

			o.getAuditEntity().setValueFields(ArrayUtil.concat(fullBaseModifyAttributeFields, baseSPAttributeFields));
			o.getAuditEntity().setColumnFields(ArrayUtil.concat(fullBaseModifyColumnFields, baseSPColumnFields));
			o.getAuditEntity().save(o);
			return "保存成功,待审核通过后生效";
		}
		return "";
	}

	/**
	 * 修改with change remark
	 */
	public void saveBaseChanges(LoginDO login, UBaseUserDO o1, UBaseUserDO o2, String changeRemark) {
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		// if (!this.compareBaseAttribute(o1, o2)) {
		// 不作用户信息修改比较，直接产生修改对象
		UserChangeDO o = new UserChangeDO();
		BeanUtils.copyProperties(o1, o);
		o.setChangeRemark(changeRemark);
		o.setChangeUser(login.getUserId());
		o.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
		long id = idGenerator.getNextKey();
		o.setId(new Long(id));
		o.setChangeStatus(AuditBase.CHANGE_TYPE_USER_MODIFY);
		o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

		o.getAuditEntity().setValueFields(ArrayUtil.concat(fullBaseModifyAndRemarkSPAttributeFields,appendEnAttributeFields));
		o.getAuditEntity().setColumnFields(ArrayUtil.concat(fullBaseModifyAndRemarkSPColumnFields,appendEnColumnFields));
		o.getAuditEntity().save(o);
		// }
	}

	/**
	 * 新增
	 */
	public void saveBaseChanges(LoginDO login, UBaseUserDO o1) {
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		UserChangeDO o = new UserChangeDO();
		BeanUtils.copyProperties(o1, o);
		long id = idGenerator.getNextKey();
		o.setChangeUser(login.getUserId());
		o.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
		o.setId(new Long(id));
		o.setChangeStatus(AuditBase.CHANGE_TYPE_USER_ADD);
		o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

		o.getAuditEntity().setValueFields(fullBaseAddAttributeFields);
		o.getAuditEntity().setColumnFields(fullBaseAddColumnFields);
		o.getAuditEntity().save(o);
	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void saveBaseChanges(LoginDO login, String[] users) throws Exception {
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);

		if (ArrayUtils.isEmpty(users))
			return;
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(AuditBase.getDao()
				.getDataSource());
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// transaction here
			for (int i = 0; i < users.length; i++) {
				String userId = users[i];
				UBaseUserDO user = getUser(userId);
				UserChangeDO o = new UserChangeDO();
				long id = idGenerator.getNextKey();
				BeanUtils.copyProperties(user, o);
				o.setUserId(userId);
				o.setChangeUser(login.getUserId());
				o.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
				o.setId(new Long(id));
				o.setChangeStatus(AuditBase.CHANGE_TYPE_USER_DELETE);
				o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

				o.getAuditEntity().setValueFields(fullBaseDeleteAttributeFields);
				o.getAuditEntity().setColumnFields(fullBaseDeleteColumnFields);
				o.getAuditEntity().save(o);
			}
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}

	}

	public String saveRoleChanges(LoginDO login, UBaseUserDO user, String[] roles) {
		List l = new ArrayList();
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		List list = this.getUserRoles(user, roles, login);
		if (!this.compareRoleAttribute(list)) {
			// todo role changes
			long id = idGenerator.getNextKey();
			// transaction here
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(AuditBase.getDao()
					.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					UserRoleChangeDO obj = (UserRoleChangeDO) iterator.next();
					// BeanUtils.copyProperties(o1, o);
					obj.setChangeUser(login.getUserId());
					obj.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
					obj.setId(new Long(id));
					obj.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

					obj.getAuditEntity().setValueFields(fullRoleAttributeFields);
					obj.getAuditEntity().setColumnFields(fullRoleColumnFields);

					// 如果已存在——不添加
					String sql = "select count(*) from " + UserRoleAuditBase.BAK_TABLE
							+ " where user_id=? and role_id=? and change_status=? and audit_status=?";
					int count = jdbcDaoAccessor.findForInt(sql, new Object[] { obj.getUserId(), obj.getRoleId(),
							obj.getChangeStatus(), new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
					if (count > 0) {
						LOG.debug("exist same change, ignore and need prompt to user");
						UAuthRoleDO o = getRole(obj.getRoleId());
						l.add(o != null ? o.getRoleName() : "无法获取角色名");
						continue;
					}
					obj.getAuditEntity().save(obj);

				}
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
				throw new AuditException("saveRoleChanges error", e);
			}
		}
		StringBuffer sb = new StringBuffer();
		if (CollectionUtils.isNotEmpty(l)) {
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				String s = (String) iterator.next();
				sb.append(s + " ");
			}
			sb = new StringBuffer(sb.toString().trim());
			sb.insert(0, "保存成功:角色[").append("]在变更审核流程中,无法保存,已忽略");
		}
		return sb.toString();
	}

	public String saveResourceChanges(LoginDO login, String roleId, String resourcesString) {
		// BASE INFO CHANGES
		// 初始化id生成器
		if (StringUtils.isNotEmpty(resourcesString)) {
			String globalResId = "";
			List resources = new ArrayList();
			String[] strArray = resourcesString.split(";");
			/*
			 * 分隔每行的值和值对应的名字,依次为 resID,resValue,resName,以&,分割
			 * 形成角色-资源对应关系，然后保存角色-资源关系表
			 */
			login.addDescription(",资源[");
			for (int i = 0; i < strArray.length; i++) {
				String value = strArray[i];
				String[] valueArray = value.split("\\" + ResourceConfigService.SPERATOR);
				if (valueArray != null && valueArray.length >= 3) {
					String resId = valueArray[0];
					String resValue = valueArray[1];
					String resName = valueArray[2];
					String systemId = valueArray[3];
					RoleResourceChangeDO rr = new RoleResourceChangeDO();
					rr.setResDetailValue(resValue);
					rr.setResDetailName(resName);
					rr.setResId(resId);// fixed
					rr.setObjectId(roleId);// fixed
					rr.setSystemId(systemId);// fixed
					resources.add(rr);
					login.addDescription(resValue).addDescription("-").addDescription(resName).addDescription(" ");
					globalResId = resId;
				}
			}
			login.addDescription("]");
			List list = this.getRoleResources(roleId, globalResId, resources);
			LOG.info("save resource change:" + list.size());
			return saveResourceChanges(login, list);
		}
		return "";
	}

	public String saveResourceChanges(LoginDO login, String roleId, List resMapList) {
		// BASE INFO CHANGES
		// 初始化id生成器

		if (CollectionUtils.isNotEmpty(resMapList)) {
			String globalResId = "";
			List resources = new ArrayList();
			/*
			 * 形成角色-资源对应关系，然后删除角色-资源关系表
			 */
			login.addDescription(",资源[");
			for (Iterator iterator = resMapList.iterator(); iterator.hasNext();) {
				String value = iterator.next().toString();
				String[] valueArray = value.split(";");
				
				if (valueArray != null && valueArray.length >= 2) {
					String resId = valueArray[0];
					String resValue = valueArray[1];
					String systemId = valueArray[2];
					RoleResourceChangeDO urr = new RoleResourceChangeDO();
					urr.setObjectId(roleId);
					urr.setResId(resId);
					urr.setResDetailValue(resValue);
					urr.setSystemId(systemId);
					urr.setResDetailName(getAuthRoleResource(roleId, resId, resValue).getResDetailName());
					urr.setChangeStatus(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE));
					login.addDescription(resValue).addDescription("-").addDescription(valueArray.length < 4 ? resValue : valueArray[3]).addDescription(" ");
					resources.add(urr);
					globalResId = resId;
				}
			}
			login.addDescription("]");
			// List list = this.getRoleResources(roleId, globalResId,
			// resources);
			return saveResourceChanges(login, resources);
		}
		return "";
	}

	public String saveResourceChanges(LoginDO login, List list) {
		List l = new ArrayList();
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		if (!this.compareResourceAttribute(list)) {
			// todo role changes
			long id = idGenerator.getNextKey();
			// new
			// transaction here
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(AuditBase.getDao()
					.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					RoleResourceChangeDO obj = (RoleResourceChangeDO) iterator.next();
					// BeanUtils.copyProperties(o1, o);
					obj.setChangeUser(login.getUserId());
					obj.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
					obj.setId(new Long(id));
					obj.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

					obj.getAuditEntity().setValueFields(fullResourceAttributeFields);
					obj.getAuditEntity().setColumnFields(fullResourceColumnFields);
					// 如果存在之前任何人提交的相同状态且未审的——不添加
					String sql = "select count(*) from "
							+ RoleResourceAuditBase.BAK_TABLE
							+ " where object_id=? and res_id=? and res_detail_value=? and system_id=? and change_status=? and audit_status=?";
					int count = jdbcDaoAccessor.findForInt(sql, new Object[] { obj.getObjectId(), obj.getResId(),
							obj.getResDetailValue(), obj.getSystemId(), obj.getChangeStatus(),
							new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
					if (count > 0) {
						LOG.debug("exist same change, ignore and need prompt to user");
						l.add(obj.getResDetailValue());
						continue;
					}
					obj.getAuditEntity().save(obj);
					LOG.debug("save one change");
				}
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
				throw new AuditException("saveRoleChanges error", e);
			}
		}
		StringBuffer sb = new StringBuffer();
		if (CollectionUtils.isNotEmpty(l)) {
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				String s = (String) iterator.next();
				sb.append(s + ",");
			}
			sb = new StringBuffer(sb.toString().trim());
			sb.insert(0, "提交失败:\n资源[").append("]\n在变更审核流程中,无法提交,已忽略");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param login
	 * @param o1
	 *            new
	 * @param o2
	 *            old
	 */
	public String saveRoleChanges(LoginDO login, UAuthRoleDO role, String[] users, boolean flag) {
		List l = new ArrayList();
		// BASE INFO CHANGES
		// 初始化id生成器
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		// List list = this.getUserRoles(role, users);
		if (ArrayUtils.isEmpty(users))
			return "";
		List list = Collections.EMPTY_LIST;
		if (flag)
			list = fillEntityFromUserArray(users, role.getRoleId(), AuditBase.USER_ROLE_CHANGE_STATUS_ADD);
		else
			list = fillEntityFromUserArray(users, role.getRoleId(), AuditBase.USER_ROLE_CHANGE_STATUS_DELETE);
		if (!this.compareRoleAttribute(list)) {
			// todo role changes
			long id = idGenerator.getNextKey();
			// new
			// transaction here
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(AuditBase.getDao()
					.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(definition);
			try {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					UserRoleChangeDO obj = (UserRoleChangeDO) iterator.next();
					// BeanUtils.copyProperties(o1, o);
					obj.setChangeUser(login.getUserId());
					obj.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
					obj.setId(new Long(id));
					obj.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));

					obj.getAuditEntity().setValueFields(fullRoleAttributeFields);
					obj.getAuditEntity().setColumnFields(fullRoleColumnFields);
					// 如果已存在——不添加
					String sql = "select count(*) from " + UserRoleAuditBase.BAK_TABLE
							+ " where user_id=? and role_id=? and change_status=? and audit_status=?";
					int count = jdbcDaoAccessor.findForInt(sql, new Object[] { obj.getUserId(), obj.getRoleId(),
							obj.getChangeStatus(), new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
					if (count > 0) {
						LOG.debug("exist same change, ignore and need prompt to user");
						l.add(obj.getUserId());
						continue;
					}
					obj.getAuditEntity().save(obj);
				}
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
				throw new AuditException("saveRoleChanges error", e);
			}
		}
		StringBuffer sb = new StringBuffer();
		if (CollectionUtils.isNotEmpty(l)) {
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				String s = (String) iterator.next();
				sb.append(s + " ");
			}
			sb = new StringBuffer(sb.toString().trim());
			sb.insert(0, "保存成功:存在用户[").append("]在变更审核流程中,无法保存,已忽略");
		}
		return sb.toString();
	}

	public String saveRoleChanges(LoginDO login, UAuthRoleDO role, List users, boolean flag) {
		String[] usersArray = (String[]) users.toArray(new String[users.size()]);
		return saveRoleChanges(login, role, usersArray, flag);
	}

	private UserRoleChangeDO transUserRoleMap2Object(Map map) {
		UserRoleChangeDO role = new UserRoleChangeDO();
		role.setRoleId(map.get("role_id").toString());
		role.setUserId(map.get("user_id").toString());
		return role;
	}

	/**
	 * @param user
	 * @param roles
	 * @return 只存在未变的，为false
	 */
	private boolean compareRoleAttribute(List list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				UserRoleChangeDO o = (UserRoleChangeDO) iterator.next();
				if (AuditBase.ROLE_RES_CHANGE_STATUS_ADD == o.getChangeStatus().longValue()
						|| AuditBase.ROLE_RES_CHANGE_STATUS_DELETE == o.getChangeStatus().longValue()) {
					return false;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * 资源更新比较
	 * 
	 * @param user
	 * @param roles
	 * @return 只存在未变的，为false
	 */
	private boolean compareResourceAttribute(List list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				RoleResourceChangeDO o = (RoleResourceChangeDO) iterator.next();
				if (AuditBase.ROLE_RES_CHANGE_STATUS_ADD == o.getChangeStatus().longValue()
						|| AuditBase.ROLE_RES_CHANGE_STATUS_DELETE == o.getChangeStatus().longValue()) {
					return false;
				}
			}
			return true;
		}
		return true;
	}

	private List fillEntityStatusFromResource(List resources, int status) {
		for (int i = 0; i < resources.size(); i++) {
			RoleResourceChangeDO o = (RoleResourceChangeDO) resources.get(i);
			o.setChangeStatus(new Long(status));
		}
		return resources;
	}

	private List fillEntityStatusFromRole(List roles, int status) {
		for (int i = 0; i < roles.size(); i++) {
			UserRoleChangeDO o = (UserRoleChangeDO) roles.get(i);
			o.setChangeStatus(new Long(status));
		}
		return roles;
	}

	private List fillEntityFromRoleArray(String[] roles, String userId, int status) {
		List list = new ArrayList();
		for (int i = 0; i < roles.length; i++) {
			UserRoleChangeDO o = new UserRoleChangeDO();
			o.setUserId(userId);
			o.setRoleId(roles[i]);
			o.setChangeStatus(new Long(status));
			list.add(o);
		}
		return list;
	}

	private List fillEntityFromUserArray(String[] users, String roleId, int status) {
		List list = new ArrayList();
		for (int i = 0; i < users.length; i++) {
			UserRoleChangeDO o = new UserRoleChangeDO();
			o.setUserId(users[i]);
			o.setRoleId(roleId);
			o.setChangeStatus(new Long(status));
			list.add(o);
		}
		return list;
	}

	/**
	 * 角色更改比较
	 * 
	 */
	private List getUserRoles(UBaseUserDO user, String[] newRoles, LoginDO login) {
		List results = new ArrayList();
		// 因为复选框的特殊性无法确定在界面上增减了哪些用户
		// 所以取用户拥有角色进行加减时，必须判断登录用户的所属子系统以缩小加减范围，保证范围与页面上配置角色显示子系统页卡一致

		List hasRoles = jdbcDaoAccessor.find("select user_id,role_id from " + UserRoleAuditBase.MAIN_TABLE
				+ " where user_id=?" + AuditBase.appendHaveSystemAuthsRangeSQL(login), new Object[] { user.getUserId() });

		// transform
		String[] oldRoles = new String[hasRoles.size()];
		for (int i = 0; i < hasRoles.size(); i++) {
			Map row = (Map) hasRoles.get(i);
			oldRoles[i] = row.get("role_id").toString();
		}
		if (ArrayUtils.isEmpty(newRoles) && hasRoles.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		if (!ArrayUtils.isEmpty(newRoles)) {
			// String[] normals = ArrayUtil.intersect(newRoles, oldRoles);
			String[] adds = ArrayUtil.minus(newRoles, oldRoles);
			String[] deletes = ArrayUtil.minus(oldRoles, newRoles);

			// List normalRoles = fillEntityFromRoleArray(normals, user
			// .getUserId(), AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
			List addRoles = fillEntityFromRoleArray(adds, user.getUserId(), AuditBase.ROLE_RES_CHANGE_STATUS_ADD);
			List deleteRoles = fillEntityFromRoleArray(deletes, user.getUserId(),
					AuditBase.ROLE_RES_CHANGE_STATUS_DELETE);
			// 正常不添加到表
			// results.addAll(normalRoles);
			results.addAll(addRoles);
			results.addAll(deleteRoles);
		} else {
			// 为空则删除原来
			for (int i = 0; i < hasRoles.size(); i++) {
				Map row = (Map) hasRoles.get(i);
				UserRoleChangeDO o = this.transUserRoleMap2Object(row);
				o.setChangeStatus(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE));
				results.add(o);
			}
		}
		return results;
	}

	private List getUserRoles(UAuthRoleDO role, String[] users) {
		List results = new ArrayList();
		List hasUsers = this.jdbcDaoAccessor.find("select user_id,role_id from " + UserRoleAuditBase.MAIN_TABLE
				+ " where role_id=?", new Object[] { role.getRoleId() });
		// transform
		String[] oldUsers = new String[hasUsers.size()];
		for (int i = 0; i < hasUsers.size(); i++) {
			Map row = (Map) hasUsers.get(i);
			oldUsers[i] = row.get("user_id").toString();
		}
		if (ArrayUtils.isEmpty(users) && hasUsers.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		if (!ArrayUtils.isEmpty(users)) {
			// String[] normals = ArrayUtil.intersect(users, oldUsers);
			String[] adds = ArrayUtil.minus(users, oldUsers);
			String[] deletes = ArrayUtil.minus(oldUsers, users);

			// List normalRoles = fillEntityFromUserArray(normals, role
			// .getRoleId(), AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
			List addRoles = fillEntityFromUserArray(adds, role.getRoleId(), AuditBase.ROLE_RES_CHANGE_STATUS_ADD);
			List deleteRoles = fillEntityFromUserArray(deletes, role.getRoleId(),
					AuditBase.ROLE_RES_CHANGE_STATUS_DELETE);
			// 正常不添加到表
			// results.addAll(normalRoles);
			results.addAll(addRoles);
			results.addAll(deleteRoles);
		} else {
			// 为空则删除原来
			for (int i = 0; i < hasUsers.size(); i++) {
				Map row = (Map) hasUsers.get(i);
				UserRoleChangeDO o = this.transUserRoleMap2Object(row);
				o.setChangeStatus(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE));
				results.add(o);
			}
		}
		return results;
	}

	/**
	 * 角色资源更改比较
	 * 
	 */
	private List getRoleResources(String roleId, String resId, List newResources) {
		List results = new ArrayList();

		// 取还未审核的
		List hasResources = this.jdbcDaoAccessor.find("select * from " + RoleResourceAuditBase.MAIN_TABLE
				+ " where object_id=? and res_id=?", new Object[] { roleId, resId });
		// transform
		List olds = new ArrayList();
		for (int i = 0; i < hasResources.size(); i++) {
			Map row = (Map) hasResources.get(i);
			RoleResourceChangeDO o = new RoleResourceChangeDO();
			o.setResDetailValue(row.get("res_detail_value").toString());
			o.setResId(row.get("res_id").toString());
			o.setObjectId(row.get("object_id").toString());
			o.setSystemId(row.get("system_id").toString());
			olds.add(o);
		}
		if (CollectionUtils.isNotEmpty(newResources)) {
			// List normals = RoleResourceUtil.intersect(newResources, olds);
			List adds = RoleResourceUtil.minus(newResources, olds);
			// List deletes = RoleResourceUtil.minus(olds, newResources);

			// List normalResources = fillEntityStatusFromResource(normals,
			// AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
			List addResources = fillEntityStatusFromResource(adds, AuditBase.ROLE_RES_CHANGE_STATUS_ADD);
			// List deleteResources = fillEntityStatusFromResource(deletes,
			// AuditBase.ROLE_RES_CHANGE_STATUS_DELETE);
			// 正常不添加到表
			// results.addAll(normalResources);
			results.addAll(addResources);
			// results.addAll(deleteResources);
		} else {
			// 为空则删除原来
			for (int i = 0; i < olds.size(); i++) {
				RoleResourceChangeDO o = (RoleResourceChangeDO) olds.get(i);
				o.setChangeStatus(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE));
				results.add(o);
			}
		}
		return results;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor) {
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}

	/** ****************show for page action************************ */

	public PaginationList getFullAuditInfos(LoginDO login, Long auditStatus, String changeUser, String auditUser,
			Long change, PaginationList page, String excludeChangeUser, boolean fetchSystemAuths) throws Exception {
		List list = new ArrayList();
		List auditInfos = new ArrayList();
		try {
			// user
			String sql0 = "select user_id object_id,change_time,change_status change_type,id from "
					+ UserBaseAuditBase.BAK_TABLE + " where 1=1 ";
			if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))) {
				// 用户新增审核关闭
				sql0 += " and change_status!=1 ";
			}
			if (auditStatus != null) {
				sql0 += " and audit_status=" + auditStatus.intValue();
			}
			if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
				sql0 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
			} else {
				if (StringUtils.isNotEmpty(changeUser))
					sql0 += " and change_user='" + changeUser + "'";
				if (StringUtils.isNotEmpty(auditUser))
					sql0 += " and audit_user='" + auditUser + "'";
			}
			if (StringUtils.isNotEmpty(excludeChangeUser))
				sql0 += " and change_user!='" + excludeChangeUser + "'";
			// role
			String sql1 = "select user_id object_id,max(change_time) change_time," + AuditBase.CHANGE_TYPE_USER2ROLE
					+ " change_type,0 id from " + UserRoleAuditBase.BAK_TABLE + " where 1=1";
			if (auditStatus != null) {
				sql1 += " and audit_status=" + auditStatus.intValue();
			}
			if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
				sql1 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
			} else {
				if (StringUtils.isNotEmpty(changeUser))
					sql1 += " and change_user='" + changeUser + "'";
				if (StringUtils.isNotEmpty(auditUser))
					sql1 += " and audit_user='" + auditUser + "'";
			}
			if (StringUtils.isNotEmpty(excludeChangeUser))
				sql1 += " and change_user!='" + excludeChangeUser + "'";
			if (fetchSystemAuths)
				sql1 += AuditBase.appendHaveSystemAuthsRangeSQL(login);
			sql1 += " group by user_id";
			// resource
			String sql2 = "select object_id,max(change_time) change_time," + AuditBase.CHANGE_TYPE_ROLE2RESOURCE
					+ " change_type,0 id from " + RoleResourceAuditBase.BAK_TABLE + " where 1=1";
			if (auditStatus != null) {
				sql2 += " and audit_status=" + auditStatus.intValue();
			}
			if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
				sql2 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
			} else {
				if (StringUtils.isNotEmpty(changeUser))
					sql2 += " and change_user='" + changeUser + "'";
				if (StringUtils.isNotEmpty(auditUser))
					sql2 += " and audit_user='" + auditUser + "'";
			}
			if (StringUtils.isNotEmpty(excludeChangeUser))
				sql2 += " and change_user!='" + excludeChangeUser + "'";
			// 取自己角色所拥有子系统对应的角色修改
			sql2 += " and object_id in (select role_id from u_auth_role where 1=1 " + AuditBase.appendHaveSystemAuthsRangeSQL(login) + ")";
			sql2 += " group by object_id";
			//机构
			String sql3 = "select inst_id object_id,change_time,change_status change_type,id from "
				+ InstBaseAuditBase.IBAK_TABLE + " where 1=1 ";
		if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_INST_AUDIT))) {
			// 机构新增审核关闭
			sql3 += " and change_status!=1 ";
		}
		if (auditStatus != null) {
			sql3 += " and audit_status=" + auditStatus.intValue();
		}
		if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
			sql3 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
		} else {
			if (StringUtils.isNotEmpty(changeUser))
				sql3 += " and change_user='" + changeUser + "'";
			if (StringUtils.isNotEmpty(auditUser))
				sql3 += " and audit_user='" + auditUser + "'";
		}
		if (StringUtils.isNotEmpty(excludeChangeUser))
			sql3 += " and change_user!='" + excludeChangeUser + "'";
		
		// Sys_param
		String sql4 = "select param_id object_id,change_time,change_status change_type,id from "
				+ SysParamAuditBase.SPBAK_TABLE + " where 1=1 ";
		if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT))) {
			//子系统信息配置审核关闭
			sql4 += " and change_status!=1 ";
		}
		if (auditStatus != null) {
			sql4 += " and audit_status=" + auditStatus.intValue();
		}
		if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
			sql4 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
		} else {
			if (StringUtils.isNotEmpty(changeUser))
				sql4 += " and change_user='" + changeUser + "'";
			if (StringUtils.isNotEmpty(auditUser))
				sql4 += " and audit_user='" + auditUser + "'";
		}
		if (StringUtils.isNotEmpty(excludeChangeUser))
			sql4 += " and change_user!='" + excludeChangeUser + "'";
		// userAdmin
		String sql5 = "select user_id object_id,change_time,change_status change_type,id from "
				+ SubSystemAdminAuditBase.SUB_BAK_TABLE + " where 1=1 ";
		if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT))) {
			// 子系统信息配置审核关闭
			sql5 += " and change_status!=1 ";
		}
		if (auditStatus != null) {
			sql5 += " and audit_status=" + auditStatus.intValue();
		}
		if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
			sql5 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
		} else {
			if (StringUtils.isNotEmpty(changeUser))
				sql5 += " and change_user='" + changeUser + "'";
			if (StringUtils.isNotEmpty(auditUser))
				sql5 += " and audit_user='" + auditUser + "'";
		}
		if (StringUtils.isNotEmpty(excludeChangeUser))
			sql5 += " and change_user!='" + excludeChangeUser + "'";
		// subSystemConfig
		String sql6 = "select system_id object_id,change_time,change_status change_type,id from "
				+ SubSystemChangeAuditBase.CFG_BAK_TABLE + " where 1=1 ";
		if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT))) {
			// 子系统信息配置审核关闭
			sql6 += " and change_status!=1 ";
		}
		if (auditStatus != null) {
			sql6 += " and audit_status=" + auditStatus.intValue();
		}
		if (StringUtils.isNotEmpty(changeUser) && StringUtils.isNotEmpty(auditUser)) {
			sql6 += " and (change_user='" + changeUser + "'" + " or audit_user='" + auditUser + "')";
		} else {
			if (StringUtils.isNotEmpty(changeUser))
				sql6 += " and change_user='" + changeUser + "'";
			if (StringUtils.isNotEmpty(auditUser))
				sql6 += " and audit_user='" + auditUser + "'";
		}
		if (StringUtils.isNotEmpty(excludeChangeUser))
			sql6 += " and change_user!='" + excludeChangeUser + "'";
		
		if (dialect == null)
			dialect = DialectFactory.getDialect(jdbcDaoAccessor.getDataSource());
		
			//分级审核控制 
			String sqlTemp="select t1.object_id,t1.change_time,t1.change_type,t1.id from (#1#) t1,#2# t2,u_base_inst t5,"+
					            "(select t4.inst_path,t4.is_head from u_base_user t3,u_base_inst t4 "+
					                 "where t3.user_id='"+login.getUserId()+"' and t4.inst_id=t3.inst_id "+
					                ") t6 "+
				"where t1.object_id=t2.user_id and t5.inst_id=t2.inst_id "+ 
				"and ("+((dialect instanceof MSSQLDialect)?"substring":"substr")+"(t5.inst_path,1,"+((dialect instanceof MSSQLDialect)?"len":"length")+"(t6.inst_path))=t6.inst_path or t6.is_head='true')";
			
			sql0=sqlTemp.replaceAll("#1#", sql0).replaceAll("#2#", "u_base_user_change");
			sql0=sql0+"and t1.id=t2.id";
			sql1=sqlTemp.replaceAll("#1#", sql1);
			sql2="select a.object_id,a.change_time,a.change_type,a.id from ("+sql2+") a where 'true'='"+login.getInstIsHead()+"'";
//			
			String sql = sql0 + " union all " + sql1 + " union all " + sql2
					+ " union all " + sql3 + " union all " + sql4
					+ " union all " + sql5 + " union all " + sql6;
			
			if (change != null && change.longValue() > 0) {
				if (change.equals(AuditBase.CHANGE_TYPE_USER_ADD) || change.equals(AuditBase.CHANGE_TYPE_USER_MODIFY)
						|| change.equals(AuditBase.CHANGE_TYPE_USER_DELETE)){
					sql = sql0 + " and change_type=" + change.intValue();
				}
				else if (change.equals(AuditBase.CHANGE_TYPE_USER2ROLE)
						|| change.equals(AuditBase.CHANGE_TYPE_ROLE2USER))
					sql = sql1;
				else if (change.equals(AuditBase.CHANGE_TYPE_ROLE2RESOURCE))
					sql = sql2;
				else if (change.equals(AuditBase.CHANGE_TYPE_INST_MODIFY)
						|| change.equals(AuditBase.CHANGE_TYPE_INST_ADD)
						|| change.equals(AuditBase.CHANGE_TYPE_INST_DELETE))
					sql = sql3;
				else if (change.equals(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY))
					sql = sql4;
				else if (change.equals(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY))
					sql = sql5;
				else if (change.equals(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY))
					sql = sql6;
			}
			sql=sql.replaceAll("#2#", "u_base_user");
			
			// Pagination here
			
			Assert.notNull(dialect);
			page.setRecordCount(jdbcDaoAccessor.findForLong(dialect.getRowCountString(sql)));
			if(!(dialect instanceof MSSQLDialect))
				sql += " order by change_time desc ";
			int startIndex = (page.getCurrentPage() - 1) * page.getPageSize();
			String pageSql = dialect.getLimitedString(sql, startIndex, page.getPageSize());
			// List authoritys =
			// userService.getJuniorUserByInstId(login.getUserId());
			if(dialect instanceof MSSQLDialect)
				sql += " order by change_time desc ";
			list = jdbcDaoAccessor.find(pageSql);
			
			for (Iterator iterator0 = list.iterator(); iterator0.hasNext();) {
				Map map = (Map) iterator0.next();
				String objectId = (String) map.get("object_id");
				Long ids = new Long(map.get("id").toString());
				java.util.Date changeTime = (java.util.Date) map.get("change_time");
				if (map.get("change_type") instanceof Number) {
					Long changeType = new Long(map.get("change_type").toString());
					BaseDO o = null;
					AbstractChangeType obj = null;
					if (changeType.equals(AuditBase.CHANGE_TYPE_USER_ADD)) {
						Long id = new Long(map.get("id").toString());
						obj = new UserAddChangeType();
						o = getUserChanges(id);
					} else if (changeType.equals(AuditBase.CHANGE_TYPE_USER_MODIFY)) {
						Long id = new Long(map.get("id").toString());
						obj = new UserModifyChangeType();
						o = getUserChanges(id);
						UBaseUserDO user = this.getUser(objectId);
						BeanUtils.copyProperties(user, o);
					} else if (changeType.equals(AuditBase.CHANGE_TYPE_USER_DELETE)) {
						Long id = new Long(map.get("id").toString());
						obj = new UserDeleteChangeType();
						o = getUserChanges(id);
					} else if (changeType.equals(AuditBase.CHANGE_TYPE_USER2ROLE)) {
						o = this.getUser(objectId);

						obj = new User2RoleChangeType();
					} else if (changeType.equals(AuditBase.CHANGE_TYPE_ROLE2RESOURCE)) {
						o = this.getRole(objectId);

						obj = new Role2ResourceChangeType();
					}  else if (changeType.equals(AuditBase.CHANGE_TYPE_INST_ADD)) {
						o = this.getInstc(objectId);

						obj = new UBaseInstAddType();
					}  else if (changeType.equals(AuditBase.CHANGE_TYPE_INST_MODIFY)) {
						o = this.getInstc(objectId);

						obj = new UBaseInstChangeType();
					}   else if (changeType.equals(AuditBase.CHANGE_TYPE_INST_DELETE)) {
						o = this.getInstc(objectId);

						obj = new UBaseInstDeleteChangeType();
					}   else if (changeType.equals(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY)) {
						o = this.getSysParma(objectId);

						obj = new SysParamChangeType();
					
					}  else if (changeType.equals(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY)) {
						o = this.getSubSystemChange(objectId,AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY,ids);

						obj = new SubSystemAdminChangeType();
					
					}    else if (changeType.equals(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE)) {
						o = this.getSubSystemChange(objectId,AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE,ids);

						obj = new SubSystemAdminDeleteChangeType();
					
					}   else if (changeType.equals(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY)) {
						o = this.getBaseCfgChangeById(objectId);

						obj = new UBaseConfigChangeType();
					
					} else {
						throw new AuditException("无效的变更类型");
					}
					obj.setChangeTime(changeTime);
					obj.setEntity(o);
					obj.setLogin(login);
				    auditInfos.add(obj);
				} else {
					throw new AuditException("无效的变更类型");
				}
			}
		} catch (Exception e) {
			throw e;
		}
		page.setRecordList(auditInfos);
		return page;
	}

	/**
	 * 用户修改审核、驳回、撤回
	 * 
	 */
	public void updateUserAudit(LoginDO login, Long changeType, Long id, int status) throws Exception {
		UserChangeDO o = new UserChangeDO();
		try {
			String sql = "select * from " + UserBaseAuditBase.BAK_TABLE + " where id=? ";
			List list = jdbcDaoAccessor.find(sql, new Object[] { id });
			if (CollectionUtils.isEmpty(list)) {
				return;
			}
			for (Iterator iterator0 = list.iterator(); iterator0.hasNext();) {
				Map map = (Map) iterator0.next();
				o = (UserChangeDO) BeanUtil.reflectToFillValue(UserChangeDO.class, map, ArrayUtil.concat(fullBaseAddAttributeFields,appendEnAttributeFields),
						ArrayUtil.concat(fullBaseAddColumnFields,appendEnColumnFields));
			}
			// if org not exist
			if (getInst(o.getInstId()) == null) {
				throw new AuditException("警告！该用户的所属机构可能已被删除，请确认用户的所属机构在系统中是否存在");
			}
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new Timestamp(System.currentTimeMillis()));
			o.setId(id);
			o.setChangeStatus(changeType);
			o.getAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_USER_ADD) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getAuditEntity().setValueFields(baseAddAttributeFields);
					o.getAuditEntity().setColumnFields(baseAddColumnFields);
					o.getAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getAuditEntity().auditThis(o);
			}
			if (changeType == AuditBase.CHANGE_TYPE_USER_MODIFY) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getAuditEntity().setValueFields(ArrayUtil.concat(ArrayUtil.concat(baseModifyAttributeFields, baseSPAttributeFields),appendEnAttributeFields));
					o.getAuditEntity().setColumnFields(ArrayUtil.concat(ArrayUtil.concat(baseModifyColumnFields, baseSPColumnFields),appendEnColumnFields));
					o.getAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getAuditEntity().auditThis(o);
			}
			if (changeType == AuditBase.CHANGE_TYPE_USER_DELETE) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getAuditEntity().setValueFields(fullBaseDeleteAttributeFields);
					o.getAuditEntity().setColumnFields(fullBaseDeleteColumnFields);
					o.getAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}

	/**
	 * 角色修改审核、驳回、撤回
	 * 
	 */
	public void updateRoleAudit(LoginDO login, Long changeType, String userId, int status) throws Exception {
		try {
			UserRoleChangeDO o = new UserRoleChangeDO();
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// o.setId(id);
			o.setChangeStatus(changeType);
			o.setUserId(userId);
			o.getAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_USER2ROLE) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getAuditEntity().setValueFields(fullRoleAttributeFields);
					o.getAuditEntity().setColumnFields(fullRoleColumnFields);
					o.getAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}

	/**
	 * 资源修改审核、驳回、撤回
	 * 
	 */
	public void updateResourceAudit(LoginDO login, Long changeType, String roleId, int status) throws Exception {
		try {
			RoleResourceChangeDO o = new RoleResourceChangeDO();
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// o.setId(id);
			o.setChangeStatus(changeType);
			o.setObjectId(roleId);
			o.getAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_ROLE2RESOURCE) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getAuditEntity().setValueFields(fullResourceAttributeFields);
					o.getAuditEntity().setColumnFields(fullResourceColumnFields);
					o.getAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}

	/**
	 * 机构修改审核、驳回、撤回
	 * 
	 */
	public void updateUBaseInstChangeAudit(LoginDO login, Long changeType, String instId, int status) throws Exception {
		try {
			UBaseInstChangeDO o = new UBaseInstChangeDO();
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// o.setId(id);
			o.setChangeStatus(changeType);
			o.setInstId(instId);
			o.getInstAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_INST_ADD) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getInstAuditEntity().setValueFields(fullInstAddAttributeFields);
					o.getInstAuditEntity().setColumnFields(fullInstAddColumnFields);
					o.getInstAuditEntity().flush2Prime(o);
					UBaseInstDO  inst = (UBaseInstDO) instService.getInstByInstId(o.getInstId());
					instService.updateInstRela1(inst);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}else {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getInstAuditEntity().auditThis(o);
			}
			if (changeType == AuditBase.CHANGE_TYPE_INST_MODIFY) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getInstAuditEntity().setValueFields(fullInstAddAttributeFields);
					o.getInstAuditEntity().setColumnFields(fullInstAddColumnFields);
					o.getInstAuditEntity().flush2Prime(o);
//					UBaseInstDO  inst = (UBaseInstDO) instService.getInstByInstId(o.getInstId());
//					instService.updateInstRela1(inst);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}else {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getInstAuditEntity().auditThis(o);
			}
			if (changeType == AuditBase.CHANGE_TYPE_INST_DELETE) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					instService.deleteInstByInstId(instId);
//					instService.deleteInstcByInstId(instId);
					instService.deleteUserInInst(instId);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}else {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getInstAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}
	
	/**
	 * 系统参数修改审核、驳回、撤回
	 * 
	 */
	public void updateSysParamChangeAudit(LoginDO login, Long changeType, String paramId, int status) throws Exception {
		try {

			UBaseSysParamChangeDO o = getSysParma(paramId);
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// o.setId(id);
			o.setChangeStatus(changeType);
			o.setParamId(Integer.valueOf(paramId));
			o.getSysParamAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
					o.getSysParamAuditEntity().flush2Prime(o);
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				} else if (status == AuditBase.AUDIT_STATUS_NOADUITED){
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}else {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getSysParamAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}
	
	/**
	 * 系统参数配置管理员修改审核、驳回、撤回
	 * 
	 */
	public void updateSubSystemAdminChangeAudit(LoginDO login, Long changeType, String userIds, int status,Long ids) throws Exception {
		try {
            String userId=userIds.substring(0,userIds.length()-5);
            String systemId =userIds.substring(userIds.length()-5,userIds.length());
		    SubSystemAdminChangeDO o = getSubSystemChange(userId,changeType,ids);
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			// o.setId(id);
			o.setChangeStatus(changeType);
			o.setUserId(userId);
			o.getSubSystemAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
					o.setSystemId(systemId);
					o.getSubSystemAuditEntity().flush2Prime(o);
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {//撤销等于是废弃
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				} else if (status == AuditBase.AUDIT_STATUS_NOADUITED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				} 
				o.getSubSystemAuditEntity().auditThis(o);
			}
			if (changeType == AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
					o.setSystemId(systemId);
					deleteAdminById(userId,systemId);
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {//撤销等于是废弃
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				} else if (status == AuditBase.AUDIT_STATUS_NOADUITED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				} 
				o.getSubSystemAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}
	
	/**
	 * 子系统参数修改审核、驳回、撤回
	 * 
	 */
	public void updateUBaseCfgChangeAudit(LoginDO login, Long changeType, String systemId, int status) throws Exception {
		try {
			UBaseConfigChangeDO o = getBaseCfgChangeById(systemId);
			// to do 更新审核信息
			o.setAuditUser(login.getUserId());
			o.setAuditTime(new java.sql.Timestamp(System.currentTimeMillis()));
			o.getConfigAuditEntity().setLogin(login);
			if (changeType == AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY) {
				if (status == AuditBase.AUDIT_STATUS_APPROVED) {
					o.getConfigAuditEntity().flush2Prime(o);
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_APPROVED));
				} else if (status == AuditBase.AUDIT_STATUS_REJECTED) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_REJECTED));
				} else if (status == AuditBase.AUDIT_STATUS_CANCEL) {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}else {
					o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_CANCEL));
				}
				o.getConfigAuditEntity().auditThis(o);
			}
		} catch (Exception e) {
			if (e instanceof AuditException)
				throw e;
			else
				throw new Exception("", e);
		}
	}
	
	
	public void deleteAdminById(String userId,String systemId)
	{
		String sql ="delete from "+SubSystemAdminAuditBase.SUB_MAIN_TABLE+" where user_id=? and system_id=?";
		jdbcDaoAccessor.update(sql, new Object[]{userId,systemId});
	}
	public List getHolidayTypeChanges(String holidayName) {
		String sql = "select * from " + UserBaseAuditBase.BAK_TABLE + " where holiday_name=? and audit_status=?";
		List list = jdbcDaoAccessor.find(sql, new Object[] { holidayName, new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
		return list;
	}

	public UserChangeDO getUserChanges(String userId, Long auditStatus) throws InstantiationException,
			IllegalAccessException {
		String sql = "select * from " + UserBaseAuditBase.BAK_TABLE + " where user_id=? ";
		if (auditStatus != null)
			sql += "and audit_status=" + auditStatus.intValue();
		List list = jdbcDaoAccessor.find(sql, new Object[] { userId });
		if (CollectionUtils.isNotEmpty(list)) {
			Map map = (Map) list.get(0);
			UserChangeDO o = (UserChangeDO) BeanUtil.reflectToFillValue(UserChangeDO.class, map, ArrayUtil.concat(
					baseAddAttributeFields, appendAttributeFields), ArrayUtil.concat(baseAddColumnFields,
					appendColumnFields));
			return o;
		}
		return null;
	}

	public UBaseUserDO getUser(String userId) throws InstantiationException, IllegalAccessException {
		UBaseUserDO o = this.userService.getUser(userId);
		if (o != null) {
			UBaseInstDO inst = this.userService.getBaseInstByUser(o);
			o.setInstName(inst.getInstName());
		} else {
			String sql = "select * from " + UserBaseAuditBase.BAK_TABLE
					+ " where user_id=? and change_status in(?,?) order by change_time desc";
			List list = jdbcDaoAccessor.find(sql, new Object[] { userId, AuditBase.CHANGE_TYPE_USER_DELETE,
					AuditBase.CHANGE_TYPE_USER_ADD });
			if (CollectionUtils.isNotEmpty(list)) {
				Map map = (Map) list.get(0);
				o = new UBaseUserDO();
				UserChangeDO user = (UserChangeDO) BeanUtil.reflectToFillValue(UserChangeDO.class, map, ArrayUtil
						.concat(baseAddAttributeFields, appendAttributeFields), ArrayUtil.concat(baseAddColumnFields,
						appendColumnFields));
				BeanUtils.copyProperties(user, o);
			} else {
				o = new UBaseUserDO();
			}

		}
		return o;
	}

	public UAuthRoleDO getRole(String roleId) {
		return this.authRoleService.getRoleByRoleId(roleId);
	}

	public UBaseInstChangeDO getInstc(String id) throws InstantiationException, IllegalAccessException{
		Assert.notNull(authRoleService.getInstService());
		return (UBaseInstChangeDO) authRoleService.getInstService().getInstcByInstId(id);
	}
	public UBaseSysParamChangeDO getSysParma(String id) throws InstantiationException, IllegalAccessException{
		Assert.notNull(authRoleService.getInstService());
		return (UBaseSysParamChangeDO) authRoleService.getInstService().getSysParmaByParamId(id);
	}
	
	
	public UBaseConfigDO getSubSystem(String systemId) {
		return this.authRoleService.getBaseConfigBySystemId(systemId);
	}
	
	public SubSystemAdminChangeDO getSubSystemChange(String userId,Long changetype,Long ids) throws InstantiationException, IllegalAccessException{
		if (userId == null) {
			return null;
		}
		String sql = "select * from " + SubSystemAdminAuditBase.SUB_BAK_TABLE
		+ " where USER_ID=? and CHANGE_STATUS=? and ID=? ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] { userId ,changetype,ids});
		if (CollectionUtils.isNotEmpty(list)) {
			Map map = (Map) list.get(0);
			SubSystemAdminChangeDO o = (SubSystemAdminChangeDO) BeanUtil
					.reflectToFillValue(SubSystemAdminChangeDO.class, map, SubSystemAdminAuditBase.subSysFullAttributeFields, 
							SubSystemAdminAuditBase.subSysFullColumnFields);
			o.setUserId(o.getUserId()==null? userId:o.getUserId());
			return o;
		}
		return null;
	}
	
	public UBaseConfigChangeDO getBaseCfgChangeById(String systemId) throws InstantiationException, IllegalAccessException{
		if (systemId == null) {
			return null;
		}
		String sql = "select * from " + SubSystemChangeAuditBase.CFG_BAK_TABLE
		+ " where SYSTEM_ID=?  ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] { systemId});
		if (CollectionUtils.isNotEmpty(list)) {
			Map map = (Map) list.get(0);
			UBaseConfigChangeDO o = (UBaseConfigChangeDO) BeanUtil
					.reflectToFillValue(UBaseConfigChangeDO.class, map,SubSystemChangeAuditBase.fullBaseCfgAttributeFields, 
							SubSystemChangeAuditBase.fullBaseCfgColumnFields);
			o.setMenuOrderNum(String.valueOf(map.get("MENU_ORDER_NUM")));
			return o;
		}
		return null;
	}

	public UBaseInstDO getInst(String instId) {
		Assert.notNull(authRoleService.getInstService());
		return (UBaseInstDO) authRoleService.getInstService().getInstByInstId(instId);
	}

	public UAuthRoleResourceDO getAuthRoleResource(String roleId, String resId, String resDetailValue) {
		UAuthRoleResourceDO o = new UAuthRoleResourceDO();
		String sql = "select * from u_auth_role_resource where object_id=? and res_id=? and res_detail_value=?";
		List list = jdbcDaoAccessor.find(sql, new Object[] { roleId, resId, resDetailValue });
		if (list.size() > 0) {
			Map map = (Map) list.get(0);
			o.setObjectId(map.get("object_id").toString());
			o.setResDetailName(map.get("res_detail_name").toString());
			o.setResId(map.get("res_id").toString());
			o.setResDetailValue(map.get("res_detail_value").toString());
		}
		return o;
	}

	public List getUsers(String roleId) {
		UAuthRoleDO role = new UAuthRoleDO();
		role.setRoleId(roleId);
		return this.authRoleService.getUserByRole(role);
	}

	public UserChangeDO getUserChanges(Long id) throws InstantiationException, IllegalAccessException {
		String sql = "select a.*,b.inst_name from " + UserBaseAuditBase.BAK_TABLE
				+ " a left join u_base_inst b on a.inst_id=b.inst_id where a.id=? ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] { id });
		if (CollectionUtils.isNotEmpty(list)) {
			Map map = (Map) list.get(0);
			UserChangeDO o = (UserChangeDO) BeanUtil
					.reflectToFillValue(UserChangeDO.class, map, ArrayUtil.concat(baseAddAttributeFields, ArrayUtil
							.concat(appendAttributeFields, changeRemarkAttributeField)), ArrayUtil.concat(
							baseAddColumnFields, ArrayUtil.concat(appendColumnFields, changeRemarkColumnField)));
			o.setInstName(map.get("inst_name") != null ? map.get("inst_name").toString() : "");
			return o;
		}
		return null;
	}

	public long roleAuditCount(String userId, String changeUser, Long auditStatus) {
		String sql = "select count(*) from " + UserRoleAuditBase.BAK_TABLE
				+ " where user_id=? and change_user!=? and audit_status=?";
		return jdbcDaoAccessor.findForInt(sql, new Object[] { userId, changeUser, auditStatus });
	}

	public long resourceAuditCount(String roleId, String changeUser, Long auditStatus) {
		String sql = "select count(*) from " + RoleResourceAuditBase.BAK_TABLE
				+ " where object_id=? and change_user!=? and audit_status=?";
		return jdbcDaoAccessor.findForInt(sql, new Object[] { roleId, changeUser, auditStatus });
	}
	

	public long instAuditCount(String roleId, String changeUser, Long auditStatus) {
		String sql = "select count(*) from " +InstBaseAuditBase.IBAK_TABLE
				+ " where inst_id=? and change_user!=? and audit_status=?";
		return jdbcDaoAccessor.findForInt(sql, new Object[] { roleId, changeUser, auditStatus });
	}

	public long sysParamAuditCount(String roleId, String changeUser, Long auditStatus) {
		String sql = "select count(*) from " +SysParamAuditBase.SPBAK_TABLE
				+ " where param_id=? and change_user!=? and audit_status=?";
		return jdbcDaoAccessor.findForInt(sql, new Object[] { roleId, changeUser, auditStatus });
	}

	public List getRoleChanges(Long auditStatus, String userId, LoginDO login) throws InstantiationException,
			IllegalAccessException {
		List list = new ArrayList();
		String sql = "select * from " + UserRoleAuditBase.BAK_TABLE + "  where user_id=? ";
		String append = " order by change_status,role_id";
		if (auditStatus != null) {
			sql += " and audit_status=" + auditStatus.longValue();
		}
		if (login != null) {
			sql += " and change_user='" + login.getUserId() + "'";
		}
		List results = jdbcDaoAccessor.find(sql + append, new Object[] { userId });
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			UserRoleChangeDO o = (UserRoleChangeDO) BeanUtil.reflectToFillValue(UserRoleChangeDO.class, map,
					fullRoleAttributeFields, fullRoleColumnFields);

			UAuthRoleDO role = getRole(map.get("role_id").toString());
			// if role delete
			if (role == null) {
				o.setRoleName("系统中不存在对应的角色");
				o.setSystemName("系统中不存在对应的角色");
			} else {
				o.setRoleName(role.getRoleName());
				UBaseConfigDO config = getSubSystem(role.getSystemId());
				o.setSystemName(config.getSystemCname());
			}
			list.add(o);
		}
		/** **********把未变的不记录表，直接从原表取 ********* */
		List hasRoles = this.jdbcDaoAccessor.find("select user_id,role_id from " + UserRoleAuditBase.MAIN_TABLE
				+ " where user_id=?", new Object[] { userId });
		// transform
		List olds = new ArrayList();
		for (int i = 0; i < hasRoles.size(); i++) {
			Map row = (Map) hasRoles.get(i);
			UserRoleChangeDO o = this.transUserRoleMap2Object(row);
			UAuthRoleDO role = getRole(row.get("role_id").toString());
			o.setRoleName(role.getRoleName());
			UBaseConfigDO config = getSubSystem(role.getSystemId());
			o.setSystemName(config.getSystemCname());
			olds.add(o);
		}
		List deletes = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserRoleChangeDO o = (UserRoleChangeDO) iterator.next();
			if (o.getChangeStatus().intValue() == AuditBase.USER_ROLE_CHANGE_STATUS_DELETE)
				deletes.add(o);
		}
		List normals = Collections.EMPTY_LIST;
		if (auditStatus != null) {
			normals = UserRoleUtil.minus(olds, deletes);
		} else {
			normals = olds;
		}
		List normalRoles = fillEntityStatusFromRole(normals, AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
		list.addAll(normalRoles);
		/** **********把未变的不记录表，直接从原表取 ********* */
		return list;
	}

	public List getResourceChanges(Long auditStatus, String roleId, Long resId, LoginDO login)
			throws InstantiationException, IllegalAccessException {
		List list = new ArrayList();
		String sql = "select * from " + RoleResourceAuditBase.BAK_TABLE + "  where object_id=? and res_id=? ";
		String append = " order by change_status,object_id";
		if (auditStatus != null) {
			sql += " and audit_status= " + auditStatus.longValue();
		}
		if (login != null) {
			sql += " and change_user='" + login.getUserId() + "'";
		}
		List results = jdbcDaoAccessor.find(sql + append, new Object[] { roleId, resId });
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			RoleResourceChangeDO o = (RoleResourceChangeDO) BeanUtil.reflectToFillValue(RoleResourceChangeDO.class,
					map, fullResourceAttributeFields, fullResourceColumnFields);
			list.add(o);
		}
		/** **********把未变的不记录表，直接从原表取 ********* */
		List hasResources = this.jdbcDaoAccessor.find("select * from " + RoleResourceAuditBase.MAIN_TABLE
				+ " where object_id=? and res_id=?", new Object[] { roleId, resId });
		// transform
		List olds = new ArrayList();
		for (int i = 0; i < hasResources.size(); i++) {
			Map row = (Map) hasResources.get(i);
			RoleResourceChangeDO o = new RoleResourceChangeDO();
			o.setResDetailValue(row.get("res_detail_value").toString());
			o.setResId(row.get("res_id").toString());
			o.setObjectId(row.get("object_id").toString());
			o.setSystemId(row.get("system_id").toString());
			o.setResDetailName(row.get("res_detail_name") == null ? "" : row.get("res_detail_name").toString());
			olds.add(o);
		}
		List deletes = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			RoleResourceChangeDO o = (RoleResourceChangeDO) iterator.next();
			if (o.getChangeStatus().intValue() == AuditBase.ROLE_RES_CHANGE_STATUS_DELETE)
				deletes.add(o);
		}
		List normals = Collections.EMPTY_LIST;
		if (auditStatus != null) {
			normals = RoleResourceUtil.minus(olds, deletes);
		} else {
			normals = olds;
		}
		List normalResources = fillEntityStatusFromResource(normals, AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
		list.addAll(normalResources);
		/** **********把未变的不记录表，直接从原表取 ********* */
		return list;
	}

	public List getResourceChangesWithoutPub(Long auditStatus, String roleId, LoginDO login)
			throws InstantiationException, IllegalAccessException {
		List list = new ArrayList();
		String sql = "select * from " + RoleResourceAuditBase.BAK_TABLE + " where object_id=? and res_id not in(?,?) ";
		String append = " order by change_status,object_id";
		if (auditStatus != null) {
			sql += " and audit_status= " + auditStatus.longValue();
		}
		if (login != null) {
			sql += " and change_user='" + login.getUserId() + "'";
		}
		List results = jdbcDaoAccessor.find(sql + append, new Object[] { roleId, new Long(Constants.INST_RES_ID),
				new Long(Constants.MENU_RES_ID) });
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			RoleResourceChangeDO o = (RoleResourceChangeDO) BeanUtil.reflectToFillValue(RoleResourceChangeDO.class,
					map, fullResourceAttributeFields, fullResourceColumnFields);
			list.add(o);
		}
		/** **********把未变的不记录表，直接从原表取 ********* */
		List hasResources = this.jdbcDaoAccessor.find("select * from " + RoleResourceAuditBase.MAIN_TABLE
				+ " where object_id=? and res_id not in(?,?)", new Object[] { roleId, new Long(Constants.INST_RES_ID),
				new Long(Constants.MENU_RES_ID) });
		// transform
		List olds = new ArrayList();
		for (int i = 0; i < hasResources.size(); i++) {
			Map row = (Map) hasResources.get(i);
			RoleResourceChangeDO o = new RoleResourceChangeDO();
			o.setResDetailValue(row.get("res_detail_value").toString());
			o.setResId(row.get("res_id").toString());
			o.setObjectId(row.get("object_id").toString());
			o.setSystemId(row.get("system_id").toString());
			o.setResDetailName(row.get("res_detail_name").toString());
			olds.add(o);
		}
		List deletes = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			RoleResourceChangeDO o = (RoleResourceChangeDO) iterator.next();
			if (o.getChangeStatus().intValue() == AuditBase.ROLE_RES_CHANGE_STATUS_DELETE)
				deletes.add(o);
		}
		List normals = Collections.EMPTY_LIST;
		if (auditStatus != null) {
			normals = RoleResourceUtil.minus(olds, deletes);
		} else {
			normals = olds;
		}
		List normalResources = fillEntityStatusFromResource(normals, AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL);
		list.addAll(normalResources);
		/** **********把未变的不记录表，直接从原表取 ********* */
		return list;
	}

	public int cancelUserChanges(String roleId) {
		String sql = "update " + UserBaseAuditBase.BAK_TABLE + "  set audit_status=" + AuditBase.AUDIT_STATUS_NOADUITED
				+ " and object_id=? and res_id not in(?,?) order by change_status,object_id";
		int count = jdbcDaoAccessor.update(sql, new Object[] { roleId });
		return count;
	}

	/** ****************show for page action************************* */

	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public List getTwoTablePlusUsers(UBaseUserDO user, UBaseInstDO inst, PaginationList page,String instId) {
		StringBuffer sb = new StringBuffer();
		ArrayList params = new ArrayList();
		if (dialect == null)
			dialect = DialectFactory.getDialect(jdbcDaoAccessor.getDataSource());
		
		String columns = " a.user_id,a.user_ename,a.user_cname,"
				+ "a.inst_id,a.is_delete,b.inst_name,b.inst_smp_name,a.is_user_locked,a.user_locked_reson,a.ENABLED ,a.IS_LIST,b.inst_path ";
		String sql = "select " + columns + ",'0' change_status from " + UserBaseAuditBase.MAIN_TABLE
				+ " a left join u_base_inst b on a.inst_id=b.inst_id where 1=1 ";
		// 用户机构号条件判断
		if (StringUtils.isNotEmpty(user.getInstId())) {
			sb.append(" and a.inst_id like ? ");
			params.add("%" + user.getInstId() + "%");
		}
		// 用户机构名称条件判断
		if (StringUtils.isNotEmpty(inst.getInstSmpName())) {
			sb.append(" and b.inst_smp_name like ? ");
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// 用户登陆名条件判断
		if (StringUtils.isNotEmpty(user.getUserEname())) {
			sb.append(" and a.user_ename like ? ");
			params.add("%" + user.getUserEname() + "%");
		}
		// 用户中文条件判断
		if (StringUtils.isNotEmpty(user.getUserCname())) {
			sb.append(" and a.user_cname like ? ");
			params.add("%" + user.getUserCname() + "%");
		}
		sql += sb.toString();
		sql += " union ";
		sql += "select " + columns + ",'1' change_status from " + UserBaseAuditBase.BAK_TABLE
				+ " a left join u_base_inst b on a.inst_id=b.inst_id where a.change_status=1 and audit_status=1";
		// 用户机构号条件判断
		if (StringUtils.isNotEmpty(user.getInstId())) {
			params.add("%" + user.getInstId() + "%");
		}
		// 用户机构名称条件判断
		if (StringUtils.isNotEmpty(inst.getInstSmpName())) {
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// 用户登陆名条件判断
		if (StringUtils.isNotEmpty(user.getUserEname())) {
			params.add("%" + user.getUserEname() + "%");
		}
		// 用户中文条件判断
		if (StringUtils.isNotEmpty(user.getUserCname())) {
			params.add("%" + user.getUserCname() + "%");
		}
		sql += sb.toString();
		sql="select tt.* from ("+sql+") tt"+" where  exists (select 1 from u_base_inst e where e.inst_Id='"+instId+"' and ( substr(tt.inst_Path,1,"+(dialect instanceof MSSQLDialect?"len":"length")+"(e.inst_Path)) = e.inst_Path or e.is_Head='true'))";
		List list = null;
		try {
			// Pagination here
			
			Assert.notNull(dialect);
			page.setRecordCount(jdbcDaoAccessor.findForInt(dialect.getRowCountString(sql),params.toArray()));
			int startIndex = (page.getCurrentPage() - 1) * page.getPageSize();
			String pageSql = dialect.getLimitedString(sql, startIndex, page.getPageSize());
			list = jdbcDaoAccessor.find(pageSql,params.toArray());
			List list0 = new ArrayList();
			if (CollectionUtils.isNotEmpty(list)) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					Map row = new HashMap();
					row.put("userId", map.get("user_id"));
					row.put("isUserLocked", map.get("is_user_locked"));
					row.put("userEname", map.get("user_ename"));
					row.put("userCname", map.get("user_cname"));
					row.put("instId", map.get("inst_id"));
					row.put("instName", map.get("inst_name"));
					row.put("instSmpName", map.get("inst_smp_name"));
					row.put("isDelete", map.get("is_delete"));
					row.put("enabled", map.get("enabled"));
					row.put("isList", map.get("IS_LIST"));
					String userStatus = null;
					if ("0".equals(map.get("change_status")))
						userStatus = (String) userLoginManager.getDetailUserStatus((String) map.get("is_user_locked"),
								(String) map.get("user_locked_reson"));
					if("0".equals(map.get("enabled"))){
						userStatus = "已停用";
					}
					if ("1".equals(map.get("change_status")))
						userStatus = "未审核";
					if ("1".equals(map.get("is_delete")))
						userStatus = "已删除";
					row.put("userStatus", userStatus);

					if ("1".equals(map.get("is_delete")) || "1".equals(map.get("change_status"))) {
						// 已删和未审不能操作
						row.put("canAccess", new Boolean(false));
					} else {
						row.put("canAccess", new Boolean(true));
					}
					row.put("hasLogin", new Boolean(onlineService.hasLoginByUserId((String) map.get("user_id"))));
					list0.add(row);
				}
			}
			page.setRecordList(list0);
		} catch (Exception e) {
			LogFactory.getLog(this.getClass()).error(e);
			e.printStackTrace();
			throw new AuditException("error when get TwoTablePlus users", e);
		}
		return list;
	}

	public List getTwoTablePlusUsers(UBaseUserDO user, UBaseInstDO inst) {
		StringBuffer sb = new StringBuffer();
		ArrayList params = new ArrayList();
		
		String columns = " a.user_id,a.user_ename,a.user_cname,"
				+ "a.inst_id,a.is_delete,b.inst_name,a.is_user_locked,a.user_locked_reson,a.last_login_date ";
		String sql = "select " + columns + ",'0' change_status from " + UserBaseAuditBase.MAIN_TABLE
				+ " a left join u_base_inst b on a.inst_id=b.inst_id where 1=1 ";
		// 用户机构号条件判断
		if (StringUtils.isNotEmpty(user.getInstId())) {
			sb.append(" and a.inst_id like ? ");
			params.add("%" + user.getInstId() + "%");
		}
		// 用户机构名称条件判断
		if (StringUtils.isNotEmpty(inst.getInstSmpName())) {
			sb.append(" and b.inst_smp_name like ? ");
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// 用户登陆名条件判断
		if (StringUtils.isNotEmpty(user.getUserEname())) {
			sb.append(" and a.user_ename like ? ");
			params.add("%" + user.getUserEname() + "%");
		}
		// 用户中文条件判断
		if (StringUtils.isNotEmpty(user.getUserCname())) {
			sb.append(" and a.user_cname like ? ");
			params.add("%" + user.getUserCname() + "%");
		}
		sql += sb.toString();
		sql += " union ";
		sql += "select " + columns + ",'1' change_status from " + UserBaseAuditBase.BAK_TABLE
				+ " a left join u_base_inst b on a.inst_id=b.inst_id where a.change_status=1 and audit_status=1";
		sql += sb.toString();
		// 用户机构号条件判断
		if (StringUtils.isNotEmpty(user.getInstId())) {
			params.add("%" + user.getInstId() + "%");
		}
		// 用户机构名称条件判断
		if (StringUtils.isNotEmpty(inst.getInstSmpName())) {
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// 用户登陆名条件判断
		if (StringUtils.isNotEmpty(user.getUserEname())) {
			params.add("%" + user.getUserEname() + "%");
		}
		// 用户中文条件判断
		if (StringUtils.isNotEmpty(user.getUserCname())) {
			params.add("%" + user.getUserCname() + "%");
		}
		List list = null;
		List list0 = new ArrayList();
		try {
			list = jdbcDaoAccessor.find(sql,params.toArray());
			if (CollectionUtils.isNotEmpty(list)) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Map map = (Map) iterator.next();
					Map row = new HashMap();
					row.put("userId", map.get("user_id"));
					row.put("isUserLocked", map.get("is_user_locked"));
					row.put("userEname", map.get("user_ename"));
					row.put("userCname", map.get("user_cname"));
					row.put("instId", map.get("inst_id"));
					row.put("instSmpName", map.get("inst_name"));
					row.put("isDelete", map.get("is_delete"));
					String userStatus = null;
					if ("0".equals(map.get("change_status")))
						userStatus = (String) userLoginManager.getDetailUserStatus((String) map.get("is_user_locked"),
								(String) map.get("user_locked_reson"));
					if ("1".equals(map.get("change_status")))
						userStatus = "未审核";
					if ("1".equals(map.get("is_delete")))
						userStatus = "已删除";
					row.put("userStatus", userStatus);

					if ("1".equals(map.get("is_delete")) || "1".equals(map.get("change_status"))) {
						// 已删和未审不能操作
						row.put("canAccess", new Boolean(false));
					} else {
						row.put("canAccess", new Boolean(true));
					}
					row.put("lastLoginDate", map.get("last_login_date"));
					List roles = userService.getRoleByUserId((String) map.get("user_id"));
					StringBuffer s = new StringBuffer();
					if (CollectionUtils.isNotEmpty(roles)) {
						for (Iterator iterator1 = roles.iterator(); iterator1.hasNext();) {
							UAuthRoleDO role = (UAuthRoleDO) iterator1.next();
							s.append(","+role.getRoleName()+"["+role.getSystemCname()+"]");
						}
					}
					row.put("roles",s.toString().length()==0?"":s.toString().substring(1));
					list0.add(row);
				}
			}
		} catch (Exception e) {
			LogFactory.getLog(this.getClass()).error(e);
			throw new AuditException("error when get TwoTablePlus users", e);
		}
		return list0;
	}

	public void setUserLoginManager(UserLoginManager userLoginManager) {
		this.userLoginManager = userLoginManager;
	}

	static class RoleComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			UserRoleChangeDO u1 = (UserRoleChangeDO) o1;
			UserRoleChangeDO u2 = (UserRoleChangeDO) o2;
			return u1.getChangeTime().compareTo(u2.getChangeTime());
		}
	}

	static class ResourceComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			RoleResourceChangeDO u1 = (RoleResourceChangeDO) o1;
			RoleResourceChangeDO u2 = (RoleResourceChangeDO) o2;
			return u1.getChangeTime().compareTo(u2.getChangeTime());
		}
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
