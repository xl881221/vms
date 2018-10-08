package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fmss.dao.entity.LoginDO;
import fmss.common.util.Constants;
import fmss.common.util.PaginationList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;



import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.crms.util.UnicodeUtils;

public abstract class BaseAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;// request对象
	protected HttpServletResponse response;// response对象
	protected Map sessionMap;// session对象
	private String RESULT_MESSAGE;

	protected PaginationList paginationList = null; // 分页信息列表对象
	protected int curPage = 1;

	protected List ids;

	protected transient final Log log = LogFactory.getLog(getClass());

	/**
	 * 取得分页变量
	 * 
	 * @return
	 */
	public BaseAction() {
		paginationList = paginationList == null ? new PaginationList()
				: paginationList;
	}

	public PaginationList getPaginationList() {
		return paginationList;
	}

	public void setPaginationList(PaginationList paginationList) {
		this.paginationList = paginationList;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List getIds() {
		return ids;
	}

	public void setIds(List ids) {
		this.ids = ids;
	}

	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}
	/**
	 * <p>
	 * 方法名称: getURL|描述:获取地址
	 * </p>
	 * 
	 * @param url 带通配符的地址
	 *        defaultIP  登陆请求的IP
	 *        defaultIP  登陆请求的端口
	 *             
	 * @return url
	 */
    public static String getURL(String url,String defaultIP,String defaultPort){
    	if(url != null){
    		if (url.indexOf(Constants.DEFAULT_URL_IP) != -1) {
        		url = url.replaceFirst(
    					Constants.DEFAULT_URL_IP_UNICODE, defaultIP);
    		}
        	if (url.indexOf(Constants.DEFAULT_URL_PORT) != -1) {
        		url = url.replaceFirst(
    					Constants.DEFAULT_URL_PORT_UNICODE, defaultPort);
    		}
    	}
		return url;
    }
    
	/**
	 * <p>
	 * 方法名称: get|描述:获取session中参数对象
	 * </p>
	 * 
	 * @param param
	 *            参数名
	 * @return session中对象
	 */
	public Object get(String param) {
		if (sessionMap == null) {
			sessionMap = ActionContext.getContext().getSession();
		}
		return ActionContext.getContext().getSession().get(param);
	}

	/**
	 * <p>
	 * 方法名称: setResultMessages|描述:设置session中存储的处理信息结果
	 * </p>
	 * 
	 * @param resultMessages
	 *            处理信息结果
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public void setResultMessages(String resultMessages){
		
		log.info(request.getHeader("user-agent"));
		log.info(request.getHeader(request.getLocale().toString()));
		resultMessages = chr2Unicode(resultMessages);
		log.info(resultMessages);
		try {
			this.RESULT_MESSAGE=java.net.URLEncoder.encode(resultMessages,"utf-8");
			request.setAttribute("RESULT_MESSAGE", RESULT_MESSAGE);
			request.setAttribute("resultMessages", resultMessages);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

	/**
	 * <p>
	 * 方法名称: getLoginUserId|描述:获取登陆用户ID
	 * </p>
	 * 
	 * @return 用户登录ID
	 */
	public String getLoginUserId() {
		LoginDO user = (LoginDO) get(Constants.LOGIN_USER);
		if (user != null) {
			return user.getUserId();
		} else {
			return null;
		}
	}

	/**
	 * 中文转unicode字符(英文环境)
	 * 
	 * @param str
	 * @return
	 */
	public String chr2Unicode(String str) {
        String[] a={"","000","00","0",""};
		String result = "";
		if (StringUtils.isNotEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				int chr = (char) str.charAt(i);
				String s=Integer.toHexString(chr);
				result += "\\u" +a[s.length()]+s;
			}
		}
		return result;
	}

	/**
	 * 输出至页面
	 * 
	 * @param a
	 * @throws IOException
	 */
	public void out2page(String a) {
		//response.setContentType("text/html;charset=UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		//response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control",
				"no-store, max-age=0, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers.
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(a);
			log.info(a);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 是外网访问还是内网访问
	 * 
	 * @param a
	 * @throws IOException
	 */
	public static boolean isInnerNet(HttpServletRequest request){
		String isInnerNet = (String)request.getSession().getAttribute("isInnerNet");
		if(isInnerNet!=null){
			return Boolean.valueOf(isInnerNet).booleanValue();
		}
		return false;
	}
	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String result_message) {
		RESULT_MESSAGE = result_message;
	}
	
	

}
