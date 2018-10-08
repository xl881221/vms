package fmss.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.common.util.ColBean;
import fmss.common.util.DbBean;
import fmss.common.util.TableBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;





/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: liruilin
 * @日期: 2011-1-4 
 * @描述: 数据库对比工具-xml导出
 * 
 * 修改
 * 修改人:liruilin
 * 修改时间:2011-07-15
 * 描述:为了实现oracle和db2对比,不再关注具体字段类型而抽象成模型
 * 
 */
public class ExportXMLService {

	private Connection conn;
	private String errmsg;
	protected transient final Log log = LogFactory.getLog(getClass());
	public Document exportXml(String url,String username,String password,int dbtype,String dbname){
		if(url==null||username==null||password==null){
			setErrmsg("url,username,password参数有错");
			return null;
		}
		DbBean db=new DbBean(dbtype,url, dbname, username, password);
		
		boolean flag=openConnection(dbtype, url, dbname, username, password);
		//String filepath=null;
		Document doc=null;
		if(flag){
			//filepath=System.getProperty("User.home")+db.getDbname()+".xml";
			doc=toParse(db);
			closeConnection();
		}
		
		return doc;
	}
	
	/**
	 * 解析准备工作
	 */
	public Document toParse(DbBean dbbean){
		Document doc=null;
		try {
			if(dbbean.getDbType()==0){
				oracleParse(dbbean);
			}else if(dbbean.getDbType()==1){
				db2Parse(dbbean);
			}
			 doc= createDoc(dbbean);
		} catch (SQLException e) {	
			e.printStackTrace();
			log.info("toParse",e);
			this.setErrmsg("查询数据库出现错误,请核实该用户拥有dba权限");
		}
		return doc;
	}
	/**
	 * 根据内存信息写xml
	 * @param dblist
	 * @return
	 */
	private Document createDoc(DbBean dbbean) {
		Document doc = DocumentHelper.createDocument(); 
		doc.setXMLEncoding("GBK");
		//Element root = doc.addElement("dbs");   
		
			//DbBean dbbean=(DbBean)dblist.get(i);
			//Element db=root.addElement("db");
			Element db=doc.addElement("db"); 
			db.addAttribute("dbname", ""+dbbean.getDbname());
			db.addAttribute("src_db_type", dbbean.getDbType()==0?"oracle":"db2");
			Map tablemap=dbbean.getTablemap();
			List viewList=dbbean.getViewList();
			List seqList=dbbean.getSeqList();
			List procList=dbbean.getProcList();
			Iterator iter=tablemap.keySet().iterator();
			while(iter.hasNext()){
				TableBean tb=(TableBean)tablemap.get(iter.next());
				Element table=db.addElement("table");
				table.addAttribute("tablename",tb.getName());
				Element cols=table.addElement("columns");			
				List collist=(List)tb.getCols();
				for(int j=0;j<collist.size();j++){
					ColBean col=(ColBean)collist.get(j);
					Element column=cols.addElement("column");
					Element colname=column.addElement("columnname");
					colname.addText(col.getName());
					Element coltype=column.addElement("type");
					coltype.addText(col.getType());
					if(col.getLength()!=null){
						Element collength=column.addElement("length");
						collength.addText(col.getLength());
					}
					if(col.getScale()!=null){
						Element coldefault=column.addElement("scale");
						coldefault.addText(col.getScale());
					}
					if(col.getDeft()!=null){
						Element coldefault=column.addElement("default");
						coldefault.addText(col.getDeft());
					}
					if(col.getConstraint_type()!=null){
						Element colConstraint_type=column.addElement("constraint_type");
						colConstraint_type.addText(col.getConstraint_type());
					}
					Element colnullable=column.addElement("nullable");
					colnullable.addText(col.getNullable());
				}
			}
			for(int j=0;j<viewList.size();j++){
				Element view=db.addElement("view");
				view.addText((String)viewList.get(j));
			}
			for(int j=0;j<seqList.size();j++){
				Element view=db.addElement("sequence");
				view.addText((String)seqList.get(j));
			}
			for(int j=0;j<procList.size();j++){
				Element view=db.addElement("procedure");
				view.addText((String)procList.get(j));
			}
				
		return doc;
	}
	/**
	 * 解析oracle数据库
	 * @param username
	 * @param tablemap
	 * @return
	 * @throws SQLException
	 */
	public void oracleParse(DbBean dbbean) throws SQLException{
		//解析表
		Map tablemap=dbbean.getTablemap();
		List viewList=dbbean.getViewList();
		List seqList=dbbean.getSeqList();
		List procList=dbbean.getProcList();
		PreparedStatement ps = conn.prepareStatement("select tab.table_name,col.COLUMN_NAME,col.DATA_TYPE,col.DATA_LENGTH,col.DATA_PRECISION,col.DATA_SCALE,col.DATA_DEFAULT,col.NULLABLE from sys.user_tables tab join user_tab_columns col on tab.table_name=col.TABLE_NAME  order by tab.table_name,col.COLUMN_ID");
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("table_name");
			TableBean tb=null;
			if(tablemap.get(tablename)==null){
				tb=new TableBean();
				tb.setName(tablename);
				ColBean cb=new ColBean();
				cb.setName(rs.getString("COLUMN_NAME"));
				//模型化处理字段类型
				String type=rs.getString("DATA_TYPE");
				if(type!=null){					
					if(("NUMBER").equals(type)){
						//处理int,integer,smallint,decimal,number,numeric
						String length=rs.getString("DATA_PRECISION");
						String scale=rs.getString("DATA_SCALE");
						if(length==null&&"0".equals(scale)){
							//oracle中int等类型scale=0,使用decimal定义数据类型如果不加长度定义出来还是int,及scale还是为0
							cb.setType("INTEGER");
						}else{
							//注意:使用number定义数据类型时,如果不加上长度,存入系统表的数据类型是number,其PRECISION和scale都为null
							cb.setType("NUMBER");
							if(length!=null){
								cb.setLength(length);
							}							
							if(scale!=null){
								cb.setScale(rs.getString("DATA_SCALE"));
							}
						}						
					}else if("FLOAT".equals(type)){
						//处理float,real
						cb.setType("NUMBER");
						if(rs.getString("DATA_PRECISION")!=null){
							cb.setLength(rs.getString("DATA_PRECISION"));
						}
					}else if(type.indexOf("CHAR")!=-1){
						//处理varchar,varchar2,NCHAR,NVARCHAR2类型
						//oracle中char如果没有设定长度系统表中会默认为1,nchar为2,varchar2等必须指定长度
						if(type.indexOf("VARCHAR")!=-1){
							cb.setType("VARCHAR");
						}else{
							cb.setType("CHAR");
						}
						
						String length=rs.getString("DATA_LENGTH");
						if(type.startsWith("N")){
							//处理NCHAR,NVARCHAR2
							cb.setLength(Integer.parseInt(length)/2+"");
						}else{
							cb.setLength(length);
						}
					}else if(type.indexOf("TIMESTAMP")!=-1){
						//处理timestamp类型
						//oracle的timestamp类型在该表中存放为TIMESTAMP(6)
						cb.setType("TIMESTAMP");
					}else if(type.indexOf("CLOB")!=-1){
						//处理clob,nclob类型
						cb.setType("CLOB");						
					}else{
						//其他,date,
						cb.setType(type);
					}
				}
				cb.setDeft(rs.getString("DATA_DEFAULT"));
				cb.setNullable(rs.getString("NULLABLE"));
				tb.getCols().add(cb);
				tablemap.put(tablename, tb);
			}else{
				tb=(TableBean)tablemap.get(tablename);
				ColBean cb=new ColBean();
				cb.setName(rs.getString("COLUMN_NAME"));
				//模型化处理字段类型
				String type=rs.getString("DATA_TYPE");
				if(type!=null){					
					if(("NUMBER").equals(type)){
						//处理int,integer,smallint,decimal,number,numeric
						String length=rs.getString("DATA_PRECISION");
						String scale=rs.getString("DATA_SCALE");
						if(length==null&&"0".equals(scale)){
							//oracle中int等类型scale=0,使用decimal定义数据类型如果不加长度定义出来还是int,及scale还是为0
							cb.setType("INTEGER");
						}else{
							//注意:使用number定义数据类型时,如果不加上长度,存入系统表的数据类型是number,其PRECISION和scale都为null
							cb.setType("NUMBER");
							if(length!=null){
								cb.setLength(length);
							}							
							if(scale!=null){
								cb.setScale(rs.getString("DATA_SCALE"));
							}
						}						
					}else if("FLOAT".equals(type)){
						//处理float,real
						cb.setType("NUMBER");
						if(rs.getString("DATA_PRECISION")!=null){
							cb.setLength(rs.getString("DATA_PRECISION"));
						}
					}else if(type.indexOf("CHAR")!=-1){
						//处理varchar,varchar2,NCHAR,NVARCHAR2类型
						//oracle中char如果没有设定长度系统表中会默认为1,nchar为2,varchar2等必须指定长度
						if(type.indexOf("VARCHAR")!=-1){
							cb.setType("VARCHAR");
						}else{
							cb.setType("CHAR");
						}
						
						String length=rs.getString("DATA_LENGTH");
						if(type.startsWith("N")){
							//处理NCHAR,NVARCHAR2
							cb.setLength(Integer.parseInt(length)/2+"");
						}else{
							cb.setLength(length);
						}
					}else if(type.indexOf("TIMESTAMP")!=-1){
						//处理timestamp类型
						//oracle的timestamp类型在该表中存放为TIMESTAMP(6)
						cb.setType("TIMESTAMP");
					}else if(type.indexOf("CLOB")!=-1){
						//处理clob,nclob类型
						cb.setType("CLOB");						
					}else{
						//其他,date,
						cb.setType(type);
					}
				}
				cb.setDeft(rs.getString("DATA_DEFAULT"));
				cb.setNullable(rs.getString("NULLABLE"));
				//cb.setConstraint_type(rs.getString("CONSTRAINT_TYPE"));
				tb.getCols().add(cb);
			}
		}
		this.colsePsRs(ps, rs);
		//得到主键信息
		ps=conn.prepareStatement("select con.table_name,concol.column_name,con.constraint_name from user_constraints con,user_cons_columns concol where con.constraint_type ='P' and con.table_name=concol.table_name and con.constraint_name=concol.constraint_name and con.owner=concol.owner order by table_name");
		rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("table_name");
			TableBean tb=(TableBean)tablemap.get(tablename);
			if(tb!=null){
				//说明是导出表的主键关系
				List cols=tb.getCols();
				for(int i=0;i<cols.size();i++){
					ColBean col=(ColBean)cols.get(i);
					if(col.getName().equals(rs.getString("column_name"))){
						col.setConstraint_type("P");
					}
				}
			}
		}
		this.colsePsRs(ps, rs);
		//解析视图
		ps = conn.prepareStatement("select view_name from SYS.user_views");
		rs = ps.executeQuery();
		while(rs.next()){
			viewList.add(rs.getString("view_name"));
		}
		this.colsePsRs(ps, rs);
		//解析seq
		ps = conn.prepareStatement("select sequence_name from SYS.User_Sequences");
		rs = ps.executeQuery();
		while(rs.next()){
			seqList.add(rs.getString("sequence_name"));
		}
		this.colsePsRs(ps, rs);
		//解析存储过程
		ps = conn.prepareStatement("select distinct NAME from user_source where TYPE='PROCEDURE'");
		rs = ps.executeQuery();
		while(rs.next()){
			procList.add(rs.getString("NAME"));
		}
		this.colsePsRs(ps, rs);
	}
	/**
	 * 解析db2数据库
	 * @param username
	 * @param tablemap
	 * @return
	 * @throws SQLException
	 */
	public void db2Parse(DbBean dbbean) throws SQLException{
		//解析表
		/*
		 * db2在建表时如果没有设定默认长度,在系统表SYSCAT.COLUMNS中会出现默认的长度,比如decimal默认为5.
		 * 系统表SYSCAT.COLUMNS中timestamp字段长度为10而不是实际的26,
		 * */
		Map tablemap=dbbean.getTablemap();
		List viewList=dbbean.getViewList();
		List seqList=dbbean.getSeqList();
		List procList=dbbean.getProcList();
		String username=dbbean.getUsername().toUpperCase();
		PreparedStatement ps=null;
		ps = conn.prepareStatement("select col.tabname, col.colname,col.typename,col.length,col.scale,col.default,col.nulls from SYSCAT.COLUMNS col,SYSCAT.TABLES tab where col.tabname=tab.tabname and tab.type='T' and tab.tabschema = col.tabschema and col.tabschema  =?");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("tabname");
			TableBean tb=null;
			if(tablemap.get(tablename)==null){
				tb=new TableBean();
				tb.setName(tablename);
				ColBean cb=new ColBean();
				cb.setName(rs.getString("colname"));				
				String type=rs.getString("typename");
				if(type!=null){
					if(type.indexOf("INT")!=-1){
						//int,BIGINT,SMALLINT
						cb.setType("INTEGER");
					}else if("DECIMAL".equals(type)||"DOUBLE".equals(type)||"REAL".equals(type)){
						//decimal,double,real
						//db2数据库中没有填写长度信息也会存放默认的长度信息而不会为空
						cb.setType("NUMBER");
						cb.setLength(rs.getString("length"));
						cb.setScale(rs.getString("scale"));
					}else if(type.indexOf("VARCHAR")!=-1){
						//VARCHAR
						cb.setType("VARCHAR");
						cb.setLength(rs.getString("length"));
					}else if("CHARACTER".equals(type)){
						//CHARACTER
						cb.setType("CHAR");
						cb.setLength(rs.getString("length"));
					}else{
						cb.setType(type);
					}
				}				
				cb.setDeft(rs.getString("default"));
				cb.setNullable(rs.getString("nulls"));
				tb.getCols().add(cb);
				tablemap.put(tablename, tb);
			}else{
				tb=(TableBean)tablemap.get(tablename);
				ColBean cb=new ColBean();
				cb.setName(rs.getString("colname"));				
				String type=rs.getString("typename");
				if(type!=null){
					if(type.indexOf("INT")!=-1){
						//int,BIGINT,SMALLINT
						cb.setType("INTEGER");
					}else if("DECIMAL".equals(type)||"DOUBLE".equals(type)||"REAL".equals(type)){
						//decimal,double,real
						//db2数据库中没有填写长度信息也会存放默认的长度信息而不会为空
						cb.setType("NUMBER");
						cb.setLength(rs.getString("length"));
						cb.setScale(rs.getString("scale"));
					}else if(type.indexOf("VARCHAR")!=-1){
						//VARCHAR
						cb.setType("VARCHAR");
						cb.setLength(rs.getString("length"));
					}else if("CHARACTER".equals(type)){
						//CHARACTER
						cb.setType("CHAR");
						cb.setLength(rs.getString("length"));
					}else{
						cb.setType(type);
					}
				}
				cb.setDeft(rs.getString("default"));
				cb.setNullable(rs.getString("nulls"));
				tb.getCols().add(cb);
			}
		}
		this.colsePsRs(ps, rs);
		
		//得到主键信息
		ps=conn.prepareStatement("select tab.tabname,con.colname,tab.constname from SYSCAT.TABCONST tab,SYSCAT.KEYCOLUSE con where tab.type ='P' and tab.tabname=con.tabname and tab.constname=con.constname and tab.tabschema=con.tabschema order by tabname");
		rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("tabname");
			TableBean tb=(TableBean)tablemap.get(tablename);
			if(tb!=null){
				//说明是导出表的主键关系
				List cols=tb.getCols();
				for(int i=0;i<cols.size();i++){
					ColBean col=(ColBean)cols.get(i);
					if(col.getName().equals(rs.getString("colname"))){
						col.setConstraint_type("P");
					}
				}
			}
		}
		this.colsePsRs(ps, rs);
		
		//解析视图
		ps = conn.prepareStatement("select name from SYSIBM.SYSVIEWS where creator = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			viewList.add(rs.getString("name"));
		}
		this.colsePsRs(ps, rs);
		
		//解析seq
		ps = conn.prepareStatement("select seqname from SYSIBM.SYSSEQUENCES where seqschema = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			seqList.add(rs.getString("seqname"));
		}
		this.colsePsRs(ps, rs);
		
		//解析存储过程
		ps = conn.prepareStatement("select procname from syscat.procedures where procschema = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			procList.add(rs.getString("procname"));
		}
		this.colsePsRs(ps, rs);
	}
	/**
	 * 建立连接
	 * @param dbType
	 * @param url
	 * @param dbname
	 * @param username
	 * @param password
	 */
	private boolean openConnection(int dbtype,String url,String dbname,String username,String password){
		try{
			
			if(dbtype==0){
				//oracle
				Class.forName("oracle.jdbc.driver.OracleDriver"); 
			}else if(dbtype==1){
				//db2
				Class.forName("com.ibm.db2.jcc.DB2Driver");
			}	
			conn = DriverManager.getConnection(url,username,password); 
			conn.setAutoCommit(false);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			log.info("openConnection",e);
			this.setErrmsg("连接数据库出现错误,请重新输入正确的URL,用户名,密码");
			return false;
		}
	}
	/**
	 * 关闭连接
	 *
	 */
	private void closeConnection(){
		try{
			if(conn!=null){
				conn.commit();
				conn.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.info("closeConnection",e);
			this.setErrmsg("关闭数据库连接异常");
		}
	}
	
	public void colsePsRs(PreparedStatement ps,ResultSet rs){
		try{
			ps.close();
			rs.close();
		}catch(Exception e){
			e.printStackTrace();
			log.info("colsePsRs",e);
		}
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
