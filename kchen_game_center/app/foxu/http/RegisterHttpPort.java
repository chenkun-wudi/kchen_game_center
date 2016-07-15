package foxu.http;

import java.util.HashMap;

import foxu.dbaccess.UserDBAccess;
import foxu.register.Register;
import foxu.uid.UidKit;
import mustang.io.ByteBuffer;
import mustang.io.ByteBufferThreadLocal;
import mustang.log.LogFactory;
import mustang.log.Logger;
import shelby.httpclient.HttpHandlerInterface;
import shelby.httpserver.HttpRequestMessage;

/**
 * 注册账号端口
 * 
 * @author kunchen
 *
 */
public class RegisterHttpPort implements HttpHandlerInterface {
	Logger log = LogFactory.getLogger(RegisterHttpPort.class);
	/** 账号读取器 */
	UserDBAccess userDBAccess;
	/** 唯一编号提供器 */
	UidKit uidKit;
	/** 注册处理器 */
	HashMap<String, Register> register_map = new HashMap<String, Register>();

	@Override
	public byte[] excute(HttpRequestMessage request, String ip) {
		ByteBuffer data = null;
		try {
			String register = request.getParameter("register");
			data = register_map.get(register).createUser(request, ip, userDBAccess, uidKit);
		} catch (Exception e) {
			data = ByteBufferThreadLocal.getByteBuffer();
			data.clear();
			data.writeByte(1);
			data.writeUTF("other Exception");

			e.printStackTrace();
			log.warn("RegisterHttpPort Exception request:" + request.toString());
		}

		return data.toArray();
	}

	@Override
	public String excuteString(HttpRequestMessage arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addRegister(String key,Register register)
	{
		register_map.put(key,register);
	}
	
	public void rmRegister(String key)
	{
		register_map.remove(key);
	}

	/* properties */
	public UserDBAccess getUserDBAccess() {
		return userDBAccess;
	}

	public void setUserDBAccess(UserDBAccess userDBAccess) {
		this.userDBAccess = userDBAccess;
	}

	public UidKit getUidKit() {
		return uidKit;
	}

	public void setUidKit(UidKit uidKit) {
		this.uidKit = uidKit;
	}

	public HashMap<String, Register> getRegister_map() {
		return register_map;
	}

	public void setRegister_map(HashMap<String, Register> register_map) {
		this.register_map = register_map;
	}
}
