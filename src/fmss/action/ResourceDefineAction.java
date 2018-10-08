package fmss.action;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import fmss.dao.entity.UAuthResMapDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseDictionaryDO;
import fmss.common.db.IdGenerator;
import fmss.services.DictionaryService;
import fmss.services.ResourceConfigService;
import fmss.common.util.Constants;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-27 ����02:39:09
 * @����: [ResourceDefAction]��ϵͳ��Դ���ö���Action
 */
public class ResourceDefineAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	/** uAuthResMap ��Դ��Ϣ���� */
	private UAuthResMapDO uAuthResMap = new UAuthResMapDO();
	/** authResMapList ��ѯ�б� */
	private List authResMapList;
	/** resIDs Ҫɾ������Դid���� */
	private String[] resIDs;
	/** subSystemList ��ϵͳ�б� */
	private List subSystemList;
	/** uauthResMapService */
	private ResourceConfigService resourceConfigService;
	/** dicService �ֵ������ */
	private DictionaryService dicService;
	/** resTypeDic ��Դ�����ֵ� */
	private List resTypeDic;
	/** idGenerator id������ */
	private static IdGenerator  idGenerator; // id������
	/** resContent ��Դ���� */
	private List resContent;
	/** uBaseConfig ��ϵͳ��Ϣ */
	private UBaseConfigDO uBaseConfig = new UBaseConfigDO();
	/** isSuccess �������±�־λ */
	private String isSuccess;


	/**
	 * <p>��������: listResource|����:��ѯ </p>
	 * @return �ɹ�ҳ��
	 */
	public String listResource(){
		try{
			// ���Ĭ�ϲ�ѯ����
			UBaseConfigDO ubc = new UBaseConfigDO();
			ubc.setSystemId("");
			ubc.setSystemCname("ȫ��");
			this.subSystemList = new ArrayList();
			this.subSystemList.add(ubc);
			// �����ϵͳ�����б�
			this.subSystemList.addAll(this.resourceConfigService
					.getSubSystemList(true));
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "ȫ��", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// �����Դ�б�
			this.authResMapList = this.resourceConfigService
					.queryAuthResMap(this.uAuthResMap);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: deleteResource|����:ɾ����Դ���� </p>
	 * @return �ɹ�ҳ��
	 */
	public String deleteResource(){
		try{
			// ����id����ɾ��
			this.resourceConfigService.deletes(this.resIDs);
			return this.listResource();
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: createResource|����:������Դ </p>
	 * @return �ɹ�ҳ��
	 */
	public String createResource(){
		try{
			// �����ϵͳ�����б�
			this.subSystemList = this.resourceConfigService
					.getSubSystemList(true);
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>��������: saveResource|����: ������Դ</p>
	 * @return �ɹ�ҳ��
	 */
	public String saveResource(){
		try{
			// �ж��Ƿ�����Դid��û��������
			if(StringUtils.isBlank(this.uAuthResMap.getResId())){
				// ��ʼ��id������
				idGenerator = IdGenerator.getInstance(DictionaryService.RES_AOT_DIC_TYPE);
				long id = idGenerator.getNextKey();
				this.uAuthResMap.setResId(String.valueOf(id));
				// ������Դ��Ϣ
				this.resourceConfigService.saveResMap(this.uAuthResMap);
			}else{
				// ������Դ
				this.resourceConfigService.updateResMap(this.uAuthResMap);
			}
			this.editResource();
			this.setResultMessages("����ɹ�");
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			this.setResultMessages("����ʧ��");
		}
		return ERROR;
	}

	/**
	 * <p>��������: editResource|����:��ñ༭����</p>
	 * @return �ɹ�ҳ��
	 */
	public String editResource(){
		try{
			// �����ϵͳ�����б�
			this.subSystemList = this.resourceConfigService
					.getSubSystemList(true);
			// ȡ��Դ�����ֵ�
			this.resTypeDic = this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE);
			this.uAuthResMap = this.resourceConfigService.get(this.uAuthResMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * <p>��������: viewResourceContent|����:�鿴��Դ����</p>
	 * @return �ɹ�ҳ��
	 */
	public String viewResourceContent(){
		try{
			String type=null;
			// ��ȡ��Դ��Ϣ
			this.uAuthResMap = this.resourceConfigService.get(this.uAuthResMap);
			// ��ȡ��ϵͳ��Ϣ
			this.uBaseConfig = (UBaseConfigDO) this.resourceConfigService.get(
					UBaseConfigDO.class, this.uAuthResMap.getSystemId());
			// ���ȡϵͳ�˵��ͻ���-��ϵͳ������Դ������Դid����Դ�������ֶ����̶�������ָ����λ��ȡ
			if(Constants.PUB_RESOURCE_DIC_TYPE.equals(this.uAuthResMap
					.getResType())){
				// ���ȡ�˵��б�
				if(Constants.SYSTEM_MENU_TABLE.equalsIgnoreCase(uAuthResMap
						.getSrcTable())){
					this.resContent = this.resourceConfigService
							.getPubMenuCtn();
					type = Constants.SYSTEM_MENU_TABLE;
				}
				// ���ȡ�����б�
				else if(Constants.SYSTEM_INST_TABLE.equalsIgnoreCase(uAuthResMap
						.getSrcTable())){
					this.resContent = this.resourceConfigService
							.getPubInstCtn();
					type = Constants.SYSTEM_INST_TABLE;
				}
				else{
					//ȡ�������е���Դ
					this.resContent = this.resourceConfigService.getResContent(
							this.uAuthResMap, this.uBaseConfig);
					type = Constants.PUB_RESOURCE_DIC_TYPE;
				}
			}else{
				String defaultIP = request.getSession().getAttribute("defaultIPAddress").toString();
				String defaultPort = request.getSession().getAttribute("defaultPort").toString();
				String sDbUrl = this.uBaseConfig.getDbUrl();

				if (sDbUrl != null && sDbUrl.indexOf(Constants.DEFAULT_URL_IP) != -1) {
					sDbUrl = sDbUrl.replaceFirst(Constants.DEFAULT_URL_IP_UNICODE, defaultIP);
				}

				if (sDbUrl != null && sDbUrl.indexOf(Constants.DEFAULT_URL_PORT) != -1) {
					sDbUrl = sDbUrl.replaceFirst(Constants.DEFAULT_URL_PORT_UNICODE, defaultPort);
				}
				this.uBaseConfig.setDbUrl(sDbUrl);
				// ��ȡ����˽����Դ���ݽ��
				this.resContent = this.resourceConfigService.getResContent(
						this.uAuthResMap, this.uBaseConfig);
				type = Constants.PRI_RESOURCE_DIC_TYPE;
			}
			this.request.setAttribute("uAuthResMap", this.uAuthResMap);
			this.request.setAttribute("resContent", this.resContent);
			this.request.setAttribute("type", type);
		}catch (Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * @return ��Դ����
	 */
	public UAuthResMapDO getUAuthResMap(){
		return uAuthResMap;
	}

	/**
	 * @param authResMap Ҫ���õ� ��Դ����
	 */
	public void setUAuthResMap(UAuthResMapDO authResMap){
		uAuthResMap = authResMap;
	}

	/**
	 * @return ��Դ�б�
	 */
	public List getAuthResMapList(){
		return authResMapList;
	}

	/**
	 * @param authResMapList Ҫ���õ� ��Դ�б�
	 */
	public void setAuthResMapList(List authResMapList){
		this.authResMapList = authResMapList;
	}

	/**
	 * @return ��Դid����
	 */
	public String[] getResIDs(){
		return resIDs;
	}

	/**
	 * @param resIDs Ҫ���õ� ��Դid����
	 */
	public void setResIDs(String[] resIDs){
		this.resIDs = resIDs;
	}

	/**
	 * @return ��ϵͳ�б�
	 */
	public List getSubSystemList(){
		return subSystemList;
	}

	/**
	 * @param subSystemList Ҫ���õ� ��ϵͳ�б�
	 */
	public void setSubSystemList(List subSystemList){
		this.subSystemList = subSystemList;
	}

	/**
	 * @return ��Դ���÷������
	 */
	public ResourceConfigService getResourceConfigService(){
		return resourceConfigService;
	}

	/**
	 * @param resourceConfigService Ҫ���õ� ��Դ���÷������
	 */
	public void setResourceConfigService(
			ResourceConfigService resourceConfigService){
		this.resourceConfigService = resourceConfigService;
	}

	/**
	 * @return �ֵ�������
	 */
	public DictionaryService getDicService(){
		return dicService;
	}

	/**
	 * @param dicService Ҫ���õ� �ֵ�������
	 */
	public void setDicService(DictionaryService dicService){
		this.dicService = dicService;
	}

	/**
	 * @return ��Դ�����ֵ�
	 */
	public List getResTypeDic(){
		return resTypeDic;
	}

	/**
	 * @param resTypeDic Ҫ���õ� ��Դ�����ֵ�
	 */
	public void setResTypeDic(List resTypeDic){
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return ��Դʵ������
	 */
	public List getResContent(){
		return resContent;
	}

	/**
	 * @param resContent Ҫ���õ� ��Դʵ������
	 */
	public void setResContent(List resContent){
		this.resContent = resContent;
	}

	/**
	 * @return ��ϵͳ����
	 */
	public UBaseConfigDO getUBaseConfig(){
		return uBaseConfig;
	}

	/**
	 * @param baseConfig Ҫ���õ� ��ϵͳ����
	 */
	public void setUBaseConfig(UBaseConfigDO baseConfig){
		uBaseConfig = baseConfig;
	}

	/**
	 * @return �Ƿ�ɹ���ʾ
	 */
	public String getIsSuccess(){
		return isSuccess;
	}

	/**
	 * @param isSuccess Ҫ���õ� �Ƿ�ɹ���ʾ
	 */
	public void setIsSuccess(String isSuccess){
		this.isSuccess = isSuccess;
	}


}
