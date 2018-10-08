/**
 * 
 */
package fmss.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.services.SubSystemService;
import fmss.common.util.Constants;
import fmss.common.util.Types;
import fmss.common.util.ZipUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jes.core.file.FileManagerDBImpl;


public class HelpFileAction extends BaseAction {

	public static final Map msgMap = new HashMap();
	private static final int INVALID_PARAM = 1;// 无效的请求参数
	private static final int INVALID_SUBSYSTEM = 2;// 无效的子系统
	private static final int NOT_FOUND_PARAM = 3;// 参数未找到
	private static final int NOT_FOUND_FILE = 4;// 文件未找到
	private static final int FOUND_FILE = 5;// 文件找到
	static {
		msgMap.put(new Integer(INVALID_PARAM), "无效的请求参数");
		msgMap.put(new Integer(INVALID_SUBSYSTEM), "无效的子系统");
		msgMap.put(new Integer(NOT_FOUND_PARAM), "参数未找到");
		msgMap.put(new Integer(NOT_FOUND_FILE), "文件未找到");
		msgMap.put(new Integer(FOUND_FILE), "文件找到");
	}
	private static final long serialVersionUID = -6680942116420778511L;

	private CacheManager cacheManager;
	
	private FileManagerDBImpl fileManager;

	private SubSystemService subSystemService;

	private String systemId;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String down() {
		return SUCCESS;
	}

	private InputStream fileStream;

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	private StringBuffer compand(int type) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"code\":" + type + "");
		sb.append(",");
		sb.append("\"message\":\"" + msgMap.get(new Integer(type)).toString() + "\"");
		sb.append("}");
		return sb;
	}

	private StringBuffer compand(int type, String append) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"code\":" + type + "");
		sb.append(",");
		sb.append("\"message\":\"" + msgMap.get(new Integer(type)).toString() + "\\n附加信息:" + append + "\"");
		sb.append("}");
		return sb;
	}

	public void isExistFile() {
		try {
			List list=fileManager.findFilesInfo("00003","HELP_"+systemId);
			if(list.size()==0)
				super.out2page(this.compand(NOT_FOUND_FILE).toString());
			else
				super.out2page(this.compand(FOUND_FILE).toString());
		}
		catch (Exception e) {
			log.error(e);
		}
	}

	public InputStream getFileStream() throws UnsupportedEncodingException {
		Map types = Types.MIME_TYPES;
		String contentType = (String) types.get("def");
		ByteArrayOutputStream bout1=null;
		ByteArrayInputStream bin1=null;
		ByteArrayOutputStream bout2=null;
		ByteArrayInputStream bin2=null;
		try {	
			List list=fileManager.findFilesInfo("00003","HELP_"+systemId);
			if(list.size()>0){
				Map map=(Map) list.get(0);
				bout1=new ByteArrayOutputStream();
				bout2=new ByteArrayOutputStream();
				String fileName=map.get("fileName").toString();
				String zipName=(fileName.indexOf(".")!=-1?fileName.substring(0, fileName.lastIndexOf(".")):fileName)+".zip";
				fileManager.downLoadFile(Long.valueOf(map.get("fileId").toString()).longValue(),bout1);
				bin1=new ByteArrayInputStream(bout1.toByteArray());
				ZipUtil.zip(new InputStream[]{bin1},new String[]{map.get("fileName").toString()},bout2);
				response.setContentType(contentType);
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String((zipName).getBytes("gbk"), "ISO8859-1"));
				bin2=new ByteArrayInputStream(bout2.toByteArray());
				return bin2;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {if(bout1!=null)bout1.close();} catch (IOException e) {e.printStackTrace();}
			try {if(bin1!=null)bin1.close();} catch (IOException e) {e.printStackTrace();}
			try {if(bout2!=null)bout2.close();} catch (IOException e) {e.printStackTrace();}
			try {if(bin2!=null)bin2.close();} catch (IOException e) {e.printStackTrace();}
		}
		return null;
	}

	private void copyFile(String srcFile, String destFile) throws IOException {
		FileUtils.copyFile(new File(srcFile), new File(destFile));
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setSubSystemService(SubSystemService subSystemService) {
		this.subSystemService = subSystemService;
	}

	public void setFileManager(FileManagerDBImpl fileManager) {
		this.fileManager = fileManager;
	}
}
