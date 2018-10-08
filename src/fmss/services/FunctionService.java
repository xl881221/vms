package fmss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UAuthRoleVO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseFuncAuthDO;
import fmss.dao.entity.UBaseFuncDO;
import fmss.dao.entity.UBaseFuncMenuDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.common.util.Constants;
import fmss.common.util.PaginationList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;


public class FunctionService extends CommonService {

	public static final String[] SYSTEM_ADMIN_MENUS = { "0001.0001",
			"0002.0001", "0003", "0003.0001", "0003.0003" };

	private SubSystemService subSystemService;

	private CacheManager cacheManager;

	private static final Log log = LogFactory.getLog(FunctionService.class);

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setSubSystemService(SubSystemService subSystemService) {
		this.subSystemService = subSystemService;
	}

	public List getBaseConfigList() {
		return find("from UBaseConfigDO u where u.systemId not in ('00000','00003') order by systemId");
	}

	public List getRoleFuncList(PaginationList page, String systemId) {
		page = page == null ? new PaginationList() : page;
		List roleList = new ArrayList();
		List roles = find(
				"from UAuthRoleDO u where u.systemId not in ('00000','00003') "
						+ (StringUtils.isNotEmpty(systemId) ? " and systemId='"
								+ systemId + "'" : "")
						+ "order by systemId,roleName", new ArrayList(), page);
		for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
			UAuthRoleDO role = (UAuthRoleDO) iterator.next();
			UAuthRoleVO vo = new UAuthRoleVO();
			BeanUtils.copyProperties(role, vo);
			vo.setUBaseConfig(subSystemService.getBaseConfigBySystemId(vo
					.getSystemId()));
			roleList.add(vo);
		}
		page.setRecordList(roleList);
		return roleList;
	}

	public List getMenuResList(String roleId) {
		Assert.notNull(roleId);
		List list = find(
				"from UAuthRoleResourceDO u where u.objectId=? and u.resId=? order by u.resDetailValue",
				new Object[] { roleId, String.valueOf(Constants.MENU_RES_ID) });
		return list;
	}

	public List getSystemMenuResList(String userId) {
		Assert.notNull(userId);
		List list = find(
				"from UAuthSystemAdminDO u where u.systemId='00003' and u.userId=?",
				new Object[] { userId });
		String hql = "from MenuDO u where u.systemId='00003' and u.url is not null "
				+ (CollectionUtils.isEmpty(list) ? " and u.itemcode not in ("
						+ PrivilegeService.IMPORTANT_MENUS + ") " : "")
				+ "order by u.itemcode";
		list = find(hql);
		return list;
	}

	public UAuthRoleDO getRoleByRoleId(String roleId) {
		Assert.notNull(roleId);
		List list = find("from UAuthRoleDO u where u.roleId=? ",
				new Object[] { roleId });
		return CollectionUtils.isNotEmpty(list) ? (UAuthRoleDO) list.get(0)
				: null;
	}

	public UBaseConfigDO getSubSystemBySystemId(String systemId) {
		Assert.notNull(systemId);
		List list = find("from UBaseConfigDO u where u.systemId=? ",
				new Object[] { systemId });
		return CollectionUtils.isNotEmpty(list) ? (UBaseConfigDO) list.get(0)
				: null;
	}

	public UBaseUserDO getBaseUserByUserId(String userId) {
		Assert.notNull(userId);
		List list = find("from UBaseUserDO u where u.userId=? ",
				new Object[] { userId });
		return CollectionUtils.isNotEmpty(list) ? (UBaseUserDO) list.get(0)
				: null;
	}

	public List getAdminUserList(PaginationList page) {
		page = page == null ? new PaginationList() : page;
		String logicDelete = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		String hql = " from UBaseUserDO where userId in (select userId from UAuthSystemAdminDO)";
		if("1".equals(logicDelete))
			hql += " and (isDelete!='1' or isDelete is null) ";
		find(hql, new ArrayList(), page);
		return page.getRecordList();
	}

	public List getFuncList(String menuId, String systemId) {
		List list = find(
				"from UBaseFuncMenuDO u where u.menuId=? and u.systemId=?",
				new Object[] { menuId, systemId });
		return list;
	}

	public List getFuncAuthList(String objectId) {
		List list = find("from UBaseFuncAuthDO u where u.objectId=? ",
				new Object[] { objectId });
		return list;
	}

	public void saveFuncAuth(String[] funcIds, String objectId,
			String objectType) {
		int counts = executeUpdate("delete from UBaseFuncAuthDO u where u.objectId='"
				+ objectId + "'");
		System.out.println(counts);
		for (int i = 0; i < funcIds.length; i++) {
			String id = funcIds[i];
			UBaseFuncAuthDO o = new UBaseFuncAuthDO();
			o.setFuncId(Integer.parseInt(id));
			o.setObjectId(objectId);
			o.setObjectType(objectType);
			save(o);
		}

	}

	public void registerFuncAuth() {
		if ("1".equals(cacheManager
				.getParemerCacheMapValue(PARAM_SYS_USER_FUNC_CHECK))) {
			CacheabledMap funcCache = new CacheabledMap(false);
			// --UncheckSet
			List allFunc = find("from UBaseFuncDO");
			// --AdminSet
			List systemAdmins = find("select a.userId from UAuthSystemAdminDO a group by a.userId");
			Set adminSet = new HashSet();
			for (Iterator iterator = systemAdmins.iterator(); iterator
					.hasNext();) {
				String userId = (String) iterator.next();
				adminSet.add(userId);
			}
			// --MENUS
			String hql = "select u.url,u.itemcode from MenuDO u where u.systemId='00003' and url is not null ";
			List allMenus00003 = find(hql);
			hql += " and u.itemcode not in ("
					+ PrivilegeService.IMPORTANT_MENUS + ")";
			List menus00003 = find(hql);

			// --UserFuncMap
			Map userFuncMap = new HashMap();
			for (Iterator iterator = systemAdmins.iterator(); iterator
					.hasNext();) {
				String userId = (String) iterator.next();
				Set funcSet = new HashSet();
				Set privilegetSet = new HashSet();
				List funcs = find("from UBaseFuncAuthDO u where u.objectId=?",
						new Object[] { userId });
				for (Iterator iterator2 = funcs.iterator(); iterator2.hasNext();) {
					UBaseFuncAuthDO o = (UBaseFuncAuthDO) iterator2.next();
					funcSet.add(o.getFunc().getFuncURL());
					for (Iterator iterator3 = o.getFunc().getFuncMenus()
							.iterator(); iterator3.hasNext();) {
						UBaseFuncMenuDO m = (UBaseFuncMenuDO) iterator3.next();
						if (Constants.SYSTEM_COMMON_ID.equals(m.getSystemId())
								&& o.getObjectId().equals(userId)) {
							// has privilege to access this menu
							privilegetSet.add(m.getMenuId());
						}
					}
				}
				// add menus
				List list = find(
						"from UAuthSystemAdminDO u where u.systemId='00003' and u.userId=?",
						new Object[] { userId });
				if (CollectionUtils.isNotEmpty(list)) {
					// if super system admin users
					for (Iterator iterator2 = allMenus00003.iterator(); iterator2
							.hasNext();) {
						Object[] o = (Object[]) iterator2.next();
						funcSet.add(o[0]);
					}
				} else {
					// if other system admin users
					for (Iterator iterator2 = menus00003.iterator(); iterator2
							.hasNext();) {
						// find if config sub-functions to determine has
						// privilege to access menu or not
						Object[] o = (Object[]) iterator2.next();
						if (privilegetSet.contains(o[1]))
							funcSet.add(o[0]);
					}
				}
				userFuncMap.put(userId, funcSet);
			}
			funcCache.put(ALL_FUNC, allFunc);
			funcCache.put(ADMIN_SET, adminSet);
			funcCache.put(FUNC_MAP, userFuncMap);
			cacheManager.register(FUNC_CACHE, funcCache, false);
		}
	}

	public boolean checkPrivilegeWith00003MenuFunc(String userId, String action) {
		if ("1".equals(cacheManager
				.getParemerCacheMapValue(PARAM_SYS_USER_FUNC_CHECK))) {
			CacheabledMap funcCache = (CacheabledMap) cacheManager
					.getCacheObject(FUNC_CACHE);
			if (funcCache != null) {
				//a.funcType='UNCHECK'
				List allFunc = (List) funcCache.get(ALL_FUNC);
				boolean isCheck=false;
				for(int i=0,j=allFunc.size();i<j;i++){
					UBaseFuncDO u=(UBaseFuncDO)allFunc.get(i);
					if(action.equals(u.getFuncURL())&&!"UNCHECK".equals(u.getFuncType())){
						isCheck=true;
						break;
					}
				}
				if (!isCheck) {
					return false;// pass
				}
				Set adminSet = (Set) funcCache.get(ADMIN_SET);
				if (adminSet.contains(userId)) {
					Map funcMap = (Map) funcCache.get(FUNC_MAP);
					Set funcSet = (Set) funcMap.get(userId);
					if (!funcSet.contains(action)) {
						log.warn("用户[" + userId + "]尝试访问了受限制的资源[" + action
								+ "]");
						return true;// function not exist in function-user
						// list,need to config
						// forbidden
					}
				} else {
					log.warn("用户[" + userId + "]不是子系统管理员,不具备访问系统管理功能权限");
					return true;// user is not exist in sysadmin list,
					// forbidden
				}
			}
		}
		return false;// pass
	}
	
	public UBaseFuncDO getBaseFunction(String id) {
		List list = find("from UBaseFuncDO where funcId = ?", new Object[] { new Integer(id) });
		return CollectionUtils.isNotEmpty(list) ? (UBaseFuncDO) list.get(0) : null;
	}

	public static final String FUNC_CACHE = "FUNC_CACHE";
	public static final String FUNC_MAP = "FUNC_MAP";
	public static final String ADMIN_SET = "ADMIN_SET";
	public static final String ALL_FUNC = "ALL_FUNC";
	public static final String PARAM_SYS_USER_FUNC_CHECK = "PARAM_SYS_USER_FUNC_CHECK";
}
