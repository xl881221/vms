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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:13:53
 * @描述: [AuthorityAction]角色权限管理Action
 */
public class AuthorityAction extends BaseAction/* implements Preparable*/{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String APPEND_MESSAGE = "，待审核通过后生效 !";
	/** logger 日志对象 */
	private static final Logger logger = Logger
			.getLogger(AuthorityAction.class);
	/** roleList 角色列表 */
	private List roleList;
	/** authRoleResList 角色资源列表 */
	private List authRoleResList;
	/** authObjList 权限主体列表 */
	private List authObjList;// 
	/** userArray 对应用户列表数组 */
	private String[] userArray;
	/** allUserArray 对应用户列表数组 */
	private String[] allUserArray;
	/** userList 对应用户列表 */
	private List userList;
	/** allUserList 所有用户列表 */
	private List allUserList;
	/** allResMap 所有资源列表 */
	private List allResMap;
	/** resMap 角色对应资源列表 */
	private List resMap;
	/** userSystemList 用户子系统列表 */
	private List userSystemList;
	/** userRoleList 用户角色列表 */
	private List userRoleList;
	/** resMapArray 角色对应资源数组 */
	private String[] resMapArray;
	/** authRoleService 角色服务 */
	private AuthRoleService authRoleService;
	/** idGenerator id生成器 */
	private IdGenerator idGenerator = IdGenerator.getInstance(DictionaryService.AUTH_AOT_DIC_TYPE); // id生成器
	/** sysLogService 日志服务 */
	private UBaseSysLogService sysLogService;
	
	private LogManagerService logManagerService;
	
	/** dicService 字典服务 */
	private DictionaryService dicService;
	/** userService 用户服务 */
	private UserService userService;
	/** authObjectService 权限主体服务 */
	private AuthObjectService authObjectService;
	/** role 角色 */
	private UAuthRoleDO role = new UAuthRoleDO();
	/** roleName 参数1 */
	private String roleName;
	/** roleId 参数2 */
	private String roleId;	
	/** formType 返回哪个页面*/
	private String returnView;
	/** inputStream 参数3输出流 */
	private InputStream inputStream;
	/** id 参数4 子系统id */
	private String id;
	/** sysList 子系统列表 */
	private List sysList;
	/** user 参数 用户配置查询 */
	private UBaseUserDO user;
	/** authResMap 参数 资源配置查询 */
	private UAuthResMapDO authResMap;
	/** resTypeDic 资源类型字典 */
	private List resTypeDic;
	/** view 参数 查看 */
	private boolean view = false;
	/** isSuccess 是否成功 */
	private String isSuccess;

	/** FORM_AUTH 跳转页面 */
	protected static final String FORM_AUTH = "formAuth";
	/** VIEW_AUTH_ROLE_RES 跳转页面 */
	protected static final String VIEW_AUTH_ROLE_RES = "viewAuthRoleRes";
	
	private UserChangingService userChangingService;//用户修改审核service
	private CacheManager cacheManager; // 缓存
	
	private boolean fixQuery = false;
	public boolean isFixQuery() {
		return fixQuery;
	}

	public void setFixQuery(boolean fixQuery) {
		this.fixQuery = fixQuery;
	}

	/**
	 * @return 角色列表
	 */
	public List getRoleList(){
		return roleList;
	}

	/**
	 * @param roleList 要设置的 角色列表
	 */
	public void setRoleList(List roleList){
		this.roleList = roleList;
	}

	/**
	 * @return 角色服务对象
	 */
	public AuthRoleService getAuthRoleService(){
		return authRoleService;
	}

	/**
	 * @param authRoleService 要设置的 角色服务对象
	 */
	public void setAuthRoleService(AuthRoleService authRoleService){
		this.authRoleService = authRoleService;
	}

	/**
	 * @return 角色名
	 */
	public String getRoleName(){
		return roleName;
	}

	/**
	 * @param roleName 要设置的 角色名
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}

	/**
	 * <p>方法名称: listAuth|描述:角色列表 </p>
	 * @return 成功页面
	 */
	public String listAuth(){
		try{
			// 取得所有角色信息
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
	 * <p>方法名称: createAuth|描述:新建角色 </p>
	 * @return 成功页面
	 */
	public String createAuth(){
		try{
			// 取子系统编号    根据登录用户过滤子系统
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = new UBaseUserDO();
			user.setUserId(loginUser.getUserId());
			// 取用户的子系统列表
			this.sysList = this.authRoleService.getBaseConfig(user);
			
			return FORM_AUTH;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: editAuth|描述:编辑角色 </p>
	 * @return 成功页面
	 */
	public String editAuth(){
		try{
			// 获取角色信息
			this.roleList = this.authRoleService.getRoleById(this.roleId);
			if(this.roleList != null && this.roleList.size() > 0){
				this.role = (UAuthRoleDO) this.authRoleService.getRoleById(
						this.roleId).get(0);
			}
			// 取子系统编号   根据登录用户过滤子系统
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = new UBaseUserDO();
			user.setUserId(loginUser.getUserId());
			// 取用户的子系统列表
			this.sysList = this.authRoleService.getBaseConfig(user);
			
			this.setIsSuccess("update");
			return FORM_AUTH;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: viewAuthRoleRes|描述:查看角色权限资源列表 </p>
	 * @return 成功页面
	 */
	public String viewAuthRoleRes(){
		try{
			this.roleList = this.authRoleService.getRoleById(this.roleId);
			if(this.roleList != null && this.roleList.size() > 0){
				this.role = (UAuthRoleDO)roleList.get(0);
			}
			// 取得角色对应权限资源
			this.authRoleResList = this.authRoleService
					.getAuthRoleResourceByObjectId(this.roleId);
			return VIEW_AUTH_ROLE_RES;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: saveAuth|描述:保存角色 </p>
	 * @return 成功页面
	 * @throws Exception 
	 */
	public String saveAuth() throws Exception{
		try{
			List list = this.authRoleService.getRoleById(this.role.getRoleId());
			// 取子系统编号
			this.sysList = this.authRoleService.getSystemList(false);
			if(list != null && list.size() > 0){
				this.setIsSuccess("saveNo");
			}else{
				// 初始化id生成器
				long id = idGenerator.getNextKey();
				// 保存权限主体
				UAuthObjectDO obj = new UAuthObjectDO();
				obj.setObjectId(String.valueOf(id));
				obj.setObjectName(this.role.getRoleName());
				obj.setObjectType(DictionaryService.ROLE_AOT_DIC_TYPE);
				this.authObjectService.save(obj);
				// 保存角色
				this.role.setRoleId(String.valueOf(id));
				this.authRoleService.save(this.role);
				// 日志操作
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("新增角色:" + this.role.getRoleName());
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"新增","1",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0003");
				sysLog.setMenuName("基础信息管理.角色人员管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
				this.setIsSuccess("saveYes");
				this.setResultMessages( "保存成功");
				return SUCCESS;
			}
		}catch (Exception e){
			try {
				LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
				user.setDescription("新增角色:" + this.role.getRoleName()+"时，保存失败");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"新增","0",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0003");
				sysLog.setMenuName("基础信息管理.角色人员管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
				logger.error("",e);
				setResultMessages("保存失败");
			} catch (Exception e1) {
				logger.error(e1);
				throw e1;
			}
		}
		return INPUT;
	}

	/**
	 * <p>方法名称: updateAuth|描述:更新角色</p>
	 * @return 成功页面
	 */
	public String updateAuth(){
		try{
			// 更新角色
			this.authRoleService.update(this.role);
			// 日志操作
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("修改角色:" + this.role.getRoleName());
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"更新","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			// 取子系统编号
			this.sysList = this.authRoleService.getSystemList(false);
			// 设置跟新成功标志
			this.setIsSuccess("updateYes");
			this.setResultMessages("更新成功");
			return SUCCESS;
		}catch (Exception e){
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("更新角色:" + this.role.getRoleName()+"时，更新失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"更新","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
			this.setResultMessages("更新失败");
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
	 * <p>方法名称: deleteAuth|描述:删除角色 </p>
	 * @return 成功页面
	 */
	public String deleteAuth(){
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			user.setDescription("删除角色信息[" + AuthorityNameFetcher.fetchRoleName(roleId) + "]");
			this.role.setRoleId(this.roleId);
			// 删除角色对应的资源
			//this.authRoleService.deleteConfigRes(this.role);
			// 删除角色对应的用户
			//this.authRoleService.deleteConfigUser(this.role);
			
			//查找角色对应的资源
			List res = authRoleService.getResByRole(this.role);
			// 查找角色对应的用户
			List users = authRoleService.getUserByRole(this.role);		 
				if(!((users!=null&&users.size()>0)||(res!=null&&res.size()>0))){
					// 删除角色
					this.authRoleService.delete(this.role);
					this.setResultMessages("删除成功！");
					this.isSuccess = "success";
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"删除","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0003");
					sysLog.setMenuName("基础信息管理.角色人员管理");
					this.sysLogService.saveUBaseSysLog(sysLog);
					return "1".equals(returnView)?INPUT:SUCCESS;	//modify by wangxin 20091110 处理页面跳转错误问题 
				}
		}catch (Exception e){
			logger.error("",e);
		}
		this.setResultMessages("角色删除失败");
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("删除角色信息[" + this.roleId + "]失败");
		UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
				"删除","0",Constants.BASE_SYS_LOG_AUTHORITY);
		sysLog.setMenuId("0002.0003");
		sysLog.setMenuName("基础信息管理.角色人员管理");
		this.sysLogService.saveUBaseSysLog(sysLog);
		this.setResultMessages("删除失败,该角色已分配权限或是已分配给用户！");
		return "input";
	}
	
	
	

	/**
	 * <p>方法名称: viewUserByRole|描述:查看该角色下现有配置用户 </p>
	 * @return 成功页面
	 */
	public String viewUserByRole(){
		try{
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// 根据角色获取用户
			this.userList = this.authRoleService.getUserByRole(this.role,
					this.paginationList,user.getInstId(),this.user,fixQuery);
			return SUCCESS;
		}catch (Exception e){
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: configUserByRole|描述:配置角色用户界面 </p>
	 * @return 成功页面
	 */
	public String configUserByRole(){
		try{
			// 根据角色获取没有配置的用户
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userList = this.authRoleService.getLevUserByRole(this.role,user.getInstId(),this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: saveUserByRole|描述:保存用户配置 </p>
	 * @return 成功页面
	 */
	public String saveUserByRole(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("保存角色人员信息,角色[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "],用户[" + StringUtils.join(AuthorityNameFetcher.fetchUserName(userList).iterator(), ",") + "]");
		try{
			/****************用户修改审核*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.role, this.userList, true);
				if (StringUtils.isNotEmpty(s)) {
					this.setResultMessages( s);
					user.addDescription("," + s);
				} else {
					this.setResultMessages( "保存成功" + APPEND_MESSAGE);
					user.addDescription("," + "保存成功" + APPEND_MESSAGE);
				}
				
			}
			/****************用户修改审核*******************/
			else{
				// 保存配置,可以增加，删除
				this.authRoleService.saveConfigUser(this.userList, this.role);
				this.setResultMessages("保存成功");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: deleteUserByRole|描述:删除用户角色关系 </p>
	 * @return 成功页面
	 */
	public String deleteUserByRole(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("删除角色人员信息" + ",角色[" + AuthorityNameFetcher.fetchRoleName(role.getRoleId()) + "],用户[" + StringUtils.join(AuthorityNameFetcher.fetchUserName(userList).iterator(), ",") + "]");
		try{
			/****************用户修改审核*******************/
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
				String s = userChangingService.saveRoleChanges(user, this.role, this.userList, false);
				if (StringUtils.isNotEmpty(s)) {
					setResultMessages(s);
				} else {
					setResultMessages("删除成功" + APPEND_MESSAGE);
				}
				user.addDescription(StringUtils.isNotEmpty(s) ? ("," + s) : "删除成功" + APPEND_MESSAGE);
			}
			/****************用户修改审核*******************/
			// 保存配置
			else{
				this.authRoleService.deleteConfigUser(this.userList, this.role);
				setResultMessages("删除成功");
			}
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"删除","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			return SUCCESS;
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"删除","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0003");
			sysLog.setMenuName("基础信息管理.角色人员管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			logger.error("",e);
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: listRoleHead|描述:显示角色视图头部 </p>
	 * @return 成功页面
	 */
	public String listRoleHead(){
		return SUCCESS;
	}

	/**
	 * <p>方法名称: listRoleTree|描述:显示角色树 </p>
	 * @return 成功页面
	 */
	public String listRoleTree(){
		return SUCCESS;
	}

	/**
	 * <p>方法名称: listRoleMain|描述:显示角色视图主页面 </p>
	 * @return 成功页面
	 */
	public String listRoleMain(){
		return SUCCESS;
	}

	/**
	 * <p>方法名称: getRoleTree|描述: 取角色树xml</p>
	 */
	public void getRoleTree(){
		// 取子系统
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// 取用户的子系统列表
		this.sysList = this.authRoleService.getBaseConfig(user);
		// modify by wangxin 由于更换数控件，修改xml结构
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
	 * 添加该方法仅仅为了兼容IE11的角色树
	 * <p>方法名称: getRoleZTree|描述: 取角色树xml</p>
	 */
	public void getRoleZTree() {
		// 取子系统
		LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
		user = new UBaseUserDO();
		user.setUserId(loginUser.getUserId());
		// 取用户的子系统列表
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
	 * <p>方法名称: queryConfigUser|描述:带条件的配置用户查询 </p>
	 * @return 成功页面
	 */
	public String queryConfigUser(){
		try{
			LoginDO loginUser = (LoginDO) super.get(Constants.LOGIN_USER);
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// 带用户输入条件的配置用户查询
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
	 * @return 成功页面
	 * */
	public String queryConfigUserLev(){
		try{
			// 取角色信息
			this.role = this.authRoleService.getRoleByRoleId(this.role
					.getRoleId());
			// 带用户输入条件的配置用户查询
			this.userList = this.authRoleService.getHavLevUserByRole(this.user,
					this.role,this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * @return 角色对应资源列表
	 */
	public List getAuthRoleResList(){
		return authRoleResList;
	}

	/**
	 * @param authRoleResList 要设置的 角色对应资源列表
	 */
	public void setAuthRoleResList(List authRoleResList){
		this.authRoleResList = authRoleResList;
	}

	/**
	 * @return 权限主体列表
	 */
	public List getAuthObjList(){
		return authObjList;
	}

	/**
	 * @param authObjList 要设置的 权限主体列表
	 */
	public void setAuthObjList(List authObjList){
		this.authObjList = authObjList;
	}

	/**
	 * @return 用户数组
	 */
	public String[] getUserArray(){
		return userArray;
	}

	/**
	 * @param userArray 要设置的 用户数组
	 */
	public void setUserArray(String[] userArray){
		this.userArray = userArray;
	}

	/**
	 * @return 所有用户数组
	 */
	public String[] getAllUserArray(){
		return allUserArray;
	}

	/**
	 * @param allUserArray 要设置的 所有用户数组
	 */
	public void setAllUserArray(String[] allUserArray){
		this.allUserArray = allUserArray;
	}

	/**
	 * @return 用户列表
	 */
	public List getUserList(){
		return userList;
	}

	/**
	 * @param userList 要设置的 用户列表
	 */
	public void setUserList(List userList){
		this.userList = userList;
	}

	/**
	 * @return 所有用户列表
	 */
	public List getAllUserList(){
		return allUserList;
	}

	/**
	 * @param allUserList 要设置的 所有用户列表
	 */
	public void setAllUserList(List allUserList){
		this.allUserList = allUserList;
	}

	/**
	 * @return 所有资源信息
	 */
	public List getAllResMap(){
		return allResMap;
	}

	/**
	 * @param allResMap 要设置的 所有资源信息
	 */
	public void setAllResMap(List allResMap){
		this.allResMap = allResMap;
	}

	/**
	 * @return 资源信息
	 */
	public List getResMap(){
		return resMap;
	}

	/**
	 * @param resMap 要设置的 资源信息
	 */
	public void setResMap(List resMap){
		this.resMap = resMap;
	}

	/**
	 * @return 用户子系统列表
	 */
	public List getUserSystemList(){
		return userSystemList;
	}

	/**
	 * @param userSystemList 要设置的 用户子系统列表
	 */
	public void setUserSystemList(List userSystemList){
		this.userSystemList = userSystemList;
	}

	/**
	 * @return 用户角色列表
	 */
	public List getUserRoleList(){
		return userRoleList;
	}

	/**
	 * @param userRoleList 要设置的 用户角色列表
	 */
	public void setUserRoleList(List userRoleList){
		this.userRoleList = userRoleList;
	}

	/**
	 * @return 资源数组
	 */
	public String[] getResMapArray(){
		return resMapArray;
	}

	/**
	 * @param resMapArray 要设置的 资源数组
	 */
	public void setResMapArray(String[] resMapArray){
		this.resMapArray = resMapArray;
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

	/**
	 * @return 字典服务对象
	 */
	public DictionaryService getDicService(){
		return dicService;
	}

	/**
	 * @param dicService 要设置的 字典服务对象
	 */
	public void setDicService(DictionaryService dicService){
		this.dicService = dicService;
	}

	/**
	 * @return 用户服务对象
	 */
	public UserService getUserService(){
		return userService;
	}

	/**
	 * @param userService 要设置的 用户服务对象
	 */
	public void setUserService(UserService userService){
		this.userService = userService;
	}

	/**
	 * @return 权限主体服务对象
	 */
	public AuthObjectService getAuthObjectService(){
		return authObjectService;
	}

	/**
	 * @param authObjectService 要设置的 权限主体服务对象
	 */
	public void setAuthObjectService(AuthObjectService authObjectService){
		this.authObjectService = authObjectService;
	}

	/**
	 * @return 角色id
	 */
	public String getRoleId(){
		return roleId;
	}

	/**
	 * @return 角色对象
	 */
	public UAuthRoleDO getRole(){
		return role;
	}

	/**
	 * @param role 要设置的 角色对象
	 */
	public void setRole(UAuthRoleDO role){
		this.role = role;
	}

	/**
	 * @param roleId 要设置的 角色id
	 */
	public void setRoleId(String roleId){
		this.roleId = roleId;
	}

	/**
	 * @return 输出流
	 */
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	 * @param inputStream 要设置的 输出流
	 */
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}

	/**
	 * @return 树节点id
	 */
	public String getId(){
		return id;
	}

	/**
	 * @param id 要设置的 树节点id
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * @return 子系统列表
	 */
	public List getSysList(){
		return sysList;
	}

	/**
	 * @param sysList 要设置的 子系统列表
	 */
	public void setSysList(List sysList){
		this.sysList = sysList;
	}

	/**
	 * @return 用户对象
	 */
	public UBaseUserDO getUser(){
		return user;
	}

	/**
	 * @param user 要设置的 用户对象
	 */
	public void setUser(UBaseUserDO user){
		this.user = user;
	}

	/**
	 * @return 资源信息
	 */
	public UAuthResMapDO getAuthResMap(){
		return authResMap;
	}

	/**
	 * @param authResMap 要设置的 资源信息
	 */
	public void setAuthResMap(UAuthResMapDO authResMap){
		this.authResMap = authResMap;
	}

	/**
	 * @return 资源字典列表
	 */
	public List getResTypeDic(){
		return resTypeDic;
	}

	/**
	 * @param resTypeDic 要设置的 资源字典列表
	 */
	public void setResTypeDic(List resTypeDic){
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return 是否查看
	 */
	public boolean isView(){
		return view;
	}

	/**
	 * @param view 要设置的 是否能查看
	 */
	public void setView(boolean view){
		this.view = view;
	}

	/**
	 * @return isSuccess 成功信息
	 */
	public String getIsSuccess(){
		return isSuccess;
	}

	/**
	 * @param isSuccess 要设置的 成功信息
	 */
	public void setIsSuccess(String isSuccess){
		this.isSuccess = isSuccess;
	}

	/**
	 * @param returnView 返回哪个页面 1:返回listResTreeRole  否则:listRoleTree.jsp
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
