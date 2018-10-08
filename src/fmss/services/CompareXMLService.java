package fmss.services;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.common.util.ColBean;
import fmss.common.util.DbBean;
import fmss.common.util.TableBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: liruilin
 * @日期: 2011-1-4 
 * @描述: 数据库对比工具-xml导入对比
 * 
 * 修改
 * 修改人:liruilin
 * 修改时间:2011-07-15
 * 描述:为了实现oracle和db2对比,不再关注具体字段类型而抽象成模型
 */
public class CompareXMLService {
	private Connection conn;
	private String errmsg;
	protected transient final Log log = LogFactory.getLog(getClass());
	
	public DbBean paresXml(String path) {
		DbBean db=null;
		SAXReader xmlReader = new SAXReader();
		try {
			Document doc = xmlReader.read(new File(path));
			Element root = doc.getRootElement();   
			
				db=new DbBean();
				//建立dbBean
				createDbbean(root,db);
				/*
				Map tabmap=db.getTablemap();
				for(Iterator j=tabmap.keySet().iterator();j.hasNext();){
					TableBean tb=(TableBean)tabmap.get((String)j.next());
					System.out.println(tb.getName());
					List list=tb.getCols();
					
					for(int k=0;k<list.size();k++){
						ColBean col=(ColBean)list.get(k);
						System.out.println("\t"+col.getName());
						System.out.println("\t"+"\t"+col.getType());
						System.out.println("\t"+"\t"+col.getLength());
						System.out.println("\t"+"\t"+col.getScale());
						System.out.println("\t"+"\t"+col.getDeft());
						System.out.println("\t"+"\t"+col.getNullable());
					}		
								
				}
				for(int j=0;j<db.getViewList().size();j++){
					System.out.println("view:"+db.getViewList().get(j));
				}
				for(int j=0;j<db.getSeqList().size();j++){
					System.out.println("seq:"+db.getSeqList().get(j));
				}
				for(int j=0;j<db.getProcList().size();j++){
					System.out.println("proc:"+db.getProcList().get(j));
				}
				*/
		} catch (DocumentException e) {
			e.printStackTrace();
			log.info("paresXml",e);
		}
		return db;
	}

	private void createDbbean(Element element, DbBean db) {
		Map tablemap=db.getTablemap();
		List viewList=db.getViewList();
		List seqList=db.getSeqList();
		List procList=db.getProcList();
		db.setDbname(element.attributeValue("dbname").trim());
		db.setDbType("db2".equals(element.attributeValue("src_db_type").trim())?1:0);
		//处理table
		for ( Iterator i = element.elementIterator("table"); i.hasNext(); ) {
			TableBean tb=new TableBean();
			//table
			Element tbElement=(Element)i.next();
			tb.setName(tbElement.attributeValue("tablename").trim());
			//cols
			Element colsElement = tbElement.element("columns");
			List colList=tb.getCols();
			for ( Iterator j = colsElement.elementIterator("column"); j.hasNext(); ) {
				ColBean col=new ColBean();
				//col
				Element colElement =(Element)j.next();
				col.setName(colElement.element("columnname").getTextTrim());
				col.setType(colElement.element("type").getTextTrim());
				if(colElement.element("length")!=null){
					col.setLength(colElement.element("length").getTextTrim());
				}
				if(colElement.element("scale")!=null){
					col.setScale(colElement.element("scale").getTextTrim());
				}
				if(colElement.element("default")!=null){
					col.setDeft(colElement.element("default").getTextTrim());
				}
				if(colElement.element("constraint_type")!=null){
					col.setConstraint_type(colElement.element("constraint_type").getTextTrim());
				}
				col.setNullable(colElement.element("nullable").getTextTrim());
				colList.add(col);
			}	
			tablemap.put(tb.getName(), tb);
		}
		//处理view
		for ( Iterator j = element.elementIterator("view"); j.hasNext(); ) {
			//System.out.println("add to view "+j);
			viewList.add(((Element)j.next()).getTextTrim());
		}
		//处理seq
		for ( Iterator j = element.elementIterator("sequence"); j.hasNext(); ) {
			seqList.add(((Element)j.next()).getTextTrim());
		}
		//处理proc
		for ( Iterator j = element.elementIterator("procedure"); j.hasNext(); ) {
			procList.add(((Element)j.next()).getTextTrim());
		}
	}

	public DbBean doCompare(String url,String username,String password,DbBean db) {
		if(url==null||username==null||password==null){
			setErrmsg("url,username,password参数有错");
			return null;
		}
		int dbtype=0;
		String dbname=null;
		if(url.indexOf("oracle")!=-1){
			dbtype=0;
			dbname=url.substring(url.lastIndexOf(":")+1,url.length());
			db.setTarget_dbtype(dbtype);
			db.setTarget_dbname(dbname);
		}else if(url.indexOf("db2")!=-1){
			dbtype=1;
			dbname=url.substring(url.lastIndexOf("/")+1,url.length());
			db.setTarget_dbtype(dbtype);
			db.setTarget_dbname(dbname);
		}
		boolean flag=openConnection(dbtype, url, dbname, username, password);		
		if(flag){
			db=compare(db,conn);	
			closeConnection();
		}
		return db;
	}

	private DbBean compare(DbBean db, Connection conn) {		
		Map tabmap=db.getTablemap();
		List viewList=db.getViewList();
		List seqList=db.getSeqList();
		//List procList=db.getProcList();
		
		List lostTable=null;
		List lostView=null;
		List lostSeq=null;
		//List lostProc=null;
		
		PreparedStatement ps=null;
		ResultSet rs =null;
		//1---处理表,没有dba权限只能一个个表查询
		for(Iterator i=tabmap.keySet().iterator();i.hasNext();){
			//System.out.println(i);
			String tabname=(String)i.next();
			TableBean tb=(TableBean)tabmap.get(tabname);
			try {
				//尝试查该表信息,如报错则说明缺表
				ps=conn.prepareStatement("select * from "+tabname);
				
				rs = ps.executeQuery();
				
				Map colmap=new HashMap();
				Map difTabMap=null;
				Map difcols=null;
				ResultSetMetaData rsmd = rs.getMetaData();
				
				//将该表在库中的列的信息存入colmap,用于对比
				for(int j=1;j<=rsmd.getColumnCount();j++){
					String colName=rsmd.getColumnName(j);
					String colType=rsmd.getColumnTypeName(j);
					String colLength=rsmd.getPrecision(j)+"";
					String colScale=rsmd.getScale(j)+"";
					String colNullable=rsmd.isNullable(j)==ResultSetMetaData.columnNoNulls?"N":"Y";
					ColBean col=new ColBean();
					col.setName(colName);
					col.setType(colType);
					col.setLength(colLength);
					col.setScale(colScale);
					col.setNullable(colNullable);
					colmap.put(col.getName(), col);
				}
				this.colsePsRs(ps, rs);
				
				//对比xml和db中该表列信息
				for(int j=0;j<tb.getCols().size();j++){
					//xml中该表col对象
					ColBean xmlcol=(ColBean)tb.getCols().get(j);
					String colname=xmlcol.getName();
					//db中该表col对象
					ColBean dbcol=(ColBean)colmap.get(colname);
					difTabMap=db.getDifTable();
					difcols=tb.getDifcols();
					if(dbcol==null){
						//db中缺少该列			
						xmlcol.getDiffType().add("0");
						//将有差别的列信息加入difcols
						difcols.put(xmlcol.getName(),xmlcol);
													
						//将有差别的tb信息存入map
						if(difTabMap.get(tabname)==null){
							difTabMap.put(tabname, tb);
						}
					}else{
						//db中该列存在
						String xmltype=xmlcol.getType();
						//字段类型不同
						//oracle中desc得到的类型为int时ResultSetMetaData可能为number
						//db2中LONG VARGRAPHIC类型ResultSetMetaData得到可能是LONG GRAPHIC
						if(("INTEGER".equals(xmltype)
								&&dbcol.getType().indexOf("INT")==-1
								&&!"NUMBER".equals(dbcol.getType())
								)
							||("NUMBER".equals(xmltype)
								&&!"DECIMAL".equals(dbcol.getType())
								&&!"DOUBLE".equals(dbcol.getType())
								&&!"FLOAT".equals(dbcol.getType())
								&&!"NUMERIC".equals(dbcol.getType())
								&&!"REAL".equals(dbcol.getType()))
								&&!"NUMBER".equals(dbcol.getType())
							||("VARCHAR".equals(xmltype))
								&&!"VARCHAR2".equals(dbcol.getType())
								&&!"VARCHAR".equals(dbcol.getType())
							||("LONG VARGRAPHIC".equals(xmltype)
								&&!"LONG VARGRAPHIC".equals(dbcol.getType())
								&&!"LONG GRAPHIC".equals(dbcol.getType()))
							){
							//处理数字字段类型不同
							xmlcol.getDiffType().add("1");
							//将数据库中字段类型保存
							xmlcol.setErr_type(dbcol.getType());
							//将有差别的列信息加入difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}
														
							//将有差别的tb信息存入map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}else if(!"INTEGER".equals(xmltype)
								&&!"NUMBER".equals(xmltype)
								&&!"VARCHAR".equals(xmltype)
								&&!"LONG VARGRAPHIC".equals(xmltype)
								&&!xmltype.equals(dbcol.getType())){	
							//处理其他字段类型不同
							xmlcol.getDiffType().add("1");
							//将数据库中字段类型保存
							xmlcol.setErr_type(dbcol.getType());
							//将有差别的列信息加入difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}
														
							//将有差别的tb信息存入map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}
						//比对列长度						
						if("NUMBER".equals(xmltype)||xmltype.indexOf("CHAR")!=-1){
							//NUMBER,CHAR,VARCHAR
							if((xmlcol.getLength()!=null&&!xmlcol.getLength().equals(dbcol.getLength()))
								||(xmlcol.getScale()!=null&&!xmlcol.getScale().equals(dbcol.getScale()))){
								
								xmlcol.getDiffType().add("2");
								//将数据库中字段长度保存
								xmlcol.setErr_length(dbcol.getLength());
								xmlcol.setErr_scale(dbcol.getScale());
								//将有差别的列信息加入difcols
								if(difcols.get(xmlcol.getName())==null){
									difcols.put(xmlcol.getName(), xmlcol);
								}						
								//将有差别的tb信息存入map
								if(difTabMap.get(tabname)==null){
									difTabMap.put(tabname, tb);
								}
							}							
						}
						//对比非空属性
						if(!xmlcol.getNullable().equals(dbcol.getNullable())){							
							xmlcol.getDiffType().add("3");
							//将数据库中字段非空属性保存
							xmlcol.setErr_nullable(dbcol.getNullable());
							//将有差别的列信息加入difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}						
							//将有差别的tb信息存入map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}						
					}
				}
			} catch (SQLException e) {
				//没有该表
				lostTable=db.getLostTable();
				if(lostTable==null){
					lostTable=new ArrayList();
					db.setLostTable(lostTable);
				}
				lostTable.add(tb);
			}
		}
		//2---处理view
		for(int i=0;i<viewList.size();i++){
			String viewname=(String)viewList.get(i);
			try {
				ps=conn.prepareStatement("select * from "+viewname);
				rs = ps.executeQuery();
				this.colsePsRs(ps, rs);
			}catch(Exception e){
				lostView=db.getLostView();
				if(lostView==null){
					lostView=new ArrayList();
					db.setLostView(lostView);
				}
				lostView.add(viewname);
			}
		}
		//3---处理seq,需要分数据库		
		for(int i=0;i<seqList.size();i++){
			String seqname=(String)seqList.get(i);
			try {
				if(db.getTarget_dbtype()==0){
					ps=conn.prepareStatement("select "+seqname+".nextval from dual");
				}else if(db.getTarget_dbtype()==1){
					ps=conn.prepareStatement("select NEXTVAL FOR "+seqname+" from sysibm.sysdummy1");
				}
				rs = ps.executeQuery();	
				this.colsePsRs(ps, rs);
			}catch(Exception e){
				//db2中如果序列是系统自动生成的序列,则不能使用上述语句查询,即库中存在该序列也会报错
				String err=e.getMessage();
				if(err.indexOf("DB2 SQL error: SQLCODE: -20142, SQLSTATE: 428FB,")==-1){
					lostSeq=db.getLostSeq();
					if(lostSeq==null){
						lostSeq=new ArrayList();
						db.setLostSeq(lostSeq);
					}
					lostSeq.add(seqname);
				}
			}
		}		
		
		//4---处理proc
		/*
		for(int i=0;i<procList.size();i++){
			String procname=(String)procList.get(i);
			try {
				ps=conn.prepareStatement("");
				ps.setString(1, procname);
				rs = ps.executeQuery();
				this.colsePsRs(ps, rs);
			}catch(Exception e){
				lostProc=db.getLostProc();
				if(lostProc==null){
					lostProc=new ArrayList();
					db.setLostProc(lostProc);
				}
				lostProc.add(procname);
			}
		}		
		*/
		return db;
	}	
	
	/**
	 * 得到差异xml
	 * 统计了缺失表,视图,序列
	 * 统计了表结构不同(缺列,列类型不同,列长度不同,非空属性)
	 * @param db
	 */
	public Document getXmlResult(DbBean dbbean) {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("GBK");
		Element db = doc.addElement("db");//root  
		
		db.addAttribute("dbname", ""+dbbean.getDbname());
		Map difTable=dbbean.getDifTable();
		
		List lostTable=dbbean.getLostTable();
		List lostView=dbbean.getLostView();
		List lostSeq=dbbean.getLostSeq();
		List lostProc=dbbean.getLostProc();
		//处理缺失表,view,seq,proc
		for(int j=0;j<lostTable.size();j++){
			Element view=db.addElement("缺失表");
			view.addText(((TableBean)lostTable.get(j)).getName());
		}
		for(int j=0;j<lostView.size();j++){
			Element view=db.addElement("缺失视图");
			view.addText((String)lostView.get(j));
		}
		for(int j=0;j<lostSeq.size();j++){
			Element view=db.addElement("缺失序列");
			view.addText((String)lostSeq.get(j));
		}
		for(int j=0;j<lostProc.size();j++){
			Element view=db.addElement("缺失存储过程");
			view.addText((String)lostProc.get(j));
		}
		
		Iterator iter=difTable.keySet().iterator();
		while(iter.hasNext()){
			TableBean tb=(TableBean)difTable.get(iter.next());
			Element table=db.addElement("表结构不同");
			table.addAttribute("tablename",tb.getName());
			
			Element cols=table.addElement("列不同");			
			Map difcols=(Map)tb.getDifcols();
			Iterator colIter=difcols.keySet().iterator();
						
			while(colIter.hasNext()){
				ColBean col=(ColBean)difcols.get(colIter.next());
				Element column=cols.addElement("column");
				Element colname=column.addElement("columnname");
				colname.addText(col.getName());
				Element colType=column.addElement("standard_columnType");
				colType.addText(col.getType());
				List difType=(List)col.getDiffType();
				for(int i=0;i<difType.size();i++){
					Element coltype=column.addElement("diffType");
					String type=(String)difType.get(i);
					if("0".equals(type)){
						coltype.addText("缺失该列。");
					}else if("1".equals(type)){
						coltype.addText("该列类型不同:标准xml中type为"+col.getType()+",数据库中type为"+col.getErr_type()+"。");
					}else if("2".equals(type)){
						coltype.addText("该列长度不同(警告:如果db2中建表时NUMBER等类型未设定长度,可能出现误判):标准xml中length为"+col.getLength());
						if(col.getScale()!=null){
							coltype.addText(" 精度为"+col.getScale()+",数据库中length为"+col.getErr_length()+" 精度为"+col.getScale()+"。");
						}else{
							coltype.addText(",数据库中length为"+col.getErr_length()+"。");
						}
					}else if("3".equals(type)){
						coltype.addText("该列非空属性不同:标准xml中nullable为"+col.getNullable()+",数据库中nullable为"+col.getErr_nullable()+"。");
					}
				}
			}
		}
		return doc;

	}
	
	
	/**
	 * 得到更新sql
	 * 目前只处理了table的更新(新增,改表结构)
	 * 修改表结构(新增列,修改列)
	 * 修改列是如果修改前后列数据类型不兼容或列长度改短sql会报错
	 * 修改列时defalut,nullable目前不考虑
	 * @param db
	 */
	public String getSqlResult(DbBean dbbean) {
		StringBuffer bf=new StringBuffer("--差异sql");		
		bf.append("\r\n");
		bf.append("--目前没有处理view,seq,proc");
		bf.append("\r\n");
		Map difTable=dbbean.getDifTable();
		int dbType=dbbean.getTarget_dbtype();
		List lostTable=dbbean.getLostTable();
		//List lostView=dbbean.getLostView();
		//List lostSeq=dbbean.getLostSeq();
		//List lostProc=dbbean.getLostProc();
		List pkList;
		for(int j=0;j<lostTable.size();j++){
			TableBean tb=(TableBean)lostTable.get(j);
			bf.append("create table ").append(tb.getName()).append(" (");
			List colList=tb.getCols();
			pkList=new ArrayList();
			for(int k=0;k<colList.size();k++){
				ColBean col=(ColBean)colList.get(k);
				
				bf.append(col.getName()).append(" ");
				if(dbType==1&&"NUMBER".equals(col.getType())){
					//db2中不能使用NUMBER类型建表,所以用NUM
					bf.append("NUM");
				}else{
					bf.append(col.getType());
				}
				//长度
				if(col.getType().indexOf("CHAR")!=-1||col.getType().indexOf("NUM")!=-1){
					if(col.getLength()!=null){
						bf.append("(").append(col.getLength());
						if(col.getType().indexOf("CHAR")==-1&&col.getScale()!=null&&!col.getScale().equals("")){
							bf.append(",").append(col.getScale());
						}
						bf.append(")");
					}
				}
				//默认值
				if(col.getDeft()!=null){
					//if(col.getType().indexOf("INT")!=-1||col.getType().indexOf("DEC")!=-1||col.getType().indexOf("NUM")!=-1){
						bf.append(" DEFAULT ").append(col.getDeft());
					//}else{
					//	bf.append(" DEFAULT '").append(col.getDeft()).append("'");
					//}
				}
				//非空
				if("N".equals(col.getNullable())){
					bf.append(" not null");
				}
				if(k!=colList.size()-1){
					bf.append(",");
				}
				if("P".equals(col.getConstraint_type())){
					//主键
					pkList.add(col.getName());
				}
				
			}
			bf.append(");");
			bf.append("\r\n");
			//处理主键
			if(pkList.size()>0){
				bf.append("alter table ").append(tb.getName()).append("  add primary key(");
				for(int k=0;k<pkList.size();k++){
					bf.append(pkList.get(k));
					if(k!=pkList.size()-1){
						bf.append(",");
					}
				}
				bf.append(");");
				bf.append("\r\n");
			}
		}
		/*暂不生成view,seq,proc缺失sql
		for(int j=0;j<lostView.size();j++){
			
		}
		for(int j=0;j<lostSeq.size();j++){
			
		}
		for(int j=0;j<lostProc.size();j++){
			
		}
		*/
		
		//处理不同表
		Iterator iter=difTable.keySet().iterator();
		while(iter.hasNext()){
			TableBean tb=(TableBean)difTable.get(iter.next());
			Map difcols=(Map)tb.getDifcols();
			Iterator colIter=difcols.keySet().iterator();
			while(colIter.hasNext()){
				ColBean col=(ColBean)difcols.get(colIter.next());
				List difType=(List)col.getDiffType();
				for(int i=0;i<difType.size();i++){
					String type=(String)difType.get(0);
					if("0".equals(type)){
						//缺列
						bf.append("alter table ").append(tb.getName()).append(" add column ").append(col.getName()).append(" ").append(col.getType());
						//长度
						if(col.getType().indexOf("CHAR")!=-1||col.getType().indexOf("NUM")!=-1){
							if(col.getLength()!=null){
								bf.append("(").append(col.getLength());
								if(col.getType().indexOf("CHAR")==-1&&col.getScale()!=null&&!col.getScale().equals("")){
									bf.append(",").append(col.getScale());
								}
								bf.append(")");
							}
						}
						//默认值
						if(col.getDeft()!=null){
							//if(col.getType().indexOf("INT")!=-1||col.getType().indexOf("DEC")!=-1||col.getType().indexOf("NUM")!=-1){
								bf.append(" DEFAULT ").append(col.getDeft());
							//}else{
								//bf.append(" DEFAULT '").append(col.getDeft()).append("'");
							//}
						}
						
						//非空
						if("N".equals(col.getNullable())){
							bf.append(" not null");
						}
						bf.append(";");
						bf.append("\r\n");
					}else if("1".equals(type)||"2".equals(type)){
						//type不同,length不同
						bf.append("alter table ").append(tb.getName());
						if(dbType==0){
							//oracle
							bf.append(" modify ").append(col.getName()).append(" ").append(col.getType());
						}else if(dbType==1){
							//db1
							bf.append(" alter ").append(col.getName()).append(" set data type ").append(col.getType());				
						}
						//长度
						if(col.getType().indexOf("CHAR")!=-1||col.getType().indexOf("NUM")!=-1){
							if(col.getLength()!=null){
								bf.append("(").append(col.getLength());
								if(col.getType().indexOf("CHAR")==-1&&col.getScale()!=null&&!col.getScale().equals("")){
									bf.append(",").append(col.getScale());
								}
								bf.append(")");
							}
						}
						bf.append(";");
						bf.append("\r\n");
					}
				}
			}
		}
		return bf.toString();
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
			log.info("openConnection",e);
			e.printStackTrace();
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
				//System.out.println("close connection");
			}
		}catch(Exception e){
			log.info("closeConnection",e);
			e.printStackTrace();
			this.setErrmsg("关闭数据库连接异常");
		}
	}

	public void colsePsRs(PreparedStatement ps,ResultSet rs){
		try{
			ps.close();
			rs.close();
		}catch(Exception e){
			log.info("closeConnection",e);
			e.printStackTrace();
		}
	}
	
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errMsg) {
		this.errmsg = errMsg;
	}
	
}
