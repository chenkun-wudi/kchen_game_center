package foxu.usergeter;

import foxu.dbaccess.UserDBAccess;
import mustang.io.ByteBuffer;

/**
 * 账户获取器
 * 
 * @author kunchen
 *
 */
public interface Getter {
	/** 中心返回用户状态常量 NOT_EXIST不存在  DISABLE禁用  NOTMAL正常  */
	public static final int NOT_EXIST=1,DISABLE=2,NORMAL=3;
	
	/** 获取账户 */
	public void getUser(ByteBuffer data,UserDBAccess userDBAccess);
}
