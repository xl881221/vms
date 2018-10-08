package fmss.services;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseNoticeFeedBackDO;
import fmss.dao.entity.UBaseNoticeInfoDO;
import fmss.dao.entity.UBaseNoticeLogDO;
import fmss.common.db.IdGenerator;
import fmss.common.util.PaginationList;

import com.jes.core.file.FileManagerDBImpl;

/**
 * <p> ��Ȩ����:(C)2003-2010  </p>
 * @����: GongRunLin
 * @����: 2009-7-25 ����13:20:00
 * @����: ͨ���������
 */
public class NoticeService extends CommonService{

	private final String filePath = "/files/";
	private static IdGenerator  idGenerator; // id������
	private FileManagerDBImpl fileManager;

	public void setFileManager(FileManagerDBImpl fileManager) {
		this.fileManager = fileManager;
	}

	/**
	 * <p> ��������: getSubSystems|����:ȡ�õ�ǰ�û���Ȩ�޵���ϵͳ </p>
	 * @param userId
	 * @param addPub �Ƿ���ӹ�����Ϣ
	 * @return 
	 */
	public List getSubSystems(String userId, boolean addPub){
		//modify by wangxin 20091110 ���˲���ʾ�Ͳ����õ���ϵͳ
		//modify by fwy 20091209 ���˽�ɫδ���õ���ϵͳ
		String hql = "select new Map(c.systemId as key,c.systemCname as value,c.linkSiteUrl as root,c.linkSiteInnerUrl as innerRoot,c.systemEname as ename,c.systemId as systemId,c.display as display)"
				+ " from UBaseConfigDO c where c.systemId<>'00003' and c.display<>'false' and c.enabled='true' and "
				+ "c.systemId in (select res.systemId from UAuthRoleResourceDO res"
				+ " left join res.resMap map where map.srcTable ='u_base_menu'"
				+ " and res.objectId in (select role.roleId from UAuthRoleUserDO role ,UAuthRoleDO au where role.roleId=au.roleId and au.enabled='1' and role.userId='"
				+ userId + "')) order by c.menuOrderNum";
		List types = this.find(hql);
		if(types == null)
			types = new ArrayList();
		if(addPub){
			Map pub = new HashMap();
			pub.put("key", "00000");
			pub.put("value", "����");
			types.add(0, pub);
		}
		return types;
	}

	/**
	 * <p> ��������: getSubSystems|����:ȡ�õ�ǰ�û���ͨ�����Ȩ�޵���ϵͳ </p>
	 * @param userId
	 * @return
	 */
	//modify by fwy 20091209 ���˽�ɫδ���õ���ϵͳ
	public List getSubSystems(String userId){
		String hql = "select new Map(c.systemId as key,c.systemCname as value,c.linkSiteUrl as root,c.systemEname as ename)"
				+ " from UBaseConfigDO c where c.systemId<>'00003'  and c.enabled ='true' and c.systemId<>'00000' and "
				+ "c.systemId in (select res.systemId from UAuthRoleResourceDO res"
				+ " left join res.resMap map where map.resType='PUB' and res.resDetailName like '%ͨ�����%'"
				+ " and  res.objectId in (select role.roleId from UAuthRoleUserDO role ,UAuthRoleDO au where role.roleId=au.roleId and au.enabled='1' and role.userId='"
				+ userId + "')) order by c.menuOrderNum";
		List types = this.find(hql);
		if(types == null)
			types = new ArrayList();
		Map pub = new HashMap();
		pub.put("key", "00000");
		pub.put("value", "����");
		types.add(0, pub);
		return types;
	}

	/**
	 * <p> ��������: getNoticeById|����:����ͨ��ID,����ͨ����Ϣ </p>
	 * @param id ͨ����
	 * @return ����ͨ����Ϣ
	 */
	public UBaseNoticeInfoDO getNoticeById(Long id){
		UBaseNoticeInfoDO notice = new UBaseNoticeInfoDO();
		try{
			notice = (UBaseNoticeInfoDO) getObjectById(UBaseNoticeInfoDO.class,
					id, "id");
			List affixs = fileManager.findFilesInfo("00003", "NOTICE", id.toString());
			notice.setAffixs(affixs);
		}catch (Exception e){
			e.printStackTrace();
		}
		return notice;
	}

	/**
	 * <p> ��������: getObjectById|����:��������ȡ����ؼ�¼ </p>
	 * @param c
	 * @param id
	 * @return
	 */
	public Object getObjectById(Class c, Long id, String colm){
		Object o = null;
		List os = find("from " + c.getName() + " o where o." + colm + " = "
				+ id);
		if(os != null && !os.isEmpty())
			o = os.get(0);
		return o;
	}

	/**
	 * <p> ��������: findNotices|����:�����������,����ͨ����Ϣ�б� </p>
	 * @param notice ������Ϣ
	 * @param pl ��ҳ��Ϣ
	 * @return ����ͨ����Ϣ�б�
	 */
	public List findNotices(String userId, UBaseNoticeInfoDO notice,
			PaginationList pl){
		StringBuffer hql = new StringBuffer();
		ArrayList params = new ArrayList();
		hql
				.append("from UBaseNoticeInfoDO n"
						+ " where (n.type='00000' or n.type in (select systemId from UBaseConfigDO where enabled ='true' and systemId in (select res.systemId"//�޶���ϵͳΪ����
						+ " from UAuthRoleResourceDO res"
						+ " left join res.resMap map where map.resType='PUB' and res.resDetailName like '%ͨ�����%'"
						+ " and res.objectId in (select role.roleId from"
						+ " UAuthRoleUserDO role where role.userId='" + userId
						+ "'))))");
		if(notice.getStatus() != null && !notice.getStatus().equals("")){
			hql.append(" and n.status = ? ");
			params.add(notice.getStatus());
		}
		
		if(notice.getTitle() != null && !notice.getTitle().equals("")){
			hql.append(" and n.title like ? ");
			params.add("%" + notice.getTitle() + "%");
		}
		if(notice.getType() != null && !notice.getType().equals("")){
			hql.append(" and n.type=? ");
			params.add(notice.getType());
		}
		hql.append(" order by n.status desc , n.createTime desc");
		return this.find(hql.toString(), params, pl);
	}
	
	public List findNotices0(String userId, UBaseNoticeInfoDO notice,
			PaginationList pl){
		StringBuffer hql = new StringBuffer();
		ArrayList params = new ArrayList();
		hql
		.append("from UBaseNoticeInfoDO n"
				+ " where (n.type='00000' or n.type in (select systemId from UBaseConfigDO where enabled ='true' and systemId in (select res.systemId"//�޶���ϵͳΪ����
				+ " from UAuthRoleResourceDO res"
				+ " left join res.resMap map where map.resType='PUB' and res.resDetailName like '%ͨ�����%'"
				+ " and res.objectId in (select role.roleId from"
				+ " UAuthRoleUserDO role where role.userId='" + userId
				+ "'))) or n.type in (select role.systemId from UAuthRoleDO role,UAuthRoleUserDO auth where auth.roleId=role.roleId and auth.userId='"+userId+"'))");
		if(notice.getTitle() != null && !notice.getTitle().equals("")){
			hql.append(" and n.title like ? ");
			params.add("%" + notice.getTitle() + "%");
		}
		if(notice.getType() != null && !notice.getType().equals("")){
			hql.append(" and n.type=? ");
			params.add(notice.getType());
		}
		if(notice.getStatus() != null && !notice.getStatus().equals("")){
			hql.append(" and n.status = ? ");
			params.add(notice.getStatus());
		}
		hql.append(" order by n.status desc , n.createTime desc");
		return this.find(hql.toString(),params, pl);
	}
	
	/**
	 * <p> ��������: findSNotices|����:�����������,����ֻ��ͨ����Ϣ�б� </p>
	 * @param notice ������Ϣ
	 * @param pl ��ҳ��Ϣ
	 * @return ����ͨ����Ϣ�б�
	 */
	public List findSNotices(String userId, UBaseNoticeInfoDO notice,
			PaginationList pl){
		StringBuffer hql = new StringBuffer();
		ArrayList params = new ArrayList();
		hql
				.append("from UBaseNoticeInfoDO n"
						+ " where (n.type='00000' or n.type in (select res.systemId"
						+ " from UAuthRoleResourceDO res"
						+ " left join res.resMap map where map.resType='PUB'"
						+ " and res.objectId in (select role.roleId from"
						+ " UAuthRoleUserDO role ,UAuthRoleDO au where role.roleId=au.roleId and au.enabled='1' and role.userId='" + userId
						+ "')))");
		if(notice.getTitle() != null && !notice.getTitle().equals("")){
			hql.append(" and n.title like ?");
			params.add("%" + notice.getTitle() + "%");
		}
		if(notice.getType() != null && !notice.getType().equals("")){
			hql.append(" and n.type=? ");
			params.add(notice.getType());
		}
		if(notice.getStatus() != null && !notice.getStatus().equals("")){
			hql.append(" and n.status = ? ");
			params.add(notice.getStatus());
		}
		
		hql.append(" order by n.status desc , n.createTime desc");
		return this.find(hql.toString(), params, pl);
	}

	/**
	 * <p> ��������: find|����:�����������,����ͨ����־��Ϣ�б� </p>
	 * @param log ������Ϣ
	 * @param pl ��ҳ��Ϣ
	 * @return ����ͨ����־��Ϣ�б�
	 */
	public List findLogs(UBaseNoticeLogDO log, PaginationList pl){
		StringBuffer hql = new StringBuffer();
		hql.append("from UBaseNoticeLogDO n where n.noticeId="
				+ log.getNoticeId());
		ArrayList params = new ArrayList();
		if(log.getUserCName() != null && !log.getUserCName().equals("")){
			hql.append(" and (n.userCName like ? or n.userEName like ? )");
			params.add("%" + log.getUserCName()+ "%");
			params.add("%" + log.getUserCName()+ "%");
		}
		hql.append(" order by n.viewTime desc");
		return this.find(hql.toString(),params, pl);
	}

	/**
	 * <p> ��������: find|����:�����������,����ͨ�淴����Ϣ�б� </p>
	 * @param feedback ������Ϣ
	 * @param pl ��ҳ��Ϣ
	 * @return ����ͨ�淴����Ϣ�б�
	 */
	public List findFeedBacks(UBaseNoticeFeedBackDO feedback, PaginationList pl){
		StringBuffer hql = new StringBuffer();
		ArrayList params = new ArrayList();
		hql.append("from UBaseNoticeFeedBackDO n where n.noticeId="
				+ feedback.getNoticeId());
		if(feedback.getUserCName() != null
				&& !feedback.getUserCName().equals("")){
			hql.append(" and n.userId in (select u.userId from UBaseUserDO u"
					+ " where u.userCname like ? or u.userEname like ? )");
			params.add("%" + feedback.getUserCName()+ "%");
			params.add("%" + feedback.getUserCName()+ "%");
		}
		if(feedback.getContent() != null && !feedback.getContent().equals("")){
			hql.append(" and n.content like ? ");
			params.add("%" +  feedback.getContent()+ "%");
		}
		hql.append(" order by n.feedTime desc");
		return this.find(hql.toString(),params, pl);
	}

	/**
	 * <p> ��������: delete|����:���ݱ��ɾ��ͨ����Ϣ </p>
	 * @param ids �������
	 */
	public void delete(String[] ids, String path){
		if(ids != null && ids.length > 0){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < ids.length; i++){
				sb.append("," + ids[i]);
				fileManager.deleteFile("00003","NOTICE", ids[i]);
			}
			String hql1 = "delete from UBaseNoticeAffixDO n where n.noticeId in ("
				+ sb.substring(1) + ")";
			String hql2 = "delete from UBaseNoticeFeedBackDO n where n.noticeId in ("
					+ sb.substring(1) + ")";
			String hql3 = "delete from UBaseNoticeLogDO n where n.noticeId in ("
					+ sb.substring(1) + ")";
			String hql4 = "delete from UBaseNoticeInfoDO n where n.id in ("
					+ sb.substring(1) + ")";
			this.executeUpdate(hql1);
			this.executeUpdate(hql2);
			this.executeUpdate(hql3);
			this.executeUpdate(hql4);
		}
	}
	/**
	 * �ö�ͨ����Ϣ
	 * @param ids
 	 */
	public void toHeadNotice(String[] ids){
		if(ids != null && ids.length > 0){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < ids.length; i++){
				sb.append("," + ids[i]);
 			}
			String hql1 = "update UBaseNoticeInfoDO n  set n.status = 'true' where n.id in ("
				+ sb.substring(1) + ")"; 
			this.executeUpdate(hql1);
 		}
	}

	/**
	 * <p> ��������: saveNotice|����:����ͨ����Ϣ </p>
	 * @param notice ͨ��������Ϣ
	 * @param path ������·��
	 * @param files �����б�
	 */
	public void saveNotice(UBaseNoticeInfoDO notice, LoginDO user, String path,
			File[] files, List fileNames){
		this.save(notice);
		saveAffixs(path, files, fileNames, notice.getId(), user.getUserCname());
	}

	/**
	 * <p> ��������: saveNotice|����:�޸�ͨ����Ϣ </p>
	 * @param notice ͨ��������Ϣ
	 * @param path ������·��
	 * @param files �����б�
	 * @param ids ��ɾ������ID
	 */
	public void saveNotice(UBaseNoticeInfoDO notice, LoginDO user, String path,
			File[] files, List fileNames, String[] ids){
		this.update(notice);
		if(ids != null && ids.length > 0){ // ɾ������
			for(int i = 0; i < ids.length; i++){
				fileManager.deleteFileById(Long.valueOf(ids[i]).longValue());
			}
		}
		saveAffixs(path, files, fileNames, notice.getId(), user.getUserCname());
	}

	/*
	 * ���渽��
	 */
	private void saveAffixs(String path, File[] files, List fileNames,
			Long noticeId, String userName){
		if(files != null && files.length > 0){
			//idGenerator = IdGenerator.getInstance(DictionaryService.NOTICE_AFFIX_TYPE);
			try{
				for(int i = 0; i < files.length; i++){
					fileManager.upLoadFile("NOTICE","00003",noticeId.toString(), fileNames.get(i).toString(),
							new java.sql.Date(System.currentTimeMillis()),new FileInputStream(files[i]), files[i].length(),null,userName);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	
	/*
	 * ���ɸ�������
	 */
	public String createName(){
		String name = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date());
		long l = (long) Math.floor(Math.random() * 1000);
		if(l < 10)
			name += "00" + l;
		else if(l < 100)
			name += "0" + l;
		else
			name += l;
		return name;
	}

}
