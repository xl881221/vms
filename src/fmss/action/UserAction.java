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
 * <p> 版权所有:(C)2003-2010  </p>
 * @作者: guojingzhan
 * @日期: 2009-6-27 上午10:44:41
 * @描述: [UserAction]用户管理相关Action
 */
public class UserAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	protected static final String FORM_USER = "formUser";
	protected static final String STREAM = "stream";
	private UBaseUserDO user = new UBaseUserDO(); // 创建用户对象实例
	private UBaseInstDO inst = new UBaseInstDO(); // 创建机构对象实例
	private UserService userService; // 创建用户方法实例
	private InstService instService; // 创建机构方法实例
	private OnlineService onlineService;
	private List userList; // 用户列表
	private String[] userIdList; // 选中用户编号列表
	private List instList = Collections.EMPTY_LIST; // 机构列表
	private List resMap; // 查询结果Map
	private List sysConfig; // 系统配置列表
	private List role; // 权限立标
	private List hisPwd; // 历史密码
	private List loginLog; // 登录日志
	private boolean fixQuery;
	private InputStream inputStream;
	private static IdGenerator idGenerator; // id生成器
	private AuthObjectService authObjectService; // 权限主体服务
	private CacheManager cacheManager; // 缓存
	/** sysLogService 日志服务 */
	private UBaseSysLogService sysLogService;
	private UBaseSysParamDO param = new UBaseSysParamDO();// 安全策略参数
	private UserChangingService userChangingService;// 用户修改审核service
	private ILdapService ldapService;
	private UserLoginManager userLoginManager;//
	private boolean manageLogin = false;// 显示kickoff列

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
	 * @param cacheManager 要设置的 cacheManager
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
	 * @param authObjectService 要设置的 authObjectService
	 */
	public void setAuthObjectService(AuthObjectService authObjectService){
		this.authObjectService = authObjectService;
	}

	/**
	 * <p> 方法名称: listUser|描述: 显示用户列表 </p>
	 * @return 显示用户列表
	 */
	public String listUser(){
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: listUserHead|描述: 显示用户列表查询帧 </p>
	 * @return 显示用户列表查询帧
	 */
	public String listUserHead(){
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: listUserInstTree|描述: 显示机构列表 </p>
	 * @return 显示机构列表
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
	 * <p>方法名称: moveInst|描述: 移动用户</p>
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
					/** **************用户修改审核****************** */
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
						UBaseUserDO originalUser = userService
								.getUserByUserId(oUser.getUserId());
						s = userChangingService.saveBaseChanges(loginUser,
								oUser, originalUser);
					}
					/** **************用户修改审核****************** */
					else{
						userService.updateUserByUserId(oUser);
					}
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
							loginUser, "更新", "1",
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0002");
					sysLog.setMenuName("基础信息管理.用户管理");
					this.sysLogService.saveUBaseSysLog(sysLog);
					userService.updateUserCName(oUser);
					if(oUser.getUserId().equals(loginUser.getUserId())){
						loginUser.setUserCname(oUser.getUserCname());
					}
					smsg += "移动用户成功" + oUser.getUserCname() + "["
							+ oUser.getUserId() + "]"
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : "")
							+ "\\n";
					loginUser.setDescription("移动用户成功," + oUser.getUserCname()
							+ "[" + oUser.getUserId() + "]" + "从" + sinstId
							+ "至" + dinstId
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : ""));
				}catch (Exception e){
					smsg += e.getLocalizedMessage() + ",移动用户失败"
							+ oUser.getUserCname() + "[" + oUser.getUserId()
							+ "]"
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : "")
							+ "\\n";
					loginUser.setDescription(e.getLocalizedMessage()
							+ "移动用户失败," + oUser.getUserCname() + "["
							+ oUser.getUserId() + "]" + "从" + sinstId + "至"
							+ dinstId
							+ (StringUtils.isNotEmpty(s) ? ("," + s) : ""));
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
							loginUser, "更新", "1",
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0002");
					sysLog.setMenuName("基础信息管理.用户管理");
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
	 * <p> 方法名称: listUserMain|描述: 查询获取用户列表 </p>
	 * @return 用户列表
	 * @throws Exception
	 */
	public String listUserMain() throws Exception{
		try{
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			// 获取用户查询的SQL语句，然后调用获取用户列表
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
							userStatus = "被管理员停用";
						}
						if("1".equals(o[8])){
							userStatus = "已删除";
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
							userStatus = "被管理员停用";
						}
						if("1".equals(o[8])){
							userStatus = "已删除";
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
			StringBuffer fileName = new StringBuffer("用户列表");
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
		ws = wb.createSheet("用户列表", 0);
		Label header1 = new Label(0, 0, "用户名", JXLTool.getHeader());
		Label header2 = new Label(1, 0, "用户中文名", JXLTool.getHeader());
		Label header3 = new Label(2, 0, "机构号", JXLTool.getHeader());
		Label header4 = new Label(3, 0, "机构简称", JXLTool.getHeader());
		Label header5 = new Label(4, 0, "用户状态", JXLTool.getHeader());
		Label header6 = new Label(5, 0, "最后登录时间", JXLTool.getHeader());
		Label header7 = new Label(6, 0, "角色", JXLTool.getHeader());
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
	 * <p> 方法名称: viewUser|描述: 通过用户编号获取用户、机构信息 </p>
	 * @return user 用户对象
	 * @return inst 机构对象
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
	 * <p> 方法名称: editUser|描述: 编辑界面通过用户编号获取用户信息 </p>
	 * @return userList 通过用户编号获取的用户信息
	 * @return instList 用于选择用户所属机构的机构列表
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
	 * <p> 方法名称: createUser|描述: 新增用户 </p>
	 * @return instList 用于选择用户所属机构的机构列表
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
	 * <p> 方法名称: deleteUser|描述:删除用户 </p>
	 * @return 成功页面
	 */
	public String deleteUser(){
		String ids = "";
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// 保存配置
			String returnMsg = "";
			/** **************用户修改审核****************** */
			if("1"
					.equals(cacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				for(int i = 0; userIdList != null && i < userIdList.length; i++){
					ids += userIdList[i] + ",";
				}
				userChangingService.saveBaseChanges(user, userIdList);
				returnMsg = "删除成功,待审核通过后生效";
				user.setDescription("删除用户" + ids + "," + returnMsg);
			}
			/** **************用户修改审核****************** */
			else{
				returnMsg = this.userService.deleteUser(this.userIdList, user
						.getUserId());
				user.setDescription("删除用户" + StringUtils.join(userIdList, ","));
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			if(StringUtils.isEmpty(returnMsg)){
				setResultMessages("删除成功。");
			}else{
				setResultMessages(returnMsg);
			}
		}catch (Exception e){
			if(e instanceof AuditException){
				setResultMessages(e.getMessage());
			}else{
				log.error(e);
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("删除用户" + ids + "失败");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"删除", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("删除失败。");
			}
		}
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: recoverUser|描述:恢复逻辑删除用户 </p>
	 * @return 成功页面
	 */
	public String recoverUser(){
		String ids = "";
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// 保存配置
			String returnMsg = "";
			returnMsg = this.userService.recoverUser(this.userIdList, user
					.getUserId());
			user.setDescription("恢复用户" + StringUtils.join(userIdList, ","));
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"恢复用户", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0002");
			sysLog.setMenuName("基础信息管理.用户管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			if(StringUtils.isEmpty(returnMsg)){
				setResultMessages("恢复成功。");
			}else{
				setResultMessages("恢复失败。");
			}
		}catch (Exception e){
			if(e instanceof AuditException){
				setResultMessages(e.getMessage());
			}else{
				log.error(e);
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("恢复用户" + ids + "失败");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"恢复用户", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("恢复失败。");
			}
		}
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: saveUser|描述: 获取用户信息保存用户 </p>
	 * @return 保存的结果值
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
				setResultMessages("用户名不能为空。");
				return INPUT;
			}
			if(user.getUserEname().length() > 20){
				setResultMessages("长度不能大于20");
				return INPUT;
			}
			String len = cacheManager
					.getParemerCacheMapValue("PARAM_SYS_USER_NAME_MIN_LEN");
			if(!"".equals(len)){
				try{
					int l = Integer.parseInt(len);
					if(user.getUserEname().length() < l){
						setResultMessages("用户名长度不能小于" + len);
						return INPUT;
					}
				}catch (Exception ex){
				}
			}
			Pattern p1 = Pattern.compile(Constants.PATTERN_PWD);
			Matcher m1 = p1.matcher(user.getUserEname());
			if(!m1.matches()){
				setResultMessages("用户名含有非法字符");
				return INPUT;
			}
			setResultMessages("保存成功。");
			if("add".equals(editType)){
				// 判断登录名是否被使用
				List listUser = userService.getUserInfoByEName(user
						.getUserEname());
				if(listUser.size() > 0){
					setResultMessages("用户登录名不能重复!");
					return INPUT;
				}
				// 初始化id生成器 王新修改 id为userEName 20090828
				String id = user.getUserEname();
				// 保存权限主体
				UAuthObjectDO obj = new UAuthObjectDO();
				obj.setObjectId(String.valueOf(id));
				obj.setObjectName(this.user.getUserCname());
				obj.setObjectType(DictionaryService.USER_AOT_DIC_TYPE);
				/** **************用户修改审核****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					// 屏蔽
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))){
						// 开启用户新增审核
						List list = userChangingService.getUserChanges(user
								.getUserEname());
						if(list.size() > 0){
							setResultMessages("审核表已存在新建用户["
									+ user.getUserEname() + "]");
							return INPUT;
						}
					}else{
						this.authObjectService.save(obj);
					}
				}
				/** **************用户修改审核****************** */
				else{
					this.authObjectService.save(obj);
				}
				loginUser.setDescription("保存权限主体" + obj.getObjectId());
				UBaseSysLogDO sysLog1 = this.sysLogService.setUBaseSysLog(
						loginUser, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog1.setMenuId("0002.0002");
				sysLog1.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog1);
				// 保存角色
				this.user.setUserId(String.valueOf(id));
				// modify by wangxin 20100423 修改默认密码
				String defPwd = "000000";
				String strPwd = cacheManager
						.getParemerCacheMapValue("PARAM_SYS_INITPWD");
				if(!"".equals(strPwd)){
					defPwd = strPwd;
				}
				this.user
						.setPassword(HexUtils.shaHex(defPwd, user.getUserId()));
				this.user.setIsFirstLogin("0"); // 0表示首次登陆
				this.user.setIsUserLocked(LoginUtil.LOCK_NORMAL); // 0表示用户未被锁定
				loginUser.setDescription("新增用户"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				/** **************用户修改审核****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					// 开启用户新增审核
					if("1"
							.equals(cacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))){
						userChangingService.saveBaseChanges(loginUser, user);
						setResultMessages("保存成功,待审核通过后生效");
						loginUser.addDescription(",保存成功,待审核通过后生效");
					}else{
						userService.saveUserByUserId(user);
					}
				}
				/** **************用户修改审核****************** */
				else{
					userService.saveUserByUserId(user);
				}
				UBaseSysLogDO sysLog2 = this.sysLogService.setUBaseSysLog(
						loginUser, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog2.setMenuId("0002.0002");
				sysLog2.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog2);
			}else{
				UBaseUserDO oUser = userService.getUser(user.getUserId());
				// 界面上的字段
				oUser.setDescription(user.getDescription());
				oUser.setEmail(user.getEmail());
				oUser.setMobile(user.getMobile());
				oUser.setTel(user.getTel());
				oUser.setUserCname(user.getUserCname());
				oUser.setAddress(user.getAddress());
				oUser.setInstId(user.getInstId());
				loginUser.setDescription("更新用户"
						+ AuthorityNameFetcher.fetchUserName(user.getUserId()));
				/** **************用户修改审核****************** */
				if("1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					UBaseUserDO originalUser = userService.getUserByUserId(user
							.getUserId());
					String s = userChangingService.saveBaseChanges(loginUser,
							oUser, originalUser);
					setResultMessages(StringUtils.isNotEmpty(s) ? s : "保存成功");
					loginUser.addDescription(StringUtils.isNotEmpty(s) ? s
							: "保存成功");
				}
				/** **************用户修改审核****************** */
				else{
					userService.updateUserByUserId(oUser);
				}
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
						loginUser, "更新", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("基础信息管理.用户管理");
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
				loginUser.setDescription("保存用户" + user.getUserId() + "失败");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
						loginUser, "保存", "0", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0002");
				sysLog.setMenuName("基础信息管理.用户管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
				setResultMessages("保存用户信息失败");
				if("add".equals(editType)){
					this.user.setUserId(null);
				}
			}
			return INPUT;
		}
	}

	/**
	 * 检查用户id是否存在，存在即可新建，不存在则不能建立该用户
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
			log.error("检查用户id失败!", e);
		}
	}

	public String editPswd(){
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: getInputStream|描述: 获取输入内容 </p>
	 * @return 获取输入内容
	 */
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	 * <p> 方法名称: setInputStream|描述: 设置输入内容 </p>
	 * @param inputStream 设置输入内容
	 */
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}

	/**
	 * <p> 方法名称: getUserService|描述: 设置用户服务 </p>
	 * @return 设置用户服务
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * <p> 方法名称: setUserService|描述: 设置用户服务 </p>
	 * @param userService 设置用户服务
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * <p> 方法名称: getUser|描述: 获取用户对象 </p>
	 * @return 获取用户对象
	 */
	public UBaseUserDO getUser(){
		return user;
	}

	/**
	 * <p> 方法名称: setUser|描述: 设置用户对象 </p>
	 * @param user 设置用户对象
	 */
	public void setUser(UBaseUserDO user){
		this.user = user;
	}

	/**
	 * <p> 方法名称: getUserList|描述: 获取用户列表 </p>
	 * @return 获取用户列表
	 */
	public List getUserList(){
		return userList;
	}

	/**
	 * <p> 方法名称: setUserList|描述: 设置用户列表 </p>
	 * @param userList 设置用户列表
	 */
	public void setUserList(List userList){
		this.userList = userList;
	}

	/**
	 * <p> 方法名称: getInst|描述: 获取机构对象 </p>
	 * @return 获取机构对象
	 */
	public UBaseInstDO getInst(){
		return inst;
	}

	/**
	 * <p> 方法名称: setInst|描述: 设置机构对象 </p>
	 * @param inst 设置机构对象
	 */
	public void setInst(UBaseInstDO inst){
		this.inst = inst;
	}

	/**
	 * <p> 方法名称: getResMap|描述: 获取结果Map </p>
	 * @return 获取结果Map
	 */
	public List getResMap(){
		return resMap;
	}

	/**
	 * <p> 方法名称: setResMap|描述: 设置结果Map </p>
	 * @param resMap 设置结果Map
	 */
	public void setResMap(List resMap){
		this.resMap = resMap;
	}

	/**
	 * <p> 方法名称: getSysConfig|描述: 获取系统配置 </p>
	 * @return 获取系统配置
	 */
	public List getSysConfig(){
		return sysConfig;
	}

	/**
	 * <p> 方法名称: setSysConfig|描述: 设置系统配置 </p>
	 * @param sysConfig 设置系统配置
	 */
	public void setSysConfig(List sysConfig){
		this.sysConfig = sysConfig;
	}

	/**
	 * <p> 方法名称: getRole|描述: 获取用户权限 </p>
	 * @return 获取用户权限
	 */
	public List getRole(){
		return role;
	}

	/**
	 * <p> 方法名称: setRole|描述: 设置用户权限 </p>
	 * @param role 设置用户权限
	 */
	public void setRole(List role){
		this.role = role;
	}

	/**
	 * <p> 方法名称: getHisPwd|描述: 获取用户历史密码 </p>
	 * @return 获取用户历史密码
	 */
	public List getHisPwd(){
		return hisPwd;
	}

	/**
	 * <p> 方法名称: setHisPwd|描述: 设置用户历史密码 </p>
	 * @param hisPwd 设置用户历史密码
	 */
	public void setHisPwd(List hisPwd){
		this.hisPwd = hisPwd;
	}

	/**
	 * <p> 方法名称: getLoginLog|描述: 获取用户登录日志 </p>
	 * @return 获取用户登录日志
	 */
	public List getLoginLog(){
		return loginLog;
	}

	/**
	 * <p> 方法名称: setLoginLog|描述: 设置用户登录日志 </p>
	 * @param loginLog 设置用户登录日志
	 */
	public void setLoginLog(List loginLog){
		this.loginLog = loginLog;
	}

	/**
	 * <p> 方法名称: getInstService|描述: 获取机构方法 </p>
	 * @return 获取机构方法
	 */
	public InstService getInstService(){
		return instService;
	}

	/**
	 * <p> 方法名称: setInstService|描述: 设置机构方法 </p>
	 * @param instService 设置机构方法
	 */
	public void setInstService(InstService instService){
		this.instService = instService;
	}

	/**
	 * <p> 方法名称: getInstList|描述: 获取机构列表 </p>
	 * @return 获取机构列表
	 */
	public List getInstList(){
		return instList;
	}

	/**
	 * <p> 方法名称: setInstList|描述: 设置机构列表 </p>
	 * @param instList 设置机构列表
	 */
	public void setInstList(List instList){
		this.instList = instList;
	}

	/**
	 * <p> 方法名称: getUserIdList|描述: 选中用户编号列表 </p>
	 * @param UserIdList 选中用户编号列表
	 */
	public String[] getUserIdList(){
		return userIdList;
	}

	/**
	 * <p> 方法名称: setUserIdList|描述: 选中用户编号列表 </p>
	 * @param UserIdList 选中用户编号列表
	 */
	public void setUserIdList(String[] userIdList){
		this.userIdList = userIdList;
	}

	/**
	 * @return 日志服务对象
	 */
	public UBaseSysLogService getSysLogService(){
		return sysLogService;
	}

	/**
	 * @param sysLogService 要设置的 日志服务对象
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
