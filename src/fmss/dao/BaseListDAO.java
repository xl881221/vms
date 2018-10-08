package fmss.dao;

import java.util.List;
import java.util.Map;



public interface BaseListDAO {

     public int deleteById(long id);
     public List selectByForm(Map map);
}
