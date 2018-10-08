package fmss.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DbBean {
	private int dbType;
	private int target_dbtype;
	private String url;
	private String dbname;
	private String target_dbname;
	private String username;
	private String password;
	
	private Map tablemap;//存放table信息
	private List viewList;//视图信息
	private List seqList;//seq信息
	private List procList;//存储过程信息
	
	
	private List lostTable;
	private List lostView;
	private List lostSeq;
	private List lostProc;
	
	private Map difTable;

	public DbBean() {
		super();
		tablemap=new TreeMap();
		viewList=new ArrayList();
		seqList=new ArrayList();
		procList=new ArrayList();
		
		lostTable=new ArrayList();
		difTable=new TreeMap();
		lostView=new ArrayList();
		lostSeq=new ArrayList();
		lostProc=new ArrayList();
	}
	
	public DbBean(int dbType,String url, String dbname, String username, String password) {
		super();
		this.dbType=dbType;
		this.url = url;
		this.dbname = dbname;
		this.username = username;
		this.password = password;
		tablemap=new TreeMap();
		viewList=new ArrayList();
		seqList=new ArrayList();
		procList=new ArrayList();
		
		lostTable=new ArrayList();
		difTable=new TreeMap();
		lostView=new ArrayList();
		lostSeq=new ArrayList();
		lostProc=new ArrayList();
	}
		
	
	

	
	public Map getDifTable() {
		return difTable;
	}

	public void setDifTable(Map difTable) {
		this.difTable = difTable;
	}

	public List getLostProc() {
		return lostProc;
	}

	public void setLostProc(List lostProc) {
		this.lostProc = lostProc;
	}

	public List getLostSeq() {
		return lostSeq;
	}

	public void setLostSeq(List lostSeq) {
		this.lostSeq = lostSeq;
	}

	public List getLostTable() {
		return lostTable;
	}

	public void setLostTable(List lostTable) {
		this.lostTable = lostTable;
	}

	public List getLostView() {
		return lostView;
	}

	public void setLostView(List lostView) {
		this.lostView = lostView;
	}

	public List getProcList() {
		return procList;
	}

	public void setProcList(List procList) {
		this.procList = procList;
	}

	public List getSeqList() {
		return seqList;
	}

	public void setSeqList(List seqList) {
		this.seqList = seqList;
	}

	public List getViewList() {
		return viewList;
	}

	public void setViewList(List viewList) {
		this.viewList = viewList;
	}

	public Map getTablemap() {
		return tablemap;
	}

	public void setTablemap(Map tablemap) {
		this.tablemap = tablemap;
	}
	public String getDbname() {
		return dbname;
	}
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	public int getDbType() {
		return dbType;
	}
	public void setDbType(int dbType) {
		this.dbType = dbType;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getTarget_dbtype() {
		return target_dbtype;
	}
	public void setTarget_dbtype(int target_dbtype) {
		this.target_dbtype = target_dbtype;
	}
	public String getTarget_dbname() {
		return target_dbname;
	}
	public void setTarget_dbname(String target_dbname) {
		this.target_dbname = target_dbname;
	}
	
	
}
