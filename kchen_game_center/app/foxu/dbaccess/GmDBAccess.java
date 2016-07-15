package foxu.dbaccess;

import foxu.base.GmUser;
import mustang.field.FieldKit;
import mustang.field.FieldObject;
import mustang.field.Fields;
import mustang.field.IntField;
import mustang.field.StringField;
import mustang.net.DataAccessException;
import mustang.orm.Persistence;
import mustang.orm.SqlKit;
import mustang.orm.SqlPersistence;

/**
 * gm用户映射加载器
 * 
 * @author kunchen
 *
 */
public class GmDBAccess extends GsoulDBAccess {

	@Override
	public boolean save(Object gm) {
		if (gm == null)
			return false;
		// 将玩家数据映射成域对象存入数据库中
		int t = gamePersistence.set(FieldKit.create("account", ((GmUser) gm).getAccount()), mapping(gm));
		if (log.isInfoEnabled())
			log.info("save, " + t + " " + gm);
		return t == Persistence.OK || t == Persistence.ADD;
	}

	@Override
	public GmUser load(String account) {
		Fields fields = mapping();
		// 使用持久器 对指定name找到相应的值赋值给fields
		int t = gamePersistence.get(FieldKit.create("account", account), fields);
		// 出错情况处理
		if (t == Persistence.EXCEPTION || t == Persistence.RESULTLESS) {
			log.warn("GmDBAccess load fail======account====" + account);
			return null;
		}
		// 将存有玩家数据的域对象封装成一个玩家对象
		GmUser gm = mapping(fields);
		return gm;
	}

	@Override
	public Fields loadSql(String sql) {
		SqlPersistence sp = (SqlPersistence) getGamePersistence();
		Fields array = null;
		try {
			array = SqlKit.query(sp.getConnectionManager(), sql);
		} catch (Exception e) {
			throw new DataAccessException(DataAccessException.SERVER_INTERNAL_ERROR,
					"GmDBAccess loadBysql valid, db error");
		}
		return array;
	}

	@Override
	public GmUser[] loadBySql(String sql) {
		SqlPersistence sp = (SqlPersistence) getGamePersistence();
		Fields[] array = null;
		try {
			array = SqlKit.querys(sp.getConnectionManager(), sql);
		} catch (Exception e) {
			throw new DataAccessException(DataAccessException.SERVER_INTERNAL_ERROR,
					"GmDBAccess loadBysql valid, db error");
		}
		if (array == null)
			return null;
		GmUser[] gms = new GmUser[array.length];
		for (int i = 0; i < array.length; i++) {
			gms[i] = mapping(array[i]);
		}
		return gms;
	}

	@Override
	public Fields mapping() {
		FieldObject[] array = new FieldObject[3];
		int i = 0;
		array[i++] = FieldKit.create("account", (String) null);
		array[i++] = FieldKit.create("pwd", (String) null);
		array[i++] = FieldKit.create("power", 0);
		Fields fs = new Fields();
		fs.add(array, 0, i);
		return fs;
	}

	@Override
	public GmUser mapping(Fields fields) {
		GmUser gm = new GmUser();
		String str = ((StringField) fields.get("account")).value;
		gm.setAccount(str);
		str = ((StringField) fields.get("pwd")).value;
		gm.setPwd(str);
		int lin = ((IntField) fields.get("power")).value;
		gm.setPower(lin);
		return gm;
	}

	@Override
	public Fields mapping(Object g) {
		GmUser gm = (GmUser) g;
		FieldObject[] array = new FieldObject[2];
		int i = 0;
		// array[i++]=FieldKit.create("id",us.getId());
		array[i++] = FieldKit.create("pwd", gm.getPwd());
		array[i++] = FieldKit.create("power", gm.getPower());
		Fields fs = new Fields();
		fs.add(array, 0, i);
		return fs;
	}

}
