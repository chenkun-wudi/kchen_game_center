package foxu.uid;

import mustang.codec.MD5;

/**
 * 设备唯一id获取器
 * 
 * @author kunchen
 *
 */
public class DeviceIdGeter {
	/** 唯一id提供器 */
	UidKit uidKit;
	/** md5编码器 */
	MD5 md5=new MD5();
	public String getDeviceId()
	{
		long id=uidKit.getPlusUid();
		String uid=md5.encode(id+"");
		return uid;
	}
	
	public UidKit getUidKit() {
		return uidKit;
	}

	public void setUidKit(UidKit uidKit) {
		this.uidKit = uidKit;
	}

}
