package foxu.dbaccess;

import mustang.field.Fields;

/**
 * 加载器接口
 * 
 * @author kunchen
 *
 */
public interface DataDBAccess {
	/**保存方法*/
	public boolean save(Object object);
	/** 加载方法 */
	public Object load(String id);
	/**按sql加载*/
	public Fields loadSql(String sql);
	/**按sql加载*/
	public Object[] loadBySql(String sql);
	/** 映射成域对象 */
	public Fields mapping();
	/** 映射成域对象 */
	public Object mapping(Fields fields);
	/** 映射成域对象 */
	public Fields mapping(Object p);
	/** 删除 数据库中记录 */
	public boolean deleteDB(String id);
}
