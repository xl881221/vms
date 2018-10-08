/*
 * 
 */
package fmss.common.ui;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:06:40
 * @描述: [GridTreeNode]树模型
 */
public class GridTreeNode{

	private String _id;// 菜单序号
	private String _parent;// 上级菜单
	private boolean _is_leaf;// 是否叶子节点

	/**
	 * <p>方法名称: get_id|描述:取得节点ID</p>
	 * @return
	 */
	public String get_id(){
		return _id;
	}

	/**
	 * <p>方法名称: set_id|描述:设置节点ID</p>
	 * @param _id
	 */
	public void set_id(String _id){
		this._id = _id;
	}

	/**
	 * <p>方法名称: get_parent|描述: 取得父节点</p>
	 * @return
	 */
	public String get_parent(){
		return _parent;
	}

	/**
	 * <p>方法名称: set_parent|描述:设置父节点 </p>
	 * @param _parent
	 */
	public void set_parent(String _parent){
		this._parent = _parent;
	}

	/**
	 * <p>方法名称: is_is_leaf|描述:判断是否叶子节点 </p>
	 * @return
	 */
	public boolean is_is_leaf(){
		return _is_leaf;
	}

	/**
	 * <p>方法名称: set_is_leaf|描述: 设置是否为叶子节点</p>
	 * @param _is_leaf
	 */
	public void set_is_leaf(boolean _is_leaf){
		this._is_leaf = _is_leaf;
	}
}
