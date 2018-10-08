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
		env.put(Context.PROVIDER_URL, "ldap://10.168.166.108:389");// ����LDAP��URL�Ͷ˿�
		env.put(Context.SECURITY_AUTHENTICATION, "simple");// ��simple��ʽ����
		env.put(Context.SECURITY_PRINCIPAL, "uid=bhu,ou=people,DC=jes,DC=css,DC=com,DC=cn");// �û���
		env.put(Context.SECURITY_CREDENTIALS, "1");// ����
		String baseDN = "OU=people,DC=jes,DC=css,DC=com,DC=cn";// ��ѯ����
		String filter = "(&(objectClass=person)(cn=*))";// ������ѯ
		try{
			ctx = new InitialDirContext(env);// ����LDAP������
			System.out.println("Success");
			SearchControls constraints = new SearchControls();// ִ�в�ѯ����
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
			System.out.println("erro��" + e);
		}
	}
}
