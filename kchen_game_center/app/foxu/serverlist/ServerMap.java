package foxu.serverlist;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import mustang.io.ByteBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 多组服务器
 * @author kunchen
 *
 */
public class ServerMap {
	/** 服务器中心编号 */
	public static int CENTER_ID = 1;
	
	/** 服务器组表 */
	Map<String,ServerGroup> server_map=new HashMap<String,ServerGroup>();

	/** 添加服务器组 */
	public void addGroup(ServerGroup group)
	{
		server_map.put(group.getKey(),group);
	}
	
	/** 写服务器列表给前台 */
	public ByteBuffer showBytesWrite(ByteBuffer data,String platid,
		String areaid,String channel,String vers)
	{
		return server_map.get(platid+"_"+areaid).showBytesWrite(data,
			channel,vers);
	}
	
	/** 写所有服务器列表给其他服务器 */
	public void bytesWriteToServer(ByteBuffer data)
	{
		Map<String,ServerGroup> map=Collections.unmodifiableMap(server_map);
		Entry<String,ServerGroup>[] entris=map.entrySet().toArray(
			new Entry[0]);
		data.writeByte(entris.length);
		for(Entry<String,ServerGroup> entry:entris)
		{
			String key=entry.getKey();
			data.writeUTF(key);
			ServerGroup group=entry.getValue();
			group.bytesWriteToServer(data);
		}

	}
	
	/** 写大区列表给gm工具 */
	public JSONObject writeArea()
	{
		JSONObject areas=new JSONObject();
		JSONArray array=new JSONArray();
		Iterator<Entry<String,ServerGroup>> iter=server_map.entrySet()
			.iterator();
		try
		{
			while(iter.hasNext())
			{
				Entry<String,ServerGroup> entry=iter.next();
				ServerGroup group=entry.getValue();

				JSONObject json=new JSONObject();
				json.put("name",group.getName());
				System.out.println(group.getName());
				json.put("key",group.getKey());
				System.out.println(json.toString());
				array.put(json);

			}
			areas.put("areas",array);
		}
		catch(Exception e)
		{
			// TODO: handle exception
		}
		return areas;
	}
	
	/** 写服务器列表给gm工具 */
	public JSONObject writeServer(String key)
	{
		JSONObject servers=new JSONObject();
		JSONArray array=new JSONArray();
		ServerGroup sg=server_map.get(key);
		try
		{
			if(sg!=null)
			{
				Object[] objs=sg.getServerlist().valueArray();
				System.out.println("----objs-----:"+objs.length);
				for(int i=0;i<objs.length;i++)
				{
					Server server=(Server)objs[i];
					JSONObject json=new JSONObject();
					json.put("ip",server.getIp());
					json.put("httpport",server.getHttp_port());
					json.put("name",server.getName());
					array.put(json);
				}
				servers.put("servers",array);
			}
		}
		catch(Exception e)
		{
		}
		return servers;
	}

	public Map<String, ServerGroup> getServer_map() {
		return server_map;
	}

	public void setServer_map(Map<String, ServerGroup> server_map) {
		this.server_map = server_map;
	}
	
	
}
