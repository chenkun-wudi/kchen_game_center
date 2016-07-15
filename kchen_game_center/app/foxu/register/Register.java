package foxu.register;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import foxu.uid.UidKit;
import mustang.io.ByteBuffer;
import mustang.text.TextKit;
import shelby.httpserver.HttpRequestMessage;

/**
 * 注册器
 * 
 * @author kunchen
 *
 */
public abstract class Register {
	/** 渠道最大长度 */
	public static final int CHANNEL_LEN=32;
	/** 单个设备最大账号量 */
	public static final int ACCOUNT_MAX=1000;
	/** 返回类型常量 SUCC成功 FAIL失败 */
	public final static int SUCC=0,FAIL=1;
	
	public ByteBuffer createUser(HttpRequestMessage request, String ip, UserDBAccess userDBAccess, UidKit uidKit) {
		ip = TextKit.replaceAll(ip, "/", "");
		ip = TextKit.split(ip, ":")[0];
		String data_str = (String) request.getParameter("data");
		System.out.println(data_str.length());
		System.out.println(data_str);
		ByteBuffer bb = SeaBackKit.load(data_str);
		System.out.println("----bb----::::" + bb.length());
		int platid = bb.readInt();
		int areaid = bb.readInt();
		String channel = bb.readUTF();
		String device_id = bb.readUTF();
		String account = bb.readUTF();
		String pwd = bb.readUTF();
		System.out.println("----------Register------------");
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
		User user = new User();
		long id = uidKit.getPlusUid();
		user.setId(id);
		user.initAttr(platid, areaid, channel, account, pwd, device_id, ip, User.USER);
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
		if (platid < 1 || platid > 127) {
			bb.writeByte(FAIL);
			bb.writeUTF("platid error");
			return false;
		}
		if (areaid < 1 || areaid > 255) {
			bb.writeByte(FAIL);
			bb.writeUTF("areaid error");
			return false;
		}
		if (channel.length() > CHANNEL_LEN) {
			bb.writeByte(FAIL);
			bb.writeUTF("channel error");
			return false;
		}
		if (!SeaBackKit.checkMD5(device_id)) {
			bb.writeByte(FAIL);
			bb.writeUTF("deviceid error");
			return false;
		}
		return true;
	}
}
