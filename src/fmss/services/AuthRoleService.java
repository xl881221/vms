package fmss.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fmss.action.base.AuditBase;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthResMapDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UAuthRoleResourceDO;
import fmss.dao.entity.UAuthRoleUserDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;
import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import fmss.common.db.BaseEntityManager;

import common.crms.util.StringUtil;


/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:11:10
 * @描述: [AuthRoleService]角色和权限的服务类
 */
public class AuthRoleService extends CommonService{

	private static final String SYSTEM_ID = "00003";

	/** 用户service */
	private UserService userService;
	/** 机构service */
	private InstService instService;
	/** id生成器 */
	private static IdGenerator  idGenerator; // id生成器
	
	private CacheManager cacheManager; // 缓存

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * <p>方法名称: save|描述:保存角色 </p>
	 * @param role 角色对象
	 */
	public void save(final UAuthRoleDO role){
		getBem().save(role);
	};

	/**
	 * <p>方法名称: update|描述:更新角色 </p>
	 * @param role 角色对象
	 */
	public void update(final UAuthRoleDO role){
		getBem().update(role);
	};

	/**
	 * <p>方法名称: delete|描述:删除角色 </p>
	 * @param role 角色对象
	 */
	public void delete(final UAuthRoleDO role){
		getBem().delete(role);
	};

	/* （非 Javadoc）
	* <p>重写方法: deleteById|描述:根据id删除对象 </p>
	* @param cl 类名
	* @param id 对象id
	* @see fmss.services.CommonService#deleteById(java.lang.Class, java.io.Serializable)
	*/
	public void deleteById(Class cl, Serializable id){
		getBem().deleteById(cl, id);
	};

	/* （非 Javadoc）
	* <p>重写方法: get|描述:根据id获取对象 </p>
	* @param entity 类名
	* @param id 对象id
	* @return 获取的对象
	* @see fmss.services.CommonService#get(java.lang.Class, java.io.Serializable)
	*/
	public Object get(final Class entity, final Serializable id){
		return getBem().get(entity, id);
	};

	/**
	 * <p>方法名称: getAllObject|描述:获取所有对象 </p>
	 * @param cl 类名
	 * @return 对象列表
	 */
	public List getAllObject(Class cl){
		return getBem().getAllObject(cl);
	};

	/* （非 Javadoc）
	* <p>重写方法: getAllByDetachedCriteria|描述:根据条件获取对象 </p>
	* @param detachedCriteria 查询条件
	* @return 查询结果集合
	* @see fmss.services.CommonService#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		
		return getBem().getAllByDetachedCriteria(detachedCriteria);
	};

	/* （非 Javadoc）
	* <p>重写方法: getCountByCriteria|描述:根据条件获取记录数量 </p>
	* @param detachedCriteria 查询条件
	* @return 记录数量
	* @see fmss.services.CommonService#getCountByCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public int getCountByCriteria(final DetachedCriteria detachedCriteria){
		return getBem().getCountByCriteria(detachedCriteria);
	};

	/* （非 Javadoc）
	* <p>重写方法: getByFormWithPaging|描述:根据查询获取page对象 </p>
	* @param detachedCriteria 查询条件
	* @param pageSize 页面size
	* @param pageNum 页数
	* @return 分页对象
	* @see fmss.services.CommonService#getByFormWithPaging(org.hibernate.criterion.DetachedCriteria, int, int)
	*/
	public PageBox getByFormWithPaging(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNum){
		return getBem()
				.getByFormWithPaging(detachedCriteria, pageSize, pageNum);
	};

	/**
	 * <p>方法名称: getAuthRoleResourceByObjectId|描述: 根据权限主体ID,返回所有当前主体资源信息</p>
	 * @param objectId 权限主体Id
	 * @param resType 资源类型
	 * @return 权限主体的所有资源
	 */
	public List getAuthRoleResourceByObjectId(String objectId, String resType){
		DetachedCriteria dc = DetachedCriteria
				.forClass(UAuthRoleResourceDO.class);
		dc.add(Property.forName("objectId").eq(objectId));
		dc.createAlias("uauthResMap", "resMap");
		dc.add(Property.forName("resMap.resType").eq(resType));
		List roleResources = getBem().getAllByDetachedCriteria(dc);
		return roleResources;
	}

	/**
	 * <p>方法名称: getBaseConfigBySystemId|描述:根据系统编号，返回子系统信息 </p>
	 * @param systemId 子系统id
	 * @return 子系统基本信息
	 */
	public UBaseConfigDO getBaseConfigBySystemId(String systemId){
		DetachedCriteria dc = DetachedCriteria.forClass(UBaseConfigDO.class);
		dc.add(Property.forName("systemId").eq(systemId));
		List ubcs = getBem().getAllByDetachedCriteria(dc);
		UBaseConfigDO ubc = (UBaseConfigDO) ubcs.get(0);
		return ubc;
	}

	/**
	 * <p>方法名称: getRoleById|描述:根据roleID,返回当前role信息 </p>
	 * @param roleId 角色Id
	 * @return 角色列表
	 */
	public List getRoleById(String roleId){
		DetachedCriteria dc = DetachedCriteria.forClass(UAuthRoleDO.class);
		dc.add(Property.forName("roleId").eq(roleId));
		List roleList = getBem().getAllByDetachedCriteria(dc);
		for(Iterator iterator = roleList.iterator(); iterator.hasNext();){
			UAuthRoleDO role = (UAuthRoleDO) iterator.next();
			List authRoleRes = this.getAuthRoleResourceByObjectId(role
					.getRoleId());
			role.setAuthRoleRes(authRoleRes);
		}
		return roleList;
	}

	/**
	 * <p>方法名称: getAuthRoleResourceByObjectId|描述:根据权限主体ID,返回所有当前主体资源对照表 </p>
	 * @param objectId 对象id
	 * @return 当前主体资源列表
	 */
	public List getAuthRoleResourceByObjectId(String objectId){
		DetachedCriteria dc = DetachedCriteria
				.forClass(UAuthRoleResourceDO.class);
		dc.add(Property.forName("objectId").eq(objectId));
		List roleResources = getBem().getAllByDetachedCriteria(dc);
		
		return roleResources;
	}

	/**
	 * <p>方法名称: getAllRole|描述:所有角色及权限资源 </p>
	 * @return 所有角色及资源列表
	 */
	public List getAllRole(){
		List roleList = getBem().getAllObject(UAuthRoleDO.class);
		for(Iterator iterator = roleList.iterator(); iterator.hasNext();){
			UAuthRoleDO role = (UAuthRoleDO) iterator.next();
			// 取每个角色对应的权限资源
			List authRoleRes = this.getAuthRoleResourceByObjectId(role
					.getRoleId());
			role.setAuthRoleRes(authRoleRes);
		}
		return roleList;
	}

	/**
	 * <p>方法名称: getUserByRole|描述:根据角色获取已有用户 </p>
	 * @param role 角色对象
	 * @return 角色对应用户列表
	 */
	public List getUserByRole(UAuthRoleDO role){
		return this.userService.getUsersByRole(role.getRoleId());
	}

	/**
	 * <p>方法名称: getUserByRole|描述:根据角色获取已有用户 </p>
	 * @param role 角色对象
	 * @param paginationList 分页对象
	 * @param instId 机构ID
	 * @param fixQuery 
	 * @param string 
	 * @param string2 
	 * @return 角色对应用户列表
	 */
	public List getUserByRole(UAuthRoleDO role, PaginationList paginationList,String instId,UBaseUserDO user, boolean fixQuery){
		String hql = "select ubu,uar.roleName from UBaseUserDO ubu left join ubu.ubaseInst ubi,UAuthRoleUserDO uaru ,UAuthRoleDO uar "+ 
				"where uaru.userId=ubu.userId and uar.roleId=uaru.roleId ";
		List list = new ArrayList();
		if(fixQuery){
			if (user!=null&&StringUtils.isNotEmpty(user.getUserEname())) {
				hql+=" and (ubu.userEname like ? or ubu.userCname like ?) ";
				list.add("%"+user.getUserEname()+"%");
				list.add("%"+user.getUserEname()+"%");
			}
			if (user!=null&&StringUtils.isNotEmpty(user.getInstName())) {
				hql+=" and ubi.instName like ?";
				list.add("%"+user.getInstName()+"%");
			}
		}else{
			list.add(role.getRoleId());
			hql+=" and uaru.roleId=? ";
		}
		
		hql+=" and exists (select 1 from UBaseInstDO a where a.instId=? and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))"+
				"order by ubu.instId,ubu.userCname";
		list.add(instId);
		return getBem().find(hql, list, paginationList);
	}

	/**
	 * <p>方法名称: getUserByRole|描述:根据角色获取该角色没有的用户 </p>
	 * @param role 角色对象
	 * @param instId 机构Id
	 * @return 该角色没有的用户列表
	 */
	public List getLevUserByRole(UAuthRoleDO role,String instId,PaginationList paginationList){
		String query = "select ub from UBaseUserDO ub left join ub.baseInst ubi"
				+ " where ub.userId not in ( select uarr.userId from UAuthRoleUserDO uarr "
				+ " where uarr.roleId=?)"
		        + " and exists (select 1 from UBaseInstDO a where a.instId=? and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC))){
			query += " and (ub.isDelete!='1' or ub.isDelete is null) ";
		}
		List list=new ArrayList();
		list.add(role.getRoleId());
		list.add(instId);
		query += " order by ub.userId";
		return getBem().find(query, list, paginationList);
	}

	/*
	 * 自己定义测试的类
	 * */
	public List getHavLevUserByRole(UBaseUserDO user, UAuthRoleDO role,PaginationList paginationList){
		String query = "select ub from UBaseUserDO ub"
				+ " where ub.userId not in ( select uarr.userId from UAuthRoleUserDO uarr "
				+ " where uarr.roleId=?)";
		Map map = new LinkedMap();
		// 组合条件查询，如果带有查询参数则将查询条件和对应的值放入map中
		/*if(StringUtils.isNotEmpty(user.getInstName())){
			String condition = " and ub.instName like ?";
			map.put(condition, "%" + user.getInstName() + "%");
		}*/
		
		// 增加按名称查询
		if(StringUtils.isNotEmpty(user.getInstName())){
			String condition = " and ub.instId in ( select ui.instId from UBaseInstDO ui where ui.instName like ? ) ";
			map.put(condition, "%" + user.getInstName() + "%");
		}
		
		if(StringUtils.isNotEmpty(user.getUserEname())){
			// String condition = " and ub.userEname like ?";
			String condition = " and (ub.userEname like ? ";
			String conditionOr = " or ub.userCname like ?)";
			map.put(condition, "%" + user.getUserEname() + "%");
			map.put(conditionOr, "%" + user.getUserEname() + "%");
		}
		List valueList = new ArrayList();
		valueList.add(role.getRoleId());
		Set keySet = map.keySet();
		// 迭代map，将查询条件连接在原有hql之后,每个条件对应的值放入list
		for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
			String key = (String) iterator.next();
			query += key;
			valueList.add(map.get(key));
		}
		if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC))){
			query += " and (ub.isDelete!='1' or ub.isDelete is null) ";
		}
		query += " order by ub.userId";
		// 根据查询条件和对应的条件数据查询结果
		return getBem().find(query, valueList,paginationList);
 
	}

	/**
	 * <p>方法名称: getUserByUserId|描述:根据用户id获取用户 </p>
	 * @param userId 用户id
	 * @return 用户对象
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return this.userService.getUserByUserId(userId);
	}

	/**
	 * <p>方法名称: getRoleByRoleId|描述: 根据角色id获取角色</p>
	 * @param roleId 角色id
	 * @return 角色对象
	 */
	public UAuthRoleDO getRoleByRoleId(String rolroleIdeId){
		return (UAuthRoleDO) this.getBem().get(UAuthRoleDO.class, rolroleIdeId);
	}

	/**
	 * <p>方法名称: getResByRole|描述:根据角色获取资源 </p>
	 * @param role 角色对象
	 * @return 角色对应的资源的列表
	 */
	public List getResByRole(UAuthRoleDO role){
		// 根据权限主体id查询
		DetachedCriteria dc = DetachedCriteria
				.forClass(UAuthRoleResourceDO.class);
		dc.add(Property.forName("objectId").eq(role.getRoleId()));
		return getBem().getAllByDetachedCriteria(dc);
	}

	/**
	 * <p>方法名称: getHavUserByRole|描述: 带用户输入条件的配置用户查询</p>
	 * @param user 用户对象
	 * @param role 角色对象
	 * @param instId 机构Id
	 * @return 角色对应的用户列表
	 */
	public List getHavUserByRole(UBaseUserDO user, UAuthRoleDO role,String instId){
		String query = "select ub from UBaseUserDO ub left join ub.ubaseInst ubi"
				+ " where ub.userId in ( select uarr.userId from UAuthRoleUserDO uarr  where uarr.roleId=?)"
				+ " and exists (select 1 from UBaseInstDO a where a.instId=? and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		
		Map map = new LinkedHashMap();
		// 组合条件查询，如果带有查询参数则将查询条件和对应的值放入map中
		/*
		if(StringUtils.isNotEmpty(user.getUserCname())){
			String condition = " and ub.userCname like ?";
			map.put(condition, "%" + user.getUserCname() + "%");
		}
		*/
		if(StringUtils.isNotEmpty(user.getInstId())){
			String condition = " and ub.instId like ?";
			map.put(condition, "%" + user.getInstId() + "%");
		}
		// 增加按名称查询
		if(StringUtils.isNotEmpty(user.getInstName())){
			String condition = " and ub.instId in ( select ui.instId from UBaseInstDO ui where ui.instName like ? ) ";
			map.put(condition, "%" + user.getInstName() + "%");
		}
		// 增加按名称查询
		if(StringUtils.isNotEmpty(user.getUserCname())){
//			String condition = " and ub.instId in ( select ui.instId from UBaseInstDO ui where ui.instName like ? ) ";
//			map.put(condition, "%" + user.getInstName() + "%");
			
			String conditionOr = " and ub.userCname like ?";			
			map.put(conditionOr, "%" + user.getUserCname() + "%");
		}
		if(StringUtils.isNotEmpty(user.getUserEname())){
			String condition = " and (ub.userEname like ? ";
			String conditionOr = " or ub.userCname like ?)";
			map.put(condition, "%" + user.getUserEname() + "%");
			map.put(conditionOr, "%" + user.getUserEname() + "%");
		}
		List valueList = new ArrayList();
		valueList.add(role.getRoleId());
		valueList.add(instId);
		Set keySet = map.keySet();
		// 迭代map，将查询条件连接在原有hql之后,每个条件对应的值放入list
		for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
			String key = (String) iterator.next();
			query += key;
			valueList.add(map.get(key));
		}
		query += "order by ub.instId,ub.userCname";
		// 根据查询条件和对应的条件数据查询结果
		return getBem().find(query, valueList.toArray(new Object[] {}));
	}

	/**
	 * <p>方法名称: deleteConfigUser|描述:删除角色对应的角色-用户关系 </p>
	 * @param role 角色对象
	 */
	public void deleteConfigUser(UAuthRoleDO role){
		// 取角色对应的用户的列表
		List users = this.getUserByRole(role);
		List roleUsers = new ArrayList();
		// 形成角色-用户对应关系，然后删除角色-用户关系表
		for(Iterator iterator = users.iterator(); iterator.hasNext();){
			UBaseUserDO user = (UBaseUserDO) iterator.next();
			UAuthRoleUserDO roleUser = new UAuthRoleUserDO();
			roleUser.setRoleId(role.getRoleId());
			roleUser.setUserId(user.getUserId());
			roleUsers.add(roleUser);
		}
		this.deleteAll(roleUsers);
	}

	/**
	 * <p>方法名称: deleteConfigUser|描述: 删除角色对应的角色-用户关系</p>
	 * @param userList 用户列表
	 * @param role 角色对象
	 */
	public void deleteConfigUser(List userList, UAuthRoleDO role){
		List roleUsers = new ArrayList();
		// 形成角色-用户对应关系，然后删除角色-用户关系表
		for(Iterator iterator = userList.iterator(); iterator.hasNext();){
			String userId = iterator.next().toString();
			UAuthRoleUserDO roleUser = new UAuthRoleUserDO();
			roleUser.setRoleId(role.getRoleId());
			roleUser.setUserId(userId);
			roleUsers.add(roleUser);
		}
		this.deleteAll(roleUsers);
	}

	/**
	 * <p>方法名称: saveConfigUser|描述:保存用户-角色关系配置 </p>
	 * @param userList 用户列表
	 * @param role 角色对象
	 */
	public void saveConfigUser(List userList, UAuthRoleDO role){
		if(CollectionUtils.isNotEmpty(userList)){
			// 形成角色-用户对应关系，然后保存角色-用户关系表
			for(Iterator iterator = userList.iterator(); iterator.hasNext();){
				String userId = (String) iterator.next();
				UAuthRoleUserDO roleUser = new UAuthRoleUserDO();
				roleUser.setRoleId(role.getRoleId());
				roleUser.setUserId(userId);
				if(getBem().get(UAuthRoleUserDO.class, roleUser) == null){
					getBem().save(roleUser);
				}
			}
		}
	}

	/**
	 * <p>方法名称: deleteConfigRes|描述:删除角色对应的资源-角色关系 </p>
	 * @param role 角色对象
	 */
	public void deleteConfigRes(UAuthRoleDO role){
		// 获取角色对应的资源
		List res = this.getResByRole(role);
		List roleUser = new ArrayList();
		// 形成角色-资源对应关系，然后删除角色-资源关系表
		for(Iterator iterator = res.iterator(); iterator.hasNext();){
			UAuthRoleResourceDO roleRes = (UAuthRoleResourceDO) iterator.next();
			UAuthRoleResourceDO urr = new UAuthRoleResourceDO();
			urr.setObjectId(role.getRoleId());
			urr.setResId(roleRes.getResId());
			roleUser.add(urr);
		}
		this.deleteAll(roleUser);
	}

	/**
	 * <p>方法名称: getAuthResMap|描述:根据资源id获取资源 </p>
	 * @param resId 资源id
	 * @return 资源信息
	 */
	public UAuthResMapDO getAuthResMap(String resId){
		UAuthResMapDO authResMap = (UAuthResMapDO) this.get(
				UAuthResMapDO.class, resId);
		return authResMap;
	}

	/**
	 * <p>方法名称: getSystemList|描述:获取子系统列表 </p>
	 * @return 子系统列表
	 */
	//modified by fwy 增加对子系统是否启用的判断
	public List getSystemList(boolean isAll){
		return this.find("select ubc from UBaseConfigDO ubc"
				+ (isAll ? "" : " where ubc.systemId<>'00003' and ubc.enabled!='false'")
				+ " order by ubc.systemId");
	}

	/**
	 * <p>方法名称: getRoleBySysId|描述:根据子系统id获取子系统下角色 </p>
	 * @param systemId 子系统id
	 * @return 子系统下的角色
	 */
	public List getRoleBySysId(String systemId){
		return this.find("select ar from UAuthRoleDO ar where ar.systemId=? order by roleName",
				systemId);
	}
	public List getRoleBySysId(String systemId,String userId){
			List list=new ArrayList();
			list.add(systemId);
			list.add(userId);
			return this.find("select ar from UAuthRoleDO ar where ar.systemId=? " +
					" and (exists (select 1 from UBaseUserDO t left join t.baseInst t1 where t.userId=? and t1.isHead='true') or ar.isHead='false') order by roleName",
					list);
		
	}

	/* （非 Javadoc）
	* <p>重写方法: deleteAll|描述:删除对象集合 </p>
	* @param entities 对象集合
	* @see fmss.services.CommonService#deleteAll(java.util.Collection)
	*/
	public void deleteAll(Collection entities){
		getBem().deleteAll(entities);
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据hql查询 </p>
	* @param queryString 查询hql
	* @return 查询对象集合
	* @see fmss.services.CommonService#find(java.lang.String)
	*/
	public List find(String queryString){
		return getBem().find(queryString);
	}

	/* （非 Javadoc）
	* <p>重写方法: delete|描述:删除对象 </p>
	* @param object 对象
	* @see fmss.services.CommonService#delete(java.lang.Object)
	*/
	public void delete(Object object){
		getBem().delete(object);
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据参数使用hql查询 </p>
	* @param query  查询hql
	* @param parameter 参数列表
	* @return 查询对象集合
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object)
	*/
	public List find(String query, Object parameter){
		return getBem().find(query, parameter);
	}

	/**
	 * <p>方法名称: getRoleTreeAsynXml|描述:异步获取xml树 </p>
	 * @param sysList 子系统列表
	 * @param id 树节点id
	 * @return xml树字符串
	 */
	public String getRoleTreeAsynXml(List sysList, String id){
		StringBuffer sb = new StringBuffer();
		List roleList;
		// 判断子系统id是否为空
		if(StringUtils.isNotBlank(id)){
			// 取子系统下对应的角色列表形成xml
			roleList = this.getRoleBySysId(id);
			if(roleList != null){
				sb.append("<tree id='");
				sb.append(id);
				sb.append("'>");
				for(int j = 0; j < roleList.size(); j++){
					UAuthRoleDO role = (UAuthRoleDO) roleList.get(j);
					sb.append("<item text='");
					sb.append(role.getRoleName());
					sb.append("' id='");
					sb.append(role.getRoleId());
					sb.append("'>");
					sb.append("<userdata name='levelType'>2</userdata>");
					sb.append("</item>");
				}
				sb.append("</tree> ");
			}
			// 将子系统信息作为根节点形成xml
		}else{
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<tree id='0'>");
			for(Iterator iterator = sysList.iterator(); iterator.hasNext();){
				UBaseConfigDO cfg = (UBaseConfigDO) iterator.next();
				String sysId = cfg.getSystemId();
				sb.append("<item text='");
				sb.append(cfg.getSystemCname());
				sb.append("' id='");
				sb.append(sysId);
				sb.append("' child='1");
				sb.append("'>");
				sb.append("<userdata name='levelType'>1</userdata>");
				sb.append("</item>");
			}
			sb.append("</tree>");
		}
		return sb.toString();
	}

	/**
	 * <p>方法名称: getRoleTreeSyncXml|描述:同步获取xml树 </p>
	 * @param sysList 子系统列表
	 * @param id 树节点id
	 * @return xml树字符串
	 */
	/*
	public String getRoleTreeSyncXml(List sysList, String id){
		StringBuffer sb = new StringBuffer();
		List roleList;
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<tree id='0'>");
		for(Iterator iterator = sysList.iterator(); iterator.hasNext();){
			UBaseConfigDO cfg = (UBaseConfigDO) iterator.next();
			String sysId = cfg.getSystemId();
			sb.append("<item text='");
			sb.append(cfg.getSystemCname());
			sb.append("' id='");
			sb.append(sysId);
			sb.append("' child='1");
			sb.append("'>");
			sb.append("<userdata name='levelType'>1</userdata>");
			// 取每个子系统对应的角色列表
			roleList = this.getRoleBySysId(sysId);
			for(int j = 0; j < roleList.size(); j++){
				UAuthRoleDO role = (UAuthRoleDO) roleList.get(j);
				sb.append("<item text='");
				sb.append(role.getRoleName());
				sb.append("' id='");
				sb.append(role.getRoleId());
				sb.append("'>");
				sb.append("<userdata name='levelType'>2</userdata>");
				sb.append("</item>");
			}
			sb.append("</item>");
		}
		sb.append("</tree>");
		return sb.toString();
	}*/
	/**
	 * <p>方法名称: getRoleTreeSyncXmlEx|描述:同步获取xml树,换新控件 </p>
	 * @param sysList 子系统列表
	 * @param id 树节点id
	 * @param userId 用户Id
	 * @return xml树字符串
	 */
	public String getRoleTreeSyncXmlEx(List sysList, String id, String userId){
		StringBuffer sb = new StringBuffer();
		List roleList;
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<Response><Data><Tree>");
		for(Iterator iterator = sysList.iterator(); iterator.hasNext();){
			UBaseConfigDO cfg = (UBaseConfigDO) iterator.next();
			String sysId = cfg.getSystemId();
			// 如果是系统管理则不加载
			if(SYSTEM_ID.equals(sysId))
				continue;
			sb.append("<TreeNode name='");
			sb.append(cfg.getSystemCname());
			sb.append("' id='");
			sb.append(sysId);
//			 sb.append("' child='1");
			sb.append("' levelType='1' ");
			// 取每个子系统对应的角色列表
			roleList = this.getRoleBySysId(sysId,userId);
			if(roleList != null && roleList.size() > 0){
				sb.append(" _hasChild='1' ");
			}
			sb.append(" _canSelect='0' ");
			sb.append(">");
			for(int j = 0; j < roleList.size(); j++){
				UAuthRoleDO role = (UAuthRoleDO) roleList.get(j);
				sb.append("<TreeNode name='");
				sb.append(role.getRoleName());
				sb.append("' id='");
				sb.append(role.getRoleId());
				sb.append("' levelType='2'");
				sb.append(" _hasChild='0' ");
				sb.append(" _canSelect='1' ");
				sb.append(">");
				sb.append("</TreeNode>");
			}
			sb.append("</TreeNode>");
		}
		sb.append("</Tree></Data></Response>");
		return sb.toString();
	}
	/**
	 * <p>方法名称: getRoleTreeSyncXmlEx|描述:同步获取xml树,换新控件 </p>
	 * @param sysList 子系统列表
	 * @param id 树节点id
	 * @param userId 用户Id
	 * @return xml树字符串
	 */
	public List getRoleZTreeSyncXmlEx(List sysList, String id, String userId){
		StringBuffer sb = new StringBuffer();
		List ztreeList = new ArrayList();
		List roleList;
//		sb.append("[");
		int m = 0;
		for(Iterator iterator = sysList.iterator(); iterator.hasNext();){
			m++;
			UBaseConfigDO cfg = (UBaseConfigDO) iterator.next();
			String sysId = cfg.getSystemId();
			// 如果是系统管理则不加载
			if(SYSTEM_ID.equals(sysId))
				continue;
			Map itemMap = new HashMap();
			itemMap.put("id", m);
			itemMap.put("pId", 0);
			itemMap.put("name", cfg.getSystemCname());
			itemMap.put("pIds", sysId);
			itemMap.put("isParent", true);
			ztreeList.add(itemMap);
			// 取每个子系统对应的角色列表
			roleList = this.getRoleBySysId(sysId,userId);
			if(null != roleList && roleList.size()>0){
				for(int j = 0; j < roleList.size(); j++){
					UAuthRoleDO role = (UAuthRoleDO) roleList.get(j);
					Map subItemMap = new HashMap();
					subItemMap.put("id", m*10+j+1);
					subItemMap.put("pId", m);
					subItemMap.put("pIds", role.getRoleId());
					subItemMap.put("name", role.getRoleName());
					ztreeList.add(subItemMap);
				}
			}
		}
		return ztreeList;
	}
	/**
	 * <p>方法名称: getUser|描述:获取单一配置用户 </p>
	 * @param user 用户信息
	 * @return 用户详细信息
	 */
	public UBaseUserDO getUser(UBaseUserDO user){
		return this.userService.getUserByUserId(user.getUserId());
	}

	/**
	 * <p>方法名称: getBaseConfig|描述:获取用户对应管理的子系统 </p>
	 * @param user 用户信息
	 * @return 用户的子系统列表
	 */
	public List getBaseConfig(UBaseUserDO user){
		
		String query = "select uc from UBaseConfigDO uc where (uc.systemId in("
				+ " select uaa.systemId as systemId from UAuthSystemAdminDO "
				+ " uaa where uaa.userId = ?) or '00003' in (select uaa.systemId as systemId from UAuthSystemAdminDO uaa where uaa.userId = ? )) and uc.systemId!='00003' and uc.enabled != 'false' and uc.systemId!='00000'   order by uc.systemId";
		List list=new ArrayList();
		list.add(user.getUserId());
		list.add(user.getUserId());
		return this.find(query, list);
	}
	/**
	 * <p>方法名称: getBaseConfig|描述:获取用户管理类型</p>
	 * @param userId 用户Id
	 * @return 1:usys管理员,2:其他系统管理员,3:普通用户
	 */
	public int getAdminType(String userId){
		String query =null;
		
		query = "select 1 from UBaseConfigDO uc where uc.systemId='00003' and uc.systemId in("
			  + " select uaa.systemId as systemId from UAuthSystemAdminDO "
			  + " uaa where uaa.userId = ?) and uc.enabled != 'false' order by uc.systemId";
		if(this.find(query, userId).size()>0)
			return 1;
		
		query = "select 1 from UBaseConfigDO uc where uc.systemId in("
			  + " select uaa.systemId as systemId from UAuthSystemAdminDO "
			  + " uaa where uaa.userId = ?) and uc.enabled != 'false' and uc.systemId!='00000' and uc.systemId!='00003'  order by uc.systemId";
		if(this.find(query, userId).size()>0)
			return 2;
		
		return 3;
	}

	/**
	 * <p>方法名称: getRoleByUser|描述:获取用户对应角色 </p>
	 * @param user 用户信息
	 * @return 用户对应的角色
	 */
	public List getRoleByUser(UBaseUserDO user){
		return this.userService.getAuthRoleByUserId(user.getUserId());
	}
	
	//限定子系统范围
	public List getRoleByUserRangeBySystem(UBaseUserDO user, LoginDO login) {
		String hql = "from UAuthRoleUserDO where userId=?" + AuditBase.appendHaveSystemAuthsRangeHQL(login);
		List l = find(hql, user.getUserId());
		return l;
	}

	/**
	 * <p>方法名称: getUserByInst|描述:获取某一机构下所有用户 </p>
	 * @param inst 机构信息
	 * @return 机构下的用户
	 */
	public List getUserByInst(UBaseInstDO inst){
		return getBem().find("from UBaseUserDO u where u.instId=?", inst
				.getInstId());
	}

	/**
	 * <p>方法名称: getUserTreeSyncXml|描述:同步获取用户xmltree </p>
	 * @return xml树的字符串
	 */
	public String getUserTreeSyncXml(){
		StringBuffer sb = new StringBuffer();
		List userList;
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<tree id='0'>");
		// 取全部机构
		for(Iterator iterator = this.getAllObject(UBaseInstDO.class).iterator(); iterator
				.hasNext();){
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			String instId = inst.getInstId();
			sb.append("<item text='");
			sb.append(inst.getInstName());
			sb.append("' id='");
			sb.append(instId);
			sb.append("' child='1");
			sb.append("'>");
			sb.append("<userdata name='levelType'>1</userdata>");
			// 取各个机构下用户
			userList = this.getUserByInst(inst);
			for(int j = 0; j < userList.size(); j++){
				UBaseUserDO user = (UBaseUserDO) userList.get(j);
				sb.append("<item text='");
				sb.append(user.getUserCname());
				sb.append("' id='");
				sb.append(user.getUserId());
				sb.append("'>");
				sb.append("<userdata name='levelType'>2</userdata>");
				sb.append("</item>");
			}
			sb.append("</item>");
		}
		sb.append("</tree>");
		return sb.toString();
	}

	/**
	 * <p>方法名称: getUserTreeAsynXml|描述:异步获取机构用户树 </p>
	 * @param instId 机构id
	 * @return xml树的字符串
	 */
	public String getUserTreeAsynXml(String instId){
		StringBuffer sb = new StringBuffer();
		UBaseInstDO inst = new UBaseInstDO();
		// 如果机构id不为空，则取该机构id的下级机构
		if(StringUtils.isNotBlank(instId)){
			// 取下级机构
			List listInst = this.instService.getAllInstByParentInst(instId);
			UBaseInstDO parentInst = new UBaseInstDO();
			parentInst.setInstId(instId);
			List users = this.getUserByInst(parentInst);
			if(listInst != null){
				sb.append("<tree id='");
				sb.append(instId);
				sb.append("'>");
				for(int i = 0; i < listInst.size(); i++){
					inst = (UBaseInstDO) listInst.get(i);
					sb.append("<item text='");
					sb.append(inst.getInstSmpName());
					sb.append("[" + inst.getInstId() + "]");
					sb.append("' id='");
					sb.append(inst.getInstId());
					sb.append("' child='1'>");
					sb.append("<userdata name='levelType'>1</userdata>");
					sb.append("</item>");
				}
				for(int i = 0; i < users.size(); i++){
					UBaseUserDO user = (UBaseUserDO) users.get(i);
					sb.append("<item text='");
					sb.append(user.getUserCname());
					sb.append("' id='");
					sb.append(user.getUserId());
					sb.append("' child='1'>");
					sb.append("<userdata name='levelType'>2</userdata>");
					sb.append("</item>");
				}
				sb.append("</tree> ");
			}
		}else{
			// 取顶层机构的树的根节点
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<tree id='0'>");
			List listInst = this.instService.getAllInsts();
			for(int i = 0; i < listInst.size(); i++){
				inst = (UBaseInstDO) listInst.get(i);
				// 判断是不是顶层机构
				if("0".equals(inst.getInstLayer().toString())){
					sb.append("<item text='");
					sb.append(inst.getInstSmpName());
					sb.append("[" + inst.getInstId() + "]");
					sb.append("' id='");
					sb.append(inst.getInstId());
					sb.append("' child='1'>");
					sb.append("<userdata name='levelType'>1</userdata>");
					sb.append("</item>");
				}
			}
			sb.append("</tree>");
		}
		return sb.toString();
	}

	/**
	 * @return 用户服务对象
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * @param userService 要设置的 用户服务对象
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * @return 机构服务对象
	 */
	public InstService getInstService(){
		return instService;
	}

	/**
	 * @param instService 要设置的 机构服务对象
	 */
	public void setInstService(InstService instService){
		this.instService = instService;
	}

	public List getRole( String roleId,String systemId, String roleName) {
		// TODO Auto-generated method stub
		List list=new ArrayList();
		if(StringUtil.isEmpty(roleId)){
			list.add(systemId);
			list.add(roleName);
			return this.getBem().find("from UAuthRoleDO where   systemId=? and roleName=? ", list);
		}else{
			list.add(roleId);
			list.add(systemId);
			list.add(roleName);
			return this.getBem().find("from UAuthRoleDO where  roleId!=? and systemId=? and roleName=? ", list);
		}
		
		
	}


}
