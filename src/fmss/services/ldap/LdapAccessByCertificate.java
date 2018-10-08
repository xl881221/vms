package fmss.services.ldap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

public class LdapAccessByCertificate extends LdapAccessConnection {

	private static Logger log = Logger.getLogger(LdapAccessByCertificate.class);

	public String err_info = "";

	private String URL;
	// private String URL = "ldap:10.190.105.2:389/";
	private String BASEDN;
	private String USERDN;
	private String MYDOMAIN;

	private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	// 安全访问需要的证书库
	private String sslTrustStore;
	private String sslTrustStorePassword;

	// 安全通道访问
	private String securityProtocol;

	private LdapContext ctx = null;
	private Hashtable env = null;

	// private Control[] connCtls = null;

	private static LdapAccessByCertificate conn = new LdapAccessByCertificate();

	public static LdapAccessByCertificate getConnection() {
		return conn;
	}

	public LdapAccessByCertificate() {
		Properties p = loadProp("ldap/ldap_cert.properties");
		URL = p.getProperty("url");
		BASEDN = p.getProperty("basedn");
		USERDN = p.getProperty("userdn");
		MYDOMAIN = p.getProperty("mydomain");
		sslTrustStore = p.getProperty("sslTrustStore");
		sslTrustStorePassword = p.getProperty("sslTrustStorePassword");
		securityProtocol = p.getProperty("securityProtocol");

		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL + BASEDN);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		if (env != null && "ssl".equals(securityProtocol)) {
			// 设定访问协议为ssl
			env.put(Context.SECURITY_PROTOCOL, securityProtocol);

			// 设置访问证书属性，若没有此证书将无法通过ssl访问AD
			System.setProperty("javax.net.ssl.trustStore", sslTrustStore);
			System.setProperty("javax.net.ssl.trustStorePassword",
					sslTrustStorePassword);

			// System.out.println("@@@javax.net.ssl.trustStore"
			// + System.getProperty("javax.net.ssl.trustStore"));
			// System.out.println("@@@javax.net.ssl.trustStorePassword"
			// + System.getProperty("javax.net.ssl.trustStorePassword"));
		}
	}

	public String auth(String userName, String password) {
		String failMsg = "连接LDAP认证 失败";
		env.put("java.naming.security.principal", "CN=" + userName + ","
				+ USERDN);
		env.put("java.naming.security.principal", MYDOMAIN + "\\" + userName);
		env.put("java.naming.security.credentials", password);
		env.put(Context.PROVIDER_URL, URL);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		log.info("*****[" + sdf.format(new Date()) + "],user:{ " + userName
				+ "}  trying to log on ldap");

		// 连接Ldap服务器
		try {
			ctx = new InitialLdapContext(env, null);
			log.info("*****" + userName + " is authenticated");
			return "ok";
		} catch (Throwable e) {
			failMsg += e.getMessage();
			log.error(failMsg, e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (Exception ie) {
				log.error(ie.toString(), ie);
			}
		}
		return failMsg;
	}
	//
	// public static void main(String[] args) {
	// LdapAccessByCertificate userAuthenticate = new LdapAccessByCertificate();
	// String user = "jerrylijianguo";
	// String pw = "Password0";
	// userAuthenticate.authenricate(user, pw);
	// }

}
