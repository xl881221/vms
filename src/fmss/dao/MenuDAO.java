package fmss.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface MenuDAO extends BaseListDAO {

	public List selectAll() throws DataAccessException;

	
	
	public List selectByCode(String code) throws DataAccessException;

}