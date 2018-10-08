package fmss.services.ldap;

import java.util.Properties;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

public class LdapAccessByWSConnection extends LdapAccessConnection {
	private String callMethod = "userVerificate";
	private String namespaceURI = "http://tempuri.org/";
	private Logger log = Logger.getLogger(LdapAccessByWSConnection.class);

	private String endpoint;
	private String ldapUrl;
	private String domain;
	private static LdapAccessByWSConnection conn = new LdapAccessByWSConnection();

	public LdapAccessByWSConnection() {
		Properties p = loadProp("ldap/ldap_ws.properties");
		this.endpoint = p.getProperty("wsUrl");
		this.ldapUrl = p.getProperty("ldapUrl");
		this.domain = p.getProperty("domain");
	}

	public static LdapAccessByWSConnection getConnection() {
		return conn;
	}

	public String auth(String user, String pw) {

		try {
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));

			// 要调用的方法名

			call.setOperationName(new javax.xml.namespace.QName(namespaceURI,
					callMethod));

			// call.setOperationName("userVerificate");
			// 设置调用方法参数

			call.addParameter(
					new javax.xml.namespace.QName(namespaceURI, "url"),
					org.apache.axis.encoding.XMLType.XSD_STRING, String.class,
					javax.xml.rpc.ParameterMode.IN);

			call.addParameter(new javax.xml.namespace.QName(namespaceURI,
					"name"), org.apache.axis.encoding.XMLType.XSD_STRING,
					String.class, javax.xml.rpc.ParameterMode.IN);

			call.addParameter(
					new javax.xml.namespace.QName(namespaceURI, "pwd"),
					org.apache.axis.encoding.XMLType.XSD_STRING, String.class,
					javax.xml.rpc.ParameterMode.IN);

			call.setReturnClass(String.class);
			/**
			 * //new
			 * javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema" //,
			 * "string")或org.apache.axis.encoding.XMLType.XSD_STRING都可以 //设置返回类型
			 * call.setReturnType(new javax.xml.namespace.QName(
			 * "http://www.w3.org/2001/XMLSchema", "string")); //返回参数名
			 * call.setReturnQName(new javax.xml.namespace.QName(
			 * "http://WebXml.com.cn/", "qqCheckOnlineResult"));
			 **/
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(namespaceURI + callMethod);

			call.setOperationName(new javax.xml.namespace.QName(namespaceURI,
					callMethod));

			String msg = (String) call.invoke(new Object[] { ldapUrl,
					domain + "\\" + user, pw });

			// if (msg.equalsIgnoreCase("ok")) {
			// System.out.println("user :" + user + " authorized success");
			// } else {
			// System.out.println("user :" + user + "authorized fail: " + msg);
			// }
			return msg;

		} catch (Throwable e) {
			// System.out.println("authorized fail: " + e.getMessage());
			String msg = "authorized fail: " + e.getMessage();
			log.error(msg, e);
			return msg;
		}
	}

}
