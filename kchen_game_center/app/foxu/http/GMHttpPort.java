package foxu.http;

import foxu.serverlist.ServerMap;
import mustang.log.LogFactory;
import mustang.log.Logger;
import shelby.httpclient.HttpHandlerInterface;
import shelby.httpserver.HttpRequestMessage;

/**
 * GM HTTP端口
 * @author kunchen
 *
 */
public class GMHttpPort implements HttpHandlerInterface{
	/** 操作常量  */
	public static final int GET_AREA_LIST=3,GET_SERVER_LIST=4;
	/* fields */
	Logger log=LogFactory.getLogger(GMHttpPort.class);
	/** 多组服务器 */
	ServerMap serverMap;
	
	@Override
	public byte[] excute(HttpRequestMessage request, String ip) {
		// 获取运营数据
		int type=Integer.parseInt(request.getParameter("table_type"));
		try
		{
			// 获取大区列表
			if(type==GET_AREA_LIST)
			{
				String strAreas=serverMap.writeArea().toString();
				System.out.println(strAreas);
				return strAreas.getBytes("UTF-8");
			}
			else if(type==GET_SERVER_LIST)
			{
				String key=request.getParameter("key");
				String strServer=serverMap.writeServer(key).toString();
				return strServer.getBytes("UTF-8");
			}
		}
		catch(Exception e)
		{
			log.error("--GMHttpPort--error--type--:"+type+","+e.toString());
			e.printStackTrace();
		}
		return new byte[]{};
	}

	@Override
	public String excuteString(HttpRequestMessage arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* properties */
	public ServerMap getServerMap() {
		return serverMap;
	}

	public void setServerMap(ServerMap serverMap) {
		this.serverMap = serverMap;
	}

	
}
