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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-27 下午02:39:09
 * @描述: [ResourceDefAction]子系统资源配置定义Action
 */
public class ResourceDefineAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	/** uAuthResMap 资源信息对象 */
	private UAuthResMapDO uAuthResMap = new UAuthResMapDO();
	/** authResMapList 查询列表 */
	private List authResMapList;
	/** resIDs 要删除的资源id数组 */
	private String[] resIDs;
	/** subSystemList 子系统列表 */
	private List subSystemList;
	/** uauthResMapService */
	private ResourceConfigService resourceConfigService;
	/** dicService 字典服务类 */
	private DictionaryService dicService;
	/** resTypeDic 资源类型字典 */
	private List resTypeDic;
	/** idGenerator id生成器 */
	private static IdGenerator  idGenerator; // id生成器
	/** resContent 资源内容 */
	private List resContent;
	/** uBaseConfig 子系统信息 */
	private UBaseConfigDO uBaseConfig = new UBaseConfigDO();
	/** isSuccess 新增更新标志位 */
	private String isSuccess;


	/**
	 * <p>方法名称: listResource|描述:查询 </p>
	 * @return 成功页面
	 */
	public String listResource(){
		try{
			// 添加默认查询条件
			UBaseConfigDO ubc = new UBaseConfigDO();
			ubc.setSystemId("");
			ubc.setSystemCname("全部");
			this.subSystemList = new ArrayList();
			this.subSystemList.add(ubc);
			// 获得子系统下拉列表
			this.subSystemList.addAll(this.resourceConfigService
					.getSubSystemList(true));
			// 取资源类型字典
			this.resTypeDic = this.dicService.addDefault(this.resTypeDic,
					new UBaseDictionaryDO(DictionaryService.RESOURCE_DIC_TYPE,
							"", "全部", ""));
			this.resTypeDic.addAll(this.dicService.find(
					DictionaryService.QUERY_STRING,
					DictionaryService.RESOURCE_DIC_TYPE));
			// 获得资源列表
			this.authResMapList = this.resourceConfigService
					.queryAuthResMap(this.uAuthResMap);
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: deleteResource|描述:删除资源配置 </p>
	 * @return 成功页面
	 */
	public String deleteResource(){
		try{
			// 根据id批量删除
			this.resourceConfigService.deletes(this.resIDs);
			return this.listResource();
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: createResource|描述:新增资源 </p>
	 * @return 成功页面
	 */
	public String createResource(){
		try{
			// 获得子系统下拉列表
			this.subSystemList = this.resourceConfigService
					.getSubSystemList(true);
			// 取资源类型字典
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
	 * <p>方法名称: saveResource|描述: 保存资源</p>
	 * @return 成功页面
	 */
	public String saveResource(){
		try{
			// 判断是否有资源id，没有则新增
			if(StringUtils.isBlank(this.uAuthResMap.getResId())){
				// 初始化id生成器
				idGenerator = IdGenerator.getInstance(DictionaryService.RES_AOT_DIC_TYPE);
				long id = idGenerator.getNextKey();
				this.uAuthResMap.setResId(String.valueOf(id));
				// 保存资源信息
				this.resourceConfigService.saveResMap(this.uAuthResMap);
			}else{
				// 保存资源
				this.resourceConfigService.updateResMap(this.uAuthResMap);
			}
			this.editResource();
			this.setResultMessages("保存成功");
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
			this.setResultMessages("保存失败");
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: editResource|描述:获得编辑内容</p>
	 * @return 成功页面
	 */
	public String editResource(){
		try{
			// 获得子系统下拉列表
			this.subSystemList = this.resourceConfigService
					.getSubSystemList(true);
			// 取资源类型字典
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
	 * <p>方法名称: viewResourceContent|描述:查看资源内容</p>
	 * @return 成功页面
	 */
	public String viewResourceContent(){
		try{
			String type=null;
			// 获取资源信息
			this.uAuthResMap = this.resourceConfigService.get(this.uAuthResMap);
			// 获取子系统信息
			this.uBaseConfig = (UBaseConfigDO) this.resourceConfigService.get(
					UBaseConfigDO.class, this.uAuthResMap.getSystemId());
			// 如果取系统菜单和机构-即系统公共资源，则资源id，资源表名，字段名固定，将从指定的位置取
			if(Constants.PUB_RESOURCE_DIC_TYPE.equals(this.uAuthResMap
					.getResType())){
				// 如果取菜单列表
				if(Constants.SYSTEM_MENU_TABLE.equalsIgnoreCase(uAuthResMap
						.getSrcTable())){
					this.resContent = this.resourceConfigService
							.getPubMenuCtn();
					type = Constants.SYSTEM_MENU_TABLE;
				}
				// 如果取机构列表
				else if(Constants.SYSTEM_INST_TABLE.equalsIgnoreCase(uAuthResMap
						.getSrcTable())){
					this.resContent = this.resourceConfigService
							.getPubInstCtn();
					type = Constants.SYSTEM_INST_TABLE;
				}
				else{
					//取其他公有的资源
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
				// 获取其他私有资源内容结果
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
	 * @return 资源对象
	 */
	public UAuthResMapDO getUAuthResMap(){
		return uAuthResMap;
	}

	/**
	 * @param authResMap 要设置的 资源对象
	 */
	public void setUAuthResMap(UAuthResMapDO authResMap){
		uAuthResMap = authResMap;
	}

	/**
	 * @return 资源列表
	 */
	public List getAuthResMapList(){
		return authResMapList;
	}

	/**
	 * @param authResMapList 要设置的 资源列表
	 */
	public void setAuthResMapList(List authResMapList){
		this.authResMapList = authResMapList;
	}

	/**
	 * @return 资源id数组
	 */
	public String[] getResIDs(){
		return resIDs;
	}

	/**
	 * @param resIDs 要设置的 资源id数组
	 */
	public void setResIDs(String[] resIDs){
		this.resIDs = resIDs;
	}

	/**
	 * @return 子系统列表
	 */
	public List getSubSystemList(){
		return subSystemList;
	}

	/**
	 * @param subSystemList 要设置的 子系统列表
	 */
	public void setSubSystemList(List subSystemList){
		this.subSystemList = subSystemList;
	}

	/**
	 * @return 资源配置服务对象
	 */
	public ResourceConfigService getResourceConfigService(){
		return resourceConfigService;
	}

	/**
	 * @param resourceConfigService 要设置的 资源配置服务对象
	 */
	public void setResourceConfigService(
			ResourceConfigService resourceConfigService){
		this.resourceConfigService = resourceConfigService;
	}

	/**
	 * @return 字典服务对象
	 */
	public DictionaryService getDicService(){
		return dicService;
	}

	/**
	 * @param dicService 要设置的 字典服务对象
	 */
	public void setDicService(DictionaryService dicService){
		this.dicService = dicService;
	}

	/**
	 * @return 资源类型字典
	 */
	public List getResTypeDic(){
		return resTypeDic;
	}

	/**
	 * @param resTypeDic 要设置的 资源类型字典
	 */
	public void setResTypeDic(List resTypeDic){
		this.resTypeDic = resTypeDic;
	}

	/**
	 * @return 资源实际内容
	 */
	public List getResContent(){
		return resContent;
	}

	/**
	 * @param resContent 要设置的 资源实际内容
	 */
	public void setResContent(List resContent){
		this.resContent = resContent;
	}

	/**
	 * @return 子系统对象
	 */
	public UBaseConfigDO getUBaseConfig(){
		return uBaseConfig;
	}

	/**
	 * @param baseConfig 要设置的 子系统对象
	 */
	public void setUBaseConfig(UBaseConfigDO baseConfig){
		uBaseConfig = baseConfig;
	}

	/**
	 * @return 是否成功标示
	 */
	public String getIsSuccess(){
		return isSuccess;
	}

	/**
	 * @param isSuccess 要设置的 是否成功标示
	 */
	public void setIsSuccess(String isSuccess){
		this.isSuccess = isSuccess;
	}


}
