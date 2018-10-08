package fmss.services.ldap;

/*****************************************************************************
 * User: Z00013388
 *       一般User僅具有讀取個人基本資料之權限，不具有搜尋讀取其所擁有
 *       應用程式角色之權限。
 * ServiceID的DN: ou=SCIDS,ou=APPs,o=CTCB
 *       這是一具有讀取應用程式與角色權限的服務帳號，被用來搜尋判別一
 *       般使用者被授權擁有的所有應用程式角色使用權限。
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
		// rootBaseDN是指會使用此AP的部門單位的DN，如:中國信託商業銀行或僅是個金或法金
		rootBaseDN = p.getProperty("rootBaseDN").trim();
		// serviceID(就是AP註冊在LDAP的物件)的DN與密碼
		sidDN = p.getProperty("sidDN");
		sidPass = p.getProperty("sidPass");
	}

	public String auth(String userName, String password) {

		// 登入使用者的帳號與密碼
		// String userName = "Z00013388";
		// String password = "123123";

		// ****步驟一：ServiceID連線*****************************************
		LdapConnection lc = new LdapConnection(ip, port);
		try {
			lc.connect(sidDN, sidPass);
			log.info("Service ID：" + sidDN + " 連線成功");
		} catch (Exception e) {
			String msg = "Service ID：" + sidDN + " 認證失敗" + e.getMessage();
			log.error(msg, e);
			return msg;
		}
		// ****步驟二：以ServiceID連線lc 取得User的DN
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

		// ****步驟三：驗證User的帳號與密碼*****************************************
		try {
			LdapConnection userLc = new LdapConnection(ip, port);
			userLc.connect(userDN, password);
			log.info("User：" + userName + " 密碼驗證成功");
			userLc.disconnect();
		} catch (Exception e) {
			String msg = "User：" + userName + "密碼驗證失敗[" + e.getMessage() + "]";
			log.error(msg, e);
			return msg;
		}

		// 通过第三步，可视为ldap 验证通过.
		try {
			// ****步驟四：以ServiceID連線lc
			// 取得User的基本資料*****************************************
			try {
				Attributes attrs = lc.getAttributes(userDN);
				// 取得中文姓名
				Attribute attr = attrs.get("fullName");
				if (attr != null) {
					String fullName = (String) attr.get();
					log.info("fullName = " + fullName);
				}
				// 取得email address
				attr = attrs.get("mail");
				if (attr != null) {
					String email = (String) attr.get();
					log.info("email = " + email);
				}
			} catch (Exception e) {
				log.warn("get user info failed " + e.getMessage());
			}
			// ****步驟五：以ServiceID連線lc
			// 取得User的角色*****************************************
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
			// ***結束Service ID連線*****************************************
			lc.disconnect();
			log.info("user:" + userName + " LDAP ceritificate finished . ");
			return "ok";
		}
	}

}
