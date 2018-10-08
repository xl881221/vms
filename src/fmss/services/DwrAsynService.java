package fmss.services;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fmss.action.base.AuditException;
import fmss.action.base.UserChangingService;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseHisUserPwdDO;
import fmss.dao.entity.UBaseNoticeFeedBackDO;
import fmss.dao.entity.UBaseNoticeInfoDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.common.cache.SystemCache;
import fmss.action.BaseAction;
import fmss.common.util.Constants;
import fmss.common.util.HexUtils;
import fmss.common.util.LoginUtil;
import fmss.common.util.PaginationList;
import fmss.common.util.SecurityPassword;
import fmss.common.util.SpringContextUtils;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: GongRunLin
 * @日期: 2009-7-29 上午10:00:00
 * @描述: DWR异步服务
 */
public class DwrAsynService {

	private static final Log log = LogFactory.getLog(DwrAsynService.class);

	private NoticeService noticeService; // 通告服务
	private static IdGenerator idGenerator; // id生成器
	private SystemCache systemCache;
	private UserService userService;
	private UBaseSysLogService sysLogService;
	private CacheManager cacheManager;
	private OnlineService onlineService;
	private UsysTask usysTask;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	private final long r = 86400000l;

	public String outPrintText() {
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject(Constants.PAPAMETER_CACHE_MAP);
		Map params = null;
		String value = "统一监管报送平台(FMSS)集华颉多年在统计报送的经<br>"
				+ "验，提供了金融机构监管报送全面方案，满足不同监管机构<br>" + "的报送要求";
		if (cache != null) {
			params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			if (null != params) {
				// 登陆页提示
				UBaseSysParamDO param2 = (UBaseSysParamDO) params
						.get(Constants.PARAM_SYS_TEXT_VALUE);
				if (param2 != null) {
					value = param2.getSelectedValue() == null ? value : param2
							.getSelectedValue();
				}
			}
		}
		return value;
	}

	/**
	 * <p>
	 * 方法名称: saveFeedBack|描述:保存通告反馈信息
	 * </p>
	 * 
	 * @param feedback
	 * @return boolean
	 */
	public boolean saveFeedBack(UBaseNoticeFeedBackDO feedback) {
		try {
			idGenerator = IdGenerator
					.getInstance(DictionaryService.NOTICE_FEEDBACK_TYPE);
			LoginDO user = getUser();
			feedback.setId(new Long(idGenerator.getNextKey()));
			feedback.setUserId(user.getUserId());
			feedback.setUserCName(user.getUserCname());
			feedback.setUserEName(user.getUserEname());
			feedback.setFeedTime(new Date());
			noticeService.save(feedback);
			user.setDescription("保存通告反馈信息");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(user,
					"保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0020");
			sysLog1.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			LoginDO user = getUser();
			user.setDescription("保存通告反馈信息");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(user,
					"保存", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0020");
			sysLog1.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
			return false;
		}
		return true;
	}

	// 加密

	private String encode(String str) {
		try {
			str = java.net.URLEncoder.encode(common.crms.util.encode.Base64
					.encode(str.getBytes()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * <p>
	 * 方法名称: getSubSystems|描述:取得当前用户有权限的子系统
	 * </p>
	 * 
	 * @param addPub
	 * @return
	 */
	public List getSubSystems(boolean addPub) {
		List sys = new ArrayList();
		List subSysList = new ArrayList();
		try {
			LoginDO user = getUser();
			sys = noticeService.getSubSystems(user.getUserId(), addPub);
			HttpServletRequest request = WebContextFactory.get()
					.getHttpServletRequest();
			String theme = WebContextFactory.get().getSession().getAttribute(
					Constants.SESSION_THEME_KEY).toString();
			String defaultIP = request.getSession().getAttribute(
					"defaultIPAddress").toString();
			String defaultPort = request.getSession().getAttribute(
					"defaultPort").toString();
			// 取得Fmss的访问地址
			StringBuffer url = request.getRequestURL();
			String parentUrl = url.substring(0, 1 + url.lastIndexOf("/dwr/"));
			String parentInnerUrl = BaseAction.getURL(cacheManager
					.getParemerCacheMapValue(Constants.PARAM_USYS_INNER_URL),
					defaultIP, defaultPort);
			String ajaxUrl = "homeNoteData.ajax?maxRowNum=" + getLimit(2);
			String param = "&usdfsddf=11232dfd0091232&loginId="
					+ encode(user.getLoginId()) + "&userId="
					+ encode(user.getUserId()) + "&userEname="
					+ encode(user.getUserEname()) + "&parentUrl="
					+ encode(parentUrl) + "&parentInnerUrl="
					+ encode(parentInnerUrl) + "&"
					+ Constants.SESSION_THEME_KEY + "=" + encode(theme);
			for (int i = 0; i < sys.size(); i++) {
				Map item = (Map) sys.get(i);
				if (item.get("key").equals("00000")) {
					subSysList.add(item);
					continue;
				}
				String display = (String) item.get("display");
				if ("menu".equalsIgnoreCase(display)) {
					// 对于只显示于菜单的子系统，不在待处理问题处出现
					continue;
				}
				String tempUrl = BaseAction.isInnerNet(request) ? item.get(
						"innerRoot").toString() : item.get("root").toString();
				log.info("-----------root:" + tempUrl);
				if (tempUrl != null
						&& tempUrl.indexOf(Constants.DEFAULT_URL_IP) != -1) {
					tempUrl = tempUrl.replaceFirst(
							Constants.DEFAULT_URL_IP_UNICODE, defaultIP);
				}
				if (tempUrl != null
						&& tempUrl.indexOf(Constants.DEFAULT_URL_PORT) != -1) {
					tempUrl = tempUrl.replaceFirst(
							Constants.DEFAULT_URL_PORT_UNICODE, defaultPort);
				}
				log.info("-----------replaceRoot:" + tempUrl);
				item.put("url", tempUrl + ajaxUrl + param + "&subSystemId="
						+ encode(item.get("key").toString()) /* 为了在crms各子系统中区分机构权限，菜单的url要加上systemId */
						+ "&systemId=" + item.get("systemId").toString()
						+ "&locale="
						+ ("c".equals(user.getLanguage()) ? "zh_CN" : "en_US"));
				item.put("root", tempUrl);
				subSysList.add(item);
			}
			Map pub = new HashMap();
			pub.put("key", "");
			pub.put("value", "全部");
			// sys.add(0, pub);
			subSysList.add(0, pub);
		} catch (Exception e) {
			log.error(e);
		}
		return subSysList;
	}

	/**
	 * <p>
	 * 方法名称: getUserInfos|描述:取得用户登录信息
	 * </p>
	 * 
	 * @return
	 */
	public Object[] getUserInfos() {
		LoginDO user = getUser();
		Map info = new HashMap();
		info.put("联系地址", user.getAddress());
		info.put("联系电话", user.getMobile());
		info.put("电子邮箱", user.getEmail());
		info.put("IP地址", user.getIp());
		PaginationList pl = new PaginationList();
		pl.setPageSize(10);
		String hql = "from UBaseSysLogDO l where l.userId='" + user.getUserId()
				+ "' order by l.execTime desc";
		noticeService.find(hql, new ArrayList(), pl);
		Object[] o = new Object[2];
		o[0] = info;
		o[1] = pl.getRecordList();
		return o;
	}

	/**
	 * <p>
	 * 方法名称: getNotices|描述:取得参数类型的通告信息
	 * </p>
	 * 
	 * @param type
	 * @return
	 */
	public Object[] getNotices(String type) {
		List notices = null;
		PaginationList pl = new PaginationList();
		pl.setPageSize(6);
		UBaseNoticeInfoDO notice = new UBaseNoticeInfoDO();
		notice.setType(type);
		try {
			LoginDO user = getUser();
			notices = noticeService.findNotices0(user.getUserId(), notice, pl);
			UBaseNoticeInfoDO n = null;
			long now = new Date().getTime();
			long t = now % 86400000;
			now = t > 0 ? (now - t + 86400000) : now;
			long reg = 86400000 * 5;
			if (notices != null && !notices.isEmpty()) {
				List types = noticeService
						.getSubSystems(user.getUserId(), true);
				Map map = new HashMap();
				Map temp = null;
				for (int i = 0; i < types.size(); i++) {
					temp = (Map) types.get(i);
					map.put(temp.get("key"), temp.get("value"));
				}
				for (int i = 0; i < notices.size(); i++) {
					n = (UBaseNoticeInfoDO) notices.get(i);
					// if(null == n.getCreateTime()){
					// n.setStatus("false");
					// }else{
					// if(now - n.getCreateTime().getTime() < reg){
					// n.setStatus("true");
					// }
					// else{
					// n.setStatus("false");
					// }
					// }
					n.setType((String) map.get(n.getType()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] o = new Object[2];
		o[0] = notices;
		o[1] = pl.getRecordCount() + "";
		return o;
	}

	/**
	 * <p>
	 * 方法名称: getPswUDTime|描述:取得用户未修改密码天数
	 * </p>
	 * 
	 * @return
	 */
	public String getPswUDTime() {
		LoginDO login = getUser();
		// long l = -1;
		if (login.getIsFirstLogin() == null
				|| login.getIsFirstLogin().equals("")
				|| login.getIsFirstLogin().equals("0")) {
			// updatePasswordCallback();
			// 不开启密码策略不用弹出修改框
			if (!checkPswIsUsing())
				return "";
			return "F";
		} else {
			long l = 0;
			if (login.getLastModifyDate() != null) {
				long d = login.getLastModifyDate().getTime();
				long n = new Date().getTime();
				long day = 31;
				// PARAM_SYS_INVAL_DAYS
				String days = cacheManager
						.getParemerCacheMapValue(Constants.PARAM_SYS_INVAL_DAYS);
				if (!"".equals(days)) {
					day = Long.parseLong(days);
				}

				if (checkPswIsUsing()) {
					d = d + day * r;
					l = d - n;
					// TODO
					l = (long) Math.ceil((double) l / r);
					int frequencyTime = 5;
					String frequency = cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_PWD_REMIND_TIME);
					try {
						frequencyTime = Integer.parseInt(frequency);
					} catch (NumberFormatException e) {
						log.error("密码过期提示设置内容格式无效,使用默认设置：5天", e);
					}

					if (l <= frequencyTime && l > 0) {
						return "密码" + l + "天后过期，请及时修改!"; // 密码3天过期，请及时修改
					}
					if (l <= 0) {
						return "T";
						/*
						 * String userUpdate
						 * =cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_TIMING_USER_UPDATE);
						 * if(userUpdate!=null&&userUpdate.equals("0")){ }
						 * return "密码已过期"; //密码已过期。
						 */}
					return "";
				}
				/*
				 * else{ l = n - d; l = (long) Math.ceil((double) l / r);
				 * if(l>=30){ return "您已经"+l+"天没有修改密码了！" ; //您已经超过30天没有修改密码了！
				 * }else{ return ""; } }
				 */

			}
			if (l == 0)
				return "";
		}
		return "F"; // First login
	}

	public void updatePasswordCallback() {
		LoginDO login = getUser();
		// long l = -1;
		if (login.getIsFirstLogin() == null
				|| login.getIsFirstLogin().equals("")
				|| login.getIsFirstLogin().equals("0")) {
			if (login.getLastModifyDate() == null) {
				try { // 修改密码后，保存最后修改日期
					UBaseUserDO user = userService.getUserByUserId(login
							.getUserId());
					user.setLastModifyDate(new Date(
							new Date().getTime() - 86400000));
					user.setIsFirstLogin("1");
					login.setLastModifyDate(user.getLastModifyDate());
					login.setIsFirstLogin("1");
					// userService.update(user);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * <p>
	 * 方法名称: isEditPwd|描述: 用户是否可以修改密码
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEditPwd() {
		if (!checkPswIsUsing())
			return true;
		// 开启策略下去掉只能一天修改一次提示
		if (true)
			return true;

		boolean b = true;
		LoginDO user = getUser();
		if (user.getIsFirstLogin() == null || user.getIsFirstLogin().equals("")
				|| user.getIsFirstLogin().equals("0")) // 首次登录可以修改
			b = true;
		else {
			if (user.getLastModifyDate() != null) {
				long d = user.getLastModifyDate().getTime();
				long n = new Date().getTime();
				long t = n - n % r;
				if (t < d && d < t + r) // 是否当天修改过
					b = false;
			}
		}
		return b;
	}

	/* 是否为最近次数内用过的密码 */
	private boolean isExist(String uid, String pwd, int num) {
		List list = userService.getBaseHisUserPwd(uid);
		if (list != null) {
			int n = getLimit(3);
			if (list.size() < n)
				n = list.size();
			for (int i = 0; i < n; i++) {
				UBaseHisUserPwdDO hup = (UBaseHisUserPwdDO) list.get(i);
				if (hup.getPassword().equals(pwd))
					return true;
			}
		}
		return false;
	}

	/***************************************************************************
	 * 检查是否启用密码策略 modify by wangxin 修改从缓存获取
	 **************************************************************************/

	public boolean checkPswIsUsing() {
		String isselcted = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE);
		if (isselcted.equals("1"))
			return true;
		return false;
	}

	/**
	 * <p>
	 * 方法名称: updatePWD|描述: 修改密码
	 * </p>
	 * 
	 * @param pwd
	 * @return int
	 */
	public Map updatePWD(String oldPwd, String pwd) {
		Map map = new HashMap();
		LoginDO login = getUser();
		UBaseUserDO user = userService.getUserByUserId(login.getUserId());
		if (checkPswIsUsing()) {
			if (!isEditPwd()) {
				map.put("rel", "5");
				return map;
			}
		}
		if (!user.getPassword().equals(
				HexUtils.shaHex(oldPwd, login.getUserId()))) {
			map.put("rel", "6");
			return map;
		}
		if (pwd == null || pwd.equals("")) {
			map.put("rel", "2");
			return map;
		}

		/*
		 * String rela = this.getPwdRela(); if (!"".equals(rela)) { Pattern p =
		 * Pattern.compile(rela); Matcher m = p.matcher(pwd); if (!m.matches()) {
		 * map.put("rel", "7"); return map; } } else { Pattern p =
		 * Pattern.compile(Constants.PATTERN_PWD); Matcher m = p.matcher(pwd);
		 * if (!m.matches()) { map.put("rel", "3"); return map; } }
		 */
		if (checkPswIsUsing()) {
			// 密码检查
			if (!SecurityPassword.isSecurity(pwd)) {
				map.put("rel", "7");
				map.put("message", SecurityPassword.securityMessage("<br>"));
				return map;
			}

			if (pwd.indexOf(login.getUserId()) >= 0) {
				map.put("rel", "8");
				return map;
			}
		}

		if (checkPswIsUsing()) {
			int iMinLen = 6;
			int iMaxLen = 30;
			UBaseSysParamDO pMinLen = (UBaseSysParamDO) noticeService
					.getObjectById(UBaseSysParamDO.class, new Long(25),
							"paramId");
			if (pMinLen != null && pMinLen.getSelectedValue() != null
					&& !pMinLen.getSelectedValue().equals("")) {
				iMinLen = Integer.parseInt(pMinLen.getSelectedValue());
			}
			UBaseSysParamDO pMaxLen = (UBaseSysParamDO) noticeService
					.getObjectById(UBaseSysParamDO.class, new Long(26),
							"paramId");
			if (pMaxLen != null && pMaxLen.getSelectedValue() != null
					&& !pMaxLen.getSelectedValue().equals("")) {
				iMaxLen = Integer.parseInt(pMaxLen.getSelectedValue());
			}

			if (pwd.length() < iMinLen || pwd.length() > iMaxLen) {
				map.put("rel", "4");
				map.put("max", iMaxLen + "");
				map.put("min", iMinLen + "");
				return map;
			}
		}
		pwd = HexUtils.shaHex(pwd, login.getUserId()); // 加密
		if (checkPswIsUsing()) {
			int num = getLimit(3);
			if (isExist(user.getUserId(), pwd, num)) {
				map.put("rel", -num + "");
				return map;
			}
		}
		try {
			Date d = new Date();
			user.setPassword(pwd);
			user.setLastModifyDate(d); // 记录最后修改时间
			user.setIsFirstLogin("1"); // 设置为非首次登录
			userService.update(user); // 保存密码
			UBaseHisUserPwdDO hup = new UBaseHisUserPwdDO();
			hup.setId(new Long(d.getTime()));
			hup.setUserId(user.getUserId());
			hup.setPassword(pwd);
			hup.setModifyTime(d);
			userService.save(hup); // 密码修改记录
			login.setLastModifyDate(d);
			login.setIsFirstLogin("1");
			login.setDescription("更新密码");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"更新", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			map.put("rel", "0");
			return map;
		}
		// return 1;
		map.put("rel", "1");
		return map;
	}

	/**
	 * <p>
	 * 方法名称: refreshCache|描述:刷新缓存
	 * </p>
	 * 
	 * @return
	 */
	public boolean refreshCache() {
		try {

			// 刷新主题样式信息
			UBaseSysParamDO param = (UBaseSysParamDO) noticeService
					.getObjectById(UBaseSysParamDO.class, new Long(1),
							"paramId");
			if (param != null) {
				WebContextFactory.get().getSession().setAttribute(
						Constants.SESSION_THEME_KEY, param.getSelectedValue());
			}
			// 刷新菜单
			systemCache.runCacheRegister();

			try {
				usysTask.afterPropertiesSetRefresh();
			} catch (Exception e) {
				log.error("定时器刷新异常：" + e.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * 方法名称: resetPWD|描述: 重置密码
	 * </p>
	 * 
	 * @param uid
	 * @return
	 */
	public String resetPWD(String uid) {
		LoginDO login = getUser();
		String message = null;
		try {
			UBaseUserDO user = userService.getUserByUserId(uid);
			// user.setPassword(HexUtils.shaHex("000000"));
			String def = "000000";
			UBaseSysParamDO p = (UBaseSysParamDO) noticeService.getObjectById(
					UBaseSysParamDO.class, new Long(24), "paramId");
			if (p != null && p.getSelectedValue() != null
					&& !p.getSelectedValue().equals("")) {
				def = p.getSelectedValue();
			}

			user.setPassword(HexUtils.shaHex(def, user.getUserId()));

			// user.setIsUserLocked(LoginUtil.LOCK_NORMAL);
			user.setLastModifyDate(new Date());

			login.setDescription("重置用户:"
					+ AuthorityNameFetcher.fetchUserName(user.getUserId())
					+ " 密码");

			// 开启密码策略，修改为首次登陆需要重新修改密码
			if ("1".equals(cacheManager
					.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE)))
				user.setIsFirstLogin("0");

			if ("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))) {
				UserChangingService userChangingService = (UserChangingService) SpringContextUtils
						.getBean("userChangingService");
				UBaseUserDO original = userService.getUserByUserId(user
						.getUserId());
				userChangingService.saveBaseChanges(login, user, original,
						"重置密码");
				message = "重置成功,待审核通过后生效";
			} else {
				userService.update(user);
				message = "重置成功";
			}
			login.addDescription("," + message);

			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"重置", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription("重置密码");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"重置", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
			if (e instanceof AuditException) {
				return "重置失败," + e.getMessage();
			}
			return "重置失败";
		}
		return message;
	}

	/**
	 * <p>
	 * 方法名称: enabled|描述: 停启用用户账户
	 * </p>
	 * 
	 * @param isable,uid
	 * @return
	 */
	public String enabled(String isable, String uid) {
		LoginDO login = null;
		String msg = null;
		String message = null;

		try {
			login = getUser();
			UBaseUserDO user = userService.getUserByUserId(uid);
			if (isable.equals("1")) {
				user.setEnabled("1");
				msg = "启用用户账户  ";
				message = "已经启用账户";
			} else if (isable.equals("0")) {
				user.setEnabled("0");
				msg = "停用用户账户  ";
				message = "已经停用账户";
			}
			login.setDescription(msg
					+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
			if ("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))) {
				UserChangingService userChangingService = (UserChangingService) SpringContextUtils
						.getBean("userChangingService");
				UBaseUserDO original = userService.getUserByUserId(user
						.getUserId());
				userChangingService.saveBaseChanges(login, user, original, msg);
				message = message + ",待审核通过后生效";
			} else {
				userService.update(user);
			}
			login.addDescription("," + message);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"更新", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription(msg);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"更新", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return msg + "失败," + e.getMessage();
			}
			return msg + "失败";
		}
		return message;
	}

	/**
	 * <p>
	 * 方法名称: enabled|描述: 停启用用户账户
	 * </p>
	 * 
	 * @param isable,uid
	 * @return
	 */
	public String updateList(String isable, String uid) {
		LoginDO login = null;
		String msg = null;
		String message = null;
		try {
			login = getUser();
			UBaseUserDO user = userService.getUserByUserId(uid);
			if (isable.equals("1")) {
				user.setIsList("1");
				msg = "加入白名单  ";
				message = "已经加入白名单";
			} else if (isable.equals("0")) {
				user.setIsList("0");
				msg = "从白名单中去除  ";
				message = "已经从白名单中去除";
			}
			login.setDescription(msg
					+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
			if ("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))) {
				UserChangingService userChangingService = (UserChangingService) SpringContextUtils
						.getBean("userChangingService");
				UBaseUserDO original = userService.getUserByUserId(user
						.getUserId());
				userChangingService.saveBaseChanges(login, user, original, msg);
				message = message + ",待审核通过后生效";
			} else {
				userService.update(user);
			}
			login.addDescription("," + message);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"更新", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription(msg);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"更新", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return msg + "失败," + e.getMessage();
			}
			return msg + "失败";
		}
		return message;
	}

	/**
	 * <p>
	 * 方法名称: removeLock|描述: 解除锁定
	 * </p>
	 * 
	 * @param uid
	 * @return
	 */
	public String removeUserLock(String uid) {
		LoginDO login = null;
		String message = null;
		try {
			login = getUser();
			UBaseUserDO user = userService.getUserByUserId(uid);
			if (user.getIsUserLocked() != null) {
				if (LoginUtil.LOCK_LOCKED.equals(user.getIsUserLocked())) {
					user.setWrongPwdCount("0");
					user.setWrongPwdDate(null);
				}
				user.setIsUserLocked(LoginUtil.LOCK_NORMAL);
				user.setUserLockedReson(null);
				user.setLastLoginDate(new Date());
				login.setDescription("解除锁定用户:"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				if ("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))) {
					UserChangingService userChangingService = (UserChangingService) SpringContextUtils
							.getBean("userChangingService");
					UBaseUserDO original = userService.getUserByUserId(user
							.getUserId());
					userChangingService.saveBaseChanges(login, user, original,
							"用户解锁");
					message = "锁定已经解除,待审核通过后生效";
				} else {
					userService.update(user);
					message = "锁定已经解除";
				}
				login.addDescription("," + message);
				UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(
						login, "删除", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog1.setMenuId("0002.0002");
				sysLog1.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog1);
			}
		} catch (Exception e) {
			log.error(e);
			login.setDescription("解除锁定");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"删除", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("基础信息管理.用户管理");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return "解锁失败," + e.getMessage();
			}
			return "解锁失败";
		}
		return message;
	}

	/**
	 * <p>
	 * 方法名称: isSysAdmin|描述: 检测是否可管理通告
	 * </p>
	 * 
	 * @return
	 */
	public boolean isSysAdmin() {
		LoginDO login = getUser();
		String hql = "from UAuthRoleResourceDO r,UAuthRoleUserDO a where r.objectId=a.roleId and a.userId='"
				+ login.getUserId()
				+ "' and r.resId='34' and r.resDetailName like '%通告管理%'";
		List list = userService.find(hql);
		if (list != null && !list.isEmpty())
			return true;
		return false;
	}

	/* 取得最大记录条数 */
	private int getLimit(long l) {
		int s = l == 2 ? 5 : 15; // 默认
		try {
			UBaseSysParamDO p = (UBaseSysParamDO) noticeService.getObjectById(
					UBaseSysParamDO.class, new Long(l), "paramId");
			if (p != null && p.getSelectedValue() != null
					&& !p.getSelectedValue().equals("")) {
				int n = Integer.parseInt(p.getSelectedValue());
				if (n > 0) // 必须为正整数
					s = n;
			}
		} catch (NumberFormatException e) {
			s = l == 2 ? 5 : 15;
			e.printStackTrace();
		}
		return s;
	}

	/* 取得当前用户 */
	private LoginDO getUser() {
		HttpSession session = WebContextFactory.get().getSession();
		LoginDO user = (LoginDO) session.getAttribute("LOGIN_USER");
		return user;
	}

	/**
	 * <p>
	 * 方法名称: getLogoutAlertInfo|描述:取得注销提示信息
	 * </p>
	 * 
	 * @return
	 */
	public String getLogoutAlertInfo() {
		String strMsg = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_LOGOUT_INFO);
		return strMsg.trim();
	}

	/**
	 * <p>
	 * 方法名称: getLogoutAlertInfo|描述:取得注销提示信息
	 * </p>
	 * 
	 * @return
	 */
	public String getPwdRela() {
		String strMsg = cacheManager
				.getParemerCacheMapValue("PARAM_SYS_PWD_RELA");
		return strMsg.trim();
	}

	/**
	 * 判断用户是否失效，定时
	 * 
	 * @return
	 */
	public int isUserValid() {
		HttpSession session = WebContextFactory.get().getSession();
		if (session == null)
			return Constants.SESSION_TIME_OUT;
		LoginDO user = getUser();
		if (user == null)
			return Constants.SESSION_TIME_OUT;
		if (!onlineService.hasLoginByLoginId(user.getLoginId()))
			return Constants.SESSION_MUTIL_LOGIN;
		return Constants.SESSION_NORMAL;
	}

	public boolean isAllowSameUserLogin() {
		return "1"
				.equals(cacheManager
						.getParemerCacheMapValue(Constants.PARAM_SYS_ALLOW_SAMEUSER_LOGIN));
	}

	public String kickoutUser(String loginIds) {
		String s = null;
		LoginDO user = getUser();
		String args[] = loginIds.split(",");
		for (int i = 0, j = args.length; i < j; i++) {
			if (!StringUtils.isEmpty(args[i])) {
				onlineService.kickoutLogin(args[i]);
			}
		}
		if (s == null) {
			s = "操作成功";
		}
		return s;
	}

	public String deleteLogin(String loginIds) {
		String s = null;
		LoginDO user = getUser();
		String args[] = loginIds.split(",");
		for (int i = 0, j = args.length; i < j; i++) {
			if (!StringUtils.isEmpty(args[i])) {
				onlineService.deleteUser(args[i]);
			}
		}
		if (s == null) {
			s = "操作成功";
		}
		return s;
	}

	public String checkLogin() {
		LoginDO login = this.getUser();
		if (login == null)
			return onlineService.checkLoginStatus(null);
		else {
			return onlineService.checkLoginStatus(login.getLoginId());
		}
	}

	/**
	 * 主动锁定用户
	 * 
	 * @param uid
	 * @return
	 */
	public int lockThisUser(String uid) {
		LoginDO login = getUser();
		try {
			if (CollectionUtils.isEmpty(userService.getAdminUserList(login
					.getUserId(), Constants.SYSTEM_COMMON_ID))) {
				return LOCK_NOT_SUPER_USER;
			}
			UBaseUserDO user = userService.getUser(uid);
			user.setIsUserLocked(LoginUtil.LOCK_LOCKED);
			user.setUserLockedReson(LoginUtil.LOCK_REASON_BY_USER);
			userService.update(user);
			login.setDescription("锁定用户"
					+ AuthorityNameFetcher.fetchUserName(uid));
			UBaseSysLogDO log = this.sysLogService.setUBaseSysLog(login, "删除",
					"1", Constants.BASE_SYS_LOG_AUTHORITY);
			log.setMenuId("0002.0002");
			log.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(log);
		} catch (Exception e) {
			LogFactory.getLog(this.getClass()).error("锁定用户出错", e);
			login.setDescription("锁定用户" + uid);
			UBaseSysLogDO log = this.sysLogService.setUBaseSysLog(login, "删除",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			log.setMenuId("0002.0002");
			log.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(log);
			return LOCK_FAIL;
		}
		return LOCK_SUCCESS;
	}

	public static final int LOCK_NOT_SUPER_USER = -2;
	public static final int LOCK_FAIL = -1;
	public static final int LOCK_SUCCESS = 0;

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public SystemCache getSystemCache() {
		return systemCache;
	}

	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}

	public UsysTask getUsysTask() {
		return usysTask;
	}

	public void setUsysTask(UsysTask usysTask) {
		this.usysTask = usysTask;
	}

}