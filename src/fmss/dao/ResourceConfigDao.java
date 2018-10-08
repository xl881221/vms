package fmss.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import fmss.dao.entity.UAuthResMapDO;
import fmss.common.db.SQLDAO;
import common.core.dataexchange.ExchangeManager;

import fmss.common.util.Constants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: sunzhan
 * @����: 2009-6-26 ����03:00:08
 * @����: [ResourceConfigDao]��Դ���õ�jdbc���ݷ����࣬��Ҫʵ�ָ���ϵͳ����Դ�����
 * 
 * �޸�: Ԭ���� ����: 2009-12-01 10:00:08 ����: ����ϵͳ��Դ�ķ���,����������base_config�������õ�jdbc����,
 * ���ǴӸ���ϵͳ����(dataSourceConfig.ajax)�õ�
 */
public class ResourceConfigDao extends SQLDAO {

	/** logger logger���� */
	private static final Logger logger = Logger
			.getLogger(ResourceConfigDao.class);

	/**
	 * <p>
	 * ��������: getResource|����:��ȡ��ϵͳ��Դ��¼
	 * </p>
	 * 
	 * @param resMap
	 *            ��Դ��Ϣ
	 * @param baseConfig
	 *            ��ϵͳ��Ϣ
	 * @return ��Դ���
	 * @throws Exception
	 */
	public List getResource(UAuthResMapDO resMap, String dbUrl)
			throws Exception {
		String query = "select " + resMap.getSrcKeyField() + ","
				+ resMap.getSrcIdField() + "," + resMap.getSrcNameField()
				+ " from " + resMap.getSrcTable();
		return getSysRes(resMap.isVisitSelfResource(), query, dbUrl);
	}

	/**
	 * <p>
	 * ��������: getLevResCtn|����:(�򻯺�)��ȡ��Դ������û�����õ���Դʵ������
	 * </p>
	 * 
	 * @param hasList
	 *            ��Դ�������Ѿ����õ�����
	 * @param resMap
	 *            ����������
	 * @param baseConfig
	 *            ��ϵͳ��Ϣ
	 * @return ��Դ������û����������
	 * @throws Exception
	 */
	public List getLevResCtn(List hasList, UAuthResMapDO resMap, String dbUrl,
			String sysId) throws Exception {
		// ȡ��Դ������û�е���Դ����
		String query = "select " + resMap.getSrcKeyField() + ","
				+ resMap.getSrcIdField() + "," + resMap.getSrcNameField()
				+ " from " + resMap.getSrcTable() + " where 1=1 ";
		// �ж��Ƿ��в˵�
		if (Constants.MENU_RES_ID == Integer.parseInt(resMap.getResId())) {
			query += " and system_id ='" + sysId + "'";
		}
		// ��������
		// baseConfig = resMap.getUbaseConfig();
		//String condition = "";
		StringBuffer sb = new StringBuffer();
		if(hasList.size()>0){
			sb.append(" and " + resMap.getSrcIdField() + " not in(");
		}
		// �򻯺�ȥ���ж���Դ�����߼�
		int count = 1;
		for (Iterator iterator = hasList.iterator(); iterator.hasNext();) {
			Map map = (Map) iterator.next();
			if (resMap.getResType().equals(map.get("resType"))) {
				if(count%1000==0){
					sb.append(") and " + resMap.getSrcIdField() + " not in(");
				}
				if(resMap.getSrcTable().toUpperCase().equals("SC_TEMPLATE")||resMap.getSrcTable().toUpperCase().equals("SC_OPER_BUTTON")||resMap.getSrcTable().toUpperCase().equals("SC_OPER")){
					//condition += "" + map.get("resDetailValue") + ",";
					sb.append("" + map.get("resDetailValue") + ",");
				}else{
					//condition += "'" + map.get("resDetailValue") + "',";
					sb.append("'" + map.get("resDetailValue") + "',");
				}
				count++;
			}
		}
		if(hasList.size()>0){
			sb.append(")");
		}
		String str = sb.toString().replaceAll(",\\)", "\\)");
		// �ص�ĩβ��,
//		if (condition.endsWith(",")) {
//			condition = condition.substring(0, condition.length() - 1);
//			query += " and " + resMap.getSrcIdField() + " not in (" + condition + ")";
//		}
		query += str;
		query += " order by 1";
		logger.info(query);

		return callSubSysRes(resMap.isVisitSelfResource(), query, dbUrl, resMap.getSystemId());
	}

	private List getSysRes(boolean isVisitSelfResource, String sql,
			String subSysUri) {
		return callSubSysRes(isVisitSelfResource, sql, subSysUri, null);
	}

	private static String SSL_PROTOCOL = "https";

	private static int SSL_PORTS = 443;

	/**
	 * ��̬��ȡ ��ϵͳ ��Դ��Ϣ.
	 * 
	 * @param sql
	 * @param subSysUri
	 * @param sysId
	 * @return
	 * @author xieli
	 * @param isVisitSelfResource
	 */
	private List callSubSysRes(boolean isVisitSelfResource, String sql,
			String subSysUri, String sysId) {

		if (isVisitSelfResource) {
			ExchangeManager ex = ExchangeManager.getInstance(this
					.getJdbcTemplate().getDataSource());
			List initL = ex.buildSqlResult(sql);
			return trans(initL, sysId);
		}

		if (subSysUri.startsWith(SSL_PROTOCOL)) {
			Protocol protocol = new Protocol(SSL_PROTOCOL,
					new NoSecureProtocolSocketFactory(), getPort(subSysUri));
			Protocol.registerProtocol(SSL_PROTOCOL, protocol);
		}

		HttpClient client = new HttpClient();
		// String encode = URLEncoder.encode(sql);
		String encode = "gbk";
		try {
			encode = URLEncoder.encode(sql, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// will not occur
		}
		String url = subSysUri + "&sql=" + encode;

		List l = null;
		HttpMethod method = null;
		logger.info("forReplace:" + url + "------");
		try {
			method = new GetMethod(url);
			client.executeMethod(method);
			// Returns the response body of the HTTP method, if any, as a {@link
			// String}.
			// * If response body is not available or cannot be read,
			// <tt>null</tt> is returned.
			// * The raw bytes in the body are converted to a
			// <code>String</code> using the
			// * character encoding specified in the response's
			// <tt>Content-Type</tt> header, or
			// * ISO-8859-1 if the response did not specify a character set.
			// * <p>
			// * Note that this method does not propagate I/O exceptions.
			// * If an error occurs while reading the body, <code>null</code>
			// will be returned.
			// *
			// * @return The response body converted to a <code>String</code>,
			// or <code>null</code>
			// * if the body is not available.

			// String resp = method.getResponseBodyAsString();
			String resp = new String(method.getResponseBody(), "gbk");
			XStream xs = new XStream(new DomDriver());
			logger.info("responseHeader: "
					+ xs.toXML(method.getResponseHeaders()));
			logger.info(resp);
			//resp += "Error 404: SRVE0190E: File not found: /getHolidayTypes.action";
			try{
				//websphere��responsexml�������Error 404: SRVE0190E: File not found�����⴦��
				final String endQuote = "</list>";
				if(StringUtils.isNotEmpty(resp) && resp.indexOf(endQuote) != -1){
					if(resp.indexOf(endQuote) + endQuote.length() < resp.length()){
						logger.warn("response string ���쳣���ݣ�" + resp.substring(resp.indexOf(endQuote) + endQuote.length()));
						resp = resp.substring(0, resp.indexOf(endQuote) + endQuote.length());
						logger.warn("�ض����:" + resp);
					}
				}
			}catch(StringIndexOutOfBoundsException e){
				logger.error("����response stringʱ�����±�Խ��,�ָ�Ĭ��ֵ ", e);
			}
			
			List list = (List) xs.fromXML(resp);

			l = trans(list, sysId);
		} catch (Throwable e) {
			logger
					.error(
							"��ȡ��ϵͳ��"
									+ url
									+ " ��Դ�����쳣,��ȷ��Security_DeleteDBpass_oracle.sql�ű���ִ�� ���ϵͳ ������",
							e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return l;
	}

	private List trans(List l, String sysId) {
		List ln = new ArrayList();
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			Object obj = iterator.next();
			Map m = new HashMap();
			if (obj instanceof resoft.uprr.dataexchange.UPRRCallSysResDO) {
				resoft.uprr.dataexchange.UPRRCallSysResDO dox = (resoft.uprr.dataexchange.UPRRCallSysResDO)obj;
				m.put(dox.getSrcNameFieldColName(), dox.getSrcNameField());
				m.put(dox.getSrcIDFieldColName(), dox.getSrcIDField());
				m.put(dox.getSrcKeyFieldColName(), dox.getSrcKeyField());
			} 
			if (obj instanceof common.core.dataexchange.UPRRCallSysResDO){
				common.core.dataexchange.UPRRCallSysResDO dox = (common.core.dataexchange.UPRRCallSysResDO)obj;
				m.put(dox.getSrcNameFieldColName(), dox.getSrcNameField());
				m.put(dox.getSrcIDFieldColName(), dox.getSrcIDField());
				m.put(dox.getSrcKeyFieldColName(), dox.getSrcKeyField());
			}
			if (sysId != null) {
				m.put("systemId", sysId);
			}
			ln.add(m);
		}
		return ln;

	}

	private int getPort(String url) {
		int port = url.startsWith(SSL_PROTOCOL) ? SSL_PORTS : 80;
		try {
			url = url.substring(url.lastIndexOf(":")).substring(1,
					url.indexOf("/"));
			port = Integer.parseInt(url);
		} catch (Exception e) {
			logger.error(e);
		}
		return port;
	}
}

class NoSecureProtocolSocketFactory implements SecureProtocolSocketFactory {
	private static final Log log = LogFactory
			.getLog(NoSecureProtocolSocketFactory.class);

	private SSLContext sslcontext = null;

	private SSLContext createSSLContext() {
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null,
					new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		} catch (KeyManagementException e) {
			log.error(e);
		}
		return sslcontext;
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host,
				port, autoClose);
	}

	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(String host, int port, InetAddress clientHost,
			int clientPort) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port,
				clientHost, clientPort);
	}

	public Socket createSocket(String host, int port, InetAddress localAddress,
			int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress,
					localPort);
		} else {
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress,
					localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

}