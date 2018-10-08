package fmss.action;

import java.util.List;

import fmss.dao.entity.BaseUserEmailDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.dao.entity.VcrmsSystemRelaDO;
import fmss.dao.entity.VcrmsSystemRelaDisDO;
import fmss.services.InstService;

import org.apache.commons.lang.StringUtils;



/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: jiazhimin
 * @日期: 2011-5-18
 * @描述: 设置部门的各模块邮箱
 */
public class InstMailAction extends JSONProviderAction {
	

	private static final long serialVersionUID = 1L;
	
	private UBaseUserDO user = new UBaseUserDO(); 
	
	private UBaseInstDO inst = new UBaseInstDO();
	
	private String bankId;
	
	private String systemId;
	
	private String vSystemId;

	private String systemName;
	
	private String userEname;
	
	
	public String getUserEname() {
		return userEname;
	}

	public void setUserEname(String userEname) {
		this.userEname = userEname;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getvSystemId() {
		return vSystemId;
	}

	public void setvSystemId(String vSystemId) {
		this.vSystemId = vSystemId;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public UBaseInstDO getInst() {
		return inst;
	}

	public void setInst(UBaseInstDO inst) {
		this.inst = inst;
	}
	private List userList;
	
	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}
	
	/** 选中的邮箱地址集*/
	private List selectedEmailAddrs;
	
	public List getSelectedEmailAddrs() {
		return selectedEmailAddrs;
	}

	public void setSelectedEmailAddrs(List selectedEmailAddrs) {
		this.selectedEmailAddrs = selectedEmailAddrs;
	}
	
	/** 模块列表集合*/
	private List instSystemReal;
	
	public List getInstSystemReal() {
		return instSystemReal;
	}

	public void setInstSystemReal(List instSystemReal) {
		this.instSystemReal = instSystemReal;
	}
	
	/** 机构信息服务类*/
	private InstService instService;
	
	
	public InstService getInstService() {
		return instService;
	}

	public void setInstService(InstService instService) {
		this.instService = instService;
	}

	private void InitPage(){
		instSystemReal= instService.getSystemRela();	
		if(null!=instSystemReal&&instSystemReal.size()>0){
			vSystemId=((VcrmsSystemRelaDisDO)instSystemReal.get(0)).getSystemId();
		}
	}
	
	public String instMail(){		
		return SUCCESS;
	}
	
	public String instMailHead(){
		InitPage();
		return SUCCESS;
	}
	
	public String instMailTree(){
		return SUCCESS;
	}
	
	public String instMailUser(){	
		String reshFlag="0";
		//查询该机构当前模块下未配置邮箱的用户
		if(null!=request.getParameter("down")&&request.getParameter("down").equals("true")){
			//设置全部
			if(null!=request.getParameter("checkAll")&&request.getParameter("checkAll").equals("true")){
				userList=instService.userInInstAll(user.getUserEname(),bankId,systemId);
				if(null!=userList&&userList.size()>0){
					for(int index=0;index<userList.size();index++){
						UBaseUserDO ubuserDo= (UBaseUserDO)userList.get(index);
						String userId= ubuserDo.getUserId();
						if(userId.equals("admin")){
							System.out.print(userId);
							
						}
						if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(bankId)&&StringUtils.isNotBlank(systemId)){
							List emailInfo=instService.getSelectedEmailAddrsByBankId(userId,bankId,systemId, this.paginationList);
							if(null!=emailInfo&&emailInfo.size()>1){
								//已存在数据
							}else{
								BaseUserEmailDO baseDO = new BaseUserEmailDO();
								baseDO.setUserId(userId);
								baseDO.setBankId(bankId);
								baseDO.setSystemId(systemId);
								List crmsSubjectIdList=instService.getSystemRelaBySystemId(systemId);
								if(null!=crmsSubjectIdList&&crmsSubjectIdList.size()>0){
									VcrmsSystemRelaDO crmsSubjectVo=(VcrmsSystemRelaDO) crmsSubjectIdList.get(0);
				            		String crmsSubjectId=crmsSubjectVo.getCrmsSubjectId();
									baseDO.setCrmsSubjectId(crmsSubjectId);
								}
								instService.saveBaseUserEmail(baseDO);
							}
						}
					}	
				}		
			}else{
				//设置所选用户
				String []ids=request.getParameterValues("ids");
				if(null!=ids&&ids.length>0){
					for(int i=0;i<ids.length;i++){
						String userId= ids[i];
						if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(bankId)&&StringUtils.isNotBlank(systemId)){
							//向邮箱记录表中插值
							List emailInfo=instService.getSelectedEmailAddrsByBankId(userId,bankId,systemId, this.paginationList);
							if(null!=emailInfo&&emailInfo.size()>1){
								//已存在数据
							}else{
								BaseUserEmailDO baseDO = new BaseUserEmailDO();
								baseDO.setUserId(userId);
								baseDO.setBankId(bankId);
								baseDO.setSystemId(systemId);
								List crmsSubjectIdList=instService.getSystemRelaBySystemId(systemId);
								if(null!=crmsSubjectIdList&&crmsSubjectIdList.size()>0){
									VcrmsSystemRelaDO crmsSubjectVo=(VcrmsSystemRelaDO) crmsSubjectIdList.get(0);
				            		String crmsSubjectId=crmsSubjectVo.getCrmsSubjectId();
									baseDO.setCrmsSubjectId(crmsSubjectId);
								}
								instService.saveBaseUserEmail(baseDO);
							}
						}
					}			
				 }
			}
			reshFlag="1";
		}
		userList=instService.userInInst(user.getUserEname(),bankId,systemId,this.paginationList);
		if(null!=userList&&userList.size()>0){
			for(int i=0;i<userList.size();i++){
				UBaseUserDO uBase=(UBaseUserDO) userList.get(i);
				String instId=uBase.getInstId();
				UBaseInstDO inst=(UBaseInstDO) instService.getInstByInstId(instId);
				if(null!=inst){
					uBase.setInstName(inst.getInstName());
				}
			}
		}
		response.setHeader("Pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		request.setAttribute("reshFlag", reshFlag);
		return SUCCESS;
	}
	
	public UBaseUserDO getUser() {
		return user;
	}

	public void setUser(UBaseUserDO user) {
		this.user = user;
	}

	public String instMailMain(){
		String reshFlag="0";
		bankId =request.getParameter("bankId");
		inst = (UBaseInstDO) instService.getInstByInstId(bankId);
		systemId =request.getParameter("systemId");
		systemName=request.getParameter("systemName");
		if(null!=request.getParameter("up")&&request.getParameter("up").equals("true")){
			if(null!=request.getParameter("checkAll")&&request.getParameter("checkAll").equals("true")){
				selectedEmailAddrs= instService.getSelectedEmailAddrsByBankIdAndUserEnameAll(user.getUserEname(),bankId,systemId);
				if(null!=selectedEmailAddrs&&selectedEmailAddrs.size()>0){
					for(int i=0;i<selectedEmailAddrs.size();i++){
						BaseUserEmailDO baseDo=(BaseUserEmailDO) selectedEmailAddrs.get(i);
						String userId=baseDo.getUserId();
						if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(bankId)&&StringUtils.isNotBlank(systemId)){
							BaseUserEmailDO baseDO = new BaseUserEmailDO();
							baseDO.setUserId(userId);
							baseDO.setBankId(bankId);
							baseDO.setSystemId(systemId);
							instService.delete(baseDO);
						}
					}
				}
			}else{
				String []ids=request.getParameterValues("ids");
				if(null!=ids&&ids.length>0){
					for(int i=0;i<ids.length;i++){
						String userId= ids[i];
						if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(bankId)&&StringUtils.isNotBlank(systemId)){
							BaseUserEmailDO baseDO = new BaseUserEmailDO();
							baseDO.setUserId(userId);
							baseDO.setBankId(bankId);
							baseDO.setSystemId(systemId);
							instService.delete(baseDO);
						}
					}
				}
			}
			reshFlag="1";
		}
		selectedEmailAddrs= instService.getSelectedEmailAddrsByBankIdAndUserEname(user.getUserEname(),bankId,systemId, this.paginationList);
		if(null!=selectedEmailAddrs&&selectedEmailAddrs.size()>0){
			for(int i=0;i<selectedEmailAddrs.size();i++){
				BaseUserEmailDO baseUserMail=(BaseUserEmailDO) selectedEmailAddrs.get(i);
				String instId=baseUserMail.getBankId();
				UBaseInstDO inst=(UBaseInstDO) instService.getInstByInstId(instId);
				if(null!=inst){
					baseUserMail.setBankName(inst.getInstName());
				}
				UBaseUserDO user=(UBaseUserDO) instService.get(UBaseUserDO.class, baseUserMail.getUserId());
				if(null!=user){
					baseUserMail.setUserCname(user.getUserCname());
				}
				List relaInfo=instService.getSystemRelaBySystemId(systemId);
				if(null!=relaInfo&&relaInfo.size()>0){
					VcrmsSystemRelaDO vSystem=(VcrmsSystemRelaDO)relaInfo.get(0);
					baseUserMail.setSystemName(vSystem.getSystemCname());
				}
			}		
		}
		response.setHeader("Pragma","No-cache"); 
		response.setHeader("Cache-Control","no-cache"); 
		response.setDateHeader("Expires", 0); 
		request.setAttribute("reshFlag", reshFlag);
		return SUCCESS;
	}
}
