/**
 * 
 */
package fmss.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.SapMapInfo;
import fmss.common.util.PaginationList;

/**
 * @author liuhaibo ά��
 * 
 */
public class SapMapInfoService extends CommonService {

	/**
	 * @param dpvId
	 *            ģ���ƶ��е�ָ���
	 * @param dpvName
	 *            ָ������
	 * @param acciNo
	 *            ��Ŀ��
	 * @param prodId
	 *            ��Ʒ��
	 * @return ��ҳ��ѯ����
	 */
	public List selectByFormWithPaging(String dpvId, String dpvName,
			String acciNo, String prodId, PaginationList pageInfo) {
		String hql = "from SapMapInfo a where 1=1";
		ArrayList list = new ArrayList();
		if (StringUtils.isNotEmpty(dpvId)) {
			list.add("%" + dpvId + "%");
			hql += " and a.dpvId like ?";
		}
		if (StringUtils.isNotEmpty(dpvName)) {
			list.add("%" + dpvName + "%");
			hql += " and a.dpvName like ?";
		}
		if (StringUtils.isNotEmpty(acciNo)) {
			list.add("%" + acciNo + "%");
			hql += " and a.acciNo like ?";
		}
		if (StringUtils.isNotEmpty(prodId)) {
			list.add("%" + prodId + "%");
			hql += " and a.prodId like ?";
		}
		return super.find(hql, list, pageInfo);
	}

	/**
	 * �޸ĺ�����
	 * @param sapMapInfo
	 */
	public void saveOrUpdateSapMapInfo(SapMapInfo sapMapInfo) {
		// ��������
		if (StringUtils.isEmpty(sapMapInfo.getId())) {
			super.save(sapMapInfo);
		} else {
			// �޸Ĳ���
			super.update(sapMapInfo);
		}
	}

	/**
	 * ���ض���
	 * @param id
	 * @return
	 */
	public SapMapInfo loadSapMapInfo(String id) {
		String hql = "from SapMapInfo a where a.id='"+id+"'";
		return (SapMapInfo) super.find(hql).get(0);
//		return (SapMapInfo) super.load(SapMapInfo.class, id);
	}

	/**
	 * @param ids ��Ҫɾ��id���ϣ���","���зָ�
	 */
	public void deleteSapMapInfos(String[] ids) {
		List sapMapInfoList = new ArrayList();
		for (int i = 0; i < ids.length; i++) {
			sapMapInfoList.add(loadSapMapInfo(ids[i]));
		}
		if (sapMapInfoList.size() > 0) {
			super.deleteAll(sapMapInfoList);
		}
	}

}
