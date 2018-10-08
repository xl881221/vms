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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: wangxin
 * @日期: 2009-9-2 上午10:13:00
 * @描述: [LdapUtil] LDAP处理
 */
public class LdapUtil {
	
	/** 访问路径 */
	private String URL = "";//"ldap://ssuzdc3:389/";
	
	/** AD服务器路径 */
	private String BASEDN = "";////"OU=Local Profile,OU=Users,OU=Suzhou,DC=xxxx,DC=com";
	private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	/** 管理员用户名 */
	private String UID = "";
	/** 管理员密码 */
	private String PWD = "";
	/** 过滤条件 */
	private String filter="";
	/** 域名*/
	private String domainName="";
	
	//private String 
	private LdapContext ctx = null;
	
	/** 存储参数 */
	private Hashtable env = null;
	
	/** 未知用途 */
	private Control[] connCtls = null;

	/**
	 * <p>方法名称: LDAP_connect|描述: 连接LDAP</p>
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
				//增加域名
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
	 * <p>方法名称: checkUserLogin|描述: LDAP 用户名密码验证</p>
	 * @param userName |描述: 用户登录名
	 * @param userPwd |描述: 用户登录名密码
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
	 * <p>方法名称: checkUserName|描述: LDAP 用户名密码验证</p>
	 * @param userName |描述: 用户登录名
	 * @return boolean;
	 */
	public boolean checkUserName(String userName){
		LDAP_connect();
		try{
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);	
			//增加域名
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
	 * @param setURL 访问路径
	 */
	public void setURL(String url) {
		URL = url;
	}

	/**
	 * @param setBASEDN 要设置的 AD服务器路径
	 */
	public void setBASEDN(String basedn) {
		BASEDN = basedn;
	}

	/**
	 * @param setUID 要设置的 管理员用户名
	 */
	public void setUID(String uid) {
		UID = uid;
	}

	/**
	 * @param setPWD 要设置的 管理员密码
	 */
	public void setPWD(String pwd) {
		PWD = pwd;
	}

	/**
	 * @param setFilter 要设置的 过滤条件
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @param setDomainName 要设置的 域
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	
	

}
