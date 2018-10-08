package fmss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.util.Constants;

public class ParamAlteration extends Observable {
	private UBaseSysLogService sysLogService;
	private ParamConfigService paramConfigService;
	private LoginDO login;
	private List newL;

	public ParamAlteration(UBaseSysLogService sysLogService, ParamConfigService paramConfigService, List newL,
			LoginDO login) {
		this.sysLogService = sysLogService;
		this.paramConfigService = paramConfigService;
		this.login = login;
		this.newL = newL;
		addObserver(new LogObserver());
	}

	private boolean isAltered() {
		boolean isModified = false;
		StringBuffer sb = new StringBuffer("from UBaseSysParamDO where paramId in (");
		List values = new ArrayList(newL.size());
		Map newM = new HashMap(), oldM = new HashMap();
		for (Iterator iterator = newL.iterator(); iterator.hasNext();) {
			UBaseSysParamDO o = (UBaseSysParamDO) iterator.next();
			values.add(o.getParamId());
			newM.put(o.getParamId(), o.getSelectedValue() == null ? "" : o.getSelectedValue());
			sb.append("?");
			if (iterator.hasNext())
				sb.append(",");
		}
		sb.append(")");
		List l = paramConfigService.find(sb.toString(), values);
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			UBaseSysParamDO o = (UBaseSysParamDO) iterator.next();
			oldM.put(o.getParamId(), o.getSelectedValue() == null ? "" : o.getSelectedValue());
		}
		// compare
		for (Iterator iterator = oldM.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry e = (Map.Entry) iterator.next();
			if (newM.get(e.getKey()) != null && !newM.get(e.getKey()).equals(e.getValue())) {
				UBaseSysParamDO o = paramConfigService.findById(e.getKey().toString());
				login.addDescription("参数[").addDescription(o.getItemCname()).addDescription("]值[").addDescription(
						isEnumParameter(o) ? getParamterEnumValue(o, (String) e.getValue()) : e.getValue())
						.addDescription("->").addDescription(
								isEnumParameter(o) ? getParamterEnumValue(o, (String) newM.get(e.getKey())) : newM
										.get(e.getKey())).addDescription("] ");
				isModified = true;
			}
		}
		return isModified;
	}

	public void afterAlteration() {
		// 如果参数被修改
		if (isAltered()) {
			paramConfigService.saveParamConfig(newL);
			addObserver(new AuditObserver());
		} else {
			login.setDescription("保存参数，没有修改任何参数");
		}
		// 无论任何都记录一下日志
		setChanged();
		notifyObservers();
	}

	private boolean isEnumParameter(UBaseSysParamDO o) {
		return o != null && StringUtils.isNotEmpty(o.getValueList());
	}

	private String getParamterEnumValue(UBaseSysParamDO o, String value) {
		String enumValue = value;
		if (StringUtils.isNotEmpty(o.getValueList()) && StringUtils.isNotEmpty(value)) {
			String[] array = o.getValueList().split(";");
			for (int i = 0; i < array.length; i++) {
				if (array[i].startsWith(value)) {
					enumValue = array[i];
					break;
				}
			}
		}
		return enumValue;
	}

	private class LogObserver implements Observer {

		public void update(Observable o, Object arg) {
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0003.0003");
			sysLog.setMenuName("系统参数管理.参数配置");
			sysLogService.saveUBaseSysLog(sysLog);
		}

	}

	private class AuditObserver implements Observer {

		public void update(Observable o, Object arg) {
			// TODO Auto-generated method stub
		}

	}
}
