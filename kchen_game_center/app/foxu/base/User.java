package foxu.base;

import mustang.util.TimeKit;

/**
 * 用户
 * 
 * @author kunchen
 *
 */
public class User {
	/** 账号状态常量  NORMAL正常 DISABLE禁用 DELETE删除*/
	public static final int NORMAL=0,DISABLE=1;//DELETE=2;
	/** 用户类型常量  GUEST游客  USER正式 */
	public static final int GUEST=0,USER=1;
	/* fields */
	/** 用户ID */
	long id;
	/** 用户渠道 */
	String channel;
	/** 用户类型 */
	int user_type;
	/** 账号状态 */
	int state;
	/** 账号 */
	String account;
	/** password */
	String pwd;
	/** 创建设备udid */
	String create_udid;
	/** 创建 ip */
	String create_ip;
	/** 创建时间 */
	int create_time;
	/** 运营商id */
	int plat_id;
	/** 大区 id */
	int area_id;

	/* methods */
	/** 初始化属性 */
	public void initAttr(int plat_id,int area_id,String channel,
		String account,String pwd,String device_id,String ip,int user_type)
	{
		this.plat_id=plat_id;
		this.area_id=area_id;
		this.channel=channel;
		this.account=account;
		this.pwd=pwd;
		this.user_type=user_type;
		create_udid=device_id;
		create_ip=ip;
		create_time=TimeKit.getSecondTime();
	}
	
	/** 绑定角色属性 */
	public void bindAttr(String account,String pwd)
	{
		this.account=account;
		this.pwd=pwd;
		user_type=User.USER;
	}
	
	/* properties */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCreate_udid() {
		return create_udid;
	}

	public void setCreate_udid(String create_udid) {
		this.create_udid = create_udid;
	}

	public String getCreate_ip() {
		return create_ip;
	}

	public void setCreate_ip(String create_ip) {
		this.create_ip = create_ip;
	}

	public int getCreate_time() {
		return create_time;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

	public int getPlat_id() {
		return plat_id;
	}

	public void setPlat_id(int plat_id) {
		this.plat_id = plat_id;
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

}

