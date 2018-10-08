package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.services.LoginService;
import fmss.services.OnlineService;

import org.apache.struts2.ServletActionContext;



import com.opensymphony.xwork2.ActionSupport;
import common.crms.util.StringUtil;


public class CheckLoginAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private OnlineService onlineService;
	/** ï¿½ï¿½Ï¢*/
	private String message; 

	public String execute() throws Exception{

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		String loginId = request.getParameter("loginId");
		String addr=request.getParameter("addr");
		UBaseOnlineDO u=null;
		if(!StringUtil.isEmpty(addr)){
			u=onlineService.findUserByAddrAndUserId(request.getParameter("addr"),loginId);
		}else{
			u=onlineService.findUserByLoginId(loginId);
		}
		if(u!=null){ 
			message = u.getUserId() + "*" + u.getUserId();			
		}else{
			message = "Err1000";			
		}
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");

		response.setHeader("Cache-Control",
				"no-store, max-age=0, no-cache, must-revalidate");

		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out = response.getWriter();
		out.print(message);
		out.close();
		return null;
	}




	/**
	* <p>ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿? getMessage|ï¿½ï¿½ï¿½ï¿½: ï¿½ï¿½ï¿½ï¿½ï¿½Ï?/p>
	* @return
	*/
	public String getMessage(){
		return message;
	}

	/**
	* <p>ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½ï¿? setMessage|ï¿½ï¿½ï¿½ï¿½: ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½Ï¢</p>
	* @param message
	*/
	public void setMessage(String message){
		this.message = message;
	}




	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
