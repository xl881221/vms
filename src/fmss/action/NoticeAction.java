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
 * <p> ��Ȩ����:(C)2003-2010  </p>
 * @����: GongRunLin
 * @����: 2009-7-25 ����11:50:00
 * @����: [NoticeAction]ͨ��������Action
 */
public class NoticeAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private String id; // ID
	private String[] nIds; // ͨ��������
	private UBaseNoticeInfoDO notice = new UBaseNoticeInfoDO(); // ͨ��
	private UBaseNoticeFeedBackDO feedBack = new UBaseNoticeFeedBackDO(); // ����
	private UBaseNoticeLogDO log = new UBaseNoticeLogDO(); // ��־
	private List notices; // ͨ���б�
	private List feedBacks; // �����б�
	private List logs; // ��־�б�
	private List affixs; // �����б�
	private List types; // ͨ������
	private File[] file;
	private List fileFileName;
	private List fileContentType;
	private static IdGenerator idGenerator; // id������
	private NoticeService noticeService; // ͨ�����
	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;
	private FileManagerDBImpl fileManager;

	public void setFileManager(FileManagerDBImpl fileManager){
		this.fileManager = fileManager;
	}

	/**
	 * <p> ��������: listNotice|����: ��ѯ��ȡͨ���б� </p>
	 * @return ͨ���б�
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
			pub.put("value", "ȫ��");
			types.add(0, pub);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> ��������: listNotice|����: ��ѯ��ȡ�޹���ͨ���б� </p>
	 * @return ͨ���б�
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
			pub.put("value", "ȫ��");
			types.add(0, pub);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * <p> ��������: editNotice|����: �༭ͨ�� </p>
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
	 * <p> ��������: saveNotice|����: ����ͨ�� </p>
	 * @return
	 */
	public String saveNotice(){
		try{
			if(file != null && file.length > 0){
				for(int i = 0; i < file.length; i++){
					if(((String) fileFileName.get(i)).length() > 100){
						this.setResultMessages("�ļ�������[" + fileFileName.get(i)
								+ "]");
						return SUCCESS;
					}
					if(file[i].length() > 1048576000l){
						setResultMessages("�ļ���" + fileFileName.get(i)
								+ " ��С����1GB�����ڽ��رգ�������������");
						return SUCCESS;
					}
				}
			}
			UBaseNoticeInfoDO ni = notice;
			String path = ServletActionContext.getRequest().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
			LoginDO user = getUser(); // ����ڴ��û�
			if(notice.getId() == null || notice.getId().longValue() == 0){
				idGenerator = IdGenerator
						.getInstance(DictionaryService.NOTICE_MAIN_TYPE);
				ni.setId(new Long(idGenerator.getNextKey()));
				ni.setCreateTime(new Date());
				ni.setUserId(user.getUserId());
				noticeService.saveNotice(ni, user, path, file, fileFileName);
				user.setDescription("����ͨ����Ϣ");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0020");
				sysLog.setMenuName("ͨ�����");
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
				user.setDescription("����ͨ����Ϣ");
				UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
						"����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLog.setMenuId("0020");
				sysLog.setMenuName("��ҳ.ͨ�����");
				this.sysLogService.saveUBaseSysLog(sysLog);
			}
			this.setResultMessages("����ɹ���");
			// this.resultMessages =
			// "<script>parent.clearWaiting();alert(\"����ɹ���\");CloseWindow(true);</script>";
		}catch (Exception e){
			LoginDO user = getUser();
			user.setDescription("����ͨ����Ϣ");
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			this.setResultMessages("����ʧ�ܣ�");
			// this.resultMessages =
			// "<script>parent.clearWaiting();alert(\"����ʧ�ܣ�\");parent.isSubmit=false;CloseWindow(true);</script>";
		}
		return SUCCESS;
	}

	/**
	 * ����޸�����Ϣ���ݣ���ô���·���ʱ��
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
	 * <p> ��������: deleteNotice|����: ɾ��ͨ�� </p>
	 * @return
	 */
	public String deleteNotice(){
		LoginDO user = getUser();
		user.setDescription("ɾ��ͨ����Ϣ");
		try{
			String path = ServletActionContext.getRequest().getRealPath("/");
			path = path.replaceAll("\\\\", "/");
			noticeService.delete(nIds, path);
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"ɾ��", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * �ö�ͨ��
	 * @return
	 */
	public String toHeadNotice(){
		LoginDO user = getUser();
		user.setDescription("�ö�ͨ����Ϣ");
		try{
			noticeService.toHeadNotice(nIds);
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"�ö�", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"�ö�", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0020");
			sysLog.setMenuName("ͨ�����");
			this.sysLogService.saveUBaseSysLog(sysLog);
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * <p> ��������: viewNotice|����: �鿴ͨ�� </p>
	 * @return
	 */
	public String viewNotice(){
		try{
			LoginDO user = getUser(); // ����ڴ��û�
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
				// ����ͨ��鿴��־
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
	 * <p> ��������: viewFile|����: �鿴���� </p>
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
	 * <p> ��������: listLog|����: ��ѯ��ȡͨ����־�б� </p>
	 * @return ͨ����־�б�
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
	 * <p> ��������: listFeedBack|����: ��ѯ��ȡͨ�淴���б� </p>
	 * @return ͨ�淴���б�
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
				.getAttribute("LOGIN_USER"); // ����ڴ��û�
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
	 * @param nIds Ҫ���õ� nIds
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
	 * @param notice Ҫ���õ� notice
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
	 * @param feedBack Ҫ���õ� feedBack
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
	 * @param log Ҫ���õ� log
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
	 * @param feedBacks Ҫ���õ� feedBacks
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
	 * @param logs Ҫ���õ� logs
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
	 * @param affixs Ҫ���õ� affixs
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
	 * @param notices Ҫ���õ� notices
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
	 * @param types Ҫ���õ� types
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
	 * @param noticeService Ҫ���õ� noticeService
	 */
	public void setNoticeService(NoticeService noticeService){
		this.noticeService = noticeService;
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
}
