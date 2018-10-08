package fmss.services.ldap;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;



public class LdapAccessByCommon extends LdapAccessConnection {
	private Logger log = Logger.getLogger(LdapAccessByCommon.class);


	private String ldapUrl;
	
	private String baseDN;
	
	private String domain;

	private static LdapAccessByCommon conn = new LdapAccessByCommon();
	
	public static LdapAccessByCommon getConnection() {
		return conn;
	}
	public LdapAccessByCommon() {
		Properties p = loadProp("ldap/ldap_common.properties");
		ldapUrl = p.getProperty("url").trim();
		if(!ldapUrl.endsWith("/")){
			ldapUrl+="/";
		}
		
		// rootBaseDN��ָ��ʹ�ô�AP�Ĳ��T��λ��DN����:�Ї���Ӛ�̘I�y�л�H�ǂ���򷨽�
		baseDN = p.getProperty("basedn").trim();
		domain=p.getProperty("domain").trim();

	}

	public String auth(String userName, String password) {

		String message="";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapUrl+baseDN); // ldap��ַ
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, domain+"\\"+userName); // �û���@����
		env.put(Context.SECURITY_CREDENTIALS, password); // ����
		
		try {  
			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);
			System.out.println("Authentication Ok!");
			ctx.close();
			return "ok";
		} catch (AuthenticationException e) {
			e.printStackTrace();
			// �����׽��AuthenticationException����ʾ��֤ʧ��
			if (e.toString().indexOf("52e") > -1) {
				System.err.println("Authentication Fail:"
						+ "invalid credentials");
			}
			
			if (e.toString().indexOf("530") > -1) {
				System.err.println("Authentication Fail:"
						+ "not permitted to logon at this time");
			}
			if (e.toString().indexOf("531") > -1) {
				System.err.println("Authentication Fail:"
						+ "not permitted to logon at this workstation");
			}
			if (e.toString().indexOf("532") > -1) {
				System.err.println("Authentication Fail:" + "password expired");
			}
			if (e.toString().indexOf("533") > -1) {
				System.err.println("Authentication Fail:" + "account disabled");
			}
			if (e.toString().indexOf("701") > -1) {
				System.err.println("Authentication Fail:" + "account expired");
			}
			if (e.toString().indexOf("773") > -1) {
				System.err.println("Authentication Fail:"
						+ "user must reset password");
			}
			if (e.toString().indexOf("775") > -1) {
				System.err.println("Authentication Fail:"
						+ "user account locked");
			}
			System.err.println("Authentication Fail:" + e);
		} catch (NamingException e) {
			System.err.println("Nameing Exception: " + e);
		}
		return "error";
		
	}

}
