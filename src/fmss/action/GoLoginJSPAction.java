package fmss.action;

import java.util.Map;

import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.common.util.Constants;


import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: xindengquan
 * @日期: 2009-6-27 上午11:28:40
 * @描述: [GoLoginJSPAction]跳转到登录页面
 */
public class GoLoginJSPAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/** 缓存 */
	private CacheManager cacheManager;

	/*
	 * （非 Javadoc） <p>重写方法: execute|描述: </p> @return @throws Exception
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		// 取参数信息
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject(Constants.PAPAMETER_CACHE_MAP);
		Map params = null;
		if (cache != null) {
			params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			// 先将系统主题名称放到application
			if (null != params) {
				UBaseSysParamDO param = (UBaseSysParamDO) params
						.get(Constants.PARAM_THEME_PATH_CONFIG);
				request.getSession().getServletContext().setAttribute(
						Constants.SESSION_THEME_KEY, param.getSelectedValue());
				// 登陆页提示
				UBaseSysParamDO param2 = (UBaseSysParamDO) params.get(Constants.PARAM_SYS_LOGIN_PAGE_TIPS);
				if(param2!=null){
					request.setAttribute("showTips", param2.getSelectedValue());
				}
				//登录大logo
				UBaseSysParamDO param3 = (UBaseSysParamDO) params.get(Constants.PARAM_SYS_BIG_LOGO_PIC_NAME);
				if(param3!=null){
					request.setAttribute("bigLogoName", param3.getSelectedValue()!=null?param3.getSelectedValue().trim():null);
				}
			}
		}
		
		
		return SUCCESS;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
