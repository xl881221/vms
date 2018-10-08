package fmss.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * 类说明: 返回Json数据<br>
 */
public abstract class JsonPagingProviderController extends JsonProviderController {

	protected List dataList = new ArrayList();
	
	protected int pageNum = 0;
	
	protected int pageSize = 0;
	
	protected int totalCount = 0;

	protected int limit = 15;
	
	protected int start = 0;
	
	/**
	 * 功能说明: 通过Ext传递的分页参数计算分页信息<br>
	 * @throws IOException
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if(StringUtils.isNotEmpty(request.getParameter("limit"))) {
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if(StringUtils.isNotEmpty(request.getParameter("start"))) {
			start = Integer.parseInt(request.getParameter("start"));
		}
	try{
		pageNum = (start / limit) + 1;
		pageSize = limit;
	}catch(ArithmeticException e){
		e.printStackTrace();
	}
		return null;
	}
	
	/**
	 * 功能说明: 通过Response返回Ajax的Grid分页数据<br>
	 */
	protected void pushJsonResponse(HttpServletResponse response, ExtPagingGridBean bean) throws IOException {
		super.pushJsonResponse(response, bean);
	}
	
}
