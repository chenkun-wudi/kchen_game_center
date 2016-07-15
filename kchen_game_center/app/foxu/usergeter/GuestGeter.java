package foxu.usergeter;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import mustang.io.ByteBuffer;

/**
 * 获取游客账户
 * 
 * @author kunchen
 *
 */
public class GuestGeter implements Getter {

	/** 获取游客账户 */
	@Override
	public void getUser(ByteBuffer data, UserDBAccess userDBAccess) {
		String device_id = data.readUTF();
		data.clear();
		User user = null;
		User[] users = userDBAccess.loadByDeviceId(device_id);
		for (int i = 0; i < users.length; i++) {
			if (users[i].getUser_type() == User.GUEST) {
				user = users[i];
			}
		}
		if (user == null) {
			data.writeByte(NOT_EXIST);
			return;
		}
		if (user.getState() == User.DISABLE) {
			data.writeByte(DISABLE);
			return;
		}
		data.writeByte(NORMAL);
		data.writeUTF(user.getAccount());
		data.writeLong(user.getId());
	}
}
