package framework.web.http;

import framework.util.JsonUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-08-29.
 */
@Component
public class RequestClient {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private RestTemplate httpRestTemplate;
    private RestTemplate httpsRestTemplate;

    public RequestClient() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(1000);
        factory.setConnectTimeout(1000);
        this.httpRestTemplate = new RestTemplate(factory);

        HttpComponentsClientHttpRequestFactory httpsFacotry = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(1000);
        factory.setConnectTimeout(1000);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContexts.custom()
                    .useProtocol("SSL")
                    // .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                    .loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                            return true;
                        }
                    }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).setSslcontext(sslcontext);
        factory.setHttpClient(httpClientBuilder.build());

        this.httpsRestTemplate = new RestTemplate(factory);
    }

    public Map<String, String> request(RequestData requestData) {
        Map<String, String> result = new HashMap<>();

        UriComponentsBuilder builder = UriComponentsBuilder
                .newInstance()
                .scheme(requestData.getProtocol())
                .host(requestData.getHost())
                .port(requestData.getPort())
                .path(requestData.getPath());

        HttpHeaders headers = new HttpHeaders();

        for (String key : requestData.getHeaders().keySet()) {
            headers.add(key, requestData.getHeader(key));
        }

        String url = getUrl(requestData, builder);


        try {
            HttpEntity<String> entity = new HttpEntity<>(JsonUtils.writeValue(requestData.getBodies()), headers);
            result.put("requestMessage",(requestData.getMethod() == HttpMethod.GET)
                    ? url
                    : entity.getBody());

            ResponseEntity<String> response = (requestData.getProtocol().equals("http"))
                    ? httpRestTemplate.exchange(url, requestData.getMethod(), entity, String.class)
                    : httpsRestTemplate.exchange(url, requestData.getMethod(), entity, String.class);

            result.put("responseCode", response.getStatusCode().toString());
            result.put("responseMessage", response.getBody());
        }
        catch (Exception e) {
            result.put("responseCode", "500");
            result.put("responseMessage", e.getMessage());
            logger.error("Request RestAPI Error : {}", e.getMessage());
        }
        result.put("target", url);

        return result;
    }

    public Map<String, String> request(String url, String body) {
        Map<String, String> result = new HashMap<>();

        try {
            HttpEntity<String> entity = new HttpEntity<>(body);
            result.put("requestMessage",entity.getBody());

            ResponseEntity<String> response = (url.startsWith("http:"))
                    ? httpRestTemplate.exchange(url, HttpMethod.POST, entity, String.class)
                    : httpsRestTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            result.put("responseCode", response.getStatusCode().toString());
            result.put("responseMessage", response.getBody());
        }
        catch (Exception e) {
            result.put("responseCode", "500");
            result.put("responseMessage", e.getMessage());
            logger.error("Request RestAPI Error : {}", e.getMessage());
        }
        result.put("target", url);

        return result;

    }

    private String getUrl(RequestData requestData, UriComponentsBuilder builder) {

        // Method가 GET인 경우 Body 데이타를 queryParam으로 설정한다.
        if (requestData.getMethod() == HttpMethod.GET) {
            for (String key : requestData.getBodies().keySet()) {
                builder.queryParam(key, requestData.getBody(key));
            }
        }

        return builder.build().encode().toUriString();
    }
}
