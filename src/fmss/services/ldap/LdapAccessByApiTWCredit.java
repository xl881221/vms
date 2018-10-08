package fmss.services.ldap;

/*****************************************************************************
 * User: Z00013388
 *       һ��User�H�����xȡ���˻����Y��֮���ޣ��������ь��xȡ��������
 *       ���ó�ʽ��ɫ֮���ޡ�
 * ServiceID��DN: ou=SCIDS,ou=APPs,o=CTCB
 *       �@��һ�����xȡ���ó�ʽ�c��ɫ���޵ķ��Վ�̖�����Á��ь��Єeһ
 *       ��ʹ���߱��ڙ����е����Б��ó�ʽ��ɫʹ�Ù��ޡ�
 ****************************************************************************/

import java.util.Properties;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;

import com.bizwave.ldap.LdapConnection;
import com.bizwave.ldap.LdapDN;

public class LdapAccessByApiTWCredit extends LdapAccessConnection {
	private Logger log = Logger.getLogger(LdapAccessByApiTWCredit.class);

	private static LdapAccessByApiTWCredit conn = new LdapAccessByApiTWCredit();

	public static LdapAccessByApiTWCredit getConnection() {
		return conn;
	}

	private String ip;
	private int port;
	private String rootBaseDN;
	private String sidDN;
	private String sidPass;

	public LdapAccessByApiTWCredit() {
		Properties p = loadProp("ldap/ldap_cert.properties");
		ip = p.getProperty("ip").trim();
		port = Integer.parseInt(p.getProperty("port").trim());
		// rootBaseDN��ָ��ʹ�ô�AP�Ĳ��T��λ��DN����:�Ї���Ӛ�̘I�y�л�H�ǂ���򷨽�
		rootBaseDN = p.getProperty("rootBaseDN").trim();
		// serviceID(����AP�]����LDAP�����)��DN�c�ܴa
		sidDN = p.getProperty("sidDN");
		sidPass = p.getProperty("sidPass");
	}

	public String auth(String userName, String password) {

		// ����ʹ���ߵĎ�̖�c�ܴa
		// String userName = "Z00013388";
		// String password = "123123";

		// ****���Eһ��ServiceID�B��*****************************************
		LdapConnection lc = new LdapConnection(ip, port);
		try {
			lc.connect(sidDN, sidPass);
			log.info("Service ID��" + sidDN + " �B���ɹ�");
		} catch (Exception e) {
			String msg = "Service ID��" + sidDN + " �J�Cʧ��" + e.getMessage();
			log.error(msg, e);
			return msg;
		}
		// ****���E������ServiceID�B��lc ȡ��User��DN
		// *****************************************
		String userDN = null;
		try {
			LdapDN DN = lc.getUserDN(userName, rootBaseDN);
			userDN = DN.getDN();
			System.out.println("User DN:" + userDN);
		} catch (Exception e) {
			String msg = "getUserDN failed " + e.getMessage();
			log.error(msg, e);
			return msg;
		}

		// ****���E������CUser�Ď�̖�c�ܴa*****************************************
		try {
			LdapConnection userLc = new LdapConnection(ip, port);
			userLc.connect(userDN, password);
			log.info("User��" + userName + " �ܴa��C�ɹ�");
			userLc.disconnect();
		} catch (Exception e) {
			String msg = "User��" + userName + "�ܴa��Cʧ��[" + e.getMessage() + "]";
			log.error(msg, e);
			return msg;
		}

		// ͨ��������������Ϊldap ��֤ͨ��.
		try {
			// ****���E�ģ���ServiceID�B��lc
			// ȡ��User�Ļ����Y��*****************************************
			try {
				Attributes attrs = lc.getAttributes(userDN);
				// ȡ����������
				Attribute attr = attrs.get("fullName");
				if (attr != null) {
					String fullName = (String) attr.get();
					log.info("fullName = " + fullName);
				}
				// ȡ��email address
				attr = attrs.get("mail");
				if (attr != null) {
					String email = (String) attr.get();
					log.info("email = " + email);
				}
			} catch (Exception e) {
				log.warn("get user info failed " + e.getMessage());
			}
			// ****���E�壺��ServiceID�B��lc
			// ȡ��User�Ľ�ɫ*****************************************
			try {
				LdapDN[] dns = lc.getUserRolesByAp(sidDN, sidDN, userDN);
				for (int i = 0; i < dns.length; i++) {
					String rdn = dns[i].getDN();
					log.info("Role." + i + " : " + rdn);
				}
			} catch (Exception e) {
				log.warn("get USER ROLE failed " + e.getMessage());
			}
		} finally {
			// ***�Y��Service ID�B��*****************************************
			lc.disconnect();
			log.info("user:" + userName + " LDAP ceritificate finished . ");
			return "ok";
		}
	}

}
