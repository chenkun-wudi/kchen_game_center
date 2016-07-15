package foxu.register;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.uid.UidKit;
import mustang.io.ByteBuffer;
import mustang.text.TextKit;
import shelby.httpserver.HttpRequestMessage;

/**
 * 绑定账户
 * 
 * @author kunchen
 *
 */
public class BindRegister extends Register {
	public ByteBuffer createUser(HttpRequestMessage request, String ip, UserDBAccess userDBAccess, UidKit uidKit) {
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
		System.out.println("----------BindRegister------------");
		System.out.println("-------platid-------:" + platid);
		System.out.println("------areaid--------:" + areaid);
		System.out.println("------channel--------:" + channel);
		System.out.println("-------device_id-------:" + device_id);
		System.out.println("-------account-------:" + account);
		System.out.println("-------pwd-------:" + pwd);
		bb.clear();
		if (!checkUser(bb, platid, areaid, channel, device_id, account, pwd, userDBAccess)) {
			return bb;
		}
		User user = userDBAccess.getGuest(device_id);
		user.bindAttr(account, pwd);
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

	public boolean checkUser(ByteBuffer bb, int platid, int areaid, String channel, String device_id, String account,
			String pwd, UserDBAccess userDBAccess) {
		if (!super.checkUser(bb, platid, areaid, channel, device_id, account, pwd, userDBAccess)) {
			return false;
		}
		if (!SeaBackKit.checkAccount(account)) {
			bb.writeByte(FAIL);
			bb.writeUTF("account error");
			return false;
		}
		if (!SeaBackKit.checkMD5(pwd)) {
			bb.writeByte(FAIL);
			bb.writeUTF("pwd error");
			return false;
		}
		User user = userDBAccess.loadByAccount(account);
		if (user != null) {
			bb.writeByte(FAIL);
			bb.writeUTF("had been registered");
			return false;
		}
		user = userDBAccess.getGuest(device_id);
		if (user == null) {
			bb.writeByte(FAIL);
			bb.writeUTF("not guest user");
			return false;
		}
		return true;
	}
}
