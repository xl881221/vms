package fmss.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import fmss.dao.entity.UBaseConfigDO;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:20:07
 * @����: [SQLDAO]JdbcTemplate���ݿ����
 */
public class SQLDAO extends JdbcDaoSupport{
	private static Map DataSourceMap = new HashMap();

	/**
	 * <p>��������: getDataForList|����: ����SQL��ѯ���������б�</p>
	 * @param sql
	 * @return
	 */
	public List getDataForList(String sql){
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		return jdbcTemplate.queryForList(sql);
	}


	/**
	 * <p>��������: getConnction|����: ȡ������</p>
	 * @param driverClass
	 * @param dburl
	 * @param user
	 * @param pass
	 * @return
	 */
	public static Connection getConnction(String driverClass, String dburl,
			String user, String pass){
		Connection dbConnection = null;
		try{
			Class.forName(driverClass).newInstance();
			dbConnection = DriverManager.getConnection(dburl, user, pass);
			System.out.println("���ݿ����ӳɹ�");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	public static Connection getConnction(UBaseConfigDO baseConfig){
		Connection dbConnection = null;
		DataSourceConfig dataSourceConfig = getDataSourceConfig(baseConfig);
		
		try{
			Class.forName(dataSourceConfig.getDbDriver()).newInstance();
			dbConnection = DriverManager.getConnection(dataSourceConfig.getDbUrl(), dataSourceConfig.getDbUserName(), dataSourceConfig.getDbPassword());
			System.out.println("���ݿ����ӳɹ�");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	private static DataSourceConfig getDataSourceConfig(UBaseConfigDO baseConfig){
		//������ϵͳ����ķ�ʽ��ȡ����ϵͳ���ݿ�������Ϣ
//		DataSourceConfig dataSourceConfig = null;
//		if(DataSourceMap.containsKey(baseConfig.getLinkSiteUrl())){
//			dataSourceConfig = (DataSourceConfig)DataSourceMap.get(baseConfig.getLinkSiteUrl());
//		}else{
//			HttpClient client = new HttpClient();
//			HttpMethod method = new GetMethod(baseConfig.getLinkSiteUrl() + Constants.DATASOURCE_CONFIG_URL);
//			try{
//				client.executeMethod(method);
//				String dataSourceString = method.getResponseBodyAsString();
//				JSONObject dataSourceJson = JSONObject.fromObject(dataSourceString);
//				dataSourceConfig = (DataSourceConfig)JSONObject.toBean(dataSourceJson, DataSourceConfig.class);
//				DataSourceMap.put(baseConfig.getLinkSiteUrl(), dataSourceConfig);
//			}catch (Exception e){
//				throw new DataAccessResourceFailureException("��ϵͳ[" + baseConfig.getLinkSiteUrl() + "]����û������, �޷���ȡ����ϵͳ��������Ϣ!");
//			}finally{
//				method.releaseConnection();
//			}
//		}
		// �������ݿ����õķ�ʽ��ȡ��ϵͳ���ݿ�����
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
//		dataSourceConfig.setDbDriver(baseConfig.getDbDriverClass());
//		dataSourceConfig.setDbUrl(baseConfig.getDbUrl());
//		dataSourceConfig.setDbUserName(baseConfig.getDbUserId());
//		dataSourceConfig.setDbPassword(baseConfig.getDbPassword());
		
		return dataSourceConfig;		
	}

	/**
	 * <p>��������: closeConnection|����:�ر����� </p>
	 * @param dbConnection
	 */
	public static void closeConnection(Connection dbConnection){
		try{
			if(dbConnection != null && (!dbConnection.isClosed())){
				dbConnection.close();
			}
		}catch (SQLException sqlEx){
			sqlEx.printStackTrace();
		}finally{
			if(dbConnection != null){
				try{
					dbConnection.close();
				}catch (SQLException e){
				}
			}
		}
	}

	/**
	 * <p>��������: closeResultSet|����: �رս����</p>
	 * @param res
	 */
	public static void closeResultSet(ResultSet res){
		try{
			if(res != null){
				res.close();
				res = null;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			if(res != null){
				try{
					res.close();
				}catch (SQLException e){
				}
			}
		}
	}

	/**
	 * <p>��������: closeStatement|����: �ر�����</p>
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt){
		try{
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			if(stmt != null){
				try{
					stmt.close();
				}catch (SQLException e){
				}
			}
		}
	}
}
