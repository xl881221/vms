package fmss.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;
import fmss.common.util.HexUtils;
import fmss.common.util.LoginUtil;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: xindengquan
 * @日期: 2009-6-27 上午11:48:57
 * @描述: [LoginService]用户登录系统信息的验证，对内存中用户信息的管理
 */
public class LoginService{

	private static final Log log = LogFactory.getLog(DwrAsynService.class);
	/** 用于存放用户登录信息 */
	private final Map logingInfoMap = new HashMap();
	/** 用户service */
	private UserService userService;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private CacheManager cacheManager;

	/**
	 * <p>方法名称: checkUser|描述: hibernate检验数据库中是否存在登录用户</p>
	 * @param userId 用户编号
	 * @param userPassword 用户密码
	 * @return loginDAO
	 */
	public LoginDO checkUser(String userEname, String userPassword){
		if(userEname == null)
			return null;
		Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
		Matcher m1 = p1.matcher(userEname);
		if(!m1.matches())
			return null;
		if(userPassword == null)
			return null;
		LoginDO user = null;
		String sql = "select ubu.userId as userId,ubu.userCname as userCname, ubu.address as address,"
				+ "ubu.email as email,ubu.instId as instId,ubu.mobile as mobile,ubu.tel as tel,ubu.isUserLocked as isLock,"
				+ "ubu.wrongPwdCount as wrgCount,ubu.wrongPwdDate as wrgDate,ubu.lastModifyDate as lastModifyDate,ubu.isFirstLogin as isFirst,"
				+ "ubu.userEname as userEname,ubu.createTime as createTime,bi.instName as instName,ubu.userLockedReson as userLockedReson ,ubu.enabled as enabled,bi.isHead,ubu.isList"
				+ " from UBaseUserDO ubu left join ubu.baseInst bi where ubu.userId=? and ubu.password=?";
		String logicDelete = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if("1".equals(logicDelete))
			sql += " and (ubu.isDelete!='1' or ubu.isDelete is null) ";
		// 查询数据库获得用户列表
		List userList = userService.find(sql, new Object[] {userEname,
				HexUtils.shaHex(userPassword, userEname)});
		// 1 userId, 2 userCname, 3 address, 4 email, 5 instId, 6 mobile
		// 7 tel, 8 isLock, 9 wrgCount, 10 wrgDate, 11 isFirst, 12 userEname
		// 13 createTime, 14 instName
		if(userList != null && userList.size() > 0){
			// Map userMap = (Map) userList.get(0);
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// 设置用户信息
			login.setAddress(o[2] == null ? "" : o[2].toString());
			login.setCreateTime((java.util.Date) o[13]);
			login.setEmail(o[3] == null ? "" : o[3].toString());
			login.setInstCname(o[14] == null ? "" : o[14].toString());
			login.setInstId(o[4] == null ? "" : o[4].toString());
			login.setMobile(o[5] == null ? "" : o[5].toString());
			login.setTel(o[6] == null ? "" : o[6].toString());
			login.setUserCname(o[1] == null ? "" : o[1].toString());
			login.setUserEname(o[12] == null ? "" : o[12].toString());
			login.setUserId(o[0] == null ? "" : o[0].toString());
			login.setLastModifyDate(o[10] == null ? null : (Date) o[10]);
			login.setIsFirstLogin(o[11] == null ? null : o[11].toString());
			login.setIsUserLocked(o[7] == null ? "" : o[7].toString());
			login.setWrongPwdCount(o[8] == null ? "" : o[8].toString());
			login.setWrongPwdDate(o[9] == null ? null : (Date) o[9]);
			login.setUserLockedReson(o[15] == null ? "" : o[15].toString());
			login.setEnabled(o[16] == null ? "" : o[16].toString());
			login.setInstIsHead(o[17] == null ? "" : o[17].toString());
			login.setIsList(o[18] == null ? "" : o[18].toString());
			user = login;
		}
		return user;
	}

	/**
	 * <p>方法名称: checkUser|描述: hibernate检验数据库中是否存在登录用户</p>
	 * @param userId 用户编号
	 * @param userPassword 用户密码
	 * @return loginDAO
	 */
	public LoginDO checkUserById(String userEname){
		if(userEname == null)
			return null;
		Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
		Matcher m1 = p1.matcher(userEname);
		if(!m1.matches())
			return null;
		LoginDO user = null;
		String sql = "select ubu.userId as userId,ubu.userCname as userCname, ubu.address as address,"
				+ "ubu.email as email,ubu.instId as instId,ubu.mobile as mobile,ubu.tel as tel,ubu.isUserLocked as isLock,"
				+ "ubu.wrongPwdCount as wrgCount,ubu.wrongPwdDate as wrgDate,ubu.lastModifyDate as lastModifyDate,ubu.isFirstLogin as isFirst,"
				+ "ubu.userEname as userEname,ubu.createTime as createTime,bi.instName as instName,ubu.userLockedReson as userLockedReson ,ubu.enabled as enabled,bi.isHead,ubu.isList"
				+ " from UBaseUserDO ubu left join ubu.baseInst bi where ubu.userId= ? ";
		String logicDelete = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if("1".equals(logicDelete))
			sql += " and (ubu.isDelete!='1' or ubu.isDelete is null) ";
		// 查询数据库获得用户列表
		List userList = userService.find(sql, userEname);
		if(userList != null && userList.size() > 0){
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// 设置用户信息
			login.setAddress(o[2] == null ? "" : o[2].toString());
			login.setCreateTime((java.util.Date) o[13]);
			login.setEmail(o[3] == null ? "" : o[3].toString());
			login.setInstCname(o[14] == null ? "" : o[14].toString());
			login.setInstId(o[4] == null ? "" : o[4].toString());
			login.setMobile(o[5] == null ? "" : o[5].toString());
			login.setTel(o[6] == null ? "" : o[6].toString());
			login.setUserCname(o[1] == null ? "" : o[1].toString());
			login.setUserEname(o[12] == null ? "" : o[12].toString());
			login.setUserId(o[0] == null ? "" : o[0].toString());
			login.setLastModifyDate(o[10] == null ? null : (Date) o[10]);
			login.setIsFirstLogin(o[11] == null ? null : o[11].toString());
			login.setIsUserLocked(o[7] == null ? "" : o[7].toString());
			login.setWrongPwdCount(o[8] == null ? "" : o[8].toString());
			login.setWrongPwdDate(o[9] == null ? null : (Date) o[9]);
			login.setUserLockedReson(o[15] == null ? "" : o[15].toString());
			login.setEnabled(o[16] == null ? "" : o[16].toString());
			login.setInstIsHead(o[17] == null ? "" : o[17].toString());
			login.setIsList(o[18] == null ? "" : o[18].toString());
			user = login;
		}
		return user;
	}

	public LoginDO checkOnlyUser(String userEname){
		if(userEname == null)
			return null;
		Pattern p1 = Pattern.compile("^[\\w!@#$]*$");
		Matcher m1 = p1.matcher(userEname);
		if(!m1.matches())
			return null;
		LoginDO user = null;
		String sql = "select ubu.userId as userId,ubu.userCname as userCname, ubu.address as address,ubu.email as email,ubu.instId as instId,ubu.mobile as mobile,ubu.tel as tel,ubu.isUserLocked as isLock,ubu.wrongPwdCount as wrgCount,ubu.wrongPwdDate as wrgDate,ubu.lastModifyDate as lastModifyDate,ubu.isFirstLogin as isFirst,ubu.userEname as userEname,ubu.createTime as createTime,bi.instName as instName from UBaseUserDO ubu left join ubu.baseInst bi where ubu.userEname=?";
		List userList = userService.find(sql, new Object[] {userEname});
		if(userList != null && userList.size() > 0){
			Object o[] = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			login.setAddress(o[2] != null ? o[2].toString() : "");
			login.setCreateTime((Date) o[13]);
			login.setEmail(o[3] != null ? o[3].toString() : "");
			login.setInstCname(o[14] != null ? o[14].toString() : "");
			login.setInstId(o[4] != null ? o[4].toString() : "");
			login.setMobile(o[5] != null ? o[5].toString() : "");
			login.setTel(o[6] != null ? o[6].toString() : "");
			login.setUserCname(o[1] != null ? o[1].toString() : "");
			login.setUserEname(o[12] != null ? o[12].toString() : "");
			login.setUserId(o[0] != null ? o[0].toString() : "");
			login.setLastModifyDate(o[10] != null ? (Date) o[10] : null);
			login.setIsFirstLogin(o[11] != null ? o[11].toString() : null);
			login.setIsUserLocked(o[7] != null ? o[7].toString() : "");
			login.setWrongPwdCount(o[8] != null ? o[8].toString() : "");
			login.setWrongPwdDate(o[9] != null ? (Date) o[9] : null);
			user = login;
		}
		return user;
	}

	public UBaseUserDO findByUser(String userEname){
		if(userEname == null)
			return null;
		Pattern p1 = Pattern.compile("^[\\w!@#$]*$");
		Matcher m1 = p1.matcher(userEname);
		if(!m1.matches())
			return null;
		UBaseUserDO user = null;
		String sql = "select ubu.userId as userId,ubu.userCname as userCname, ubu.isList from UBaseUserDO ubu left join ubu.baseInst bi where ubu.userEname=?";
		List userList = userService.find(sql, new Object[] {userEname});
		if(userList != null && userList.size() > 0){
			Object o[] = (Object[]) userList.get(0);
			UBaseUserDO u = new UBaseUserDO();
			u.setUserId(o[0] != null ? o[0].toString() : "");
			u.setUserCname(o[1] != null ? o[1].toString() : "");
			u.setIsList(o[2] != null ? o[2].toString() : "");
			user = u;
		}
		return user;
	}

	/**
	 * <p>方法名称: checkUserExit|描述: hibernate检验数据库中是否存在登录用户,之验证用户名</p>
	 * @param userId 用户编号
	 * @return loginDAO
	 */
	public LoginDO checkUserExit(String userId){
		if(userId == null)
			return null;
		Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
		Matcher m1 = p1.matcher(userId);
		if(!m1.matches())
			return null;
		LoginDO user = null;
		String sql = "select ubu.userId as userId,ubu.userCname as userCname, ubu.address as address,"
				+ "ubu.email as email,ubu.instId as instId,ubu.mobile as mobile,ubu.tel as tel,ubu.isUserLocked as isLock,"
				+ "ubu.wrongPwdCount as wrgCount,ubu.wrongPwdDate as wrgDate,ubu.lastModifyDate as lastModifyDate,ubu.isFirstLogin as isFirst, "
				+ "ubu.userEname as userEname,ubu.createTime as createTime,bi.instName as instName,ubu.userLockedReson as userLockedReson,ubu.enabled as enabled,bi.isHead ,ubu.isList"
				+ " from UBaseUserDO ubu left join ubu.baseInst bi where ubu.userId=? ";
		// 排除逻辑删除的
		String logicDelete = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if("1".equals(logicDelete))
			sql += " and (ubu.isDelete!='1' or ubu.isDelete is null) ";
		// 查询数据库获得用户列表
		List userList = userService.find(sql, new Object[] {userId});
		// 1 userId, 2 userCname, 3 address, 4 email, 5 instId, 6 mobile
		// 7 tel, 8 isLock, 9 wrgCount, 10 wrgDate, 11 isFirst, 12 userEname
		// 13 createTime, 14 instName
		if(userList != null && userList.size() > 0){
			// Map userMap = (Map) userList.get(0);
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// 设置用户信息
			login.setAddress(o[2] == null ? "" : o[2].toString());
			login.setCreateTime((java.util.Date) o[13]);
			login.setEmail(o[3] == null ? "" : o[3].toString());
			login.setInstCname(o[14] == null ? "" : o[14].toString());
			login.setUserLockedReson(o[15] == null ? "" : o[15].toString());
			login.setInstId(o[4] == null ? "" : o[4].toString());
			login.setMobile(o[5] == null ? "" : o[5].toString());
			login.setTel(o[6] == null ? "" : o[6].toString());
			login.setUserCname(o[1] == null ? "" : o[1].toString());
			login.setUserEname(o[12] == null ? "" : o[12].toString());
			login.setUserId(o[0] == null ? "" : o[0].toString());
			login.setLastModifyDate(o[10] == null ? null : (Date) o[10]);
			login.setIsFirstLogin(o[11] == null ? null : o[11].toString());
			login.setIsUserLocked(o[7] == null ? "" : o[7].toString());
			login.setWrongPwdCount(o[8] == null ? "" : o[8].toString());
			login.setWrongPwdDate(o[9] == null ? null : (Date) o[9]);
			login.setEnabled(o[16] == null ? "" : o[16].toString());
			login.setInstIsHead(o[17] == null ? "" : o[17].toString());
			login.setIsList(o[18] == null ? "" : o[18].toString());
			user = login;
		}
		// if(userList != null && userList.size() > 0){
		// Map userMap = (Map) userList.get(0);
		// LoginDO login = new LoginDO();
		// // 设置用户信息
		// login.setAddress(userMap.get("address") == null ? "" : userMap.get(
		// "address").toString());
		// login.setCreateTime((java.util.Date) userMap.get("createTime"));
		// login.setEmail(userMap.get("email") == null ? "" : userMap.get(
		// "email").toString());
		// login.setInstCname(userMap.get("instName") == null ? "" : userMap
		// .get("instName").toString());
		// login.setInstId(userMap.get("instId") == null ? "" : userMap.get(
		// "instId").toString());
		// login.setMobile(userMap.get("mobile") == null ? "" : userMap.get(
		// "mobile").toString());
		// login.setTel(userMap.get("tel") == null ? "" : userMap.get("tel")
		// .toString());
		// login.setUserCname(userMap.get("userCname") == null ? "" : userMap
		// .get("userCname").toString());
		// login.setUserEname(userMap.get("userEname") == null ? "" : userMap
		// .get("userEname").toString());
		// login.setUserId(userMap.get("userId") == null ? "" : userMap.get(
		// "userId").toString());
		// login.setIsFirstLogin(userMap.get("isFirst") == null ? null
		// : userMap.get("isFirst").toString());
		// login.setIsUserLocked(userMap.get("isLock") == null ? "" : userMap
		// .get("isLock").toString());
		// login.setWrongPwdCount(userMap.get("wrgCount") == null ? ""
		// : userMap.get("wrgCount").toString());
		// login.setWrongPwdDate(userMap.get("wrgDate") == null ? null
		// : (Date) userMap.get("wrgDate"));
		// user = login;
		// }
		return user;
	}

	/*判断用户对应的角色是否启用 并且在启用期间*/
	public boolean checkRoleUserInUseing(String userEname){
		String enabled = "";
		Date startdate = null;
		Date enddate = null;
		String hql = "select uru.startDate,uru.endDate,uru.enabled from UAuthRoleUserInfoDO uru where uru.userId=?";
		List roleUserList = userService.find(hql, new Object[] {userEname});
		if(roleUserList != null && roleUserList.size() > 0){
			for(Iterator i = roleUserList.iterator(); i.hasNext();){
				Object[] o = (Object[]) i.next();
				startdate = (Date) o[0];
				enddate = (Date) o[1];
				enabled = (String) o[2];
				if(enabled.equals("1")){
					return true;
				}
			}
		}
		return false;
	}

	/*判断用户的当前的登录时间是否在用户的启用期间*/
	public boolean checkUserDate(String userEname){
		Date startdate = null;
		Date enddate = null;
		String sql = "select ubu.startDate as startDate,ubu.endDate as endDate"
				+ " from UBaseUserDO ubu  where ubu.userEname=?";
		// 查询数据库获得用户列表
		List userList = userService.find(sql, new Object[] {userEname});
		if(userList != null && userList.size() > 0){
			Object[] o = (Object[]) userList.get(0);
			startdate = (Date) o[0];
			enddate = (Date) o[1];
		}
		Date now = new Date();
		if(startdate != null && enddate != null){
			if(now.after(startdate) && now.before(enddate))
				return true;
			else
				return false;
		}
		if(startdate != null){
			if(now.after(startdate))
				return true;
			else
				return false;
		}
		if(enddate != null){
			if(now.before(enddate))
				return true;
			else
				return false;
		}
		return true;
	}

	/**
	 * <p>方法名称: checkUser|描述: 检查用户登录错误次数</p>
	 * @param userEname
	 * @return
	 */
	public int checkUser(String userEname){
		if(userEname == null)
			return 0;
		Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
		Matcher m1 = p1.matcher(userEname);
		if(!m1.matches())
			return 0;
		List userList = getUsersByEname(userEname);
		int count = 0;
		if(userList != null && userList.size() > 0){
			UBaseUserDO user = (UBaseUserDO) userList.get(0);
			if(user.getWrongPwdDate() != null
					&& LoginUtil.compareDate(new Date(),
							user.getWrongPwdDate(), LoginUtil.SHORT_FORMAT)){ // 同一天
				try{
					if(StringUtils.isNotEmpty(user.getWrongPwdCount()))
						count = Integer.parseInt(user.getWrongPwdCount()); // 错误次数
				}catch (NumberFormatException e){
					log.error(e);
				}
			}else{
				user.setWrongPwdDate(new Date());
			}
			user.setWrongPwdCount(++count + "");
			int num = getPWDInvalidCounts();
			if(count >= num){
				user.setIsUserLocked(LoginUtil.LOCK_LOCKED);
				user
						.setUserLockedReson(LoginUtil.LOCK_REASON_PWD_INVALID_COUNTS);
				count = -num;
			}
			UBaseUserDO o = (UBaseUserDO) userList.get(0);
			if("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC))
					&& "1".equals(o.getIsDelete())){
				// 逻辑删除的特殊处理不记密码次数
				count = 0;
			}else{
				userService.update(user);
			}
		}
		return count;
	}

	/***************************************************************************
	 * 检查是否启用密码策略
	 **************************************************************************/
	public boolean checkPswIsUsing(){
		if("1".equals(cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE)))
			return true;
		return false;
	}

	/**
	 * <p>方法名称: comeback|描述: 清除密码输错记录</p>
	 * @param uid
	 */
	public void comeback(String uid){
		try{
			UBaseUserDO user = userService.getUserByUserId(uid);
			user.setIsUserLocked(LoginUtil.LOCK_NORMAL);
			user.setUserLockedReson(null);
			user.setWrongPwdCount("0");
			user.setWrongPwdDate(null);
			userService.update(user);
		}catch (Exception e){
			log.error(e);
		}
	}

	/**
	 * <p>方法名称: isOverdue|描述: 用户密码是否过期</p>
	 * @param login
	 * @return
	 */
	public boolean isOverdue(LoginDO login){
		if(!checkPswIsUsing())
			return false;
		int m = getPasswordInvalidDays();
		if(login.getLastModifyDate() != null){
			long d = login.getLastModifyDate().getTime();
			long n = new Date().getTime();
			long l = n - d;
			l = (long) Math.ceil((double) l / 86400000l);
			if(l > m){
				UBaseUserDO user = userService.getUserByUserId(login
						.getUserId());
				user.setIsUserLocked(LoginUtil.LOCK_PWD_OVERDUE);
				user.setUserLockedReson(LoginUtil.LOCK_REASON_PWD_OVERDUE);
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>方法名称: checkUser|描述: 检查此用户名是否存在</p>
	 * @param userEname
	 * @return
	 */
	public List getUsersByEname(String userEname){
		String sql = "from UBaseUserDO u where u.userEname=?";
		// 查询数据库获得用户列表
		return userService.find(sql, userEname);
	}

	/**
	 * <p>方法名称: checkUser|描述: 检查此用户名是否存在</p>
	 * @param userEname
	 * @return
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return userService.getUserByUserId(userId);
	}

	/**
	 * <p>方法名称: registerLoginInfo|描述: 向内存中注册登录信息</p>
	 * @param loginId 登录ID
	 * @param loginDAO 登录用户信息
	 */
	public void registerLoginInfo(String loginId, LoginDO loginDAO){
		logingInfoMap.put(loginId, loginDAO);
	}

	/**
	 * <p>方法名称: checkLoginId|描述: 检查内存中是否存在loginId对应的用户</p>
	 * @param loginId 登录ID
	 * @return 返回loginDAO
	 */
	public LoginDO checkLoginId(String loginId){
		return (LoginDO) logingInfoMap.get(loginId);
	}

	/**
	 * <p>方法名称: removeLoginId|描述: 删除内存中loginId对应的用户信息</p>
	 * @param loginId 登录ID
	 * @return LoginDO
	 */
	public LoginDO removeLoginId(String loginId){
		return (LoginDO) logingInfoMap.remove(loginId);
	}

	/**
	 * <p>方法名称: getNewLoginId|描述: 根据ip创建用户的登录ID</p>
	 * @param ip 用户的访问IP地址
	 * @return String
	 */
	public synchronized String getNewLoginId(String loginId, String ip){
		// 将loginId修改成ip开头，保证访问子系统不受影响 保证拼接后的loginId唯一即可。
		return ip + "-" + loginId + "-" + System.currentTimeMillis();
	}

	/**
	 * <p>方法名称: verifyLoginId|描述: 检查内存中是否包含该loginId</p>
	 * @param loginId 登录ID
	 * @return boolean
	 */
	public boolean verifyLoginId(String loginId){
		return logingInfoMap.containsKey(loginId);
	}

	/**
	 * <p>方法名称: uBaseUserToLoginDO|描述: UBaseUser转换为LoginDO</p>
	 * @param ub UBaseUser对应的对象
	 * @return LoginDO
	 */
	public LoginDO uBaseUserToLoginDO(UBaseUserDO ub){
		LoginDO login = new LoginDO();
		// 设置LoginDO对象信息
		login.setUserId(ub.getUserId());
		login.setUserCname(ub.getUserCname());
		login.setUserEname(ub.getUserEname());
		login.setUserLockedReson(ub.getUserLockedReson());
		login.setAddress(ub.getAddress());
		login.setDepartId(ub.getDepartId());
		login.setEmail(ub.getEmail());
		login.setInstId(ub.getInstId());
		login.setIsFirstLogin(ub.getIsFirstLogin());
		login.setPassword(ub.getPassword());
		login.setMobile(ub.getMobile());
		login.setTel(ub.getTel());
		return login;
	}

	// 密码错误次数
	public int getPWDInvalidCounts(){
		int count = 3;
		String counts = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_ERROR_NUM);
		if(StringUtils.isNotEmpty(counts)){
			try{
				count = Integer.parseInt(counts);
			}catch (NumberFormatException e){
				log.error(e);
			}
		}
		return count;
	}

	// 用户失效时间
	public int getUserOverdueDays(){
		int day = 180;
		String days = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_OVERDUE_DAYS);
		if(StringUtils.isNotEmpty(days)){
			try{
				day = Integer.parseInt(days);
			}catch (NumberFormatException e){
				log.error(e);
			}
		}
		return day;
	}

	// 密码过期天数
	public int getPasswordInvalidDays(){
		int day = 60;
		String days = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_INVAL_DAYS);
		if(StringUtils.isNotEmpty(days)){
			try{
				day = Integer.parseInt(days);
			}catch (NumberFormatException e){
				log.error(e);
			}
		}
		return day;
	}

	// 用户不存在或未锁
	public boolean isNotExistOrNotLocked(String uid){
		if(uid == null)
			return true;
		UBaseUserDO user = userService.getUserByUserId(uid);
		if(user == null)
			return true;
		return (user.getIsUserLocked() == null || LoginUtil.LOCK_NORMAL
				.equals(user.getIsUserLocked()));
	}

	// 更新最近登录时间
	public void updateLastLoginDate(String uid){
		UBaseUserDO user = userService.getUserByUserId(uid);
		user.setLastLoginDate(new Date());
		userService.update(user);
	}

	/**
	 * <p>方法名称: setUserService|描述: 设置UserService对象</p>
	 * @param userService
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}
}
