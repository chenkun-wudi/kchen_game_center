package foxu.register;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.uid.UidKit;
import mustang.io.ByteBuffer;
import mustang.text.TextKit;
import shelby.httpserver.HttpRequestMessage;

/**
 * 游客注册器
 * 
 * @author kunchen
 *
 */
public class GuestRegister extends Register {
	@Override
	public ByteBuffer createUser(HttpRequestMessage request, String ip, UserDBAccess userDBAccess, UidKit uidKit) {
		ip = TextKit.replaceAll(ip, "/", "");
		ip = TextKit.split(ip, ":")[0];
		String data_str = (String) request.getParameter("data");
		ByteBuffer bb = SeaBackKit.load(data_str);
		int platid = bb.readInt();
		int areaid = bb.readInt();
		String channel = bb.readUTF();
		String device_id = bb.readUTF();
		System.out.println("----------GuestRegister-------GuestRegister--------");
		System.out.println("-------platid-------:" + platid);
		System.out.println("------areaid--------:" + areaid);
		System.out.println("------channel--------:" + channel);
		System.out.println("-------device_id-------:" + device_id);
		bb.clear();
		if (!checkUser(bb, platid, areaid, channel, device_id, null, null, userDBAccess)) {
			return bb;
		}
		User user = new User();
		long id = uidKit.getPlusUid();
		user.setId(id);
		String account = "guest:" + id;
		user.initAttr(platid, areaid, channel, account, null, device_id, ip, User.GUEST);
		if (!userDBAccess.save(user)) {
			System.out.println("--------FAIL---11-----");
			bb.writeByte(FAIL);
			bb.writeUTF("save user fail");
			return bb;
		}
		System.out.println("--------succ--------");
		bb.writeByte(SUCC);
		bb.writeUTF(account);
		return bb;
	}

	@Override
	public boolean checkUser(ByteBuffer bb, int platid, int areaid, String channel, String device_id, String account,
			String pwd, UserDBAccess userDBAccess) {
		if (!super.checkUser(bb, platid, areaid, channel, device_id, account, pwd, userDBAccess)) {
			return false;
		}
		User[] old_users = userDBAccess.loadByDeviceId(device_id);
		if (old_users != null) {
			if (old_users.length > ACCOUNT_MAX) {
				bb.writeByte(FAIL);
				bb.writeUTF("account max");
				return false;
			}
			for (int i = 0; i < old_users.length; i++) {
				if (old_users[i].getUser_type() == User.GUEST) {
					bb.writeByte(FAIL);
					bb.writeUTF("had registered");
					return false;
				}
			}
		}
		return true;
	}
}
