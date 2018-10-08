/**
 * 
 */
package fmss.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseFuncDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.config.LoggingConfig;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


// under parameter open mode, fetch role&resource name, user name, function
// name, also system name
public class AuthorityNameFetcher {
	public static String fetchRoleName(String id) {
		if (LoggingConfig.isloggingConfigedName()) {
			AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
			Assert.notNull(authRoleService, "authRoleService must not null");
			try {
				UAuthRoleDO role = authRoleService.getRoleByRoleId(id);
				return role != null ? role.getRoleName() : id;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return id;
	}

	public static List fetchRoleName(List ids) {
		if (LoggingConfig.isloggingConfigedName()) {
			AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
			Assert.notNull(authRoleService, "authRoleService must not null");
			List l = new ArrayList(ids.size());
			try {
				for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
					String s = (String) iterator.next();
					UAuthRoleDO role = authRoleService.getRoleByRoleId(s);
					l.add(role != null ? role.getRoleName() : s);
				}
				return l;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return ids;
	}

	public static String[] fetchRoleName(String[] ids) {
		if (LoggingConfig.isloggingConfigedName()) {
			AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
			Assert.notNull(authRoleService, "authRoleService must not null");
			try {
				String[] roles = new String[ids.length];
				for (int i = 0; i < ids.length; i++) {
					String s = ids[i];
					UAuthRoleDO role = authRoleService.getRoleByRoleId(s);
					roles[i] = role != null ? role.getRoleName() : s;
				}
				return roles;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return ids;
	}

	public static String fetchUserName(String id) {
		if (LoggingConfig.isloggingConfigedName()) {
			UserService userService = (UserService) SpringContextUtils.getBean("userService");
			Assert.notNull(userService, "userService must not null");
			try {
				UBaseUserDO user = userService.getUser(id);
				return user != null ? id + "-" + user.getUserCname() : id;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return id;
	}

	public static List fetchUserName(List ids) {
		if (LoggingConfig.isloggingConfigedName()) {
			UserService userService = (UserService) SpringContextUtils.getBean("userService");
			Assert.notNull(userService, "userService must not null");
			List l = new ArrayList(ids.size());
			try {
				for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
					String s = (String) iterator.next();
					UBaseUserDO user = userService.getUser(s);
					l.add(user != null ? s + "-" + user.getUserCname() : s);
				}
				return l;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return ids;
	}

	public static String[] fetchUserName(String[] ids) {
		if (LoggingConfig.isloggingConfigedName()) {
			UserService userService = (UserService) SpringContextUtils.getBean("userService");
			Assert.notNull(userService, "userService must not null");
			String[] users = new String[ids.length];
			try {
				for (int i = 0; i < ids.length; i++) {
					String s = ids[i];
					UBaseUserDO user = userService.getUser(s);
					users[i] = user != null ? s + "-" + user.getUserCname() : s;
				}
				return users;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return ids;
	}

	public static String[] fetchFunctionName(String[] ids) {
		if (LoggingConfig.isloggingConfigedName()) {
			FunctionService functionService = (FunctionService) SpringContextUtils.getBean("functionService");
			Assert.notNull(functionService, "functionService must not null");
			String[] funcs = new String[ids.length];
			try {
				for (int i = 0; i < ids.length; i++) {
					String s = ids[i];
					UBaseFuncDO o = functionService.getBaseFunction(s);
					funcs[i] = o != null ? o.getFuncURL() + "-" + o.getFuncDesc() : s;
				}
				return funcs;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return ids;
	}

	public static String fetchSystemName(String id) {
		if (LoggingConfig.isloggingConfigedName()) {
			SubSystemService subSystemService = (SubSystemService) SpringContextUtils.getBean("subSystemService");
			Assert.notNull(subSystemService, "subSystemService must not null");
			try {
				UBaseConfigDO o = subSystemService.getBaseConfigBySystemId(id);
				return o != null ? o.getSystemCname() : id;
			} catch (RuntimeException e) {
				LogFactory.getLog(AuthorityNameFetcher.class).error("", e);
				throw e;
			}
		}
		return id;
	}
}
