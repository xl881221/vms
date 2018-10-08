package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.PrivilegeService;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.opensymphony.xwork2.ActionSupport;
import common.crms.util.StringUtil;


public class CheckPrivsAction extends ActionSupport{

	private static final long serialVersionUID = 1L;


	private PrivilegeService privilegeService;



	public String execute() throws Exception{

		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			response.setHeader("Cache-Control",
					"no-store, max-age=0, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Pragma", "no-cache");
			PrintWriter out = response.getWriter();
			
			String loginId = request.getParameter("loginId");
			String systemId = request.getParameter("systemId");
			
			if(StringUtils.isNotEmpty(systemId)&&StringUtils.isNotEmpty(loginId)){
				Map map = privilegeService.getUserPrivs(loginId, systemId);
				if(!map.isEmpty())
					out.write(JSONObject.fromObject(privilegeService.getUserPrivs(loginId, systemId)).toString());
			}
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}



	
	public void setPrivilegeService(PrivilegeService privilegeService){
		this.privilegeService = privilegeService;
	}
}
