package foxu.serverlist;

import java.util.HashMap;

import mustang.io.ByteBuffer;
import mustang.set.IntKeyHashMap;
import mustang.set.ObjectArray;

/**
 * 一组服务器
 * 
 * @author kunchen
 *
 */
public class ServerGroup {
	/** 最大服务器id */
	public static final int MAX_SERVERID = 65535;
	/** 运营商id */
	int platid;
	/** 大区id */
	int areaid;
	/** 名称 */
	String name;
	/** 服务器列表 */
	IntKeyHashMap serverlist = new IntKeyHashMap();
	/** 测试版本号 (前端) */
	HashMap<String, Version> test_ver = new HashMap<String, Version>();
	/** 正式版本号 (前端) */
	HashMap<String, Version> version = new HashMap<String, Version>();
	/** 联系方式 (邮箱) */
	ObjectArray contack;

	/** 获取key值 */
	public String getKey() {
		return platid + "_" + areaid;
	}

	/** 添加服务器 */
	public void addServers(Server[] servers) {
		for (int i = 0; i < servers.length; i++) {
			serverlist.put(servers[i].getServerid(), servers[i]);
		}
	}

	/** 添加测试版 */
	public void addTestVersions(Version[] versions) {
		for (int i = 0; i < versions.length; i++) {
			test_ver.put(versions[i].getName(), versions[i]);
		}
	}

	/** 添加正式版 */
	public void addVersions(Version[] versions) {
		for (int i = 0; i < versions.length; i++) {
			version.put(versions[i].getName(), versions[i]);
		}
	}

	/** 添加联系方式 */
	public void addEmails(String[] mails) {
		contack = new ObjectArray(mails);
	}

	/** 写服务器信息给前台 */
	public ByteBuffer showBytesWrite(ByteBuffer data, String channel, String vers) {
		Version ver = test_ver.get(channel);
		System.out.println("-------channel-------:" + channel);
		if (ver.getVer().equals(vers)) {
			System.out.println("---test--ver--------");
			ver.showBytesWrite(data);
			showBytesWriteList(data, true);
		} else {
			System.out.println("---real--ver--------");
			ver = version.get(channel);
			ver.showBytesWrite(data);
			showBytesWriteList(data, false);

		}
		bytesWriteMail(data);
		return data;
	}

	/** 写服务器列表给前台 */
	public void showBytesWriteList(ByteBuffer data, boolean test) {
		int len = 0;
		int top = data.top();
		data.writeShort(len);
		System.out.println("---------test--------:" + test);
		for (int i = 1; i <= MAX_SERVERID; i++) {
			Server server = (Server) serverlist.get(i);
			if (server == null)
				continue;
			if (test != server.isTest())
				continue;
			bytesWritePlatArea(data, server);
			server.showBytesWrite(data);
			len++;
		}
		if (len > 0) {
			System.out.println("-----showBytesWriteList-----len-------:" + len);
			int nowTop = data.top();
			data.setTop(top);
			data.writeShort(len);
			data.setTop(nowTop);
		}
	}

	/** 写运营商id&大区id */
	public void bytesWritePlatArea(ByteBuffer data, Server server) {
		int p_id = server.getPlat_id() == 0 ? platid : server.getPlat_id();
		int a_id = server.getArea_id() == 0 ? platid : server.getArea_id();
		data.writeInt(p_id);
		data.writeInt(a_id);
	}

	/** 写联系方式 */
	public void bytesWriteMail(ByteBuffer data) {
		Object[] objs = contack.getArray();
		data.writeShort(objs.length);
		System.out.println("----------bytesWriteMail.length---------:" + objs.length);
		for (int i = 0; i < objs.length; i++) {
			data.writeUTF((String) objs[i]);
		}
	}

	public void bytesWriteToServer(ByteBuffer data) {
		data.writeByte(platid);// 运营id
		data.writeShort(areaid);// 大区id
		// 写入服务器列表
		int[] keys = serverlist.keyArray();
		data.writeShort(keys.length);
		for (int i = 0; i < keys.length; i++) {
			Server server = (Server) serverlist.get(keys[i]);
			server.bytesWriteToServer(data);
		}
	}

	public int getPlatid() {
		return platid;
	}

	public void setPlatid(int platid) {
		this.platid = platid;
	}

	public int getAreaid() {
		return areaid;
	}

	public void setAreaid(int areaid) {
		this.areaid = areaid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IntKeyHashMap getServerlist() {
		return serverlist;
	}

	public void setServerlist(IntKeyHashMap serverlist) {
		this.serverlist = serverlist;
	}

	public HashMap<String, Version> getTest_ver() {
		return test_ver;
	}

	public void setTest_ver(HashMap<String, Version> test_ver) {
		this.test_ver = test_ver;
	}

	public HashMap<String, Version> getVersion() {
		return version;
	}

	public void setVersion(HashMap<String, Version> version) {
		this.version = version;
	}

	public ObjectArray getContack() {
		return contack;
	}

	public void setContack(ObjectArray contack) {
		this.contack = contack;
	}

}
