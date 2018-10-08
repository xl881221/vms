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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: guojingzhan
 * @����: 2009-6-27 ����01:57:18
 * @����: [InstitueAction]�����������Action
 */
public class InstitueAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	protected static final String EDIT_INST = "formInst";
	protected static final String STREAM = "stream";
	private InstService instService; // ������������
	private List instList; // �����б�
	private String id; // �������
	private InputStream inputStream; // ����ֵ

	/**
	* <p>��������: loadInstAndUsrXml|����: �첽��ȡ���ź��û�</p>
	*/
	public void loadInstAndUsrXml(){
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER); 
		String level = SecurityPassword.filterStr(request.getParameter("level"));
 		String reInit = SecurityPassword.filterStr(request.getParameter("reInit"));
		id = SecurityPassword.filterStr(id);
		
		String strInstList =null;
		if(StringUtils.isNotEmpty(level)){//����Ĳ㼶�Ǹ��ݵ��ҳ�水ť������
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
	* <p>��������: getInstList|����: ��ȡ�����б�</p>
	* @return ��ȡ�����б�
	*/
	public List getInstList(){
		return instList;
	}

	/**
	* <p>��������: setInstList|����: ���û����б�</p>
	* @param instList ���û����б�
	*/
	public void setInstList(List instList){
		this.instList = instList;
	}

	/**
	* <p>��������: getId|����: ��ȡ�������</p>
	* @return ��ȡ�������
	*/
	public String getId(){
		return id;
	}

	/**
	* <p>��������: setId|����: ���û������</p>
	* @param id ���û������
	*/
	public void setId(String id){
		this.id = id;
	}

	/**
	* <p>��������: getInstService|����: ��ȡ��������</p>
	* @return ��ȡ��������
	*/
	public InstService getInstService(){
		return instService;
	}

	
	/**
	 * @param instService Ҫ���õ� instService
	 */
	public void setInstService(InstService instService){
		this.instService = instService;
	}


	/**
	* <p>��������: getInputStream|����: ��ȡ��������</p>
	* @return ��ȡ��������
	*/
	public InputStream getInputStream(){
		return inputStream;
	}

	/**
	* <p>��������: setInputStream|����: ������������</p>
	* @param inputStream ������������
	*/
	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
}
