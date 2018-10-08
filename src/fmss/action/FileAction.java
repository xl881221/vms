package fmss.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseFileDO;
import fmss.dao.entity.UBaseFolderDO;
import fmss.dao.entity.UBaseNoticeAffixDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.cache.CacheManager;
import fmss.services.FileService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Types;
import fmss.common.util.ZipUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;


import com.jes.core.file.FileManagerDBImpl;

import common.crms.util.StringUtil;
import common.crms.util.zip.ZipEntry;
import common.crms.util.zip.ZipFile;
import common.crms.util.zip.ZipOutputStream;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: liuyi
 * @日期: 2011-7-11 下午02:16:31

 */
public class FileAction extends BaseAction{

	private boolean fixQuery;
	
	private FileService fileService;
	private FileManagerDBImpl fileManager;
	private UBaseSysLogService sysLogService;
	private ParamConfigService paramConfigService;
	private CacheManager cacheManager;
	
	private UBaseFolderDO baseFolder=new UBaseFolderDO();
	private UBaseFileDO baseFile=new UBaseFileDO();
	private String[] fileIdList;
	private String action;
	
	private File[] files;
	private List filesFileName;
	private List filesContentType;
	
	public boolean isFixQuery() {
		return fixQuery;
	}

	public void setFixQuery(boolean fixQuery) {
		this.fixQuery = fixQuery;
	}
	/**
	* <p>方法名称: loadFolderXml|描述: 异步获取文件夹</p>
	*/
	public void loadFolderXml(){
		try {
			String folderList = fileService.loadFolderXmlEx(request
					.getParameter("id"), 2);
			this.response.setContentType("text/html; charset=UTF-8");

			this.response.getWriter().print(folderList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.response.getWriter().close();
			} catch (IOException e) {
				log.error("",e);
			}
		}
	}
	/**
	* <p>方法名称: downLoad|描述: 下载文件</p>
	*/
	public void downLoadFile(){
		ServletOutputStream o = null;
		try {
			Map types = Types.MIME_TYPES;
			String type = (String) types.get("def");			
			String fileName = fileManager.getFileName(Long.valueOf(request.getParameter("fileId")).longValue());
			String pt = fileName.indexOf(".")!=-1?fileName.substring(fileName.lastIndexOf(".") + 1):"";
			if(types.containsKey(pt))
				type = (String) types.get(pt);
			response.setContentType(type);
			response.setHeader("Content-Disposition",
					"attachment; filename="
							+ new String(fileName.getBytes(
									"gbk"), "ISO8859-1"));
			o = response.getOutputStream();
			fileManager.downLoadFile(new Long(request.getParameter("fileId"))
					.longValue(), o);
		} catch (Exception e) {
			log.error("",e);
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("",e);
			}
		}
	}
	/**
	* <p>方法名称: downLoadZipFile|描述: 打包下载文件</p>
	*/
	public void downLoadZipFile(){

		ZipOutputStream zout=null;
		Map types = Types.MIME_TYPES;
		String type = (String) types.get("nar");	
		try {
			StringBuffer sb = new StringBuffer();
			List list=new ArrayList();
			sb.append("from UBaseFileDO u left join u.baseFolder ubu left join ubu.baseConfig ubi where 1=1  ");
			list.add(baseFolder.getFolderId());
			sb.append(" and exists (select 1 from UBaseFolderDO a where a.folderId=? and substring(ubu.folderPath,1,length(a.folderPath))=a.folderPath)");
			list=fileService.getBem().find(sb.toString(),list);
			if(list!=null&&list.size()>0){
				UBaseFolderDO folder = (UBaseFolderDO) fileService.get(UBaseFolderDO.class,baseFolder.getFolderId());
				String zipName=folder.getFolderName()+"["+folder.getFolderCode()+"].zip";
				response.setContentType(type);
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String((zipName).getBytes("gbk"), "ISO8859-1"));
				zout =new ZipOutputStream(response.getOutputStream());
				for(int i=0;i<list.size();i++){
					Object [] obs=(Object []) list.get(i);
					UBaseFileDO u=(UBaseFileDO) obs[0];
					ZipEntry entry = new ZipEntry(u.getFileName());
					zout.putNextEntry(entry);
					fileManager.downLoadFile(u.getFileId().longValue(),zout);
				}
				zout.flush();
			}
			
		} catch (Exception e) {
			log.error("",e);
		} finally {
			try {
				if(zout!=null)zout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("",e);;
			}
		}
	}
	/**
	* <p>方法名称: listFileMain|描述: 获取文件列表</p>
	*/
	public String listFileMain() throws Exception {
		try {
			StringBuffer sb = new StringBuffer();
			List list=new ArrayList();
			sb.append("from UBaseFileDO u left join u.baseFolder ubu left join ubu.baseConfig ubi where 1=1  ");
			if(!fixQuery){
				// 用户登陆名条件判断
				if (StringUtils.isNotEmpty(baseFolder.getFolderCode())) {
					sb.append(" and ubu.folderCode like ? ");
					list.add("%" + baseFolder.getFolderCode()
							+ "%");
				}
				if (StringUtils.isNotEmpty(baseFile.getFileName())) {
					sb.append(" and u.fileName like ? ");
					list.add("%" + baseFile.getFileName()
							+ "%");
				}
			}else{
				list.add(baseFolder.getFolderId());
				sb.append(" and exists (select 1 from UBaseFolderDO a where a.folderId=? and substring(ubu.folderPath,1,length(a.folderPath))=a.folderPath)");
			}
			sb.append(" order by ubu.folderLevel,u.fileId");
			fileService.getBem().find(sb.toString(),list, this.paginationList);
			return SUCCESS;
		} catch (Exception e) {
			//log.error("",e);
			log.error("",e);;
			throw e;
		}
	}
	/**
	* <p>方法名称: deleteFile|描述: 删除文件</p>
	*/
	public String deleteFile() throws Exception {
		try {
			//LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			for(int i=0,j=fileIdList.length;i<j;i++){
				fileManager.deleteFileById(Long.valueOf(fileIdList[i]).longValue());
			}
			this.setResultMessages("删除成功.");
			return SUCCESS;
		} catch (Exception e) {
			this.setResultMessages("删除失败.");
			//log.error("",e);
			log.error(e);
			throw e;
		}
	}
	/**
	* <p>方法名称: changeStatus|描述: 改变状态</p>
	*/
	public String changeStatus() throws Exception {
		try {
			for(int i=0,j=fileIdList.length;i<j;i++){
				fileManager.changeStatus(Long.valueOf(fileIdList[i]).longValue(), request.getParameter("status"));
			}
			return SUCCESS;
		} catch (Exception e) {
			log.error("",e);
			log.error(e);
			throw e;
		}
	}
	/**
	* <p>方法名称: upLoad|描述: 上传文件</p>
	* @return 
	*/
	public String upLoadFile() throws FileNotFoundException{
		LoginDO temp =  (LoginDO) request.getSession().getAttribute("LOGIN_USER");
		boolean isSameFileName = fileManager.isSameFileName(baseFolder.getFolderId().longValue());
		Map nameMap=new HashMap();
		if(files != null && files.length > 0){
			for(int i = 0; i < files.length; i++){
				if(((String) filesFileName.get(i) ).length()> 100){
					this.setResultMessages( "文件名过长[" + filesFileName.get(i)
							+ "]");
					return SUCCESS;
				}else if(files[i].length() > 1024*1024*10l){
					this.setResultMessages( "文件：" + filesFileName.get(i)
							+ " 大小超过10M，窗口将关闭，请重新上传！");
					return SUCCESS;
				}else if((!isSameFileName)&&fileManager.hasSameFileName(baseFolder.getFolderId().longValue(), filesFileName.get(i).toString())){
					this.setResultMessages("上传失败,此目录不能上传相同文件\n已经存在相同文件["+filesFileName.get(i)+"]");
					return SUCCESS;
				}
				if(!isSameFileName){
					if(!nameMap.containsKey(filesFileName.get(i))){
						nameMap.put( filesFileName.get(i), "");
					}else{
						this.setResultMessages("上传失败,存在相同文件:"+(String) filesFileName.get(i));
					    return SUCCESS;
					}
				}
			}
			for(int i = 0; i < files.length; i++){
				fileManager.upLoadFile(baseFolder.getFolderId().longValue(), null, filesFileName.get(i).toString(), 
						new Date(System.currentTimeMillis()),new FileInputStream(files[i]), files[i].length(),temp.getUserId(),temp.getUserCname());
			}
			
		}
		this.setResultMessages("上传成功.");
		return SUCCESS;
	}
	/**
	* <p>方法名称: upLoadZipFile|描述: 上传zip文件</p>
	* @return 
	 * @throws Exception 
	*/
	public String upLoadZipFile() throws Exception{
		LoginDO temp =  (LoginDO) request.getSession().getAttribute("LOGIN_USER");
		boolean isSameFileName = fileManager.isSameFileName(baseFolder.getFolderId().longValue());
		Map nameMap=new HashMap();
		if(files != null && files.length > 0){
			for(int i = 0; i < files.length; i++){
				if(((String) filesContentType.get(i)).indexOf("-zip-")==-1){
					this.setResultMessages("只允许上传zip文件["+(String) filesFileName.get(i)+"]");
					return SUCCESS;
				}
				ZipFile file =new ZipFile(files[i]);
				Map map = ZipUtil.getZipEntriesStreamMap(file);
				Iterator it = map.keySet().iterator();
				while(it.hasNext()){
					ZipEntry entry=(ZipEntry) it.next();
					String name=(String) map.get(entry);	
					if(name.length()> 100){
						this.setResultMessages("zip文件:"+(String) filesFileName.get(i)+"["+name+"],文件名过长");
						return SUCCESS;
					}else if(entry.getSize() > 1024*1024*10l){
						this.setResultMessages("zip文件:"+(String) filesFileName.get(i)+"["+name+"],大小超过10M，窗口将关闭，请重新上传！");
						return SUCCESS;
					}else if((!isSameFileName)&&fileManager.hasSameFileName(baseFolder.getFolderId().longValue(), name)){
						this.setResultMessages("上传失败,此目录不能上传相同文件\n已经存在相同文件:"+(String) filesFileName.get(i)+"["+name+"]");
						return SUCCESS;
					}
					if(!isSameFileName){
						if(!nameMap.containsKey(name)){
							nameMap.put(name, (String) filesFileName.get(i)+"["+name+"]");
						}else{
							this.setResultMessages("上传失败,存在相同文件:"+(String) filesFileName.get(i)+"["+name+"],"+nameMap.get(name));
						    return SUCCESS;
						}
					}
				}
			}
			for(int i = 0; i < files.length; i++){
				ZipFile file =new ZipFile(files[i]);
				Map map = ZipUtil.getZipEntriesStreamMap(file);
				Iterator it = map.keySet().iterator();
				while(it.hasNext()){
					ZipEntry entry=(ZipEntry) it.next();
					String name=(String) map.get(entry);
					fileManager.upLoadFile(baseFolder.getFolderId().longValue(), null, name, 
							new Date(System.currentTimeMillis()),file.getInputStream(entry),entry.getSize(),temp.getUserId(),temp.getUserCname());
				}
			}
		}
		this.setResultMessages("上传成功.");
		return SUCCESS;
	}
	/**
	* <p>方法名称: synchronization|描述: 把历史文件同步到数据库</p>
	* @return 
	*/
    public String synchronization(){
    	LoginDO temp =  (LoginDO) request.getSession().getAttribute("LOGIN_USER");
    	//公告 开始
    	String path = ServletActionContext.getRequest().getRealPath("/");
		path = path.replaceAll("\\\\", "/");
    	List list=fileService.getBem().find("from UBaseNoticeAffixDO");
		for(int i=0,j=list.size();i<j;i++){
			UBaseNoticeAffixDO baseNoticeAffix=(UBaseNoticeAffixDO)list.get(i);
			if(StringUtil.isNotEmpty(baseNoticeAffix.getFilePath())){
				File file = new File(path + "files/" + baseNoticeAffix.getFilePath());
				if(file.exists()){
					try {
						if(fileManager.findFilesInfo("00003", "NOTICE", baseNoticeAffix.getNoticeId().toString(),file.length(),baseNoticeAffix.getFileName()).size()==0){
							fileManager.upLoadFile("NOTICE", "00003",baseNoticeAffix.getNoticeId().toString(), baseNoticeAffix.getFileName()
									, new java.sql.Date(baseNoticeAffix.getUploadTime().getTime()),new FileInputStream(file), file.length(), null, baseNoticeAffix.getUploadUser());
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						log.error("",e);
					}
				}
			}
		}
		//公告 结束
		
		//图片开始
		list=paramConfigService.findByItem("PARAM_SYS_BIG_LOGO_PIC_NAME");
		if(list.size()!=0){
			UBaseSysParamDO u=(UBaseSysParamDO)list.get(0);
			if(StringUtils.isNotEmpty(u.getSelectedValue())){
				File file = new File(u.getSelectedValue());
				if(file.exists()){
					if(fileManager.findFilesInfo("00003", "LOGO1",file.length(),file.getName()).size()==0){
						try {
							fileManager.upLoadFile("LOGO1", "00003",null,file.getName()
									, new java.sql.Date(System.currentTimeMillis()),new FileInputStream(file), file.length(),temp.getUserId(), temp.getUserCname());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							log.error("",e);
						}
					}
				}
			}
		}
		list=paramConfigService.findByItem("PARAM_SYS_BIG_LOGO_RIGHT_NAME");
		if(list.size()!=0){
			UBaseSysParamDO u=(UBaseSysParamDO)list.get(0);
			if(StringUtils.isNotEmpty(u.getSelectedValue())){
				File file = new File(u.getSelectedValue());
				if(file.exists()){
					if(fileManager.findFilesInfo("00003", "LOGO2",file.length(),file.getName()).size()==0){
						try {
							fileManager.upLoadFile("LOGO2", "00003",null,file.getName()
									, new java.sql.Date(System.currentTimeMillis()),new FileInputStream(file), file.length(),temp.getUserId(), temp.getUserCname());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							log.error("",e);
						}
					}
				} 
			}
		}
		list=paramConfigService.findByItem("PARAM_SYS_BIG_LOGO_RIGHT_NAME");
		if(list.size()!=0){
			UBaseSysParamDO u=(UBaseSysParamDO)list.get(0);
			if(StringUtils.isNotEmpty(u.getSelectedValue())){
				File file = new File(u.getSelectedValue());
				if(file.exists()){
					if(fileManager.findFilesInfo("00003", "LOGO2",file.length(),file.getName()).size()==0){
						try {
							fileManager.upLoadFile("LOGO2", "00003",null,file.getName()
									, new java.sql.Date(System.currentTimeMillis()),new FileInputStream(file), file.length(),temp.getUserId(), temp.getUserCname());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							log.error("",e);
						}
					}
				}
			}
		}
		list=paramConfigService.findByItem("PARAM_SYS_SMALL_LOGO_PIC_NAME");
		if(list.size()!=0){
			UBaseSysParamDO u=(UBaseSysParamDO)list.get(0);
			if(StringUtils.isNotEmpty(u.getSelectedValue())){
				File file = new File(u.getSelectedValue());
				if(file.exists()){
					if(fileManager.findFilesInfo("00003", "LOGO3",file.length(),file.getName()).size()==0){
						try {
							fileManager.upLoadFile("LOGO3", "00003",null,file.getName()
									, new java.sql.Date(System.currentTimeMillis()),new FileInputStream(file), file.length(),temp.getUserId(), temp.getUserCname());
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							log.error("",e);
						}
					}
				}
			}
		}
		//图片结束
		
		//用户手册开始
		String helpPath=cacheManager.getParemerCacheMapValue("PARAM_SYS_HELPFILE_PATH");
		File file = new File(helpPath);
		if(file.exists()&&file.isDirectory()){
			File[] f = file.listFiles();
			for(int i=0,j=f.length;i<j;i++){
				List sysList=fileService.getBem().find("from UBaseConfigDO where systemNickCname=?",f[i].getName().lastIndexOf(".")!=-1?f[i].getName().substring(0,f[i].getName().lastIndexOf(".")):f[i].getName());
				if(sysList.size()>0){
					UBaseConfigDO u=(UBaseConfigDO) sysList.get(0);
					if(fileManager.findFilesInfo("00003", "HELP_"+u.getSystemId()).size()==0){
						List listFolder=fileManager.findFolder("00003", "HELP_"+u.getSystemId());
						if(listFolder.size()>0){
							Map map=(Map) listFolder.get(0);
							try {
								fileManager.upLoadFile(map.get("folderCode").toString(), "00003",null,f[i].getName()
										, new java.sql.Date(System.currentTimeMillis()),new FileInputStream(f[i]), f[i].length(),temp.getUserId(), temp.getUserCname());
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								log.error("",e);
							}
						}
					}
				}
					
				
			}
			
		}
		this.setResultMessages("同步成功.");
		//用户手册结束
		return SUCCESS;
    	
    }
	public String createFile(){
		return "formFile";
	}
	
	/**
	 * <p>
	 * 方法名称: listFile|描述: 文件列表
	 * </p>
	 * 
	 * @return 显示文件列表
	 */
	public String listFile() {
		return SUCCESS;
	}
	/**
	 * <p>
	 * 方法名称: listFileHead
	 * </p>
	 * 
	 * @return 
	 */
	public String listFileHead() {
		return SUCCESS;
	}
	/**
	 * <p>
	 * 方法名称: listFolderTree|描述: 显示文件夹
	 * </p>
	 * 
	 * @return 显示文件夹
	 */
	public String listFolderTree() {
		return SUCCESS;
	}


	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public void setFilesFileName(List filesFileName) {
		this.filesFileName = filesFileName;
	}

	public void setFilesContentType(List filesContentType) {
		this.filesContentType = filesContentType;
	}

	public UBaseFolderDO getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(UBaseFolderDO baseFolder) {
		this.baseFolder = baseFolder;
	}



	public FileService getFileService() {
		return fileService;
	}

	public File[] getFiles() {
		return files;
	}

	public List getFilesFileName() {
		return filesFileName;
	}

	public List getFilesContentType() {
		return filesContentType;
	}

	public void setFileManager(FileManagerDBImpl fileManager) {
		this.fileManager = fileManager;
	}

	public void setBaseFile(UBaseFileDO baseFile) {
		this.baseFile = baseFile;
	}

	public UBaseFileDO getBaseFile() {
		return baseFile;
	}

	public String[] getFileIdList() {
		return fileIdList;
	}

	public void setFileIdList(String[] fileIdList) {
		this.fileIdList = fileIdList;
	}

	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public void setParamConfigService(ParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
