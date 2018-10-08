package fmss.common.util;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import fmss.services.ParamConfigService;


/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: wangxin
 * @����: 2009-9-2 ����10:13:00
 * @����: [LdapUtil] LDAP����
 */
public class LdapUtil {
	
	/** ����·�� */
	private String URL = "";//"ldap://ssuzdc3:389/";
	
	/** AD������·�� */
	private String BASEDN = "";////"OU=Local Profile,OU=Users,OU=Suzhou,DC=xxxx,DC=com";
	private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	/** ����Ա�û��� */
	private String UID = "";
	/** ����Ա���� */
	private String PWD = "";
	/** �������� */
	private String filter="";
	/** ����*/
	private String domainName="";
	
	//private String 
	private LdapContext ctx = null;
	
	/** �洢���� */
	private Hashtable env = null;
	
	/** δ֪��; */
	private Control[] connCtls = null;

	/**
	 * <p>��������: LDAP_connect|����: ����LDAP</p>
	 * @param null
	 * @return null;
	 */
	private void LDAP_connect() {
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		try {
			if(UID !=null && !"".equalsIgnoreCase(UID.trim())){
				//��������
				String uid = UID;
				if(uid.indexOf("@")<0 && !"".equalsIgnoreCase(domainName.trim())){
					uid=uid+"@"+domainName;
			}
			env.put("java.naming.security.principal", uid);    
			env.put("java.naming.security.credentials", PWD);
			ctx = new InitialLdapContext(env, connCtls);
		}
		} catch (javax.naming.AuthenticationException e) {
			System.out.println("Authentication faild: " + e.toString());
		} catch (Exception e) {
			System.out.println("Something wrong while authenticating: "
					+ e.toString());
		}
	}
	
	/**
	 * <p>��������: checkUserLogin|����: LDAP �û���������֤</p>
	 * @param userName |����: �û���¼��
	 * @param userPwd |����: �û���¼������
	 * @return boolean;
	 */
	public boolean checkUserLogin(String userName,String userPwd){
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL );
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		try {
			if(userName !=null && !"".equalsIgnoreCase(userName.trim())){
				String uid = userName;
				if(uid.indexOf("@")<0 && !"".equalsIgnoreCase(domainName.trim())){
					uid =userName+"@"+domainName;
				}
				env.put("java.naming.security.principal", uid);    
				env.put("java.naming.security.credentials", userPwd);
			}else{
				return false;
			}
			ctx = new InitialLdapContext(env, connCtls);
			ctx = null;
			return true;
			
		} catch (javax.naming.AuthenticationException e) {
			System.out.println("Authentication faild: " + e.toString());
			return false;
		} catch (Exception e) {
			System.out.println("Something wrong while authenticating: "
					+ e.toString());
			return false;
		}
	}
	
	/**
	 * <p>��������: checkUserName|����: LDAP �û���������֤</p>
	 * @param userName |����: �û���¼��
	 * @return boolean;
	 */
	public boolean checkUserName(String userName){
		LDAP_connect();
		try{
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);	
			//��������
			String uid = userName;
//			if(uid.indexOf("\\")<0){
//				uid=domainName+"\\"+userName;
//			}
			String strFilter ="sAMAccountName="+uid+"";//filter.r("{0}", uid);
			NamingEnumeration en = ctx.search(BASEDN, strFilter, constraints);
			if (en == null) {
				System.out.println("No NamingEnumeration.");
				return false;
			}
			if (!en.hasMoreElements()) {
				System.out.println("No element.");
				return false;
			}

			return true;
		}catch (javax.naming.AuthenticationException e) {
			System.out.println("Authentication faild: " + e.toString());
			return false;
		} catch (Exception e) {
			System.out.println("Something wrong while authenticating: "
					+ e.toString());
			return false;
		}
	}

	/**
	 * @param setURL ����·��
	 */
	public void setURL(String url) {
		URL = url;
	}

	/**
	 * @param setBASEDN Ҫ���õ� AD������·��
	 */
	public void setBASEDN(String basedn) {
		BASEDN = basedn;
	}

	/**
	 * @param setUID Ҫ���õ� ����Ա�û���
	 */
	public void setUID(String uid) {
		UID = uid;
	}

	/**
	 * @param setPWD Ҫ���õ� ����Ա����
	 */
	public void setPWD(String pwd) {
		PWD = pwd;
	}

	/**
	 * @param setFilter Ҫ���õ� ��������
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param setDomainName Ҫ���õ� ��
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	
	

}
