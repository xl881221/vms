package fmss.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import common.crms.util.encode.Base64;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.SystemCache;
import fmss.services.PrivilegeService;
import fmss.common.util.Constants;

/**
* <p>版权所有:(C)2003-2010 </p>
* @作者: xindengquan
* @日期: 2009-6-27 下午02:27:29
* @描述: [TopMenuAction] 获得子系统列表
*/
public class TopMenuAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	/** cacheManager*/
	private CacheManager cacheManager;
	/** systemCache*/
	private SystemCache systemCache;
	/** privilegeService*/
	private PrivilegeService privilegeService;
	/** 子系统列表*/
	private List topMenuList;  
	
	private List top10MenuList;
	
	private List otherTopMenuList;
	private String menu_language=null;

	public List getTop10MenuList() {
		return top10MenuList;
	}

	public void setTop10MenuList(List top10MenuList) {
		this.top10MenuList = top10MenuList;
	}

	public List getOtherTopMenuList() {
		return otherTopMenuList;
	}

	public void setOtherTopMenuList(List otherTopMenuList) {
		this.otherTopMenuList = otherTopMenuList;
	}

	/* （非 Javadoc）
	* <p>重写方法: execute|描述: 获得子系统列表</p>
	* @return
	* @throws Exception
	* @see com.opensymphony.xwork2.ActionSupport#execute()
	*/
	public String execute() throws Exception{
		
		// 取参数信息
		String param=cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_SMALL_LOGO_PIC_NAME);
		if(!"".equals(param))
			request.setAttribute("smallLogoName", param);
		request.setAttribute("PARAM_TIME_OUT", cacheManager.getParemerCacheMapValue(Constants.PARAM_TIME_OUT));		
		request.setAttribute("PARAM_KICKOUT", cacheManager.getParemerCacheMapValue(Constants.PARAM_KICKOUT));
		request.setAttribute("PARAM_SYS_USER_INFO_SHOW", cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_INFO_SHOW));
		
		String defaultIP = request.getSession().getAttribute("defaultIPAddress").toString();
		String defaultPort = request.getSession().getAttribute("defaultPort").toString();
		
		
		StringBuffer url = request.getRequestURL();
		String parentUrl = url.substring(0, 1 + url.lastIndexOf("/"));
		String parentInnerUrl = getURL(cacheManager.getParemerCacheMapValue(Constants.PARAM_USYS_INNER_URL),defaultIP,defaultPort);
		
		LoginDO user = (LoginDO) ServletActionContext.getRequest().getSession()
						.getAttribute("LOGIN_USER"); // 登录用户
				
		//if(PrivilegeService.getMenuMap().isEmpty()){
			privilegeService.registMenu(user.getUserId(),user.getUserEname());
		//}
		
		List subSysOldLst = privilegeService.getSubSystemList(user.getUserId()); //可以查出有哪些子系统菜单  xzw
		
//		
//		CacheabledMap cache = (CacheabledMap) cacheManager.getCacheObject(cacheManager.U_BASE_CONFIG_LIST);
//		
//		List subSysOldLst = (List) cache.get(cacheManager.U_BASE_CONFIG_LIST); // 子系统列表
		List subSysNewLst = new ArrayList();
		top10MenuList = new ArrayList();
		otherTopMenuList = new ArrayList();
		
//		if(cacheManager.getCacheObject(cacheManager.U_BASE_CONFIG_LIST) == null){
//			systemCache.registerUBaseConfig();
//		}
		
		/*
		 * 深度拷贝并在菜单url后面加参数loginId和userId,还有个误导参数
		 */
		if(subSysOldLst != null && user != null){
			for(int i = 0; i < subSysOldLst.size(); i++){
				
				UBaseConfigDO mo = new UBaseConfigDO();
				//对象拷贝
				BeanUtils.copyProperties((UBaseConfigDO) subSysOldLst.get(i), mo);
				
				if ("left".equals(mo.getDisplay())
						|| "false".equals(mo.getDisplay())){
					continue;
				}
				
				String sUnitLoginUrl = this.isInnerNet(request)?mo.getUnitLoginInnerUrl():mo.getUnitLoginUrl();
				String linkSiteUrl = this.isInnerNet(request)?mo.getLinkSiteInnerUrl():mo.getLinkSiteUrl();
				
				sUnitLoginUrl=this.getURL(sUnitLoginUrl, defaultIP, defaultPort);
				linkSiteUrl=this.getURL(linkSiteUrl, defaultIP, defaultPort);
				
				mo.setLinkSiteUrl(linkSiteUrl);

				int index = sUnitLoginUrl.indexOf("?");

				if("1".equals(mo.getMenuRes())){
				    if(index == -1){
				    	mo.setUnitLoginUrl(sUnitLoginUrl
				    			         +"?usdfsddf=11232dfd0091232&loginId="
				    			         +encode(user.getLoginId()) + "&userName="
										 + encode(user.getUserId()) + "&userEname="
										 + encode(user.getUserEname())
										 + "&parentUrl=" + encode(parentUrl)
										 + "&parentInnerUrl=" + encode(parentInnerUrl)
										 + "&locale="+ ("c".equals(user.getLanguage())?"zh_CN":"en_US"));
				    			      			    	               
				    }	
				    else{
				    	mo.setUnitLoginUrl(sUnitLoginUrl
		    			         +"&usdfsddf=11232dfd0091232&loginId="
		    			         +encode(user.getLoginId()) + "&userName="
								 + encode(user.getUserId()) + "&userEname="
								 + encode(user.getUserEname())
								 + "&parentUrl=" + encode(parentUrl)
								 + "&parentInnerUrl=" + encode(parentInnerUrl)
								 + "&locale="+ ("c".equals(user.getLanguage())?"zh_CN":"en_US"));
				    }
				}
				else{
					if(index == -1){
						mo.setUnitLoginUrl(sUnitLoginUrl
								+ "?loindda=90820101ssdfda&loginId="
								+ java.net.URLEncoder.encode(Base64.encode(user.getLoginId().getBytes()),"utf-8")
								+ "&parentUrl=" + encode(parentUrl)
								+ "&parentInnerUrl=" + encode(parentInnerUrl)
								+ "&userId="
								+ java.net.URLEncoder.encode(Base64.encode(user.getUserEname().getBytes()),"utf-8")
								+ "&locale="+ ("c".equals(user.getLanguage())?"zh_CN":"en_US"));
					}else{
						mo.setUnitLoginUrl(sUnitLoginUrl
								+ "&loindda=90820101ssdfda&loginId="
								+ java.net.URLEncoder.encode(Base64.encode(user.getLoginId().getBytes()),"utf-8")
								+ "&parentUrl=" + encode(parentUrl)
								+ "&parentInnerUrl=" + encode(parentInnerUrl)
								+ "&userId="
								+ java.net.URLEncoder.encode(Base64.encode(user.getUserEname().getBytes()),"utf-8")
								+ "&locale="+ ("c".equals(user.getLanguage())?"zh_CN":"en_US"));
					}
				}
				mo.setSystemEname(mo.getSystemEname()!=null?mo.getSystemEname().replaceAll("\'", "\\\\\'"):null);
				subSysNewLst.add(mo);
				HttpServletRequest request = ServletActionContext.getRequest();
				Object language=request.getSession().getServletContext().getAttribute(Constants.SESSION_MENU_LANGUAGE);
				if(language==null)language="";
				menu_language=(language.toString());
//				if(i<10){
					top10MenuList.add(mo);
//				} else if(i>12){
//					otherTopMenuList.add(mo);
//				}
			}
		}
		//设置页面中子系统列表
		topMenuList = subSysNewLst;

		return SUCCESS;
	}


	// 加密
	private String encode(String str){
		try{
			str = java.net.URLEncoder.encode(Base64.encode(str.getBytes()),
					"utf-8");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* <p>方法名称: getTopMenuList|描述: </p>
	* @return
	*/
	public List getTopMenuList(){
		return topMenuList;
	}

	/**
	* <p>方法名称: setTopMenuList|描述: </p>
	* @param topMenuList
	*/
	public void setTopMenuList(List topMenuList){
		this.topMenuList = topMenuList;
	}

	/**
	* <p>方法名称: getCacheManager|描述: </p>
	* @return
	*/
	public CacheManager getCacheManager(){
		return cacheManager;
	}

	/**
	* <p>方法名称: setCacheManager|描述: </p>
	* @param cacheManager
	*/
	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	/**
	* <p>方法名称: getSystemCache|描述: </p>
	* @return
	*/
	public SystemCache getSystemCache(){
		return systemCache;
	}

	/**
	* <p>方法名称: setSystemCache|描述: </p>
	* @param systemCache
	*/
	public void setSystemCache(SystemCache systemCache){
		this.systemCache = systemCache;
	}

	
	public PrivilegeService getPrivilegeService(){
		return privilegeService;
	}

	
	public void setPrivilegeService(PrivilegeService privilegeService){
		this.privilegeService = privilegeService;
	}

	public String getMenu_language() {
		return menu_language;
	}

	public void setMenu_language(String menu_language) {
		this.menu_language = menu_language;
	}
	
}
