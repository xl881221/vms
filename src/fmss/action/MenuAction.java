package fmss.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import common.crms.util.encode.Base64;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.MenuDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.common.cache.SystemCache;
import fmss.services.AuthRoleService;
import fmss.services.PrivilegeService;
import fmss.common.util.Constants;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: xindengquan
 * @日期: 2009-6-27 下午02:01:41
 * @描述: [MenuAction] 获取子系统菜单
 */
public class MenuAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	/** 子系统E名称 */
	private String subSysName;
	/** 子系统id */
	private String subSysId;
	/** cacheManager */
	private CacheManager cacheManager;
	/** systemCache */
	private SystemCache systemCache;
	/** privilegeService */
	private PrivilegeService privilegeService;

	private AuthRoleService authRoleService;

	/** 菜单列表 */
	private List menuList;
	private String menuHTML ="";

	public String execute() throws Exception {
		
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject(CacheManager.SUB_SYS_MENU_MAP);
		LoginDO user = (LoginDO) ServletActionContext.getRequest().getSession()
				.getAttribute("LOGIN_USER"); // 获得内存用户

		//subSysId = SecurityPassword.filterStr(subSysId);
		// subSysName = SecurityPassword.filterStr(subSysName);

		List menuAllLst = (List) cache.get(subSysName); // 该子系统所有菜单列表 xzw
		List menuInLst = new ArrayList(); // 存放菜单列表
		// 取得FMSS的访问地址
		String defaultIP = request.getSession()
				.getAttribute("defaultIPAddress").toString();
		String defaultPort = request.getSession().getAttribute("defaultPort")
				.toString();
		StringBuffer url = request.getRequestURL();
		String parentUrl = url.substring(0, 1 + url.lastIndexOf("/"));
		String parentInnerUrl = getURL(
				cacheManager
						.getParemerCacheMapValue(Constants.PARAM_USYS_INNER_URL),
				defaultIP, defaultPort);
		String sysTheme = (String) request.getSession().getAttribute(
				Constants.SESSION_THEME_KEY);

		String tempUrl = "";
		// if(PrivilegeService.getMenuMap().isEmpty()){
		privilegeService.registMenu(user.getUserId(), user.getUserEname());
		// }
		List privmenuLst = privilegeService.getMenuList(subSysId); // 用户能访问的菜单编号列表
		// 子系统是否验证ip参数(1-是0-否)
		String validateIP = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_IP_VALIDATE);

		/*
		 * 测试IP下载限制 String testHOstName = "testHostName";
		 * request.getSession().setAttribute("QASComputerName",testHOstName);
		 */

		// 主机名称
		Object computerName = request.getSession().getAttribute(
				"QASComputerName");

		// 管理员类型
		int systemType = authRoleService.getAdminType(user.getUserId());
		
		// 设置用户能访问的菜单列表
		if (menuAllLst != null && user != null) {
			menuHTML ="";
			if(menuAllLst.size()>=1)
				menuHTML += "<ul class='leftmenu'>";
			for (int j = 0; j < menuAllLst.size(); j++) { // 取得用户能操作的一级菜单列表
				
				// 2013-01-23
				boolean isShowMenu = false;
				String subMenuTemp = "";
				// 2013-01-23
				
				MenuDO mn = (MenuDO) menuAllLst.get(j);
				MenuDO outer = new MenuDO();
				if(mn.getSubMenuList().size()>=1)
				{
					List innerLst = new ArrayList();
					
					for (int k = 0; k < mn.getSubMenuList().size(); k++) { // 取得用户能操作的二级菜单列表
						
						// xzw
						MenuDO menu = (MenuDO) mn.getSubMenuList().get(k);
						MenuDO inner = new MenuDO();
						for (int i = 0; i < privmenuLst.size(); i++) {
							Map temp = (Map) privmenuLst.get(i);
							String itemCode = (String) temp.get("resDetailValue");
	
							// modify by wangxin 20090825 过滤不显示的菜单
							if (Constants.STR_NO.equals(menu.getDisplay()))
								continue;
							if (!validataMenu(user, systemType, menu.getSystemId(),
									menu.getItemcode()))
								continue;
	
							if (itemCode.equals(menu.getItemcode())) {
								BeanUtils.copyProperties(menu, inner);
								String menuUrl = this.isInnerNet(request) ? inner
										.getInnerUrl() : inner.getUrl();
								tempUrl = (-1 == (menuUrl != null ? menuUrl : "")
										.indexOf("?")) ? "?" : "&";
								tempUrl = menuUrl
										+ tempUrl
										+ "usdfsddf=11232dfd0091232&loginId="
										+ encode(user.getLoginId())
										+ "&userId="
										+ encode(user.getUserId())
										+ "&userEname="
										+ encode(user.getUserEname())
										+ "&subSystemId="
										+ encode(menu.getSystemId())
										+ "&systemId="
										+ encode(menu.getSystemId())
										+ "&locale="
										+ ("c".equals(user.getLanguage()) ? "zh_CN"
												: "en_US") + "&parentUrl="
										+ encode(parentUrl) + "&parentInnerUrl="
										+ encode(parentInnerUrl);
								if (!StringUtils.isEmpty(sysTheme)) {
									tempUrl += "&" + Constants.SESSION_THEME_KEY
											+ "=" + encode(sysTheme);
								}
								if ("1".equals(validateIP)) {
									tempUrl += "&validateIP=" + validateIP;
								}
								
								tempUrl = getURL(tempUrl, defaultIP, defaultPort);
	
								if (computerName != null) {
									tempUrl += "&hostName="
											+ encode(computerName.toString());
								}
	
								inner.setUrl(tempUrl);
								innerLst.add(inner);
								subMenuTemp += "<li><a target=\"mainFramePage\" href=\"" + tempUrl + "\" >" + menu.getItemname() + "</a></li>";
								// 2013-01-23
								isShowMenu = true;
								// 2013-01-23
								break;
							}
							
						}
						
					}
					
					
					// 只有把存在二级菜单的一级菜单放入菜单列表
					if (innerLst.size() > 0) {
						BeanUtils.copyProperties(mn, outer);
						outer.setSubMenuList(innerLst);
						menuInLst.add(outer);
					}
					// 2013-01-23					
					if(isShowMenu){
						menuHTML += "<li><a href=\"#\">" + mn.getItemname() + "</a><ul class='dropmenu'>" + subMenuTemp + "</ul></li>";
					}
					
					//test
					if ( "红冲管理".equals(mn.getItemname()) ) {
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println(mn.getSubMenuList().size());
						System.out.println( mn.getSubMenuList() );
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println("--------------------------------------------------------------------------------------------------------------------");
						System.out.println( innerLst.size());
						System.out.println( innerLst );
						
					}
					//test
				}
			}
			if(menuAllLst.size()>=1)
				menuHTML += "</ul>";
		}
		// 设置页面中菜单列表
		if (menuInLst != null && menuInLst.isEmpty()) {
			menuList = null;
		} else {
			menuList = menuInLst;
		}
		request.setAttribute("menuList", menuList);
		request.setAttribute("menuHTML", menuHTML);
		return SUCCESS;
	}

	public boolean validataMenu(LoginDO user, int systemType, String systemId,
			String menuCode) {
		String str1 = "0001.9999,0002.9999,0002.0002,0002.0003,0003.0001,0002.0005,0002.0006,0002.0007,0002.9998,0003.0005";// 分行usys管理员可访问的usys菜单
		String str2 = "0002.9999,0002.0002,0002.0003,0003.0001,0002.0005,";// 分行其他系统管理员可访问的usys菜单
		String str3 = "0002.0010,0002.9999,0002.0009,0002.0002,0002.0003,0002.0004,0003.0001,0002.0005,0002.0006,0002.0007,";// 总行其他系统管理员可访问的usys菜单

		if (!user.getUserId().equals("admin")) {
			if (systemId.equals("00003") && systemType == 1
					&& !"true".equals(user.getInstIsHead())) {
				// 分行usys管理员
				if (str1.indexOf(menuCode + ",") == -1) {
					return false;
				}
			} else if (systemId.equals("00003") && systemType == 2
					&& !"true".equals(user.getInstIsHead())) {
				// 分行其他系统管理员
				if (str2.indexOf(menuCode + ",") == -1) {
					return false;
				}
			} else if (systemId.equals("00003") && systemType == 2
					&& "true".equals(user.getInstIsHead())) {
				// 总行其他管理员
				if (str3.indexOf(menuCode + ",") == -1) {
					return false;
				}
			}
		}
		return true;
	}

	// 加密
	private String encode(String str) {
		try {
			str = java.net.URLEncoder.encode(
					Base64.encode(str.getBytes("GBK")), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * <p>
	 * 方法名称: getSubSysName|描述:
	 * </p>
	 * 
	 * @return
	 */
	public String getSubSysName() {
		return subSysName;
	}

	/**
	 * <p>
	 * 方法名称: setSubSysName|描述:
	 * </p>
	 * 
	 * @param subSysName
	 */
	public void setSubSysName(String subSysName) {
		this.subSysName = subSysName;
	}

	/**
	 * <p>
	 * 方法名称: getCacheManager|描述:
	 * </p>
	 * 
	 * @return
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * <p>
	 * 方法名称: setCacheManager|描述:
	 * </p>
	 * 
	 * @param cacheManager
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * <p>
	 * 方法名称: getSystemCache|描述:
	 * </p>
	 * 
	 * @return
	 */
	public SystemCache getSystemCache() {
		return systemCache;
	}

	/**
	 * <p>
	 * 方法名称: setSystemCache|描述:
	 * </p>
	 * 
	 * @param systemCache
	 */
	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}

	/**
	 * <p>
	 * 方法名称: getMenuList|描述:
	 * </p>
	 * 
	 * @return
	 */
	public List getMenuList() {
		return menuList;
	}

	/**
	 * <p>
	 * 方法名称: setMenuList|描述:
	 * </p>
	 * 
	 * @param menuList
	 */
	public void setMenuList(List menuList) {
		this.menuList = menuList;
	}

	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	public String getSubSysId() {
		return subSysId;
	}

	public void setSubSysId(String subSysId) {
		this.subSysId = subSysId;
	}

	public static void main(String args[]) {
		try {
			String aa = "1234.123";
			String ab = "1234";
			System.out.println(aa.substring(0, aa.indexOf(".")));
			System.out.println(ab.substring(0, ab.indexOf(".")));
			String url = java.net.URLEncoder.encode("http://www.baidu.com",
					"utf-8");
			String after = java.net.URLDecoder.decode(url, "utf-8");
			System.out.println(url);
			System.out.println(after);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

}
