package fmss.action;

import java.io.PrintWriter;

import fmss.common.db.DataSourceConfig;
import net.sf.json.JSONObject;


public class DataSourceConfigAction extends BaseAction{

	/** serialVersionUID*/
	private static final long serialVersionUID = -530363557859239431L;
	
	private DataSourceConfig dataSourceConfig;
	public String execute() throws Exception{	
		JSONObject dataSourceJson = JSONObject.fromObject(dataSourceConfig);
		
		PrintWriter resWriter = response.getWriter();
		resWriter.write(dataSourceJson.toString());
		
		return null;
	}
	
	public void setDataSourceConfig(DataSourceConfig dataSourceConfig){
		this.dataSourceConfig = dataSourceConfig;
	}
}
