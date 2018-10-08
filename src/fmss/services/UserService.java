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
 * <p> 版权所有:(C)2003-2010  </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 上午10:20:33
 * @描述: 用户操作服务
 */
public class UserService extends CommonService{

	/**
	 * <p> 方法名称: getUserByUserId|描述:根据用户ID,返回当前用户信息 </p>
	 * @param userId 用户编号
	 * @return 根据用户ID,返回当前用户信息
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return (UBaseUserDO) this.get(UBaseUserDO.class, userId);
	}

	/**
	 * <p> 方法名称: getUser|描述:根据用户ID,返回当前用户信息 </p>
	 * @param userId 用户编号
	 * @return 根据用户ID,返回当前用户信息
	 */
	public UBaseUserDO getUser(String userId){
		List ubus = find("from UBaseUserDO ubu where ubu.userId = ?", userId);
		UBaseUserDO user = null;
		if(CollectionUtils.isNotEmpty(ubus))
			user = (UBaseUserDO) ubus.get(0);
		return user;
	}

	/**
	 * <p> 方法名称: getAllUsers|描述:返回所有用户信息 </p>
	 * @return 所有用户列表
	 */
	public List getAllUsers(){
		return find("from UBaseUserDO");
	}

	/**
	 * <p> 方法名称: getUsersByCondition|描述:通过查询条件取得用户列表 </p>
	 * @param user 用户对象
	 * @param inst 机构对象
	 * @param login
	 * @return 形成的查询语句
	 */
	public Object[] getUsersByCondition(UBaseUserDO user, UBaseInstDO inst,
			String instId){
		// 获取的字段
		StringBuffer sb = new StringBuffer();
		ArrayList params = new ArrayList();
		sb.append("select ");
		sb.append("ubu.userId as userId, ubu.isUserLocked as isUserLocked, ");
		sb.append("ubu.userEname as userEname, ");
		sb.append("ubu.userCname as userCname, ubi.instId as instId, ");
		sb
				.append("ubi.instSmpName as instSmpName,ubu.isUserLocked as isUserLocked,ubu.userLockedReson as userLockedReson,ubu.isDelete as isDelete,ubu.lastLoginDate as lastLoginDate ,ubu.enabled as enabled ,ubu.isList from UBaseUserDO ubu ");
		sb.append("left join ubu.ubaseInst ubi where 1 = 1 ");
		// 用户机构号条件判断
		if(StringUtils.isNotEmpty(user.getInstId())){
			sb.append(" and ubu.instId like ? ");
			params.add("%" + user.getInstId() + "%");
		}
		// 用户机构名称条件判断
		if(StringUtils.isNotEmpty(inst.getInstSmpName())){
			sb.append(" and ubi.instSmpName like ? ");
			params.add("%" + inst.getInstSmpName() + "%");
		}
		// 用户登陆名条件判断
		if(StringUtils.isNotEmpty(user.getUserEname())){
			sb.append(" and ubu.userEname like ? ");
			params.add("%" + user.getUserEname() + "%");
		}
		// 用户中文条件判断
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
	 * <p> 方法名称: saveUserByUserId|描述: 根据用户ID,保存修改用户信息 </p>
	 * @param user 用户对象
	 */
	public void saveUserByUserId(UBaseUserDO user){
		save(user);
	}

	/**
	 * <p> 方法名称: saveUserByUserId|描述: 根据用户ID,保存修改用户信息 </p>
	 * @param user 用户对象
	 */
	public void updateUserByUserId(UBaseUserDO user){
		update(user);
	}

	/**
	 * <p> 方法名称: deleteUser|描述: 删除用户 </p>
	 * @param userList 用户列表
	 */
	public String deleteUser(String[] userList, String userCurrentId){
		String returnMsg = "";
		if(null != userList && userList.length > 0){
			List removeObj = new ArrayList();
			String userId = "";
			// 拼接选中的用户编号
			for(int index = 0; index < userList.length; index++){
				// 判断当前用户是否删除自己
				if(userCurrentId.equals(userList[index].toString())){
					returnMsg = "不能删除自己账号";
					break;
				}
				UBaseUserDO ubu = new UBaseUserDO();
				ubu.setUserId(userList[index]);
				removeObj.add(ubu);
				userId += "'" + userList[index] + "',";
			}
			// 删除用户, 先删除与用户相关的数据(用户历史密码表、系统管理员管理员表、角色用户表、系统日志表、用户表、权限主体表)
			if(returnMsg.equals("") && !StringUtils.isEmpty(userId)){
				userId = userId.substring(0, userId.length() - 1);
				String logicDelete = cacheManager
						.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
				if(!"1".equals(logicDelete)){
					// 清除用户历史密码表
					String sqlDelete = "delete from UBaseHisUserPwdDO hup where hup.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// 清除系统管理员管理员表
					sqlDelete = "delete from UAuthSystemAdminDO sad where sad.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// 角色用户表
					sqlDelete = "delete from UAuthRoleUserDO rud where rud.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// 系统日志
					sqlDelete = "delete from UBaseSysLogDO sld where sld.userId in ("
							+ userId + ")";
					// this.executeUpdate(sqlDelete);
					// 用户表
					sqlDelete = "delete from UBaseUserDO ud where ud.userId in ("
							+ userId + ")";
					this.executeUpdate(sqlDelete);
					// 权限主体表
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
	 * <p> 方法名称: recoverUser|描述: 恢复逻辑删除用户 </p>
	 * @param userList 用户列表
	 */
	public String recoverUser(String[] userList, String userCurrentId){
		String returnMsg = "";
		if(null != userList && userList.length > 0){
			List removeObj = new ArrayList();
			String userId = "";
			// 拼接选中的用户编号
			for(int index = 0; index < userList.length; index++){
				UBaseUserDO ubu = new UBaseUserDO();
				ubu.setUserId(userList[index]);
				removeObj.add(ubu);
				userId += "'" + userList[index] + "',";
			}
			if(returnMsg.equals("") && !StringUtils.isEmpty(userId)){
				userId = userId.substring(0, userId.length() - 1);
				// 将isDelete标识去掉 即可恢复用户
				String sqlDelete = "update UBaseUserDO ud set ud.isDelete='' where ud.userId in ("
						+ userId + ")";
				this.executeUpdate(sqlDelete);
			}
		}
		return returnMsg;
	}

	/**
	 * <p> 方法名称: getAuthRoleByUserId|描述:根据用户ID，返回当前用户的所有角色信息 </p>
	 * @param userId 用户的编号
	 * @return 返回当前用户的所有角色信息
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
	 * <p> 方法名称: getBaseConfigByObjectId|描述:根据权限主体ID，返回当前用户的所有子系统信息 </p>
	 * @param objectId 权限主体编号
	 * @return 所有子系统信息列表
	 */
	public List getBaseConfigByObjectId(String objectId){
		List configs = find(
				"select ubc from UBaseConfigDO ubc,UAuthRoleResourceDO uarr,UAuthResMapDO uarm "
						+ "where  uarr.objectId=? and uarr.resId=uarm.resId and uarm.systemId=ubc.systemId ",
				objectId);
		return configs;
	}

	/**
	 * <p> 方法名称: getBaseConfigByUserId|描述:根据用户ID，返回当前用户的所有子系统信息 </p>
	 * @param userId 用户编号
	 * @return 该用户所属子系统信息列表
	 */
	public List getBaseConfigByUserId(String userId){
		return getBaseConfigByObjectId(userId);
	}

	/**
	 * <p> 方法名称: getBaseConfigByEName|描述:根据用户登录名，返回当前用户的所有子系统信息 </p>
	 * @param userEName 用户登录名
	 * @return 该用户所属子系统信息列表
	 */
	public List getUserInfoByEName(String userEName){
		List configs = find("from UBaseUserDO ubc where ubc.userEname=?",
				userEName);
		return configs;
	}

	/**
	 * <p> 方法名称: getBaseInstByUser|描述:根据用户对象，返回当前用户的机构信息 </p>
	 * @param user 用户对象
	 * @return
	 */
	public UBaseInstDO getBaseInstByUser(UBaseUserDO user){
		return (UBaseInstDO) this.get(UBaseInstDO.class, user.getInstId());
	}

	/**
	 * <p> 方法名称: getBaseConfigBySystemId|描述:根据系统编号，返回子系统信息 </p>
	 * @param systemId 系统编号
	 * @return 系统信息
	 */
	public UBaseConfigDO getBaseConfigBySystemId(String systemId){
		List ubcs = find("from UBaseConfigDO ubc where ubc.systemId = ?",
				systemId);
		UBaseConfigDO ubc = (UBaseConfigDO) ubcs.get(0);
		return ubc;
	}

	/**
	 * <p> 方法名称: getBaseHisUserPwd|描述:根据用户ID，返回当前用户历史密码信息 </p>
	 * @param userId 用户编号
	 * @return
	 */
	public List getBaseHisUserPwd(String userId){
		return find(
				"from UBaseHisUserPwdDO ubhuw where ubhuw.userId=? order by ubhuw.modifyTime desc",
				userId);
	}

	/**
	 * <p> 方法名称: getBaseSysLog|描述:根据用户ID，返回当前用户登录日志信息 </p>
	 * @param userId 用户编号
	 * @return
	 */
	public List getBaseSysLog(String userId){
		return find("from UBaseSysLogDO ubsl where ubsl.userId = ?", userId);
	}

	/**
	 * <p> 方法名称: getAllBaseConfigBySubAdmin|描述: 根据子系统管理员账号，返回其拥有的所有子系统信息 </p>
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
	 * <p> 方法名称: getUsersByRole|描述: 根据角色，返回当前角色下的所有用户数据 </p>
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
	 * <p> 方法名称: updateUserCName|描述: 修改用户名称时，关联更新有用户名称冗余字段的表 </p>
	 * @param user
	 * @return
	 */
	public void updateUserCName(UBaseUserDO user){
		try{
			// 系统日志
			this.executeUpdate("update UBaseSysLogDO set  userCname =  '"
					+ user.getUserCname() + "' where userId = '"
					+ user.getUserId() + "'");
			// 系统公告日志
			this.executeUpdate("update UBaseNoticeLogDO set  userCName =  '"
					+ user.getUserCname() + "' where userEName = '"
					+ user.getUserId() + "'");
			// 系统公告回执
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
	 * 根据当前用户机构获取所有下级机构的用户
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
