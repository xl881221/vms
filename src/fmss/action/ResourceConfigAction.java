package fmss.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fmss.action.base.UserChangingService;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthResMapDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UAuthRoleUserDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseDictionaryDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.services.AuthRoleService;
import fmss.services.AuthorityNameFetcher;
import fmss.services.DictionaryService;
import fmss.services.LoggingRoleDifference;
import fmss.services.ResourceConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:13:35
 * @描述: [ResourceConfigAction]资源配置Action
 */
public class ResourceConfigAction extends BaseAction {

	private static final String APPEND_MESSAGE = "，待审核通过后生效 !";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** logger 日志对象 */
	private static final Logger logger = Logger
			.getLogger(ResourceConfigAction.class);

	/** authRoleService 角色服务 */
	private AuthRoleService authRoleService;

	/** resourceConfigService 资源配置服务 */
	private ResourceConfigService resourceConfigService;

	/** dicService 字典服务 */
	private DictionaryService dicService;

	/** sysLogService 日志服务 */
	private UBaseSysLogService sysLogService;

	/** id 参数1 树id */
	private String id = null;
	
	private String readOnly=null;

	/** value 参数 字符串值 */
	private String value = null;

	/** authResMap 参数2 资源 */
	private UAuthResMapDO authResMap;

	/** user 用户 */
	private UBaseUserDO user;

	/** role 角色 */
	private UAuthRoleDO role = new UAuthRoleDO();

	/** uBaseConfig 子系统信息 */
	private UBaseConfigDO uBaseConfig = new UBaseConfigDO();

	/** resTypeDic 资源类型字典 */
	private List resTypeDic;

	/** resMapList 资源list */
	private List resMapList;

	/** resMapCtn 资源列表Map */
	private Map resMapCtn;

	/** resMapKey 资源列表字段名 */
	private Map resMapKey;



	/** 资源名 */
	private List resNames;

	/** 用户角色列表 */
	private String[] userRoles;

	/** 角色列表 */
	private List allRoles;

	private String selectTab;
	
	private UserChangingService userChangingService;//用户修改审核service
	private CacheManager cacheManager; // 缓存

	public String getSelectTab() {
		return selectTab;
	}

	public void setSelectTab(String selectTab) {
		this.selectTab = selectTab;
	}

	/**
	 * @return allRoles
	 */
	public List getAllRoles() {
		return allRoles;
	}

	/**
	 * @param allRoles
	 *            要设置的 allRoles
	 */
	public void setAllRoles(List allRoles) {
		this.allRoles = allRoles;
	}

	/**
	 * <p>
	 * 方法名称: listResHead|描述:配置视图head
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String listResHead() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: listResFrm|描述:资源配置frame
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String listResFrm() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: listResMain|描述:主页面
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String listResMain() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: listResTreeUser|描述:用户树
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String listResTreeUser() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: listResTreeRole|描述:角色树
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String listResTreeRole() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: getResTreeUser|描述:获取用户树
	 * </p>
	 */
	public void getResTreeUser() {
		// 树的xml字符串
		String s = this.authRoleService.getUserTreeAsynXml(this.id);
		logger.info(s);
		// 参数置空
		this.id = "";
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(s);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: getResTreeRole|描述:获取角色树
	 * </p>
	 */
	public void getResTreeRole() {
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// 取用户的子系统列表
		List systems = this.authRoleService.getBaseConfig(user);
		// 树的xml字符串

		// modify by wangxin 由于更换TREE组件，所以修改xml格式
		String s = this.authRoleService.getRoleTreeSyncXmlEx(systems, this.id,user.getUserId());
		logger.info(s);
		// 参数置空
		this.id = "";
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(s);
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * <p>
	 * 方法名称: getResTreeRole|描述:获取角色树
	 * </p>
	 */
	public void getResZTreeRole() {
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// 取用户的子系统列表
		List systems = this.authRoleService.getBaseConfig(user);
		// 树的xml字符串

		// modify by wangxin 由于更换TREE组件，所以修改xml格式
		List s = this.authRoleService.getRoleZTreeSyncXmlEx(systems, this.id,user.getUserId());
		logger.info(s);
		// 参数置空
		this.id = "";
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(JSONArray.fromObject(s).toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			try{
				this.response.getWriter().close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	// add by fanwenyu 2009-12-02
	public String initResByRole() {
		try {
			// 清空条件
			this.authResMap = null;
			this.paginationList.setCurrentPage(1);
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// 取资源类型字典
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "全部", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// 取资源名列表,带角色子系统条件
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);

			// 根据角色获取资源list

			this.resMapList = this.resourceConfigService.getHavResByRole(
					this.role, this.uBaseConfig, this.paginationList);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: viewResByRole|描述:查看当前角色所有资源
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String viewResByRole() {
		try {
			// 清空条件

			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// 取资源类型字典
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "全部", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// 取资源名列表,带角色子系统条件
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);
			// add by fanwenyu 2009-12-02 翻页时记录上次的authResMap
			// 根据角色获取资源list
			if (this.authResMap == null) {
				this.resMapList = this.resourceConfigService.getHavResByRole(
						this.role, this.uBaseConfig, this.paginationList);
			} else {
				this.resMapList = this.resourceConfigService.getHavResByRole(
						this.role, this.authResMap, this.uBaseConfig,
						this.paginationList);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: queryResByRole|描述:查看当前角色所有资源
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String queryResByRole() {
		try {
			this.paginationList.setCurrentPage(1);
			// 取资源类型字典
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "全部", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// 取资源名列表,带角色子系统条件
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);
			// 根据角色获取资源list
			this.resMapList = this.resourceConfigService.getHavResByRole(
					this.role, this.authResMap, this.uBaseConfig,
					this.paginationList);
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: saveResByRole|描述:保存配置当前角色资源
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String saveResByRole() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("保存角色资源信息" + ",角色[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "]");
		try {
			// 保存配置
			
			/****************用户修改审核*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = this.userChangingService.saveResourceChanges(user, this.role.getRoleId(), this.value);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s) ;
				} else {
					this.setResultMessages("保存成功" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "保存成功" + APPEND_MESSAGE);
			}
			/****************用户修改审核*******************/
			else{
				this.resourceConfigService.saveConfigRes(user, this.value, this.role
					.getRoleId());
				this.setResultMessages("保存成功！");
			}
			 // 操作成功不提示
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("基础信息管理.角色资源管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			
			return SUCCESS;
		} catch (Exception e) {
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("基础信息管理.角色资源管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			this.setResultMessages("操作失败");
			log.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: delAllResByRole|描述:删除指定的资源-批量删除
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String delAllResByRole() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("删除角色资源信息" + ",角色[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "]");
		try {
			// 删除资源
			/****************用户修改审核*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveResourceChanges(user, this.role.getRoleId(), this.resMapList);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s);
					user.addDescription("," + s);
				} else {
					this.setResultMessages("删除成功" + APPEND_MESSAGE);
					user.addDescription("," + APPEND_MESSAGE);
				}
				
			}
			/****************用户修改审核*******************/
			else{
				this.resourceConfigService.deleteAllRes(user, this.resMapList, this.role
					.getRoleId());
				this.setResultMessages("删除成功");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("基础信息管理.角色资源管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		} catch (Exception e) {
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("基础信息管理.角色资源管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("", e);
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: showResTreeByRole|描述:显示没有配置的资源树
	 * </p>
	 */
	public String showResTreeByRole() {
		try {
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			this.resNames = this.resourceConfigService.getResMap(uBaseConfig);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: getResTreeXMLRole|描述:调用没有配置的资源树的xml串
	 * </p>
	 */
	public void getResTreeXMLRole() {
		// 树的xml字符串
		try {
			// 特殊考虑系统管理的公共角色，可以共享所有资源
			// String s = this.resourceConfigService.getResTreeByRole(this.role,
			// this.uBaseConfig);
			
			Map defaultUrlInfo = new HashMap();
			defaultUrlInfo.put(Constants.URL_IP, request.getSession()
					.getAttribute("defaultIPAddress").toString());

			defaultUrlInfo.put(Constants.URL_PORT, request.getSession()
					.getAttribute("defaultPort").toString());
			role = authRoleService.getRoleByRoleId(role.getRoleId());
//			String s = this.resourceConfigService.getResTreeXmlEx(this.role,
//					this.uBaseConfig, this.authResMap, defaultUrlInfo);
			
			String s = this.resourceConfigService.getResTreeListEx(this.role,
					this.uBaseConfig, this.authResMap, defaultUrlInfo);

			logger.info(s);
			// 参数置空
			this.id = "";
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			try{
				this.response.getWriter().close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public String checkRoleByUser() {
		try {
			// 取用户信息
			this.user = this.authRoleService.getUserByUserId(this.user
					.getUserId());
			// 获取角色列表
			List userRoles = this.authRoleService.getRoleByUser(this.user);
			List roleIds = new ArrayList();
			for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO role = (UAuthRoleUserDO) iterator.next();
				roleIds.add(role.getRoleId());
			}

			this.userRoles = (String[]) roleIds.toArray(new String[0]);

			// 根据登录用户获取子系统列表
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			UBaseUserDO userLogin = new UBaseUserDO();
			userLogin.setUserId(loginUser.getUserId());

			this.allRoles = this.resourceConfigService.getAllRoles(userLogin);
			if (allRoles.size() > 0) {
				String str = ((Map) allRoles.get(0)).keySet().toArray()[0]
						.toString();
				setSelectTab(str);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: viewResByUser|描述:查看当前用户所有资源
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String viewRoleByUser() {
		try {
			// 取用户信息
			this.user = this.authRoleService.getUserByUserId(this.user
					.getUserId());
			// 获取角色列表
			List userRoles = this.authRoleService.getRoleByUser(this.user);
			List roleIds = new ArrayList();
			for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO role = (UAuthRoleUserDO) iterator.next();
				roleIds.add(role.getRoleId());
			}

			this.userRoles = (String[]) roleIds.toArray(new String[0]);

			// 根据登录用户获取子系统列表
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			UBaseUserDO userLogin = new UBaseUserDO();
			userLogin.setUserId(loginUser.getUserId());

			this.allRoles = this.resourceConfigService.getAllRoles(userLogin);
			if (allRoles.size() > 0) {
				String str = ((Map) allRoles.get(0)).keySet().toArray()[0]
						.toString();
				setSelectTab(str);
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * 方法名称: saveResByUser|描述:保存配置当前用户资源
	 * </p>
	 * 
	 * @return 成功页面
	 */
	public String saveRoleByUser() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("配置人员角色信息" + ",用户["+ AuthorityNameFetcher.fetchUserName(this.user.getUserId())+"]");
		try {
			List l = authRoleService.getRoleByUserRangeBySystem(this.user, user);
			List olds = new ArrayList();
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO o = (UAuthRoleUserDO) iterator.next();
				if(!olds.contains(o.getRoleId())) olds.add(o.getRoleId());
			}
			user.addDescription(",[").addDescription(
					new LoggingRoleDifference("角色", (String[]) olds.toArray(new String[] {}), userRoles))
					.addDescription("]");
			
			/****************用户修改审核*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.user, this.userRoles);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s);
				} else {
					this.setResultMessages("保存成功" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "保存成功" + APPEND_MESSAGE);
			}
			/****************用户修改审核*******************/
			else{
				// 保存配置,可以增加，删除
				this.resourceConfigService
					.saveConfigRole(this.user, this.userRoles, user);
				this.setResultMessages("保存成功！");
			}
			 // 操作成功不提示
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		} catch (Exception e) {
			this.setResultMessages("操作失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			log.error(e);
		}
		return ERROR;
	}

	/**
	 * @return 角色权限服务对象
	 */
	public AuthRoleService getAuthRoleService() {
		return authRoleService;
	}

	/**
	 * @param authRoleService
	 *            要设置的 角色权限服务对象
	 */
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

	/**
	 * @return 字典服务对象
	 */
	public DictionaryService getDicService() {
		return dicService;
	}

	/**
	 * @param dicService
	 *            要设置的 字典服务对象
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	/**
	 * @return 树ID参数
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 树ID参数
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return 资源信息
	 */
	public UAuthResMapDO getAuthResMap() {
		return authResMap;
	}

	/**
	 * @param authResMap
	 *            要设置的 资源信息
	 */
	public void setAuthResMap(UAuthResMapDO authResMap) {
		this.authResMap = authResMap;
	}

	/**
	 * @return 用户对象
	 */
	public UBaseUserDO getUser() {
		return user;
	}

	/**
	 * @param user
	 *            要设置的 用户对象
	 */
	public void setUser(UBaseUserDO user) {
		this.user = user;
	}

	/**
	 * @return 角色对象
	 */
	public UAuthRoleDO getRole() {
		return role;
	}

	/**
	 * @param role
	 *            要设置的 角色对象
	 */
	public void setRole(UAuthRoleDO role) {
		this.role = role;
	}

	/**
	 * @return 资源类型字典
	 */
	public List getResTypeDic() {
		return resTypeDic;
	}

	/**
	 * @param resTypeDic
	 *            要设置的 资源类型字典
	 */
	public void setResTypeDic(List resTypeDic) {
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return 资源信息列表
	 */
	public List getResMapList() {
		return resMapList;
	}

	/**
	 * @param resMapList
	 *            要设置的 资源信息列表
	 */
	public void setResMapList(List resMapList) {
		this.resMapList = resMapList;
	}

	/**
	 * @return uBaseConfig
	 */
	public UBaseConfigDO getUBaseConfig() {
		return uBaseConfig;
	}

	/**
	 * @param baseConfig
	 *            要设置的 uBaseConfig
	 */
	public void setUBaseConfig(UBaseConfigDO baseConfig) {
		uBaseConfig = baseConfig;
	}

	/**
	 * @return resourceConfigService
	 */
	public ResourceConfigService getResourceConfigService() {
		return resourceConfigService;
	}

	/**
	 * @param resourceConfigService
	 *            要设置的 resourceConfigService
	 */
	public void setResourceConfigService(
			ResourceConfigService resourceConfigService) {
		this.resourceConfigService = resourceConfigService;
	}

	/**
	 * @return resMapCtn
	 */
	public Map getResMapCtn() {
		return resMapCtn;
	}

	/**
	 * @param resMapCtn
	 *            要设置的 resMapCtn
	 */
	public void setResMapCtn(Map resMapCtn) {
		this.resMapCtn = resMapCtn;
	}

	/**
	 * @return resMapKey
	 */
	public Map getResMapKey() {
		return resMapKey;
	}

	/**
	 * @param resMapKey
	 *            要设置的 resMapKey
	 */
	public void setResMapKey(Map resMapKey) {
		this.resMapKey = resMapKey;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            要设置的 value
	 */
	public void setValue(String value) {
		this.value = value;
	}



	/**
	 * @return resNames
	 */
	public List getResNames() {
		return resNames;
	}

	/**
	 * @param resNames
	 *            要设置的 resNames
	 */
	public void setResNames(List resNames) {
		this.resNames = resNames;
	}

	/**
	 * @return userRoles
	 */
	public String[] getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles
	 *            要设置的 userRoles
	 */
	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return 日志服务对象
	 */
	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	/**
	 * @param sysLogService
	 *            要设置的 日志服务对象
	 */
	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public void setUserChangingService(UserChangingService userChangingService) {
		this.userChangingService = userChangingService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

}
