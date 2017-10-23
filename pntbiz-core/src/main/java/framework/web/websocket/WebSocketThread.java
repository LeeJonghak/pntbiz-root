package framework.web.websocket;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebSocketThread  extends Thread {

    public String url;
    public String method;
    public Integer readTimeout;
    public String data;

    @Override
    public void run() {
        try {
            send(this.url, this.method, this.readTimeout, this.data);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static String send(String url, String method, Integer readTimeout, String data) throws IOException {
        HttpResponse response = httpRequest(url, readTimeout, data);
        checkResponseStatus(response);
        return getResponseData(response);
    }

    private static String getResponseData(HttpResponse response) throws IOException {
        StringBuilder responseData= new StringBuilder();

        HttpEntity ent = response.getEntity();
        InputStream inpst = ent.getContent();
        BufferedReader rd= new BufferedReader(new InputStreamReader(inpst));
        String line;
        while ((line=rd.readLine())!=null)
        {
            responseData.append(line);
        }

        return responseData.toString();
    }

    private static void checkResponseStatus(HttpResponse response) throws HttpResponseException {
        Integer statCode = response.getStatusLine().getStatusCode();

        if (statCode != 200)
            throw new HttpResponseException(statCode, response.getStatusLine().toString());
    }

    private static HttpResponse httpRequest(String url, Integer readTimeout, String data) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, readTimeout);
        HttpClient client= new DefaultHttpClient(httpParameters);

        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "application/json; charset=UTF-8");

        httppost.setEntity(new StringEntity(data, "UTF-8"));

        return client.execute(httppost);
    }
}
