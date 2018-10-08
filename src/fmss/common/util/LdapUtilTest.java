package fmss.common.util;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.DirContext;
import javax.naming.NamingException;
import javax.naming.AuthenticationException;
import java.util.Hashtable;

class AuthUser {
    public static void main(String[] args) {
    	
    	/*
    	 * 
    	 * #'525': 'user not found',
                   '52e': 'invalid credentials',
                   '530': 'not permitted to logon at this time',
                   '531': 'not permitted to logon at this workstation',
                   '532': 'password expired',
                   '533': 'account disabled',
                   '701': 'account expired',
                   '773': 'user must reset password',
                   '775': 'user account locked',

    	 * */
		// Identify service provider to use
		Hashtable env = new Hashtable(11);
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://test:389");
		// 你需要将localhost改为domino服务器的全称或IP，并且，将O=改为你的Domino组织名称
		// Authenticate as xinxibu and password "1234"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "w@test");
		env.put(Context.SECURITY_CREDENTIALS, "chy-12314");
		try {
			// Create the initial directory context
			DirContext ctx = new InitialDirContext(env);
			System.out.println("Authentication Ok!");
			ctx.close();
		} catch (AuthenticationException e) {
			// 如果捕捉到AuthenticationException，表示验证失败
			if(e.toString().indexOf("52e")>-1){
				System.err.println("Authentication Fail:" + "invalid credentials");
			}
			if(e.toString().indexOf("530")>-1){
				System.err.println("Authentication Fail:" + "not permitted to logon at this time");
			}
			if(e.toString().indexOf("531")>-1){
				System.err.println("Authentication Fail:" + "not permitted to logon at this workstation");
			}
			if(e.toString().indexOf("532")>-1){
				System.err.println("Authentication Fail:" + "password expired");
			}
			if(e.toString().indexOf("533")>-1){
				System.err.println("Authentication Fail:" + "account disabled");
			}
			if(e.toString().indexOf("701")>-1){
				System.err.println("Authentication Fail:" + "account expired");
			}
			if(e.toString().indexOf("773")>-1){
				System.err.println("Authentication Fail:" + "user must reset password");
			}
			if(e.toString().indexOf("775")>-1){
				System.err.println("Authentication Fail:" + "user account locked");
			}
			System.err.println("Authentication Fail:" + e);
		} catch (NamingException e) {
			System.err.println("Nameing Exception: " + e);
		}
    }
}

