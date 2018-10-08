package fmss.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.BeanUtils;

import fmss.dao.entity.UBaseConfigDO;
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
 * @日期: 2009-6-27 下午02:20:25
 * @描述: [MessageDisplayAction]向特定页面展示错误信息
 */
public class MessageDisplayAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private List baseConfigs = null;

	private CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public List getBaseConfigs() {
		return baseConfigs;
	}

	/*
	 * （非 Javadoc） <p>重写方法: execute|描述: 向特定页面展示错误信息</p> @return @throws
	 * Exception
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String mes = request.getParameter("mes"); // 从子系统获得信息
		String temp = "";

		if ("1000".equals(mes)) {
			temp = "访问子系统的用户没有登录FMSS系统";
		} else if ("1001".equals(mes)) {
			temp = "访问本平台和访问子系统的IP不一致，IP验证失败";
		} else if ("1002".equals(mes)) {
			temp = "子系统中不存在该用户,请仔细检查数据库连接配置";
		} else if ("1003".equals(mes)) {
			temp = "该用户没有相应的权限";
		} else if ("1004".equals(mes)) {
			temp = "<script>alert('用户失效，请重新登录！');top.location='goLoginJsp.ajax'</script>";
		} else if ("1005".equals(mes)) {
			temp = "该用户没有权限访问受限制的资源";
		} else if ("1006".equals(mes)) {
			CacheabledMap cache = (CacheabledMap) cacheManager.getCacheObject(CacheManager.U_BASE_CONFIG_LIST);
			if (cache != null) {
				baseConfigs = buildLinkURLs((List) cache.get(CacheManager.U_BASE_CONFIG_LIST));
			}
			temp = "<script>alert('用户被超级管理员注销，请重新登录！');top.location='goLoginJsp.ajax';logout()</script>";
			request.setAttribute("mes", temp);
			return "logoutAndOthers";
		}
		request.setAttribute("mes", temp);

		return SUCCESS;
	}

	private List buildLinkURLs(List list) {
		if (list == null) {
			return Collections.EMPTY_LIST;
		}
		List l = new ArrayList(list.size());
		String defaultIP = request.getSession().getAttribute("defaultIPAddress").toString();
		String defaultPort = request.getSession().getAttribute("defaultPort").toString();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseConfigDO o = (UBaseConfigDO) iterator.next();
			UBaseConfigDO dest = new UBaseConfigDO();
			BeanUtils.copyProperties(o, dest);
			String linkSiteUrl = dest.getLinkSiteUrl();
			if (linkSiteUrl != null && linkSiteUrl.indexOf(Constants.DEFAULT_URL_IP) != -1) {
				linkSiteUrl = linkSiteUrl.replaceFirst(Constants.DEFAULT_URL_IP_UNICODE, defaultIP);
			}
			if (linkSiteUrl != null && linkSiteUrl.indexOf(Constants.DEFAULT_URL_PORT) != -1) {
				linkSiteUrl = linkSiteUrl.replaceFirst(Constants.DEFAULT_URL_PORT_UNICODE, defaultPort);
			}
			dest.setLinkSiteUrl(linkSiteUrl);
			l.add(dest);
		}
		return l;
	}
}
