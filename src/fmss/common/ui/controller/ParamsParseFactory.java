package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;





/**
 * 类说明:<br>
 * 创建时间: 2008-10-9 下午06:03:32<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class ParamsParseFactory {
	
	/** Logger for this class */
	private static final Logger logger = Logger.getLogger(ParamsParseFactory.class);
	
	private HashMap dataM = new HashMap();

	private List keyList = new ArrayList();
	
	public List getKeyList() {
		return keyList;
	}

	public void initialize() {
		// 注册现有的触发器
		logger.debug("注册自由查询中系统支持的参数值...");
		for (Iterator iterator = dataM.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			keyList.add(key);
		}
	}
	
	public void setDataM(HashMap dataM) {
		this.dataM = dataM;
	}
	
	public ProviderParams getRuntimeDataNewInstance(String reportSaveType) {
		
		return(ProviderParams)dataM.get(reportSaveType);
	}
}
