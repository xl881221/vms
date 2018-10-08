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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����02:20:25
 * @����: [MessageDisplayAction]���ض�ҳ��չʾ������Ϣ
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
	 * ���� Javadoc�� <p>��д����: execute|����: ���ض�ҳ��չʾ������Ϣ</p> @return @throws
	 * Exception
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String mes = request.getParameter("mes"); // ����ϵͳ�����Ϣ
		String temp = "";

		if ("1000".equals(mes)) {
			temp = "������ϵͳ���û�û�е�¼FMSSϵͳ";
		} else if ("1001".equals(mes)) {
			temp = "���ʱ�ƽ̨�ͷ�����ϵͳ��IP��һ�£�IP��֤ʧ��";
		} else if ("1002".equals(mes)) {
			temp = "��ϵͳ�в����ڸ��û�,����ϸ������ݿ���������";
		} else if ("1003".equals(mes)) {
			temp = "���û�û����Ӧ��Ȩ��";
		} else if ("1004".equals(mes)) {
			temp = "<script>alert('�û�ʧЧ�������µ�¼��');top.location='goLoginJsp.ajax'</script>";
		} else if ("1005".equals(mes)) {
			temp = "���û�û��Ȩ�޷��������Ƶ���Դ";
		} else if ("1006".equals(mes)) {
			CacheabledMap cache = (CacheabledMap) cacheManager.getCacheObject(CacheManager.U_BASE_CONFIG_LIST);
			if (cache != null) {
				baseConfigs = buildLinkURLs((List) cache.get(CacheManager.U_BASE_CONFIG_LIST));
			}
			temp = "<script>alert('�û�����������Աע���������µ�¼��');top.location='goLoginJsp.ajax';logout()</script>";
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
