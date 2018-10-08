package fmss.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface UBaseConfigDAO extends BaseListDAO {
	public List selectAll() throws DataAccessException;
}
