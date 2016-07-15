package foxu.register;

import foxu.base.Player;
import foxu.base.User;
import foxu.dbaccess.PlayerDBAccess;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.uid.UidKit;
import mustang.io.ByteBuffer;
import mustang.orm.SqlPersistence;
import mustang.text.TextKit;
import shelby.httpserver.HttpRequestMessage;

/**
 * 认正注册器(验证账号密码)
 * 
 * @author kunchen
 *
 */
public class CertifyRegister extends Register {
	/** 角色信息加载器 */
	PlayerDBAccess playerDBAccess;

	@Override
	public ByteBuffer createUser(HttpRequestMessage request, String ip, UserDBAccess userDBAccess, UidKit uidKit) {
		System.out.println("------CertifyRegister---------");
		ip = TextKit.replaceAll(ip, "/", "");
		ip = TextKit.split(ip, ":")[0];
		String data_str = (String) request.getParameter("data");
		System.out.println("------data_str-------:" + data_str + "|");
		ByteBuffer bb = SeaBackKit.load(data_str);
		System.out.println("-----bb-----:" + bb.length());
		int platid = bb.readInt();
		int areaid = bb.readInt();
		String channel = bb.readUTF();
		String device_id = bb.readUTF();
		String account = bb.readUTF();
		String pwd = bb.readUTF();
		System.out.println("----------CertifyRegister------------");
		System.out.println("-------platid-------:" + platid);
		System.out.println("------areaid--------:" + areaid);
		System.out.println("------channel--------:" + channel);
		System.out.println("-------device_id-------:" + device_id);
		System.out.println("-------account-------:" + account);
		System.out.println("-------pwd-------:" + pwd);
		if (account.length() == 0) {
			User[] us = userDBAccess.loadByDeviceId(device_id);
			if (us == null || us.length <= 0) {
				bb.clear();
				bb.writeByte(FAIL);
				bb.writeUTF("account not exist");
				return bb;
			}
			account = us[0].getAccount();
		}
		bb.clear();
		checkUser(bb, platid, areaid, channel, device_id, account, pwd, userDBAccess);
		return bb;

	}

	@Override
	public boolean checkUser(ByteBuffer bb, int platid, int areaid, String channel, String device_id, String account,
			String pwd, UserDBAccess userDBAccess) {
		System.out.println("----------CertifyRegister----------");
		if (!super.checkUser(bb, platid, areaid, channel, device_id, account, pwd, userDBAccess)) {
			return false;
		}
		User user = userDBAccess.loadByAccount(account);
		if (user != null) {
			if (account.startsWith("guest") || user.getPwd().equals(pwd)) {
				bb.writeByte(SUCC);
				bb.writeUTF(account);
				SqlPersistence sqlp = (SqlPersistence) playerDBAccess.getGamePersistence();
				String sql = "select * from " + sqlp.getTable() + " where user_id=" + user.getId();
				Player[] ps = playerDBAccess.loadBySql(sql);
				System.out.println("----------ps----------:" + ps);
				if (ps == null) {
					bb.writeShort(0);
				} else {
					System.out.println("----------ps.length----------:" + ps.length);
					bb.writeShort(ps.length);
					for (int i = 0; i < ps.length; i++) {
						ps[i].bytesWrite(bb);
					}
				}
			} else {
				System.out.println("-------FAIL--pwd error---");
				bb.writeByte(FAIL);
				bb.writeUTF("pwd error");
			}
		} else {
			System.out.println("-------FAIL--account not exist---");
			bb.writeByte(FAIL);
			bb.writeUTF("account not exist");
		}
		return false;
	}

	public PlayerDBAccess getPlayerDBAccess() {
		return playerDBAccess;
	}

	public void setPlayerDBAccess(PlayerDBAccess playerDBAccess) {
		this.playerDBAccess = playerDBAccess;
	}
}
