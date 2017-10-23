package framework.web.nodejs;

import framework.web.util.JsonUtil;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class webSocketThread  extends Thread {
	
	public String url;
	public String method;
	public Integer readTimeout;
	public Map<String, String> data;

	@Override
	public void run() {
		try {
			send(this.url, this.method, this.readTimeout, this.data);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public static void send(String url, String method, Integer readTimeout, Map<String, String> data) throws Exception {
        URL urlAddr = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlAddr.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setReadTimeout(readTimeout);  
        
        int status = 0;
		JsonUtil res = new JsonUtil(data);
		
        try {
        	OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(res.toJson());
            writer.flush();            
            status = conn.getResponseCode(); 
            System.out.println(status);
            conn.disconnect();
            //System.exit(0);
        } catch(Exception e) {
        	conn.disconnect();
        } finally {
        	conn.disconnect();
        	//System.exit(0);
        }
        
    }
}
