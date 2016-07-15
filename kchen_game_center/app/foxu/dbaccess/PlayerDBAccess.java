package foxu.dbaccess;

import foxu.base.Player;
import mustang.field.FieldKit;
import mustang.field.FieldObject;
import mustang.field.Fields;
import mustang.field.IntField;
import mustang.field.LongField;
import mustang.field.StringField;
import mustang.net.DataAccessException;
import mustang.orm.Persistence;
import mustang.orm.SqlKit;
import mustang.orm.SqlPersistence;

/**
 * 角色映射加载器
 * 
 * @author kunchen
 *
 */
public class PlayerDBAccess extends GsoulDBAccess{

	@Override
	public boolean save(Object player) {
		if(player==null) return false;
		// 将玩家数据映射成域对象存入数据库中
		int t=gamePersistence.set(
			FieldKit.create("id",((Player)player).getId()),mapping(player));
		if(log.isInfoEnabled()) log.info("save, "+t+" "+player);
		return t==Persistence.OK||t==Persistence.ADD;
	}

	@Override
	public Player load(String id) {
		// 构造一个空域（包括了player表所有属性）
		Fields fields=mapping();
		// 使用持久器 对指定name找到相应的值赋值给fields
		int t=gamePersistence.get(FieldKit.create("id",id),fields);
		// 出错情况处理
		if(t==Persistence.EXCEPTION||t==Persistence.RESULTLESS)
		{
			log.warn("PlayerDBAccess load fail======id===="+id);
			return null;
		}
		// 将存有玩家数据的域对象封装成一个玩家对象
		Player p=mapping(fields);
		return p;
	}

	@Override
	public Fields loadSql(String sql) {
		SqlPersistence sp=(SqlPersistence)getGamePersistence();
		Fields array=null;
		try
		{
			array=SqlKit.query(sp.getConnectionManager(),sql);
		}
		catch(Exception e)
		{
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,
				"PlayerDBAccess loadBysql valid, db error");
		}
		return array;
	}

	@Override
	public Player[] loadBySql(String sql) {
		SqlPersistence sp=(SqlPersistence)getGamePersistence();
		Fields[] array=null;
		try
		{
			array=SqlKit.querys(sp.getConnectionManager(),sql);
		}
		catch(Exception e)
		{
			throw new DataAccessException(
				DataAccessException.SERVER_INTERNAL_ERROR,
				"PlayerDBAccess loadBysql valid, db error");
		}
		if(array==null) return null;
		Player[] ps=new Player[array.length];
		for(int i=0;i<array.length;i++)
		{
			ps[i]=mapping(array[i]);
		}
		return ps;
	}

	@Override
	public Fields mapping() {
		FieldObject[] array=new FieldObject[9];
		int i=0;
		array[i++]=FieldKit.create("id",0l);
		array[i++]=FieldKit.create("user_id",0l);
		array[i++]=FieldKit.create("plat_id",0);
		array[i++]=FieldKit.create("area_id",0);
		array[i++]=FieldKit.create("server_id",0);
		array[i++]=FieldKit.create("name",(String)null);
		array[i++]=FieldKit.create("lv",0);
		array[i++]=FieldKit.create("face",0);
		array[i++]=FieldKit.create("vip",0);
		Fields fs=new Fields();
		fs.add(array,0,i);
		return fs;
	}

	@Override
	public Player mapping(Fields fields) {
		Player p=new Player();
		long lon=((LongField)fields.get("id")).value;
		p.setId(lon);
		lon=((LongField)fields.get("user_id")).value;
		p.setUser_id(lon);
		int lin=((IntField)fields.get("plat_id")).value;
		p.setPlat_id(lin);
		lin=((IntField)fields.get("area_id")).value;
		p.setArea_id(lin);
		lin=((IntField)fields.get("server_id")).value;
		p.setServer_id(lin);
		String str=((StringField)fields.get("name")).value;
		p.setName(str);
		lin=((IntField)fields.get("lv")).value;
		p.setLv(lin);
		lin=((IntField)fields.get("face")).value;
		p.setFace(lin);
		lin=((IntField)fields.get("vip")).value;
		p.setVip(lin);
		
		return p;
	}

	@Override
	public Fields mapping(Object player) {
		Player p=(Player)player;
		FieldObject[] array=new FieldObject[8];
		int i=0;
		// array[i++]=FieldKit.create("id",us.getId());
		array[i++]=FieldKit.create("user_id",p.getUser_id());
		array[i++]=FieldKit.create("plat_id",p.getPlat_id());
		array[i++]=FieldKit.create("area_id",p.getArea_id());
		array[i++]=FieldKit.create("server_id",p.getServer_id());
		array[i++]=FieldKit.create("name",p.getName());
		array[i++]=FieldKit.create("lv",p.getLv());
		array[i++]=FieldKit.create("face",p.getFace());
		array[i++]=FieldKit.create("vip",p.getVip());
		Fields fs=new Fields();
		fs.add(array,0,i);
		return fs;
	}
}
