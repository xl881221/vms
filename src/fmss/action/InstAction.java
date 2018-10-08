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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����02:16:31
 * @����: [AjaxInstAction]�첽��ȡ������Ϣ�Ŀ�����
 */
public class InstAction extends JSONProviderAction{

	private static final long serialVersionUID = 1L;	
	protected static final String FORM_INST = "formInst";
	final String DIC_TYPE_INSTLAYER = "LEVEL";		//���������ֵ�����
	final String TAXPAYERTYPE="TAXPAYER_TYPE";		//��������ֵ�����
	
	private UBaseInstDO inst = new UBaseInstDO(); 	// ������������ʵ��
	private UBaseInstChangeDO instc = new UBaseInstChangeDO(); 	// ��������change����ʵ��
	private List<UBaseInstDO> instList; 							// �����б�
	private List instLevList; 						// ���������б�
	private List instPayerList;						//��������б�
	private List<Object> instTnumList=new ArrayList<Object>();    //��˰��ʶ����б�
	private String editType;						//�༭����
	private String isRoot;
	private List<Object> instPathList = new ArrayList<Object>();						//��ǰ���������ϼ������б�
	
	public List<Object> getInstPathList() {
		return instPathList;
	}

	public void setInstPathList(List<Object> instPathList) {
		this.instPathList = instPathList;
	}

	private List instSystemReal;                    //ģ���б���
	
	private List instEmailAddrs;                    //�����ַ��
	
	private List selectedEmailAddrs;                //ѡ�е������ַ��
	
	String vSystemId;
	
	public String getvSystemId() {
		return vSystemId;
	}

	public void setvSystemId(String vSystemId) {
		this.vSystemId = vSystemId;
	}

	private String emailJsonp;
	
	private CacheManager cacheManager; // ����
	
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

	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;

	/** ��¼��־��Ϣ*/
	private static final Logger logger = Logger.getLogger(InstAction.class);
	
	/** ����Ϣר����ת����*/
	protected static final String STREAM = "stream";
	
	/** ������Ϣ������*/
	private InstService instService;
	
	/** �ֵ���Ϣ������*/
	private DictionaryService dicService;

	/**����ɾ�������ʾ��Ϣ**/
	private String isDelteSuccess = null;
	
	private boolean fixQuery = false;
	
	public String getIsDelteSuccess() {
		return isDelteSuccess;
	}

	public void setIsDelteSuccess(String isDelteSuccess) {
		this.isDelteSuccess = isDelteSuccess;
	}

	/**
	 * <p>��������: getInstService|����:��ȡ������Ϣ������ </p>
	 * @return ��ȡ������Ϣ������
	 */
	public InstService getInstService() {
		return instService;
	}

	/**
	 * <p>��������: setInstService|����:���û�����Ϣ������ </p>
	 * @return ���û�����Ϣ������
	 */
	public void setInstService(InstService instService) {
		this.instService = instService;
	}


	/**
	 * <p>��������: getDicService|����:��ȡ�ֵ���Ϣ������ </p>
	 * @return ��ȡ�ֵ���Ϣ������
	 */
	public DictionaryService getDicService() {
		return dicService;
	}

	/**
	 * <p>��������: setDicService|����:�����ֵ���Ϣ������ </p>
	 * @return �����ֵ���Ϣ������
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}


	/**
	 * <p>��������: list|����:��ת����������ҳ�� </p>
	 * @return ���ص��ɹ�ҳ��
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
	* <p>��������: saveInst|����: ��ȡ������Ϣ������</p>
	* @return ����Ľ��ֵ
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
					user.setDescription("��������" + inst.getInstId() + "[" + instc.getInstName() + "]");
					try{
						instService.copyInst(instc,inst);
						instc.setChangeUser(user.getUserId());
						instc.setChangeTime(new java.sql.Timestamp(System.currentTimeMillis()));
						instc.setChangeStatus(AuditBase.CHANGE_TYPE_INST_ADD);
						// ������¼
						instService.saveInst(instc); 
//						updateInstRela1(inst);
					
						setResultMessages("����ɹ�,�����ͨ������Ч");
						
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						this.sysLogService.saveUBaseSysLog(sysLog);
					}catch (Exception e){ 
						logger.error("", e);
						setResultMessages("��������ʧ��");
						if(e instanceof InstLevelException)
							setResultMessages("��������ʧ�ܣ�" + e.getMessage());
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						user.setAddress("������������" + inst.getInstId() + "[" + inst.getInstName() + "]");
						this.sysLogService.saveUBaseSysLog(sysLog);
						InitPage();
						return INPUT;
					}
				}else{
					user.setDescription("���»���" + inst.getInstId());
					try {
						if (instService.modifiedInst(inst.getInstId())) {
							setResultMessages("�޸�ʧ��,�Ѿ�����ͬ����["
									+ inst.getInstId() + "]�������");
						} else {
							// ���¼�¼
							instService.copyInst(instc, inst);
							instc.setChangeUser(user.getUserId());
							instc.setChangeTime(new java.sql.Timestamp(System
									.currentTimeMillis()));
							instc
									.setChangeStatus(AuditBase.CHANGE_TYPE_INST_MODIFY);
							// ������¼
							instService.updateInstc(instc);

							setResultMessages("�޸ĳɹ�,�����ͨ������Ч");

							if (user.getInstId().equals(inst.getInstId())) {
								user.setInstCname(inst.getInstName());
							}
						}
					} catch (Exception e) {
						setResultMessages("���»���ʧ�ܡ�");
						if (e instanceof InstLevelException)
							setResultMessages("���»���ʧ�ܣ�" + e.getMessage());
						UBaseSysLogDO sysLog = this.sysLogService
								.setUBaseSysLog(user, "����", "0",
										Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						user.setAddress("���»�������" + inst.getInstId());
						this.sysLogService.saveUBaseSysLog(sysLog);
						InitPage();
						logger.error("", e);
						return INPUT;
					}
				}
//				setResultMessages("����ɹ�,�����ͨ������Ч");
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
				user.setDescription("��������" + inst.getInstId() + "[" + inst.getInstName() + "]");
				try{
					
					if(instService.getUBaseInstDOBytaxPernumberAndTaxPayerType(inst.getTaxpernumber(),inst.getTaxPayerType())!=null){
						setResultMessages("�˻����Ѵ��ڸ���˰��ʶ��ţ�����������");
					}
					else{
					// ������¼
					instService.saveInst(inst); 
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("������Ϣ����.��������");
					this.sysLogService.saveUBaseSysLog(sysLog);
					setResultMessages("����ɹ���");
					}
				}catch (Exception e){ 
					logger.error("", e);
					setResultMessages("");
					if(e instanceof InstLevelException)
						setResultMessages("��������ʧ�ܣ�" + e.getMessage());
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("������Ϣ����.��������");
					user.setAddress("������������" + inst.getInstId() + "[" + inst.getInstName() + "]");
					this.sysLogService.saveUBaseSysLog(sysLog);
					InitPage();
					return INPUT;
				}
			}else{
				user.setDescription("���»���" + inst.getInstId());
				try{
					// ���¼�¼
					instService.updateInst(inst);  
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("������Ϣ����.��������");
					this.sysLogService.saveUBaseSysLog(sysLog);
					
					instService.updateInstCName(inst);
					
					if (user.getInstId().equals(inst.getInstId())) {
						user.setInstCname(inst.getInstName());
						user.setInstIsHead(inst.getIsHead());
					}
				}catch (Exception e){
					setResultMessages("���»���ʧ�ܡ�"); 
					if(e instanceof InstLevelException)
						setResultMessages("���»���ʧ�ܣ�" + e.getMessage());
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
							"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0002.0001");
					sysLog.setMenuName("������Ϣ����.��������");
					user.setAddress("���»�������" + inst.getInstId());
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
			user.setDescription("�����������" + inst.getInstId());
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0001");
			sysLog.setMenuName("������Ϣ����.��������");
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
				logger.info("ˢ����ϵͳ:"+resp);
			} catch (Throwable e) {
				logger.error("ˢ����ϵͳ���淢���쳣!",e);
			} finally {
				if (method != null) {
					method.releaseConnection();
				}
			}
		}
		
	}
	/**
	* <p>��������: InitPage|����: ��ʼ��ҳ����Ϣ</p>
	* @return instList �ϼ������б�
	* @return instLevList ���������б�
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
	* <p>��������: viewInst|����: ͨ��������Ż�ȡ������Ϣ</p>
	* @return inst ��������
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
	* <p>��������: viewInst|����: ͨ��������Ż�ȡ������Ϣ</p>
	* @return inst ��������
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
	* <p>��������: editInst|����: �༭����ͨ��������Ż�ȡ������Ϣ</p>
	* @return inst ͨ��������Ż�ȡ�Ļ�����Ϣ
	* @return instList ����ѡ���ϼ������Ļ����б�
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
	* <p>��������: createInst|����: ��������</p>
	* @return instList ����ѡ���û����������Ļ����б�
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
	 * @title ��ʼ����˰������
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
	 * @title ��ʼ����˰������
	 * @description TODO
	 * @author dev4
	 */
	/**
	 * ����ѡ���������ҷ���Ϣ �¹���ȫ��ʹ�ô˷��������loadOurInfo();
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
	* <p>��������: ģ��Ԫ�������û�����</p>
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
	* <p>��������: InitInstList|����: ��ʼ���ϼ������б������ӿ���</p> 
	* @return instList ����ѡ���ϼ������Ļ����б�
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
		this.setResultMessages("��ʼ���ɹ���");
		return SUCCESS;
	}
	
	/**
	* <p>��������: deleteInst|����: ɾ������</p>
	* @return ɾ������
	*/
	public String deleteInst(){
		boolean deleteOwnInst = false;
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("ɾ������");
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
									setResultMessages("����[" + getIds().get(i).toString() + "]�´����û�,����ֱ��ɾ��,���ȵ��û�������ɾ���û����µ��û�");
									isExistUserThisInst = true;
									break;
								}
								// instService.deleteUserInInst(userList);
								// ɾ����ɫ�������Ĵ˻��� (���ͨ��֮����ɾ��)
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
//							user.setDescription("ɾ������" + StringUtils.join(getIds().iterator(), ","));
//							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "ɾ��", "1",
//									Constants.BASE_SYS_LOG_AUTHORITY);
//							sysLog.setMenuId("0002.0001");
//							sysLog.setMenuName("������Ϣ����.��������");
//							this.sysLogService.saveUBaseSysLog(sysLog);
						}
					} catch (Exception e) {
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "ɾ��", "0",
								Constants.BASE_SYS_LOG_AUTHORITY);
						user.setDescription("ɾ������ʧ��" + StringUtils.join(getIds().iterator(), ","));
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						this.sysLogService.saveUBaseSysLog(sysLog);
						setResultMessages("ɾ������ʧ�ܣ����ܸû�������ʹ�á�");
					}
					if (deleteOwnInst) {
						this.isDelteSuccess = "false";
						this.setResultMessages("�û�����ɾ���Լ����ڻ���");
					} else if (isExistUserThisInst) {
						this.isDelteSuccess = "false";
					} else {
						this.isDelteSuccess = "success";
						setResultMessages("�ѱ��棬���֮�����ɾ����");
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
									setResultMessages("����[" + getIds().get(i).toString() + "]�´����û�,����ֱ��ɾ��,���ȵ��û�������ɾ���û����µ��û�");
									isExistUserThisInst = true;
									break;
								}
								// instService.deleteUserInInst(userList);
								// ɾ����ɫ�������Ĵ˻���
								String rolehql = "delete from UAuthRoleResourceDO res where res.resDetailValue='"
										+ getIds().get(i).toString() + "' and res.resId = '35'";
								instService.executeUpdate(rolehql);
							} else {
								deleteOwnInst = true;
							}
						}
						if(!isExistUserThisInst){
							instService.deleteAllInst(ls);
							user.setDescription("ɾ������" + StringUtils.join(getIds().iterator(), ","));
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "ɾ��", "1",
									Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0001");
							sysLog.setMenuName("������Ϣ����.��������");
							this.sysLogService.saveUBaseSysLog(sysLog);
						}
					} catch (Exception e) {
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user, "ɾ��", "0",
								Constants.BASE_SYS_LOG_AUTHORITY);
						user.setDescription("ɾ������ʧ��" + StringUtils.join(getIds().iterator(), ","));
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						this.sysLogService.saveUBaseSysLog(sysLog);
						setResultMessages("ɾ������ʧ�ܣ����ܸû�������ʹ�á�");
					}
					if (deleteOwnInst) {
						this.isDelteSuccess = "false";
						this.setResultMessages("�û�����ɾ���Լ����ڻ���");
					} else if (isExistUserThisInst) {
						this.isDelteSuccess = "false";
					} else {
						this.isDelteSuccess = "success";
						setResultMessages("ɾ���ɹ���");
					}
				}
				return SUCCESS;
			}
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��","0",Constants.BASE_SYS_LOG_AUTHORITY);
			user.setDescription("ɾ������ʧ��");
			sysLog.setMenuId("0002.0001");
			sysLog.setMenuName("������Ϣ����.��������");
			this.sysLogService.saveUBaseSysLog(sysLog);
			log.error(e);
		}
		return ERROR;
	}

	/**
	* <p>��������: moveInst|����: �ƶ���������</p>
	* @return null
	*/
	public String moveInst(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("�ƶ�����");
		
		//У������Ƿ�����ƶ�
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
			s.append("�����ƶ����Լ������Լ�����������");
			this.out2page("{success:false,msg:'"+s.toString()+"'}");
		}else{
			String ids[]=request.getParameter("ids").split(",");
			String dest=this.request.getParameter("dest");
			String message="";
			for(int i=0,j=ids.length;i<j;i++){
				if ("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
						&& "1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_INST_AUDIT))) {
					user.setDescription("���»���" + inst.getInstId());
					try {
						if (instService.modifiedInst(ids[i]) ){
							message+="�ƶ�ʧ��,�Ѿ�����ͬ����["
								+ ids[i] + "]�������\\n";
						} else {
							// ���¼�¼
							UBaseInstDO u = (UBaseInstDO) instService.getInstByInstId(ids[i]);
							u.setParentInstId(dest);
							instService.copyInst(instc, u);
							instc.setChangeUser(user.getUserId());
							instc.setChangeTime(new java.sql.Timestamp(System
									.currentTimeMillis()));
							instc
									.setChangeStatus(AuditBase.CHANGE_TYPE_INST_MODIFY);
							// ������¼
							instService.updateInstc(instc);
							message+="�ƶ������ɹ�["+ids[i]+"],�����ͨ������Ч\\n";
						}
					} catch (Exception e) {
						message+="�ƶ�����ʧ��["+ids[i]+"],"+  e.getMessage()+"\\n";
						UBaseSysLogDO sysLog = this.sysLogService
								.setUBaseSysLog(user, "����", "0",
										Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						user.setAddress("���»�������" + ids[i]);
						this.sysLogService.saveUBaseSysLog(sysLog);
						logger.error("", e);
						return null;
					}
				}else{
					user.setDescription("���»���" + inst.getInstId());
					try{
						// ���¼�¼
						UBaseInstDO inst = (UBaseInstDO) instService.getInstByInstId(ids[i]);
						inst.setParentInstId(dest);
						instService.updateInst(inst);  
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"����","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						this.sysLogService.saveUBaseSysLog(sysLog);
						
						message+="�ƶ������ɹ�["+ids[i]+"]\\n";
						
					}catch (Exception e){
						message+="�ƶ�����ʧ��["+ids[i]+"],"+  e.getMessage()+"\\n";
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
								"����","0",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0001");
						sysLog.setMenuName("������Ϣ����.��������");
						user.setAddress("���»�������" + ids[i]);
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
	* <p>��������: listInstMain|����: ��ѯ��ȡ�����б�</p>
	* @return �û��б�
	*/
	public String listInstMain(){
		try{
			List params = new ArrayList();
			// ��ȡ���ֶ�
			StringBuffer sb = new StringBuffer();
			sb.append("select bInst from UBaseInstDO bInst ");
			sb.append("where 1 = 1 ");
			
			// �û������������ж�
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
			// �û��������������ж�
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
	 * @title ajax���ã����ݻ�����Ÿı���˰����Ϣ
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
	* <p>��������: listInst |����: ��ʾ�����б�</p>
	* @return ��ʾ�����б�
	*/
	public String listInst(){
		return SUCCESS;
	}

	/**
	* <p>��������: listInstHead|����: ��ʾ�����б��ѯ֡</p>
	* @return ��ʾ�û��б��ѯ֡
	*/
	public String listInstHead(){
		return SUCCESS;
	}

	/**
	* <p>��������: listInstTree|����: ��ʾ�����б�</p>
	* @return ��ʾ�����б�
	*/
	public String listInstTree(){
		return SUCCESS;
	}
	
	/**
	* <p>��������: getInst|����: ��ȡ��������</p>
	* @return ��ȡ��������
	*/
	public UBaseInstDO getInst(){
		return inst;
	}

	/**
	* <p>��������: setInst|����: ���û�������</p>
	* @param inst ���û�������
	*/
	public void setInst(UBaseInstDO inst){
		this.inst = inst;
	}
		
	/**
	* <p>��������: getInstList|����: ��ȡ�����б�</p>
	* @return ��ȡ�����б�
	*/
	public List getInstList(){
		return instList;
	}

	/**
	* <p>��������: setInstList|����: ���û����б�</p>
	* @param instList ���û����б�
	*/
	public void setInstList(List instList){
		this.instList = instList;
	}
	
	/**
	* <p>��������: getInstLevList|����: ��ȡ���������б�</p>
	* @param instList  ��ȡ���������б�
	*/
	public List getInstLevList() {
		return instLevList;
	}

	/**
	* <p>��������: setInstLevList|����: ���û��������б�</p>
	* @param instList ���û��������б�
	*/
	public void setInstLevList(List instLevList) {
		this.instLevList = instLevList;
	}
		
	/**
	* <p>��������: setEditType|����: ���ñ༭����</p>
	* @param editType ���ñ༭����
	*/
	public void setEditType(String editType) {
		this.editType = editType;
	}

	/**
	* <p>��������: getEditType|����: ��ȡ�༭����</p>
	* @param editType ��ȡ�༭����
	*/
	public String getEditType() {
		return editType;
	}
	/**
	 * @return ��־�������
	 */
	public UBaseSysLogService getSysLogService(){
		return sysLogService;
	}

	/**
	 * @param sysLogService Ҫ���õ� ��־�������
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
