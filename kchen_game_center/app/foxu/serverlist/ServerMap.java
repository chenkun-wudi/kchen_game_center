package foxu.serverlist;

import java.util.HashMap;
import java.util.Map;

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
}
