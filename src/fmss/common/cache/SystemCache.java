package fmss.common.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;

import com.jes.core.file.FileManagerDBImpl;

import common.crms.util.io.InputStreamProvider;
import common.crms.util.io.LocalBufferInputStreamProvider;

import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.services.DictionaryService;
import fmss.services.FunctionService;
import fmss.services.ParamConfigService;
import fmss.services.SubSystemService;
import fmss.common.util.Constants;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午05:24:28
 * @描述: [SystemCache]系统缓存
 */
public class SystemCache implements DisposableBean {

	private CacheManager cacheManager;// 缓存管理器
	private CacheabledMap baseConfigCache = new CacheabledMap(false);// 子系统缓存对象
	private CacheabledMap subSysMenuCache = new CacheabledMap(false);// 子菜单缓存对象
	private CacheabledMap paramCache = new CacheabledMap(false);// 参数缓存对象
	private ParamConfigService paramConfigService;// 参数服务
	private SubSystemService subSystemService;
	private FunctionService functionService;
	private DictionaryService dicService;
	private CacheabledMap regions = new CacheabledMap(false);
	private FileManagerDBImpl fileManager;
	public static Map byteCache=new HashMap();

	
	public void setFileManager(FileManagerDBImpl fileManager){
		this.fileManager = fileManager;
	}

	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	/**
	 * <p>方法名称: runCacheRegister|描述: 注册缓存对象 </p>
	 * @throws IOException 
	 */
	public final void runCacheRegister() throws IOException{
		registerUBaseConfig();
		reqisterSubSysMenu();
		registerParams();
		functionService.registerFuncAuth();
		registerRegions();
	}

	/**
	 * <p>方法名称: registerUBaseConfig|描述: 注册一个子系统对象</p>
	 */
	public void registerUBaseConfig(){
		// List baseConfigList = baseConfigDAO.selectAll();
		List baseConfigList = subSystemService.getAllUBaseConfig();
		baseConfigCache.put(CacheManager.U_BASE_CONFIG_LIST, baseConfigList);
		cacheManager.register(CacheManager.U_BASE_CONFIG_LIST, baseConfigCache,
				false);
	}

	/**
	 * <p>方法名称: reqisterSubSysMenu|描述:缓存各个子系统菜单 </p>
	 */
	public void reqisterSubSysMenu(){
		List baseConfigList = (List) baseConfigCache
				.get(CacheManager.U_BASE_CONFIG_LIST);
		for(Iterator it = baseConfigList.iterator(); it.hasNext();){
			UBaseConfigDO ubc = (UBaseConfigDO) it.next();
			List menuList = null;
			try{
				// menuList = sqlDAO.getSubSysMenuListByBaseConfig(ubc);
				menuList = subSystemService.getSubMenuList(ubc);
			}catch (Exception e){
				e.printStackTrace();
			}
			subSysMenuCache.put(ubc.getSystemEname(), menuList);
			cacheManager.register(CacheManager.SUB_SYS_MENU_MAP,
					subSysMenuCache, false);
		}
	}

	/**
	 * <p>方法名称: registerParams|描述:缓存参数信息 </p>
	 * @throws IOException 
	 */
	public void registerParams() throws IOException{
		Map map=paramConfigService.findWithMap();
		UBaseConfigDO baseConfig=(UBaseConfigDO) subSystemService.get(UBaseConfigDO.class, "00003");
		UBaseSysParamDO baseSysParam=new UBaseSysParamDO();
		baseSysParam.setSelectedValue(baseConfig.getLinkSiteInnerUrl());
		map.put(Constants.PARAM_USYS_INNER_URL,baseSysParam);
		paramCache.put(Constants.PAPAMETER_CACHE_MAP, map);
		byteCache.clear();
		registerLogo("LOGO1");
		registerLogo("LOGO2");
		registerLogo("LOGO3");
		registerLogo("WELCOME");
		cacheManager.register(Constants.PAPAMETER_CACHE_MAP, paramCache, false);
	}
	
	public void registerRegions(){
		List dics = dicService.getDictionaryByDicType("region".toUpperCase());
		regions.put(DictionaryService.INST_REGIONS, dics);
		cacheManager.register(DictionaryService.INST_REGIONS, regions,
					false);
	}

	public void registerLogo(String name) throws IOException{
		InputStreamProvider isp=fileManager.downLoadFile("00003",name);
		if (isp!=null){
			byteCache.put(name, isp);
		}
	}
	public void setCacheManager(CacheManager cacheManager){
		this.cacheManager = cacheManager;
	}

	public void setParamConfigService(ParamConfigService paramConfigService){
		this.paramConfigService = paramConfigService;
	}

	public void setSubSystemService(SubSystemService subSystemService){
		this.subSystemService = subSystemService;
	}

	public void setFunctionService(FunctionService functionService) {
		this.functionService = functionService;
	}

	public void destroy() throws Exception {
		cacheManager.cleanAll();
	}
}
