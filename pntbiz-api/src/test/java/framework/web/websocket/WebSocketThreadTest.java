package framework.web.websocket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ucjung on 2017-04-16.
 */
public class WebSocketThreadTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void send() throws JSONException {
        String response = null;
        try {
            response = WebSocketThread.send(
                    "http://beacon.pntbiz.com:10001/presence/send",
                    "POST",
                    1000,
                    "{\"id\":\"6B62736D-632D-706E-7462-697A2E636F6D_40031_300\",\"sid\":\"G1\",\"areaNum\":\"0\",\"nodeIdx\":10000,\"areaName\":\"-\",\"floor\":\"1\",\"scannerPos\":[],\"UUID\":\"6B62736D-632D-706E-7462-697A2E636F6D\",\"lng\":\"126.96778982877731\",\"battLevel\":30,\"lat\":\"37.56855058831552\",\"sos\":0}"
            );
            logger.info(response);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        JSONObject json = new JSONObject(response);

        assertThat((String) json.get("result"), is("success"));
        assertThat((String) json.get("code"), is("0"));

    }

}