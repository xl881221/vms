package fmss.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fmss.action.base.AuditBase;
import fmss.action.base.InstBaseAuditBase;
import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.SubSystemAdminAuditBase;
import fmss.action.base.SubSystemChangeAuditBase;
import fmss.action.entity.SubSystemAdminChangeDO;
import fmss.action.entity.UBaseConfigChangeDO;
import fmss.common.util.BeanUtil;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.MenuDO;
import fmss.dao.entity.UAuthSystemAdminDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: yuanshihong
 * @日期: 2009-6-23 上午09:56:51
 * @描述: [SubSystemService]提供子系统信息的业务操作功能
 */
public class SubSystemService extends CommonService{
	
	private CacheManager cacheManager;

	private JdbcDaoAccessor jdbcDaoAccessor;
	
	public JdbcDaoAccessor getJdbcDaoAccessor() {
		return jdbcDaoAccessor;
	}

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor) {
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/** idGenerator 子系统Id生成器 */
	private static IdGenerator  idGenerator; // id生成器

	/**
	 * <p>方法名称: save|描述: 插入指定的子系统对象</p>
	 * @param baseConfig 待插入的子系统对象
	 */
	public void save(final UBaseConfigDO baseConfig){
		idGenerator = IdGenerator.getInstance("SYS");
		baseConfig.setSystemId(String.valueOf(idGenerator.getNextKey()));
		super.save(baseConfig);
	};
	
	/**
	 * <p>方法名称: saveChange|描述: 插入指定的子系统对象</p>
	 * @param baseConfigChange 待插入的子系统对象
	 */
	public void saveChange(UBaseConfigDO ubc,LoginDO user) {
		UBaseConfigChangeDO ubcc = copyUbc(ubc);
		String sql="delete from "+SubSystemChangeAuditBase.CFG_BAK_TABLE+" where system_id = ?";
		jdbcDaoAccessor.update(sql, new Object[]{ubcc.getSystemId()});
		ubcc.setChangeUser(user.getUserId());
		ubcc.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
		ubcc.setChangeStatus(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY);
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		long id = idGenerator.getNextKey();
		ubcc.setId(new Long(id));
		ubcc.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));
		ubcc.getConfigAuditEntity().setValueFields(
				SubSystemChangeAuditBase.fullBaseCfgAttributeFields);
		ubcc.getConfigAuditEntity().setColumnFields(
				SubSystemChangeAuditBase.fullBaseCfgColumnFields);
		jdbcDaoAccessor.save(SubSystemChangeAuditBase.CFG_BAK_TABLE, ubcc, ubcc
				.getConfigAuditEntity().getValueFields(), ubcc
				.getConfigAuditEntity().getColumnFields());

	};
	//判断是否修改过
	public boolean modifiedAdmin(String systemId)
	{
		String sql ="select count(*) from "+SubSystemAdminAuditBase.SUB_BAK_TABLE+ " where system_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { systemId});
		if(count>0) return true ;
		return false ;
	}
	
	//判断是否修改过
	public boolean modifiedSystem(String systemId)
	{
		String sql ="select count(*) from "+SubSystemChangeAuditBase.CFG_BAK_TABLE+ " where system_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { systemId});
		if(count>0) return true ;
		return false ;
	}
	//获取子系统名
	public String getSystemCname(String systemId){
		String sql ="select system_cname from "+SubSystemChangeAuditBase.CFG_MAIN_TABLE+" where system_id=?";
		Map map=jdbcDaoAccessor.findForMap(sql, new Object[]{systemId});
		return (String)map.get("SYSTEM_CNAME");
	}
    
	public UBaseConfigChangeDO copyUbc(UBaseConfigDO ubc){
		 UBaseConfigChangeDO ubcc=new UBaseConfigChangeDO();
		 ubcc.setDbUrl(ubc.getDbUrl());
		 ubcc.setDisplay(ubc.getDisplay());
		 ubcc.setEnabled(ubc.getEnabled());
		 ubcc.setLinkSiteUrl(ubc.getLinkSiteUrl());
		 ubcc.setLinkTarget(ubc.getLinkTarget());
		 ubcc.setMenuImgSrc(ubc.getMenuImgSrc());
		 ubcc.setMenuName(ubc.getMenuName());
		 ubcc.setMenuOrderNum(ubc.getMenuOrderNum());
		 ubcc.setMenuRes(ubc.getMenuRes());
		 ubcc.setMenuTable(ubc.getMenuTable());
		 ubcc.setResDbPassword(ubc.getResDbPassword());
		 ubcc.setResDbServerIp(ubc.getResDbServerIp());
		 ubcc.setResDbServerPort(ubc.getResDbServerPort());
		 ubcc.setResDbSid(ubcc.getResDbSid());
		 ubcc.setResDbType(ubc.getResDbType());
		 ubcc.setResDbUserId(ubc.getResDbUserId());
		 ubcc.setSystemCname(ubc.getSystemCname());
		 ubcc.setSystemEname(ubc.getSystemEname());
		 ubcc.setSystemId(ubc.getSystemId());
		 ubcc.setSystemNickCname(ubc.getSystemNickCname());
		 ubcc.setSystemNickEname(ubc.getSystemNickEname());
		 ubcc.setUnitLoginUrl(ubc.getUnitLoginUrl());
		 ubcc.setLinkSiteInnerUrl(ubc.getLinkSiteInnerUrl());
		 ubcc.setUnitLoginInnerUrl(ubc.getUnitLoginInnerUrl());
		return ubcc;
	}

	/**
	 * <p>方法名称: update|描述: 更新指定的子系统</p>
	 * @param baseConfig 要更新的子系统对象
	 */
	public void update(final UBaseConfigDO baseConfig){
		super.update(baseConfig);
	};

	/**
	 * <p>方法名称: delete|描述:删除指定的子系统 </p>
	 * @param baseConfig 要删除的子系统对象
	 */
	public void delete(final UBaseConfigDO baseConfig){
		super.delete(baseConfig);
	};

	/**
	 * <p>方法名称: deletes|描述: 删除多个子系统</p>
	 * @param systemIds 要删除子系统id列表
	 */
	public void deletes(String[] systemIds){
		if(null != systemIds && systemIds.length > 0){
			List removeObj = new ArrayList();
			String ids = "";
			// 拼接选中的systemId
			for(int index = 0; index < systemIds.length; index++){
				UBaseConfigDO ubc = new UBaseConfigDO();
				ubc.setSystemId(systemIds[index]);
				removeObj.add(ubc);
				ids += "'" + systemIds[index] + "',";
			}
			// 删除子系统前, 先清除和改子系统关联的数据(子系统管理员表, 角色表, 用户角色表, 资源映射表, 角色资源权限表)
			if(!StringUtils.isEmpty(ids)){
				ids = ids.substring(0, ids.length() - 1);
				// 清除用户角色表
				String sqlDelete = "delete from UAuthRoleUserDO ru where ru.roleId in "
						+ "(select r.roleId from UAuthRoleDO r where r.systemId in ("
						+ ids + "))";
				this.executeUpdate(sqlDelete);
				// 清除角色表
				sqlDelete = "delete from UAuthRoleDO where systemId in (" + ids
						+ ")";
				this.executeUpdate(sqlDelete);
				// 清除角色资源表
				sqlDelete = "delete from UAuthRoleResourceDO where resId in "
						+ "(select resId from UAuthResMapDO where systemId in ("
						+ ids + "))";
				this.executeUpdate(sqlDelete);
				// 清除资源映射表
				sqlDelete = "delete from UAuthResMapDO where systemId in ("
						+ ids + ")";
				this.executeUpdate(sqlDelete);
				// 清除系统管理员表
				sqlDelete = "delete from UAuthSystemAdminDO where systemId in ("
						+ ids + ")";
				this.executeUpdate(sqlDelete);
			}
			// 清除选中的子系统
			deleteAll(removeObj);
		}
	}

	/**
	 * <p>方法名称: getBaseConfigBySystemId|描述:根据系统ID,返回子系统信息 </p>
	 * @param systemId 子系统ID
	 * @return 子系统对象
	 */
	public UBaseConfigDO getBaseConfigBySystemId(String systemId){
		List ubcs = find("from UBaseConfigDO ubc where ubc.systemId=?",
				systemId);
		if(ubcs.size() > 0)
			return (UBaseConfigDO) ubcs.get(0);
		return null;
	};

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
			o.setSystemId(o.getSystemId()==null? systemId:o.getSystemId());
			return o;
		}
		return null;
	}

	/**
	 * <p>方法名称: querySubSystems|描述: 根据英文和中文名称查询(模糊查询)子系统列表</p>
	 * @param enName 子系统英文名称
	 * @param chName 子系统中文名称
	 * @return 子系统列表
	 */
	public List querySubSystems(String enName, String chName){
		String query = "from UBaseConfigDO as u where 1=1 ";
		List params = new ArrayList();
		if(!StringUtils.isEmpty(chName)){
			query += " and  u.systemCname like  ?";
			params.add("%" + chName + "%");
		}
		if(!StringUtils.isEmpty(enName)){
			query += " and u.systemEname like ? ";
			params.add("%" + enName + "%");
		}
		query += " order by u.menuOrderNum ";
		return this.find(query,params);
	}

	/**
	 * <p>方法名称: queryDbTypes|描述: 查询字典表获取数据库类型</p>
	 * @return 数据库类型列表
	 */
	public List queryDbTypes(){
		String query = "select new Map(dic.dicName as dbEname, dic.description as dbCname) "
				+ "from UBaseDictionaryDO as dic where dic.dicType='DBT'";
		return this.find(query);
	}

	/**
	 * <p>方法名称: queryAllUsers|描述: 查询所有非指定系统管理员的用户</p>
	 * @param systemId 子系统ID
	 * @return 非管理员的用户列表
	 */
	public List queryAllUsers(String systemId,String instId){
		String query = "select new Map(user.userId as userId, "
				+ "user.userEname||'-'||user.userCname as userCname) "
				+ "from UBaseUserDO user left join user.ubaseInst ubi where user.userId not in"
				+ "(select auth.userId from UAuthSystemAdminDO auth where auth.systemId='"
				+ systemId + "') ";
		String logicDelete = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if ("1".equals(logicDelete)) {
			query += " and (user.isDelete!='1' or user.isDelete is null) ";
		}
		if(instId!=null)
			query+=" and exists (select 1 from UBaseInstDO a where a.instId='"+instId+"' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		return this.find(query + " order by user.userCname");
	}
	
	/**
	 * <p>方法名称: queryAllUsersChange|描述: 查询所有非指定系统管理员的用户</p>
	 * @param systemId 子系统ID
	 * @return 非管理员的用户列表
	 */
	public List queryAllUsersChange(String systemId,String instId){
		String query = "select new Map(user.userId as userId, "
				+ "user.userEname||'-'||user.userCname as userCname) "
				+ "from UBaseUserDO user left join user.ubaseInst ubi where user.userId not in"
				+ "(select auth.userId from UAuthSystemAdminChangeDO auth where auth.systemId='"
				+ systemId + "') ";
		String logicDelete = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if ("1".equals(logicDelete)) {
			query += " and (user.isDelete!='1' or user.isDelete is null) ";
		}
		if(instId!=null)
			query+=" and exists (select 1 from UBaseInstDO a where a.instId='"+instId+"' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		return this.find(query + " order by user.userCname");
	}

	/**
	 * <p>方法名称: queryAdmins|描述: 查询指定子系统的管理员列表</p>
	 * @param systemId 子系统ID
	 * @return 用户对象(只包含userId和userCname)列表
	 */
	public List queryAdmins(String systemId,String instId){
		String query = "select new Map(user.userId as userId, "
				+ "user.userEname||'-'||user.userCname as userCname) "
				+ "from UBaseUserDO user left join user.ubaseInst ubi , UAuthSystemAdminDO auth  "
				+ "where user.userId=auth.userId and auth.systemId='"
				+ systemId + "'  ";
		String logicDelete = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if ("1".equals(logicDelete)) {
			query += " and (user.isDelete!='1' or user.isDelete is null) ";
		}
		if(instId!=null)
			query+=" and exists (select 1 from UBaseInstDO a where a.instId='"+instId+"' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		String orderBy = "order by user.userCname";
		return this.find(query + orderBy);
	}
	/**
	 * <p>方法名称: queryAdminsChange|描述: 查询指定子系统的管理员列表</p>
	 * @param systemId 子系统ID
	 * @return 用户对象(只包含userId和userCname)列表
	 */
	public List queryAdminsChange(String systemId,String instId){
		String query = "select new Map(user.userId as userId, "
				+ "user.userEname||'-'||user.userCname as userCname) "
				+ "from UBaseUserDO user left join user.ubaseInst ubi , UAuthSystemAdminChangeDO auth  "
				+ "where user.userId=auth.userId and auth.systemId='"
				+ systemId + "'  ";
		String logicDelete = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if ("1".equals(logicDelete)) {
			query += " and (user.isDelete!='1' or user.isDelete is null) ";
		}
		if(instId!=null)
			query+=" and exists (select 1 from UBaseInstDO a where a.instId='"+instId+"' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		String orderBy = "order by user.userCname";
		return this.find(query + orderBy);
	}

	/**
	 * <p>方法名称: saveAdmins|描述: 保存子系统的管理员设置</p>
	 * @param systemId 子系统ID
	 * @param systemAdmins 子系统管理员ID列表
	 */
	public void saveAdmins(String systemId, List systemAdmins){
		// 清除原来管理员信息
		this.executeUpdate("delete UAuthSystemAdminDO where systemId='"
				+ systemId + "'");
	
		systemAdmins = systemAdmins != null ? systemAdmins : Collections.EMPTY_LIST;
		Iterator admins = systemAdmins.iterator();
		// 添加配置新的管理员信息
		while(admins.hasNext()){
			String userId = (String) admins.next();
			UAuthSystemAdminDO admin = new UAuthSystemAdminDO();
			admin.setSystemId(systemId);
			admin.setUserId(userId);
			this.save(admin);
		}
	}

	/**
	 * <p>方法名称: saveAdmins|描述: 保存子系统的管理员设置</p>
	 * @param systemId 子系统ID
	 * @param systemAdmins 子系统管理员ID列表
	 */
	public String saveAdminsChange(String systemId, List systemAdmins,LoginDO user,List oldSystemAdmins){
		// 清除原来管理员信息
		String sql ="delete "+SubSystemAdminAuditBase.SUB_BAK_TABLE+ " where system_id=?";
		jdbcDaoAccessor.update(sql, new Object[]{systemId});
		systemAdmins = systemAdmins != null ? systemAdmins : Collections.EMPTY_LIST;
		//找出 增删的list内容 addList ,deleteList
		List deleteList = new ArrayList(oldSystemAdmins);
		deleteList.removeAll(systemAdmins);
		List addList = new ArrayList(systemAdmins);
		addList.removeAll(oldSystemAdmins);
		if(addList.size()==0&&deleteList.size()==0) return "没有任何变化";
		else{
		if(addList!=null&&addList.size()>0){
			Iterator addAdmins = addList.iterator();
			while(addAdmins.hasNext()){
				String userId = (String) addAdmins.next();
				insertIntoTables(systemId,userId,user);
				
			}
		}
		if(deleteList!=null&&deleteList.size()>0){
			Iterator deleteAdmins = deleteList.iterator();
			while(deleteAdmins.hasNext()){
				String userId = (String) deleteAdmins.next();
				insertIntoTables(systemId,userId,user);
				String sql4="update "+SubSystemAdminAuditBase.SUB_BAK_TABLE+" set CHANGE_STATUS =? where system_id =? and user_id=?" ;
				jdbcDaoAccessor.update(sql4, new Object[]{AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE,systemId,userId});
				
			}
		}
		return "保存成功,待审核通过后生效!";
	}
	}
 
	 public void insertIntoTables(String systemId,String userId,LoginDO user)
	 {
		 SubSystemAdminChangeDO admin = new SubSystemAdminChangeDO();
			IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
			long id = idGenerator.getNextKey();
			admin.setId(new Long(id));
			admin.setSystemId(systemId);
			String sql3="select SYSTEM_CNAME from u_base_config where system_id =?" ;
			Map map=jdbcDaoAccessor.findForMap(sql3, new Object[]{systemId});
			String systemCname =(String)map.get("SYSTEM_CNAME");
			admin.setSystemCname(systemCname);
			admin.setUserId(userId);
			admin.setChangeUser(user.getUserId());
			admin.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
			admin.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));
			//默认是管理员添加
			admin.setChangeStatus(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY);
			String sql2 = "insert into "
					+ SubSystemAdminAuditBase.SUB_BAK_TABLE
					+ " (USER_ID,SYSTEM_ID,CHANGE_USER,CHANGE_TIME,CHANGE_STATUS,AUDIT_STATUS,ID,SYSTEM_CNAME)"
					+ " values(?,?,?,?,?,?,?,?)";
			jdbcDaoAccessor.update(sql2, new Object[] { admin.getUserId(),
					admin.getSystemId(), admin.getChangeUser(),
					admin.getChangeTime(), admin.getChangeStatus(),
					admin.getAuditStatus(), admin.getId(),admin.getSystemCname()});
	 }
	/**
	 * <p>方法名称: getAllUBaseConfig|描述: 获取所有子系统信息</p>
	 * @return 子系统信息列表
	 */
	public List getAllUBaseConfig(){
		return find("from UBaseConfigDO ");
	}

	/**
	 * <p>方法名称: getSubMenuList|描述: 获取子系统的菜单信息</p>
	 * @param uc
	 * @return
	 */
	public List getSubMenuList(UBaseConfigDO uc){
		List menuTempList = this.find("from MenuDO m where m.systemId='"
				+ uc.getSystemId()
				+ "' and m.enabled='YES' order by m.ordernum");
		Map menuMap = new HashMap();
		List menuList = new ArrayList();
		String parentMenuId = "";
		int index = 0;
		
		if(null != menuTempList && 0 < menuTempList.size()){
			MenuDO parentMenu = null;
			for(int parentIndex=0; parentIndex < menuTempList.size(); parentIndex++){
				parentMenu = (MenuDO) menuTempList.get(parentIndex);
				if(4 == parentMenu.getItemcode().length()){
					parentMenu.setSubMenuList(new ArrayList());
					menuMap.put(parentMenu.getItemcode(), new Integer(menuList.size()));
					menuList.add(parentMenu);
				}
			}
			
			for(int menuIndex = 0; menuIndex < menuTempList.size(); menuIndex++){
				MenuDO menu = (MenuDO) menuTempList.get(menuIndex);
				if(4 < menu.getItemcode().length()){
					if(StringUtils.isNotEmpty(menu.getUrl())){
						if(menu.getUrl().toUpperCase().startsWith("HTTP") || menu.getUrl().toUpperCase().startsWith("FTP")){
							menu.setUrl(menu.getUrl());
							menu.setInnerUrl(menu.getUrl());
						}else{
							if (uc.getLinkSiteUrl() != null) {
								String url=menu.getUrl();
								menu.setUrl(uc.getLinkSiteUrl() + url);
								menu.setInnerUrl(uc.getLinkSiteInnerUrl() + url);
							} else {
								menu.setUrl(menu.getUrl());
								menu.setInnerUrl(menu.getUrl());
							}
						}
					}
					parentMenuId = menu.getItemcode().substring(0,menu.getItemcode().indexOf("."));
					if(menuMap.containsKey(parentMenuId)){
						index = ((Integer) menuMap.get(parentMenuId)).intValue();
						parentMenu = (MenuDO) menuList.get(index);

						if(null != parentMenu){
							parentMenu.getSubMenuList().add(menu);
						}
					}
				}
			}
		}
		
		return menuList;		
	}

	/**
	 * <p>方法名称: getAllUBaseConfig|描述: 获取所有子系统信息</p>
	 * @return CRMS子系统信息列表
	 */
	public List getCrmsUBaseConfig(){
		return find("from UBaseConfigDO where dbUrl like '%crms%' and enabled ='true'");
	}
}
