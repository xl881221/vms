package fmss.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;

/**
 * <p> ��Ȩ����:(C)2003-2010  </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����10:20:33
 * @����: �û���������
 */
public class UserService extends CommonService{

	/**
	 * <p> ��������: getUserByUserId|����:�����û�ID,���ص�ǰ�û���Ϣ </p>
	 * @param userId �û����
	 * @return �����û�ID,���ص�ǰ�û���Ϣ
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return (UBaseUserDO) this.get(UBaseUserDO.class, userId);
	}

	/**
	 * <p> ��������: getUser|����:�����û�ID,���ص�ǰ�û���Ϣ </p>
	 * @param userId �û����
	 * @return �����û�ID,���ص�ǰ�û���Ϣ
	 */
	public UBaseUserDO getUser(String userId){
		List ubus = find("from UBaseUserDO ubu where ubu.userId = ?", userId);
		UBaseUserDO user = null;
		if(CollectionUtils.isNotEmpty(ubus))
			user = (UBaseUserDO) ubus.get(0);
		return user;
	}

	/**
	 * <p> ��������: getAllUsers|����:���������û���Ϣ </p>
	 * @return �����û��б�
	 */
	public List getAllUsers(){
		return find("from UBaseUserDO");
	}

	/**
	 * <p> ��������: getUsersByCondition|����:ͨ����ѯ����ȡ���û��б� </p>
	 * @param user �û�����
	 * @param inst ��������
	 * @param login
	 * @return �γɵĲ�ѯ���
	 */
	public Object[] getUsersByCondition(UBaseUserDO user, UBaseInstDO inst,
			String instId){
		// ��ȡ���ֶ�
		StringBuffer sb = new StringBuffer();
		ArrayList params = new ArrayList();
		sb.append("select ");
		sb.append("ubu.userId as userId, ubu.isUserLocked as isUserLocked, ");
		sb.append("ubu.userEname as userEname, ");
		sb.append("ubu.userCname as userCname, ubi.instId as instId, ");
		sb
				.append("ubi.instSmpName as instSmpName,ubu.isUserLocked as isUserLocked,ubu.userLockedReson as userLockedReson,ubu.isDelete as isDelete,ubu.lastLoginDate as lastLoginDate ,ubu.enabled as enabled ,ubu.isList from UBaseUserDO ubu ");
		sb.append("left join ubu.ubaseInst ubi where 1 = 1 ");
		// �û������������ж�
		if(StringUtils.isNotEmpty(user.getInstId())){
			sb.append(" and ubu.instId like ? ");
			params.add("%" + user.getInstId() + "%");
		}
		// �û��������������ж�
		if(StringUtils.isNotEmpty(inst.getInstSmpName())){
			sb.append(" and ubi.instSmpName like ? ");
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// �û���½�������ж�
		if(StringUtils.isNotEmpty(user.getUserEname())){
			sb.append(" and ubu.userEname like ? ");
			params.add("%" + user.getUserEname() + "%");
		}
		// �û����������ж�
		if(StringUtils.isNotEmpty(user.getUserCname())){
			sb.append(" and ubu.userCname like ? ");
			params.add("%" + user.getUserCname() + "%");
		}
		sb
				.append(" and exists (select 1 from UBaseInstDO a where a.instId='"
						+ instId
						+ "' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))");
		// sb.append(" and (ubu.isDelete!='1' or ubu.isDelete is null) ");
		sb.append(" order by ubu.instId,ubu.userCname");
		return new Object[] {sb.toString(), params};
	}

	/**
	 * <p> ��������: saveUserByUserId|����: �����û�ID,�����޸��û���Ϣ </p>
	 * @param user �û�����
	 */
	public void saveUserByUserId(UBaseUserDO user){
		save(user);
	}

	/**
	 * <p> ��������: saveUserByUserId|����: �����û�ID,�����޸��û���Ϣ </p>
	 * @param user �û�����
	 */
	public void updateUserByUserId(UBaseUserDO user){
		update(user);
	}

	/**
	 * <p> ��������: deleteUser|����: ɾ���û� </p>
	 * @param userList �û��б�
	 */
	public String deleteUser(String[] userList, String userCurrentId){
		String returnMsg = "";
		if(null != userList && userList.length > 0){
			List removeObj = new ArrayList();
			String userId = "";
			// ƴ��ѡ�е��û����
			for(int index = 0; index < userList.length; index++){
				// �жϵ�ǰ�û��Ƿ�ɾ���Լ�
				if(userCurrentId.equals(userList[index].toString())){
					returnMsg = "����ɾ���Լ��˺�";
					break;
				}
				UBaseUserDO ubu = new UBaseUserDO();
				ubu.setUserId(userList[index]);
				removeObj.add(ubu);
				userId += "'" + userList[index] + "',";
			}
			// ɾ���û�, ��ɾ�����û���ص�����(�û���ʷ�����ϵͳ����Ա����Ա����ɫ�û���ϵͳ��־���û���Ȩ�������)
			if(returnMsg.equals("") && !StringUtils.isEmpty(userId)){
				userId = userId.substring(0, userId.length() - 1);
				String logicDelete = cacheManager
						.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
				if(!"1".equals(logicDelete)){
					// ����û���ʷ�����
					String sqlDelete = "delete from UBaseHisUserPwdDO hup where hup.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// ���ϵͳ����Ա����Ա��
					sqlDelete = "delete from UAuthSystemAdminDO sad where sad.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// ��ɫ�û���
					sqlDelete = "delete from UAuthRoleUserDO rud where rud.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// ϵͳ��־
					sqlDelete = "delete from UBaseSysLogDO sld where sld.userId in ("
							+ userId + ")";
					// this.executeUpdate(sqlDelete);
					// �û���
					sqlDelete = "delete from UBaseUserDO ud where ud.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// Ȩ�������
					sqlDelete = "delete from UAuthObjectDO od where od.objectId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
				}else{
					String sqlDelete = "update UBaseUserDO ud set ud.isDelete='1' where ud.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
				}
			}
		}
		return returnMsg;
	}

	/**
	 * <p> ��������: recoverUser|����: �ָ��߼�ɾ���û� </p>
	 * @param userList �û��б�
	 */
	public String recoverUser(String[] userList, String userCurrentId){
		String returnMsg = "";
		if(null != userList && userList.length > 0){
			List removeObj = new ArrayList();
			String userId = "";
			// ƴ��ѡ�е��û����
			for(int index = 0; index < userList.length; index++){
				UBaseUserDO ubu = new UBaseUserDO();
				ubu.setUserId(userList[index]);
				removeObj.add(ubu);
				userId += "'" + userList[index] + "',";
			}
			if(returnMsg.equals("") && !StringUtils.isEmpty(userId)){
				userId = userId.substring(0, userId.length() - 1);
				// ��isDelete��ʶȥ�� ���ɻָ��û�
				String sqlDelete = "update UBaseUserDO ud set ud.isDelete='' where ud.userId in ("
						+ userId + ")";
				this.executeUpdate(sqlDelete);
			}
		}
		return returnMsg;
	}

	/**
	 * <p> ��������: getAuthRoleByUserId|����:�����û�ID�����ص�ǰ�û������н�ɫ��Ϣ </p>
	 * @param userId �û��ı��
	 * @return ���ص�ǰ�û������н�ɫ��Ϣ
	 */
	public List getAuthRoleByUserId(String userId){
		List urus = find(
				"select uaru from UAuthRoleUserDO uaru,UAuthRoleDO uar "
						+ "where uaru.userId=? and uaru.roleId=uar.roleId ",
				userId);
		return urus;
	}

	public List getRoleByUserId(String userId){
		List urus = find(
				"select uar from UAuthRoleUserDO uaru,UAuthRoleDO uar "
						+ "where uaru.userId=? and uaru.roleId=uar.roleId ",
				userId);
		return urus;
	}

	/**
	 * <p> ��������: getBaseConfigByObjectId|����:����Ȩ������ID�����ص�ǰ�û���������ϵͳ��Ϣ </p>
	 * @param objectId Ȩ��������
	 * @return ������ϵͳ��Ϣ�б�
	 */
	public List getBaseConfigByObjectId(String objectId){
		List configs = find(
				"select ubc from UBaseConfigDO ubc,UAuthRoleResourceDO uarr,UAuthResMapDO uarm "
						+ "where  uarr.objectId=? and uarr.resId=uarm.resId and uarm.systemId=ubc.systemId ",
				objectId);
		return configs;
	}

	/**
	 * <p> ��������: getBaseConfigByUserId|����:�����û�ID�����ص�ǰ�û���������ϵͳ��Ϣ </p>
	 * @param userId �û����
	 * @return ���û�������ϵͳ��Ϣ�б�
	 */
	public List getBaseConfigByUserId(String userId){
		return getBaseConfigByObjectId(userId);
	}

	/**
	 * <p> ��������: getBaseConfigByEName|����:�����û���¼�������ص�ǰ�û���������ϵͳ��Ϣ </p>
	 * @param userEName �û���¼��
	 * @return ���û�������ϵͳ��Ϣ�б�
	 */
	public List getUserInfoByEName(String userEName){
		List configs = find("from UBaseUserDO ubc where ubc.userEname=?",
				userEName);
		return configs;
	}

	/**
	 * <p> ��������: getBaseInstByUser|����:�����û����󣬷��ص�ǰ�û��Ļ�����Ϣ </p>
	 * @param user �û�����
	 * @return
	 */
	public UBaseInstDO getBaseInstByUser(UBaseUserDO user){
		return (UBaseInstDO) this.get(UBaseInstDO.class, user.getInstId());
	}

	/**
	 * <p> ��������: getBaseConfigBySystemId|����:����ϵͳ��ţ�������ϵͳ��Ϣ </p>
	 * @param systemId ϵͳ���
	 * @return ϵͳ��Ϣ
	 */
	public UBaseConfigDO getBaseConfigBySystemId(String systemId){
		List ubcs = find("from UBaseConfigDO ubc where ubc.systemId = ?",
				systemId);
		UBaseConfigDO ubc = (UBaseConfigDO) ubcs.get(0);
		return ubc;
	}

	/**
	 * <p> ��������: getBaseHisUserPwd|����:�����û�ID�����ص�ǰ�û���ʷ������Ϣ </p>
	 * @param userId �û����
	 * @return
	 */
	public List getBaseHisUserPwd(String userId){
		return find(
				"from UBaseHisUserPwdDO ubhuw where ubhuw.userId=? order by ubhuw.modifyTime desc",
				userId);
	}

	/**
	 * <p> ��������: getBaseSysLog|����:�����û�ID�����ص�ǰ�û���¼��־��Ϣ </p>
	 * @param userId �û����
	 * @return
	 */
	public List getBaseSysLog(String userId){
		return find("from UBaseSysLogDO ubsl where ubsl.userId = ?", userId);
	}

	/**
	 * <p> ��������: getAllBaseConfigBySubAdmin|����: ������ϵͳ����Ա�˺ţ�������ӵ�е�������ϵͳ��Ϣ </p>
	 * @param adminId
	 * @return
	 */
	public List getAllBaseConfigBySubAdmin(String adminId){
		List configs = find(
				"select ubc from UBaseConfigDO ubc,UAuthSystemAdminDO uasa "
						+ "where uasa.userId = ? and ubc.systemId=uasa.systemId",
				adminId);
		return configs;
	}

	/**
	 * <p> ��������: getUsersByRole|����: ���ݽ�ɫ�����ص�ǰ��ɫ�µ������û����� </p>
	 * @param roleId
	 * @return
	 */
	public List getUsersByRole(String roleId){
		List users = find(
				"select ubu from UBaseUserDO ubu,UAuthRoleUserDO uaru "
						+ "where uaru.roleId=? and uaru.userId=ubu.userId",
				roleId);
		return users;
	}

	/**
	 * <p> ��������: updateUserCName|����: �޸��û�����ʱ�������������û����������ֶεı� </p>
	 * @param user
	 * @return
	 */
	public void updateUserCName(UBaseUserDO user){
		try{
			// ϵͳ��־
			this.executeUpdate("update UBaseSysLogDO set  userCname =  '"
					+ user.getUserCname() + "' where userId = '"
					+ user.getUserId() + "'");
			// ϵͳ������־
			this.executeUpdate("update UBaseNoticeLogDO set  userCName =  '"
					+ user.getUserCname() + "' where userEName = '"
					+ user.getUserId() + "'");
			// ϵͳ�����ִ
			this
					.executeUpdate("update UBaseNoticeFeedBackDO set  userCName =  '"
							+ user.getUserCname()
							+ "' where userId = '"
							+ user.getUserId() + "'");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * ���ݵ�ǰ�û�������ȡ�����¼��������û�
	 * @param userId
	 * @return
	 */
	public List getJuniorUserByInstId(String userId){
		List list = new ArrayList();
		List instList = new ArrayList();
		UBaseUserDO user = getUser(userId);
		instService.getAllChildInst(user.getInstId(), instList);
		for(Iterator iterator = instList.iterator(); iterator.hasNext();){
			UBaseInstDO o = (UBaseInstDO) iterator.next();
			List userIds = find(
					"select u.userId from UBaseUserDO u where u.instId=?",
					new Object[] {o.getInstId()});
			if(CollectionUtils.isNotEmpty(userIds))
				list.addAll(userIds);
		}
		return list;
	}

	public List getAdminUserList(String uid, String systemId){
		String hql = "from UAuthSystemAdminDO o where o.userId=? and o.systemId=?";
		return find(hql, new Object[] {uid, systemId});
	}

	private InstService instService;
	private CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	public void setInstService(InstService instService){
		this.instService = instService;
	}

	public boolean isSuperAdministrator(String userId){
		List list = getAdminUserList(userId, Constants.SYSTEM_COMMON_ID);
		return CollectionUtils.isNotEmpty(list);
	}
}
