package foxu.register;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import foxu.kit.SeaBackKit;
import mustang.io.ByteBuffer;

/**
 * 一般账号注册器
 * 
 * @author kunchen
 *
 */
public class UserRegister extends Register {
	@Override
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
		User[] old_users = userDBAccess.loadByDeviceId(device_id);
		if (old_users != null && old_users.length > ACCOUNT_MAX) {
			bb.writeByte(FAIL);
			bb.writeUTF("account max");
			return false;
		}

		User user = userDBAccess.loadByAccount(account);
		if (user != null) {
			System.out.println("--------FAIL---00-----");
			bb.writeByte(FAIL);
			bb.writeUTF("had been registered");
			return false;
		}
		return true;
	}
}
