package framework.web.http;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 * Created by ucjung on 2017-08-30.
 */
public class RequestClientTest {
    private RequestData requestData;
    private RequestClient requestClient;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Before
    public void setUp() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        requestData = new RequestData();

        requestData.setProtocol("http");
        requestData.setHost("devapi");
        requestData.setPath("/mock/floor/in");
        requestData.setPort(8002);

        requestData.setMethod(HttpMethod.POST);
        requestData.addHeader("Content-Type", "application/json; charset=UTF-8");
        requestData.addHeader("apiKey", "81d765ad552348b9973d7e761c649b68");
        requestData.addHeader("secret", "pnt1011");

        requestData.addBody("SUUID", "A0BB73F0-1088-4D36-845F-C13A2EBF3B63");
        requestData.addBody("UUID", "A0BB73F0-1088-4D36-845F-C13A2EBF3B63");
        requestData.addBody("majorVer", 99);
        requestData.addBody("minorVer", 0);
        requestData.addBody("floor", "1");
        requestData.addBody("lng", 127.05656837429738);
        requestData.addBody("lat", 37.51096656285132);
        requestData.addBody("dateTime", "2017-08-24 오후 03:49:57");

        requestClient = new RequestClient();
        Timestamp end = new Timestamp(System.currentTimeMillis());
        logger.info("{}, {}" , end, start);
    }

    @Test
    public void requestTest() {
        Timestamp start = new Timestamp(System.currentTimeMillis());
        requestClient.request(requestData);
        Timestamp end = new Timestamp(System.currentTimeMillis());
        logger.info("{}, {}" , end, start);
    }
}