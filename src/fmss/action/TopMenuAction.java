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
* <p>��Ȩ����:(C)2003-2010 </p>
* @����: xindengquan
* @����: 2009-6-27 ����02:27:29
* @����: [TopMenuAction] �����ϵͳ�б�
*/
public class TopMenuAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	/** cacheManager*/
	private CacheManager cacheManager;
	/** systemCache*/
	private SystemCache systemCache;
	/** privilegeService*/
	private PrivilegeService privilegeService;
	/** ��ϵͳ�б�*/
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

	/* ���� Javadoc��
	* <p>��д����: execute|����: �����ϵͳ�б�</p>
	* @return
	* @throws Exception
	* @see com.opensymphony.xwork2.ActionSupport#execute()
	*/
	public String execute() throws Exception{
		
		// ȡ������Ϣ
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
						.getAttribute("LOGIN_USER"); // ��¼�û�
				
		//if(PrivilegeService.getMenuMap().isEmpty()){
			privilegeService.registMenu(user.getUserId(),user.getUserEname());
		//}
		
		List subSysOldLst = privilegeService.getSubSystemList(user.getUserId()); //���Բ������Щ��ϵͳ�˵�  xzw
		
//		
//		CacheabledMap cache = (CacheabledMap) cacheManager.getCacheObject(cacheManager.U_BASE_CONFIG_LIST);
//		
//		List subSysOldLst = (List) cache.get(cacheManager.U_BASE_CONFIG_LIST); // ��ϵͳ�б�
		List subSysNewLst = new ArrayList();
		top10MenuList = new ArrayList();
		otherTopMenuList = new ArrayList();
		
//		if(cacheManager.getCacheObject(cacheManager.U_BASE_CONFIG_LIST) == null){
//			systemCache.registerUBaseConfig();
//		}
		
		/*
		 * ��ȿ������ڲ˵�url����Ӳ���loginId��userId,���и��󵼲���
		 */
		if(subSysOldLst != null && user != null){
			for(int i = 0; i < subSysOldLst.size(); i++){
				
				UBaseConfigDO mo = new UBaseConfigDO();
				//���󿽱�
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
		//����ҳ������ϵͳ�б�
		topMenuList = subSysNewLst;

		return SUCCESS;
	}


	// ����
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
	* <p>��������: getTopMenuList|����: </p>
	* @return
	*/
	public List getTopMenuList(){
		return topMenuList;
	}

	/**
	* <p>��������: setTopMenuList|����: </p>
	* @param topMenuList
	*/
	public void setTopMenuList(List topMenuList){
		this.topMenuList = topMenuList;
	}

	/**
	* <p>��������: getCacheManager|����: </p>
	* @return
	*/
	public CacheManager getCacheManager(){
		return cacheManager;
	}

	/**
	* <p>��������: setCacheManager|����: </p>
	* @param cacheManager
	*/
	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	/**
	* <p>��������: getSystemCache|����: </p>
	* @return
	*/
	public SystemCache getSystemCache(){
		return systemCache;
	}

	/**
	* <p>��������: setSystemCache|����: </p>
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
