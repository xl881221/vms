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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:20:07
 * @描述: [SQLDAO]JdbcTemplate数据库操作
 */
public class SQLDAO extends JdbcDaoSupport{
	private static Map DataSourceMap = new HashMap();

	/**
	 * <p>方法名称: getDataForList|描述: 根据SQL查询返回数据列表</p>
	 * @param sql
	 * @return
	 */
	public List getDataForList(String sql){
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		return jdbcTemplate.queryForList(sql);
	}


	/**
	 * <p>方法名称: getConnction|描述: 取得连接</p>
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
			System.out.println("数据库连接成功");
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
			System.out.println("数据库连接成功");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	private static DataSourceConfig getDataSourceConfig(UBaseConfigDO baseConfig){
		//采用子系统请求的方式获取各子系统数据库连接信息
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
//				throw new DataAccessResourceFailureException("子系统[" + baseConfig.getLinkSiteUrl() + "]可能没有启动, 无法获取该子系统的连接信息!");
//			}finally{
//				method.releaseConnection();
//			}
//		}
		// 采用数据库配置的方式获取子系统数据库链接
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
//		dataSourceConfig.setDbDriver(baseConfig.getDbDriverClass());
//		dataSourceConfig.setDbUrl(baseConfig.getDbUrl());
//		dataSourceConfig.setDbUserName(baseConfig.getDbUserId());
//		dataSourceConfig.setDbPassword(baseConfig.getDbPassword());
		
		return dataSourceConfig;		
	}

	/**
	 * <p>方法名称: closeConnection|描述:关闭连接 </p>
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
	 * <p>方法名称: closeResultSet|描述: 关闭结果集</p>
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
	 * <p>方法名称: closeStatement|描述: 关闭声明</p>
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
