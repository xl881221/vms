package fmss.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:35:34
 * @����: [JsonProviderController]JSON�������
 */
public abstract class JsonProviderController extends AbstractController{

	private static final Logger log = Logger
			.getLogger(JsonProviderController.class);

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		return null;
	}

	/**
	 * <p>��������: pushJsonResponse|����:ͨ��Response����Ajax��Json���� </p>
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	protected void pushJsonResponse(HttpServletResponse response, Object obj)
			throws IOException{
		Assert.notNull(obj);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Content-Type", "text/xml; charset=utf-8");
		// response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if(obj instanceof List){
			log.info("JsonData - [Array]:");
			JSONArray jsonArray = JSONArray.fromObject(obj);
			log.info("JsonData - [Array]:" + jsonArray);
			out.print(jsonArray);
			out.close();
		}else{
			log.info("JsonData - [Object]: ");
			JSONObject jsonObject = JSONObject.fromObject(obj);
			log.info("JsonData - [Object]: " + jsonObject);
			out.print(jsonObject);
			out.close();
		}
	}
}
