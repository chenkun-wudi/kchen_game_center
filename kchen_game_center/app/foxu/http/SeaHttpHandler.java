package foxu.http;

import mustang.log.LogFactory;
import mustang.log.Logger;
import mustang.text.TextKit;
import shelby.httpclient.HttpPortService;
import shelby.httpserver.HttpHandler;
import shelby.httpserver.HttpRequestMessage;
import shelby.httpserver.HttpResponseMessage;

/**
 * GM操作handler
 * 
 * @author kunchen
 *
 */
public class SeaHttpHandler implements HttpHandler {

	public static final byte[] prot="port is null".getBytes();
	HttpPortService httpService;

	public static Logger log=LogFactory.getLogger(SeaHttpHandler.class);
	@Override
	public HttpResponseMessage handle(HttpRequestMessage request, String ip) {
		log.info("-------------------handle---------------："+request.toString());
		byte responses[] = prot;
		HttpResponseMessage response;
		if(request.getParameter("port")!=null&&!request.getParameter("port").equals(""))
		{
			int port = Integer.parseInt(request.getParameter("port"));
			responses = httpService.getPort(port).excute(request,ip);
		}
		//回应
		response=new HttpResponseMessage();
		response.setContentType("text/plain");
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		response.getHeaders().put("Access-Control-Allow-Origin","*");
		response.appendBody(responses);
		return response;
	}

	public HttpPortService getHttpService() {
		return httpService;
	}

	public void setHttpService(HttpPortService httpService) {
		this.httpService = httpService;
	}

}
