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
 * ��Ȩ����:(C)2003-2010
 * </p>
 * 
 * @����: yuanshihong
 * @����: 2009-6-23 ����10:37:32
 * @����: [SubSystemAction]������ϵͳ��Ϣ��ص�����
 */
public class SubSystemAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/** ��ϵͳ��ѡ���ͼ���б� */
	private List subSystemIcons;

	/** ��ϵͳҵ����� */
	private SubSystemService subSystemService;

	/** ��־���� */
	private UBaseSysLogService sysLogService;

	/** �༭,����ʱ��Ӧ����ϵͳʵ�� */
	private UBaseConfigDO subSystem;

	/** ��ϵͳ�б��Ӧ���б����� */
	private List baseConfigList;

	/** ��ϵͳ���б����ݵĸ�ѡ��ѡ�� */
	private String[] checkItems;

	/** ��ϵͳ��������ѯ���� */
	private String conditionSystemCname;

	/** ��ϵͳӢ������ѯ���� */
	private String conditionSystemEname;

	/** �ϴ����ļ� */
	private File uploadfile;

	/** �ϴ����ļ����� */
	private String uploadfileFileName;

	/** ���ݿ����� */
	private List dbTypes;

	/** ��ϵͳ�Ĳ������� add,edit,view */
	private String operType;

	/** ϵͳ����Ա�б� */
	private List systemAdmins;

	/** �����û��б� */
	private List allUsers;

	/** �༭��Ĺ���Ա�б� */
	private List newAdmins;

	/** ��ϵͳ��ʾ��ʽ�б� */
	private List subSysDisplayList;

	private String roleId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	private CacheManager cacheManager; // ����

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
	 * ��������: createSystem|����: ����������ϵͳ������
	 * </p>
	 * 
	 * @return ��ϵͳ������ͼ
	 */
	public String createSystem() {
		subSystem = null;
		this.setOperType("add");
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: editSystem|����: ����༭��ϵͳ������
	 * </p>
	 * 
	 * @return ��ϵͳ�༭��ͼ
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
	 * ��������: editSystem|����: ����༭��ϵͳ������
	 * </p>
	 * 
	 * @return ��ϵͳ�༭��ͼ
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
	 * ��������: saveSystem|����: ��������ϵͳ����
	 * </p>
	 * 
	 * @return ��ϵͳ�б���ͼ
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
					user.setDescription("������ϵͳ: [" + subSystem.getSystemCname()
							+ "]");
				} else if (operType.equalsIgnoreCase("edit")) {
					user.setDescription("�޸���ϵͳ: [" + subSystem.getSystemCname()
							+ "]");

					if (subSystemService
							.modifiedSystem(subSystem.getSystemId())) {
						this.setResultMessages("�޸�ʧ��,�Ѿ�����ͬ��ϵͳ["
								+ subSystem.getSystemCname() + "]�������");
					} else {
						subSystemService.saveChange(subSystem, user);
						this.setResultMessages("����ɹ�,�����ͨ������Ч!");
					}

				}

				// UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user,
				// "����","1",Constants.BASE_SYS_LOG_AUTHORITY);
				// sysLog.setMenuId("0001.0001");
				// sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
				// sysLogService.saveUBaseSysLog(sysLog);
				// /*logManagerService.writeLog(user,
				// Constants.BASE_SYS_LOG_BASEINFO, "1");*/

			} else {
				if (operType.equalsIgnoreCase("add")) {
					subSystemService.save(subSystem);
					user.setDescription("������ϵͳ: [" + subSystem.getSystemCname()
							+ "]");
				} else if (operType.equalsIgnoreCase("edit")) {
					user.setDescription("�޸���ϵͳ: [" + subSystem.getSystemCname()
							+ "]");
					subSystemService.update(subSystem);
				}
				UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "����",
						"1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0001.0001");
				sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
				sysLogService.saveUBaseSysLog(sysLog);
				/*
				 * logManagerService.writeLog(user,
				 * Constants.BASE_SYS_LOG_BASEINFO, "1");
				 */

				this.setResultMessages("����ɹ�!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			if (operType.equalsIgnoreCase("add")) {
				user.setDescription("������ϵͳ: [" + subSystem.getSystemCname()
						+ "]ʱ������ʧ��!");
			} else if (operType.equalsIgnoreCase("edit")) {
				user.setDescription("�޸���ϵͳ: [" + subSystem.getSystemCname()
						+ "]ʱ������ʧ��!");
			}
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "����",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */

			this.setResultMessages("����ʧ��!");
		}
		return listSystem();
	}

	/**
	 * <p>
	 * ��������: upload|����: �ϴ���ϵͳͼ���ļ�
	 * </p>
	 */
	private void upload() {
		if (null == uploadfile || StringUtils.isEmpty(uploadfile.getName())) {
			return;
		}
		subSystem.setMenuImgSrc(this.uploadfileFileName);
		File destFile = new File(request.getRealPath("/image/system/"
				+ subSystem.getMenuImgSrc()));

		// ���Ŀ���ļ�����,����������һ���µ��ļ���
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
	 * ��������: deleteSystem|����: ����ɾ����ϵͳ������
	 * </p>
	 * 
	 * @return ��ϵͳ�б���ͼ
	 */
	public String deleteSystem() {
		try {
			subSystemService.deletes(this.checkItems);

			// ɾ����־
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("ɾ������ϵͳ: [" + this.checkItems + "]");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "ɾ��",
					"1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "1");
			 */
			this.setResultMessages("ɾ���ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("ɾ������ϵͳ: [" + this.checkItems + "]ʱ��ɾ��ʧ��!");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "ɾ��",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */
			this.setResultMessages("ɾ��ʧ��!");
		}

		return listSystem();
	}

	/**
	 * <p>
	 * ��������: listSystem|����: ������ϵͳ�б��ѯ����
	 * </p>
	 * 
	 * @return ��ϵͳ�б���ͼ
	 */
	public String listSystem() {
		baseConfigList = subSystemService.querySubSystems(conditionSystemEname,
				conditionSystemCname);
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: setAdmin|����: �������ù���Ա������
	 * </p>
	 * 
	 * @return ����Ա������ͼ
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
	 * ��������: saveAdmin|����: ���������Ա������Ϣ������
	 * </p>
	 * 
	 * @return ��ϵͳ�б���ͼ
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
					this.setResultMessages("����ʧ��,�Ѿ�����ͬ��ϵͳ[" + cname
							+ "]����Ա�������");
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
				// ���ù���Ա��־
				user.setDescription("������ϵͳ["
						+ AuthorityNameFetcher.fetchSystemName(subSystem
								.getSystemId()) + "]�Ĺ���Ա");

				if (CollectionUtils.isEmpty(newAdmins))
					newAdmins = Collections.EMPTY_LIST;
				user.addDescription("[").addDescription(
						new LoggingUserDifference("����Ա", olds, newAdmins))
						.addDescription("]");
				subSystemService.saveAdmins(subSystem.getSystemId(), newAdmins);
				UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "����",
						"1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0001.0001");
				sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
				sysLogService.saveUBaseSysLog(sysLog);
				/*
				 * logManagerService.writeLog(user,
				 * Constants.BASE_SYS_LOG_BASEINFO, "1");
				 */
				this.setResultMessages("����ɹ�!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("������ϵͳ["
					+ AuthorityNameFetcher.fetchSystemName(subSystem
							.getSystemId()) + "]�Ĺ���Աʱ������ʧ��!");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(user, "����",
					"0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0001.0001");
			sysLog.setMenuName("��ϵͳ��Ϣ����.��ϵͳ��Ϣ����");
			sysLogService.saveUBaseSysLog(sysLog);
			/*
			 * logManagerService.writeLog(user, Constants.BASE_SYS_LOG_BASEINFO,
			 * "0");
			 */
			this.setResultMessages("����ʧ��!");
		}

		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: setSubSystemIcons|����: ����ϵͳͼ���б�
	 * </p>
	 * 
	 * @param subSystemIcons
	 *            ϵͳͼ���б�
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
	 * ��������: getSubSystemService|����: ��ȡ��ϵͳҵ����
	 * </p>
	 * 
	 * @return ��ϵͳҵ����
	 */
	public SubSystemService getSubSystemService() {
		return subSystemService;
	}

	/**
	 * <p>
	 * ��������: setSubSystemService|����: ������ϵͳҵ����
	 * </p>
	 * 
	 * @param subSystemService
	 *            ��ϵͳҵ����
	 */
	public void setSubSystemService(SubSystemService subSystemService) {
		this.subSystemService = subSystemService;
	}

	/**
	 * <p>
	 * ��������: getSubSystem|����: ��ȡ��ǰ��������ϵͳ����
	 * </p>
	 * 
	 * @return ��ϵͳ����
	 */
	public UBaseConfigDO getSubSystem() {
		return subSystem;
	}

	/**
	 * <p>
	 * ��������: setSubSystem|����: ���õ�ǰ��������ϵͳ����
	 * </p>
	 * 
	 * @param subSystem
	 *            ��ϵͳ����
	 */
	public void setSubSystem(UBaseConfigDO subSystem) {
		this.subSystem = subSystem;
	}

	/**
	 * <p>
	 * ��������: getBaseConfigList|����: ��ȡ��ϵͳ�б�
	 * </p>
	 * 
	 * @return ��ϵͳ�б�
	 */
	public List getBaseConfigList() {
		return baseConfigList;
	}

	/**
	 * <p>
	 * ��������: setBaseConfigList|����: ������ϵͳ�б�
	 * </p>
	 * 
	 * @param baseConfigList
	 *            ��ϵͳ�б�
	 */
	public void setBaseConfigList(List baseConfigList) {
		this.baseConfigList = baseConfigList;
	}

	/**
	 * <p>
	 * ��������: getNewAdmins|����: ��ȡ�༭��Ĺ���Ա�б�
	 * </p>
	 * 
	 * @return ����Ա�б�
	 */
	public List getNewAdmins() {
		return newAdmins;
	}

	/**
	 * <p>
	 * ��������: setNewAdmins|����: ���ñ༭��Ĺ���Ա�б�
	 * </p>
	 * 
	 * @param newAdmins
	 *            ����Ա�б�
	 */
	public void setNewAdmins(List newAdmins) {
		this.newAdmins = newAdmins;
	}

	/**
	 * <p>
	 * ��������: getSystemAdmins|����: ��ȡ���еĵ�ǰ��ϵͳ����Ա�û�
	 * </p>
	 * 
	 * @return ���еĵ�ǰ��ϵͳ����Ա�û�
	 */
	public List getSystemAdmins() {
		return systemAdmins;
	}

	/**
	 * <p>
	 * ��������: setSystemAdmins|����: �������еĵ�ǰ��ϵͳ����Ա�û�
	 * </p>
	 * 
	 * @param systemAdmins
	 *            ���еĵ�ǰ��ϵͳ����Ա�û�
	 */
	public void setSystemAdmins(List systemAdmins) {
		this.systemAdmins = systemAdmins;
	}

	/**
	 * <p>
	 * ��������: getAllUsers|����: ��ȡ���еķǵ�ǰ��ϵͳ����Ա�û�
	 * </p>
	 * 
	 * @return ���еķǵ�ǰ��ϵͳ����Ա�û�
	 */
	public List getAllUsers() {
		return allUsers;
	}

	/**
	 * <p>
	 * ��������: setAllUsers|����: �������еķǵ�ǰ��ϵͳ����Ա�û�
	 * </p>
	 * 
	 * @param allUsers
	 *            ���еķǵ�ǰ��ϵͳ����Ա�û�
	 */
	public void setAllUsers(List allUsers) {
		this.allUsers = allUsers;
	}

	/**
	 * <p>
	 * ��������: getUploadfileFileName|����: ��ȡ�ϴ��ļ�������
	 * </p>
	 * 
	 * @return �ϴ��ļ�������
	 */
	public String getUploadfileFileName() {
		return uploadfileFileName;
	}

	/**
	 * <p>
	 * ��������: setUploadfileFileName|����: �����ϴ��ļ�������
	 * </p>
	 * 
	 * @param uploadfileFileName
	 *            �ϴ��ļ�������
	 */
	public void setUploadfileFileName(String uploadfileFileName) {
		this.uploadfileFileName = uploadfileFileName;
	}

	/**
	 * <p>
	 * ��������: getDbTypes|����: ��ȡ��ϵͳ�����ݿ������б�
	 * </p>
	 * 
	 * @return ���ݿ������б�
	 */
	public List getDbTypes() {
		if (null == dbTypes) {
			dbTypes = subSystemService.queryDbTypes();
		}
		return dbTypes;
	}

	/**
	 * <p>
	 * ��������: setDbTypes|����: ������ϵͳ�����ݿ������б�
	 * </p>
	 * 
	 * @param dbTypes
	 *            ���ݿ������б�
	 */
	public void setDbTypes(List dbTypes) {
		this.dbTypes = dbTypes;
	}

	/**
	 * <p>
	 * ��������: getUploadfile|����: ��ȡ�ϴ��ļ�
	 * </p>
	 * 
	 * @return �ϴ����ļ�
	 */
	public File getUploadfile() {
		return uploadfile;
	}

	/**
	 * <p>
	 * ��������: setUploadfile|����: �����ϴ��ļ�
	 * </p>
	 * 
	 * @param uploadfile
	 *            �ϴ����ļ�
	 */
	public void setUploadfile(File uploadfile) {
		this.uploadfile = uploadfile;
	}

	/**
	 * <p>
	 * ��������: getOperType|����: ��ȡ��ϵͳ�Ĳ�������
	 * </p>
	 * 
	 * @return ��ϵͳ�Ĳ�������
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * <p>
	 * ��������: setOperType|����: ������ϵͳ�Ĳ�������
	 * </p>
	 * 
	 * @param operType
	 *            ��ϵͳ�Ĳ�������
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * <p>
	 * ��������: getConditionSystemCname|����: ��ȡ��ϵͳ�������ƵĲ�ѯ����
	 * </p>
	 * 
	 * @return ��ϵͳ�������ƵĲ�ѯ����
	 */
	public String getConditionSystemCname() {
		return conditionSystemCname;
	}

	/**
	 * <p>
	 * ��������: setConditionSystemCname|����: ������ϵͳ�������ƵĲ�ѯ����
	 * </p>
	 * 
	 * @param conditionSystemCname
	 *            ��ϵͳ�������ƵĲ�ѯ����
	 */
	public void setConditionSystemCname(String conditionSystemCname) {
		this.conditionSystemCname = conditionSystemCname;
	}

	/**
	 * <p>
	 * ��������: getConditionSystemEname|����: ��ȡ��ϵͳӢ�����ƵĲ�ѯ����
	 * </p>
	 * 
	 * @return ��ϵͳӢ�����ƵĲ�ѯ����
	 */
	public String getConditionSystemEname() {
		return conditionSystemEname;
	}

	/**
	 * <p>
	 * ��������: setConditionSystemEname|����: ������ϵͳӢ�����ƵĲ�ѯ����
	 * </p>
	 * 
	 * @param conditionSystemEname
	 *            ��ϵͳӢ�����ƵĲ�ѯ����
	 */
	public void setConditionSystemEname(String conditionSystemEname) {
		this.conditionSystemEname = conditionSystemEname;
	}

	/**
	 * <p>
	 * ��������: getCheckItems|����: ��ȡ��ϵͳ�б��ѡ����
	 * </p>
	 * 
	 * @return ѡ�е���ϵͳID����
	 */
	public String[] getCheckItems() {
		return checkItems;
	}

	/**
	 * <p>
	 * ��������: setCheckItems|����: ������ϵͳ�б��ѡ����
	 * </p>
	 * 
	 * @param checkItems
	 *            ѡ�е���ϵͳID����
	 */
	public void setCheckItems(String[] checkItems) {
		this.checkItems = checkItems;
	}

	/**
	 * <p>
	 * ��������: getSubSystemIcons|����: ��ȡϵͳͼ���б�
	 * </p>
	 * 
	 * @return ϵͳͼ���б�
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
			s1.setValue("ȫ��ʾ");
			subSysDisplayList.add(s1);
			SelectTag s2 = new SelectTag();
			s2.setKey("menu");
			s2.setValue("���˵���ʾ");
			subSysDisplayList.add(s2);
			SelectTag s3 = new SelectTag();
			s3.setKey("left");
			s3.setValue("������������ʾ");
			subSysDisplayList.add(s3);
			SelectTag s4 = new SelectTag();
			s4.setKey("false");
			s4.setValue("����ʾ");
			subSysDisplayList.add(s4);
		}
		return subSysDisplayList;
	}

	public void setSubSysDisplayList(List subSysDisplayList) {
		this.subSysDisplayList = subSysDisplayList;
	}
}
