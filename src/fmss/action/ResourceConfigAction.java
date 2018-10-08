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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: sunzhan
 * @����: 2009-6-24 ����10:13:35
 * @����: [ResourceConfigAction]��Դ����Action
 */
public class ResourceConfigAction extends BaseAction {

	private static final String APPEND_MESSAGE = "�������ͨ������Ч !";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** logger ��־���� */
	private static final Logger logger = Logger
			.getLogger(ResourceConfigAction.class);

	/** authRoleService ��ɫ���� */
	private AuthRoleService authRoleService;

	/** resourceConfigService ��Դ���÷��� */
	private ResourceConfigService resourceConfigService;

	/** dicService �ֵ���� */
	private DictionaryService dicService;

	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;

	/** id ����1 ��id */
	private String id = null;
	
	private String readOnly=null;

	/** value ���� �ַ���ֵ */
	private String value = null;

	/** authResMap ����2 ��Դ */
	private UAuthResMapDO authResMap;

	/** user �û� */
	private UBaseUserDO user;

	/** role ��ɫ */
	private UAuthRoleDO role = new UAuthRoleDO();

	/** uBaseConfig ��ϵͳ��Ϣ */
	private UBaseConfigDO uBaseConfig = new UBaseConfigDO();

	/** resTypeDic ��Դ�����ֵ� */
	private List resTypeDic;

	/** resMapList ��Դlist */
	private List resMapList;

	/** resMapCtn ��Դ�б�Map */
	private Map resMapCtn;

	/** resMapKey ��Դ�б��ֶ��� */
	private Map resMapKey;



	/** ��Դ�� */
	private List resNames;

	/** �û���ɫ�б� */
	private String[] userRoles;

	/** ��ɫ�б� */
	private List allRoles;

	private String selectTab;
	
	private UserChangingService userChangingService;//�û��޸����service
	private CacheManager cacheManager; // ����

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
	 *            Ҫ���õ� allRoles
	 */
	public void setAllRoles(List allRoles) {
		this.allRoles = allRoles;
	}

	/**
	 * <p>
	 * ��������: listResHead|����:������ͼhead
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String listResHead() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: listResFrm|����:��Դ����frame
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String listResFrm() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: listResMain|����:��ҳ��
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String listResMain() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: listResTreeUser|����:�û���
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String listResTreeUser() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: listResTreeRole|����:��ɫ��
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String listResTreeRole() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * ��������: getResTreeUser|����:��ȡ�û���
	 * </p>
	 */
	public void getResTreeUser() {
		// ����xml�ַ���
		String s = this.authRoleService.getUserTreeAsynXml(this.id);
		logger.info(s);
		// �����ÿ�
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
	 * ��������: getResTreeRole|����:��ȡ��ɫ��
	 * </p>
	 */
	public void getResTreeRole() {
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// ȡ�û�����ϵͳ�б�
		List systems = this.authRoleService.getBaseConfig(user);
		// ����xml�ַ���

		// modify by wangxin ���ڸ���TREE����������޸�xml��ʽ
		String s = this.authRoleService.getRoleTreeSyncXmlEx(systems, this.id,user.getUserId());
		logger.info(s);
		// �����ÿ�
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
	 * ��������: getResTreeRole|����:��ȡ��ɫ��
	 * </p>
	 */
	public void getResZTreeRole() {
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// ȡ�û�����ϵͳ�б�
		List systems = this.authRoleService.getBaseConfig(user);
		// ����xml�ַ���

		// modify by wangxin ���ڸ���TREE����������޸�xml��ʽ
		List s = this.authRoleService.getRoleZTreeSyncXmlEx(systems, this.id,user.getUserId());
		logger.info(s);
		// �����ÿ�
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
			// �������
			this.authResMap = null;
			this.paginationList.setCurrentPage(1);
			// ȡ��ɫ��Ϣ
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "ȫ��", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// ȡ��Դ���б�,����ɫ��ϵͳ����
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);

			// ���ݽ�ɫ��ȡ��Դlist

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
	 * ��������: viewResByRole|����:�鿴��ǰ��ɫ������Դ
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String viewResByRole() {
		try {
			// �������

			// ȡ��ɫ��Ϣ
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "ȫ��", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// ȡ��Դ���б�,����ɫ��ϵͳ����
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);
			// add by fanwenyu 2009-12-02 ��ҳʱ��¼�ϴε�authResMap
			// ���ݽ�ɫ��ȡ��Դlist
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
	 * ��������: queryResByRole|����:�鿴��ǰ��ɫ������Դ
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String queryResByRole() {
		try {
			this.paginationList.setCurrentPage(1);
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "ȫ��", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// ȡ��Դ���б�,����ɫ��ϵͳ����
			this.resNames = this.resourceConfigService
					.getResMap(this.uBaseConfig);
			// ���ݽ�ɫ��ȡ��Դlist
			this.resMapList = this.resourceConfigService.getHavResByRole(
					this.role, this.authResMap, this.uBaseConfig,
					this.paginationList);
			// ȡ��ɫ��Ϣ
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
	 * ��������: saveResByRole|����:�������õ�ǰ��ɫ��Դ
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String saveResByRole() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("�����ɫ��Դ��Ϣ" + ",��ɫ[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "]");
		try {
			// ��������
			
			/****************�û��޸����*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = this.userChangingService.saveResourceChanges(user, this.role.getRoleId(), this.value);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s) ;
				} else {
					this.setResultMessages("����ɹ�" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "����ɹ�" + APPEND_MESSAGE);
			}
			/****************�û��޸����*******************/
			else{
				this.resourceConfigService.saveConfigRes(user, this.value, this.role
					.getRoleId());
				this.setResultMessages("����ɹ���");
			}
			 // �����ɹ�����ʾ
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("������Ϣ����.��ɫ��Դ����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			
			return SUCCESS;
		} catch (Exception e) {
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("������Ϣ����.��ɫ��Դ����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			this.setResultMessages("����ʧ��");
			log.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>
	 * ��������: delAllResByRole|����:ɾ��ָ������Դ-����ɾ��
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String delAllResByRole() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("ɾ����ɫ��Դ��Ϣ" + ",��ɫ[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "]");
		try {
			// ɾ����Դ
			/****************�û��޸����*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveResourceChanges(user, this.role.getRoleId(), this.resMapList);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s);
					user.addDescription("," + s);
				} else {
					this.setResultMessages("ɾ���ɹ�" + APPEND_MESSAGE);
					user.addDescription("," + APPEND_MESSAGE);
				}
				
			}
			/****************�û��޸����*******************/
			else{
				this.resourceConfigService.deleteAllRes(user, this.resMapList, this.role
					.getRoleId());
				this.setResultMessages("ɾ���ɹ�");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("������Ϣ����.��ɫ��Դ����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		} catch (Exception e) {
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0004");
			sysLog.setMenuName("������Ϣ����.��ɫ��Դ����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("", e);
		}
		return ERROR;
	}

	/**
	 * <p>
	 * ��������: showResTreeByRole|����:��ʾû�����õ���Դ��
	 * </p>
	 */
	public String showResTreeByRole() {
		try {
			// ȡ��ɫ��Ϣ
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
	 * ��������: getResTreeXMLRole|����:����û�����õ���Դ����xml��
	 * </p>
	 */
	public void getResTreeXMLRole() {
		// ����xml�ַ���
		try {
			// ���⿼��ϵͳ����Ĺ�����ɫ�����Թ���������Դ
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
			// �����ÿ�
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
			// ȡ�û���Ϣ
			this.user = this.authRoleService.getUserByUserId(this.user
					.getUserId());
			// ��ȡ��ɫ�б�
			List userRoles = this.authRoleService.getRoleByUser(this.user);
			List roleIds = new ArrayList();
			for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO role = (UAuthRoleUserDO) iterator.next();
				roleIds.add(role.getRoleId());
			}

			this.userRoles = (String[]) roleIds.toArray(new String[0]);

			// ���ݵ�¼�û���ȡ��ϵͳ�б�
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
	 * ��������: viewResByUser|����:�鿴��ǰ�û�������Դ
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String viewRoleByUser() {
		try {
			// ȡ�û���Ϣ
			this.user = this.authRoleService.getUserByUserId(this.user
					.getUserId());
			// ��ȡ��ɫ�б�
			List userRoles = this.authRoleService.getRoleByUser(this.user);
			List roleIds = new ArrayList();
			for (Iterator iterator = userRoles.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO role = (UAuthRoleUserDO) iterator.next();
				roleIds.add(role.getRoleId());
			}

			this.userRoles = (String[]) roleIds.toArray(new String[0]);

			// ���ݵ�¼�û���ȡ��ϵͳ�б�
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
	 * ��������: saveResByUser|����:�������õ�ǰ�û���Դ
	 * </p>
	 * 
	 * @return �ɹ�ҳ��
	 */
	public String saveRoleByUser() {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("������Ա��ɫ��Ϣ" + ",�û�["+ AuthorityNameFetcher.fetchUserName(this.user.getUserId())+"]");
		try {
			List l = authRoleService.getRoleByUserRangeBySystem(this.user, user);
			List olds = new ArrayList();
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				UAuthRoleUserDO o = (UAuthRoleUserDO) iterator.next();
				if(!olds.contains(o.getRoleId())) olds.add(o.getRoleId());
			}
			user.addDescription(",[").addDescription(
					new LoggingRoleDifference("��ɫ", (String[]) olds.toArray(new String[] {}), userRoles))
					.addDescription("]");
			
			/****************�û��޸����*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.user, this.userRoles);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages(s);
				} else {
					this.setResultMessages("����ɹ�" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "����ɹ�" + APPEND_MESSAGE);
			}
			/****************�û��޸����*******************/
			else{
				// ��������,�������ӣ�ɾ��
				this.resourceConfigService
					.saveConfigRole(this.user, this.userRoles, user);
				this.setResultMessages("����ɹ���");
			}
			 // �����ɹ�����ʾ
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		} catch (Exception e) {
			this.setResultMessages("����ʧ��");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			log.error(e);
		}
		return ERROR;
	}

	/**
	 * @return ��ɫȨ�޷������
	 */
	public AuthRoleService getAuthRoleService() {
		return authRoleService;
	}

	/**
	 * @param authRoleService
	 *            Ҫ���õ� ��ɫȨ�޷������
	 */
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

	/**
	 * @return �ֵ�������
	 */
	public DictionaryService getDicService() {
		return dicService;
	}

	/**
	 * @param dicService
	 *            Ҫ���õ� �ֵ�������
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	/**
	 * @return ��ID����
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            Ҫ���õ� ��ID����
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return ��Դ��Ϣ
	 */
	public UAuthResMapDO getAuthResMap() {
		return authResMap;
	}

	/**
	 * @param authResMap
	 *            Ҫ���õ� ��Դ��Ϣ
	 */
	public void setAuthResMap(UAuthResMapDO authResMap) {
		this.authResMap = authResMap;
	}

	/**
	 * @return �û�����
	 */
	public UBaseUserDO getUser() {
		return user;
	}

	/**
	 * @param user
	 *            Ҫ���õ� �û�����
	 */
	public void setUser(UBaseUserDO user) {
		this.user = user;
	}

	/**
	 * @return ��ɫ����
	 */
	public UAuthRoleDO getRole() {
		return role;
	}

	/**
	 * @param role
	 *            Ҫ���õ� ��ɫ����
	 */
	public void setRole(UAuthRoleDO role) {
		this.role = role;
	}

	/**
	 * @return ��Դ�����ֵ�
	 */
	public List getResTypeDic() {
		return resTypeDic;
	}

	/**
	 * @param resTypeDic
	 *            Ҫ���õ� ��Դ�����ֵ�
	 */
	public void setResTypeDic(List resTypeDic) {
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return ��Դ��Ϣ�б�
	 */
	public List getResMapList() {
		return resMapList;
	}

	/**
	 * @param resMapList
	 *            Ҫ���õ� ��Դ��Ϣ�б�
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
	 *            Ҫ���õ� uBaseConfig
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
	 *            Ҫ���õ� resourceConfigService
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
	 *            Ҫ���õ� resMapCtn
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
	 *            Ҫ���õ� resMapKey
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
	 *            Ҫ���õ� value
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
	 *            Ҫ���õ� resNames
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
	 *            Ҫ���õ� userRoles
	 */
	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return ��־�������
	 */
	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	/**
	 * @param sysLogService
	 *            Ҫ���õ� ��־�������
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
