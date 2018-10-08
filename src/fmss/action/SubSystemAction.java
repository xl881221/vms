package fmss.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import fmss.action.base.SelectTag;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.services.AuthorityNameFetcher;
import fmss.services.LoggingUserDifference;
import fmss.services.SubSystemService;
import fmss.services.UBaseSysLogService;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: yuanshihong
 * @日期: 2009-6-23 上午10:37:32
 * @描述: [SubSystemAction]处理子系统信息相关的请求
 */
public class SubSystemAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/** 子系统待选择的图标列表 */
	private List subSystemIcons;

	/** 子系统业务对象 */
	private SubSystemService subSystemService;

	/** 日志服务 */
	private UBaseSysLogService sysLogService;

	/** 编辑,新增时对应的子系统实体 */
	private UBaseConfigDO subSystem;

	/** 子系统列表对应的列表数据 */
	private List baseConfigList;

	/** 子系统的列表数据的复选框选项 */
	private String[] checkItems;

	/** 子系统中文名查询条件 */
	private String conditionSystemCname;

	/** 子系统英文名查询条件 */
	private String conditionSystemEname;

	/** 上传的文件 */
	private File uploadfile;

	/** 上传的文件名称 */
	private String uploadfileFileName;

	/** 数据库类型 */
	private List dbTypes;

	/** 子系统的操作类型 add,edit,view */
	private String operType;

	/** 系统管理员列表 */
	private List systemAdmins;

	/** 所有用户列表 */
	private List allUsers;

	/** 编辑后的管理员列表 */
	private List newAdmins;

	/** 子系统显示方式列表 */
	private List subSysDisplayList;

	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	private CacheManager cacheManager; // 缓存

	private String isGrant;

	public String getIsGrant() {
		return isGrant;
	}

	public void setIsGrant(String isGrant) {
		this.isGrant = isGrant;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * <p>
	 * 方法名称: createSystem|描述: 处理新增子系统的请求
	 * </p>
	 * 
	 * @return 子系统新增视图
	 */
	public String createSystem() {
		subSystem = null;
		this.setOperType("add");
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: editSystem|描述: 处理编辑子系统的请求
	 * </p>
	 * 
	 * @return 子系统编辑视图
	 */
	public String editSystem() {
		if (null != subSystem && !StringUtils.isEmpty(subSystem.getSystemId())) {
			subSystem = (UBaseConfigDO) this.subSystemService
					.getBaseConfigBySystemId(subSystem.getSystemId());
			subSysDisplayList = this.getSubSysDisplayList();
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: editSystem|描述: 处理编辑子系统的请求
	 * </p>
	 * 
	 * @return 子系统编辑视图
	 */
	public String viewSystem() {
		if (null != subSystem && !StringUtils.isEmpty(subSystem.getSystemId())) {
			subSystem = (UBaseConfigDO) this.subSystemService
					.getBaseConfigBySystemId(subSystem.getSystemId());
		}
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: saveSystem|描述: 处理保存子系统请求
	 * </p>
	 * 
	 * @return 子系统列表视图
	 */
	public String saveSystem() {
		try {

			String regex = "^[^/]+://[^/]+(:[^/]+)?/[^/]+/";
			String linkSiteUrl = subSystem.getLinkSiteUrl().replace('\\', '/');
			if (linkSiteUrl.lastIndexOf("/") != linkSiteUrl.length() - 1) {
				linkSiteUrl += "/";
			}
			String linkSiteInnerUrl = subSystem.getLinkSiteInnerUrl().replace(
					'\\', '/');
			if (linkSiteInnerUrl.lastIndexOf("/") != linkSiteInnerUrl.length() - 1) {
				linkSiteInnerUrl += "/";
			}
			String dbUrl = linkSiteInnerUrl
					+ subSystem.getDbUrl().replace('\\', '/').replaceAll(regex,
							"");
			String unitLoginUrl = linkSiteUrl
					+ subSystem.getUnitLoginUrl().replace('\\', '/')
							.replaceAll(regex, "");
			String unitLoginInnerUrl = linkSiteInnerUrl
					+ subSystem.getUnitLoginUrl().replace('\\', '/')
							.replaceAll(regex, "");
			subSystem.setLinkSiteUrl(linkSiteUrl);
			subSystem.setDbUrl(dbUrl);
			subSystem.setUnitLoginUrl(unitLoginUrl);
			subSystem.setUnitLoginInnerUrl(unitLoginInnerUrl);

			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			if ("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
					&& "1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT))) {
				if (operType.equalsIgnoreCase("add")) {
					subSystemService.save(subSystem);
					user.setDescription("新增子系统: [" + subSystem.getSystemCname()
							+ "]");
				} else if (operType.equalsIgnoreCase("edit")) {
					user.setDescription("修改子系统: [" + subSystem.getSystemCname()
							+ "]");

					if (subSystemService
							.modifiedSystem(subSystem.getSystemId())) {
						this.setResultMessages("修改失败,已经有相同子系统["
								+ subSystem.getSystemCname() + "]在审核中");
					} else {
						subSystemService.saveChange(subSystem, user);
						this.setResultMessages("保存成功,待审核通过后生效!");
					}

				}

				// UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user,
				// "保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
				// sysLog.setMenuId("0001.0001");
				// sysLog.setMenuName("子系统信息管理.子系统信息配置");
				// sysLogService.saveUBaseSysLog(sysLog);
				// /*logManagerService.writeLog(user,
				// Constants.BASE_SYS_LOG_BASEINFO, "1");*/

			} else {
				if (operType.equalsIgnoreCase("add")) {
					subSystemService.save(subSystem);
					user.setDescription("新增子系统: [" + subSystem.getSystemCname()
							+ "]");
				} else if (operType.equalsIgnoreCase("edit")) {
					user.setDescription("修改子系统: [" + subSystem.getSystemCname()
							+ "]");
					subSystemService.update(subSystem);
				}
				UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "保存",
						"1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0001.0001");
				sysLog.setMenuName("子系统信息管理.子系统信息配置");
				sysLogService.saveUBaseSysLog(sysLog);
				/*
				 * logManagerService.writeLog(user,
				 * Constants.BASE_SYS_LOG_BASEINFO, "1");
				 */

				this.setResultMessages("保存成功!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			if (operType.equalsIgnoreCase("add")) {
				user.setDescription("新增子系统: [" + subSystem.getSystemCname()
						+ "]时，保存失败!");
			} else if (operType.equalsIgnoreCase("edit")) {
				user.setDescription("修改子系统: [" + subSystem.getSystemCname()
						+ "]时，保存失败!");
			}
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "保存",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("子系统信息管理.子系统信息配置");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */

			this.setResultMessages("保存失败!");
		}
		return listSystem();
	}

	/**
	 * <p>
	 * 方法名称: upload|描述: 上传子系统图标文件
	 * </p>
	 */
	private void upload() {
		if (null == uploadfile || StringUtils.isEmpty(uploadfile.getName())) {
			return;
		}
		subSystem.setMenuImgSrc(this.uploadfileFileName);
		File destFile = new File(request.getRealPath("/image/system/"
				+ subSystem.getMenuImgSrc()));

		// 如果目标文件存在,则重新生成一个新的文件名
		if (destFile.exists()) {
			String fileExtention = ".gif";
			if (uploadfile.getName().lastIndexOf(".") > 0) {
				fileExtention = uploadfileFileName
						.substring(this.uploadfileFileName.lastIndexOf("."));
			}
			subSystem.setMenuImgSrc(RandomStringUtils.randomAlphanumeric(25)
					+ fileExtention);
			destFile = new File(request.getRealPath("/image/system/"
					+ subSystem.getMenuImgSrc()));
		}
		;

		try {
			FileUtils.copyFile(uploadfile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: deleteSystem|描述: 处理删除子系统的请求
	 * </p>
	 * 
	 * @return 子系统列表视图
	 */
	public String deleteSystem() {
		try {
			subSystemService.deletes(this.checkItems);

			// 删除日志
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("删除了子系统: [" + this.checkItems + "]");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "删除",
					"1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("子系统信息管理.子系统信息配置");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "1");
			 */
			this.setResultMessages("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("删除了子系统: [" + this.checkItems + "]时，删除失败!");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "删除",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("子系统信息管理.子系统信息配置");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */
			this.setResultMessages("删除失败!");
		}

		return listSystem();
	}

	/**
	 * <p>
	 * 方法名称: listSystem|描述: 处理子系统列表查询请求
	 * </p>
	 * 
	 * @return 子系统列表视图
	 */
	public String listSystem() {
		baseConfigList = subSystemService.querySubSystems(conditionSystemEname,
				conditionSystemCname);
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: setAdmin|描述: 处理设置管理员的请求
	 * </p>
	 * 
	 * @return 管理员配置视图
	 */
	public String setAdmin() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		systemAdmins = subSystemService.queryAdmins(subSystem.getSystemId(),
				user.getInstId());
		allUsers = subSystemService.queryAllUsers(subSystem.getSystemId(), user
				.getInstId());
		return SUCCESS;
	}

	public String showOwnSubSystemChanges() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		systemAdmins = subSystemService.queryAdminsChange(roleId, user
				.getInstId());
		allUsers = subSystemService.queryAllUsersChange(roleId, user
				.getInstId());
		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: saveAdmin|描述: 处理保存管理员设置信息的请求
	 * </p>
	 * 
	 * @return 子系统列表视图
	 */
	public String saveAdmin() {
		try {
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			List l = subSystemService.queryAdmins(subSystem.getSystemId(), user
					.getInstId());
			List olds = new ArrayList();
			if ("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
					&& "1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT))) {
				for (Iterator iterator = l.iterator(); l != null
						&& iterator.hasNext();) {
					java.util.Map map = (java.util.Map) iterator.next();
					olds.add(map.get("userId"));
				}
				String msg = "";
				if (subSystemService.modifiedAdmin(subSystem.getSystemId())) {
					String cname = subSystemService.getSystemCname(subSystem
							.getSystemId());
					this.setResultMessages("保存失败,已经有相同子系统[" + cname
							+ "]管理员在审核中");
				} else {
					msg = subSystemService.saveAdminsChange(subSystem
							.getSystemId(), newAdmins, user, olds);
					this.setResultMessages(msg);
				}

			} else {
				for (Iterator iterator = l.iterator(); l != null
						&& iterator.hasNext();) {
					java.util.Map map = (java.util.Map) iterator.next();
					olds.add(map.get("userId"));
				}
				// 配置管理员日志
				user.setDescription("设置子系统["
						+ AuthorityNameFetcher.fetchSystemName(subSystem
								.getSystemId()) + "]的管理员");

				if (CollectionUtils.isEmpty(newAdmins))
					newAdmins = Collections.EMPTY_LIST;
				user.addDescription("[").addDescription(
						new LoggingUserDifference("管理员", olds, newAdmins))
						.addDescription("]");
				subSystemService.saveAdmins(subSystem.getSystemId(), newAdmins);
				UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "保存",
						"1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0001.0001");
				sysLog.setMenuName("子系统信息管理.子系统信息配置");
				sysLogService.saveUBaseSysLog(sysLog);
				/*
				 * logManagerService.writeLog(user,
				 * Constants.BASE_SYS_LOG_BASEINFO, "1");
				 */
				this.setResultMessages("保存成功!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("设置子系统["
					+ AuthorityNameFetcher.fetchSystemName(subSystem
							.getSystemId()) + "]的管理员时，保存失败!");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "保存",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("子系统信息管理.子系统信息配置");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */
			this.setResultMessages("保存失败!");
		}

		return SUCCESS;
	}

	/**
	 * <p>
	 * 方法名称: setSubSystemIcons|描述: 设置系统图标列表
	 * </p>
	 * 
	 * @param subSystemIcons
	 *            系统图标列表
	 */
	public void setSubSystemIcons(List subSystemIcons) {
		this.subSystemIcons = subSystemIcons;
	}

	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	/**
	 * <p>
	 * 方法名称: getSubSystemService|描述: 获取子系统业务类
	 * </p>
	 * 
	 * @return 子系统业务类
	 */
	public SubSystemService getSubSystemService() {
		return subSystemService;
	}

	/**
	 * <p>
	 * 方法名称: setSubSystemService|描述: 设置子系统业务类
	 * </p>
	 * 
	 * @param subSystemService
	 *            子系统业务类
	 */
	public void setSubSystemService(SubSystemService subSystemService) {
		this.subSystemService = subSystemService;
	}

	/**
	 * <p>
	 * 方法名称: getSubSystem|描述: 获取当前操作的子系统对象
	 * </p>
	 * 
	 * @return 子系统对象
	 */
	public UBaseConfigDO getSubSystem() {
		return subSystem;
	}

	/**
	 * <p>
	 * 方法名称: setSubSystem|描述: 设置当前操作的子系统对象
	 * </p>
	 * 
	 * @param subSystem
	 *            子系统对象
	 */
	public void setSubSystem(UBaseConfigDO subSystem) {
		this.subSystem = subSystem;
	}

	/**
	 * <p>
	 * 方法名称: getBaseConfigList|描述: 获取子系统列表
	 * </p>
	 * 
	 * @return 子系统列表
	 */
	public List getBaseConfigList() {
		return baseConfigList;
	}

	/**
	 * <p>
	 * 方法名称: setBaseConfigList|描述: 设置子系统列表
	 * </p>
	 * 
	 * @param baseConfigList
	 *            子系统列表
	 */
	public void setBaseConfigList(List baseConfigList) {
		this.baseConfigList = baseConfigList;
	}

	/**
	 * <p>
	 * 方法名称: getNewAdmins|描述: 获取编辑后的管理员列表
	 * </p>
	 * 
	 * @return 管理员列表
	 */
	public List getNewAdmins() {
		return newAdmins;
	}

	/**
	 * <p>
	 * 方法名称: setNewAdmins|描述: 设置编辑后的管理员列表
	 * </p>
	 * 
	 * @param newAdmins
	 *            管理员列表
	 */
	public void setNewAdmins(List newAdmins) {
		this.newAdmins = newAdmins;
	}

	/**
	 * <p>
	 * 方法名称: getSystemAdmins|描述: 获取所有的当前子系统管理员用户
	 * </p>
	 * 
	 * @return 所有的当前子系统管理员用户
	 */
	public List getSystemAdmins() {
		return systemAdmins;
	}

	/**
	 * <p>
	 * 方法名称: setSystemAdmins|描述: 设置所有的当前子系统管理员用户
	 * </p>
	 * 
	 * @param systemAdmins
	 *            所有的当前子系统管理员用户
	 */
	public void setSystemAdmins(List systemAdmins) {
		this.systemAdmins = systemAdmins;
	}

	/**
	 * <p>
	 * 方法名称: getAllUsers|描述: 获取所有的非当前子系统管理员用户
	 * </p>
	 * 
	 * @return 所有的非当前子系统管理员用户
	 */
	public List getAllUsers() {
		return allUsers;
	}

	/**
	 * <p>
	 * 方法名称: setAllUsers|描述: 设置所有的非当前子系统管理员用户
	 * </p>
	 * 
	 * @param allUsers
	 *            所有的非当前子系统管理员用户
	 */
	public void setAllUsers(List allUsers) {
		this.allUsers = allUsers;
	}

	/**
	 * <p>
	 * 方法名称: getUploadfileFileName|描述: 获取上传文件的名称
	 * </p>
	 * 
	 * @return 上传文件的名称
	 */
	public String getUploadfileFileName() {
		return uploadfileFileName;
	}

	/**
	 * <p>
	 * 方法名称: setUploadfileFileName|描述: 设置上传文件的名称
	 * </p>
	 * 
	 * @param uploadfileFileName
	 *            上传文件的名称
	 */
	public void setUploadfileFileName(String uploadfileFileName) {
		this.uploadfileFileName = uploadfileFileName;
	}

	/**
	 * <p>
	 * 方法名称: getDbTypes|描述: 获取子系统的数据库类型列表
	 * </p>
	 * 
	 * @return 数据库类型列表
	 */
	public List getDbTypes() {
		if (null == dbTypes) {
			dbTypes = subSystemService.queryDbTypes();
		}
		return dbTypes;
	}

	/**
	 * <p>
	 * 方法名称: setDbTypes|描述: 设置子系统的数据库类型列表
	 * </p>
	 * 
	 * @param dbTypes
	 *            数据库类型列表
	 */
	public void setDbTypes(List dbTypes) {
		this.dbTypes = dbTypes;
	}

	/**
	 * <p>
	 * 方法名称: getUploadfile|描述: 获取上传文件
	 * </p>
	 * 
	 * @return 上传的文件
	 */
	public File getUploadfile() {
		return uploadfile;
	}

	/**
	 * <p>
	 * 方法名称: setUploadfile|描述: 设置上传文件
	 * </p>
	 * 
	 * @param uploadfile
	 *            上传的文件
	 */
	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	/**
	 * <p>
	 * 方法名称: getOperType|描述: 获取子系统的操作类型
	 * </p>
	 * 
	 * @return 子系统的操作类型
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * <p>
	 * 方法名称: setOperType|描述: 设置子系统的操作类型
	 * </p>
	 * 
	 * @param operType
	 *            子系统的操作类型
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * <p>
	 * 方法名称: getConditionSystemCname|描述: 获取子系统中文名称的查询条件
	 * </p>
	 * 
	 * @return 子系统中文名称的查询条件
	 */
	public String getConditionSystemCname() {
		return conditionSystemCname;
	}

	/**
	 * <p>
	 * 方法名称: setConditionSystemCname|描述: 设置子系统中文名称的查询条件
	 * </p>
	 * 
	 * @param conditionSystemCname
	 *            子系统中文名称的查询条件
	 */
	public void setConditionSystemCname(String conditionSystemCname) {
		this.conditionSystemCname = conditionSystemCname;
	}

	/**
	 * <p>
	 * 方法名称: getConditionSystemEname|描述: 获取子系统英文名称的查询条件
	 * </p>
	 * 
	 * @return 子系统英文名称的查询条件
	 */
	public String getConditionSystemEname() {
		return conditionSystemEname;
	}

	/**
	 * <p>
	 * 方法名称: setConditionSystemEname|描述: 设置子系统英文名称的查询条件
	 * </p>
	 * 
	 * @param conditionSystemEname
	 *            子系统英文名称的查询条件
	 */
	public void setConditionSystemEname(String conditionSystemEname) {
		this.conditionSystemEname = conditionSystemEname;
	}

	/**
	 * <p>
	 * 方法名称: getCheckItems|描述: 获取子系统列表的选中项
	 * </p>
	 * 
	 * @return 选中的子系统ID数组
	 */
	public String[] getCheckItems() {
		return checkItems;
	}

	/**
	 * <p>
	 * 方法名称: setCheckItems|描述: 设置子系统列表的选中项
	 * </p>
	 * 
	 * @param checkItems
	 *            选中的子系统ID数组
	 */
	public void setCheckItems(String[] checkItems) {
		this.checkItems = checkItems;
	}

	/**
	 * <p>
	 * 方法名称: getSubSystemIcons|描述: 获取系统图标列表
	 * </p>
	 * 
	 * @return 系统图标列表
	 */
	public List getSubSystemIcons() {
		if (null == subSystemIcons) {
			subSystemIcons = new ArrayList();
			File iconDir = new File(request.getRealPath("/image/system/"));
			Iterator files = FileUtils.listFiles(iconDir,
					new String[] { "gif" }, false).iterator();
			while (files.hasNext()) {
				subSystemIcons.add(((File) files.next()).getName());
			}
		}
		return subSystemIcons;
	}

	public List getSubSysDisplayList() {
		if (subSysDisplayList == null) {
			subSysDisplayList = new ArrayList();
			SelectTag s1 = new SelectTag();
			s1.setKey("true");
			s1.setValue("全显示");
			subSysDisplayList.add(s1);
			SelectTag s2 = new SelectTag();
			s2.setKey("menu");
			s2.setValue("仅菜单显示");
			subSysDisplayList.add(s2);
			SelectTag s3 = new SelectTag();
			s3.setKey("left");
			s3.setValue("待处理问题显示");
			subSysDisplayList.add(s3);
			SelectTag s4 = new SelectTag();
			s4.setKey("false");
			s4.setValue("不显示");
			subSysDisplayList.add(s4);
		}
		return subSysDisplayList;
	}

	public void setSubSysDisplayList(List subSysDisplayList) {
		this.subSysDisplayList = subSysDisplayList;
	}
}
