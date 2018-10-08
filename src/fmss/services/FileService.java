package fmss.services;

import java.util.List;

import fmss.dao.entity.UBaseFolderDO;

import org.apache.commons.lang.StringUtils;


public class FileService extends CommonService {
	
	/**
	 * <p>
	 * 方法名称: loadFolderXmlEx|描述:获取文件夹
	 * </p>
	 * 
	 * @param  folderId 
	 *            文件夹ID
	 * @param  next
	 *            显示几级   
	 * @return 
	 */
	public String loadFolderXmlEx(String folderId,int level) {
		StringBuffer sb = new StringBuffer();
		UBaseFolderDO folder = null;
		if (StringUtils.isNotBlank(folderId)) {
			// 异步加载该机构下包含的子机构
			return LoadSubFolderTree(folderId, true);
		} else {
			// 第一次加载时，获取上级机构的清单-显示两层
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

				// FIXME: 此地方判断是否有子结点，需要优化
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
	 * 方法名称: LoadSubFolderTree|描述:获取子文件夹
	 * </p>
	 * 
	 * @param folderId
	 *            文件夹ID
	 * @param isHaveRoot
	 *            是否包含根节点
	 * @return 
	 */
	private String LoadSubFolderTree(String folderId, boolean isHaveRoot) {
		StringBuffer sb = new StringBuffer();
		UBaseFolderDO folder = null;
		if (StringUtils.isNotBlank(folderId)) {
			// 异步加载该机构下包含的子机构
			List list = this.find("from UBaseFolderDO where parentFolderId=? and display='true'", Long.valueOf(folderId));
			if (list != null) {
				// 如果是异步获取需要增加<data>节点，否则无法加载
				if (isHaveRoot)
					sb.append("<data>");
				for (int i = 0; i < list.size(); i++) {
					folder = (UBaseFolderDO) list.get(i);
					sb.append("<TreeNode name='");
					sb.append(folder.getFolderName()).append("[").append(folder.getFolderCode()).append("]");
					sb.append("' id='");
					sb.append(folder.getFolderId());
					sb.append("' levelType='2' ");
					// FIXME: 此地方判断是否有子结点，需要优化
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
