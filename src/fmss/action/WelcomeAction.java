package fmss.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.jes.core.api.servlet.ServletHelper;
import com.opensymphony.xwork2.ActionSupport;

public class WelcomeAction extends ActionSupport {

	private static final long serialVersionUID = 1L;


	public String execute() throws Exception {
		String language=ServletHelper.getCookieValue(ServletActionContext.getRequest(),"USYS_USER_LANGUAGE");
		if("e".equals(language)){
			return "sysImg";
		}else{
			return SUCCESS;
		}
		
	}

	
	
	
	
}
