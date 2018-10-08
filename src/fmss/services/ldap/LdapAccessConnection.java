package fmss.services.ldap;

import java.io.InputStream;
import java.util.Properties;

public abstract class LdapAccessConnection {
	public abstract String auth(String user, String pw);

	protected Properties loadProp(String propFile) {
		Properties p = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				propFile);
		try {
			p.load(in);
		} catch (Throwable e) {
			throw new RuntimeException("���������ļ���" + propFile + "�ļ�ʧ��:"
					+ e.getMessage());
		}
		return p;
	}

	public static boolean isAuthorizeSuccess(String msg) {
		return "ok".equalsIgnoreCase(msg);
	}
}
