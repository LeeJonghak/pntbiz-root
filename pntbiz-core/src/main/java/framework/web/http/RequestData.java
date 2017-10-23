package framework.web.http;

import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-08-29.
 */
public class RequestData {
    private String protocol;
    private String host;
    private Integer port;
    private String path;
    private HttpMethod method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> bodies = new HashMap<>();

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return (port == null)
                ? (protocol == "http") ? 80 : 443
                : port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getBodies() {
        return bodies;
    }

    public void setBodies(Map<String, Object> bodies) {
        this.bodies = bodies;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addBody(String key, Object value) {
        bodies.put(key, value);
    }

    public Object getBody(String key) {
        return bodies.get(key);
    }
}
