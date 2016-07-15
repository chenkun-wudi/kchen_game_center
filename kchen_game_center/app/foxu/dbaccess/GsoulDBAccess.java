package foxu.dbaccess;

import mustang.log.LogFactory;
import mustang.log.Logger;
import mustang.orm.Persistence;
import mustang.orm.SqlKit;
import mustang.orm.SqlPersistence;

/**
 * 一般加载器
 * 
 * @author kunchen
 *
 */
public abstract class GsoulDBAccess implements DataDBAccess {
	/* fields */
	/** 日志记录 */
	protected static final Logger log = LogFactory.getLogger(GsoulDBAccess.class);

	/** 游戏玩家对象持久器 用来控制player表 */
	protected Persistence gamePersistence;

	public Persistence getGamePersistence() {
		return gamePersistence;
	}

	public void setGamePersistence(Persistence gamePersistence) {
		this.gamePersistence = gamePersistence;
	}

	@Override
	public boolean deleteDB(String id) {
		SqlPersistence sp = (SqlPersistence) gamePersistence;
		String sql = "delete from " + sp.getTable() + " where id=" + id;
		SqlKit.execute(sp.getConnectionManager(), sql);
		return true;
	}

}
