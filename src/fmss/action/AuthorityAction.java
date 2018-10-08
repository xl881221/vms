package fmss.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import fmss.action.base.UserChangingService;
import fmss.common.cache.CacheManager;
import fmss.common.db.IdGenerator;
import fmss.common.util.Constants;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthObjectDO;
import fmss.dao.entity.UAuthResMapDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.services.AuthObjectService;
import fmss.services.AuthRoleService;
import fmss.services.AuthorityNameFetcher;
import fmss.services.DictionaryService;
import fmss.services.LogManagerService;
import fmss.services.UBaseSysLogService;
import fmss.services.UserService;


/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����10:13:53
 * @����: [AuthorityAction]��ɫȨ�޹���Action
 */
public class AuthorityAction extends BaseAction/* implements Preparable*/{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String APPEND_MESSAGE = "�������ͨ������Ч !";
	/** logger ��־���� */
	private static final Logger logger = Logger
			.getLogger(AuthorityAction.class);
	/** roleList ��ɫ�б� */
	private List roleList;
	/** authRoleResList ��ɫ��Դ�б� */
	private List authRoleResList;
	/** authObjList Ȩ�������б� */
	private List authObjList;// 
	/** userArray ��Ӧ�û��б����� */
	private String[] userArray;
	/** allUserArray ��Ӧ�û��б����� */
	private String[] allUserArray;
	/** userList ��Ӧ�û��б� */
	private List userList;
	/** allUserList �����û��б� */
	private List allUserList;
	/** allResMap ������Դ�б� */
	private List allResMap;
	/** resMap ��ɫ��Ӧ��Դ�б� */
	private List resMap;
	/** userSystemList �û���ϵͳ�б� */
	private List userSystemList;
	/** userRoleList �û���ɫ�б� */
	private List userRoleList;
	/** resMapArray ��ɫ��Ӧ��Դ���� */
	private String[] resMapArray;
	/** authRoleService ��ɫ���� */
	private AuthRoleService authRoleService;
	/** idGenerator id������ */
	private IdGenerator idGenerator = IdGenerator.getInstance(DictionaryService.AUTH_AOT_DIC_TYPE); // id������
	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;
	
	private LogManagerService logManagerService;
	
	/** dicService �ֵ���� */
	private DictionaryService dicService;
	/** userService �û����� */
	private UserService userService;
	/** authObjectService Ȩ��������� */
	private AuthObjectService authObjectService;
	/** role ��ɫ */
	private UAuthRoleDO role = new UAuthRoleDO();
	/** roleName ����1 */
	private String roleName;
	/** roleId ����2 */
	private String roleId;	
	/** formType �����ĸ�ҳ��*/
	private String returnView;
	/** inputStream ����3����� */
	private InputStream inputStream;
	/** id ����4 ��ϵͳid */
	private String id;
	/** sysList ��ϵͳ�б� */
	private List sysList;
	/** user ���� �û����ò�ѯ */
	private UBaseUserDO user;
	/** authResMap ���� ��Դ���ò�ѯ */
	private UAuthResMapDO authResMap;
	/** resTypeDic ��Դ�����ֵ� */
	private List resTypeDic;
	/** view ���� �鿴 */
	private boolean view = false;
	/** isSuccess �Ƿ�ɹ� */
	private String isSuccess;

	/** FORM_AUTH ��תҳ�� */
	protected static final String FORM_AUTH = "formAuth";
	/** VIEW_AUTH_ROLE_RES ��תҳ�� */
	protected static final String VIEW_AUTH_ROLE_RES = "viewAuthRoleRes";
	
	private UserChangingService userChangingService;//�û��޸����service
	private CacheManager cacheManager; // ����
	
	private boolean fixQuery = false;
	public boolean isFixQuery() {
		return fixQuery;
	}

	public void setFixQuery(boolean fixQuery) {
		this.fixQuery = fixQuery;
	}

	/**
	 * @return ��ɫ�б�
	 */
	public List getRoleList(){
		return roleList;
	}

	/**
	 * @param roleList Ҫ���õ� ��ɫ�б�
	 */
	public void setRoleList(List roleList){
		this.roleList = roleList;
	}

	/**
	 * @return ��ɫ�������
	 */
	public AuthRoleService getAuthRoleService(){
		return authRoleService;
	}

	/**
	 * @param authRoleService Ҫ���õ� ��ɫ�������
	 */
	public void setAuthRoleService(AuthRoleService authRoleService){
		this.authRoleService = authRoleService;
	}

	/**
	 * @return ��ɫ��
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 * @param roleName Ҫ���õ� ��ɫ��
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	/**
	 * <p>��������: listAuth|����:��ɫ�б� </p>
	 * @return �ɹ�ҳ��
	 */
	public String listAuth(){
		try{
			// ȡ�����н�ɫ��Ϣ
			if(StringUtils.isNotBlank(this.roleName)){
				this.roleList = this.authRoleService.getRoleById(this.roleName);
			}else{
				this.roleList = this.authRoleService.getAllRole();
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: createAuth|����:�½���ɫ </p>
	 * @return �ɹ�ҳ��
	 */
	public String createAuth(){
		try{
			// ȡ��ϵͳ���    ���ݵ�¼�û�������ϵͳ
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = new UBaseUserDO();
			user.setUserId(loginUser.getUserId());
			// ȡ�û�����ϵͳ�б�
			this.sysList = this.authRoleService.getBaseConfig(user);
			
			return FORM_AUTH;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: editAuth|����:�༭��ɫ </p>
	 * @return �ɹ�ҳ��
	 */
	public String editAuth(){
		try{
			// ��ȡ��ɫ��Ϣ
			this.roleList = this.authRoleService.getRoleById(this.roleId);
			if(this.roleList != null && this.roleList.size() > 0){
				this.role = (UAuthRoleDO) this.authRoleService.getRoleById(
						this.roleId).get(0);
			}
			// ȡ��ϵͳ���   ���ݵ�¼�û�������ϵͳ
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = new UBaseUserDO();
			user.setUserId(loginUser.getUserId());
			// ȡ�û�����ϵͳ�б�
			this.sysList = this.authRoleService.getBaseConfig(user);
			
			this.setIsSuccess("update");
			return FORM_AUTH;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: viewAuthRoleRes|����:�鿴��ɫȨ����Դ�б� </p>
	 * @return �ɹ�ҳ��
	 */
	public String viewAuthRoleRes(){
		try{
			this.roleList = this.authRoleService.getRoleById(this.roleId);
			if(this.roleList != null && this.roleList.size() > 0){
				this.role = (UAuthRoleDO)roleList.get(0);
			}
			// ȡ�ý�ɫ��ӦȨ����Դ
			this.authRoleResList = this.authRoleService
					.getAuthRoleResourceByObjectId(this.roleId);
			return VIEW_AUTH_ROLE_RES;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: saveAuth|����:�����ɫ </p>
	 * @return �ɹ�ҳ��
	 * @throws Exception 
	 */
	public String saveAuth() throws Exception{
		try{
			List list = this.authRoleService.getRoleById(this.role.getRoleId());
			// ȡ��ϵͳ���
			this.sysList = this.authRoleService.getSystemList(false);
			if(list != null && list.size() > 0){
				this.setIsSuccess("saveNo");
			}else{
				// ��ʼ��id������
				long id = idGenerator.getNextKey();
				// ����Ȩ������
				UAuthObjectDO obj = new UAuthObjectDO();
				obj.setObjectId(String.valueOf(id));
				obj.setObjectName(this.role.getRoleName());
				obj.setObjectType(DictionaryService.ROLE_AOT_DIC_TYPE);
				this.authObjectService.save(obj);
				// �����ɫ
				this.role.setRoleId(String.valueOf(id));
				this.authRoleService.save(this.role);
				// ��־����
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("������ɫ:" + this.role.getRoleName());
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0003");
				sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				this.setIsSuccess("saveYes");
				this.setResultMessages( "����ɹ�");
				return SUCCESS;
			}
		}catch (Exception e){
			try {
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("������ɫ:" + this.role.getRoleName()+"ʱ������ʧ��");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0003");
				sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				logger.error("",e);
				setResultMessages("����ʧ��");
			} catch (Exception e1) {
				logger.error(e1);
				throw e1;
			}
		}
		return INPUT;
	}

	/**
	 * <p>��������: updateAuth|����:���½�ɫ</p>
	 * @return �ɹ�ҳ��
	 */
	public String updateAuth(){
		try{
			// ���½�ɫ
			this.authRoleService.update(this.role);
			// ��־����
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("�޸Ľ�ɫ:" + this.role.getRoleName());
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			// ȡ��ϵͳ���
			this.sysList = this.authRoleService.getSystemList(false);
			// ���ø��³ɹ���־
			this.setIsSuccess("updateYes");
			this.setResultMessages("���³ɹ�");
			return SUCCESS;
		}catch (Exception e){
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("���½�ɫ:" + this.role.getRoleName()+"ʱ������ʧ��");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
			this.setResultMessages("����ʧ��");
			this.setIsSuccess("updateNo");
		}
		return INPUT;
	}
	public String checkAuth(){
		try {
			List  list= authRoleService.getRole(this.role.getRoleId(),this.role.getSystemId(),URLDecoder.decode(this.role.getRoleName(), "utf-8"));
			if(list.size()>0){
				out2page("false");
			}else{
				out2page("true");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out2page("false");
		}
		
		return null;
	}

	/**
	 * <p>��������: deleteAuth|����:ɾ����ɫ </p>
	 * @return �ɹ�ҳ��
	 */
	public String deleteAuth(){
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("ɾ����ɫ��Ϣ[" + AuthorityNameFetcher.fetchRoleName(roleId) + "]");
			this.role.setRoleId(this.roleId);
			// ɾ����ɫ��Ӧ����Դ
			//this.authRoleService.deleteConfigRes(this.role);
			// ɾ����ɫ��Ӧ���û�
			//this.authRoleService.deleteConfigUser(this.role);
			
			//���ҽ�ɫ��Ӧ����Դ
			List res = authRoleService.getResByRole(this.role);
			// ���ҽ�ɫ��Ӧ���û�
			List users = authRoleService.getUserByRole(this.role);		 
				if(!((users!=null&&users.size()>0)||(res!=null&&res.size()>0))){
					// ɾ����ɫ
					this.authRoleService.delete(this.role);
					this.setResultMessages("ɾ���ɹ���");
					this.isSuccess = "success";
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"ɾ��","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0003");
					sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
					this.sysLogService.saveUBaseSysLog(sysLog);
					return "1".equals(returnView)?INPUT:SUCCESS;	//modify by wangxin 20091110 ����ҳ����ת�������� 
				}
		}catch (Exception e){
			logger.error("",e);
		}
		this.setResultMessages("��ɫɾ��ʧ��");
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("ɾ����ɫ��Ϣ[" + this.roleId + "]ʧ��");
		UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
				"ɾ��","0",Constants.BASE_SYS_LOG_AUTHORITY);
		sysLog.setMenuId("0002.0003");
		sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
		this.sysLogService.saveUBaseSysLog(sysLog);
		this.setResultMessages("ɾ��ʧ��,�ý�ɫ�ѷ���Ȩ�޻����ѷ�����û���");
		return "input";
	}
	
	
	

	/**
	 * <p>��������: viewUserByRole|����:�鿴�ý�ɫ�����������û� </p>
	 * @return �ɹ�ҳ��
	 */
	public String viewUserByRole(){
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// ȡ��ɫ��Ϣ
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// ���ݽ�ɫ��ȡ�û�
			this.userList = this.authRoleService.getUserByRole(this.role,
					this.paginationList,user.getInstId(),this.user,fixQuery);
			return SUCCESS;
		}catch (Exception e){
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>��������: configUserByRole|����:���ý�ɫ�û����� </p>
	 * @return �ɹ�ҳ��
	 */
	public String configUserByRole(){
		try{
			// ���ݽ�ɫ��ȡû�����õ��û�
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userList = this.authRoleService.getLevUserByRole(this.role,user.getInstId(),this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>��������: saveUserByRole|����:�����û����� </p>
	 * @return �ɹ�ҳ��
	 */
	public String saveUserByRole(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("�����ɫ��Ա��Ϣ,��ɫ[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "],�û�[" + StringUtils.join(AuthorityNameFetcher.fetchUserName(userList).iterator(), ",") + "]");
		try{
			/****************�û��޸����*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.role, this.userList, true);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages( s);
					user.addDescription("," + s);
				} else {
					this.setResultMessages( "����ɹ�" + APPEND_MESSAGE);
					user.addDescription("," + "����ɹ�" + APPEND_MESSAGE);
				}
				
			}
			/****************�û��޸����*******************/
			else{
				// ��������,�������ӣ�ɾ��
				this.authRoleService.saveConfigUser(this.userList, this.role);
				this.setResultMessages("����ɹ�");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>��������: deleteUserByRole|����:ɾ���û���ɫ��ϵ </p>
	 * @return �ɹ�ҳ��
	 */
	public String deleteUserByRole(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("ɾ����ɫ��Ա��Ϣ" + ",��ɫ[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "],�û�[" + StringUtils.join(AuthorityNameFetcher.fetchUserName(userList).iterator(), ",") + "]");
		try{
			/****************�û��޸����*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.role, this.userList, false);
				if (StringUtils.isNotEmpty(s)) {
					setResultMessages(s);
				} else {
					setResultMessages("ɾ���ɹ�" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "ɾ���ɹ�" + APPEND_MESSAGE);
			}
			/****************�û��޸����*******************/
			// ��������
			else{
				this.authRoleService.deleteConfigUser(this.userList, this.role);
				setResultMessages("ɾ���ɹ�");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"ɾ��","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"ɾ��","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("������Ϣ����.��ɫ��Ա����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>��������: listRoleHead|����:��ʾ��ɫ��ͼͷ�� </p>
	 * @return �ɹ�ҳ��
	 */
	public String listRoleHead(){
		return SUCCESS;
	}

	/**
	 * <p>��������: listRoleTree|����:��ʾ��ɫ�� </p>
	 * @return �ɹ�ҳ��
	 */
	public String listRoleTree(){
		return SUCCESS;
	}

	/**
	 * <p>��������: listRoleMain|����:��ʾ��ɫ��ͼ��ҳ�� </p>
	 * @return �ɹ�ҳ��
	 */
	public String listRoleMain(){
		return SUCCESS;
	}

	/**
	 * <p>��������: getRoleTree|����: ȡ��ɫ��xml</p>
	 */
	public void getRoleTree(){
		// ȡ��ϵͳ
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// ȡ�û�����ϵͳ�б�
		this.sysList = this.authRoleService.getBaseConfig(user);
		// modify by wangxin ���ڸ������ؼ����޸�xml�ṹ
		String s = this.authRoleService.getRoleTreeSyncXmlEx(this.sysList,
				this.id,user.getUserId());
		logger.debug(s);
		try{
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(s);
			this.response.getWriter().close();
		}catch (IOException ex){
			ex.printStackTrace();
		}
	}
	/**
	 * ��Ӹ÷�������Ϊ�˼���IE11�Ľ�ɫ��
	 * <p>��������: getRoleZTree|����: ȡ��ɫ��xml</p>
	 */
	public void getRoleZTree() {
		// ȡ��ϵͳ
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// ȡ�û�����ϵͳ�б�
		this.sysList = this.authRoleService.getBaseConfig(user);
		List s = this.authRoleService.getRoleZTreeSyncXmlEx(this.sysList,
				this.id,user.getUserId());
		logger.debug(s);
		try{
			this.response.setContentType("text/html; charset=utf-8");
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
	/**
	 * <p>��������: queryConfigUser|����:�������������û���ѯ </p>
	 * @return �ɹ�ҳ��
	 */
	public String queryConfigUser(){
		try{
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			// ȡ��ɫ��Ϣ
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// ���û����������������û���ѯ
			this.userList = this.authRoleService.getHavUserByRole(this.user,
					this.role,loginUser.getInstId());
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/*
	 * <p></p>
	 * @return �ɹ�ҳ��
	 * */
	public String queryConfigUserLev(){
		try{
			// ȡ��ɫ��Ϣ
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// ���û����������������û���ѯ
			this.userList = this.authRoleService.getHavLevUserByRole(this.user,
					this.role,this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * @return ��ɫ��Ӧ��Դ�б�
	 */
	public List getAuthRoleResList(){
		return authRoleResList;
	}

	/**
	 * @param authRoleResList Ҫ���õ� ��ɫ��Ӧ��Դ�б�
	 */
	public void setAuthRoleResList(List authRoleResList){
		this.authRoleResList = authRoleResList;
	}

	/**
	 * @return Ȩ�������б�
	 */
	public List getAuthObjList(){
		return authObjList;
	}

	/**
	 * @param authObjList Ҫ���õ� Ȩ�������б�
	 */
	public void setAuthObjList(List authObjList){
		this.authObjList = authObjList;
	}

	/**
	 * @return �û�����
	 */
	public String[] getUserArray(){
		return userArray;
	}

	/**
	 * @param userArray Ҫ���õ� �û�����
	 */
	public void setUserArray(String[] userArray){
		this.userArray = userArray;
	}

	/**
	 * @return �����û�����
	 */
	public String[] getAllUserArray(){
		return allUserArray;
	}

	/**
	 * @param allUserArray Ҫ���õ� �����û�����
	 */
	public void setAllUserArray(String[] allUserArray){
		this.allUserArray = allUserArray;
	}

	/**
	 * @return �û��б�
	 */
	public List getUserList(){
		return userList;
	}

	/**
	 * @param userList Ҫ���õ� �û��б�
	 */
	public void setUserList(List userList){
		this.userList = userList;
	}

	/**
	 * @return �����û��б�
	 */
	public List getAllUserList(){
		return allUserList;
	}

	/**
	 * @param allUserList Ҫ���õ� �����û��б�
	 */
	public void setAllUserList(List allUserList){
		this.allUserList = allUserList;
	}

	/**
	 * @return ������Դ��Ϣ
	 */
	public List getAllResMap(){
		return allResMap;
	}

	/**
	 * @param allResMap Ҫ���õ� ������Դ��Ϣ
	 */
	public void setAllResMap(List allResMap){
		this.allResMap = allResMap;
	}

	/**
	 * @return ��Դ��Ϣ
	 */
	public List getResMap(){
		return resMap;
	}

	/**
	 * @param resMap Ҫ���õ� ��Դ��Ϣ
	 */
	public void setResMap(List resMap){
		this.resMap = resMap;
	}

	/**
	 * @return �û���ϵͳ�б�
	 */
	public List getUserSystemList(){
		return userSystemList;
	}

	/**
	 * @param userSystemList Ҫ���õ� �û���ϵͳ�б�
	 */
	public void setUserSystemList(List userSystemList){
		this.userSystemList = userSystemList;
	}

	/**
	 * @return �û���ɫ�б�
	 */
	public List getUserRoleList(){
		return userRoleList;
	}

	/**
	 * @param userRoleList Ҫ���õ� �û���ɫ�б�
	 */
	public void setUserRoleList(List userRoleList){
		this.userRoleList = userRoleList;
	}

	/**
	 * @return ��Դ����
	 */
	public String[] getResMapArray(){
		return resMapArray;
	}

	/**
	 * @param resMapArray Ҫ���õ� ��Դ����
	 */
	public void setResMapArray(String[] resMapArray){
		this.resMapArray = resMapArray;
	}

	/**
	 * @return ��־�������
	 */
	public UBaseSysLogService getSysLogService(){
		return sysLogService;
	}

	/**
	 * @param sysLogService Ҫ���õ� ��־�������
	 */
	public void setSysLogService(UBaseSysLogService sysLogService){
		this.sysLogService = sysLogService;
	}

	/**
	 * @return �ֵ�������
	 */
	public DictionaryService getDicService(){
		return dicService;
	}

	/**
	 * @param dicService Ҫ���õ� �ֵ�������
	 */
	public void setDicService(DictionaryService dicService){
		this.dicService = dicService;
	}

	/**
	 * @return �û��������
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * @param userService Ҫ���õ� �û��������
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * @return Ȩ������������
	 */
	public AuthObjectService getAuthObjectService(){
		return authObjectService;
	}

	/**
	 * @param authObjectService Ҫ���õ� Ȩ������������
	 */
	public void setAuthObjectService(AuthObjectService authObjectService){
		this.authObjectService = authObjectService;
	}

	/**
	 * @return ��ɫid
	 */
	public String getRoleId(){
		return roleId;
	}

	/**
	 * @return ��ɫ����
	 */
	public UAuthRoleDO getRole(){
		return role;
	}

	/**
	 * @param role Ҫ���õ� ��ɫ����
	 */
	public void setRole(UAuthRoleDO role){
		this.role = role;
	}

	/**
	 * @param roleId Ҫ���õ� ��ɫid
	 */
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	/**
	 * @return �����
	 */
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	 * @param inputStream Ҫ���õ� �����
	 */
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}

	/**
	 * @return ���ڵ�id
	 */
	public String getId(){
		return id;
	}

	/**
	 * @param id Ҫ���õ� ���ڵ�id
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * @return ��ϵͳ�б�
	 */
	public List getSysList(){
		return sysList;
	}

	/**
	 * @param sysList Ҫ���õ� ��ϵͳ�б�
	 */
	public void setSysList(List sysList){
		this.sysList = sysList;
	}

	/**
	 * @return �û�����
	 */
	public UBaseUserDO getUser(){
		return user;
	}

	/**
	 * @param user Ҫ���õ� �û�����
	 */
	public void setUser(UBaseUserDO user){
		this.user = user;
	}

	/**
	 * @return ��Դ��Ϣ
	 */
	public UAuthResMapDO getAuthResMap(){
		return authResMap;
	}

	/**
	 * @param authResMap Ҫ���õ� ��Դ��Ϣ
	 */
	public void setAuthResMap(UAuthResMapDO authResMap){
		this.authResMap = authResMap;
	}

	/**
	 * @return ��Դ�ֵ��б�
	 */
	public List getResTypeDic(){
		return resTypeDic;
	}

	/**
	 * @param resTypeDic Ҫ���õ� ��Դ�ֵ��б�
	 */
	public void setResTypeDic(List resTypeDic){
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return �Ƿ�鿴
	 */
	public boolean isView(){
		return view;
	}

	/**
	 * @param view Ҫ���õ� �Ƿ��ܲ鿴
	 */
	public void setView(boolean view){
		this.view = view;
	}

	/**
	 * @return isSuccess �ɹ���Ϣ
	 */
	public String getIsSuccess(){
		return isSuccess;
	}

	/**
	 * @param isSuccess Ҫ���õ� �ɹ���Ϣ
	 */
	public void setIsSuccess(String isSuccess){
		this.isSuccess = isSuccess;
	}

	/**
	 * @param returnView �����ĸ�ҳ�� 1:����listResTreeRole  ����:listRoleTree.jsp
	 */
	public void setReturnView(String returnView) {
		this.returnView = returnView;
	}
	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public void setUserChangingService(UserChangingService userChangingService) {
		this.userChangingService = userChangingService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

//	public void prepare() throws Exception {
//		this.sysList = this.authRoleService.getSystemList(false);
//	}
}
