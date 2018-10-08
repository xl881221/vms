package fmss.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fmss.dao.entity.UBaseInstDO;
import fmss.services.DictionaryService;
import fmss.services.InstService;
import fmss.common.util.ExtPagingGridBean;

import org.apache.log4j.Logger;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午02:16:31
 * @描述: [AjaxInstAction]异步获取机构信息的控制类
 */
public class AjaxInstAction extends JSONProviderAction{

	private static final long serialVersionUID = 1L;
	
	/** 记录日志信息*/
	private static final Logger logger = Logger.getLogger(AjaxInstAction.class);
	
	/** 流信息专用跳转常量*/
	protected static final String STREAM = "stream";
	
	/** 机构信息服务类*/
	private InstService instService;
	
	/**数据输入流 */
	private InputStream inputStream;
	
	/** 字典信息服务类*/
	private DictionaryService dicService;

	/**
	 * <p>方法名称: list|描述:跳转到机构管理页面 </p>
	 * @return 返回到成功页面
	 */
	public String list(){
		return SUCCESS;
	}

	/**
	 * <p>方法名称: instList|描述: 机构列表</p>
	 * @return 返回机构列表
	 */
	public String instList(){
		try{
			// 重新对request进行编码
			super.request.setCharacterEncoding("utf-8");
			super.handleRequestInternal(request, response);
			// 从页面接收过滤条件
			String instId = request.getParameter("instId");
			String instName = request.getParameter("instName");
			// 获取记录总数
			int count = instService.getInstsCountByFilterCriteria(instId,
					instName);
			// 获取机构树的数据模型及数据
			List instTrees = instService.buildData(instId, instName, pageSize,
					pageNum);
			// 数据封装
			ExtPagingGridBean bean = new ExtPagingGridBean();
			bean.setDataList(instTrees);
			bean.setTotalCount(count);
			// 对象的JSON序列化 
			super.pushJsonResponse(super.response, bean);
			return null;
		}catch (Exception e){
			logger.error(e);
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: instUpdate|描述: 更新机构数据</p>
	 * @return
	 */
	public String instUpdate(){
		//TODO 此方法中应该进行操作日志的记录工作
		try{
			// 重新对request进行编码
			super.request.setCharacterEncoding("utf-8");
			// 获取操作类型
			String operStyle = request.getParameter("type");
			// 获取修改后的用户数据信息
			String instInfo = request.getParameter("instInfo");
			// 重置response，为写数据做准备
			response.reset();
			if("look".equals(operStyle)){
				Object object = instService.getInstByInstId(instInfo);
				if(object != null){
					pushJsonResponse(response, object);
				}
			}else if("remove".equals(operStyle)){
				String inst[] = instInfo.split("[\\|]");
				List ls = new ArrayList();
				for(int i = 0; i < inst.length; i++){
					UBaseInstDO temp = new UBaseInstDO();
					temp.setInstId(inst[i]);
					ls.add(temp);
				}
				try{
					instService.deleteAll(ls);
					response.getWriter().write("Dok");
				}catch (Exception e){
					response.getWriter().write("D01");
				}
			}else if("add".equals(operStyle)){
				UBaseInstDO inst = instService.toUbaseInst(instInfo);
				try{
					// 新增记录
					instService.save(inst);
					response.getWriter().write("Aok");
				}catch (Exception e){
					response.getWriter().write("A01");
				}
			}else if("modify".equals(operStyle)){
				UBaseInstDO inst = instService.toUbaseInst(instInfo);
				try{
					// 保存修改后的值
					instService.update(inst);
					response.getWriter().write("Mok");
				}catch (Exception e){
					response.getWriter().write("M01");
				}
			}
			return null;
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	/**
	 * <p>方法名称: commonInfo|描述:获取公共信息 </p>
	 * @return
	 */
	public String commonInfo(){
		try{
			super.handleRequestInternal(request, response);
			String style = request.getParameter("style");
			List commonInfo = new ArrayList();
			if("dic".equals(style)){
				String dicType = request.getParameter("dicType");
				// 获取字典信息
				commonInfo = dicService.getDictoryByDicType(dicType);
			}else if("inst".equals(style)){
				// 获取机构信息
				commonInfo = instService.getParentsByInstId(null);
			}
			ExtPagingGridBean bean = new ExtPagingGridBean();
			bean.setDataList(commonInfo);
			bean.setTotalCount(commonInfo.size());
			super.pushJsonResponse(super.response, bean);
			return null;
		}catch (Exception e){
			logger.error(e);
		}
		return ERROR;
	}

	public void setDicService(DictionaryService dicService){
		this.dicService = dicService;
	}

	public void setInstService(InstService instService){
		this.instService = instService;
	}

	public InputStream getInputStream(){
		return inputStream;
	}

	public void setInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
}
