package fmss.action;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.jes.core.file.FileManagerDBImpl;

import fmss.common.db.IdGenerator;
import fmss.common.util.Constants;
import fmss.common.util.Types;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseNoticeFeedBackDO;
import fmss.dao.entity.UBaseNoticeInfoDO;
import fmss.dao.entity.UBaseNoticeLogDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.services.DictionaryService;
import fmss.services.NoticeService;
import fmss.services.UBaseSysLogService;

/**
 * <p> 版权所有:(C)2003-2010  </p>
 * @作者: GongRunLin
 * @日期: 2009-7-25 上午11:50:00
 * @描述: [NoticeAction]通告管理相关Action
 */
public class NoticeAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private String id; // ID
	private String[] nIds; // 通告编号数组
	private UBaseNoticeInfoDO notice = new UBaseNoticeInfoDO(); // 通告
	private UBaseNoticeFeedBackDO feedBack = new UBaseNoticeFeedBackDO(); // 反馈
	private UBaseNoticeLogDO log = new UBaseNoticeLogDO(); // 日志
	private List notices; // 通告列表
	private List feedBacks; // 反馈列表
	private List logs; // 日志列表
	private List affixs; // 附件列表
	private List types; // 通告类型
	private File[] file;
	private List fileFileName;
	private List fileContentType;
	private static IdGenerator idGenerator; // id生成器
	private NoticeService noticeService; // 通告服务
	/** sysLogService 日志服务 */
	private UBaseSysLogService sysLogService;
	private FileManagerDBImpl fileManager;

	public void setFileManager(FileManagerDBImpl fileManager){
		this.fileManager = fileManager;
	}

	/**
	 * <p> 方法名称: listNotice|描述: 查询获取通告列表 </p>
	 * @return 通告列表
	 */
	public String listNotice(){
		try{
			LoginDO user = getUser();
			this.types = noticeService.getSubSystems(user.getUserId());
			Map map = new HashMap();
			Map temp = null;
			for(int i = 0; i < types.size(); i++){
				temp = (Map) types.get(i);
				map.put(temp.get("key"), temp.get("value"));
			}
			notices = noticeService.findNotices(user.getUserId(), notice,
					this.paginationList);
			if(notices != null){
				UBaseNoticeInfoDO n = null;
				for(int i = 0; i < notices.size(); i++){
					n = (UBaseNoticeInfoDO) notices.get(i);
					n.setType((String) map.get(n.getType()));
				}
			}
			Map pub = new HashMap();
			pub.put("key", "");
			pub.put("value", "全部");
			types.add(0, pub);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: listNotice|描述: 查询获取无管理通告列表 </p>
	 * @return 通告列表
	 */
	public String listSNotice(){
		try{
			LoginDO user = getUser();
			this.types = noticeService.getSubSystems(user.getUserId(), true);
			Map map = new HashMap();
			Map temp = null;
			for(int i = 0; i < types.size(); i++){
				temp = (Map) types.get(i);
				map.put(temp.get("key"), temp.get("value"));
			}
			notices = noticeService.findSNotices(user.getUserId(), notice,
					this.paginationList);
			if(notices != null){
				UBaseNoticeInfoDO n = null;
				for(int i = 0; i < notices.size(); i++){
					n = (UBaseNoticeInfoDO) notices.get(i);
					if(map.get(n.getType()) != null){
						n.setType(map.get(n.getType()).toString());
					}
				}
			}
			Map pub = new HashMap();
			pub.put("key", "");
			pub.put("value", "全部");
			types.add(0, pub);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: editNotice|描述: 编辑通告 </p>
	 * @return
	 */
	public String editNotice(){
		this.types = noticeService.getSubSystems(getUser().getUserId());
		if(id != null && !id.equals("")){
			notice = noticeService.getNoticeById(new Long(id));
		}
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: saveNotice|描述: 保存通告 </p>
	 * @return
	 */
	public String saveNotice(){
		try{
			if(file != null && file.length > 0){
				for(int i = 0; i < file.length; i++){
					if(((String) fileFileName.get(i)).length() > 100){
						this.setResultMessages("文件名过长[" + fileFileName.get(i)
								+ "]");
						return SUCCESS;
					}
					if(file[i].length() > 1048576000l){
						setResultMessages("文件：" + fileFileName.get(i)
								+ " 大小超过1GB，窗口将关闭，请重新新增！");
						return SUCCESS;
					}
				}
			}
			UBaseNoticeInfoDO ni = notice;
			String path = ServletActionContext.getRequest().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
			LoginDO user = getUser(); // 获得内存用户
			if(notice.getId() == null || notice.getId().longValue() == 0){
				idGenerator = IdGenerator
						.getInstance(DictionaryService.NOTICE_MAIN_TYPE);
				ni.setId(new Long(idGenerator.getNextKey()));
				ni.setCreateTime(new Date());
				ni.setUserId(user.getUserId());
				noticeService.saveNotice(ni, user, path, file, fileFileName);
				user.setDescription("保存通告信息");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0020");
				sysLog.setMenuName("通告管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
			}else{
				ni = (UBaseNoticeInfoDO) noticeService.getObjectById(
						UBaseNoticeInfoDO.class, notice.getId(), "id");
				this.isChangeTime(ni, notice);
				ni.setContent(notice.getContent());
				ni.setTitle(notice.getTitle());
				ni.setType(notice.getType());
				ni.setStatus(notice.getStatus());
				noticeService.saveNotice(ni, user, path, file, fileFileName,
						nIds);
				user.setDescription("更新通告信息");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"更新", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0020");
				sysLog.setMenuName("首页.通告管理");
				this.sysLogService.saveUBaseSysLog(sysLog);
			}
			this.setResultMessages("保存成功！");
			// this.resultMessages =
			// "<script>parent.clearWaiting();alert(\"保存成功！\");CloseWindow(true);</script>";
		}catch (Exception e){
			LoginDO user = getUser();
			user.setDescription("保存通告信息");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"保存", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			this.setResultMessages("保存失败！");
			// this.resultMessages =
			// "<script>parent.clearWaiting();alert(\"保存失败！\");parent.isSubmit=false;CloseWindow(true);</script>";
		}
		return SUCCESS;
	}

	/**
	 * 如果修改了信息内容，那么更新发布时间
	 * @param ni
	 * @param notice
	 * @return
	 */
	private boolean isChangeTime(UBaseNoticeInfoDO ni, UBaseNoticeInfoDO notice){
		boolean b = false;
		try{
			if(null != ni && null != notice){
				if(!NullToStr(notice.getContent()).equals(
						NullToStr(ni.getContent()))
						|| !NullToStr(notice.getTitle()).equals(
								NullToStr(ni.getTitle()))
						|| !NullToStr(notice.getType()).equals(
								NullToStr(ni.getType()))){
					ni.setCreateTime(new Date());
				}
			}
		}catch (Exception e){
		}
		return b;
	}

	private String NullToStr(String str){
		if(null == str){
			str = "";
		}
		return str;
	}

	/**
	 * <p> 方法名称: deleteNotice|描述: 删除通告 </p>
	 * @return
	 */
	public String deleteNotice(){
		LoginDO user = getUser();
		user.setDescription("删除通告信息");
		try{
			String path = ServletActionContext.getRequest().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
			noticeService.delete(nIds, path);
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"删除", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 置顶通告
	 * @return
	 */
	public String toHeadNotice(){
		LoginDO user = getUser();
		user.setDescription("置顶通告信息");
		try{
			noticeService.toHeadNotice(nIds);
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"置顶", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"置顶", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("通告管理");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: viewNotice|描述: 查看通告 </p>
	 * @return
	 */
	public String viewNotice(){
		try{
			LoginDO user = getUser(); // 获得内存用户
			this.types = noticeService.getSubSystems(user.getUserId());
			if(id != null && !id.equals("")){
				notice = noticeService.getNoticeById(new Long(id));
				for(int i = 0; i < types.size(); i++){
					Map temp = (Map) types.get(i);
					if(notice.getType().equals(temp.get("key"))){
						notice.setType(temp.get("value").toString());
						break;
					}
				}
				// 保存通告查看日志
				idGenerator = IdGenerator
						.getInstance(DictionaryService.NOTICE_LOG_TYPE);
				log.setId(new Long(idGenerator.getNextKey()));
				log.setNoticeId(notice.getId());
				log.setIp(user.getIp());
				log.setUserCName(user.getUserCname());
				log.setUserEName(user.getUserEname());
				log.setViewTime(new Date());
				noticeService.save(log);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * <p> 方法名称: viewFile|描述: 查看附件 </p>
	 * @return
	 */
	public String viewFile(){
		return SUCCESS;
	}

	public void downloadAttach(){
		if(id != null && !id.equals("")){
			String path = ServletActionContext.getRequest().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
			Map types = Types.MIME_TYPES;
			String type = (String) types.get("def");
			String fileName = fileManager.getFileName(new Long(id).longValue());
			String pt = fileName.indexOf(".") != -1 ? fileName
					.substring(fileName.lastIndexOf(".") + 1) : "";
			HttpServletResponse response = ServletActionContext.getResponse();
			if(types.containsKey(pt))
				type = (String) types.get(pt);
			response.setContentType(type);
			try{
				response.setHeader("Content-Disposition",
						"attachment; filename="
								+ new String(fileName.getBytes("gbk"),
										"ISO8859-1"));
				OutputStream out = response.getOutputStream();
				fileManager.downLoadFile(new Long(id).longValue(), out);
				out.flush();
				out.close();
			}catch (Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * <p> 方法名称: listLog|描述: 查询获取通告日志列表 </p>
	 * @return 通告日志列表
	 */
	public String listLog(){
		try{
			logs = noticeService.findLogs(log, this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> 方法名称: listFeedBack|描述: 查询获取通告反馈列表 </p>
	 * @return 通告反馈列表
	 */
	public String listFeedBack(){
		try{
			feedBacks = noticeService.findFeedBacks(feedBack,
					this.paginationList);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	private LoginDO getUser(){
		return (LoginDO) ServletActionContext.getRequest().getSession()
				.getAttribute("LOGIN_USER"); // 获得内存用户
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	/**
	 * @return nIds
	 */
	public String[] getNIds(){
		return nIds;
	}

	/**
	 * @param nIds 要设置的 nIds
	 */
	public void setNIds(String[] nIds){
		this.nIds = nIds;
	}

	/**
	 * @return notice
	 */
	public UBaseNoticeInfoDO getNotice(){
		return notice;
	}

	/**
	 * @param notice 要设置的 notice
	 */
	public void setNotice(UBaseNoticeInfoDO notice){
		this.notice = notice;
	}

	/**
	 * @return feedBack
	 */
	public UBaseNoticeFeedBackDO getFeedBack(){
		return feedBack;
	}

	/**
	 * @param feedBack 要设置的 feedBack
	 */
	public void setFeedBack(UBaseNoticeFeedBackDO feedBack){
		this.feedBack = feedBack;
	}

	/**
	 * @return log
	 */
	public UBaseNoticeLogDO getLog(){
		return log;
	}

	/**
	 * @param log 要设置的 log
	 */
	public void setLog(UBaseNoticeLogDO log){
		this.log = log;
	}

	/**
	 * @return feedBacks
	 */
	public List getFeedBacks(){
		return feedBacks;
	}

	/**
	 * @param feedBacks 要设置的 feedBacks
	 */
	public void setFeedBacks(List feedBacks){
		this.feedBacks = feedBacks;
	}

	/**
	 * @return logs
	 */
	public List getLogs(){
		return logs;
	}

	/**
	 * @param logs 要设置的 logs
	 */
	public void setLogs(List logs){
		this.logs = logs;
	}

	/**
	 * @return affixs
	 */
	public List getAffixs(){
		return affixs;
	}

	/**
	 * @param affixs 要设置的 affixs
	 */
	public void setAffixs(List affixs){
		this.affixs = affixs;
	}

	/**
	 * @return notices
	 */
	public List getNotices(){
		return notices;
	}

	/**
	 * @param notices 要设置的 notices
	 */
	public void setNotices(List notices){
		this.notices = notices;
	}

	/**
	 * @return types
	 */
	public List getTypes(){
		return types;
	}

	/**
	 * @param types 要设置的 types
	 */
	public void setTypes(List types){
		this.types = types;
	}

	public File[] getFile(){
		return file;
	}

	public void setFile(File[] file){
		this.file = file;
	}

	public List getFileFileName(){
		return fileFileName;
	}

	public void setFileFileName(List fileFileName){
		this.fileFileName = fileFileName;
	}

	public List getFileContentType(){
		return fileContentType;
	}

	public void setFileContentType(List fileContentType){
		this.fileContentType = fileContentType;
	}

	/**
	 * @return noticeService
	 */
	public NoticeService getNoticeService(){
		return noticeService;
	}

	/**
	 * @param noticeService 要设置的 noticeService
	 */
	public void setNoticeService(NoticeService noticeService){
		this.noticeService = noticeService;
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
}
