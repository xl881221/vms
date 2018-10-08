package fmss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import fmss.dao.entity.LoginDO;

/**
 * <p>版权�?��:(C)2003-2010 </p>
 * @作�?: xindengquan
 * @日期: 2009-7-7 下午05:41:58
 * @描述: [PrivilegeService]用于用户登录时获得用户对应的权限信息
 */
public class PrivilegeService extends CommonService{

	private static final Map menuMap = new HashMap();
	private AuthRoleService authRoleService;
	private LoginService loginService;

	/**
	 * <p>方法名称: registMenu|描述: 把所有的菜单信息注册到内存中</p>
	 * @param userEname 登录�?
	 */
	public void registMenu(String userId, String userEname){
		menuMap.put("menuList", getAllMenuList(userId, userEname));
	}

	/**
	 * <p>方法名称: getMenuList|描述: 取得该用户能操作的所有菜单信�?/p>
	 * @param userEname 登录�?
	 * @return 该用户对应的菜单列表
	 */
	public List getAllMenuList(String userId, String userEname){
		String querySql = "";
		List menuLst = null;
		if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userEname)){
			querySql = "select new Map(res.objectId as objectId,res.resId as resId,"
					+ " res.resDetailValue as resDetailValue,res.resDetailName as resDetailName,res.systemId as systemId) "
					+ " from UAuthRoleResourceDO res left join res.resMap map "
					+ " where (res.objectId in (select user.userId from UBaseUserDO user where user.userEname='"
					+ userEname
					+ "')"
					+ " or res.objectId in (select role.roleId from UAuthRoleUserDO role,UAuthRoleDO roleDes  where role.roleId=" +
							"roleDes.roleId and roleDes.enabled='1' and role.userId='"
					+ userId
					+ "')) and map.srcTable ='u_base_menu' and res.systemId<>'00003'";
			menuLst = this.find(querySql); // 取得菜单信息列表
		}
		String hql = "select a.systemId from UAuthSystemAdminDO a where a.userId='"
				+ userId + "'";
		List admins = find(hql);
		if(admins != null && !admins.isEmpty()){
			int isAdmin = 0;
			for(int i = 0; i < admins.size(); i++){
				String sysId = admins.get(i).toString();
				if("00003".equals(sysId)){
					isAdmin = 1;
					break;
				}
			}
			if(menuLst == null)
				menuLst = new ArrayList();
			menuLst.addAll(this.getAdminMenus(isAdmin));
		}
		return menuLst;
	}
	public Map getUserPrivs(String userId, String systemId){
		
		List list=null;
		List list1=null;
		if(!"00003".equals(systemId)){
			String query = " select menu.url "
				+ " from MenuDO menu,UAuthRoleResourceDO res left join res.resMap map " 
				+ " where (res.objectId  in (select user.userId from UBaseUserDO user where user.userId=?)"
				+ " or res.objectId in (select role.roleId from UAuthRoleUserDO role,UAuthRoleDO roleDes  where role.roleId=" +
						"roleDes.roleId and roleDes.enabled='1' and role.userId=?)) and map.srcTable ='u_base_menu' and res.systemId=? " +
						" and menu.itemcode=res.resDetailValue and res.systemId=menu.systemId and menu.url is not null ";
			String query1="select new Map(a1.url as url) from MenuDO a1,UAuthRoleResourceDO a2 left join a2.resMap a3 where a3.srcTable ='u_base_menu' " +
					"and a2.systemId=? and  a1.itemcode=a2.resDetailValue and a2.systemId=a1.systemId " +
					" and url not in ("+query+")";
		    list=this.find(query1, new Object[]{systemId,userId,userId,systemId});
		    list1=this.find(query, new Object[]{userId,userId,systemId});
		}else{
			list=this.getAdminMenus(1);
		}
		HashMap privs = new HashMap();
		if(!"00003".equals(systemId)){
			for(int i=0;i<list1.size();i++){//白名�?
		    	String url=(String)list1.get(i);
		    	if(StringUtils.isNotEmpty(url)){
		    		url=url.replaceFirst("^http[^/]+//[^/]+/[^/]+/","");
		    		privs.put(url, "true");
		    	}
		    }
			for(int i=0;i<list.size();i++){//黑名�?
		    	Map map=(Map)list.get(i);
		    	String url=(String) map.get("url");
		    	if(StringUtils.isNotEmpty(url)){
		    		url=url.replaceFirst("^http[^/]+//[^/]+/[^/]+/","");
		    		privs.put(url, "false");
		    	}
		    	
		    }
    	}else{
    		int systemType=authRoleService.getAdminType(userId);
    		LoginDO user = loginService.checkUserById(userId);
    		for(int i=0;i<list.size();i++){
		    	Map map=(Map)list.get(i);
		    	String url=(String) map.get("url");
		    	if(StringUtils.isNotEmpty(url)){
		    		url=url.replaceFirst("^http[^/]+//[^/]+/[^/]+/","");
		    		if(!validataMenu(user,systemType,(String)map.get("systemId"),(String)map.get("resDetailValue"))){
			    		privs.put(url, "false");//黑名�?
			    	}else{
			    		privs.put(url, "true");//白名�?
			    	}
		    	}

		    }
    	}
		
		return privs;
	}
	public boolean validataMenu(LoginDO user,int systemType,String systemId,String menuCode){
		String str1="0001.9999,0002.9999,0002.0002,0002.0003,0003.0001,0002.0005,0002.0006,0002.0007,0002.9998,0003.0005";//分行usys管理员可访问的usys菜单
        String str2="0002.9999,0002.0002,0002.0003,0003.0001,0002.0005,";//分行其他系统管理员可访问的usys菜单
        String str3="0002.0010,0002.9999,0002.0009,0002.0002,0002.0003,0002.0004,0003.0001,0002.0005,0002.0006,0002.0007,";//总行其他系统管理员可访问的usys菜单

		if(!user.getUserId().equals("admin")){
			if(systemId.equals("00003")&&systemType==1&&!"true".equals(user.getInstIsHead())){
				//分行usys管理�?
				if(str1.indexOf(menuCode+",")==-1){
					return false;
				}
			}else if(systemId.equals("00003")&&systemType==2&&!"true".equals(user.getInstIsHead())){
				//分行其他系统管理�?
				if(str2.indexOf(menuCode+",")==-1){
					return false;
				}
			}else if(systemId.equals("00003")&&systemType==2&&"true".equals(user.getInstIsHead())){
				//总行其他管理�?
				if(str3.indexOf(menuCode+",")==-1){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * <p>方法名称: getSubSystemList|描述: 取得用户对应的子系统信息</p>
	 * @return List
	 */
	public List getSubSystemList(String userId){
		List subsysList = new ArrayList();
		List menuAllList = (List) menuMap.get("menuList");
		List sysIdList = new ArrayList();
		/*
		 * 获得子系统id列表
		 */
		if(menuAllList != null){
			for(int i = 0; i < menuAllList.size(); i++){
				Map temp = (Map) menuAllList.get(i);
				if(!sysIdList.contains(temp.get("systemId"))){
					sysIdList.add(temp.get("systemId"));
				}
			}
		}
		//获取从外部读取的菜单的子系统的系统编�?
	/*	
		String hql = "select t.systemId from UBaseConfigDO t, UAuthRoleDO ar   where "
		           + "  t.systemId = ar.systemId  and ar.roleId in (select roleId from UAuthRoleUserDO s "
		           + " where userId='"+userId+"')  and ar.enabled<>'false'  and t.display='true' and t.enabled='true' and  t.menuRes='1' ";
		
		List OtherList = this.find(hql);
		for(int j=0;j<OtherList.size();j++)
			sysIdList.add(OtherList.get(j));*/
		/*
		 * 形成查询条件，查出子系统信息列表
		 */

		String condition = "('',";
		for(int i = 0; i < sysIdList.size(); i++){
			condition = condition + "'" + sysIdList.get(i) + "'" + ",";
		}
		condition = condition.substring(0, condition.length() - 1)
				+ ") and display <>'false' and enabled ='true') or (conf.menuRes='1' and conf.enabled='true' and conf.display='true') order by conf.menuOrderNum";
		subsysList = this
				.find("from UBaseConfigDO as conf where ( conf.systemId in "
						+ condition);
		
		return subsysList;
	}

	/**
	 * <p>方法名称: getMenuList|描述: 取得子系统对应的菜单信息</p>
	 * @param systemId 子系统编�?
	 * @return list
	 */
	public List getMenuList(String systemId){
		List menuList = new ArrayList();
		List menuAllList = (List) menuMap.get("menuList");
		if(menuAllList != null){
			for(int i = 0; i < menuAllList.size(); i++){
				Map temp = (Map) menuAllList.get(i);
				if((systemId != null) && systemId.equals(temp.get("systemId"))){
					menuList.add(temp);
				}
			}
		}
		return menuList;
	}

	private List getAdminMenus(int isAdmin){
		String hql = "select new Map(m.itemcode as resDetailValue,m.itemname as resDetailName,m.systemId as systemId,m.url as url)"
				+ " from MenuDO m where m.systemId='00003'";
//		if(isAdmin != 1){
//			hql += " and m.itemcode not in ("+IMPORTANT_MENUS+")";
//		}
		return this.find(hql);
	}
	
	public static final String IMPORTANT_MENUS = "'0001.0001','0002.0001','0003','0003.0001','0003.0003','0002.0008'";

	/**
	 * <p>方法名称: getMenuMap|描述: 取得菜单map</p>
	 * @return map
	 */
	public static Map getMenuMap(){
		return menuMap;
	}

	
	public void setAuthRoleService(AuthRoleService authRoleService){
		this.authRoleService = authRoleService;
	}

	
	public void setLoginService(LoginService loginService){
		this.loginService = loginService;
	}
}
