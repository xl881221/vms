package fmss.common.db;

public class DataSourceConfig{
	/** serialVersionUID*/
	private static final long serialVersionUID = 3258153421400009018L;
	private  String hibernateDialect = "";
	private  String dbDriver = "";
	private  String dbUrl = "";
	private  String dbUserName = "";
	private  String dbPassword = "";
	
	public  String getHibernateDialect(){
		return hibernateDialect;
	}
	
	public  void setHibernateDialect(String hibernateDialect){
		this.hibernateDialect = hibernateDialect;
	}
	
	public String getDbDriver(){
		return dbDriver;
	}
	
	public void setDbDriver(String dbDriver){
		this.dbDriver = dbDriver;
	}
	
	public String getDbUrl(){
		return dbUrl;
	}
	
	public void setDbUrl(String dbUrl){
		this.dbUrl = dbUrl;
	}
	
	public String getDbUserName(){
		return dbUserName;
	}
	
	public void setDbUserName(String dbUserName){
		this.dbUserName = dbUserName;
	}
	
	public String getDbPassword(){
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword){
		this.dbPassword = dbPassword;
	}
	
}
