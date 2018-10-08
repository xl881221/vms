package fmss.common;

import java.util.Date;

import com.easycon.busi.element.UniKeyValueObject;
import com.easycon.busi.util.PackUtil;

import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.services.InstService;
import fmss.services.UserService;
import fmss.common.util.SpringContextUtils;

public class PortalBusiBus extends com.easycon.busi.core.AbstractBusinessBus {


	/** ��Ϣ */
	private String mess;

	public UniKeyValueObject doExecute(UniKeyValueObject ukvo) throws Exception {
		

		UserService userService = (UserService)SpringContextUtils.getBean("userService");
		InstService instService = (InstService)SpringContextUtils.getBean("instService");
		logger.info("userService ====="+ userService);
		logger.info("instService-----"+instService);
		
		logger.info("gbk name ====="+ ukvo.getValue("userName"));
		logger.info("������jym-----"+ukvo.getIntValue("JYM"));
		logger.info("YDM-----"+ukvo.getValue("YDM"));
		logger.info("YDXX-------"+ukvo.getValue("YDXX"));
		logger.info("������----"+ukvo.getValue("deptId"));
		logger.info("��������-----"+ ukvo.getValue("deptname"));
		logger.info("KEY_FIELD------"+ukvo.getIntValue(KEY_FIELD));
		
		int jym = ukvo.getIntValue("JYM");
		UniKeyValueObject backUvo =  null;

		String userId = ukvo.getValue("userId"); // UBaseUserDO����userId
		String userCname = ukvo.getValue("userName");// UBaseUserDO����userCname

		String instId = ukvo.getValue("deptId");// ����ID
		String instName = ukvo.getValue("deptname");// ��������
		String parentInstId = ukvo.getValue("PARENTID");// ������ID
		
		switch (jym) {
		case 1101:// ���������û�����
			backUvo =  new UniKeyValueObject();
			// ��ѯ���е�userId
			UBaseUserDO userInTable = userService.getUserByUserId(userId);
			if (userInTable!=null) {
				String msg = "�û� "+userCname+"(" + userId+")"+ "�û��Ѵ���";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}else if(userService.get(UBaseInstDO.class, instId)==null){
				String msg = "û���ҵ�����" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}else {
				// ���洫�����Ķ������ݿ���
				UBaseUserDO user = new UBaseUserDO();
				user.setUserId(userId);
				user.setUserEname(userId);
				user.setUserCname(userCname);
				user.setInstId(instId);
				user.setIsFirstLogin("1");
				user.setIsUserLocked("0");
				user.setStartDate(new Date());
				user.setCreateTime(new Date());
				user.setEnabled("1");
				userService.saveUserByUserId(user);
				String msg = "�û� "+userCname+"(" + userId+")"+"����ɹ�";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1102:
			backUvo =  new UniKeyValueObject();
			// �����޸��û�����
			UBaseUserDO updateUser = userService.getUserByUserId(userId);
			if (updateUser != null) {
				if(userService.get(UBaseInstDO.class, instId)==null){
					String msg = "û���ҵ����� "+instName+"(" + instId+")";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDXX", msg);
				}else{
					updateUser.setUserCname(userCname);
					updateUser.setInstId(instId);
					userService.update(updateUser);
					String msg = "�û�" + userId + "�޸ĳɹ�";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDM", "00");
					backUvo.addVariable("YDXX", msg);
				}
				
			}else{
				String msg = "û���ҵ��û� "+userCname+"(" + userId+")";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1103:
			backUvo =  new UniKeyValueObject();
			// ����ɾ���û�����
			UBaseUserDO deleteUser = userService.getUserByUserId(userId);
			if (deleteUser != null) {
				userService.deleteById(UBaseUserDO.class, userId);
				String msg = "�û� "+deleteUser.getUserCname()+"(" + userId+")" + "ɾ���ɹ�";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "û���ҵ��û�" + userId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1104:
			// ����ͬ��ȫ���û�����
			backUvo.addVariable("JYM", ""+jym);
			backUvo.addVariable("YDM", "00");
//			String users = ukvo.getValue("USERS");
//			String test = "";
//			String[] str = users.split("\\|");
//			for (int i = 0; i < str.length; i++) {
//				test = str[i];
//				String[] tt = test.split(",");
//				for (int j = 0; j < tt.length; j++) {
//					if (j % 2 == 0) {
//						String userId1 = tt[j];
//						if (j % 2 == 1) {
//							String userCname1 = tt[j];
//							UBaseUserDO userAll = new UBaseUserDO(userId1,
//									userCname1);
//							userService.saveUserByUserId(userAll);
//						}
//					}
//				}
//			}

			break;
		case 1105:
			// �����������ź���
			backUvo =  new UniKeyValueObject();
			// ��ѯ���е�instId
			UBaseInstDO instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			// ���������instId������instId��ͬ�� ��˵���Ѿ��д˻���
			if (instInTable!=null) {
				String msg = "���� "+instName+"(" + instId+")"+ "�����Ѵ���";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			} else {
				// ���洫�����Ķ������ݿ���
				instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, parentInstId);
				// ���������instId������instId��ͬ�� ��˵���Ѿ��д˻���
				if (instInTable==null) {
					String msg = "������ "+parentInstId+ "����������";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDM", "00");
					backUvo.addVariable("YDXX", msg);
				}else{
					UBaseInstDO inst = new UBaseInstDO(instId, instName, parentInstId);
					inst.setInstId(instId);
					inst.setEnabled("true");
					inst.setCreateTime(new Date());
					inst.setStartDate(new Date());
					inst.setInstName(instName);
					inst.setParentInstId(parentInstId);
					instService.saveInst(inst);
					String msg = "���� "+instName+"(" + instId+")" + "����ɹ�";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDM", "00");
					backUvo.addVariable("YDXX", msg);
				}
			}
			break;
		case 1106:
			backUvo =  new UniKeyValueObject();
			// �����޸Ĳ��ź���
			instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			if (instInTable != null) {
				instInTable.setInstName(instName);
				instInTable.setParentInstId(parentInstId);
				instService.updateInst(instInTable);
				String msg = "���� "+instName+"(" + instId+")" + "�޸ĳɹ�";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "û���ҵ�����" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}

			break;
		case 1107:
			backUvo =  new UniKeyValueObject();
			// ����ɾ�����ź���
			instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			if (instInTable != null) {
				instService.deleteById(UBaseInstDO.class, instId);
				String msg = "���� "+instName+"(" + instId+")" + "ɾ���ɹ�";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "û���ҵ�����" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1108:
			// ����ͬ�����в�������
			backUvo.addVariable("JYM", ""+jym);
			backUvo.addVariable("YDM", "00");
//			String depts = ukvo.getValue("DEPTS");
//			String test1 = "";
//			String[] str1 = depts.split("\\|");
//			for (int i = 0; i < str1.length; i++) {
//				test1 = str1[i];
//				String[] tt = test1.split(",");
//				for (int j = 0; j < tt.length; j++) {
//					if (j % 2 == 0) {
//						String instId1 = tt[j];
//						if (j % 2 == 1) {
//							String instName1 = tt[j];
//							UBaseInstDO deptAll = new UBaseInstDO(instId1,
//									instName1);
//							instService.save(deptAll);
//						}
//					}
//				}
//			}

			break;
		default:
			//backUvo = PackUtil.backErrorPack("" + jym, "δ֪�Ľӿں�");
			backUvo=PackUtil.backErrorPack(""+jym, "δ֪�Ľӿں�");
		}
		return backUvo != null ? backUvo : PackUtil.backErrorPack(ukvo.getValue("JYM"), "ͬ���쳣");

	}


	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

}

