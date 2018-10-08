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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����02:16:31
 * @����: [AjaxInstAction]�첽��ȡ������Ϣ�Ŀ�����
 */
public class AjaxInstAction extends JSONProviderAction{

	private static final long serialVersionUID = 1L;
	
	/** ��¼��־��Ϣ*/
	private static final Logger logger = Logger.getLogger(AjaxInstAction.class);
	
	/** ����Ϣר����ת����*/
	protected static final String STREAM = "stream";
	
	/** ������Ϣ������*/
	private InstService instService;
	
	/**���������� */
	private InputStream inputStream;
	
	/** �ֵ���Ϣ������*/
	private DictionaryService dicService;

	/**
	 * <p>��������: list|����:��ת����������ҳ�� </p>
	 * @return ���ص��ɹ�ҳ��
	 */
	public String list(){
		return SUCCESS;
	}

	/**
	 * <p>��������: instList|����: �����б�</p>
	 * @return ���ػ����б�
	 */
	public String instList(){
		try{
			// ���¶�request���б���
			super.request.setCharacterEncoding("utf-8");
			super.handleRequestInternal(request, response);
			// ��ҳ����չ�������
			String instId = request.getParameter("instId");
			String instName = request.getParameter("instName");
			// ��ȡ��¼����
			int count = instService.getInstsCountByFilterCriteria(instId,
					instName);
			// ��ȡ������������ģ�ͼ�����
			List instTrees = instService.buildData(instId, instName, pageSize,
					pageNum);
			// ���ݷ�װ
			ExtPagingGridBean bean = new ExtPagingGridBean();
			bean.setDataList(instTrees);
			bean.setTotalCount(count);
			// �����JSON���л� 
			super.pushJsonResponse(super.response, bean);
			return null;
		}catch (Exception e){
			logger.error(e);
		}
		return ERROR;
	}

	/**
	 * <p>��������: instUpdate|����: ���»�������</p>
	 * @return
	 */
	public String instUpdate(){
		//TODO �˷�����Ӧ�ý��в�����־�ļ�¼����
		try{
			// ���¶�request���б���
			super.request.setCharacterEncoding("utf-8");
			// ��ȡ��������
			String operStyle = request.getParameter("type");
			// ��ȡ�޸ĺ���û�������Ϣ
			String instInfo = request.getParameter("instInfo");
			// ����response��Ϊд������׼��
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
					// ������¼
					instService.save(inst);
					response.getWriter().write("Aok");
				}catch (Exception e){
					response.getWriter().write("A01");
				}
			}else if("modify".equals(operStyle)){
				UBaseInstDO inst = instService.toUbaseInst(instInfo);
				try{
					// �����޸ĺ��ֵ
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
	 * <p>��������: commonInfo|����:��ȡ������Ϣ </p>
	 * @return
	 */
	public String commonInfo(){
		try{
			super.handleRequestInternal(request, response);
			String style = request.getParameter("style");
			List commonInfo = new ArrayList();
			if("dic".equals(style)){
				String dicType = request.getParameter("dicType");
				// ��ȡ�ֵ���Ϣ
				commonInfo = dicService.getDictoryByDicType(dicType);
			}else if("inst".equals(style)){
				// ��ȡ������Ϣ
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
