package foxu.serverlist;

import java.util.HashMap;

import mustang.set.IntKeyHashMap;
import mustang.set.ObjectArray;

/**
 * 一组服务器
 * @author kunchen
 *
 */
public class ServerGroup {
	/** 运营商id */
	int platid;
	/** 大区id */
	int areaid;
	/** 名称 */
	String name;
	/** 服务器列表 */
	IntKeyHashMap serverlist=new IntKeyHashMap();
	/** 测试版本号 (前端) */
	HashMap<String,Version> test_ver=new HashMap<String,Version>();
	/** 正式版本号 (前端) */
	HashMap<String,Version> version=new HashMap<String,Version>();
	/** 联系方式 (邮箱) */
	ObjectArray contack;
	
	
	/** 获取key值 */
	public String getKey()
	{
		return platid+"_"+areaid;
	}
	
	/** 添加服务器 */
	public void addServers(Server[] servers)
	{
		for(int i=0;i<servers.length;i++)
		{
			serverlist.put(servers[i].getServerid(),servers[i]);
		}
	}
	
	/** 添加测试版 */
	public void addTestVersions(Version[] versions)
	{
		for(int i=0;i<versions.length;i++)
		{
			test_ver.put(versions[i].getName(),versions[i]);
		}
	}
	
	/** 添加正式版 */
	public void addVersions(Version[] versions)
	{
		for(int i=0;i<versions.length;i++)
		{
			version.put(versions[i].getName(),versions[i]);
		}
	}
	
	/** 添加联系方式 */
	public void addEmails(String[] mails)
	{
		contack=new ObjectArray(mails);
	}
}
