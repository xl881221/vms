/*
 * 
 */
package fmss.common.ui;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:06:40
 * @����: [GridTreeNode]��ģ��
 */
public class GridTreeNode{

	private String _id;// �˵����
	private String _parent;// �ϼ��˵�
	private boolean _is_leaf;// �Ƿ�Ҷ�ӽڵ�

	/**
	 * <p>��������: get_id|����:ȡ�ýڵ�ID</p>
	 * @return
	 */
	public String get_id(){
		return _id;
	}

	/**
	 * <p>��������: set_id|����:���ýڵ�ID</p>
	 * @param _id
	 */
	public void set_id(String _id){
		this._id = _id;
	}

	/**
	 * <p>��������: get_parent|����: ȡ�ø��ڵ�</p>
	 * @return
	 */
	public String get_parent(){
		return _parent;
	}

	/**
	 * <p>��������: set_parent|����:���ø��ڵ� </p>
	 * @param _parent
	 */
	public void set_parent(String _parent){
		this._parent = _parent;
	}

	/**
	 * <p>��������: is_is_leaf|����:�ж��Ƿ�Ҷ�ӽڵ� </p>
	 * @return
	 */
	public boolean is_is_leaf(){
		return _is_leaf;
	}

	/**
	 * <p>��������: set_is_leaf|����: �����Ƿ�ΪҶ�ӽڵ�</p>
	 * @param _is_leaf
	 */
	public void set_is_leaf(boolean _is_leaf){
		this._is_leaf = _is_leaf;
	}
}
