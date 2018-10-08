package fmss.action;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fmss.action.base.UserChangingService;
import fmss.common.cache.CacheManager;
import fmss.common.util.PaginationList;

public class UserSimultaneousAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PaginationList paginationList;
	private UserChangingService userChangingService;
 	private String userIdCas;
	private String userCnameCas;
	private String instIdCas;
	private String emailCas;
	private String isExistsUprr;
	private String[] chkId;
	/** ���� */
	private CacheManager cacheManager;
	
	/**
	 * ͬ���û�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String userTongBu() throws Exception {
 		try { 
 			this.userChangingService.updateUserList(chkId); 
 			setResultMessages("ͬ���û���Ϣ�ɹ�" );
 		} catch (Exception e) {
 			setResultMessages("ͬ���û���Ϣʧ��" );
			log.error("ͬ���û���Ϣ�����쳣", e);
			throw e;
		}
		return queryUser();
	}
	
	
	public String coreMethod() throws Exception {
		PrintWriter out = null;
		InputStream in = null;
 		java.io.ByteArrayOutputStream byteOut = null;
		try { 
			log.info("��ʼͬ����������...");
			String url = cacheManager.getParemerCacheMapValue("PARAM_SYS_TONG_BU_URL");
			if(null!=url&&!"".equals(url)){
				URL realUrl = new URL(url);
	  			URLConnection conn = realUrl.openConnection();
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setDoOutput(true);
				conn.setDoInput(true);
  				
				String param = this.userChangingService.sendMsgToCore();
				log.info("����ķ��ͱ���"+param);
				
				out = new PrintWriter(conn.getOutputStream());
				out.print(param);
				out.flush();
				
				log.info("����ķ��ͱ��Ľ���!!!");
				log.info("���պ��ķ��ر��Ŀ�ʼ...");
 				byteOut = new java.io.ByteArrayOutputStream();
 				
				in = conn.getInputStream();
				int b = 0;
				while((b=in.read()) != -1){
					byteOut.write(b);
				}
				
				String responseMsg = new String(byteOut.toByteArray(),"UTF-8");
				log.info("���պ��ķ��ر���:"+responseMsg);
 				this.userChangingService.receiveMsgByCore(responseMsg);
  	   			setResultMessages("���պ����û���Ϣ�ɹ�" );
 			}else{
 	  			setResultMessages("ͬ���û�URLΪ��,��ȥ�������������á�ͬ���û�URL���Ĳ���!" );
  			}
  		} catch (Exception e) {
  			if(e.getMessage().indexOf("Connection refused: connect")>-1){
  				log.error("����URL�޷�����", e);
  	 			setResultMessages("����URL�޷�����" );
  			}else{
  				log.error("���պ����û���Ϣʧ��", e);
  				setResultMessages("���պ����û���Ϣʧ��" );
  			}
  			throw e;
		}finally{
			try{
				if(out!=null){
					out.close();
				}
				if(byteOut != null){
					byteOut.close();
				}
				if (out != null){
					out.close();
				}
				if (in != null){
					in.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return queryUser();
	}
	
	
	public static void main(String[] args) {
		UserSimultaneousAction  u = new UserSimultaneousAction();
		try {
			u.coreMethod();
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}
	

	/**
	 * ��ѯ��Ҫͬ�����û��б�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUser() throws Exception {
		try {
			if (paginationList == null) {
				paginationList = new PaginationList();
			}
			Map parmsMap = new HashMap();
			if(StringUtils.isNotEmpty(userIdCas)){
				userIdCas = userIdCas.replaceAll("'", "''"); 
				parmsMap.put("userIdCas", userIdCas);
			} 
			if(StringUtils.isNotEmpty(userCnameCas)){
				userCnameCas = userCnameCas.replaceAll("'", "''"); 
				parmsMap.put("userCnameCas", userCnameCas);
			} 
   			if(StringUtils.isNotEmpty(instIdCas)){
				instIdCas = instIdCas.replaceAll("'", "''"); 
				parmsMap.put("instIdCas", instIdCas);
			} 
   			if(StringUtils.isNotEmpty(emailCas)){
   				emailCas = emailCas.replaceAll("'", "''"); 
				parmsMap.put("emailCas", emailCas);
			} 

   			if(StringUtils.isNotEmpty(isExistsUprr)){
   				isExistsUprr = isExistsUprr.replaceAll("'", "''"); 
				parmsMap.put("isExistsUprr", isExistsUprr);
			} 
			
			paginationList = this.userChangingService.getUserList(paginationList, parmsMap);
 		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	  
	
	
	
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}


	public String[] getChkId() {
		return chkId;
	}

	public void setChkId(String[] chkId) {
		this.chkId = chkId;
	}

	public String getUserIdCas() {
		return userIdCas;
	}

	public void setUserIdCas(String userIdCas) {
		this.userIdCas = userIdCas;
	}

	public String getUserCnameCas() {
		return userCnameCas;
	}

	public void setUserCnameCas(String userCnameCas) {
		this.userCnameCas = userCnameCas;
	}

	public String getInstIdCas() {
		return instIdCas;
	}

	public void setInstIdCas(String instIdCas) {
		this.instIdCas = instIdCas;
	}

	public String getEmailCas() {
		return emailCas;
	}

	public void setEmailCas(String emailCas) {
		this.emailCas = emailCas;
	}

	public String getIsExistsUprr() {
		return isExistsUprr;
	}

	public void setIsExistsUprr(String isExistsUprr) {
		this.isExistsUprr = isExistsUprr;
	}

	public UserChangingService getUserChangingService() {
		return userChangingService;
	}

	public void setUserChangingService(UserChangingService userChangingService) {
		this.userChangingService = userChangingService;
	}

	
	public PaginationList getPaginationList() {
		return paginationList;
	}

	public void setPaginationList(PaginationList paginationList) {
		this.paginationList = paginationList;
	}

}
