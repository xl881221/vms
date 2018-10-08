package fmss.services;

import java.util.List;

import fmss.dao.entity.UBaseFolderDO;

import org.apache.commons.lang.StringUtils;


public class FileService extends CommonService {
	
	/**
	 * <p>
	 * ��������: loadFolderXmlEx|����:��ȡ�ļ���
	 * </p>
	 * 
	 * @param  folderId 
	 *            �ļ���ID
	 * @param  next
	 *            ��ʾ����   
	 * @return 
	 */
	public String loadFolderXmlEx(String folderId,int level) {
		StringBuffer sb = new StringBuffer();
		UBaseFolderDO folder = null;
		if (StringUtils.isNotBlank(folderId)) {
			// �첽���ظû����°������ӻ���
			return LoadSubFolderTree(folderId, true);
		} else {
			// ��һ�μ���ʱ����ȡ�ϼ��������嵥-��ʾ����
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<Response><Data><Tree>");
			List list = this.getBem().find("select u from UBaseFolderDO u  join u.baseConfig u1 where u1.enabled='true' and u.parentFolderId is null and u.display='true'");
			for (int i = 0; i < list.size(); i++) {
				folder = (UBaseFolderDO) list.get(i);
				sb.append("<TreeNode name='");
				sb.append(folder.getFolderName()).append("[").append(folder.getFolderCode()).append("]");
				sb.append("' id='");
				sb.append(folder.getFolderId());
				sb.append("' levelType='1' ");

				// FIXME: �˵ط��ж��Ƿ����ӽ�㣬��Ҫ�Ż�
				List listTmp = this.find("from UBaseFolderDO where parentFolderId=? and display='true'", folder.getFolderId());
				if (listTmp != null && listTmp.size() > 0) {
					sb.append(" _hasChild='1' ");
					sb.append(" _opened='true' ");
				}
				sb.append(" _canSelect='1' ");
				sb.append("> ");
				if (listTmp != null && listTmp.size() > 0) {
					if(level==2)
						sb.append(LoadSubFolderTree(folder.getFolderId().toString(), false));
				}
				sb.append("</TreeNode>");
			}
			sb.append("</Tree></Data></Response>");
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * ��������: LoadSubFolderTree|����:��ȡ���ļ���
	 * </p>
	 * 
	 * @param folderId
	 *            �ļ���ID
	 * @param isHaveRoot
	 *            �Ƿ�������ڵ�
	 * @return 
	 */
	private String LoadSubFolderTree(String folderId, boolean isHaveRoot) {
		StringBuffer sb = new StringBuffer();
		UBaseFolderDO folder = null;
		if (StringUtils.isNotBlank(folderId)) {
			// �첽���ظû����°������ӻ���
			List list = this.find("from UBaseFolderDO where parentFolderId=? and display='true'", Long.valueOf(folderId));
			if (list != null) {
				// ������첽��ȡ��Ҫ����<data>�ڵ㣬�����޷�����
				if (isHaveRoot)
					sb.append("<data>");
				for (int i = 0; i < list.size(); i++) {
					folder = (UBaseFolderDO) list.get(i);
					sb.append("<TreeNode name='");
					sb.append(folder.getFolderName()).append("[").append(folder.getFolderCode()).append("]");
					sb.append("' id='");
					sb.append(folder.getFolderId());
					sb.append("' levelType='2' ");
					// FIXME: �˵ط��ж��Ƿ����ӽ�㣬��Ҫ�Ż�
					List listInstTmp = this.find("from UBaseFolderDO where parentFolderId=? and display='true'", folder.getFolderId());
					if (listInstTmp != null && listInstTmp.size() > 0) {
						sb.append(" _hasChild='1' ");
						sb.append(" _opened='false' ");
					}
					sb.append(" _canSelect='1' ");
					sb.append("> ");
					
					sb.append("</TreeNode>");
				}
				if (isHaveRoot)
					sb.append("</data>");
			}
		}
		return sb.toString();
	}

	
}
