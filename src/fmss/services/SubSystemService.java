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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: yuanshihong
 * @����: 2009-6-23 ����09:56:51
 * @����: [SubSystemService]�ṩ��ϵͳ��Ϣ��ҵ���������
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

	/** idGenerator ��ϵͳId������ */
	private static IdGenerator  idGenerator; // id������

	/**
	 * <p>��������: save|����: ����ָ������ϵͳ����</p>
	 * @param baseConfig ���������ϵͳ����
	 */
	public void save(final UBaseConfigDO baseConfig){
		idGenerator = IdGenerator.getInstance("SYS");
		baseConfig.setSystemId(String.valueOf(idGenerator.getNextKey()));
		super.save(baseConfig);
	};
	
	/**
	 * <p>��������: saveChange|����: ����ָ������ϵͳ����</p>
	 * @param baseConfigChange ���������ϵͳ����
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
	//�ж��Ƿ��޸Ĺ�
	public boolean modifiedAdmin(String systemId)
	{
		String sql ="select count(*) from "+SubSystemAdminAuditBase.SUB_BAK_TABLE+ " where system_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { systemId});
		if(count>0) return true ;
		return false ;
	}
	
	//�ж��Ƿ��޸Ĺ�
	public boolean modifiedSystem(String systemId)
	{
		String sql ="select count(*) from "+SubSystemChangeAuditBase.CFG_BAK_TABLE+ " where system_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { systemId});
		if(count>0) return true ;
		return false ;
	}
	//��ȡ��ϵͳ��
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
	 * <p>��������: update|����: ����ָ������ϵͳ</p>
	 * @param baseConfig Ҫ���µ���ϵͳ����
	 */
	public void update(final UBaseConfigDO baseConfig){
		super.update(baseConfig);
	};

	/**
	 * <p>��������: delete|����:ɾ��ָ������ϵͳ </p>
	 * @param baseConfig Ҫɾ������ϵͳ����
	 */
	public void delete(final UBaseConfigDO baseConfig){
		super.delete(baseConfig);
	};

	/**
	 * <p>��������: deletes|����: ɾ�������ϵͳ</p>
	 * @param systemIds Ҫɾ����ϵͳid�б�
	 */
	public void deletes(String[] systemIds){
		if(null != systemIds && systemIds.length > 0){
			List removeObj = new ArrayList();
			String ids = "";
			// ƴ��ѡ�е�systemId
			for(int index = 0; index < systemIds.length; index++){
				UBaseConfigDO ubc = new UBaseConfigDO();
				ubc.setSystemId(systemIds[index]);
				removeObj.add(ubc);
				ids += "'" + systemIds[index] + "',";
			}
			// ɾ����ϵͳǰ, ������͸���ϵͳ����������(��ϵͳ����Ա��, ��ɫ��, �û���ɫ��, ��Դӳ���, ��ɫ��ԴȨ�ޱ�)
			if(!StringUtils.isEmpty(ids)){
				ids = ids.substring(0, ids.length() - 1);
				// ����û���ɫ��
				String sqlDelete = "delete from UAuthRoleUserDO ru where ru.roleId in "
						+ "(select r.roleId from UAuthRoleDO r where r.systemId in ("
						+ ids + "))";
				this.executeUpdate(sqlDelete);
				// �����ɫ��
				sqlDelete = "delete from UAuthRoleDO where systemId in (" + ids
						+ ")";
				this.executeUpdate(sqlDelete);
				// �����ɫ��Դ��
				sqlDelete = "delete from UAuthRoleResourceDO where resId in "
						+ "(select resId from UAuthResMapDO where systemId in ("
						+ ids + "))";
				this.executeUpdate(sqlDelete);
				// �����Դӳ���
				sqlDelete = "delete from UAuthResMapDO where systemId in ("
						+ ids + ")";
				this.executeUpdate(sqlDelete);
				// ���ϵͳ����Ա��
				sqlDelete = "delete from UAuthSystemAdminDO where systemId in ("
						+ ids + ")";
				this.executeUpdate(sqlDelete);
			}
			// ���ѡ�е���ϵͳ
			deleteAll(removeObj);
		}
	}

	/**
	 * <p>��������: getBaseConfigBySystemId|����:����ϵͳID,������ϵͳ��Ϣ </p>
	 * @param systemId ��ϵͳID
	 * @return ��ϵͳ����
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
	 * <p>��������: querySubSystems|����: ����Ӣ�ĺ��������Ʋ�ѯ(ģ����ѯ)��ϵͳ�б�</p>
	 * @param enName ��ϵͳӢ������
	 * @param chName ��ϵͳ��������
	 * @return ��ϵͳ�б�
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
	 * <p>��������: queryDbTypes|����: ��ѯ�ֵ���ȡ���ݿ�����</p>
	 * @return ���ݿ������б�
	 */
	public List queryDbTypes(){
		String query = "select new Map(dic.dicName as dbEname, dic.description as dbCname) "
				+ "from UBaseDictionaryDO as dic where dic.dicType='DBT'";
		return this.find(query);
	}

	/**
	 * <p>��������: queryAllUsers|����: ��ѯ���з�ָ��ϵͳ����Ա���û�</p>
	 * @param systemId ��ϵͳID
	 * @return �ǹ���Ա���û��б�
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
	 * <p>��������: queryAllUsersChange|����: ��ѯ���з�ָ��ϵͳ����Ա���û�</p>
	 * @param systemId ��ϵͳID
	 * @return �ǹ���Ա���û��б�
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
	 * <p>��������: queryAdmins|����: ��ѯָ����ϵͳ�Ĺ���Ա�б�</p>
	 * @param systemId ��ϵͳID
	 * @return �û�����(ֻ����userId��userCname)�б�
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
	 * <p>��������: queryAdminsChange|����: ��ѯָ����ϵͳ�Ĺ���Ա�б�</p>
	 * @param systemId ��ϵͳID
	 * @return �û�����(ֻ����userId��userCname)�б�
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
	 * <p>��������: saveAdmins|����: ������ϵͳ�Ĺ���Ա����</p>
	 * @param systemId ��ϵͳID
	 * @param systemAdmins ��ϵͳ����ԱID�б�
	 */
	public void saveAdmins(String systemId, List systemAdmins){
		// ���ԭ������Ա��Ϣ
		this.executeUpdate("delete UAuthSystemAdminDO where systemId='"
				+ systemId + "'");
	
		systemAdmins = systemAdmins != null ? systemAdmins : Collections.EMPTY_LIST;
		Iterator admins = systemAdmins.iterator();
		// ��������µĹ���Ա��Ϣ
		while(admins.hasNext()){
			String userId = (String) admins.next();
			UAuthSystemAdminDO admin = new UAuthSystemAdminDO();
			admin.setSystemId(systemId);
			admin.setUserId(userId);
			this.save(admin);
		}
	}

	/**
	 * <p>��������: saveAdmins|����: ������ϵͳ�Ĺ���Ա����</p>
	 * @param systemId ��ϵͳID
	 * @param systemAdmins ��ϵͳ����ԱID�б�
	 */
	public String saveAdminsChange(String systemId, List systemAdmins,LoginDO user,List oldSystemAdmins){
		// ���ԭ������Ա��Ϣ
		String sql ="delete "+SubSystemAdminAuditBase.SUB_BAK_TABLE+ " where system_id=?";
		jdbcDaoAccessor.update(sql, new Object[]{systemId});
		systemAdmins = systemAdmins != null ? systemAdmins : Collections.EMPTY_LIST;
		//�ҳ� ��ɾ��list���� addList ,deleteList
		List deleteList = new ArrayList(oldSystemAdmins);
		deleteList.removeAll(systemAdmins);
		List addList = new ArrayList(systemAdmins);
		addList.removeAll(oldSystemAdmins);
		if(addList.size()==0&&deleteList.size()==0) return "û���κα仯";
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
		return "����ɹ�,�����ͨ������Ч!";
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
			//Ĭ���ǹ���Ա���
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
	 * <p>��������: getAllUBaseConfig|����: ��ȡ������ϵͳ��Ϣ</p>
	 * @return ��ϵͳ��Ϣ�б�
	 */
	public List getAllUBaseConfig(){
		return find("from UBaseConfigDO ");
	}

	/**
	 * <p>��������: getSubMenuList|����: ��ȡ��ϵͳ�Ĳ˵���Ϣ</p>
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
	 * <p>��������: getAllUBaseConfig|����: ��ȡ������ϵͳ��Ϣ</p>
	 * @return CRMS��ϵͳ��Ϣ�б�
	 */
	public List getCrmsUBaseConfig(){
		return find("from UBaseConfigDO where dbUrl like '%crms%' and enabled ='true'");
	}
}
