package fmss.action;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fmss.services.ScriptManager;

import com.opensymphony.xwork2.ActionContext;

public class ScriptAction extends BaseAction {

	private ScriptManager scriptManager;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void script() {

		PrintWriter pw = null;
		try {

			// 开启脚本验证功能,参数化....
			if (true) {
				String s = request.getParameter("list");
				String[] sqls = new String[0];
				if (StringUtils.isNotEmpty(s))
					sqls = s.split(scriptManager.getSeparator());
				if (ArrayUtils.isEmpty(sqls)) {
					log.warn("获取的sql参数是空:" + s);
					pw.print("");
					return;
				}
				Set set = scriptManager.getNames(new HashSet(Arrays.asList(sqls)));
				pw = response.getWriter();
				response.setContentType("text/html; charset=GBK");
				pw.print(set.isEmpty() ? "" : StringUtils.join(set.iterator(), scriptManager.getSeparator()));
			} else {
				pw.print("");
				return;
			}
		} catch (Exception e) {
			log.error("script errors:", e);
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	public String forbidden() {
		// 开启脚本验证功能,参数化....
		if (true) {
			String s = request.getParameter("list");
			if (StringUtils.isNotEmpty(s) && s.indexOf(scriptManager.getSeparator()) != -1)
				ActionContext.getContext().put("list", Arrays.asList(s.split(scriptManager.getSeparator())));
		}
		return SUCCESS;
	}

	public void setScriptManager(ScriptManager scriptManager) {
		this.scriptManager = scriptManager;
	}

}
