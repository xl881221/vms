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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: liruilin
 * @����: 2011-1-4 
 * @����: ���ݿ�Աȹ���-xml����Ա�
 * 
 * �޸�
 * �޸���:liruilin
 * �޸�ʱ��:2011-07-15
 * ����:Ϊ��ʵ��oracle��db2�Ա�,���ٹ�ע�����ֶ����Ͷ������ģ��
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
				//����dbBean
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
		//����table
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
		//����view
		for ( Iterator j = element.elementIterator("view"); j.hasNext(); ) {
			//System.out.println("add to view "+j);
			viewList.add(((Element)j.next()).getTextTrim());
		}
		//����seq
		for ( Iterator j = element.elementIterator("sequence"); j.hasNext(); ) {
			seqList.add(((Element)j.next()).getTextTrim());
		}
		//����proc
		for ( Iterator j = element.elementIterator("procedure"); j.hasNext(); ) {
			procList.add(((Element)j.next()).getTextTrim());
		}
	}

	public DbBean doCompare(String url,String username,String password,DbBean db) {
		if(url==null||username==null||password==null){
			setErrmsg("url,username,password�����д�");
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
		//1---�����,û��dbaȨ��ֻ��һ�������ѯ
		for(Iterator i=tabmap.keySet().iterator();i.hasNext();){
			//System.out.println(i);
			String tabname=(String)i.next();
			TableBean tb=(TableBean)tabmap.get(tabname);
			try {
				//���Բ�ñ���Ϣ,�籨����˵��ȱ��
				ps=conn.prepareStatement("select * from "+tabname);
				
				rs = ps.executeQuery();
				
				Map colmap=new HashMap();
				Map difTabMap=null;
				Map difcols=null;
				ResultSetMetaData rsmd = rs.getMetaData();
				
				//���ñ��ڿ��е��е���Ϣ����colmap,���ڶԱ�
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
				
				//�Ա�xml��db�иñ�����Ϣ
				for(int j=0;j<tb.getCols().size();j++){
					//xml�иñ�col����
					ColBean xmlcol=(ColBean)tb.getCols().get(j);
					String colname=xmlcol.getName();
					//db�иñ�col����
					ColBean dbcol=(ColBean)colmap.get(colname);
					difTabMap=db.getDifTable();
					difcols=tb.getDifcols();
					if(dbcol==null){
						//db��ȱ�ٸ���			
						xmlcol.getDiffType().add("0");
						//���в�������Ϣ����difcols
						difcols.put(xmlcol.getName(),xmlcol);
													
						//���в���tb��Ϣ����map
						if(difTabMap.get(tabname)==null){
							difTabMap.put(tabname, tb);
						}
					}else{
						//db�и��д���
						String xmltype=xmlcol.getType();
						//�ֶ����Ͳ�ͬ
						//oracle��desc�õ�������ΪintʱResultSetMetaData����Ϊnumber
						//db2��LONG VARGRAPHIC����ResultSetMetaData�õ�������LONG GRAPHIC
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
							//���������ֶ����Ͳ�ͬ
							xmlcol.getDiffType().add("1");
							//�����ݿ����ֶ����ͱ���
							xmlcol.setErr_type(dbcol.getType());
							//���в�������Ϣ����difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}
														
							//���в���tb��Ϣ����map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}else if(!"INTEGER".equals(xmltype)
								&&!"NUMBER".equals(xmltype)
								&&!"VARCHAR".equals(xmltype)
								&&!"LONG VARGRAPHIC".equals(xmltype)
								&&!xmltype.equals(dbcol.getType())){	
							//���������ֶ����Ͳ�ͬ
							xmlcol.getDiffType().add("1");
							//�����ݿ����ֶ����ͱ���
							xmlcol.setErr_type(dbcol.getType());
							//���в�������Ϣ����difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}
														
							//���в���tb��Ϣ����map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}
						//�ȶ��г���						
						if("NUMBER".equals(xmltype)||xmltype.indexOf("CHAR")!=-1){
							//NUMBER,CHAR,VARCHAR
							if((xmlcol.getLength()!=null&&!xmlcol.getLength().equals(dbcol.getLength()))
								||(xmlcol.getScale()!=null&&!xmlcol.getScale().equals(dbcol.getScale()))){
								
								xmlcol.getDiffType().add("2");
								//�����ݿ����ֶγ��ȱ���
								xmlcol.setErr_length(dbcol.getLength());
								xmlcol.setErr_scale(dbcol.getScale());
								//���в�������Ϣ����difcols
								if(difcols.get(xmlcol.getName())==null){
									difcols.put(xmlcol.getName(), xmlcol);
								}						
								//���в���tb��Ϣ����map
								if(difTabMap.get(tabname)==null){
									difTabMap.put(tabname, tb);
								}
							}							
						}
						//�Աȷǿ�����
						if(!xmlcol.getNullable().equals(dbcol.getNullable())){							
							xmlcol.getDiffType().add("3");
							//�����ݿ����ֶηǿ����Ա���
							xmlcol.setErr_nullable(dbcol.getNullable());
							//���в�������Ϣ����difcols
							if(difcols.get(xmlcol.getName())==null){
								difcols.put(xmlcol.getName(), xmlcol);
							}						
							//���в���tb��Ϣ����map
							if(difTabMap.get(tabname)==null){
								difTabMap.put(tabname, tb);
							}
						}						
					}
				}
			} catch (SQLException e) {
				//û�иñ�
				lostTable=db.getLostTable();
				if(lostTable==null){
					lostTable=new ArrayList();
					db.setLostTable(lostTable);
				}
				lostTable.add(tb);
			}
		}
		//2---����view
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
		//3---����seq,��Ҫ�����ݿ�		
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
				//db2�����������ϵͳ�Զ����ɵ�����,����ʹ����������ѯ,�����д��ڸ�����Ҳ�ᱨ��
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
		
		//4---����proc
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
	 * �õ�����xml
	 * ͳ����ȱʧ��,��ͼ,����
	 * ͳ���˱�ṹ��ͬ(ȱ��,�����Ͳ�ͬ,�г��Ȳ�ͬ,�ǿ�����)
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
		//����ȱʧ��,view,seq,proc
		for(int j=0;j<lostTable.size();j++){
			Element view=db.addElement("ȱʧ��");
			view.addText(((TableBean)lostTable.get(j)).getName());
		}
		for(int j=0;j<lostView.size();j++){
			Element view=db.addElement("ȱʧ��ͼ");
			view.addText((String)lostView.get(j));
		}
		for(int j=0;j<lostSeq.size();j++){
			Element view=db.addElement("ȱʧ����");
			view.addText((String)lostSeq.get(j));
		}
		for(int j=0;j<lostProc.size();j++){
			Element view=db.addElement("ȱʧ�洢����");
			view.addText((String)lostProc.get(j));
		}
		
		Iterator iter=difTable.keySet().iterator();
		while(iter.hasNext()){
			TableBean tb=(TableBean)difTable.get(iter.next());
			Element table=db.addElement("��ṹ��ͬ");
			table.addAttribute("tablename",tb.getName());
			
			Element cols=table.addElement("�в�ͬ");			
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
						coltype.addText("ȱʧ���С�");
					}else if("1".equals(type)){
						coltype.addText("�������Ͳ�ͬ:��׼xml��typeΪ"+col.getType()+",���ݿ���typeΪ"+col.getErr_type()+"��");
					}else if("2".equals(type)){
						coltype.addText("���г��Ȳ�ͬ(����:���db2�н���ʱNUMBER������δ�趨����,���ܳ�������):��׼xml��lengthΪ"+col.getLength());
						if(col.getScale()!=null){
							coltype.addText(" ����Ϊ"+col.getScale()+",���ݿ���lengthΪ"+col.getErr_length()+" ����Ϊ"+col.getScale()+"��");
						}else{
							coltype.addText(",���ݿ���lengthΪ"+col.getErr_length()+"��");
						}
					}else if("3".equals(type)){
						coltype.addText("���зǿ����Բ�ͬ:��׼xml��nullableΪ"+col.getNullable()+",���ݿ���nullableΪ"+col.getErr_nullable()+"��");
					}
				}
			}
		}
		return doc;

	}
	
	
	/**
	 * �õ�����sql
	 * Ŀǰֻ������table�ĸ���(����,�ı�ṹ)
	 * �޸ı�ṹ(������,�޸���)
	 * �޸���������޸�ǰ�����������Ͳ����ݻ��г��ȸĶ�sql�ᱨ��
	 * �޸���ʱdefalut,nullableĿǰ������
	 * @param db
	 */
	public String getSqlResult(DbBean dbbean) {
		StringBuffer bf=new StringBuffer("--����sql");		
		bf.append("\r\n");
		bf.append("--Ŀǰû�д���view,seq,proc");
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
					//db2�в���ʹ��NUMBER���ͽ���,������NUM
					bf.append("NUM");
				}else{
					bf.append(col.getType());
				}
				//����
				if(col.getType().indexOf("CHAR")!=-1||col.getType().indexOf("NUM")!=-1){
					if(col.getLength()!=null){
						bf.append("(").append(col.getLength());
						if(col.getType().indexOf("CHAR")==-1&&col.getScale()!=null&&!col.getScale().equals("")){
							bf.append(",").append(col.getScale());
						}
						bf.append(")");
					}
				}
				//Ĭ��ֵ
				if(col.getDeft()!=null){
					//if(col.getType().indexOf("INT")!=-1||col.getType().indexOf("DEC")!=-1||col.getType().indexOf("NUM")!=-1){
						bf.append(" DEFAULT ").append(col.getDeft());
					//}else{
					//	bf.append(" DEFAULT '").append(col.getDeft()).append("'");
					//}
				}
				//�ǿ�
				if("N".equals(col.getNullable())){
					bf.append(" not null");
				}
				if(k!=colList.size()-1){
					bf.append(",");
				}
				if("P".equals(col.getConstraint_type())){
					//����
					pkList.add(col.getName());
				}
				
			}
			bf.append(");");
			bf.append("\r\n");
			//��������
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
		/*�ݲ�����view,seq,procȱʧsql
		for(int j=0;j<lostView.size();j++){
			
		}
		for(int j=0;j<lostSeq.size();j++){
			
		}
		for(int j=0;j<lostProc.size();j++){
			
		}
		*/
		
		//����ͬ��
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
						//ȱ��
						bf.append("alter table ").append(tb.getName()).append(" add column ").append(col.getName()).append(" ").append(col.getType());
						//����
						if(col.getType().indexOf("CHAR")!=-1||col.getType().indexOf("NUM")!=-1){
							if(col.getLength()!=null){
								bf.append("(").append(col.getLength());
								if(col.getType().indexOf("CHAR")==-1&&col.getScale()!=null&&!col.getScale().equals("")){
									bf.append(",").append(col.getScale());
								}
								bf.append(")");
							}
						}
						//Ĭ��ֵ
						if(col.getDeft()!=null){
							//if(col.getType().indexOf("INT")!=-1||col.getType().indexOf("DEC")!=-1||col.getType().indexOf("NUM")!=-1){
								bf.append(" DEFAULT ").append(col.getDeft());
							//}else{
								//bf.append(" DEFAULT '").append(col.getDeft()).append("'");
							//}
						}
						
						//�ǿ�
						if("N".equals(col.getNullable())){
							bf.append(" not null");
						}
						bf.append(";");
						bf.append("\r\n");
					}else if("1".equals(type)||"2".equals(type)){
						//type��ͬ,length��ͬ
						bf.append("alter table ").append(tb.getName());
						if(dbType==0){
							//oracle
							bf.append(" modify ").append(col.getName()).append(" ").append(col.getType());
						}else if(dbType==1){
							//db1
							bf.append(" alter ").append(col.getName()).append(" set data type ").append(col.getType());				
						}
						//����
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
			log.info("openConnection",e);
			e.printStackTrace();
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
				//System.out.println("close connection");
			}
		}catch(Exception e){
			log.info("closeConnection",e);
			e.printStackTrace();
			this.setErrmsg("�ر����ݿ������쳣");
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
