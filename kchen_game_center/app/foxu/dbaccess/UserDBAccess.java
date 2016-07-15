package foxu.dbaccess;

import foxu.base.User;
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

public class UserDBAccess extends GsoulDBAccess {

	/** 通过账号加载user */
	public User loadByAccount(String account)
	{
		Fields fields=mapping();
		int t=gamePersistence.get(FieldKit.create("account",account),fields);
		if(t==Persistence.EXCEPTION||t==Persistence.RESULTLESS)
		{
			log.warn("UserDBAccess load fail======iaccountd===="+account);
			return null;
		}
		User user=mapping(fields);
		return user;
	}
	
	/** 通过设备id加载user */
	public User[] loadByDeviceId(String device_id)
	{
		String sql="select * from user where create_udid='"+device_id+"'";
		return loadBySql(sql);
	}
	
	public User getGuest(String device_id)
	{
		User[] users=loadByDeviceId(device_id);
		if(users==null) return null;
		for(int i=0;i<users.length;i++)
		{
			if(users[i].getUser_type()==User.GUEST)
			{
				return users[i];
			}
		}
		return null;
	}
	
	@Override
	public boolean save(Object user) {
		if (user == null)
			return false;
		// 将玩家数据映射成域对象存入数据库中
		int t = gamePersistence.set(FieldKit.create("id", ((User) user).getId()), mapping(user));
		if (log.isInfoEnabled())
			log.info("save, " + t + " " + user);
		return t == Persistence.OK || t == Persistence.ADD;
	}

	@Override
	public Object load(String id) {
		Fields f = mapping();
		int t = gamePersistence.get(FieldKit.create("id", id), f);
		if (t == Persistence.EXCEPTION || t == Persistence.RESULTLESS) {
			log.warn("UserDBAccess load fail======id====" + id);
			throw new DataAccessException(DataAccessException.SERVER_INTERNAL_ERROR, "UserDBAccess db error");
		}
		// 将存有玩家数据的域对象封装成一个玩家对象
		User user = mapping(f);
		return user;
	}

	@Override
	public Fields loadSql(String sql) {
		// TODO Auto-generated method stub
		SqlPersistence sp = (SqlPersistence) getGamePersistence();
		Fields array = null;
		try {
			array = SqlKit.query(sp.getConnectionManager(), sql);
		} catch (Exception e) {
			throw new DataAccessException(DataAccessException.SERVER_INTERNAL_ERROR,
					"UserDBAccess loadBysql valid, db error");
		}
		return array;
	}

	@Override
	public User[] loadBySql(String sql) {
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
				"UserDBAccess loadBysql valid, db error");
		}
		if(array==null) return null;
		User[] users=new User[array.length];
		for(int i=0;i<array.length;i++)
		{
			users[i]=mapping(array[i]);
		}
		return users;
	}

	@Override
	/** 映射成域对象 */
	public Fields mapping() {
		FieldObject[] array = new FieldObject[11];
		int i = 0;
		array[i++] = FieldKit.create("id", 0l);
		array[i++] = FieldKit.create("channel", (String) null);
		array[i++] = FieldKit.create("state", 0);
		array[i++] = FieldKit.create("user_type", 0);
		array[i++] = FieldKit.create("account", (String) null);
		array[i++] = FieldKit.create("pwd", (String) null);
		array[i++] = FieldKit.create("create_udid", (String) null);
		array[i++] = FieldKit.create("create_ip", (String) null);
		array[i++] = FieldKit.create("create_time", 0);
		array[i++] = FieldKit.create("plat_id", 0);
		array[i++] = FieldKit.create("area_id", 0);
		Fields fs = new Fields();
		fs.add(array, 0, i);
		return fs;
	}

	@Override
	/** 映射成域对象 */
	public User mapping(Fields fields) {
		// TODO Auto-generated method stub
		User user = new User();
		long lon = ((LongField) fields.get("id")).value;
		user.setId(lon);
		String str = ((StringField) fields.get("channel")).value;
		user.setChannel(str);
		int lin = ((IntField) fields.get("state")).value;
		user.setState(lin);
		lin = ((IntField) fields.get("user_type")).value;
		user.setUser_type(lin);
		str = ((StringField) fields.get("account")).value;
		user.setAccount(str);
		str = ((StringField) fields.get("pwd")).value;
		user.setPwd(str);
		str = ((StringField) fields.get("create_udid")).value;
		user.setCreate_udid(str);
		str = ((StringField) fields.get("create_ip")).value;
		user.setCreate_ip(str);
		lin = ((IntField) fields.get("create_time")).value;
		user.setCreate_time(lin);
		lin = ((IntField) fields.get("plat_id")).value;
		user.setPlat_id(lin);
		lin = ((IntField) fields.get("area_id")).value;
		user.setArea_id(lin);
		return user;
	}

	@Override
	/** 映射成域对象 */
	public Fields mapping(Object user) {
		User us = (User) user;
		FieldObject[] array = new FieldObject[10];
		int i = 0;
		// array[i++]=FieldKit.create("id",us.getId());
		array[i++] = FieldKit.create("channel", us.getChannel());
		array[i++] = FieldKit.create("state", us.getState());
		array[i++] = FieldKit.create("user_type", us.getUser_type());
		array[i++] = FieldKit.create("account", us.getAccount());
		array[i++] = FieldKit.create("pwd", us.getPwd());
		array[i++] = FieldKit.create("create_udid", us.getCreate_udid());
		array[i++] = FieldKit.create("create_ip", us.getCreate_ip());
		array[i++] = FieldKit.create("create_time", us.getCreate_time());
		array[i++] = FieldKit.create("plat_id", us.getPlat_id());
		array[i++] = FieldKit.create("area_id", us.getArea_id());
		Fields fs = new Fields();
		fs.add(array, 0, i);
		return fs;
	}
}
