package foxu.usergeter;

import foxu.base.User;
import foxu.dbaccess.UserDBAccess;
import mustang.io.ByteBuffer;

/**
 * 获取一般账户
 * 
 * @author kunchen
 *
 */
public class UserGeter implements Getter {

	@Override
	public void getUser(ByteBuffer data, UserDBAccess userDBAccess) {
		String account = data.readUTF();
		data.clear();
		User user = userDBAccess.loadByAccount(account);
		if (user == null) {
			data.writeByte(NOT_EXIST);
			return;
		}
		if (user.getState() == User.DISABLE) {
			data.writeByte(DISABLE);
			return;
		}
		data.writeByte(NORMAL);
		data.writeUTF(user.getPwd());
		data.writeLong(user.getId());

	}

}
