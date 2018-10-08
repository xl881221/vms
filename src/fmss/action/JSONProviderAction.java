package fmss.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:27:29
 * @描述: [JSONProviderAction]分页及对象序列化类
 */
public abstract class JSONProviderAction extends BaseAction{

	private static final Logger logger = Logger.getLogger(BaseAction.class);
	protected List dataList = new ArrayList();
	protected int pageNum = 0;// 页数
	protected int pageSize = 0;// 页号
	protected int totalCount = 0;// 总记录数
	protected int limit = 15;// 每页记录条数
	protected int start = 0;// 开始条数

	/**
	 * <p>方法名称: pushJSON2Stream|描述:序列化对象 </p>
	 * @param object
	 * @param inputStream
	 * @return
	 */
	public InputStream pushJSON2Stream(Object object, InputStream inputStream){
		try{
			if(object instanceof List){
				JSONArray jsonArray = JSONArray.fromObject(object);
				logger.info("jsonArray:" + jsonArray);
				inputStream = new StringBufferInputStream(jsonArray.toString());
			}else{
				JSONObject jsonObject = JSONObject.fromObject(object);
				logger.info("jsonObject:" + jsonObject);
				inputStream = new StringBufferInputStream(jsonObject.toString());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * <p>方法名称: toJSONObject|描述:对象转换 </p>
	 * @param object
	 * @return
	 */
	public Object toJSONObject(Object object){
		try{
			if(object instanceof List){
				JSONArray jsonArray = JSONArray.fromObject(object);
				logger.info("jsonArray:" + jsonArray);
				return jsonArray;
			}else{
				JSONObject jsonObject = JSONObject.fromObject(object);
				logger.info("jsonObject:" + jsonObject);
				return jsonObject;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * <p>方法名称: handleRequestInternal|描述:通过Ext传递的分页参数计算分页信息 </p>
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		if(StringUtils.isNotEmpty(request.getParameter("limit"))){
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		if(StringUtils.isNotEmpty(request.getParameter("start"))){
			start = Integer.parseInt(request.getParameter("start"));
		}
		try{
			pageNum = (start / limit) + 1;
			pageSize = limit;
		}catch (ArithmeticException e){
			e.printStackTrace();
		}
	}

	/**
	 * <p>方法名称: parseJSON|描述:解析JSON串 </p>
	 * @param json
	 * @return
	 */
	public List parseJSON(String json){
		// Guard statement
		if(json == null){
			return null;
		}
		String SPLIT_CHAR = ",";
		String temp = json.substring(2, json.length() - 2);
		String[] strs = temp.split("],\\[");
		List jsonArray = new ArrayList();
		for(int i = 0; i < strs.length; i++){
			List jsonObject = new ArrayList();
			String[] values = StringUtils.split(strs[i], SPLIT_CHAR, 5);
			jsonObject.add(parseString(values[0]));
			jsonObject.add(parseString(values[1]));
			jsonObject.add(parseFloat(values[2]));
			jsonObject.add(parseFloat(values[3]));
			jsonObject.add(parseString(values[4]));
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * <p>方法名称: parseString|描述:字符串处理 </p>
	 * @param str
	 * @return
	 */
	private static String parseString(String str){
		String temp = StringUtils.trimToEmpty(str);
		return temp.substring(1, temp.length() - 1).trim();
	}

	/**
	 * <p>方法名称: parseFloat|描述: 浮点数处理</p>
	 * @param str
	 * @return
	 */
	private static String parseFloat(String str){
		return StringUtils.trimToEmpty(str);
	}

	/**
	 * <p>方法名称: pushJsonResponse|描述:通过Response返回Ajax的Json数据 </p>
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
		PrintWriter out = response.getWriter();
		if(obj instanceof List){
			logger.info("JsonData - [Array]:");
			JSONArray jsonArray = JSONArray.fromObject(obj);
			logger.info("JsonData - [Array]:" + jsonArray);
			out.print(jsonArray);
			out.close();
		}else{
			logger.info("JsonData - [Object]: ");
			JSONObject jsonObject = JSONObject.fromObject(obj);
			logger.info("JsonData - [Object]: " + jsonObject);
			out.print(jsonObject);
			out.close();
		}
	}
	protected void pushResponse(HttpServletResponse response, String s){
		try{
			Assert.notNull(s);
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Expires", "0");
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(s);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
