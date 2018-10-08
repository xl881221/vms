package fmss.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

import com.jes.core.file.FileManagerDBImpl;

import common.crms.util.io.InputStreamProvider;

import fmss.action.base.AuditBase;
import fmss.action.entity.UBaseSysParamChangeDO;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.db.IdGenerator;
import fmss.common.cache.CacheManager;
import fmss.common.cache.SystemCache;
import fmss.common.config.LoggingConfig;
import fmss.services.ParamAlteration;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-7-10 ����02:16:50
 * @����: [ParameterConfigAction]��������action
 */
public class ParamConfigAction extends BaseAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** ��������service */
	private ParamConfigService paramConfigService;
	private FileManagerDBImpl fileManager;
	/** sysLogService ��־���� */
	private UBaseSysLogService sysLogService;
	/** ҳ��html */
	private String innerHtml;
	/** ѡ�е�tabҳ */
	private String selectTab;
	/** ����map,keyΪ��ϵͳ�� */
	private Map paramMaps;
	/** ����Ĭ��ͼƬ·�� */
	private String defaultPath;
	/** ����ͼƬ������ */
	private String pramName;

	/**
	 * @return paramMaps
	 */
	public Map getParamMaps(){
		return paramMaps;
	}

	private CacheManager cacheManager; // ����

	public CacheManager getCacheManager(){
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	/**
	 * @param paramMaps Ҫ���õ� paramMaps
	 */
	public void setParamMaps(Map paramMaps){
		this.paramMaps = paramMaps;
	}

	/**
	 * @return innerHtml
	 */
	public String getInnerHtml(){
		return this.innerHtml;
	}

	/**
	 * @param innerHtml Ҫ���õ� innerHtml
	 */
	public void setInnerHtml(String innerHtml){
		this.innerHtml = innerHtml;
	}

	public void setDefaultPath(String defaultPath){
		this.defaultPath = defaultPath;
	}

	/**
	 * @param pramName the pramName to set
	 */
	public void setPramName(String pramName){
		this.pramName = pramName;
	}

	/**
	 * <p>��������: saveParamConfig|����: �����������</p>
	 * @return
	 */
	public String saveParamConfig(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		user.setDescription("�����������");
		try{
			if("1"
					.equals(CacheManager
							.getParemerCacheMapValue(Constants.PARAM_SYS_USER_CHANGE_AUDIT))
					&& "1"
							.equals(CacheManager
									.getParemerCacheMapValue(Constants.PARAM_SYS_PARAM_CONFIG_AUDIT))){
				List paramList = new ArrayList();
				Map reqMap = this.request.getParameterMap();
				Set reqKey = reqMap.keySet();
				boolean modifiedBefore = false;
				for(Iterator iterator = reqKey.iterator(); iterator.hasNext();){
					String key = (String) iterator.next();
					// ������������param_��ͷ
					if(key.startsWith("param_")){
						UBaseSysParamChangeDO param = new UBaseSysParamChangeDO();
						param.setParamId(new Integer(Integer.parseInt(key
								.replaceAll("param_", ""))));
						String[] reqParam = (String[]) reqMap.get(key);
						if(reqParam != null && reqParam.length == 1){
							if(reqParam[0].trim().getBytes().length > 200){
								this.innerHtml = "cannot";
								return "isOvered";
							}
							// �����жϿ��Ƿ����޸Ĺ���param_id ����޸Ĺ����򱣴浽��ʱ����ȥ
							if(paramConfigService.isChanged(param.getParamId(),
									reqParam[0].trim())){
								if(paramConfigService.modifiedSysSystem(String
										.valueOf(param.getParamId()))){
									modifiedBefore = true;
								}
								param.setSelectedValue(reqParam[0].trim());
								IdGenerator idGenerator = IdGenerator
										.getInstance(AuditBase.AUDIT);
								long id = idGenerator.getNextKey();
								param.setId(new Long(id));
								param
										.setChangeStatus(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY);
								param.setAuditStatus(new Long(
										AuditBase.AUDIT_STATUS_NOADUITED));
								param.setChangeUser(user.getUserId());
								param.setChangeTime(new java.sql.Timestamp(
										System.currentTimeMillis()));
								paramList.add(param);
							}
						}
					}
				}
				// ������־��Ϣ
				// if (LoggingConfig.isLoggingParamChanges()) {
				// new ParamAlteration(this.sysLogService,
				// this.paramConfigService, paramList, user).afterAlteration();
				// } else {
				if(paramList.size() == 0){
					this.innerHtml = "noChanges";
				}else{
					if(modifiedBefore){
						this.innerHtml = "saveFaliures";
					}else{
						paramConfigService.saveParamChangeConfig(paramList);
						this.innerHtml = "saveSuccess";
					}
				}
				// UBaseSysLogDO sysLog =
				// this.sysLogService.setUBaseSysLog(user,
				// "����","1",Constants.BASE_SYS_LOG_AUTHORITY);
				// sysLog.setMenuId("0003.0003");
				// sysLog.setMenuName("ϵͳ��������.��������");
				// this.sysLogService.saveUBaseSysLog(sysLog);
				// }
				return SUCCESS;
			}else{
				List paramList = new ArrayList();
				Map reqMap = this.request.getParameterMap();
				Set reqKey = reqMap.keySet();
				for(Iterator iterator = reqKey.iterator(); iterator.hasNext();){
					String key = (String) iterator.next();
					// ������������param_��ͷ
					if(key.startsWith("param_")){
						UBaseSysParamDO param = new UBaseSysParamDO();
						param.setParamId(new Integer(Integer.parseInt(key
								.replaceAll("param_", ""))));
						String[] reqParam = (String[]) reqMap.get(key);
						if(reqParam != null && reqParam.length == 1){
							if(reqParam[0].trim().getBytes().length > 200){
								this.innerHtml = "cannot";
								return "isOvered";
							}
							param.setSelectedValue(reqParam[0].trim());
							paramList.add(param);
						}
					}
				}
				if(LoggingConfig.isLoggingParamChanges()){
					new ParamAlteration(this.sysLogService,
							this.paramConfigService, paramList, user)
							.afterAlteration();
				}else{
					paramConfigService.saveParamConfig(paramList);
					UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(
							user, "����", "1", Constants.BASE_SYS_LOG_AUTHORITY);
					sysLog.setMenuId("0003.0003");
					sysLog.setMenuName("ϵͳ��������.��������");
					this.sysLogService.saveUBaseSysLog(sysLog);
				}
				this.innerHtml = "success";
				return SUCCESS;
			}
		}catch (Exception e){
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,
					"����", "0", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0003.0003");
			sysLog.setMenuName("ϵͳ��������.��������");
			this.sysLogService.saveUBaseSysLog(sysLog);
			this.innerHtml = "fail";
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: listParamConfig|����:�����б� </p>
	 * @return
	 */
	public String listParamConfig(){
		try{
			List systemIds = this.paramConfigService.findSystemId();
			List configs = new ArrayList();
			for(Iterator iterator = systemIds.iterator(); iterator.hasNext();){
				String systemId = (String) iterator.next();
				configs.add(this.paramConfigService.findSystemById(systemId));
			}
			this.paramMaps = this.paramConfigService.getParamsMap(configs);
			this.request.setAttribute("paramMaps", paramMaps);
			// ȡĬ��ѡ����
			if(StringUtils.isEmpty(this.selectTab)){
				this.selectTab = "00003";
			}
			return SUCCESS;
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: listParamConfig|����:�����б� </p>
	 * @return
	 */
	public String listParamOneConfig(){
		try{
			this.paramMaps = this.paramConfigService.getParamsOneMap("00003");
			// ȡĬ��ѡ����
			if(StringUtils.isEmpty(this.selectTab)){
				Set keys = this.paramMaps.keySet();
				for(Iterator iterator = keys.iterator(); iterator.hasNext();){
					String key = (String) iterator.next();
					this.selectTab = key;
					break;
				}
			}
			return SUCCESS;
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: listParamChangeConfig|����:�����б� </p>
	 * @return
	 */
	public String listParamChangeConfig(){
		try{
			List systemIds = this.paramConfigService.findSystemId();
			List configs = new ArrayList();
			for(Iterator iterator = systemIds.iterator(); iterator.hasNext();){
				String systemId = (String) iterator.next();
				configs.add(this.paramConfigService
						.findSystemChangeById(systemId));
			}
			this.paramMaps = this.paramConfigService.getParamsMap(configs);
			// ȡĬ��ѡ����
			if(StringUtils.isEmpty(this.selectTab)){
				this.selectTab = "00003";
			}
			return SUCCESS;
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ERROR;
	}

	/** ��ҳIO�����ͼƬ */
	public void outLogo(){
		doOutPrint(pramName, response, defaultPath);
	}

	public void doOutPrint(String pramName, HttpServletResponse response,
			String dafaultPath){
		OutputStream output = null;
		try{
			output = response.getOutputStream();
			InputStreamProvider isp = (InputStreamProvider) SystemCache.byteCache
					.get(pramName);
			if(isp == null){
				String type = defaultPath.substring(dafaultPath
						.lastIndexOf(".") + 1, dafaultPath.length());
				response.setContentType("image/" + type);
				String realPath = request.getRealPath("/") + "//";
				String url = request.getRequestURL().substring(
						0,
						request.getRequestURL().indexOf(
								request.getRequestURI().substring(
										request.getContextPath().length())));
				// ���httpsЭ��
				if(dafaultPath.startsWith("https")){
					dafaultPath = realPath
							+ dafaultPath.replaceFirst(":443/", "/").substring(
									url.replaceFirst(":443/", "/").length());
				}else{
					dafaultPath = realPath
							+ dafaultPath.replaceFirst(":80/", "/").substring(
									url.replaceFirst(":80/", "/").length());
				}
				dafaultPath = dafaultPath.replace('\\', '/').replaceAll("//",
						"/");
				InputStream in = new FileInputStream(new File(dafaultPath));
				org.apache.commons.io.IOUtils.copy(in, output);
				in.close();
			}else{
				InputStream in = isp.getInputStream();
				org.apache.commons.io.IOUtils.copy(in, output);
				in.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{
				output.flush();
			}catch (IOException e){
			}
			try{
				output.close();
			}catch (IOException e){
			}
		}
	}

	/**
	 * @param paramConfigService Ҫ���õ� paramConfigService
	 */
	public void setParamConfigService(ParamConfigService paramConfigService){
		this.paramConfigService = paramConfigService;
	}

	/**
	 * @return selectTab
	 */
	public String getSelectTab(){
		return selectTab;
	}

	/**
	 * @param selectTab Ҫ���õ� selectTab
	 */
	public void setSelectTab(String selectTab){
		this.selectTab = selectTab;
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

	public void setFileManager(FileManagerDBImpl fileManager){
		this.fileManager = fileManager;
	}
}
