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
 * ��Ȩ����:(C)2003-2010
 * </p>
 * 
 * @����: GongRunLin
 * @����: 2009-7-29 ����10:00:00
 * @����: DWR�첽����
 */
public class DwrAsynService {

	private static final Log log = LogFactory.getLog(DwrAsynService.class);

	private NoticeService noticeService; // ͨ�����
	private static IdGenerator idGenerator; // id������
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
		String value = "ͳһ��ܱ���ƽ̨(FMSS)����������ͳ�Ʊ��͵ľ�<br>"
				+ "�飬�ṩ�˽��ڻ�����ܱ���ȫ�淽�������㲻ͬ��ܻ���<br>" + "�ı���Ҫ��";
		if (cache != null) {
			params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			if (null != params) {
				// ��½ҳ��ʾ
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
	 * ��������: saveFeedBack|����:����ͨ�淴����Ϣ
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
			user.setDescription("����ͨ�淴����Ϣ");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(user,
					"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0020");
			sysLog1.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			LoginDO user = getUser();
			user.setDescription("����ͨ�淴����Ϣ");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(user,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0020");
			sysLog1.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
			return false;
		}
		return true;
	}

	// ����

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
	 * ��������: getSubSystems|����:ȡ�õ�ǰ�û���Ȩ�޵���ϵͳ
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
			// ȡ��Fmss�ķ��ʵ�ַ
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
					// ����ֻ��ʾ�ڲ˵�����ϵͳ�����ڴ��������⴦����
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
						+ encode(item.get("key").toString()) /* Ϊ����crms����ϵͳ�����ֻ���Ȩ�ޣ��˵���urlҪ����systemId */
						+ "&systemId=" + item.get("systemId").toString()
						+ "&locale="
						+ ("c".equals(user.getLanguage()) ? "zh_CN" : "en_US"));
				item.put("root", tempUrl);
				subSysList.add(item);
			}
			Map pub = new HashMap();
			pub.put("key", "");
			pub.put("value", "ȫ��");
			// sys.add(0, pub);
			subSysList.add(0, pub);
		} catch (Exception e) {
			log.error(e);
		}
		return subSysList;
	}

	/**
	 * <p>
	 * ��������: getUserInfos|����:ȡ���û���¼��Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public Object[] getUserInfos() {
		LoginDO user = getUser();
		Map info = new HashMap();
		info.put("��ϵ��ַ", user.getAddress());
		info.put("��ϵ�绰", user.getMobile());
		info.put("��������", user.getEmail());
		info.put("IP��ַ", user.getIp());
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
	 * ��������: getNotices|����:ȡ�ò������͵�ͨ����Ϣ
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
	 * ��������: getPswUDTime|����:ȡ���û�δ�޸���������
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
			// ������������Բ��õ����޸Ŀ�
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
						log.error("���������ʾ�������ݸ�ʽ��Ч,ʹ��Ĭ�����ã�5��", e);
					}

					if (l <= frequencyTime && l > 0) {
						return "����" + l + "�����ڣ��뼰ʱ�޸�!"; // ����3����ڣ��뼰ʱ�޸�
					}
					if (l <= 0) {
						return "T";
						/*
						 * String userUpdate
						 * =cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_TIMING_USER_UPDATE);
						 * if(userUpdate!=null&&userUpdate.equals("0")){ }
						 * return "�����ѹ���"; //�����ѹ��ڡ�
						 */}
					return "";
				}
				/*
				 * else{ l = n - d; l = (long) Math.ceil((double) l / r);
				 * if(l>=30){ return "���Ѿ�"+l+"��û���޸������ˣ�" ; //���Ѿ�����30��û���޸������ˣ�
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
				try { // �޸�����󣬱�������޸�����
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
	 * ��������: isEditPwd|����: �û��Ƿ�����޸�����
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEditPwd() {
		if (!checkPswIsUsing())
			return true;
		// ����������ȥ��ֻ��һ���޸�һ����ʾ
		if (true)
			return true;

		boolean b = true;
		LoginDO user = getUser();
		if (user.getIsFirstLogin() == null || user.getIsFirstLogin().equals("")
				|| user.getIsFirstLogin().equals("0")) // �״ε�¼�����޸�
			b = true;
		else {
			if (user.getLastModifyDate() != null) {
				long d = user.getLastModifyDate().getTime();
				long n = new Date().getTime();
				long t = n - n % r;
				if (t < d && d < t + r) // �Ƿ����޸Ĺ�
					b = false;
			}
		}
		return b;
	}

	/* �Ƿ�Ϊ����������ù������� */
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
	 * ����Ƿ������������ modify by wangxin �޸Ĵӻ����ȡ
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
	 * ��������: updatePWD|����: �޸�����
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
			// ������
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
		pwd = HexUtils.shaHex(pwd, login.getUserId()); // ����
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
			user.setLastModifyDate(d); // ��¼����޸�ʱ��
			user.setIsFirstLogin("1"); // ����Ϊ���״ε�¼
			userService.update(user); // ��������
			UBaseHisUserPwdDO hup = new UBaseHisUserPwdDO();
			hup.setId(new Long(d.getTime()));
			hup.setUserId(user.getUserId());
			hup.setPassword(pwd);
			hup.setModifyTime(d);
			userService.save(hup); // �����޸ļ�¼
			login.setLastModifyDate(d);
			login.setIsFirstLogin("1");
			login.setDescription("��������");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
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
	 * ��������: refreshCache|����:ˢ�»���
	 * </p>
	 * 
	 * @return
	 */
	public boolean refreshCache() {
		try {

			// ˢ��������ʽ��Ϣ
			UBaseSysParamDO param = (UBaseSysParamDO) noticeService
					.getObjectById(UBaseSysParamDO.class, new Long(1),
							"paramId");
			if (param != null) {
				WebContextFactory.get().getSession().setAttribute(
						Constants.SESSION_THEME_KEY, param.getSelectedValue());
			}
			// ˢ�²˵�
			systemCache.runCacheRegister();

			try {
				usysTask.afterPropertiesSetRefresh();
			} catch (Exception e) {
				log.error("��ʱ��ˢ���쳣��" + e.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * ��������: resetPWD|����: ��������
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

			login.setDescription("�����û�:"
					+ AuthorityNameFetcher.fetchUserName(user.getUserId())
					+ " ����");

			// ����������ԣ��޸�Ϊ�״ε�½��Ҫ�����޸�����
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
						"��������");
				message = "���óɹ�,�����ͨ������Ч";
			} else {
				userService.update(user);
				message = "���óɹ�";
			}
			login.addDescription("," + message);

			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription("��������");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
			if (e instanceof AuditException) {
				return "����ʧ��," + e.getMessage();
			}
			return "����ʧ��";
		}
		return message;
	}

	/**
	 * <p>
	 * ��������: enabled|����: ͣ�����û��˻�
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
				msg = "�����û��˻�  ";
				message = "�Ѿ������˻�";
			} else if (isable.equals("0")) {
				user.setEnabled("0");
				msg = "ͣ���û��˻�  ";
				message = "�Ѿ�ͣ���˻�";
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
				message = message + ",�����ͨ������Ч";
			} else {
				userService.update(user);
			}
			login.addDescription("," + message);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription(msg);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return msg + "ʧ��," + e.getMessage();
			}
			return msg + "ʧ��";
		}
		return message;
	}

	/**
	 * <p>
	 * ��������: enabled|����: ͣ�����û��˻�
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
				msg = "���������  ";
				message = "�Ѿ����������";
			} else if (isable.equals("0")) {
				user.setIsList("0");
				msg = "�Ӱ�������ȥ��  ";
				message = "�Ѿ��Ӱ�������ȥ��";
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
				message = message + ",�����ͨ������Ч";
			} else {
				userService.update(user);
			}
			login.addDescription("," + message);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog1);
		} catch (Exception e) {
			log.error(e);
			login.setDescription(msg);
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return msg + "ʧ��," + e.getMessage();
			}
			return msg + "ʧ��";
		}
		return message;
	}

	/**
	 * <p>
	 * ��������: removeLock|����: �������
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
				login.setDescription("��������û�:"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				if ("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))) {
					UserChangingService userChangingService = (UserChangingService) SpringContextUtils
							.getBean("userChangingService");
					UBaseUserDO original = userService.getUserByUserId(user
							.getUserId());
					userChangingService.saveBaseChanges(login, user, original,
							"�û�����");
					message = "�����Ѿ����,�����ͨ������Ч";
				} else {
					userService.update(user);
					message = "�����Ѿ����";
				}
				login.addDescription("," + message);
				UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(
						login, "ɾ��", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog1.setMenuId("0002.0002");
				sysLog1.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog1);
			}
		} catch (Exception e) {
			log.error(e);
			login.setDescription("�������");
			UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(login,
					"ɾ��", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog1.setMenuId("0002.0002");
			sysLog1.setMenuName("������Ϣ����.�û�����");
			try {
				this.sysLogService.saveUBaseSysLog(sysLog1);
			} catch (Exception e1) {
				log.error(e);
			}
			if (e instanceof AuditException) {
				return "����ʧ��," + e.getMessage();
			}
			return "����ʧ��";
		}
		return message;
	}

	/**
	 * <p>
	 * ��������: isSysAdmin|����: ����Ƿ�ɹ���ͨ��
	 * </p>
	 * 
	 * @return
	 */
	public boolean isSysAdmin() {
		LoginDO login = getUser();
		String hql = "from UAuthRoleResourceDO r,UAuthRoleUserDO a where r.objectId=a.roleId and a.userId='"
				+ login.getUserId()
				+ "' and r.resId='34' and r.resDetailName like '%ͨ�����%'";
		List list = userService.find(hql);
		if (list != null && !list.isEmpty())
			return true;
		return false;
	}

	/* ȡ������¼���� */
	private int getLimit(long l) {
		int s = l == 2 ? 5 : 15; // Ĭ��
		try {
			UBaseSysParamDO p = (UBaseSysParamDO) noticeService.getObjectById(
					UBaseSysParamDO.class, new Long(l), "paramId");
			if (p != null && p.getSelectedValue() != null
					&& !p.getSelectedValue().equals("")) {
				int n = Integer.parseInt(p.getSelectedValue());
				if (n > 0) // ����Ϊ������
					s = n;
			}
		} catch (NumberFormatException e) {
			s = l == 2 ? 5 : 15;
			e.printStackTrace();
		}
		return s;
	}

	/* ȡ�õ�ǰ�û� */
	private LoginDO getUser() {
		HttpSession session = WebContextFactory.get().getSession();
		LoginDO user = (LoginDO) session.getAttribute("LOGIN_USER");
		return user;
	}

	/**
	 * <p>
	 * ��������: getLogoutAlertInfo|����:ȡ��ע����ʾ��Ϣ
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
	 * ��������: getLogoutAlertInfo|����:ȡ��ע����ʾ��Ϣ
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
	 * �ж��û��Ƿ�ʧЧ����ʱ
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
			s = "�����ɹ�";
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
			s = "�����ɹ�";
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
	 * ���������û�
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
			login.setDescription("�����û�"
					+ AuthorityNameFetcher.fetchUserName(uid));
			UBaseSysLogDO log = this.sysLogService.setUBaseSysLog(login, "ɾ��",
					"1", Constants.BASE_SYS_LOG_AUTHORITY);
			log.setMenuId("0002.0002");
			log.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(log);
		} catch (Exception e) {
			LogFactory.getLog(this.getClass()).error("�����û�����", e);
			login.setDescription("�����û�" + uid);
			UBaseSysLogDO log = this.sysLogService.setUBaseSysLog(login, "ɾ��",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			log.setMenuId("0002.0002");
			log.setMenuName("������Ϣ����.�û�����");
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