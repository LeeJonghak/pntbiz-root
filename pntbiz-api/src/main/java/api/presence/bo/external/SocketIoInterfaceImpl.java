package api.presence.bo.external;

import core.common.config.domain.InterfaceConfig;
import framework.web.http.RequestClient;
import framework.web.http.RequestData;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Map 표시를 위한 Socket.io 연동 모듈
 *
 * Created by ucjung on 2017-06-16.
 */
@Component
public class SocketIoInterfaceImpl implements ExternalInterface {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Socket socket;

    @Autowired
    private RequestClient requestClient;

    @Override
    public Object request(Map<String, Object> requestParam, InterfaceConfig config) {
        //socketRequest(requestParam);
        socketRequestByRest(requestParam);
        return null;
    }

    private void socketRequestByRest(final Map<String, Object> requestParam) {
        logger.debug("SocketRequest Start ----------------------------------------");
        String url = requestParam.get("url").toString() + "/presence/send/" + requestParam.get("command").toString();
        String body = requestParam.get("body").toString();

        logger.debug("SocketRequest_PARAMETER : {}, {}", url, body);
        Map<String, String>  result = requestClient.request(url, body);
        logger.debug("SocketRequest_RESPONSE : {}, {}", url, result.toString());
        logger.debug("SocketRequest End   ----------------------------------------");
    }

    private void socketRequest(final Map<String, Object> requestParam) {
        final SocketIoInterfaceImpl me = this;
        try {
            logger.debug("SocketRequest Start ----------------------------------------");
            if (socket == null) {
                logger.debug("SocketRequest Socket : null");
                String url = requestParam.get("url").toString();
                if (url.startsWith("https")) {
                    prepareSSLSocket(url);
                } else {
                    socket = IO.socket(url);
                }

                logger.debug("SocketRequest Socket Create Url : {}", url);
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String command = requestParam.get("command").toString();
                        String body = requestParam.get("body").toString();
                        logger.debug("Socket Connected -----------------------");
                        try {
                            logger.debug("Socket Connect Emit Message Start: {} \n{}", command, body);
                            socket.emit(command, body.getBytes("UTF-8"));
                            logger.debug("Socket Connect Emit Message END : {} \n", command);
                        } catch (UnsupportedEncodingException e) {
                            logger.error("UnsupportedEncodingException : {}", e.getMessage());
                        }
                    }
                }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        logger.debug("Connect Error : {}", args[0]);
                    }
                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        logger.debug("disconnected");
                    }
                }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        logger.debug("SocketRequest Event Error : {}", args[0]);
                    }
                });
                socket.connect();
            } else if (socket.connected() == true) {
                String command = requestParam.get("command").toString();
                String body = requestParam.get("body").toString();
                logger.debug("Socket Connected -----------------------");
                try {
                    logger.debug("Socket Emit Message Start: {} \n{}", command, body);
                    socket.emit(command, body.getBytes("UTF-8"));
                    logger.debug("Socket Emit Message END : {} \n", command);
                } catch (UnsupportedEncodingException e) {
                    logger.error("UnsupportedEncodingException : {}", e.getMessage());
                }
            } else {
                logger.info("Socket connect Start : socket not null but connected false");
                socket.connect();
                logger.info("Socket connect End");
            }

        } catch (URISyntaxException e) {
            logger.error("Socket.IO UIR SyntaxException Error : {}", e.getMessage());
        } catch (Exception e) {
            logger.error("SocketRequest Socket.IO Exception Error : {}", e.getMessage());
        }
        logger.debug("SocketRequest End   ----------------------------------------");
    }


    @Override
    @Async
    public Future<Object> requestAsync(Map<String, Object> requestParam, InterfaceConfig config) {
        //socketRequest(requestParam);
        socketRequestByRest(requestParam);
        return new AsyncResult<>(null);
    }

    private void prepareSSLSocket(String url) {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(hv)
                .sslSocketFactory(sc.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .build();

        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);

        IO.Options opts = new IO.Options();
        opts = new IO.Options();
        opts.callFactory = okHttpClient;
        opts.webSocketFactory = okHttpClient;

        try {
            socket = IO.socket(url, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
