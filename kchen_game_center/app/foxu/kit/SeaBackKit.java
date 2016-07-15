package foxu.kit;

import mustang.codec.Base64;
import mustang.io.ByteBuffer;

/**
 * 公用方法库
 * 
 * @author kunchen
 *
 */
public class SeaBackKit {
	/** mail格式 */
	public static final String EMAIL_MATCHES="^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
	/** md5格式 */
	public static final String MD5_MATCHES="[a-fA-F0-9]{32,32}";
	/** 账号最大长度 */
	public static final int USER_MAX_LEN=100;
	/** md5码长度 */
	public static final int MD5_LEN=32;
	
	/** 默认的Base64编解码算法 */
	public static final Base64 BASE64=new Base64();
	
	/** 解码base 64字符串 */
	public static ByteBuffer load(String data)
	{
		if(data==null) return null;
		ByteBuffer bb=new ByteBuffer();
		bb.clear();
		BASE64.decode(data,0,data.length(),bb);
		return bb;
	}
	
	/** 检测账户是否合法 */
	public static boolean checkAccount(String mail)
	{
		if(mail==null) return false;
		if(mail.length()>USER_MAX_LEN) return false;
		return mail.matches(EMAIL_MATCHES);
	}
	
	/** 检测是否是md5 */
	public static boolean checkMD5(String md5)
	{
		if(md5==null) return false;
		if(md5.length()!=MD5_LEN)return false;
		return md5.matches(MD5_MATCHES);
	}
	
	/**
	 * Base64编解码算法 二进制数据转化为字符串
	 */
	public static String createBase64(ByteBuffer data)
	{
		byte[] array=data.toArray();
		data.clear();
		BASE64.encode(array,0,array.length,data);
		return new String(data.getArray(),0,data.top());
	}
}
