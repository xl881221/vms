package fmss.action;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jes.core.api.servlet.ServletHelper;

import fmss.dao.entity.UBaseConfigDO;
import fmss.services.OnlineService;
import fmss.common.util.SecurityPassword;

public class SysImgAction extends BaseAction {

	private String subSysId;
	
	private String subSysName;
	
	private String img;
	
	private String title;
	
	private OnlineService onlineService;



	public String getImg() {
		return img;
	}



	public void setImg(String img) {
		this.img = img;
	}



	public String execute() throws Exception {

		
		String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
 		subSysId = SecurityPassword.filterStr(subSysId); //防止SQL注入
  		if(StringUtils.isNotEmpty(subSysId)){
 			List list = onlineService.getBem().find("from UBaseConfigDO u1 where u1.systemId=? ",subSysId);
 			if(null!=list&&list.size()>0){
 				UBaseConfigDO u=(UBaseConfigDO) list.get(0);
 				if("e".equals(language)){
					title= u.getSystemNickEname();
				}else{
					title= u.getSystemNickCname();
				}
 			}else{
 				if("e".equals(language)){
 					title="Unified Platform for Regulatory Reporting";
 				}else{
 					title="统一监管报送平台";
 				}
 			}
  		}else{ 
			if("e".equals(language)){
				title="Unified Platform for Regulatory Reporting";
			}else{
				title="统一监管报送平台";
			}
 		}	 
		request.setAttribute("title", title); 
		return SUCCESS;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getSubSysId() {
		return subSysId;
	}



	public void setSubSysId(String subSysId) {
		this.subSysId = subSysId;
	}


	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
