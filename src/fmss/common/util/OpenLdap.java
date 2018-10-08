package fmss.common.util;


/**
 *
 * @author River
 */
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class OpenLdap{

	public static void main(String[] args){
		OpenLdap ldap = new OpenLdap();
		ldap.init();
	}

	public void init(){
		DirContext ctx = null;
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.168.166.108:389");// 连接LDAP的URL和端口
		env.put(Context.SECURITY_AUTHENTICATION, "simple");// 以simple方式发送
		env.put(Context.SECURITY_PRINCIPAL, "uid=bhu,ou=people,DC=jes,DC=css,DC=com,DC=cn");// 用户名
		env.put(Context.SECURITY_CREDENTIALS, "1");// 密码
		String baseDN = "OU=people,DC=jes,DC=css,DC=com,DC=cn";// 查询区域
		String filter = "(&(objectClass=person)(cn=*))";// 条件查询
		try{
			ctx = new InitialDirContext(env);// 连接LDAP服务器
			System.out.println("Success");
			SearchControls constraints = new SearchControls();// 执行查询操作
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration en = ctx.search(baseDN, filter, constraints);
			if(en == null){
				System.out.println("There have no value");
			}else{
				while(en.hasMoreElements()){
					Object obj = en.nextElement();
					if(obj instanceof SearchResult){
						SearchResult sr = (SearchResult) obj;
						String cn = sr.getName();
						System.out.println("cccccc: " + cn);
					}
				}
			}
		}catch (javax.naming.AuthenticationException e){
			System.out.println("fail");
		}catch (Exception e){
			System.out.println("erro：" + e);
		}
	}
}
