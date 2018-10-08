// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LoginSSOAction.java

package fmss.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.services.LogManagerService;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;

import com.jes.core.api.servlet.ServletHelper;

import ewp.comm.EwpBankUserManager;
import ewp.comm.EwpCommException;
import ewp.comm.EwpUserRight;

// Referenced classes of package fmss.action:
//			BaseAction

public class LoginSSOAction extends BaseAction {

	private static final Logger logger;
	private static final long serialVersionUID = 1L;
	private LoginDO user;
	private LoginService loginService;
	private UBaseSysLogService sysLogService;
	private LogManagerService logManagerService;
	private String mess;
	private CacheManager cacheManager;
	private ParamConfigService paramConfigService;
	private DataSource dataSource;
	private OnlineService onlineService;

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}

	public LoginSSOAction() {
	}

	String getSessionKey(HttpSession hs, String Key) {
		Object KeyObj = hs.getAttribute(Key);
		logger.info(Key + " the  key name:  " + Key + ", the value is :"
				+ KeyObj);
		if (KeyObj != null) {
			logger.info("the value is not null " + KeyObj);
			return (String) KeyObj;
		} else {
			return "";
		}
	}

	
	public String execute() throws Exception {
		try {
			LoginDO tempUser = (LoginDO) request.getSession().getAttribute(
					"LOGIN_USER");
			if (tempUser != null && tempUser.getLoginId() != null
					&& tempUser.getLoginId().length() > 0) {
				loginService.removeLoginId(tempUser.getLoginId());
				request.getSession().removeAttribute("LOGIN_USER");
			}
			if(!SSOInfoSyc()){
				logger.info("�˻���Ϣͬ������������½ҳ");
				this.setMess("�˻���Ϣͬ������");
				return INPUT;
			}
			
			HttpSession UmsHttpSession = request.getSession();
			String QASUser = getSessionKey(UmsHttpSession, "QASUser");
			user = new LoginDO();
			user.setUserId(QASUser);
			logger.info("�˻�:" + QASUser);
			if (StringUtils.isEmpty(user.getUserId())) {
				logger.info("�˻������ڣ��û���Ϊ�գ�������½ҳ");
				this.setMess("�˻������ڣ��û���Ϊ��");
				return INPUT;
			}
			LoginDO usdo = null;
			usdo = loginService.checkOnlyUser(user.getUserId());
			if (usdo != null) {
				usdo.setUserId(user.getUserId());
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setDescription("��¼FMSSϵͳ");
				usdo.setIp(ServletHelper.getRemoteAddr(request));
				String newLoginId = loginService.getNewLoginId(usdo.getUserId(),usdo.getIp());
				synchronized(this){
					newLoginId=onlineService.registerUser(new UBaseOnlineDO(newLoginId,usdo.getUserId(),usdo.getIp()));
					UsysSSOFilter.setUserId(response, usdo.getUserId());
					UsysSSOFilter.setParam(response,request, "USYS_LOGIN_ID", newLoginId);
					UsysSSOFilter.setParam(response,request, "USYS_ISLDAPTYPE","false");
					request.getSession().setAttribute(UsysSSOFilter.SSO_SESSION_USER_ID, usdo.getUserId());
					usdo.setLoginId(newLoginId);
				}
				request.getSession().setAttribute("LOGIN_USER", usdo);
				logger.info("�û�" + user.getUserId() + "����session");
				loginService.registerLoginInfo(newLoginId, usdo);
				try {
					fmss.dao.entity.UBaseSysLogDO sysLog = sysLogService
							.setUBaseSysLog(usdo, "��¼", "1", "00003");
					sysLog.setDescription("QASUser is " + QASUser);
					sysLogService.saveUBaseSysLog(sysLog);
				} catch (Exception e) {
					log.error("login write system log failed ", e);
				}
				CacheabledMap cache = (CacheabledMap) cacheManager
						.getCacheObject("PAPAMETER_CACHE_MAP");
				Map params = null;
				if (cache != null) {
					params = (Map) cache.get("PAPAMETER_CACHE_MAP");
					if (params != null) {
						UBaseSysParamDO param = (UBaseSysParamDO) params
								.get("PARAM_SYS_THEME_PATH");
						request.getSession().setAttribute("sysTheme",
								param.getSelectedValue());
					}
				}
				logger.info("����" + user.getUserId() + "��¼�ɹ�");
				setUseSessionUrlInfo(request.getRequestURL().toString());
				return SUCCESS;
			} else {
				logger.info("�˻�" + user.getUserId() + "�����ݿ���в����ڣ�������½ҳ");
				this.setMess("�˻�" + user.getUserId() + "�����ݿ���в�����");
				return INPUT;
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	public void setUseSessionUrlInfo(String sUrl) throws Exception {
		try {
			int lastIndex = sUrl.lastIndexOf(":");

			String sDefaultPort = sUrl.substring(lastIndex + 1, sUrl.length());
			String sDefaultIPAddress = sUrl.substring(0, lastIndex);

			String defaultIPAddress = sDefaultIPAddress
					.substring(sDefaultIPAddress.lastIndexOf("/") + 1);
			String defaultPort = sDefaultPort.substring(0, sDefaultPort
					.indexOf("/"));

			request.getSession().setAttribute("defaultIPAddress",
					defaultIPAddress);
			request.getSession().setAttribute("defaultPort", defaultPort);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	public LoginDO getUser() {
		return user;
	}

	public void setUser(LoginDO user) {
		this.user = user;
	}

	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setParamConfigService(ParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	private boolean isLdapLoginTypeByWebservice() {
		return "0".equals(cacheManager
				.getParemerCacheMapValue("PARAM_SYS_METHOD"));
	}

	private boolean isLdapLoginTypeByApi() {
		return "1".equals(cacheManager
				.getParemerCacheMapValue("PARAM_SYS_METHOD"));
	}

	private boolean isLdapLoginTypeByCertificate() {
		return "2".equals(cacheManager
				.getParemerCacheMapValue("PARAM_SYS_METHOD"));
	}

	static {
		logger = Logger.getLogger(LoginSSOAction.class);
	}
	
	
	private boolean SSOInfoSyc(){
		try {
			HttpSession UmsHttpSession = request.getSession(); 
			JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
			String QASUser = getSessionKey(UmsHttpSession, "QASUser"); //user_id
			//String QASUserName=getSessionKey(UmsHttpSession,"QASUserName");
			String QASFullUserName=getSessionKey(UmsHttpSession,"QASFullUserName");
			//QASUserName=new String(QASUserName.getBytes("BIG5"),"iso-8859-1");//user_cname

			Enumeration enumer = UmsHttpSession.getAttributeNames();
			System.out.println(enumer.toString()) ;
			
		    String QASBankCode=getSessionKey(UmsHttpSession,"QASBankCode");
		    String QASBrCode=getSessionKey(UmsHttpSession,"QASBrCode");
		    if(QASBrCode==null){
		    	QASBrCode=""; 
		    }
	        if(QASBrCode.equals("null")){
		    	QASBrCode="";
		    }
	        
		    if(QASBankCode.equalsIgnoreCase("NCB")){
		    	QASBankCode="043";
		    	logger.info("ת��[NCB]-->[043]");
		    }
		    String bankCode=QASBankCode+QASBrCode;//inst_id
		    
		    //��ȡ�û���ɫ
		    List userlist=new ArrayList();
		    String QASGroup=getSessionKey(UmsHttpSession,"QASGroup");
		    try {
		    	userlist=EwpBankUserManager.findUserRight(QASUser, QASGroup);
			} catch (EwpCommException e) {
				logger.error("ͨ��[EwpBankUserManager.findUserRight("+QASUser+", "+QASGroup+")]��ȡ�û���ɫʧ��["+e.getMessage()+"]");
				return false;
			}
			Iterator it=userlist.iterator();
			System.out.println(it.hasNext());
			String role="";
			List sysRoles=new ArrayList();
			List mdrRoles=new ArrayList();
			while(it.hasNext())
			{
				EwpUserRight userRight=(EwpUserRight)it.next();
				role=userRight.getSysRight();
				if(role.startsWith("X1")||role.startsWith("X2")||role.startsWith("X3")||role.startsWith("X5")){
					sysRoles.add(role.replaceFirst("X", "1"));
				}
				else if (role.startsWith("X4")){
					mdrRoles.add(role.replaceFirst("X", "1"));
				}else{
					logger.warn("����Ľ�ɫ���["+role+"]");
				}
		    }
			
			
			
		    String delRole="delete from u_auth_role_user t where t.user_id=?";
		    String delMdrRole="delete from  mdr_sc_role_user t where user_id=?";
		    String delUser="delete from u_base_user t where t.user_id=?";
		    String delAuth="delete from U_AUTH_OBJECT t where t.OBJECT_ID=? and t.OBJECT_TYPE='USER'";

		    String mappingUserInstSql="SELECT INST_ID FROM U_INST_MAPPING T WHERE T.USER_ID=?";
		 	String addAuth="insert into u_auth_object (object_id, object_name, object_type) values  (?, ?, 'USER')";
		    String addUser="insert into u_base_user " +
		    		" (user_id, user_ename, user_cname, password, inst_id,  last_modify_date, is_first_login, start_date, end_date, create_time, last_login_date) " +
		    		" values " +
		    		" (?, ?, ?, 'sso', ?, sysdate,1,to_date('2000-01-01','yyyy-MM-dd'),to_date('2050-01-01','yyyy-MM-dd'),sysdate,sysdate)";
		    
		    String addRole="insert into u_auth_role_user (role_id, user_id) values (?, ?)";
		    String addMdrRole="insert into mdr_sc_role_user (role_id, user_id) values (?, ?)";
		    
		    
		    //�������ݱ�ӳ���µĻ�����
		    try {
		    		String mappingUserInst = (String) jdbcTemplate.queryForObject(
							mappingUserInstSql, new Object[] { QASUser },
							String.class);
					if (mappingUserInst != null && !mappingUserInst.equals("")) {
						logger.info("ת��["+QASUser+"]�Ļ���["+bankCode+"]-->["+mappingUserInst+"]");
						bankCode = mappingUserInst;
					}
		    }catch (EmptyResultDataAccessException ex){
		    	logger.info("["+QASUser+"]�Ļ���["+bankCode+"]����ת��");
		  
			}catch (Exception ex) {
					logger.error("�������ݱ�ӳ���µĻ����ų��ִ���["+ex.getMessage()+"]");
					return false;
			}
			
			PlatformTransactionManager   tm   =   new   DataSourceTransactionManager(jdbcTemplate.getDataSource());
			TransactionStatus   status   =   tm.getTransaction(null);
			try {
				logger.info("��ʼͬ���û����û�Ȩ����Ϣ");
				logger.info("-------------------------------");
				
				jdbcTemplate.update(delRole, new Object[]{QASUser});
				jdbcTemplate.update(delMdrRole, new Object[]{QASUser});
				jdbcTemplate.update(delUser, new Object[]{QASUser});
				jdbcTemplate.update(delAuth, new Object[]{QASUser});
				
				logger.info("��ʼͬ���û�");

				
				logger.info("��ʼͬ���û���Ϣ");
				jdbcTemplate.update(addAuth, new Object[]{QASUser,QASUser});
				jdbcTemplate.update(addUser, new Object[]{QASUser,QASUser,QASFullUserName,bankCode});
				
				logger.info("��ʼͬ��ϵͳ�û���ɫ");
				for (Iterator iterator = sysRoles.iterator(); iterator
						.hasNext();) {
					String  ur = (String ) iterator.next();
					jdbcTemplate.update(addRole, new Object[]{ur,QASUser});
				}
				if(mdrRoles.size()>0){
					logger.info("��ʼͬ��MDR�û��û���ɫ");
					jdbcTemplate.update(addRole, new Object[]{"1076",QASUser});
					for (Iterator iterator = mdrRoles.iterator(); iterator
							.hasNext();) {
						String mr = (String) iterator.next();
						jdbcTemplate.update(addMdrRole, new Object[]{new Integer(mr),QASUser});	
					}
				}
				logger.info("����ͬ���û����û�Ȩ����Ϣ");
				tm.commit(status) ;
			} catch (Exception e) {
				tm.rollback(status);
				logger.error("����ʧ��,ͬ���û�Ȩ����Ϣʧ��["+e.getMessage()+"]!");
				return false;
				
			}finally{
				tm=null;
				logger.info("-------------------------------");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("����ʧ��,["+e.getMessage()+"]!");
			return false;
		}
		
		
		return true;
		
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
