package fmss.action;

import java.util.Map;

import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.common.util.Constants;


import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����11:28:40
 * @����: [GoLoginJSPAction]��ת����¼ҳ��
 */
public class GoLoginJSPAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	/** ���� */
	private CacheManager cacheManager;

	/*
	 * ���� Javadoc�� <p>��д����: execute|����: </p> @return @throws Exception
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		// ȡ������Ϣ
		CacheabledMap cache = (CacheabledMap) cacheManager
				.getCacheObject(Constants.PAPAMETER_CACHE_MAP);
		Map params = null;
		if (cache != null) {
			params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			// �Ƚ�ϵͳ�������Ʒŵ�application
			if (null != params) {
				UBaseSysParamDO param = (UBaseSysParamDO) params
						.get(Constants.PARAM_THEME_PATH_CONFIG);
				request.getSession().getServletContext().setAttribute(
						Constants.SESSION_THEME_KEY, param.getSelectedValue());
				// ��½ҳ��ʾ
				UBaseSysParamDO param2 = (UBaseSysParamDO) params.get(Constants.PARAM_SYS_LOGIN_PAGE_TIPS);
				if(param2!=null){
					request.setAttribute("showTips", param2.getSelectedValue());
				}
				//��¼��logo
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
