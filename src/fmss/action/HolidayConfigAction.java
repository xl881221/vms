package fmss.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fmss.action.base.AuditBase;
import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.UserChangingService;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseHolidayDO;
import fmss.dao.entity.UBaseHolidayTypeDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.services.HolidayService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;
import fmss.common.util.SecurityPassword;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


import com.jes.core.api.holiday.HolidayManager;
import com.opensymphony.xwork2.Preparable;

public class HolidayConfigAction extends BaseAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HolidayService holidayService;

	private UBaseHolidayTypeDO holidayTypeDO;
	private UBaseHolidayTypeDO b_holidayTypeDO;
	private UBaseHolidayTypeDO a_holidayTypeDO;

	private String holidayType;
	
	private String RESULT_MESSAGE;
	
	public static final String HTBAK_TABLE = "u_base_holiday_change";
	
	private String readOnly;
	
	private String changes;

	private String isDelete;

	private boolean isUpdate = false;
	
	private boolean unAudit = false;

	private static final String SHORT_PATTERN = "yyyy-MM-dd";

	private static final DateFormat SHORT_FORMAT = new SimpleDateFormat(
			SHORT_PATTERN);

	private String holidayArray;

	IdGenerator idGenerator = IdGenerator.getInstance("HLDT");
	
	private UBaseSysLogService sysLogService;
	
	// 缓存
	private CacheManager cacheManager; 
	
	private UserChangingService userChangingService;
	
	protected static JdbcDaoAccessor dao = (JdbcDaoAccessor) SpringContextUtils.getBean("jdbcDaoAccessor");

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String getHolidayArray() {
		return holidayArray;
	}

	public void setHolidayArray(String holidayArray) {
		this.holidayArray = holidayArray;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

	public String getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}

	public UBaseHolidayTypeDO getHolidayTypeDO() {
		return holidayTypeDO;
	}

	public UBaseHolidayTypeDO getB_holidayTypeDO() {
		return b_holidayTypeDO;
	}

	public void setB_holidayTypeDO(UBaseHolidayTypeDO typeDO) {
		b_holidayTypeDO = typeDO;
	}

	public UBaseHolidayTypeDO getA_holidayTypeDO() {
		return a_holidayTypeDO;
	}

	public void setA_holidayTypeDO(UBaseHolidayTypeDO typeDO) {
		a_holidayTypeDO = typeDO;
	}

	public void setHolidayTypeDO(UBaseHolidayTypeDO holidayTypeDO) {
		this.holidayTypeDO = holidayTypeDO;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public void prepare() throws Exception {
		// TODO Auto-generated method stub

	}

	public String listHoliday() throws Exception {
		readOnly = SecurityPassword.filterStr(readOnly);
		return SUCCESS;
	}

	public String listHolidayHead() throws Exception {
		return SUCCESS;
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String result_message) {
		RESULT_MESSAGE = result_message;
	}

	public String listHolidayLeft() throws Exception {
		if ("1".equals(this.isDelete)) {
			isDelete = null;
		}else if("2".equals(this.isDelete)){
			unAudit=true;
			isDelete = null;
		}
		/*if(request.getParameter("RESULT_MESSAGE")!=null){
			RESULT_MESSAGE=request.getParameter("RESULT_MESSAGE");
		}*/
		
 		readOnly = SecurityPassword.filterStr(readOnly);
		
		return SUCCESS;
	}

	public String listHolidayMain() throws Exception {
		return SUCCESS;
	}

	public String addHolidayType() throws Exception {
		isUpdate = false;
		UBaseConfigDO u=(UBaseConfigDO) holidayService.get(UBaseConfigDO.class, "00005");
		if(u!=null&&"AML".equals(u.getSystemEname())&&(!"NO".equals(u.getEnabled()))){
			request.setAttribute("hasAML", "true");
		} 
		return SUCCESS;
	}
 
	public String editHolidayType() throws Exception {
		try {
			if (StringUtils.isNotEmpty(holidayType)) {
				List list = userChangingService.getHolidayTypeChangesByType(holidayType);
				isUpdate = true;
				if(list!=null&&list.size()>0){
					Map map = (Map)list.get(0);
					holidayTypeDO = new UBaseHolidayTypeDO();
					holidayTypeDO.setEnable(map.get("ENABLE").toString());
					holidayTypeDO.setHolidayName(map.get("HOLIDAY_NAME").toString());
					holidayTypeDO.setHolidayType(map.get("HOLIDAY_TYPE").toString());
					if(map.get("REMARK")!=null){
						holidayTypeDO.setRemark(map.get("REMARK").toString());
					}
					unAudit=true;
				}else{
					holidayTypeDO = holidayService.getHolidayType(holidayType);
				}
			}
			UBaseConfigDO u=(UBaseConfigDO) holidayService.get(UBaseConfigDO.class, "00005");
			if(u!=null&&"AML".equals(u.getSystemEname())&&(!"NO".equals(u.getEnabled()))){
				request.setAttribute("hasAML", "true");
			}
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return SUCCESS;
	}

	public String saveHolidayType() throws Exception {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		try {
			if (holidayTypeDO != null) {
				holidayTypeDO.setHolidayType(String.valueOf(idGenerator
						.getNextKey()));
			
				String holidyNameInsert = SecurityPassword.filterStr(request.getParameter("holidayTypeDO.holidayName"));
				
				String holidyRemarkInsert = SecurityPassword.filterStr(request.getParameter("holidayTypeDO.remark"));
				
				String queryString ="from UBaseHolidayTypeDO where holidayName='"+holidyNameInsert+ "'";
				
				String hql = "select remark from UBaseHolidayTypeDO where remark like'%AMLM%'";
				
				List holidyNameInList = holidayService.find(queryString);				
				
				List holidyRemarkInList= holidayService.find(hql);
				
				// 对同名的节假日做判断
				if(holidyNameInList!=null&&holidyNameInList.size()>0){
					UBaseHolidayTypeDO holidyNameInTable = (UBaseHolidayTypeDO) holidyNameInList.get(0);
					if(holidyNameInTable.getHolidayName().equals(holidyNameInsert)){
						this.setResultMessages("名为 "+holidayTypeDO.getHolidayName()+" 的节假日已存在！");
						 return "input";
					}
				}
				
				// 对备注为AMLM的记录做判断
				boolean rs = false;
				String s="AMLM";
				Pattern pat = Pattern.compile(s);  
				Matcher mat = pat.matcher(holidyRemarkInsert);   
				rs = mat.find();
				if(rs==true&&(holidyRemarkInList!=null&&holidyRemarkInList.size()>0)){
					this.setResultMessages("备注为AMLM的节假日已存在！");
					 return "input";
				}
				//判断是否开启节假日审核
				if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					List list = userChangingService.getHolidayTypeChanges(holidayTypeDO.getHolidayName());
					if (list.size() > 0) {
						setResultMessages("审核表已存在节假日类型["+holidayTypeDO.getHolidayName()+"]");
						return INPUT;
					}else{
						userChangingService.saveHolidayTypeChanges(user, holidayTypeDO,AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD);
						this.setResultMessages("保存成功，审核后才能生效！");
						user.setDescription("保存节假日");
						user.addDescription("类型" + holidayTypeDO.getHolidayName()).addDescription("成功");
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0009");
						sysLog.setMenuName("基础信息管理.节假日管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
					}
				}else{
					holidayService.save(holidayTypeDO);
					this.setResultMessages("保存成功");
				}
					
			}
			isDelete = null;
		} catch (Exception e) {
			log.error("", e);
			user.setDescription("保存节假日");
			user.addDescription("节假日类型" + holidayTypeDO.getHolidayName()).addDescription("失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0009");
			sysLog.setMenuName("基础信息管理.节假日管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			throw e;
		}
		return SUCCESS;
	}

	public String updateHolidayType() throws Exception {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		try {
			if (holidayTypeDO != null) {
				isUpdate = true;
				
				
				String hql = "select remark from UBaseHolidayTypeDO where remark like'%AMLM%'";
				String hqlForName = "from UBaseHolidayTypeDO where holidayName='"+holidayTypeDO.getHolidayName()+"'";
				List holidyRemarkInList= holidayService.find(hql);
				String queryString ="from UBaseHolidayTypeDO where holidayType='"+holidayTypeDO.getHolidayType()+"'" ;
				List holidyNameInList = holidayService.find(queryString);
				List listName = holidayService.find(hqlForName);
				if(holidyNameInList!=null&&holidyNameInList.size()>0){
					UBaseHolidayTypeDO ubht = (UBaseHolidayTypeDO)holidyNameInList.get(0) ;
					if(!holidayTypeDO.getHolidayName().equals(ubht.getHolidayName())){
						if(listName!=null && listName.size()>0){
							this.setResultMessages("节假日名称为 "+holidayTypeDO.getHolidayName()+" 的节假日已存在，请重新修改！");
							return "input";
						}
					}
					if(holidayTypeDO.getRemark()!=null && holidayTypeDO.getRemark().equals("AMLM")){
						if(ubht.getRemark()!=null&&ubht.getRemark().equals("AMLM")&&holidyRemarkInList.size()>1){
							this.setResultMessages("备注内容为AMLM的节假日已存在，请重新修改！");
							return "input";
						}
						if(ubht.getRemark()==null&&holidyRemarkInList.size()>0){
							this.setResultMessages("备注内容为AMLM的节假日已存在，请重新修改！");
							return "input";
						}
						if(ubht.getRemark()!=null&&!ubht.getRemark().equals("AMLM")&&holidyRemarkInList.size()>0){
							this.setResultMessages("备注内容为AMLM的节假日已存在，请重新修改！");
							return "input";
						}
					}
				}
				// 对同名的节假日做判断
				//判断是否开启节假日审核
				if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					List list = userChangingService.getHolidayTypeChanges(holidayTypeDO.getHolidayName());
					if (list.size() > 0) {
						setResultMessages("审核表已存在节假日类型["+holidayTypeDO.getHolidayName()+"]");
						return INPUT;
					}else{
						userChangingService.saveHolidayTypeChanges(user, holidayTypeDO,AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY);
					}
					this.setResultMessages("保存成功，审核后才能生效！");

				}else{
					if (StringUtils.isNotEmpty(holidayType)) {
						holidayService.update(holidayTypeDO);
						this.setResultMessages("保存成功");
					}
				}
				user.setDescription("更新节假日");
				user.addDescription("类型" + holidayTypeDO.getHolidayName()).addDescription("成功");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0009");
				sysLog.setMenuName("基础信息管理.节假日管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
			}
			isDelete = null;
		} catch (Exception e) {
			log.error("", e);
			user.setDescription("更新节假日");
			user.addDescription("类型" + holidayTypeDO.getHolidayName()).addDescription("失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0009");
			sysLog.setMenuName("基础信息管理.节假日管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			throw e;
		}
		return SUCCESS;
	}

	public String deleteHolidayType() throws Exception {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		try {
			if (StringUtils.isNotEmpty(holidayType)) {
				UBaseHolidayTypeDO o = holidayService.getHolidayType(holidayType);
				if(o==null){
					this.isDelete = "2";
					return SUCCESS;
				}
				List list = userChangingService.getHolidayTypeChanges(o.getHolidayName());
				List listH = userChangingService.getHolidayChanges(holidayType);
				if((list!=null&&list.size()>0)||(listH!=null&&listH.size()>0)){
					this.isDelete = "2";
					return SUCCESS;
				}
				//判断是否开启节假日审核
				if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					holidayTypeDO = holidayService.getHolidayType(holidayType);
					userChangingService.saveHolidayTypeChanges(user, holidayTypeDO,AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE);
					//holidayService.deteteHolidayTypeForAudit(holidayType);
					this.isDelete = "3";
				}else{
					holidayService.deteteHolidayType(holidayType);
					this.isDelete = "1";
				}
				user.setDescription("删除节假日类型");
				user.addDescription("类型" + o.getHolidayName()).addDescription("成功");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0002.0009");
				sysLog.setMenuName("基础信息管理.节假日管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
			}
		} catch (Exception e) {
			log.error("", e);
			user.setDescription("删除节假日类型");
			user.addDescription("类型" + holidayType).addDescription("失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0009");
			sysLog.setMenuName("基础信息管理.节假日管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			throw e;
		}
		return SUCCESS;
	}

	//子系统获取节假日资源接口
	public void getHolidayTypeXML() throws Exception {
		try {
			StringBuffer sb = new StringBuffer();
			List list = holidayService.getHolidayTypes(HolidayManager.TYPE_ALL);
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<Response><Data><Tree>");
			sb.append("<TreeNode name='");
			sb.append("节假日类别");
			sb.append("' id='types'");
			sb.append(" levelType='1' ");
			if (CollectionUtils.isNotEmpty(list)) {
				sb.append(" _hasChild='1' ");
			}
			sb.append(" _canSelect='1' ");
			sb.append(">");
			for (int j = 0; list != null && j < list.size(); j++) {
				UBaseHolidayTypeDO o = (UBaseHolidayTypeDO) list.get(j);
				sb.append("<TreeNode name='");
				sb.append(o.getHolidayName());
				sb.append("' id='");
				sb.append(o.getHolidayType());
				sb.append("' levelType='2'");
				sb.append(" _canSelect='1' ");
				sb.append(" value='");
				sb.append("'>");
				sb.append("</TreeNode>");
			}
			sb.append("</TreeNode>");
			sb.append("</Tree></Data></Response>");
			log.debug(sb.toString());
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(sb.toString());
			response.getWriter().close();
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	public void getHolidayTypeThisSystemXML() throws Exception {
		try {
			StringBuffer sb = new StringBuffer();
			List list = holidayService.getAllHolidayTypes();
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<Response><Data><Tree>");
			sb.append("<TreeNode name='");
			sb.append("节假日类别");
			sb.append("' id='types'");
			sb.append(" levelType='1' ");
			if (CollectionUtils.isNotEmpty(list)) {
				sb.append(" _hasChild='1' ");
			}
			sb.append(" _canSelect='1' ");
			sb.append(">");
			for (int j = 0; list != null && j < list.size(); j++) {
				Map map = (Map) list.get(j);
				sb.append("<TreeNode name='");
				sb.append(map.get("NAME"));
				sb.append("' id='");
				sb.append(map.get("HOLIDAY_TYPE"));
				sb.append("' levelType='2'");
				sb.append(" _canSelect='1' ");
				sb.append(" value='");
				sb.append("'>");
				sb.append("</TreeNode>");
			}
			sb.append("</TreeNode>");
			sb.append("</Tree></Data></Response>");
			log.debug(sb.toString());
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(sb.toString());
			response.getWriter().close();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public String listHolidayByType() throws Exception {
		holidayType = SecurityPassword.filterStr(holidayType);
		if (StringUtils.isNotEmpty(holidayType)) {
			List list = holidayService.getHolidays(holidayType);
			List listC = userChangingService.getHolidayChanges(holidayType);
			if(listC!=null&&listC.size()>0){
				unAudit=true;
			}
			if (list != null) {
				StringBuffer sb = new StringBuffer();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					UBaseHolidayDO o = (UBaseHolidayDO) iterator.next();
					sb.append(o.getHolidayValue() + ",");
				}
				holidayArray = sb.toString();
			}
		}
		if(changes!=null&&!changes.equals("")){
			holidayTypeDO = holidayService.getHolidayType(holidayType);
			if(holidayTypeDO==null){
				//List list = userChangingService.getHolidayTypeChanges(holidayTypeDO.getHolidayName());
				List list = userChangingService.getHolidayTypeChangesByType(holidayType);
				a_holidayTypeDO = new UBaseHolidayTypeDO();
				Map map = (Map)list.get(0);
				a_holidayTypeDO.setEnable(map.get("ENABLE").toString());
				a_holidayTypeDO.setHolidayName(map.get("HOLIDAY_NAME").toString());
				a_holidayTypeDO.setHolidayType(holidayType);
				if(map.get("REMARK")!=null){
					a_holidayTypeDO.setRemark(map.get("REMARK").toString());
				}
				holidayTypeDO=a_holidayTypeDO;
				return "changeHoliday";
			}else{
				String returnString="changeHolidayType";
				List list = userChangingService.getHolidayTypeChangesByType(holidayType);
				if(list==null||list.size()==0){
					returnString="changeHoliday";
					list = userChangingService.getHolidayTypeChangesByTypeNoStatus(holidayType);
				}
				a_holidayTypeDO = new UBaseHolidayTypeDO();
				if(list!=null&&list.size()>0){
					Map map = (Map)list.get(0);
					a_holidayTypeDO.setEnable(map.get("ENABLE").toString());
					a_holidayTypeDO.setHolidayName(map.get("HOLIDAY_NAME").toString());
					if(map.get("change_status").toString().equals(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE+"")){
						returnString="changeHoliday";
					}
					a_holidayTypeDO.setHolidayType(holidayType);
					if(map.get("REMARK")!=null){
						a_holidayTypeDO.setRemark(map.get("REMARK").toString());
					}
				}
				b_holidayTypeDO = holidayTypeDO;
				return returnString;
			}
		}
		//request.setAttribute("RESULT_MESSAGE", "节假日有所变更，审核后才能生效。");
		return SUCCESS;
	}

	public void setThis2Holiday() throws Exception {
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		try {
			String thisDate = SecurityPassword.filterStr(request.getParameter("thisDate"));
			String holidayType = SecurityPassword.filterStr(request.getParameter("holidayType"));
			String method = SecurityPassword.filterStr(request.getParameter("method"));
			String removeType = SecurityPassword.filterStr(request.getParameter("removeType"));
			if (StringUtils.isNotEmpty(thisDate)) {
				log.info("set date is:" + thisDate);
				SHORT_FORMAT.parse(thisDate);

				UBaseHolidayDO holiday = new UBaseHolidayDO();
				holiday.setHolidayValue(thisDate);
				holiday.setHolidayType(holidayType);
				
				UBaseHolidayTypeDO o = holidayService.getHolidayType(holidayType);
				if(o==null){//说明新增的节假日类型，未审核。
					o=new UBaseHolidayTypeDO();
					List list = userChangingService.getHolidayTypeChangesByType(holidayType);
					if(list!=null && list.size()>0){//HOLIDAY_TYPE=6682, HOLIDAY_NAME=4, ENABLE=1, REMARK=null, ID=3882, CHANGE_USER=admin,
						Map map = (Map)list.get(0);
						o.setEnable(map.get("ENABLE").toString());
						o.setHolidayName(map.get("HOLIDAY_NAME").toString());
						o.setHolidayType(holidayType);
						if(map.get("REMARK")!=null){
							o.setRemark(map.get("REMARK").toString());
						}
					}
				}
				//判断是否开启节假日审核
				/*if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
					List list = userChangingService.getHolidayChanges(o.getHolidayType());
					if (list.size() > 0) {
						setResultMessages("审核表已存在节假日类型["+o.getHolidayName()+"]的节假日信息");
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().print("未审核的节假日无法进行设置");
						response.getWriter().close();
						return ;
					}
				}*/
				if ("add".equals(method)) {
					if (holidayService.getHoliday(holidayType, thisDate) == null) {
						//判断是否开启节假日审核
						if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
							userChangingService.saveHolidayChanges(user, holiday);
							//holidayService.save(holiday);
							user.setDescription("设置节假日");
							user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0009");
							sysLog.setMenuName("基础信息管理.节假日管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
							response.setContentType("text/html; charset=UTF-8");
							response.getWriter().print("okAdd");
							response.getWriter().close();
						}else{
							holidayService.save(holiday);
							user.setDescription("设置节假日");
							user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0009");
							sysLog.setMenuName("基础信息管理.节假日管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
						}
					}
				}else{
					if("1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))){
						if ("remove".equals(removeType)) {
							userChangingService.addHolidayChanges(user, holiday);
							user.setDescription("设置节假日");
							user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0009");
							sysLog.setMenuName("基础信息管理.节假日管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
							response.setContentType("text/html; charset=UTF-8");
							response.getWriter().print("okRemove");
							response.getWriter().close();
						}else if("removeAdd".equals(removeType)){
							userChangingService.deleteHolidayChanges(user, holiday);
							user.setDescription("设置节假日");
							user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0009");
							sysLog.setMenuName("基础信息管理.节假日管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
							response.setContentType("text/html; charset=UTF-8");
							response.getWriter().print("removeAdd");
							response.getWriter().close();
						}else if("removeRemove".equals(removeType)){
							UBaseHolidayDO uho = holidayService.getHoliday(holiday.getHolidayType(),holiday.getHolidayValue());
							userChangingService.deleteHolidayChanges(user, holiday);
							user.setDescription("设置节假日");
							user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
							UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
							sysLog.setMenuId("0002.0009");
							sysLog.setMenuName("基础信息管理.节假日管理");
							this.sysLogService.saveUBaseSysLog(sysLog);
							response.setContentType("text/html; charset=UTF-8");
							if(uho==null){
								response.getWriter().print("ok");
							}else{
								response.getWriter().print("removeRemove");
							}
							response.getWriter().close();
						}
					}else{
						holidayService.delete(holiday);
						user.setDescription("移除节假日");
						user.addDescription("类型").addDescription(o.getHolidayName()).addDescription("日期").addDescription(thisDate).addDescription("成功");
						UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","1",Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setMenuId("0002.0009");
						sysLog.setMenuName("基础信息管理.节假日管理");
						this.sysLogService.saveUBaseSysLog(sysLog);
					}
				}
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("ok");
			response.getWriter().close();
		} catch (Exception e) {
			log.error("", e);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("节假日设置失败");
			if (e instanceof ParseException)
				response.getWriter().print(",日期格式不对");
			response.getWriter().close();
			
			user.addDescription("设置节假日失败");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"保存","0",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0009");
			sysLog.setMenuName("基础信息管理.节假日管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}
	}

	public void getHolidayOneMonth() throws Exception {
		try {
 			String thisYear = SecurityPassword.filterStr(request.getParameter("thisYear"));
			String thisMonth = SecurityPassword.filterStr(request.getParameter("thisMonth"));
			String holidayType = SecurityPassword.filterStr(request.getParameter("holidayType"));
			List list = holidayService.getHolidays(holidayType);
			String sql2 = "select HOLIDAY_VALUE,CHANGE_STATUS from " + HTBAK_TABLE
			+" where holiday_type=? and audit_status=?";
			List listHolidayChanges = dao.find(sql2, new Object[] {holidayType,new Long(AuditBase.AUDIT_STATUS_NOADUITED)});
			StringBuffer sb = new StringBuffer();
			if (list != null&&list.size()>0) {
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					UBaseHolidayDO o = (UBaseHolidayDO) iterator.next();
					Date date = SHORT_FORMAT.parse(o.getHolidayValue());
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					if ((c.get(Calendar.MONTH) + 1) == Integer
							.parseInt(thisMonth)
							&& c.get(Calendar.YEAR) == Integer
									.parseInt(thisYear)) {
						sb.append(o.getHolidayValue() + ",");
					}
				}
			}
			if(listHolidayChanges!=null&&listHolidayChanges.size()>0){
				for (int i=0;i<listHolidayChanges.size();i++) {
					Map map = (Map)listHolidayChanges.get(i);//{change_status=1, holiday_value=2012-02-01}
					if(map.get("change_status")!=null&&map.get("change_status").toString().equals((AuditBase.CHANGE_TYPE_HOLIDAY_ADD).toString())){
						sb.append(map.get("holiday_value").toString() + "-add,");
					}else{
						String sb_=sb.toString().replaceAll(map.get("holiday_value").toString()+",", map.get("holiday_value").toString() + "-remove,");
						sb = new StringBuffer(sb_) ;
						//sb.append(map.get("holiday_value").toString() + "-remove,");
					}
				}
			}
			holidayArray = sb.toString();
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter()
					.print(holidayArray != null ? holidayArray : "");
			response.getWriter().close();
		} catch (Exception e) {
			log.error(e);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print("节假日设置失败");
			if (e instanceof ParseException)
				response.getWriter().print(",日期格式不对");
			response.getWriter().close();
		}
	}
	
	public void getMostEarlyMonth() throws Exception {
		String thisYear = SecurityPassword.filterStr(request.getParameter("thisYear"));
		String thisMonth = SecurityPassword.filterStr(request.getParameter("thisMonth"));
		String holidayType = SecurityPassword.filterStr(request.getParameter("holidayType"));
		try {
			String date = holidayService.getMostEarlyHoliday(holidayType, thisMonth, thisYear);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(date);
			response.getWriter().close();
		} catch (Exception e) {
			log.error("获取月份失败",e);
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(thisYear + "-" + thisMonth);
			response.getWriter().close();
		}
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public UserChangingService getUserChangingService() {
		return userChangingService;
	}

	public void setUserChangingService(UserChangingService userChangingService) {
		this.userChangingService = userChangingService;
	}

	public boolean isUnAudit() {
		return unAudit;
	}

	public void setUnAudit(boolean unAudit) {
		this.unAudit = unAudit;
	}

	public static JdbcDaoAccessor getDao() {
		return dao;
	}
}
