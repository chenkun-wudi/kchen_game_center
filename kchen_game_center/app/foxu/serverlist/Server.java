package foxu.serverlist;

/**
 * 服务器信息
 * 
 * @author kunchen
 *
 */
public class Server {
	/** 服务器状态常量 即将开启1,维护2,推荐4,空闲8,良好16,繁忙32,爆满64 */
	public static final int OPENLING = 1 << 0, CLOSE = 1 << 1, RECOMMEND = 1 << 2, GOOD = 1 << 3, BUSY = 1 << 4,
			CROWD = 1 << 5;
	/** 服务器类型 (true为测试) */
	boolean test;
	/** 服务器名 */
	String name;
	/** 服务器id */
	int serverid;
	/** 服务器状态 */
	int state = CLOSE;
	/** ip */
	String ip;
	/** 端口 */
	int port;
	/** http端口 */
	int http_port;

	/** 运营商id */
	int plat_id;
	/** 大区id */
	int area_id;

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getServerid() {
		return serverid;
	}

	public void setServerid(int serverid) {
		this.serverid = serverid;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getHttp_port() {
		return http_port;
	}

	public void setHttp_port(int http_port) {
		this.http_port = http_port;
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
