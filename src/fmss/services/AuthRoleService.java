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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����10:11:10
 * @����: [AuthRoleService]��ɫ��Ȩ�޵ķ�����
 */
public class AuthRoleService extends CommonService{

	private static final String SYSTEM_ID = "00003";

	/** �û�service */
	private UserService userService;
	/** ����service */
	private InstService instService;
	/** id������ */
	private static IdGenerator  idGenerator; // id������
	
	private CacheManager cacheManager; // ����

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * <p>��������: save|����:�����ɫ </p>
	 * @param role ��ɫ����
	 */
	public void save(final UAuthRoleDO role){
		getBem().save(role);
	};

	/**
	 * <p>��������: update|����:���½�ɫ </p>
	 * @param role ��ɫ����
	 */
	public void update(final UAuthRoleDO role){
		getBem().update(role);
	};

	/**
	 * <p>��������: delete|����:ɾ����ɫ </p>
	 * @param role ��ɫ����
	 */
	public void delete(final UAuthRoleDO role){
		getBem().delete(role);
	};

	/* ���� Javadoc��
	* <p>��д����: deleteById|����:����idɾ������ </p>
	* @param cl ����
	* @param id ����id
	* @see fmss.services.CommonService#deleteById(java.lang.Class, java.io.Serializable)
	*/
	public void deleteById(Class cl, Serializable id){
		getBem().deleteById(cl, id);
	};

	/* ���� Javadoc��
	* <p>��д����: get|����:����id��ȡ���� </p>
	* @param entity ����
	* @param id ����id
	* @return ��ȡ�Ķ���
	* @see fmss.services.CommonService#get(java.lang.Class, java.io.Serializable)
	*/
	public Object get(final Class entity, final Serializable id){
		return getBem().get(entity, id);
	};

	/**
	 * <p>��������: getAllObject|����:��ȡ���ж��� </p>
	 * @param cl ����
	 * @return �����б�
	 */
	public List getAllObject(Class cl){
		return getBem().getAllObject(cl);
	};

	/* ���� Javadoc��
	* <p>��д����: getAllByDetachedCriteria|����:����������ȡ���� </p>
	* @param detachedCriteria ��ѯ����
	* @return ��ѯ�������
	* @see fmss.services.CommonService#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		
		return getBem().getAllByDetachedCriteria(detachedCriteria);
	};

	/* ���� Javadoc��
	* <p>��д����: getCountByCriteria|����:����������ȡ��¼���� </p>
	* @param detachedCriteria ��ѯ����
	* @return ��¼����
	* @see fmss.services.CommonService#getCountByCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public int getCountByCriteria(final DetachedCriteria detachedCriteria){
		return getBem().getCountByCriteria(detachedCriteria);
	};

	/* ���� Javadoc��
	* <p>��д����: getByFormWithPaging|����:���ݲ�ѯ��ȡpage���� </p>
	* @param detachedCriteria ��ѯ����
	* @param pageSize ҳ��size
	* @param pageNum ҳ��
	* @return ��ҳ����
	* @see fmss.services.CommonService#getByFormWithPaging(org.hibernate.criterion.DetachedCriteria, int, int)
	*/
	public PageBox getByFormWithPaging(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNum){
		return getBem()
				.getByFormWithPaging(detachedCriteria, pageSize, pageNum);
	};

	/**
	 * <p>��������: getAuthRoleResourceByObjectId|����: ����Ȩ������ID,�������е�ǰ������Դ��Ϣ</p>
	 * @param objectId Ȩ������Id
	 * @param resType ��Դ����
	 * @return Ȩ�������������Դ
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
	 * <p>��������: getBaseConfigBySystemId|����:����ϵͳ��ţ�������ϵͳ��Ϣ </p>
	 * @param systemId ��ϵͳid
	 * @return ��ϵͳ������Ϣ
	 */
	public UBaseConfigDO getBaseConfigBySystemId(String systemId){
		DetachedCriteria dc = DetachedCriteria.forClass(UBaseConfigDO.class);
		dc.add(Property.forName("systemId").eq(systemId));
		List ubcs = getBem().getAllByDetachedCriteria(dc);
		UBaseConfigDO ubc = (UBaseConfigDO) ubcs.get(0);
		return ubc;
	}

	/**
	 * <p>��������: getRoleById|����:����roleID,���ص�ǰrole��Ϣ </p>
	 * @param roleId ��ɫId
	 * @return ��ɫ�б�
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
	 * <p>��������: getAuthRoleResourceByObjectId|����:����Ȩ������ID,�������е�ǰ������Դ���ձ� </p>
	 * @param objectId ����id
	 * @return ��ǰ������Դ�б�
	 */
	public List getAuthRoleResourceByObjectId(String objectId){
		DetachedCriteria dc = DetachedCriteria
				.forClass(UAuthRoleResourceDO.class);
		dc.add(Property.forName("objectId").eq(objectId));
		List roleResources = getBem().getAllByDetachedCriteria(dc);
		
		return roleResources;
	}

	/**
	 * <p>��������: getAllRole|����:���н�ɫ��Ȩ����Դ </p>
	 * @return ���н�ɫ����Դ�б�
	 */
	public List getAllRole(){
		List roleList = getBem().getAllObject(UAuthRoleDO.class);
		for(Iterator iterator = roleList.iterator(); iterator.hasNext();){
			UAuthRoleDO role = (UAuthRoleDO) iterator.next();
			// ȡÿ����ɫ��Ӧ��Ȩ����Դ
			List authRoleRes = this.getAuthRoleResourceByObjectId(role
					.getRoleId());
			role.setAuthRoleRes(authRoleRes);
		}
		return roleList;
	}

	/**
	 * <p>��������: getUserByRole|����:���ݽ�ɫ��ȡ�����û� </p>
	 * @param role ��ɫ����
	 * @return ��ɫ��Ӧ�û��б�
	 */
	public List getUserByRole(UAuthRoleDO role){
		return this.userService.getUsersByRole(role.getRoleId());
	}

	/**
	 * <p>��������: getUserByRole|����:���ݽ�ɫ��ȡ�����û� </p>
	 * @param role ��ɫ����
	 * @param paginationList ��ҳ����
	 * @param instId ����ID
	 * @param fixQuery 
	 * @param string 
	 * @param string2 
	 * @return ��ɫ��Ӧ�û��б�
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
	 * <p>��������: getUserByRole|����:���ݽ�ɫ��ȡ�ý�ɫû�е��û� </p>
	 * @param role ��ɫ����
	 * @param instId ����Id
	 * @return �ý�ɫû�е��û��б�
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
	 * �Լ�������Ե���
	 * */
	public List getHavLevUserByRole(UBaseUserDO user, UAuthRoleDO role,PaginationList paginationList){
		String query = "select ub from UBaseUserDO ub"
				+ " where ub.userId not in ( select uarr.userId from UAuthRoleUserDO uarr "
				+ " where uarr.roleId=?)";
		Map map = new LinkedMap();
		// ���������ѯ��������в�ѯ�����򽫲�ѯ�����Ͷ�Ӧ��ֵ����map��
		/*if(StringUtils.isNotEmpty(user.getInstName())){
			String condition = " and ub.instName like ?";
			map.put(condition, "%" + user.getInstName() + "%");
		}*/
		
		// ���Ӱ����Ʋ�ѯ
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
		// ����map������ѯ����������ԭ��hql֮��,ÿ��������Ӧ��ֵ����list
		for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
			String key = (String) iterator.next();
			query += key;
			valueList.add(map.get(key));
		}
		if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC))){
			query += " and (ub.isDelete!='1' or ub.isDelete is null) ";
		}
		query += " order by ub.userId";
		// ���ݲ�ѯ�����Ͷ�Ӧ���������ݲ�ѯ���
		return getBem().find(query, valueList,paginationList);
 
	}

	/**
	 * <p>��������: getUserByUserId|����:�����û�id��ȡ�û� </p>
	 * @param userId �û�id
	 * @return �û�����
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return this.userService.getUserByUserId(userId);
	}

	/**
	 * <p>��������: getRoleByRoleId|����: ���ݽ�ɫid��ȡ��ɫ</p>
	 * @param roleId ��ɫid
	 * @return ��ɫ����
	 */
	public UAuthRoleDO getRoleByRoleId(String rolroleIdeId){
		return (UAuthRoleDO) this.getBem().get(UAuthRoleDO.class, rolroleIdeId);
	}

	/**
	 * <p>��������: getResByRole|����:���ݽ�ɫ��ȡ��Դ </p>
	 * @param role ��ɫ����
	 * @return ��ɫ��Ӧ����Դ���б�
	 */
	public List getResByRole(UAuthRoleDO role){
		// ����Ȩ������id��ѯ
		DetachedCriteria dc = DetachedCriteria
				.forClass(UAuthRoleResourceDO.class);
		dc.add(Property.forName("objectId").eq(role.getRoleId()));
		return getBem().getAllByDetachedCriteria(dc);
	}

	/**
	 * <p>��������: getHavUserByRole|����: ���û����������������û���ѯ</p>
	 * @param user �û�����
	 * @param role ��ɫ����
	 * @param instId ����Id
	 * @return ��ɫ��Ӧ���û��б�
	 */
	public List getHavUserByRole(UBaseUserDO user, UAuthRoleDO role,String instId){
		String query = "select ub from UBaseUserDO ub left join ub.ubaseInst ubi"
				+ " where ub.userId in ( select uarr.userId from UAuthRoleUserDO uarr  where uarr.roleId=?)"
				+ " and exists (select 1 from UBaseInstDO a where a.instId=? and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))";
		
		Map map = new LinkedHashMap();
		// ���������ѯ��������в�ѯ�����򽫲�ѯ�����Ͷ�Ӧ��ֵ����map��
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
		// ���Ӱ����Ʋ�ѯ
		if(StringUtils.isNotEmpty(user.getInstName())){
			String condition = " and ub.instId in ( select ui.instId from UBaseInstDO ui where ui.instName like ? ) ";
			map.put(condition, "%" + user.getInstName() + "%");
		}
		// ���Ӱ����Ʋ�ѯ
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
		// ����map������ѯ����������ԭ��hql֮��,ÿ��������Ӧ��ֵ����list
		for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
			String key = (String) iterator.next();
			query += key;
			valueList.add(map.get(key));
		}
		query += "order by ub.instId,ub.userCname";
		// ���ݲ�ѯ�����Ͷ�Ӧ���������ݲ�ѯ���
		return getBem().find(query, valueList.toArray(new Object[] {}));
	}

	/**
	 * <p>��������: deleteConfigUser|����:ɾ����ɫ��Ӧ�Ľ�ɫ-�û���ϵ </p>
	 * @param role ��ɫ����
	 */
	public void deleteConfigUser(UAuthRoleDO role){
		// ȡ��ɫ��Ӧ���û����б�
		List users = this.getUserByRole(role);
		List roleUsers = new ArrayList();
		// �γɽ�ɫ-�û���Ӧ��ϵ��Ȼ��ɾ����ɫ-�û���ϵ��
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
	 * <p>��������: deleteConfigUser|����: ɾ����ɫ��Ӧ�Ľ�ɫ-�û���ϵ</p>
	 * @param userList �û��б�
	 * @param role ��ɫ����
	 */
	public void deleteConfigUser(List userList, UAuthRoleDO role){
		List roleUsers = new ArrayList();
		// �γɽ�ɫ-�û���Ӧ��ϵ��Ȼ��ɾ����ɫ-�û���ϵ��
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
	 * <p>��������: saveConfigUser|����:�����û�-��ɫ��ϵ���� </p>
	 * @param userList �û��б�
	 * @param role ��ɫ����
	 */
	public void saveConfigUser(List userList, UAuthRoleDO role){
		if(CollectionUtils.isNotEmpty(userList)){
			// �γɽ�ɫ-�û���Ӧ��ϵ��Ȼ�󱣴��ɫ-�û���ϵ��
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
	 * <p>��������: deleteConfigRes|����:ɾ����ɫ��Ӧ����Դ-��ɫ��ϵ </p>
	 * @param role ��ɫ����
	 */
	public void deleteConfigRes(UAuthRoleDO role){
		// ��ȡ��ɫ��Ӧ����Դ
		List res = this.getResByRole(role);
		List roleUser = new ArrayList();
		// �γɽ�ɫ-��Դ��Ӧ��ϵ��Ȼ��ɾ����ɫ-��Դ��ϵ��
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
	 * <p>��������: getAuthResMap|����:������Դid��ȡ��Դ </p>
	 * @param resId ��Դid
	 * @return ��Դ��Ϣ
	 */
	public UAuthResMapDO getAuthResMap(String resId){
		UAuthResMapDO authResMap = (UAuthResMapDO) this.get(
				UAuthResMapDO.class, resId);
		return authResMap;
	}

	/**
	 * <p>��������: getSystemList|����:��ȡ��ϵͳ�б� </p>
	 * @return ��ϵͳ�б�
	 */
	//modified by fwy ���Ӷ���ϵͳ�Ƿ����õ��ж�
	public List getSystemList(boolean isAll){
		return this.find("select ubc from UBaseConfigDO ubc"
				+ (isAll ? "" : " where ubc.systemId<>'00003' and ubc.enabled!='false'")
				+ " order by ubc.systemId");
	}

	/**
	 * <p>��������: getRoleBySysId|����:������ϵͳid��ȡ��ϵͳ�½�ɫ </p>
	 * @param systemId ��ϵͳid
	 * @return ��ϵͳ�µĽ�ɫ
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

	/* ���� Javadoc��
	* <p>��д����: deleteAll|����:ɾ�����󼯺� </p>
	* @param entities ���󼯺�
	* @see fmss.services.CommonService#deleteAll(java.util.Collection)
	*/
	public void deleteAll(Collection entities){
		getBem().deleteAll(entities);
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:����hql��ѯ </p>
	* @param queryString ��ѯhql
	* @return ��ѯ���󼯺�
	* @see fmss.services.CommonService#find(java.lang.String)
	*/
	public List find(String queryString){
		return getBem().find(queryString);
	}

	/* ���� Javadoc��
	* <p>��д����: delete|����:ɾ������ </p>
	* @param object ����
	* @see fmss.services.CommonService#delete(java.lang.Object)
	*/
	public void delete(Object object){
		getBem().delete(object);
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:���ݲ���ʹ��hql��ѯ </p>
	* @param query  ��ѯhql
	* @param parameter �����б�
	* @return ��ѯ���󼯺�
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object)
	*/
	public List find(String query, Object parameter){
		return getBem().find(query, parameter);
	}

	/**
	 * <p>��������: getRoleTreeAsynXml|����:�첽��ȡxml�� </p>
	 * @param sysList ��ϵͳ�б�
	 * @param id ���ڵ�id
	 * @return xml���ַ���
	 */
	public String getRoleTreeAsynXml(List sysList, String id){
		StringBuffer sb = new StringBuffer();
		List roleList;
		// �ж���ϵͳid�Ƿ�Ϊ��
		if(StringUtils.isNotBlank(id)){
			// ȡ��ϵͳ�¶�Ӧ�Ľ�ɫ�б��γ�xml
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
			// ����ϵͳ��Ϣ��Ϊ���ڵ��γ�xml
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
	 * <p>��������: getRoleTreeSyncXml|����:ͬ����ȡxml�� </p>
	 * @param sysList ��ϵͳ�б�
	 * @param id ���ڵ�id
	 * @return xml���ַ���
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
			// ȡÿ����ϵͳ��Ӧ�Ľ�ɫ�б�
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
	 * <p>��������: getRoleTreeSyncXmlEx|����:ͬ����ȡxml��,���¿ؼ� </p>
	 * @param sysList ��ϵͳ�б�
	 * @param id ���ڵ�id
	 * @param userId �û�Id
	 * @return xml���ַ���
	 */
	public String getRoleTreeSyncXmlEx(List sysList, String id, String userId){
		StringBuffer sb = new StringBuffer();
		List roleList;
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<Response><Data><Tree>");
		for(Iterator iterator = sysList.iterator(); iterator.hasNext();){
			UBaseConfigDO cfg = (UBaseConfigDO) iterator.next();
			String sysId = cfg.getSystemId();
			// �����ϵͳ�����򲻼���
			if(SYSTEM_ID.equals(sysId))
				continue;
			sb.append("<TreeNode name='");
			sb.append(cfg.getSystemCname());
			sb.append("' id='");
			sb.append(sysId);
//			 sb.append("' child='1");
			sb.append("' levelType='1' ");
			// ȡÿ����ϵͳ��Ӧ�Ľ�ɫ�б�
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
	 * <p>��������: getRoleTreeSyncXmlEx|����:ͬ����ȡxml��,���¿ؼ� </p>
	 * @param sysList ��ϵͳ�б�
	 * @param id ���ڵ�id
	 * @param userId �û�Id
	 * @return xml���ַ���
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
			// �����ϵͳ�����򲻼���
			if(SYSTEM_ID.equals(sysId))
				continue;
			Map itemMap = new HashMap();
			itemMap.put("id", m);
			itemMap.put("pId", 0);
			itemMap.put("name", cfg.getSystemCname());
			itemMap.put("pIds", sysId);
			itemMap.put("isParent", true);
			ztreeList.add(itemMap);
			// ȡÿ����ϵͳ��Ӧ�Ľ�ɫ�б�
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
	 * <p>��������: getUser|����:��ȡ��һ�����û� </p>
	 * @param user �û���Ϣ
	 * @return �û���ϸ��Ϣ
	 */
	public UBaseUserDO getUser(UBaseUserDO user){
		return this.userService.getUserByUserId(user.getUserId());
	}

	/**
	 * <p>��������: getBaseConfig|����:��ȡ�û���Ӧ�������ϵͳ </p>
	 * @param user �û���Ϣ
	 * @return �û�����ϵͳ�б�
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
	 * <p>��������: getBaseConfig|����:��ȡ�û���������</p>
	 * @param userId �û�Id
	 * @return 1:usys����Ա,2:����ϵͳ����Ա,3:��ͨ�û�
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
	 * <p>��������: getRoleByUser|����:��ȡ�û���Ӧ��ɫ </p>
	 * @param user �û���Ϣ
	 * @return �û���Ӧ�Ľ�ɫ
	 */
	public List getRoleByUser(UBaseUserDO user){
		return this.userService.getAuthRoleByUserId(user.getUserId());
	}
	
	//�޶���ϵͳ��Χ
	public List getRoleByUserRangeBySystem(UBaseUserDO user, LoginDO login) {
		String hql = "from UAuthRoleUserDO where userId=?" + AuditBase.appendHaveSystemAuthsRangeHQL(login);
		List l = find(hql, user.getUserId());
		return l;
	}

	/**
	 * <p>��������: getUserByInst|����:��ȡĳһ�����������û� </p>
	 * @param inst ������Ϣ
	 * @return �����µ��û�
	 */
	public List getUserByInst(UBaseInstDO inst){
		return getBem().find("from UBaseUserDO u where u.instId=?", inst
				.getInstId());
	}

	/**
	 * <p>��������: getUserTreeSyncXml|����:ͬ����ȡ�û�xmltree </p>
	 * @return xml�����ַ���
	 */
	public String getUserTreeSyncXml(){
		StringBuffer sb = new StringBuffer();
		List userList;
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<tree id='0'>");
		// ȡȫ������
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
			// ȡ�����������û�
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
	 * <p>��������: getUserTreeAsynXml|����:�첽��ȡ�����û��� </p>
	 * @param instId ����id
	 * @return xml�����ַ���
	 */
	public String getUserTreeAsynXml(String instId){
		StringBuffer sb = new StringBuffer();
		UBaseInstDO inst = new UBaseInstDO();
		// �������id��Ϊ�գ���ȡ�û���id���¼�����
		if(StringUtils.isNotBlank(instId)){
			// ȡ�¼�����
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
			// ȡ������������ĸ��ڵ�
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<tree id='0'>");
			List listInst = this.instService.getAllInsts();
			for(int i = 0; i < listInst.size(); i++){
				inst = (UBaseInstDO) listInst.get(i);
				// �ж��ǲ��Ƕ������
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
	 * @return �û��������
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * @param userService Ҫ���õ� �û��������
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * @return �����������
	 */
	public InstService getInstService(){
		return instService;
	}

	/**
	 * @param instService Ҫ���õ� �����������
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
