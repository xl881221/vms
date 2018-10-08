package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import fmss.action.base.AuditBase;
import fmss.action.entity.UBaseInstChangeDO;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;
import fmss.dao.entity.BaseUserEmailDO;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.VBaseUserEmailDO;
import fmss.services.DictionaryService;
import fmss.services.InstLevelException;
import fmss.services.InstService;
import fmss.services.SubSystemService;
import fmss.services.UBaseSysLogService;


/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午02:16:31
 * @描述: [AjaxInstAction]异步获取机构信息的控制类
 */
public class InstAction extends JSONProviderAction{

	private static final long serialVersionUID = 1L;	
	protected static final String FORM_INST = "formInst";
	final String DIC_TYPE_INSTLAYER = "LEVEL";		//机构级别字典类型
	final String TAXPAYERTYPE="TAXPAYER_TYPE";		//机构类别字典类型
	
	private UBaseInstDO inst = new UBaseInstDO(); 	// 创建机构对象实例
	private UBaseInstChangeDO instc = new UBaseInstChangeDO(); 	// 创建机构change对象实例
	private List<UBaseInstDO> instList; 							// 机构列表
	private List instLevList; 						// 机构级别列表
	private List instPayerList;						//机构类别列表
	private List<Object> instTnumList=new ArrayList<Object>();    //纳税人识别号列表
	private String editType;						//编辑类型
	private String isRoot;
	private List<Object> instPathList = new ArrayList<Object>();						//当前机构类型上级机构列表
	
	public List<Object> getInstPathList() {
		return instPathList;
	}

	public void setInstPathList(List<Object> instPathList) {
		this.instPathList = instPathList;
	}

	private List instSystemReal;                    //模块列表集合
	
	private List instEmailAddrs;                    //邮箱地址集
	
	private List selectedEmailAddrs;                //选中的邮箱地址集
	
	String vSystemId;
	
	public String getvSystemId() {
		return vSystemId;
	}

	public void setvSystemId(String vSystemId) {
		this.vSystemId = vSystemId;
	}

	private String emailJsonp;
	
	private CacheManager cacheManager; // 缓存
	
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	public String getEmailJsonp() {
		return emailJsonp;
	}

	public void setEmailJsonp(String emailJsonp) {
		this.emailJsonp = emailJsonp;
	}

	public List getSelectedEmailAddrs() {
		return selectedEmailAddrs;
	}

	public void setSelectedEmailAddrs(List selectedEmailAddrs) {
		this.selectedEmailAddrs = selectedEmailAddrs;
	}

	public List getInstEmailAddrs() {
		return instEmailAddrs;
	}

	public void setInstEmailAddrs(List instEmailAddrs) {
		this.instEmailAddrs = instEmailAddrs;
	}

	public List getInstSystemReal() {
		return instSystemReal;
	}

	public void setInstSystemReal(List instSystemReal) {
		this.instSystemReal = instSystemReal;
	}

	/** sysLogService 日志服务 */
	private UBaseSysLogService sysLogService;

	/** 记录日志信息*/
	private static final Logger logger = Logger.getLogger(InstAction.class);
	
	/** 流信息专用跳转常量*/
	protected static final String STREAM = "stream";
	
	/** 机构信息服务类*/
	private InstService instService;
	
	/** 字典信息服务类*/
	private DictionaryService dicService;

	/**保存删除后的提示信息**/
	private String isDelteSuccess = null;
	
	private boolean fixQuery = false;
	
	public String getIsDelteSuccess() {
		return isDelteSuccess;
	}

	public void setIsDelteSuccess(String isDelteSuccess) {
		this.isDelteSuccess = isDelteSuccess;
	}

	/**
	 * <p>方法名称: getInstService|描述:获取机构信息服务类 </p>
	 * @return 获取机构信息服务类
	 */
	public InstService getInstService() {
		return instService;
	}

	/**
	 * <p>方法名称: setInstService|描述:设置机构信息服务类 </p>
	 * @return 设置机构信息服务类
	 */
	public void setInstService(InstService instService) {
		this.instService = instService;
	}


	/**
	 * <p>方法名称: getDicService|描述:获取字典信息服务类 </p>
	 * @return 获取字典信息服务类
	 */
	public DictionaryService getDicService() {
		return dicService;
	}

	/**
	 * <p>方法名称: setDicService|描述:设置字典信息服务类 </p>
	 * @return 设置字典信息服务类
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}


	/**
	 * <p>方法名称: list|描述:跳转到机构管理页面 </p>
	 * @return 返回到成功页面
	 */
	public String list(){
		return SUCCESS;
	}

	private SubSystemService subSystemService;

	 
	public SubSystemService getSubSystemService() {
		return subSystemService;
	}

	public void setSubSystemService(SubSystemService subSystemService) {
		this.subSystemService = subSystemService;
	}

	/**
	* <p>方法名称: saveInst|描述: 获取机构信息并保存</p>
	* @return 保存的结果值
	*/
	public String saveInst(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		if(inst.getOrderNum()==null){
			inst.setOrderNum(new Integer(0));
		}
		try{
			String startDate = request.getParameter("startDate");
			if ("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
				&& "1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_INST_AUDIT))) {
				if (StringUtils.isNotEmpty(startDate)) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						inst.setStartDate(sdf.parse(startDate));
					} catch (Exception e) {
						System.out.println("error date format:" + startDate);
						log.error(e);
					}
				} else {
					inst.setStartDate(null);
				}
				if("add".equals(editType)){
					user.setDescription("新增机构" + inst.getInstId() + "[" + instc.getInstName() + "]");
					try{
						instService.copyInst(instc,inst);
						instc.setChangeUser(user.getUserId());
						instc.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
						instc.setChangeStatus(AuditBase.CHANGE_TYPE_INST_ADD);
						// 新增记录
						instService.saveInst(instc); 
//						updateInstRela1(inst);
					
						setResultMessages("保存成功,待审核通过后生效");
						
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"新增","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
					}catch (Exception e){ 
						logger.error("", e);
						setResultMessages("新增机构失败");
						if(e instanceof InstLevelException)
							setResultMessages("新增机构失败，" + e.getMessage());
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"新增","0",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						user.setAddress("新增机构出错" + inst.getInstId() + "[" + inst.getInstName() + "]");
						this.sysLogService.saveUBaseSysLog(sysLog);
						InitPage();
						return INPUT;
					}
				}else{
					user.setDescription("更新机构" + inst.getInstId());
					try {
						if (instService.modifiedInst(inst.getInstId())) {
							setResultMessages("修改失败,已经有相同机构["
									+ inst.getInstId() + "]在审核中");
						} else {
							// 更新记录
							instService.copyInst(instc, inst);
							instc.setChangeUser(user.getUserId());
							instc.setChangeTime(new java.sql.Timestamp(System
									.currentTimeMillis()));
							instc
									.setChangeStatus(AuditBase.CHANGE_TYPE_INST_MODIFY);
							// 新增记录
							instService.updateInstc(instc);

							setResultMessages("修改成功,待审核通过后生效");

							if (user.getInstId().equals(inst.getInstId())) {
								user.setInstCname(inst.getInstName());
							}
						}
					} catch (Exception e) {
						setResultMessages("更新机构失败。");
						if (e instanceof InstLevelException)
							setResultMessages("更新机构失败，" + e.getMessage());
						UBaseSysLogDO sysLog = this.sysLogService
								.setUBaseSysLog(user, "更新", "0",
										Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						user.setAddress("更新机构出错" + inst.getInstId());
						this.sysLogService.saveUBaseSysLog(sysLog);
						InitPage();
						logger.error("", e);
						return INPUT;
					}
				}
//				setResultMessages("保存成功,待审核通过后生效");
				refreshSubSystem();
				
				return SUCCESS;
			
			}
			else{
			if (StringUtils.isNotEmpty(startDate)) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					inst.setStartDate(sdf.parse(startDate));
				} catch (Exception e) {
					System.out.println("error date format:" + startDate);
					log.error(e);
				}
			} else {
				inst.setStartDate(null);
			}
			if("add".equals(editType)){
				user.setDescription("新增机构" + inst.getInstId() + "[" + inst.getInstName() + "]");
				try{
					
					if(instService.getUBaseInstDOBytaxPernumberAndTaxPayerType(inst.getTaxpernumber(),inst.getTaxPayerType())!=null){
						setResultMessages("此机构已存在该纳税人识别号，请重新输入");
					}
					else{
					// 新增记录
					instService.saveInst(inst); 
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"新增","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("基础信息管理.机构管理");
					this.sysLogService.saveUBaseSysLog(sysLog);
					setResultMessages("保存成功！");
					}
				}catch (Exception e){ 
					logger.error("", e);
					setResultMessages("");
					if(e instanceof InstLevelException)
						setResultMessages("新增机构失败，" + e.getMessage());
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"新增","0",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("基础信息管理.机构管理");
					user.setAddress("新增机构出错" + inst.getInstId() + "[" + inst.getInstName() + "]");
					this.sysLogService.saveUBaseSysLog(sysLog);
					InitPage();
					return INPUT;
				}
			}else{
				user.setDescription("更新机构" + inst.getInstId());
				try{
					// 更新记录
					instService.updateInst(inst);  
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"更新","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("基础信息管理.机构管理");
					this.sysLogService.saveUBaseSysLog(sysLog);
					
					instService.updateInstCName(inst);
					
					if (user.getInstId().equals(inst.getInstId())) {
						user.setInstCname(inst.getInstName());
						user.setInstIsHead(inst.getIsHead());
					}
				}catch (Exception e){
					setResultMessages("更新机构失败。"); 
					if(e instanceof InstLevelException)
						setResultMessages("更新机构失败，" + e.getMessage());
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"更新","0",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("基础信息管理.机构管理");
					user.setAddress("更新机构出错" + inst.getInstId());
					this.sysLogService.saveUBaseSysLog(sysLog);
					InitPage();
					logger.error("", e);
					return INPUT;
				}
			}
			refreshSubSystem();
			return SUCCESS;
			}
		}catch (Exception e){
			user.setDescription("保存机构出错" + inst.getInstId());
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0001");
			sysLog.setMenuName("基础信息管理.机构管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			log.error(e);
		}
		return ERROR;
	}
	
	public void refreshSubSystem(){
		List crmsList = subSystemService.getCrmsUBaseConfig();
		
		if(crmsList.size() > 0){
			String dbUrl =  ((UBaseConfigDO)crmsList.get(0)).getDbUrl();
			String url  = dbUrl.substring(0,dbUrl.indexOf("getRes.ajax"));
			url += "refreshBankCache.ajax?onInit=refresh";
			
			HttpClient client = new HttpClient();
			HttpMethod method = null;
			try {
				method = new GetMethod(url);
				client.executeMethod(method);
				String resp = method.getStatusText();
				logger.info("刷新子系统:"+resp);
			} catch (Throwable e) {
				logger.error("刷新子系统缓存发生异常!",e);
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
			}
		}
		
	}
	/**
	* <p>方法名称: InitPage|描述: 初始化页面信息</p>
	* @return instList 上级机构列表
	* @return instLevList 机构级别列表
	*/
	private void InitPage(){
		InitInstList();
		instLevList = dicService.getDictoryByDicType(DIC_TYPE_INSTLAYER);
		instPayerList=dicService.getDictoryByCodeType(TAXPAYERTYPE);
	
	}
	
	public void createJsonp(List instEmailAddrs,List selectedEmailAddrs){
		emailJsonp="[";
		if(null!=instEmailAddrs&&instEmailAddrs.size()>0){
			for(int i=0;i<instEmailAddrs.size();i++){
				VBaseUserEmailDO vb=(VBaseUserEmailDO)instEmailAddrs.get(i);
				if(null!=selectedEmailAddrs&&selectedEmailAddrs.size()>0){
					String selFlag="0";
					for(int j=0;j<selectedEmailAddrs.size();j++){
						BaseUserEmailDO baseDo=(BaseUserEmailDO)selectedEmailAddrs.get(j);
						if(null!=baseDo.getUserId()&&baseDo.getUserId().equals(vb.getUserId())){
							emailJsonp=emailJsonp+"{'id':'"+vb.getUserId()+"','text':'"+vb.getUserEname()+"','selected':true},";
							selFlag="1";
							break;
						}
					}
					if(!selFlag.equals("1")){
						emailJsonp=emailJsonp+"{'id':'"+vb.getUserId()+"','text':'"+vb.getUserEname()+"'},";
					}
					
				}else{
					emailJsonp=emailJsonp+"{'id':'"+vb.getUserId()+"','text':'"+vb.getUserEname()+"'},";
				}
			}
			emailJsonp=emailJsonp.substring(0,emailJsonp.length()-1);
		}
		emailJsonp=emailJsonp+"]";
	}
	/**
	* <p>方法名称: viewInst|描述: 通过机构编号获取机构信息</p>
	* @return inst 机构对象
	*/
	public String viewInst(){
		try{
			inst = (UBaseInstDO) instService.getInstByInstId(inst.getInstId());
			InitPage();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	* <p>方法名称: viewInst|描述: 通过机构编号获取机构信息</p>
	* @return inst 机构对象
	*/
	public String viewInstc(){
		try{
			instc = (UBaseInstChangeDO) instService.getInstcByInstId(instc.getInstId());
			InitPage();
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	/**
	* <p>方法名称: editInst|描述: 编辑界面通过机构编号获取机构信息</p>
	* @return inst 通过机构编号获取的机构信息
	* @return instList 用于选择上级机构的机构列表
	*/
	public String editInst(){
		try{
			inst = (UBaseInstDO) instService.getInstByInstId(inst.getInstId());
			InitPage();
			return FORM_INST;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	/**
	* <p>方法名称: createInst|描述: 新增机构</p>
	* @return instList 用于选择用户所属机构的机构列表
	*/
	public String createInst(){
		try{
			InitPage();
			String taxPayerType = instService.getInstTaxPayerTypeByInstId(inst.getInstId());
			if("1".equals(taxPayerType)){
				initTax();
				inst=(UBaseInstDO) instService.getInstByInstId(inst.getInstId());
			}
			inst.setTaxPayerType(taxPayerType);
			return FORM_INST;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	 * @title 初始化纳税人主体
	 * @description TODO
	 * @author dev4
	 */
	public void initTax(){
		if(null!=inst){
			String instId = this.inst.getInstId();
			UBaseInstDO inst = (UBaseInstDO) instService.getInstByInstId(instId);
			String path = inst.getInstPath();
			String[] instIds = path.split("\\\\");
			for (int i = 1; i < instIds.length; i++) {
				instId = instIds[i];
				inst = (UBaseInstDO) instService.getInstByInstId(instId);
				if("0".equals(inst.getTaxPayerType())){
					instPathList.add(inst);
				}
			}
		}
	}
	
	/**
	 * @title 初始化纳税人主体
	 * @description TODO
	 * @author dev4
	 */
	/**
	 * 依所选机构加载我方信息 新功能全部使用此方法，替代loadOurInfo();
	 */
	public void loadSelfInfo() {
		try {
			StringBuffer sb = new StringBuffer();
			String instCode = this.request.getParameter("instCode");
			UBaseInstDO inst = (UBaseInstDO) instService.getInstByInstId(instCode);
			if (inst != null) {
				sb.append(JSONObject.fromObject(inst));
			}
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(sb.toString());
			this.response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
			log.error("loadSelfInfo : ", ex);
		}
	}
	
	/**
	* <p>方法名称: 模块元素联动用户邮箱</p>
	* @return void
	*/
	public void ajaxReadData()throws IOException{
		String systemId=request.getParameter("systemId");
		String bankId = request.getParameter("instId");
		instEmailAddrs=instService.getEmailAddrs(systemId);
		if(StringUtils.isNotBlank(bankId)){
			selectedEmailAddrs= instService.getSelectedEmailAddrs(systemId,bankId);
		}
		createJsonp(instEmailAddrs,selectedEmailAddrs);
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		PrintWriter out=response.getWriter();
		out.print(emailJsonp);
		out.close();
	}
	
	/**
	* <p>方法名称: InitInstList|描述: 初始化上级机构列表，并增加空行</p> 
	* @return instList 用于选择上级机构的机构列表
	*/
	private void InitInstList(){
		instList = instService.getAllInsts();
		UBaseInstDO ui = new UBaseInstDO();
		ui.setInstId("");
		ui.setInstName("");
		instList.add(0, ui);
	}
	
	public void checkUserInInst(){
		LoginDO user = (LoginDO)request.getSession().getAttribute("LOGIN_USER");
		String result = "";
		this.response.setContentType("text/html; charset=UTF-8");
		String allId = getIds().toString().trim();
		String managerId = allId.substring(1,allId.length()-1);
		String[] ids = managerId.split(",");
		for(int i = 0; i < ids.length; i++){
			if(user.getInstId().equals(ids[i])){
				result = "-1";
				break;
			}
			if("1".equals(instService.isHavingUserInInst(ids[i]))){
				result = "-3";
				break;
			}
			if(!instService.isLeaf(ids[i])){
				result = "-4";
				break;
			}
		}	
		if("".equals(result))
			result = "-2";
		try{
			this.response.getWriter().write(result);
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				this.response.getWriter().close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	public String initInst(){
		instService.initInst();
		this.setResultMessages("初始化成功！");
		return SUCCESS;
	}
	
	/**
	* <p>方法名称: deleteInst|描述: 删除机构</p>
	* @return 删除机构
	*/
	public String deleteInst(){
		boolean deleteOwnInst = false;
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("删除机构");
		try{
			if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))&&"1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_INST_AUDIT))){
				List ls = new ArrayList();
				String hql = null;
				for(int i = 0; i < getIds().size(); i++){
					UBaseInstDO temp = new UBaseInstDO();
					if (!getIds().get(i).toString().equals(user.getInstId())) {
						temp.setInstId(getIds().get(i).toString());
						ls.add(temp);
					} else {
						deleteOwnInst = true;
					}
				}
				
				boolean isExistUserThisInst = false;
				StringBuffer sb = new StringBuffer();
				if (instService.hasChildInst(ls, sb)) {
					setResultMessages(sb.toString());
				} else {
					try {
						for (int i = 0; i < getIds().size(); i++) {
							if (!getIds().get(i).toString().equals(user.getInstId())) {
								hql = "select u from UBaseUserDO u where u.instId='" + getIds().get(i).toString() + "'";
								List userList = instService.find(hql);
								if(CollectionUtils.isNotEmpty(userList)){
									setResultMessages("机构[" + getIds().get(i).toString() + "]下存在用户,不能直接删除,请先到用户管理里删除该机构下的用户");
									isExistUserThisInst = true;
									break;
								}
								// instService.deleteUserInInst(userList);
								// 删除角色所包含的此机构 (审核通过之后再删除)
//								String rolehql = "delete from UAuthRoleResourceDO res where res.resDetailValue='"
//										+ getIds().get(i).toString() + "' and res.resId = '35'";
//								instService.executeUpdate(rolehql);
							} else {
								deleteOwnInst = true;
							}
						}
						if(!isExistUserThisInst){
//							instService.deleteAllInst(ls);
							instService.saveDeleteInst(instc,ls,user.getUserId());
//							user.setDescription("删除机构" + StringUtils.join(getIds().iterator(), ","));
//							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "删除", "1",
//									Constants.BASE_SYS_LOG_AUTHORITY);
//							sysLog.setMenuId("0002.0001");
//							sysLog.setMenuName("基础信息管理.机构管理");
//							this.sysLogService.saveUBaseSysLog(sysLog);
						}
					} catch (Exception e) {
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "删除", "0",
								Constants.BASE_SYS_LOG_AUTHORITY);
						user.setDescription("删除机构失败" + StringUtils.join(getIds().iterator(), ","));
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
						setResultMessages("删除机构失败，可能该机构正被使用。");
					}
					if (deleteOwnInst) {
						this.isDelteSuccess = "false";
						this.setResultMessages("用户不可删除自己所在机构");
					} else if (isExistUserThisInst) {
						this.isDelteSuccess = "false";
					} else {
						this.isDelteSuccess = "success";
						setResultMessages("已保存，审核之后可以删除。");
					}
				}
				return SUCCESS;
			}else{
				List ls = new ArrayList();
				String hql = null;
				for(int i = 0; i < getIds().size(); i++){
					UBaseInstDO temp = new UBaseInstDO();
					if (!getIds().get(i).toString().equals(user.getInstId())) {
						temp.setInstId(getIds().get(i).toString());
						ls.add(temp);
					} else {
						deleteOwnInst = true;
					}
				}
				
				boolean isExistUserThisInst = false;
				StringBuffer sb = new StringBuffer();
				if (instService.hasChildInst(ls, sb)) {
					setResultMessages(sb.toString());
				} else {
					try {
						for (int i = 0; i < getIds().size(); i++) {
							if (!getIds().get(i).toString().equals(user.getInstId())) {
								hql = "select u from UBaseUserDO u where u.instId='" + getIds().get(i).toString() + "'";
								List userList = instService.find(hql);
								if(CollectionUtils.isNotEmpty(userList)){
									setResultMessages("机构[" + getIds().get(i).toString() + "]下存在用户,不能直接删除,请先到用户管理里删除该机构下的用户");
									isExistUserThisInst = true;
									break;
								}
								// instService.deleteUserInInst(userList);
								// 删除角色所包含的此机构
								String rolehql = "delete from UAuthRoleResourceDO res where res.resDetailValue='"
										+ getIds().get(i).toString() + "' and res.resId = '35'";
								instService.executeUpdate(rolehql);
							} else {
								deleteOwnInst = true;
							}
						}
						if(!isExistUserThisInst){
							instService.deleteAllInst(ls);
							user.setDescription("删除机构" + StringUtils.join(getIds().iterator(), ","));
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "删除", "1",
									Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0001");
							sysLog.setMenuName("基础信息管理.机构管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
						}
					} catch (Exception e) {
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "删除", "0",
								Constants.BASE_SYS_LOG_AUTHORITY);
						user.setDescription("删除机构失败" + StringUtils.join(getIds().iterator(), ","));
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
						setResultMessages("删除机构失败，可能该机构正被使用。");
					}
					if (deleteOwnInst) {
						this.isDelteSuccess = "false";
						this.setResultMessages("用户不可删除自己所在机构");
					} else if (isExistUserThisInst) {
						this.isDelteSuccess = "false";
					} else {
						this.isDelteSuccess = "success";
						setResultMessages("删除成功。");
					}
				}
				return SUCCESS;
			}
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除","0",Constants.BASE_SYS_LOG_AUTHORITY);
			user.setDescription("删除机构失败");
			sysLog.setMenuId("0002.0001");
			sysLog.setMenuName("基础信息管理.机构管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			log.error(e);
		}
		return ERROR;
	}

	/**
	* <p>方法名称: moveInst|描述: 移动机构机构</p>
	* @return null
	*/
	public String moveInst(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("移动机构");
		
		//校验机构是否可以移动
		List list=instService.checkMoveInst(this.request.getParameter("ids"),this.request.getParameter("dest"));
		if(list.size()>0){
			StringBuffer s = new StringBuffer();
			for(int i=0,j=list.size();i<j;i++){
				UBaseInstDO u=(UBaseInstDO) list.get(i);
				s.append(u.getInstName());
				s.append('[');
				s.append(u.getInstId());
				s.append("],");
			}
			s.deleteCharAt(s.lastIndexOf(","));
			s.append("不能移动到自己，或自己的下属机构");
			this.out2page("{success:false,msg:'"+s.toString()+"'}");
		}else{
			String ids[]=request.getParameter("ids").split(",");
			String dest=this.request.getParameter("dest");
			String message="";
			for(int i=0,j=ids.length;i<j;i++){
				if ("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
						&& "1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_INST_AUDIT))) {
					user.setDescription("更新机构" + inst.getInstId());
					try {
						if (instService.modifiedInst(ids[i]) ){
							message+="移动失败,已经有相同机构["
								+ ids[i] + "]在审核中\\n";
						} else {
							// 更新记录
							UBaseInstDO u = (UBaseInstDO) instService.getInstByInstId(ids[i]);
							u.setParentInstId(dest);
							instService.copyInst(instc, u);
							instc.setChangeUser(user.getUserId());
							instc.setChangeTime(new java.sql.Timestamp(System
									.currentTimeMillis()));
							instc
									.setChangeStatus(AuditBase.CHANGE_TYPE_INST_MODIFY);
							// 新增记录
							instService.updateInstc(instc);
							message+="移动机构成功["+ids[i]+"],待审核通过后生效\\n";
						}
					} catch (Exception e) {
						message+="移动机构失败["+ids[i]+"],"+  e.getMessage()+"\\n";
						UBaseSysLogDO sysLog = this.sysLogService
								.setUBaseSysLog(user, "更新", "0",
										Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						user.setAddress("更新机构出错" + ids[i]);
						this.sysLogService.saveUBaseSysLog(sysLog);
						logger.error("", e);
						return null;
					}
				}else{
					user.setDescription("更新机构" + inst.getInstId());
					try{
						// 更新记录
						UBaseInstDO inst = (UBaseInstDO) instService.getInstByInstId(ids[i]);
						inst.setParentInstId(dest);
						instService.updateInst(inst);  
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"更新","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
						
						message+="移动机构成功["+ids[i]+"]\\n";
						
					}catch (Exception e){
						message+="移动机构失败["+ids[i]+"],"+  e.getMessage()+"\\n";
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"更新","0",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("基础信息管理.机构管理");
						user.setAddress("更新机构出错" + ids[i]);
						this.sysLogService.saveUBaseSysLog(sysLog);
						logger.error("", e);
						return null;
					}
				}
			}
			
			out2page("{success:true,msg:'"+message.substring(0,message.lastIndexOf("\\n"))+"'}");
		}
			
		
		return null;
	}

	/**
	* <p>方法名称: listInstMain|描述: 查询获取机构列表</p>
	* @return 用户列表
	*/
	public String listInstMain(){
		try{
			List params = new ArrayList();
			// 获取的字段
			StringBuffer sb = new StringBuffer();
			sb.append("select bInst from UBaseInstDO bInst ");
			sb.append("where 1 = 1 ");
			
			// 用户机构号条件判断
			if (StringUtils.isNotEmpty(inst.getInstId())) {
				if(fixQuery){
					sb.append(" and exists (select 1 from UBaseInstDO a where a.instId=? and substring(bInst.instPath,1,length(a.instPath))=a.instPath)");
					//sb.append(" and bInst.instId = ? ");
					params.add(inst.getInstId());
				}
				else{
					sb.append(" and bInst.instId like ? ");
					params.add("%"+inst.getInstId()+"%");
				}
			}
			// 用户机构名称条件判断
			if (StringUtils.isNotEmpty(inst.getInstName())) {
				sb.append(" and bInst.instName like ?");
				params.add("%"+inst.getInstName()+"%");
			}
			
			sb.append(" and exists (select 1 from UBaseInstDO a where a.instId=? and (substring(bInst.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))");
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			params.add(user.getInstId());
			
			sb.append(" order by bInst.instLevel,bInst.orderNum,bInst.instId");
			
			instService.find(sb.toString(), params, this.paginationList);
			
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			log.error(e);
		}
		return ERROR;
	}
	
	
	/**
	 * @title ajax调用，根据机构编号改变纳税人信息
	 * @description TODO
	 * @author dev4
	 */
	public void onSelectChang() {
		List<Object> ajaxList=new ArrayList<Object>();
		inst=(UBaseInstDO) instService.getInstByInstId(inst.getInstId());
		initTax();
		if(instPathList.size()!=0){
			ajaxList.add(instPathList.get(0));
		}
		ajaxList.add(instPathList);
		try {
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(
					JSONArray.fromObject(ajaxList).toString());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	* <p>方法名称: listInst |描述: 显示机构列表</p>
	* @return 显示机构列表
	*/
	public String listInst(){
		return SUCCESS;
	}

	/**
	* <p>方法名称: listInstHead|描述: 显示机构列表查询帧</p>
	* @return 显示用户列表查询帧
	*/
	public String listInstHead(){
		return SUCCESS;
	}

	/**
	* <p>方法名称: listInstTree|描述: 显示机构列表</p>
	* @return 显示机构列表
	*/
	public String listInstTree(){
		return SUCCESS;
	}
	
	/**
	* <p>方法名称: getInst|描述: 获取机构对象</p>
	* @return 获取机构对象
	*/
	public UBaseInstDO getInst(){
		return inst;
	}

	/**
	* <p>方法名称: setInst|描述: 设置机构对象</p>
	* @param inst 设置机构对象
	*/
	public void setInst(UBaseInstDO inst){
		this.inst = inst;
	}
		
	/**
	* <p>方法名称: getInstList|描述: 获取机构列表</p>
	* @return 获取机构列表
	*/
	public List getInstList(){
		return instList;
	}

	/**
	* <p>方法名称: setInstList|描述: 设置机构列表</p>
	* @param instList 设置机构列表
	*/
	public void setInstList(List instList){
		this.instList = instList;
	}
	
	/**
	* <p>方法名称: getInstLevList|描述: 获取机构级别列表</p>
	* @param instList  获取机构级别列表
	*/
	public List getInstLevList() {
		return instLevList;
	}

	/**
	* <p>方法名称: setInstLevList|描述: 设置机构级别列表</p>
	* @param instList 设置机构级别列表
	*/
	public void setInstLevList(List instLevList) {
		this.instLevList = instLevList;
	}
		
	/**
	* <p>方法名称: setEditType|描述: 设置编辑类型</p>
	* @param editType 设置编辑类型
	*/
	public void setEditType(String editType) {
		this.editType = editType;
	}

	/**
	* <p>方法名称: getEditType|描述: 获取编辑类型</p>
	* @param editType 获取编辑类型
	*/
	public String getEditType() {
		return editType;
	}
	/**
	 * @return 日志服务对象
	 */
	public UBaseSysLogService getSysLogService(){
		return sysLogService;
	}

	/**
	 * @param sysLogService 要设置的 日志服务对象
	 */
	public void setSysLogService(UBaseSysLogService sysLogService){
		this.sysLogService = sysLogService;
	}

	public boolean isFixQuery() {
		return fixQuery;
	}

	public void setFixQuery(boolean fixQuery) {
		this.fixQuery = fixQuery;
	}

	public String getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}
	public List getInstPayerList() {
		return instPayerList;
	}

	public void setInstPayerList(List instPayerList) {
		this.instPayerList = instPayerList;
	}

}
