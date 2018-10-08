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


	/** 消息 */
	private String mess;

	public UniKeyValueObject doExecute(UniKeyValueObject ukvo) throws Exception {
		

		UserService userService = (UserService)SpringContextUtils.getBean("userService");
		InstService instService = (InstService)SpringContextUtils.getBean("instService");
		logger.info("userService ====="+ userService);
		logger.info("instService-----"+instService);
		
		logger.info("gbk name ====="+ ukvo.getValue("userName"));
		logger.info("交易码jym-----"+ukvo.getIntValue("JYM"));
		logger.info("YDM-----"+ukvo.getValue("YDM"));
		logger.info("YDXX-------"+ukvo.getValue("YDXX"));
		logger.info("机构码----"+ukvo.getValue("deptId"));
		logger.info("机构名称-----"+ ukvo.getValue("deptname"));
		logger.info("KEY_FIELD------"+ukvo.getIntValue(KEY_FIELD));
		
		int jym = ukvo.getIntValue("JYM");
		UniKeyValueObject backUvo =  null;

		String userId = ukvo.getValue("userId"); // UBaseUserDO类中userId
		String userCname = ukvo.getValue("userName");// UBaseUserDO类中userCname

		String instId = ukvo.getValue("deptId");// 机构ID
		String instName = ukvo.getValue("deptname");// 机构名称
		String parentInstId = ukvo.getValue("PARENTID");// 父机构ID
		
		switch (jym) {
		case 1101:// 调用增加用户函数
			backUvo =  new UniKeyValueObject();
			// 查询表中的userId
			UBaseUserDO userInTable = userService.getUserByUserId(userId);
			if (userInTable!=null) {
				String msg = "用户 "+userCname+"(" + userId+")"+ "用户已存在";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}else if(userService.get(UBaseInstDO.class, instId)==null){
				String msg = "没有找到机构" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}else {
				// 保存传过来的对象到数据库中
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
				String msg = "用户 "+userCname+"(" + userId+")"+"保存成功";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1102:
			backUvo =  new UniKeyValueObject();
			// 调用修改用户函数
			UBaseUserDO updateUser = userService.getUserByUserId(userId);
			if (updateUser != null) {
				if(userService.get(UBaseInstDO.class, instId)==null){
					String msg = "没有找到机构 "+instName+"(" + instId+")";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDXX", msg);
				}else{
					updateUser.setUserCname(userCname);
					updateUser.setInstId(instId);
					userService.update(updateUser);
					String msg = "用户" + userId + "修改成功";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDM", "00");
					backUvo.addVariable("YDXX", msg);
				}
				
			}else{
				String msg = "没有找到用户 "+userCname+"(" + userId+")";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1103:
			backUvo =  new UniKeyValueObject();
			// 调用删除用户函数
			UBaseUserDO deleteUser = userService.getUserByUserId(userId);
			if (deleteUser != null) {
				userService.deleteById(UBaseUserDO.class, userId);
				String msg = "用户 "+deleteUser.getUserCname()+"(" + userId+")" + "删除成功";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "没有找到用户" + userId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1104:
			// 调用同步全部用户函数
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
			// 调用新增部门函数
			backUvo =  new UniKeyValueObject();
			// 查询表中的instId
			UBaseInstDO instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			// 如果表中有instId和现有instId相同的 则说明已经有此机构
			if (instInTable!=null) {
				String msg = "机构 "+instName+"(" + instId+")"+ "机构已存在";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			} else {
				// 保存传过来的对象到数据库中
				instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, parentInstId);
				// 如果表中有instId和现有instId相同的 则说明已经有此机构
				if (instInTable==null) {
					String msg = "父机构 "+parentInstId+ "机构不存在";
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
					String msg = "机构 "+instName+"(" + instId+")" + "保存成功";
					this.setMess(msg);
					backUvo.addVariable("JYM", ""+jym);
					backUvo.addVariable("YDM", "00");
					backUvo.addVariable("YDXX", msg);
				}
			}
			break;
		case 1106:
			backUvo =  new UniKeyValueObject();
			// 调用修改部门函数
			instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			if (instInTable != null) {
				instInTable.setInstName(instName);
				instInTable.setParentInstId(parentInstId);
				instService.updateInst(instInTable);
				String msg = "机构 "+instName+"(" + instId+")" + "修改成功";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "没有找到机构" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}

			break;
		case 1107:
			backUvo =  new UniKeyValueObject();
			// 调用删除部门函数
			instInTable = (UBaseInstDO) instService.get(UBaseInstDO.class, instId);
			if (instInTable != null) {
				instService.deleteById(UBaseInstDO.class, instId);
				String msg = "机构 "+instName+"(" + instId+")" + "删除成功";
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDM", "00");
				backUvo.addVariable("YDXX", msg);
			}else{
				String msg = "没有找到机构" + instId;
				this.setMess(msg);
				backUvo.addVariable("JYM", ""+jym);
				backUvo.addVariable("YDXX", msg);
			}
			break;
		case 1108:
			// 调用同步所有部门数据
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
			//backUvo = PackUtil.backErrorPack("" + jym, "未知的接口号");
			backUvo=PackUtil.backErrorPack(""+jym, "未知的接口号");
		}
		return backUvo != null ? backUvo : PackUtil.backErrorPack(ukvo.getValue("JYM"), "同步异常");

	}


	public String getMess() {
		return mess;
	}

	public void setMess(String mess) {
		this.mess = mess;
	}

}

