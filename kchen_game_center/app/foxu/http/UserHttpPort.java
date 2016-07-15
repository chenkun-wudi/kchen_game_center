package foxu.http;

import java.util.HashMap;

import foxu.base.GmUser;
import foxu.base.Player;
import foxu.dbaccess.GmDBAccess;
import foxu.dbaccess.PlayerDBAccess;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.usergeter.Getter;
import mustang.io.ByteBuffer;
import mustang.log.LogFactory;
import mustang.log.Logger;
import mustang.orm.SqlKit;
import mustang.orm.SqlPersistence;
import mustang.text.TextKit;
import shelby.httpclient.HttpHandlerInterface;
import shelby.httpserver.HttpRequestMessage;

/**
 * 游戏服务器 请求处理账号端口
 * 
 * @author kunchen
 *
 */
public class UserHttpPort implements HttpHandlerInterface {

	private static Logger log = LogFactory.getLogger(UserHttpPort.class);
	/**
	 * 处理类型常量 GET_USER获取用户 CREATE_PLAYER创建角色 SET_PLAYER设置角色信息 GM_USER获取gm账号
	 * CREATE_GM创建gm账号 GM_PWD修改gm密码
	 */
	public static final int GET_USER = 1, CREATE_PLAYER = 2, SET_PLAYER = 3, GM_USER = 4, CREATE_GM = 5, GM_PWD = 6;
	/** 返回常量 SUCC成功 NO_ACCOUNT账号不存在 SAVE_FAIL入库失败 */
	public static final int SUCC = 0, NO_ACCOUNT = 1, SAVE_FAIL = 2;

	/** 账户获取器 */
	HashMap<String, Getter> geter_map = new HashMap<String, Getter>();
	/** 账号读取器 */
	UserDBAccess userDBAccess;
	/** 角色读取器 */
	PlayerDBAccess playerDBAccess;
	/** gm读取器 */
	GmDBAccess gmDBAccess;

	@Override
	public byte[] excute(HttpRequestMessage request, String ip) {
		String stringData = request.getParameter("data");
		int type = TextKit.parseInt(request.getParameter("type"));
		System.out.println("----------typetypetype---------::" + type);
		ByteBuffer data = SeaBackKit.load(stringData);
		if (type == GET_USER) {
			String key = data.readUTF();
			geter_map.get(key).getUser(data, userDBAccess);
		} else if (type == SET_PLAYER) {
			updatePlayer(data);
		} else if (type == GM_USER) {
			getGmUser(data);
		} else if (type == CREATE_GM) {
			createGmUser(data);
		} else if (type == GM_PWD) {
			modifyGmPwd(data);
		}
		return SeaBackKit.createBase64(data).getBytes();
	}

	@Override
	public String excuteString(HttpRequestMessage arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 设置角色 */
	public void updatePlayer(ByteBuffer data) {
		try {
			long user_id = data.readLong();
			int plat_id = data.readUnsignedByte();
			int area_id = data.readUnsignedShort();
			int server_id = data.readInt();
			long id = data.readLong();
			String name = data.readUTF();
			int lv = data.readUnsignedShort();
			int face = data.readUnsignedShort();
			int vip = data.readUnsignedByte();
			Player player = playerDBAccess.load(id + "");
			if (player == null) {
				player = new Player();
				player.setUser_id(user_id);
				player.setPlat_id(plat_id);
				player.setArea_id(area_id);
				player.setServer_id(server_id);
				player.setId(id);
				player.setName(name);
				player.setLv(lv);
				player.setFace(face);
				player.setVip(vip);
				data.clear();
				boolean succ = playerDBAccess.save(player);
				if (!succ) {
					data.writeByte(0);
					log.error("UserHttpPort createPlayer save fail=" + player.toString());
				} else {
					data.writeByte(1);
				}
			} else {
				SqlPersistence sqlp = (SqlPersistence) playerDBAccess.getGamePersistence();
				String sql = "update " + sqlp.getTable() + " set name='" + name + "',lv=" + lv + ",face=" + face
						+ ",vip=" + vip + " where id=" + id;
				SqlKit.execute(sqlp.getConnectionManager(), sql);
				data.clear();
				data.writeByte(1);
			}

		} catch (Exception e) {
			data.clear();
			data.writeByte(0);
			log.error("UserHttpPort updatePlayer fail:" + e.toString());
		}

	}

	/** 获取gm账号 */
	public void getGmUser(ByteBuffer data) {
		System.out.println("------gmDBAccess---::" + gmDBAccess);
		GmUser gm = gmDBAccess.load(data.readUTF());
		data.clear();
		if (gm == null) {
			data.writeByte(NO_ACCOUNT);
		} else {
			data.writeByte(SUCC);
			data.writeUTF(gm.getPwd());
			data.writeShort(gm.getPower());
		}
	}

	/** 创建gm账号 */
	public void createGmUser(ByteBuffer data) {
		GmUser gm = new GmUser();
		gm.setAccount(data.readUTF());
		gm.setPwd(data.readUTF());
		gm.setPower(data.readUnsignedShort());
		boolean succ = gmDBAccess.save(gm);
		data.clear();
		if (!succ) {
			data.writeByte(SAVE_FAIL);
		} else {
			data.writeByte(SUCC);
		}
	}

	/** 修改gm密码 */
	public void modifyGmPwd(ByteBuffer data) {
		GmUser gm = gmDBAccess.load(data.readUTF());
		if (gm == null) {
			data.clear();
			data.writeByte(NO_ACCOUNT);
			return;
		}
		gm.setPwd(data.readUTF());
		gmDBAccess.save(gm);
		data.clear();
		data.writeByte(SUCC);
	}

	public void addGeter(String key, Getter geter) {
		geter_map.put(key, geter);
	}

	public void rmGeter(String key) {
		geter_map.remove(key);
	}

	public HashMap<String, Getter> getGeter_map() {
		return geter_map;
	}

	public void setGeter_map(HashMap<String, Getter> geter_map) {
		this.geter_map = geter_map;
	}

	public UserDBAccess getUserDBAccess() {
		return userDBAccess;
	}

	public void setUserDBAccess(UserDBAccess userDBAccess) {
		this.userDBAccess = userDBAccess;
	}

	public PlayerDBAccess getPlayerDBAccess() {
		return playerDBAccess;
	}

	public void setPlayerDBAccess(PlayerDBAccess playerDBAccess) {
		this.playerDBAccess = playerDBAccess;
	}

	public GmDBAccess getGmDBAccess() {
		return gmDBAccess;
	}

	public void setGmDBAccess(GmDBAccess gmDBAccess) {
		this.gmDBAccess = gmDBAccess;
	}
}
