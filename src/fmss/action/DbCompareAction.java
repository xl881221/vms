package fmss.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import fmss.services.CompareXMLService;
import fmss.services.ExportXMLService;
import fmss.common.util.DbBean;

import org.dom4j.Document;



/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: liruilin
 * @日期: 2011-01-04 
 * @描述: 数据库对比工具
 * 
 * 修改
 * 修改人:liruilin
 * 修改时间:2011-07-15
 * 描述:为了实现oracle和db2对比,不再关注具体字段类型而抽象成模型
 */
public class DbCompareAction extends BaseAction{

	
	private static final long serialVersionUID = 1L;
	/**
	 * 跳转到导出xml页面
	 * @return
	 */
	
	private String url;
	private String username;
	private String password;
	private File file;
	private String filename;
	private String resultType;
	
	private ExportXMLService exportXMLService;
	private CompareXMLService compareXMLService;
	
	public String toExportXML(){
		return SUCCESS;
	}
	/**
	 * 跳转到导入xml对比数据库页面
	 * @return
	 */
	public String toComparetXML(){
		return SUCCESS;
	}
	
	/**
	 * 导出xml
	 * @return
	 */
	public String expAction(){

		//流
		OutputStream out=null;
        
        int dbtype=0;
        String dbname=null;
        if(url==null||url.trim().length()<1||username==null||password==null){
        	request.setAttribute("errmsg", "url,username,password参数有错");
			return ERROR;
        }
		if(url.indexOf("oracle")!=-1){
			dbtype=0;
			dbname=url.substring(url.lastIndexOf(":")+1,url.length());
		}else if(url.indexOf("db2")!=-1){
			dbtype=1;
			dbname=url.substring(url.lastIndexOf("/")+1,url.length());
		}
        
        Document doc=exportXMLService.exportXml(url, username, password,dbtype,dbname);
		
        if(doc==null){
			request.setAttribute("errmsg", exportXMLService.getErrmsg());
			return ERROR;
		}
		
		try{
			
			//设置喷响应正文的MIMI类型   
	        response.setContentType("application/force-download");
	        response.setHeader("Content-Disposition", "attachment;filename=\"" +new String(dbname.getBytes("GBK"),"iso8859-1")+".xml" + "\" "); 
		
	        out=response.getOutputStream();
	        out.write(doc.asXML().getBytes("GBK"));

		}catch(IOException e){
			e.printStackTrace();
			log.info("expAction",e);
			request.setAttribute("errmsg", "生成XML文件出错");
			return ERROR;
		}finally{
			try {				
				if(out!=null){
					out.close(); 
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.info("",e);
			} 
		}
		return null;
	}
	
	/**
	 * 导入xml,对比数据库
	 * @return
	 */
	public String compareAction(){
		if(file == null ){
			request.setAttribute("errmsg", "未取到xml文件");
			compareXMLService.setErrmsg(null);
			return ERROR;	
		}
		if(file.length()>10240000l){
			request.setAttribute("errmsg", "文件大小超过10M,请上传小于10M的xml文件");
			compareXMLService.setErrmsg(null);
			return ERROR;
		}
		//解析xml得到bean
		DbBean db=compareXMLService.paresXml(file.getPath());
		if(db == null ){
			request.setAttribute("errmsg", "解析xml文件出现错误");
			compareXMLService.setErrmsg(null);
			return ERROR;	
		}
		//比对得到包含差异信息的bean
		db=compareXMLService.doCompare(url, username, password,db);
		
		if(compareXMLService.getErrmsg()!=null&&compareXMLService.getErrmsg().length()>0){
			request.setAttribute("errmsg", compareXMLService.getErrmsg());
			compareXMLService.setErrmsg(null);
			return ERROR;
		}
		
		OutputStream out=null; 
		response.setContentType("application/force-download");
	
		try{
			if("0".equals(resultType)){
				Document doc=compareXMLService.getXmlResult(db);
				response.setHeader("Content-Disposition", "attachment;filename=\"" + db.getDbname()+ "_" + db.getTarget_dbname() + "_dif.xml\""); 
				out=response.getOutputStream();
				out.write(doc.asXML().getBytes());
			}else if("1".equals(resultType)){
				String sql=compareXMLService.getSqlResult(db);			
				response.setHeader("Content-Disposition", "attachment;filename=\"" + db.getDbname() + "_" + db.getTarget_dbname()  + "_dif.sql\""); 
				out=response.getOutputStream();
				out.write(sql.getBytes("GBK"));
			}
		}catch(IOException e){
			e.printStackTrace();
			log.info("compareAction",e);
			request.setAttribute("errmsg", "生成比对文件出错");
			compareXMLService.setErrmsg(null);
			return ERROR;
		}finally{
			try {			
				if(out!=null){
					out.close(); 
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.info("compareAction",e);
			} 
		}
		compareXMLService.setErrmsg(null);
		return null;
	}
	
	
	//-------------------------------set&get--------------------------------------
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public CompareXMLService getCompareXMLService() {
		return compareXMLService;
	}
	public void setCompareXMLService(CompareXMLService compareXMLService) {
		this.compareXMLService = compareXMLService;
	}
	public ExportXMLService getExportXMLService() {
		return exportXMLService;
	}
	public void setExportXMLService(ExportXMLService exportXMLService) {
		this.exportXMLService = exportXMLService;
	}
	
}
