package fmss.services.ldap;

import java.net.ConnectException;
import java.util.Properties;

import javax.naming.directory.Attributes;

import fmss.services.ILdapService;

import org.apache.log4j.Logger;


import com.bizwave.ldap.LdapConnection;
import com.bizwave.ldap.LdapDN;

public class LdapAccessServiceByApiGK extends LdapAccessConnection implements ILdapService{
	
	private Logger log = Logger.getLogger(LdapAccessServiceByApiGK.class);
	
	private String rootDN;
	private String sidDN;
	private String sidPass;
	private String ip;
	private int port;

	//***************************method******************************************
	public LdapAccessServiceByApiGK(String propertyFilePath)
	{
		Properties p=loadProp(propertyFilePath);
		setProperty(p);
	}
	public LdapAccessServiceByApiGK(Properties properties)
	{		
		setProperty(properties);
	}
	private void setProperty(Properties p) {
		this.setIp(p.getProperty("ip").trim());
		this.setPort(Integer.parseInt(p.getProperty("port").trim()));
		this.setRootDN(p.getProperty("rootDN").trim());
		this.setSidDN(p.getProperty("sidDN"));
		this.setSidPass(p.getProperty("sidPass"));
	}
	
	/**
	 * ͨ��AD��֤����û���
	 * @param userId
	 * @return
	 */
	public String getUserNameFormAD(String userId)
	{
		LdapConnection ldapCon=new LdapConnection(getIp(), getPort());
		String userName="";
		try{
			ldapCon.connect(getSidDN(),getSidPass());
			LdapDN[] ldapList = ldapCon.searchDN(getRootDN(), getUserIdFilter(userId) , 2);
			if(ldapList.length==1)
			{
				Attributes attr = ldapCon.getAttributes(ldapList[0].getDN(),new String[]{"sn","givenName"});
				userName= (String)attr.get("sn").get(0);
			}
			else if(ldapList.length==0)
				throw new Exception("�û�������");
			else 
				throw new Exception("�û�id��Ψһ");
		}
		catch (Exception e){
			log.error("��ȡ�û����쳣",e);
			return userName;
		}
		finally{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
		return userName;
	}
	
	/**
	 * ���ɸѡ��ʽ
	 * @param userId
	 * @return
	 */
	protected String getUserIdFilter(String userId) {
		return "(&(objectclass=user)(cn=" + userId + "))";
	}

	/**
	 * ��֤�û��Ƿ����
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean isUserExists(String userId) throws Exception
	{
		boolean exists=false;
		LdapConnection ldapCon=new LdapConnection(getIp(), getPort());
		try{
			ldapCon.connect(getSidDN(),getSidPass());
			LdapDN[] ldapList = ldapCon.searchDN(getRootDN(), getUserIdFilter(userId) , 2);
			exists = ldapList.length>0;
			return exists;
		}
		catch (Exception e){
			log.error("��ȡ�û������쳣",e);
			throw e;
		}
		finally{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
	}
	
	/**
	 * ����û�DN
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getUserDN(String userId) throws Exception
	{
		LdapConnection ldapCon=new LdapConnection(getIp(), getPort());
		try{
			ldapCon.connect(getSidDN(),getSidPass());
			LdapDN[] ldapList = ldapCon.searchDN(getRootDN(), getUserIdFilter(userId) , 2);
			String dn = ldapList[0].getDN();
			if(ldapList.length==1)
				return dn;
			else if(ldapList.length==0)
				throw new Exception("�û�������");
			else 
				throw new Exception("�û�id��Ψһ");
		}
		catch (Exception e){
			log.error("��ȡ�û������쳣",e);
			throw e;
		}
		finally{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
	}

	/**
	 * ����û������Ƿ�У��ͨ��
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean checkUserPass(String user, String password)
	{
		boolean checkPass=false;
		LdapConnection ldapCon=new LdapConnection(getIp(), getPort());
		try{
			ldapCon.connect(getUserDN(user),password);
			LdapDN[] ldapList = ldapCon.searchDN(getRootDN(), getUserIdFilter(user) , 2);
			checkPass=ldapList.length>0;
			return checkPass;
		}
		catch (Exception e){
			log.error("�û�У���쳣",e);
			return checkPass;
		}
		finally{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
	}
	
	/**
	 * ����û������Ƿ�У��ͨ��������У����Ϣ
	 * @param user
	 * @param password
	 * @return
	 */
	public String checkUserPassWithInfo(String user, String password)
	{
		LdapConnection ldapCon=new LdapConnection(getIp(), getPort());
		String userDN = null;
		try {
			//****ServiceID�����Ƿ�����****************************
			ldapCon.connect(getSidDN(), getSidPass());
			log.info("Service ID��" + getSidDN() + " ���ӳɹ�");
			
			//****��֤�û��Ƿ����**********************************
			String filter = "(&(objectclass=user)(cn=" + user + "))";
			int scope = 2;
			LdapDN[] DNList = ldapCon.searchDN(getRootDN(), filter, scope);
			if(DNList.length == 1)
	        {
	            userDN = DNList[0].getDN();
	        } 
			else if(DNList.length == 0)
				return "�û�������";
		    else
		    	return "�û�ID��Ψһ";
			
		}
		catch(ConnectException e)
		{
			log.error("�û���֤�쳣", e);
			return "�����������쳣";
		}
		catch (Exception e) {
			log.error("�û���֤�쳣", e);
			return "�����������쳣";
		}
		finally
		{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
		
		//****��֤�û�������*****************************************
		try {
			ldapCon.connect(userDN, password);
			log.info("User��" + user + " ������֤�ɹ�");
		} 
		catch (Exception e) {
			String msg = "User��" + user + "������֤ʧ��[" + e.getMessage() + "]";
			log.error(msg, e);
			return "�û������������!";
		} 
		finally
		{
			if(ldapCon.isConnect()) ldapCon.disconnect();
		}
		
		log.info("user:" + user + " LDAP ceritificate finished . ");
		return "ok";
	}
	public String auth(String user, String password) 
	{
		return checkUserPassWithInfo(user, password);
	}

	//**************************get set**********************************************
	public String getRootDN() {
		return rootDN;
	}


	public void setRootDN(String rootDN) {
		this.rootDN = rootDN;
	}


	public String getSidDN() {
		return sidDN;
	}


	public void setSidDN(String sidDN) {
		this.sidDN = sidDN;
	}


	public String getSidPass() {
		return sidPass;
	}


	public void setSidPass(String sidPass) {
		this.sidPass = sidPass;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}
}
