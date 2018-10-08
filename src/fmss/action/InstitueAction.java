package fmss.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fmss.dao.entity.LoginDO;
import fmss.services.InstService;
import fmss.common.util.Constants;
import fmss.common.util.SecurityPassword;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;


/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: guojingzhan
 * @日期: 2009-6-27 下午01:57:18
 * @描述: [InstitueAction]机构操作相关Action
 */
public class InstitueAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	protected static final String EDIT_INST = "formInst";
	protected static final String STREAM = "stream";
	private InstService instService; // 机构方法服务
	private List instList; // 机构列表
	private String id; // 机构编号
	private InputStream inputStream; // 输入值

	/**
	* <p>方法名称: loadInstAndUsrXml|描述: 异步获取部门和用户</p>
	*/
	public void loadInstAndUsrXml(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER); 
		String level = SecurityPassword.filterStr(request.getParameter("level"));
 		String reInit = SecurityPassword.filterStr(request.getParameter("reInit"));
		id = SecurityPassword.filterStr(id);
		
		String strInstList =null;
		if(StringUtils.isNotEmpty(level)){//这里的层级是根据点击页面按钮进来的
			strInstList= instService.loadInstAndUsrXmlEx(id,Integer.valueOf(level).intValue(),user,reInit);
		}else{
			strInstList= instService.loadInstAndUsrXmlEx(id,user,reInit);
		}
		this.response.setContentType("text/html; charset=UTF-8");
		
		try{
			this.response.getWriter().print(strInstList);
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				this.response.getWriter().close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	public void loadInstXmlZTree(){
		id = SecurityPassword.filterStr(id);
		List instList= instService.loadInstZTreeEx();
		try{
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(JSONArray.fromObject(instList).toString());
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				this.response.getWriter().close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	/**
	* <p>方法名称: getInstList|描述: 获取机构列表</p>
	* @return 获取机构列表
	*/
	public List getInstList(){
		return instList;
	}

	/**
	* <p>方法名称: setInstList|描述: 设置机构列表</p>
	* @param instList 设置机构列表
	*/
	public void setInstList(List instList){
		this.instList = instList;
	}

	/**
	* <p>方法名称: getId|描述: 获取机构编号</p>
	* @return 获取机构编号
	*/
	public String getId(){
		return id;
	}

	/**
	* <p>方法名称: setId|描述: 设置机构编号</p>
	* @param id 设置机构编号
	*/
	public void setId(String id){
		this.id = id;
	}

	/**
	* <p>方法名称: getInstService|描述: 获取机构服务</p>
	* @return 获取机构服务
	*/
	public InstService getInstService(){
		return instService;
	}

	
	/**
	 * @param instService 要设置的 instService
	 */
	public void setInstService(InstService instService){
		this.instService = instService;
	}


	/**
	* <p>方法名称: getInputStream|描述: 获取输入内容</p>
	* @return 获取输入内容
	*/
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	* <p>方法名称: setInputStream|描述: 设置输入内容</p>
	* @param inputStream 设置输入内容
	*/
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
}
