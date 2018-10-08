package fmss.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import fmss.action.base.AuditException;
import fmss.action.base.UserChangingService;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthObjectDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.services.AuthObjectService;
import fmss.services.AuthorityNameFetcher;
import fmss.services.DictionaryService;
import fmss.services.ILdapService;
import fmss.services.InstService;
import fmss.services.OnlineService;
import fmss.services.UBaseSysLogService;
import fmss.services.UserLoginManager;
import fmss.services.UserService;
import fmss.common.util.Constants;
import fmss.common.util.HexUtils;
import fmss.common.util.JXLTool;
import fmss.common.util.LoginUtil;

/**
 * <p> ��Ȩ����:(C)2003-2010  </p>
 * @����: guojingzhan
 * @����: 2009-6-27 ����10:44:41
 * @����: [UserAction]�û��������Action
 */
public class UserAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	protected static final String FORM_USER = "formUser";
	protected static final String STREAM = "stream";
	private UBaseUserDO user = new UBaseUserDO(); // �����û�����ʵ��
	private UBaseInstDO inst = new UBaseInstDO(); // ������������ʵ��
	private UserService userService; // �����û�����ʵ��
	private InstService instService; // ������������ʵ��
	private OnlineService onlineService;
	private List userList; // �û��б�
	private String[] userIdList; // ѡ���û�����б�
	private List instList = Collections.EMPTY_LIST; // �����б�
	private List resMap; // ��ѯ���Map
	private List sysConfig; // ϵͳ�����б�
	private List role; // Ȩ������
	private List hisPwd; // ��ʷ����
	private List loginLog; // ��¼��־
	private boolean fixQuery;
	private InputStream inputStream;
	private static IdGenerator idGenerator; // id������
	private AuthObjectService authObjectService; // Ȩ���������
	private CacheManager cacheManager; // ����
	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;
	private UBaseSysParamDO param = new UBaseSysParamDO();// ��ȫ���Բ���
	private UserChangingService userChangingService;// �û��޸����service
	private ILdapService ldapService;
	private UserLoginManager userLoginManager;//
	private boolean manageLogin = false;// ��ʾkickoff��

	public void setUserLoginManager(UserLoginManager userLoginManager){
		this.userLoginManager = userLoginManager;
	}

	/**
	 * @return cacheManager
	 */
	public CacheManager getCacheManager(){
		return cacheManager;
	}

	/**
	 * @param cacheManager Ҫ���õ� cacheManager
	 */
	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	/**
	 * @return authObjectService
	 */
	public AuthObjectService getAuthObjectService(){
		return authObjectService;
	}

	/**
	 * @param authObjectService Ҫ���õ� authObjectService
	 */
	public void setAuthObjectService(AuthObjectService authObjectService){
		this.authObjectService = authObjectService;
	}

	/**
	 * <p> ��������: listUser|����: ��ʾ�û��б� </p>
	 * @return ��ʾ�û��б�
	 */
	public String listUser(){
		return SUCCESS;
	}

	/**
	 * <p> ��������: listUserHead|����: ��ʾ�û��б��ѯ֡ </p>
	 * @return ��ʾ�û��б��ѯ֡
	 */
	public String listUserHead(){
		return SUCCESS;
	}

	/**
	 * <p> ��������: listUserInstTree|����: ��ʾ�����б� </p>
	 * @return ��ʾ�����б�
	 */
	public String listUserInstTree(){
		return SUCCESS;
	}

	protected void pushResponse(HttpServletResponse response, String s){
		try{
			Assert.notNull(s);
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(s);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * <p>��������: moveInst|����: �ƶ��û�</p>
	 * @return null
	 */
	public String moveUser(){
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		String ids[] = request.getParameter("ids").split(",");
		String smsg = "";
		String emsg = "";
		UBaseUserDO oUser = null;
		String sinstId = null;
		String dinstId = this.request.getParameter("dest");
		String s = null;
		for(int i = 0, j = ids.length; i < j; i++){
			if(!"".equals(ids[i].trim())){
				try{
					oUser = userService.getUser(ids[i]);
					sinstId = oUser.getInstId();
					oUser.setInstId(dinstId);
					/** **************�û��޸����****************** */
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
						UBaseUserDO originalUser = userService
								.getUserByUserId(oUser.getUserId());
						s = userChangingService.saveBaseChanges(loginUser,
								oUser, originalUser);
					}
					/** **************�û��޸����****************** */
					else{
						userService.updateUserByUserId(oUser);
					}
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
							loginUser, "����", "1",
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0002");
					sysLog.setMenuName("������Ϣ����.�û�����");
					this.sysLogService.saveUBaseSysLog(sysLog);
					userService.updateUserCName(oUser);
					if(oUser.getUserId().equals(loginUser.getUserId())){
						loginUser.setUserCname(oUser.getUserCname());
					}
					smsg += "�ƶ��û��ɹ�" + oUser.getUserCname() + "["
							+ oUser.getUserId() + "]"
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : "")
							+ "\\n";
					loginUser.setDescription("�ƶ��û��ɹ�," + oUser.getUserCname()
							+ "[" + oUser.getUserId() + "]" + "��" + sinstId
							+ "��" + dinstId
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : ""));
				}catch (Exception e){
					smsg += e.getLocalizedMessage() + ",�ƶ��û�ʧ��"
							+ oUser.getUserCname() + "[" + oUser.getUserId()
							+ "]"
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : "")
							+ "\\n";
					loginUser.setDescription(e.getLocalizedMessage()
							+ "�ƶ��û�ʧ��," + oUser.getUserCname() + "["
							+ oUser.getUserId() + "]" + "��" + sinstId + "��"
							+ dinstId
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : ""));
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
							loginUser, "����", "1",
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0002");
					sysLog.setMenuName("������Ϣ����.�û�����");
					this.sysLogService.saveUBaseSysLog(sysLog);
					userService.updateUserCName(oUser);
					log.error(e);
				}
			}
		}
		out2page("{success:true,msg:'"
				+ (smsg + emsg).substring(0, (smsg + emsg).length() - 2) + "'}");
		return null;
	}

	/**
	 * <p> ��������: listUserMain|����: ��ѯ��ȡ�û��б� </p>
	 * @return �û��б�
	 * @throws Exception
	 */
	public String listUserMain() throws Exception{
		try{
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			// ��ȡ�û���ѯ��SQL��䣬Ȼ����û�ȡ�û��б�
			if("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))
					&& "1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				userChangingService.getTwoTablePlusUsers(user, inst,
						paginationList, login.getInstId());
			}else{
				String strCon = null;
				ArrayList params = null;
				if(!fixQuery){
					Object[] objs = userService.getUsersByCondition(user, inst,
							login.getInstId());
					strCon = (String) objs[0];
					params = (ArrayList) objs[1];
				}else if(fixQuery){
					params = new ArrayList();
					strCon = "select ubu.userId as userId, ubu.isUserLocked as isUserLocked, ubu.userEname as userEname, ubu.userCname as userCname, ubi.instId as instId, ubi.instSmpName as instSmpName,ubu.isUserLocked as isUserLocked,ubu.userLockedReson as userLockedReson,ubu.isDelete as isDelete,ubu.lastLoginDate as lastLoginDate ,ubu.enabled as enabled ,ubu.isList"
							+ " from UBaseUserDO ubu left join ubu.ubaseInst ubi where 1 = 1"
							+ " and exists (select 1 from UBaseInstDO a where a.instId=? and substring(ubi.instPath,1,length(a.instPath)) = a.instPath)"
							+ " order by  ubi.instLevel,ubu.instId,ubu.userCname";
					params.add(user.getInstId());
				}
				userService.find(strCon, params, this.paginationList);
				if(this.paginationList.getRecordList() != null)
					for(int i = 0; i < this.paginationList.getRecordList()
							.size(); i++){
						Object[] o = (Object[]) this.paginationList
								.getRecordList().get(i);
						Map m = new HashMap();
						m.put("userId", o[0]);
						m.put("isUserLocked", o[1]);
						m.put("userEname", o[2]);
						m.put("userCname", o[3]);
						m.put("instId", o[4]);
						m.put("instSmpName", o[5]);
						m.put("canAccess", new Boolean(true));
						String userStatus = userLoginManager
								.getDetailUserStatus((String) o[6],
										(String) o[7]);
						if("0".equals(o[10])){
							userStatus = "������Աͣ��";
						}
						if("1".equals(o[8])){
							userStatus = "��ɾ��";
							m.put("isDelete", o[8]);
							m.put("canAccess", new Boolean(false));
						}
						m.put("enabled", o[10]);
						m.put("isList", o[11]);
						m.put("userStatus", userStatus);
						m.put("hasLogin", new Boolean(onlineService
								.hasLoginByUserId((String) o[0])));
						this.paginationList.getRecordList().set(i, m);
					}
			}
			CacheabledMap cache = (CacheabledMap) cacheManager
					.getCacheObject(Constants.PAPAMETER_CACHE_MAP);
			Map params = null;
			if(cache != null){
				params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
				param = (UBaseSysParamDO) params.get(Constants.PARAM_SYS_ISUSE);
			}
			manageLogin = false;
			manageLogin = isSuperAdmin(login.getUserId())
					&& "1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_ALLOW_SAMEUSER_LOGIN));
			request.setAttribute("loginUserId", login.getUserId() + "11");
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error(e);
			throw e;
		}
	}

	private boolean isSuperAdmin(String userId){
		// TODO implements
		return userService.isSuperAdministrator(userId);
	}

	public void userToExcel() throws Exception{
		try{
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			List list = new ArrayList();
			if("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))
					&& "1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				list = userChangingService.getTwoTablePlusUsers(user, inst);
			}else{
				String strCon = null;
				ArrayList params = null;
				if(!fixQuery){
					Object[] objs = userService.getUsersByCondition(user, inst,
							login.getInstId());
					strCon = (String) objs[0];
					params = (ArrayList) objs[1];
				}else if(fixQuery){
					params = new ArrayList();
					strCon = "select ubu.userId as userId, ubu.isUserLocked as isUserLocked, ubu.userEname as userEname, ubu.userCname as userCname, ubi.instId as instId, ubi.instSmpName as instSmpName,ubu.isUserLocked as isUserLocked,ubu.userLockedReson as userLockedReson,ubu.isDelete as isDelete,ubu.lastLoginDate as lastLoginDate ,ubu.enabled as enabled ,ubu.isList"
							+ " from UBaseUserDO ubu left join ubu.ubaseInst ubi where 1 = 1"
							+ " and exists (select 1 from UBaseInstDO a where a.instId=? and substring(ubi.instPath,1,length(a.instPath)) = a.instPath)"
							+ " order by  ubi.instLevel,ubu.instId,ubu.userCname";
					params.add(request.getParameter("instTreeId"));
				}
				List list0 = userService.find(strCon, params);
				if(list0 != null){
					for(int i = 0; i < list0.size(); i++){
						Object[] o = (Object[]) list0.get(i);
						Map m = new HashMap();
						m.put("userId", o[0]);
						m.put("isUserLocked", o[1]);
						m.put("userEname", o[2]);
						m.put("userCname", o[3]);
						m.put("instId", o[4]);
						m.put("instSmpName", o[5]);
						m.put("canAccess", new Boolean(true));
						String userStatus = userLoginManager
								.getDetailUserStatus((String) o[6],
										(String) o[7]);
						if("0".equals(o[10])){
							userStatus = "������Աͣ��";
						}
						if("1".equals(o[8])){
							userStatus = "��ɾ��";
							m.put("isDelete", o[8]);
							m.put("canAccess", new Boolean(false));
						}
						m.put("userStatus", userStatus);
						m.put("lastLoginDate", o[9]);
						List roles = userService.getRoleByUserId((String) o[0]);
						StringBuffer sb = new StringBuffer();
						if(CollectionUtils.isNotEmpty(roles)){
							for(Iterator iterator = roles.iterator(); iterator
									.hasNext();){
								UAuthRoleDO role = (UAuthRoleDO) iterator
										.next();
								sb.append("," + role.getRoleName() + "["
										+ role.getSystemCname() + "]");
							}
						}
						m.put("roles", sb.toString().length() == 0 ? "" : sb
								.toString().substring(1));
						list.add(m);
					}
				}
			}
			StringBuffer fileName = new StringBuffer("�û��б�");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, list);
			os.flush();
			os.close();
		}catch (Exception e){
			log.error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException{
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("�û��б�", 0);
		Label header1 = new Label(0, 0, "�û���", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "�û�������", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "������", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "�������", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "�û�״̬", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "����¼ʱ��", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "��ɫ", JXLTool.getHeader());
		ws.addCell(header1);
		ws.addCell(header2);
		ws.addCell(header3);
		ws.addCell(header4);
		ws.addCell(header5);
		ws.addCell(header6);
		ws.addCell(header7);
		for(int i = 0; i < 7; i++){
			ws.setColumnView(i, 18);
		}
		int count = 1;
		for(int i = 0; i < content.size(); i++){
			Map o = (Map) content.get(i);
			int column = count++;
			setWritableSheet(ws, o, column);
		}
		wb.write();
		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	private static final String PATTREN = "yyyy-MM-dd hh:mm:ss";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTREN);

	private void setWritableSheet(WritableSheet ws, Map o, int column)
			throws WriteException{
		Label cell1 = new Label(0, column, (String) o.get("userId"), JXLTool
				.getContentFormat());
		Label cell2 = new Label(1, column, (String) o.get("userCname"), JXLTool
				.getContentFormat());
		Label cell3 = new Label(2, column, (String) o.get("instId"), JXLTool
				.getContentFormat());
		Label cell4 = new Label(3, column, (String) o.get("instSmpName"),
				JXLTool.getContentFormat());
		Label cell5 = new Label(4, column, (String) o.get("userStatus"),
				JXLTool.getContentFormat());
		Label cell6 = new Label(5, column,
				o.get("lastLoginDate") != null ? DATE_FORMAT.format(o
						.get("lastLoginDate")) : "", JXLTool.getContentFormat());
		Label cell7 = new Label(6, column, (String) o.get("roles"), JXLTool
				.getContentFormat());
		ws.addCell(cell1);
		ws.addCell(cell2);
		ws.addCell(cell3);
		ws.addCell(cell4);
		ws.addCell(cell5);
		ws.addCell(cell6);
		ws.addCell(cell7);
	}

	/**
	 * <p> ��������: viewUser|����: ͨ���û���Ż�ȡ�û���������Ϣ </p>
	 * @return user �û�����
	 * @return inst ��������
	 */
	public String viewUser(){
		try{
			user = userService.getUserByUserId(user.getUserId());
			inst = (UBaseInstDO) instService.getInstByInstId(user.getInstId());
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p> ��������: editUser|����: �༭����ͨ���û���Ż�ȡ�û���Ϣ </p>
	 * @return userList ͨ���û���Ż�ȡ���û���Ϣ
	 * @return instList ����ѡ���û����������Ļ����б�
	 */
	public String editUser(){
		try{
			user = userService.getUserByUserId(user.getUserId());
			instList = instService.getAllInsts();
			for(Iterator iterator = instList.iterator(); iterator.hasNext();){
				UBaseInstDO inst = (UBaseInstDO) iterator.next();
				inst.setInstSmpName("[" + inst.getInstId() + "]"
						+ inst.getInstSmpName());
			}
			return FORM_USER;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p> ��������: createUser|����: �����û� </p>
	 * @return instList ����ѡ���û����������Ļ����б�
	 */
	public String createUser(){
		try{
			instList = instService.getAllInsts();
			for(Iterator iterator = instList.iterator(); iterator.hasNext();){
				UBaseInstDO inst = (UBaseInstDO) iterator.next();
				inst.setInstSmpName("[" + inst.getInstId() + "]"
						+ inst.getInstSmpName());
			}
			return FORM_USER;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p> ��������: deleteUser|����:ɾ���û� </p>
	 * @return �ɹ�ҳ��
	 */
	public String deleteUser(){
		String ids = "";
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// ��������
			String returnMsg = "";
			/** **************�û��޸����****************** */
			if("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				for(int i = 0; userIdList != null && i < userIdList.length; i++){
					ids += userIdList[i] + ",";
				}
				userChangingService.saveBaseChanges(user, userIdList);
				returnMsg = "ɾ���ɹ�,�����ͨ������Ч";
				user.setDescription("ɾ���û�" + ids + "," + returnMsg);
			}
			/** **************�û��޸����****************** */
			else{
				returnMsg = this.userService.deleteUser(this.userIdList, user
						.getUserId());
				user.setDescription("ɾ���û�" + StringUtils.join(userIdList, ","));
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			if(StringUtils.isEmpty(returnMsg)){
				setResultMessages("ɾ���ɹ���");
			}else{
				setResultMessages(returnMsg);
			}
		}catch (Exception e){
			if(e instanceof AuditException){
				setResultMessages(e.getMessage());
			}else{
				log.error(e);
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("ɾ���û�" + ids + "ʧ��");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"ɾ��", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("ɾ��ʧ�ܡ�");
			}
		}
		return SUCCESS;
	}

	/**
	 * <p> ��������: recoverUser|����:�ָ��߼�ɾ���û� </p>
	 * @return �ɹ�ҳ��
	 */
	public String recoverUser(){
		String ids = "";
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// ��������
			String returnMsg = "";
			returnMsg = this.userService.recoverUser(this.userIdList, user
					.getUserId());
			user.setDescription("�ָ��û�" + StringUtils.join(userIdList, ","));
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"�ָ��û�", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("������Ϣ����.�û�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			if(StringUtils.isEmpty(returnMsg)){
				setResultMessages("�ָ��ɹ���");
			}else{
				setResultMessages("�ָ�ʧ�ܡ�");
			}
		}catch (Exception e){
			if(e instanceof AuditException){
				setResultMessages(e.getMessage());
			}else{
				log.error(e);
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("�ָ��û�" + ids + "ʧ��");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"�ָ��û�", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("�ָ�ʧ�ܡ�");
			}
		}
		return SUCCESS;
	}

	/**
	 * <p> ��������: saveUser|����: ��ȡ�û���Ϣ�����û� </p>
	 * @return ����Ľ��ֵ
	 */
	public String saveUser(){
		String editType = StringUtils.isNotEmpty(user.getUserId()) ? "update"
				: "add";
		instList = instService.getAllInsts();
		for(Iterator iterator = instList.iterator(); iterator.hasNext();){
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			inst.setInstSmpName("[" + inst.getInstId() + "]"
					+ inst.getInstSmpName());
		}
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		try{
			if(user.getUserEname() == null || user.getUserEname().equals("")){
				setResultMessages("�û�������Ϊ�ա�");
				return INPUT;
			}
			if(user.getUserEname().length() > 20){
				setResultMessages("���Ȳ��ܴ���20");
				return INPUT;
			}
			String len = cacheManager
					.getParemerCacheMapValue("PARAM_SYS_USER_NAME_MIN_LEN");
			if(!"".equals(len)){
				try{
					int l = Integer.parseInt(len);
					if(user.getUserEname().length() < l){
						setResultMessages("�û������Ȳ���С��" + len);
						return INPUT;
					}
				}catch (Exception ex){
				}
			}
			Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
			Matcher m1 = p1.matcher(user.getUserEname());
			if(!m1.matches()){
				setResultMessages("�û������зǷ��ַ�");
				return INPUT;
			}
			setResultMessages("����ɹ���");
			if("add".equals(editType)){
				// �жϵ�¼���Ƿ�ʹ��
				List listUser = userService.getUserInfoByEName(user
						.getUserEname());
				if(listUser.size() > 0){
					setResultMessages("�û���¼�������ظ�!");
					return INPUT;
				}
				// ��ʼ��id������ �����޸� idΪuserEName 20090828
				String id = user.getUserEname();
				// ����Ȩ������
				UAuthObjectDO obj = new UAuthObjectDO();
				obj.setObjectId(String.valueOf(id));
				obj.setObjectName(this.user.getUserCname());
				obj.setObjectType(DictionaryService.USER_AOT_DIC_TYPE);
				/** **************�û��޸����****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					// ����
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))){
						// �����û��������
						List list = userChangingService.getUserChanges(user
								.getUserEname());
						if(list.size() > 0){
							setResultMessages("��˱��Ѵ����½��û�["
									+ user.getUserEname() + "]");
							return INPUT;
						}
					}else{
						this.authObjectService.save(obj);
					}
				}
				/** **************�û��޸����****************** */
				else{
					this.authObjectService.save(obj);
				}
				loginUser.setDescription("����Ȩ������" + obj.getObjectId());
				UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(
						loginUser, "����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog1.setMenuId("0002.0002");
				sysLog1.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog1);
				// �����ɫ
				this.user.setUserId(String.valueOf(id));
				// modify by wangxin 20100423 �޸�Ĭ������
				String defPwd = "000000";
				String strPwd = cacheManager
						.getParemerCacheMapValue("PARAM_SYS_INITPWD");
				if(!"".equals(strPwd)){
					defPwd = strPwd;
				}
				this.user
						.setPassword(HexUtils.shaHex(defPwd, user.getUserId()));
				this.user.setIsFirstLogin("0"); // 0��ʾ�״ε�½
				this.user.setIsUserLocked(LoginUtil.LOCK_NORMAL); // 0��ʾ�û�δ������
				loginUser.setDescription("�����û�"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				/** **************�û��޸����****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					// �����û��������
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))){
						userChangingService.saveBaseChanges(loginUser, user);
						setResultMessages("����ɹ�,�����ͨ������Ч");
						loginUser.addDescription(",����ɹ�,�����ͨ������Ч");
					}else{
						userService.saveUserByUserId(user);
					}
				}
				/** **************�û��޸����****************** */
				else{
					userService.saveUserByUserId(user);
				}
				UBaseSysLogDO sysLog2 = this.sysLogService.setUBaseSysLog(
						loginUser, "����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog2.setMenuId("0002.0002");
				sysLog2.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog2);
			}else{
				UBaseUserDO oUser = userService.getUser(user.getUserId());
				// �����ϵ��ֶ�
				oUser.setDescription(user.getDescription());
				oUser.setEmail(user.getEmail());
				oUser.setMobile(user.getMobile());
				oUser.setTel(user.getTel());
				oUser.setUserCname(user.getUserCname());
				oUser.setAddress(user.getAddress());
				oUser.setInstId(user.getInstId());
				loginUser.setDescription("�����û�"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				/** **************�û��޸����****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					UBaseUserDO originalUser = userService.getUserByUserId(user
							.getUserId());
					String s = userChangingService.saveBaseChanges(loginUser,
							oUser, originalUser);
					setResultMessages(StringUtils.isNotEmpty(s) ? s : "����ɹ�");
					loginUser.addDescription(StringUtils.isNotEmpty(s) ? s
							: "����ɹ�");
				}
				/** **************�û��޸����****************** */
				else{
					userService.updateUserByUserId(oUser);
				}
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
						loginUser, "����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				userService.updateUserCName(user);
				if(user.getUserId().equals(loginUser.getUserId())){
					loginUser.setUserCname(user.getUserCname());
				}
			}
			return SUCCESS;
		}catch (Exception e){
			if(e instanceof AuditException){
				setResultMessages(e.getMessage());
			}else{
				log.error(e);
				e.printStackTrace();
				loginUser.setDescription("�����û�" + user.getUserId() + "ʧ��");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
						loginUser, "����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("������Ϣ����.�û�����");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("�����û���Ϣʧ��");
				if("add".equals(editType)){
					this.user.setUserId(null);
				}
			}
			return INPUT;
		}
	}

	/**
	 * ����û�id�Ƿ���ڣ����ڼ����½������������ܽ������û�
	 */
	public void checkUserId(){
		try{
			String userId = URLDecoder.decode(request.getParameter("userId"),
					"UTF-8");
			PrintWriter pw;
			pw = response.getWriter();
			if(getLdapService().isUserExists(userId))
				pw.write("true");
			else
				pw.write("false");
		}catch (Exception e){
			log.error("����û�idʧ��!", e);
		}
	}

	public String editPswd(){
		return SUCCESS;
	}

	/**
	 * <p> ��������: getInputStream|����: ��ȡ�������� </p>
	 * @return ��ȡ��������
	 */
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	 * <p> ��������: setInputStream|����: ������������ </p>
	 * @param inputStream ������������
	 */
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}

	/**
	 * <p> ��������: getUserService|����: �����û����� </p>
	 * @return �����û�����
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * <p> ��������: setUserService|����: �����û����� </p>
	 * @param userService �����û�����
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * <p> ��������: getUser|����: ��ȡ�û����� </p>
	 * @return ��ȡ�û�����
	 */
	public UBaseUserDO getUser(){
		return user;
	}

	/**
	 * <p> ��������: setUser|����: �����û����� </p>
	 * @param user �����û�����
	 */
	public void setUser(UBaseUserDO user){
		this.user = user;
	}

	/**
	 * <p> ��������: getUserList|����: ��ȡ�û��б� </p>
	 * @return ��ȡ�û��б�
	 */
	public List getUserList(){
		return userList;
	}

	/**
	 * <p> ��������: setUserList|����: �����û��б� </p>
	 * @param userList �����û��б�
	 */
	public void setUserList(List userList){
		this.userList = userList;
	}

	/**
	 * <p> ��������: getInst|����: ��ȡ�������� </p>
	 * @return ��ȡ��������
	 */
	public UBaseInstDO getInst(){
		return inst;
	}

	/**
	 * <p> ��������: setInst|����: ���û������� </p>
	 * @param inst ���û�������
	 */
	public void setInst(UBaseInstDO inst){
		this.inst = inst;
	}

	/**
	 * <p> ��������: getResMap|����: ��ȡ���Map </p>
	 * @return ��ȡ���Map
	 */
	public List getResMap(){
		return resMap;
	}

	/**
	 * <p> ��������: setResMap|����: ���ý��Map </p>
	 * @param resMap ���ý��Map
	 */
	public void setResMap(List resMap){
		this.resMap = resMap;
	}

	/**
	 * <p> ��������: getSysConfig|����: ��ȡϵͳ���� </p>
	 * @return ��ȡϵͳ����
	 */
	public List getSysConfig(){
		return sysConfig;
	}

	/**
	 * <p> ��������: setSysConfig|����: ����ϵͳ���� </p>
	 * @param sysConfig ����ϵͳ����
	 */
	public void setSysConfig(List sysConfig){
		this.sysConfig = sysConfig;
	}

	/**
	 * <p> ��������: getRole|����: ��ȡ�û�Ȩ�� </p>
	 * @return ��ȡ�û�Ȩ��
	 */
	public List getRole(){
		return role;
	}

	/**
	 * <p> ��������: setRole|����: �����û�Ȩ�� </p>
	 * @param role �����û�Ȩ��
	 */
	public void setRole(List role){
		this.role = role;
	}

	/**
	 * <p> ��������: getHisPwd|����: ��ȡ�û���ʷ���� </p>
	 * @return ��ȡ�û���ʷ����
	 */
	public List getHisPwd(){
		return hisPwd;
	}

	/**
	 * <p> ��������: setHisPwd|����: �����û���ʷ���� </p>
	 * @param hisPwd �����û���ʷ����
	 */
	public void setHisPwd(List hisPwd){
		this.hisPwd = hisPwd;
	}

	/**
	 * <p> ��������: getLoginLog|����: ��ȡ�û���¼��־ </p>
	 * @return ��ȡ�û���¼��־
	 */
	public List getLoginLog(){
		return loginLog;
	}

	/**
	 * <p> ��������: setLoginLog|����: �����û���¼��־ </p>
	 * @param loginLog �����û���¼��־
	 */
	public void setLoginLog(List loginLog){
		this.loginLog = loginLog;
	}

	/**
	 * <p> ��������: getInstService|����: ��ȡ�������� </p>
	 * @return ��ȡ��������
	 */
	public InstService getInstService(){
		return instService;
	}

	/**
	 * <p> ��������: setInstService|����: ���û������� </p>
	 * @param instService ���û�������
	 */
	public void setInstService(InstService instService){
		this.instService = instService;
	}

	/**
	 * <p> ��������: getInstList|����: ��ȡ�����б� </p>
	 * @return ��ȡ�����б�
	 */
	public List getInstList(){
		return instList;
	}

	/**
	 * <p> ��������: setInstList|����: ���û����б� </p>
	 * @param instList ���û����б�
	 */
	public void setInstList(List instList){
		this.instList = instList;
	}

	/**
	 * <p> ��������: getUserIdList|����: ѡ���û�����б� </p>
	 * @param UserIdList ѡ���û�����б�
	 */
	public String[] getUserIdList(){
		return userIdList;
	}

	/**
	 * <p> ��������: setUserIdList|����: ѡ���û�����б� </p>
	 * @param UserIdList ѡ���û�����б�
	 */
	public void setUserIdList(String[] userIdList){
		this.userIdList = userIdList;
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

	public UBaseSysParamDO getParam(){
		return param;
	}

	public void setParam(UBaseSysParamDO param){
		this.param = param;
	}

	public void setUserChangingService(UserChangingService userChangingService){
		this.userChangingService = userChangingService;
	}

	public boolean isManageLogin(){
		return manageLogin;
	}

	public void setManageLogin(boolean manageLogin){
		this.manageLogin = manageLogin;
	}

	public boolean isFixQuery(){
		return fixQuery;
	}

	public void setFixQuery(boolean fixQuery){
		this.fixQuery = fixQuery;
	}

	public void setOnlineService(OnlineService onlineService){
		this.onlineService = onlineService;
	}

	public ILdapService getLdapService(){
		return ldapService;
	}

	public void setLdapService(ILdapService ldapService){
		this.ldapService = ldapService;
	}
}
