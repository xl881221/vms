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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: xindengquan
 * @����: 2009-6-27 ����11:48:57
 * @����: [LoginService]�û���¼ϵͳ��Ϣ����֤�����ڴ����û���Ϣ�Ĺ���
 */
public class LoginService{

	private static final Log log = LogFactory.getLog(DwrAsynService.class);
	/** ���ڴ���û���¼��Ϣ */
	private final Map logingInfoMap = new HashMap();
	/** �û�service */
	private UserService userService;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private CacheManager cacheManager;

	/**
	 * <p>��������: checkUser|����: hibernate�������ݿ����Ƿ���ڵ�¼�û�</p>
	 * @param userId �û����
	 * @param userPassword �û�����
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
		// ��ѯ���ݿ����û��б�
		List userList = userService.find(sql, new Object[] {userEname,
				HexUtils.shaHex(userPassword, userEname)});
		// 1 userId, 2 userCname, 3 address, 4 email, 5 instId, 6 mobile
		// 7 tel, 8 isLock, 9 wrgCount, 10 wrgDate, 11 isFirst, 12 userEname
		// 13 createTime, 14 instName
		if(userList != null && userList.size() > 0){
			// Map userMap = (Map) userList.get(0);
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// �����û���Ϣ
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
	 * <p>��������: checkUser|����: hibernate�������ݿ����Ƿ���ڵ�¼�û�</p>
	 * @param userId �û����
	 * @param userPassword �û�����
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
		// ��ѯ���ݿ����û��б�
		List userList = userService.find(sql, userEname);
		if(userList != null && userList.size() > 0){
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// �����û���Ϣ
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
	 * <p>��������: checkUserExit|����: hibernate�������ݿ����Ƿ���ڵ�¼�û�,֮��֤�û���</p>
	 * @param userId �û����
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
		// �ų��߼�ɾ����
		String logicDelete = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		if("1".equals(logicDelete))
			sql += " and (ubu.isDelete!='1' or ubu.isDelete is null) ";
		// ��ѯ���ݿ����û��б�
		List userList = userService.find(sql, new Object[] {userId});
		// 1 userId, 2 userCname, 3 address, 4 email, 5 instId, 6 mobile
		// 7 tel, 8 isLock, 9 wrgCount, 10 wrgDate, 11 isFirst, 12 userEname
		// 13 createTime, 14 instName
		if(userList != null && userList.size() > 0){
			// Map userMap = (Map) userList.get(0);
			Object[] o = (Object[]) userList.get(0);
			LoginDO login = new LoginDO();
			// �����û���Ϣ
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
		// // �����û���Ϣ
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

	/*�ж��û���Ӧ�Ľ�ɫ�Ƿ����� �����������ڼ�*/
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

	/*�ж��û��ĵ�ǰ�ĵ�¼ʱ���Ƿ����û��������ڼ�*/
	public boolean checkUserDate(String userEname){
		Date startdate = null;
		Date enddate = null;
		String sql = "select ubu.startDate as startDate,ubu.endDate as endDate"
				+ " from UBaseUserDO ubu  where ubu.userEname=?";
		// ��ѯ���ݿ����û��б�
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
	 * <p>��������: checkUser|����: ����û���¼�������</p>
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
							user.getWrongPwdDate(), LoginUtil.SHORT_FORMAT)){ // ͬһ��
				try{
					if(StringUtils.isNotEmpty(user.getWrongPwdCount()))
						count = Integer.parseInt(user.getWrongPwdCount()); // �������
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
				// �߼�ɾ�������⴦�����������
				count = 0;
			}else{
				userService.update(user);
			}
		}
		return count;
	}

	/***************************************************************************
	 * ����Ƿ������������
	 **************************************************************************/
	public boolean checkPswIsUsing(){
		if("1".equals(cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE)))
			return true;
		return false;
	}

	/**
	 * <p>��������: comeback|����: �����������¼</p>
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
	 * <p>��������: isOverdue|����: �û������Ƿ����</p>
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
	 * <p>��������: checkUser|����: �����û����Ƿ����</p>
	 * @param userEname
	 * @return
	 */
	public List getUsersByEname(String userEname){
		String sql = "from UBaseUserDO u where u.userEname=?";
		// ��ѯ���ݿ����û��б�
		return userService.find(sql, userEname);
	}

	/**
	 * <p>��������: checkUser|����: �����û����Ƿ����</p>
	 * @param userEname
	 * @return
	 */
	public UBaseUserDO getUserByUserId(String userId){
		return userService.getUserByUserId(userId);
	}

	/**
	 * <p>��������: registerLoginInfo|����: ���ڴ���ע���¼��Ϣ</p>
	 * @param loginId ��¼ID
	 * @param loginDAO ��¼�û���Ϣ
	 */
	public void registerLoginInfo(String loginId, LoginDO loginDAO){
		logingInfoMap.put(loginId, loginDAO);
	}

	/**
	 * <p>��������: checkLoginId|����: ����ڴ����Ƿ����loginId��Ӧ���û�</p>
	 * @param loginId ��¼ID
	 * @return ����loginDAO
	 */
	public LoginDO checkLoginId(String loginId){
		return (LoginDO) logingInfoMap.get(loginId);
	}

	/**
	 * <p>��������: removeLoginId|����: ɾ���ڴ���loginId��Ӧ���û���Ϣ</p>
	 * @param loginId ��¼ID
	 * @return LoginDO
	 */
	public LoginDO removeLoginId(String loginId){
		return (LoginDO) logingInfoMap.remove(loginId);
	}

	/**
	 * <p>��������: getNewLoginId|����: ����ip�����û��ĵ�¼ID</p>
	 * @param ip �û��ķ���IP��ַ
	 * @return String
	 */
	public synchronized String getNewLoginId(String loginId, String ip){
		// ��loginId�޸ĳ�ip��ͷ����֤������ϵͳ����Ӱ�� ��֤ƴ�Ӻ��loginIdΨһ���ɡ�
		return ip + "-" + loginId + "-" + System.currentTimeMillis();
	}

	/**
	 * <p>��������: verifyLoginId|����: ����ڴ����Ƿ������loginId</p>
	 * @param loginId ��¼ID
	 * @return boolean
	 */
	public boolean verifyLoginId(String loginId){
		return logingInfoMap.containsKey(loginId);
	}

	/**
	 * <p>��������: uBaseUserToLoginDO|����: UBaseUserת��ΪLoginDO</p>
	 * @param ub UBaseUser��Ӧ�Ķ���
	 * @return LoginDO
	 */
	public LoginDO uBaseUserToLoginDO(UBaseUserDO ub){
		LoginDO login = new LoginDO();
		// ����LoginDO������Ϣ
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

	// ����������
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

	// �û�ʧЧʱ��
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

	// �����������
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

	// �û������ڻ�δ��
	public boolean isNotExistOrNotLocked(String uid){
		if(uid == null)
			return true;
		UBaseUserDO user = userService.getUserByUserId(uid);
		if(user == null)
			return true;
		return (user.getIsUserLocked() == null || LoginUtil.LOCK_NORMAL
				.equals(user.getIsUserLocked()));
	}

	// ���������¼ʱ��
	public void updateLastLoginDate(String uid){
		UBaseUserDO user = userService.getUserByUserId(uid);
		user.setLastLoginDate(new Date());
		userService.update(user);
	}

	/**
	 * <p>��������: setUserService|����: ����UserService����</p>
	 * @param userService
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}
}
