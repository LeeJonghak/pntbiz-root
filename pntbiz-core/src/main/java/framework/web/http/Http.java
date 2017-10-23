package framework.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Http {
	
	public static String send(String url, String method, Map<String, String> header, String body) throws IOException {
		URL postUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod(method);		
		
		for(String key : header.keySet()){
			connection.setRequestProperty(key, header.get(key));
		}
//		connection.setRequestProperty("Accept-Charset", "UTF-8");
//		connection.setRequestProperty("Content-Type", "application/json");
//		connection.setRequestProperty("charset", "UTF-8");
		
		if(!method.toLowerCase().equals("delete")) {
			OutputStream os= connection.getOutputStream();
			os.write(body.getBytes());
			os.flush();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String output;
		String result = "";
		while ((output = br.readLine()) != null) {
			//System.out.println(output);
			if(output != null || "".equals(output)) result += output; 
		}
		connection.disconnect();
		return result;
	}
	
}
