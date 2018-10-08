package fmss.dao;

import fmss.dao.entity.LoginDO;

public interface LoginDAO {
	public LoginDO getUserByIdAndPwd(String userId,String userPassword);
	
	
}
