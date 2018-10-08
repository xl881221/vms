/**
 * 
 */
package fmss.services;

import fmss.common.db.BaseEntityManager;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:03:08
 * @描述: [AuthObjectService]权限主体服务类
 */
public class AuthObjectService extends CommonService{

	/** bem 实体管理器 */
	private BaseEntityManager bem;

	/**
	 * @return 实体管理器
	 */
	public BaseEntityManager getBem(){
		return bem;
	}

	/**
	 * @param bem 要设置的 实体管理器
	 */
	public void setBem(BaseEntityManager bem){
		this.bem = bem;
	}

	/* （非 Javadoc）
	* <p>重写方法: save|描述:保存权限主体对象 </p>
	* @param object
	* @see fmss.services.CommonService#save(java.lang.Object)
	*/
	public void save(Object object){
		this.bem.save(object);
	}

}
