package foxu.http;

import foxu.base.Player;
import foxu.base.User;
import foxu.dbaccess.PlayerDBAccess;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.serverlist.ServerMap;
import foxu.uid.DeviceIdGeter;
import mustang.io.ByteBuffer;
import mustang.io.ByteBufferThreadLocal;
import mustang.log.LogFactory;
import mustang.log.Logger;
import mustang.orm.SqlPersistence;
import mustang.text.TextKit;
import shelby.httpclient.HttpHandlerInterface;
import shelby.httpserver.HttpRequestMessage;

/**
 * 中心HTTP端口
 * 
 * @author kunchen
 *
 */
public class ServerHttpPort implements HttpHandlerInterface {
	Logger log = LogFactory.getLogger(ServerHttpPort.class);

	/**
	 * 处理类型 GET_SERVERLIST获取服务器列表 GET_DEVICEID获取设备id GET_PLAYER获取账号角色
	 * GET_PLAYER_GUEST获取游客角色,GET_SERVER_MAP获取所有服务器
	 */
	public static final int GET_SERVERLIST = 1, GET_DEVICEID = 2, GET_PLAYER = 3, GET_PLAYER_GUEST = 4,
			GET_SERVER_MAP = 5;
	/** 多组服务器 */
	ServerMap serverMap;
	/** 设备唯一id提供器 */
	DeviceIdGeter deviceGeter;
	/** 角色加载器 */
	PlayerDBAccess playerDBaccess;
	/** 账号加载器 */
	UserDBAccess userDBaccess;
	@Override
	public byte[] excute(HttpRequestMessage request, String ip) {
		ByteBuffer data = ByteBufferThreadLocal.getByteBuffer();
		data.clear();
		try {
			int type = TextKit.parseInt(request.getParameter("type"));
			// 取服务器列表
			if (type == GET_SERVERLIST) {
				String platid = request.getParameter("platid");
				String areaid = request.getParameter("areaid");
				String channel = request.getParameter("channel");
				String vers = request.getParameter("vers");
				serverMap.showBytesWrite(data, platid, areaid, channel, vers);
			}
			// 取设备id
			else if (type == GET_DEVICEID) {
				data.writeUTF(deviceGeter.getDeviceId());
			}
			else if(type==GET_PLAYER||type==GET_PLAYER_GUEST)
			{
				String account=request.getParameter("account");
				getPlayers(type,account,data);
			}
			else if(type==GET_SERVER_MAP)
			{
				data.clear();
				serverMap.bytesWriteToServer(data);
				return SeaBackKit.createBase64(data).getBytes();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("ServerHttpPort Exception request:" + request.toString());
		}
		return data.toArray();
	}
	
	/** 获取角色信息 */
	public void getPlayers(int type,String account,ByteBuffer data)
	{
		User user=null;
		if(type==GET_PLAYER)
		{
			user=userDBaccess.loadByAccount(account);
		}
		else if(type==GET_PLAYER_GUEST)
		{
			user=userDBaccess.getGuest(account);
		}
//		System.out.println(type+"-------user------:"+user);
		if(user==null)
		{
			data.writeByte(0);
			data.writeUTF("user not exist");
		}
		else
		{
			SqlPersistence sqlp=(SqlPersistence)playerDBaccess
				.getGamePersistence();
			String sql="select * from "+sqlp.getTable()+" where user_id="
				+user.getId();
			Player[] ps=playerDBaccess.loadBySql(sql);
//			System.out.println(ps);
			data.writeByte(1);
			if(ps==null)
			{
				data.writeShort(0);
			}
			else
			{
				data.writeShort(ps.length);
				for(int i=0;i<ps.length;i++)
				{
					ps[i].bytesWrite(data);
				}
			}

		}
	}


	@Override
	public String excuteString(HttpRequestMessage arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public ServerMap getServerMap() {
		return serverMap;
	}

	public void setServerMap(ServerMap serverMap) {
		this.serverMap = serverMap;
	}

	public DeviceIdGeter getDeviceGeter() {
		return deviceGeter;
	}

	public void setDeviceGeter(DeviceIdGeter deviceGeter) {
		this.deviceGeter = deviceGeter;
	}

	public PlayerDBAccess getPlayerDBaccess() {
		return playerDBaccess;
	}

	public void setPlayerDBaccess(PlayerDBAccess playerDBaccess) {
		this.playerDBaccess = playerDBaccess;
	}

	public UserDBAccess getUserDBaccess() {
		return userDBaccess;
	}

	public void setUserDBaccess(UserDBAccess userDBaccess) {
		this.userDBaccess = userDBaccess;
	}

}
