package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;





/**
 * ��˵��:<br>
 * ����ʱ��: 2008-10-9 ����06:03:32<br>
 * 
 * @author �����<br>
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
		// ע�����еĴ�����
		logger.debug("ע�����ɲ�ѯ��ϵͳ֧�ֵĲ���ֵ...");
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
