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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: liruilin
 * @����: 2011-1-4 
 * @����: ���ݿ�Աȹ���-xml����
 * 
 * �޸�
 * �޸���:liruilin
 * �޸�ʱ��:2011-07-15
 * ����:Ϊ��ʵ��oracle��db2�Ա�,���ٹ�ע�����ֶ����Ͷ������ģ��
 * 
 */
public class ExportXMLService {

	private Connection conn;
	private String errmsg;
	protected transient final Log log = LogFactory.getLog(getClass());
	public Document exportXml(String url,String username,String password,int dbtype,String dbname){
		if(url==null||username==null||password==null){
			setErrmsg("url,username,password�����д�");
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
	 * ����׼������
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
			this.setErrmsg("��ѯ���ݿ���ִ���,���ʵ���û�ӵ��dbaȨ��");
		}
		return doc;
	}
	/**
	 * �����ڴ���Ϣдxml
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
	 * ����oracle���ݿ�
	 * @param username
	 * @param tablemap
	 * @return
	 * @throws SQLException
	 */
	public void oracleParse(DbBean dbbean) throws SQLException{
		//������
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
				//ģ�ͻ������ֶ�����
				String type=rs.getString("DATA_TYPE");
				if(type!=null){					
					if(("NUMBER").equals(type)){
						//����int,integer,smallint,decimal,number,numeric
						String length=rs.getString("DATA_PRECISION");
						String scale=rs.getString("DATA_SCALE");
						if(length==null&&"0".equals(scale)){
							//oracle��int������scale=0,ʹ��decimal������������������ӳ��ȶ����������int,��scale����Ϊ0
							cb.setType("INTEGER");
						}else{
							//ע��:ʹ��number������������ʱ,��������ϳ���,����ϵͳ�������������number,��PRECISION��scale��Ϊnull
							cb.setType("NUMBER");
							if(length!=null){
								cb.setLength(length);
							}							
							if(scale!=null){
								cb.setScale(rs.getString("DATA_SCALE"));
							}
						}						
					}else if("FLOAT".equals(type)){
						//����float,real
						cb.setType("NUMBER");
						if(rs.getString("DATA_PRECISION")!=null){
							cb.setLength(rs.getString("DATA_PRECISION"));
						}
					}else if(type.indexOf("CHAR")!=-1){
						//����varchar,varchar2,NCHAR,NVARCHAR2����
						//oracle��char���û���趨����ϵͳ���л�Ĭ��Ϊ1,ncharΪ2,varchar2�ȱ���ָ������
						if(type.indexOf("VARCHAR")!=-1){
							cb.setType("VARCHAR");
						}else{
							cb.setType("CHAR");
						}
						
						String length=rs.getString("DATA_LENGTH");
						if(type.startsWith("N")){
							//����NCHAR,NVARCHAR2
							cb.setLength(Integer.parseInt(length)/2+"");
						}else{
							cb.setLength(length);
						}
					}else if(type.indexOf("TIMESTAMP")!=-1){
						//����timestamp����
						//oracle��timestamp�����ڸñ��д��ΪTIMESTAMP(6)
						cb.setType("TIMESTAMP");
					}else if(type.indexOf("CLOB")!=-1){
						//����clob,nclob����
						cb.setType("CLOB");						
					}else{
						//����,date,
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
				//ģ�ͻ������ֶ�����
				String type=rs.getString("DATA_TYPE");
				if(type!=null){					
					if(("NUMBER").equals(type)){
						//����int,integer,smallint,decimal,number,numeric
						String length=rs.getString("DATA_PRECISION");
						String scale=rs.getString("DATA_SCALE");
						if(length==null&&"0".equals(scale)){
							//oracle��int������scale=0,ʹ��decimal������������������ӳ��ȶ����������int,��scale����Ϊ0
							cb.setType("INTEGER");
						}else{
							//ע��:ʹ��number������������ʱ,��������ϳ���,����ϵͳ�������������number,��PRECISION��scale��Ϊnull
							cb.setType("NUMBER");
							if(length!=null){
								cb.setLength(length);
							}							
							if(scale!=null){
								cb.setScale(rs.getString("DATA_SCALE"));
							}
						}						
					}else if("FLOAT".equals(type)){
						//����float,real
						cb.setType("NUMBER");
						if(rs.getString("DATA_PRECISION")!=null){
							cb.setLength(rs.getString("DATA_PRECISION"));
						}
					}else if(type.indexOf("CHAR")!=-1){
						//����varchar,varchar2,NCHAR,NVARCHAR2����
						//oracle��char���û���趨����ϵͳ���л�Ĭ��Ϊ1,ncharΪ2,varchar2�ȱ���ָ������
						if(type.indexOf("VARCHAR")!=-1){
							cb.setType("VARCHAR");
						}else{
							cb.setType("CHAR");
						}
						
						String length=rs.getString("DATA_LENGTH");
						if(type.startsWith("N")){
							//����NCHAR,NVARCHAR2
							cb.setLength(Integer.parseInt(length)/2+"");
						}else{
							cb.setLength(length);
						}
					}else if(type.indexOf("TIMESTAMP")!=-1){
						//����timestamp����
						//oracle��timestamp�����ڸñ��д��ΪTIMESTAMP(6)
						cb.setType("TIMESTAMP");
					}else if(type.indexOf("CLOB")!=-1){
						//����clob,nclob����
						cb.setType("CLOB");						
					}else{
						//����,date,
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
		//�õ�������Ϣ
		ps=conn.prepareStatement("select con.table_name,concol.column_name,con.constraint_name from user_constraints con,user_cons_columns concol where con.constraint_type ='P' and con.table_name=concol.table_name and con.constraint_name=concol.constraint_name and con.owner=concol.owner order by table_name");
		rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("table_name");
			TableBean tb=(TableBean)tablemap.get(tablename);
			if(tb!=null){
				//˵���ǵ������������ϵ
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
		//������ͼ
		ps = conn.prepareStatement("select view_name from SYS.user_views");
		rs = ps.executeQuery();
		while(rs.next()){
			viewList.add(rs.getString("view_name"));
		}
		this.colsePsRs(ps, rs);
		//����seq
		ps = conn.prepareStatement("select sequence_name from SYS.User_Sequences");
		rs = ps.executeQuery();
		while(rs.next()){
			seqList.add(rs.getString("sequence_name"));
		}
		this.colsePsRs(ps, rs);
		//�����洢����
		ps = conn.prepareStatement("select distinct NAME from user_source where TYPE='PROCEDURE'");
		rs = ps.executeQuery();
		while(rs.next()){
			procList.add(rs.getString("NAME"));
		}
		this.colsePsRs(ps, rs);
	}
	/**
	 * ����db2���ݿ�
	 * @param username
	 * @param tablemap
	 * @return
	 * @throws SQLException
	 */
	public void db2Parse(DbBean dbbean) throws SQLException{
		//������
		/*
		 * db2�ڽ���ʱ���û���趨Ĭ�ϳ���,��ϵͳ��SYSCAT.COLUMNS�л����Ĭ�ϵĳ���,����decimalĬ��Ϊ5.
		 * ϵͳ��SYSCAT.COLUMNS��timestamp�ֶγ���Ϊ10������ʵ�ʵ�26,
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
						//db2���ݿ���û����д������ϢҲ����Ĭ�ϵĳ�����Ϣ������Ϊ��
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
						//db2���ݿ���û����д������ϢҲ����Ĭ�ϵĳ�����Ϣ������Ϊ��
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
		
		//�õ�������Ϣ
		ps=conn.prepareStatement("select tab.tabname,con.colname,tab.constname from SYSCAT.TABCONST tab,SYSCAT.KEYCOLUSE con where tab.type ='P' and tab.tabname=con.tabname and tab.constname=con.constname and tab.tabschema=con.tabschema order by tabname");
		rs = ps.executeQuery();
		while(rs.next()){
			String tablename=rs.getString("tabname");
			TableBean tb=(TableBean)tablemap.get(tablename);
			if(tb!=null){
				//˵���ǵ������������ϵ
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
		
		//������ͼ
		ps = conn.prepareStatement("select name from SYSIBM.SYSVIEWS where creator = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			viewList.add(rs.getString("name"));
		}
		this.colsePsRs(ps, rs);
		
		//����seq
		ps = conn.prepareStatement("select seqname from SYSIBM.SYSSEQUENCES where seqschema = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			seqList.add(rs.getString("seqname"));
		}
		this.colsePsRs(ps, rs);
		
		//�����洢����
		ps = conn.prepareStatement("select procname from syscat.procedures where procschema = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		while(rs.next()){
			procList.add(rs.getString("procname"));
		}
		this.colsePsRs(ps, rs);
	}
	/**
	 * ��������
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
			this.setErrmsg("�������ݿ���ִ���,������������ȷ��URL,�û���,����");
			return false;
		}
	}
	/**
	 * �ر�����
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
			this.setErrmsg("�ر����ݿ������쳣");
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
