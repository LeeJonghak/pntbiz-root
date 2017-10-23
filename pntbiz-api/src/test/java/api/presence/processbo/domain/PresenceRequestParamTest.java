package api.presence.processbo.domain;

import api.presence.bo.domain.BeaconPresenceRequestParam;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import framework.util.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * ScannerPresenceRequestParam Json Data Binding 테스트
 *
 * Created by ucjung on 2017-06-13.
 */
public class PresenceRequestParamTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void scannerJsonDataBinding() throws Exception {
        String requestJson = "{\"id\":\"44790ba4-7eb3-4095-9e14-4b43ae67512b_40000_45\",\"pos\":[{\"lat\":\"37.511175\",\"lng\":\"127.056713\"},{\"lat\":\"37.511185\",\"lng\":\"127.056712\"}],\"UUID\":\"706E7430-F5F8-466E-AFF9-25556B57FE6D\",\"sid\":\"\",\"floor\":\"3\",\"nodeIdx\":\"3045\",\"nodeName\":\"\",\"battLevel\":95,\"mac\":\"b8:27:eb:e7:d5:38:50:4E:54:00:00:06\",\"sos\":0,\"heartbeat\":95,\"scannerPos\":[{\"lat\":37.511179,\"lng\":127.056711,\"r\":1},{\"lat\":37.511183374999995,\"lng\":127.0567394375,\"r\":2},{\"lat\":37.511152708,\"lng\":127.05672352,\"r\":3},{\"lat\":37.51117550666667,\"lng\":127.056685848,\"r\":4}]}";
        ScannerPresenceRequestParam requestParam = JsonUtils.readValue(requestJson, ScannerPresenceRequestParam.class);

        logger.info(JsonUtils.writeValue(requestParam));

        assertThat(requestParam.getSUUID(), is("706E7430-F5F8-466E-AFF9-25556B57FE6D".toUpperCase()));
        assertThat(requestParam.getUUID(), is("44790ba4-7eb3-4095-9e14-4b43ae67512b".toUpperCase()));
        assertThat(requestParam.getMajorVer(), is(40000));
        assertThat(requestParam.getMinorVer(), is(45));
    }

    @Test
    public void beaconJsonDataBinding() throws Exception {
        String requestJson = "{\"mode\":\"B\", \"UUID\":\"39ffef30-8699-11e4-b4a9-0800200c9a66\",\"lat\":365.12345678,\"lng\":127.12345678,\"floor\":\"1\",\"nodeID\":20,\"fcNum\":12,\"fcName\":\"fence1\",\"phoneNumber\":\"01027503529\",\"deviceInfo\":\"01027503529\"}";
        BeaconPresenceRequestParam requestParam = JsonUtils.readValue(requestJson, BeaconPresenceRequestParam.class);

        logger.info(JsonUtils.writeValue(requestParam));

        assertThat(requestParam.getUUID(), is("39ffef30-8699-11e4-b4a9-0800200c9a66".toUpperCase()));
    }
}