package api.presence.processbo.external;

import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.enums.PresenceEventType;
import api.presence.bo.external.ExternalInterface;
import api.presence.bo.external.SocketIoInterfaceImpl;
import framework.util.JsonUtils;
import core.api.presence.domain.GeoLocation;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-06-16.
 */
public class SocketIoInterfaceImplTest {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExternalInterface externalInterface;
    private Map<String, Object> requestParam;

    @Before
    public void before() {
        externalInterface = new SocketIoInterfaceImpl();
        requestParam = new HashMap<>();

        requestParam.put("url", "http://localhost:10000");
        requestParam.put("eventType", PresenceEventType.FLOOR_IN);
        requestParam.put("key", PresenceEventType.FLOOR_IN);
        requestParam.put("body", "");
    }

    @Test
    public void request() throws InterruptedException {
        String json = "{\"id\":\"44790ba4-7eb3-4095-9e14-4b43ae67512b_40000_45\",\"pos\":[{\"lat\":\"37.511176\",\"lng\":\"127.056713\"},{\"lat\":\"37.511186\",\"lng\":\"127.056712\"}],\"UUID\":\"706E7430-F5F8-466E-AFF9-25556B57FE6D\",\"sid\":\"\",\"floor\":\"3\",\"nodeIdx\":\"3045\",\"nodeName\":\"\",\"battLevel\":95,\"mac\":\"b8:27:eb:e7:d5:38:50:4E:54:00:00:06\",\"sos\":0,\"heartbeat\":95,\"scannerPos\":[{\"lat\":37.511179,\"lng\":127.056711,\"r\":1},{\"lat\":37.511183374999995,\"lng\":127.0567394375,\"r\":2},{\"lat\":37.511152708,\"lng\":127.05672352,\"r\":3},{\"lat\":37.51117550666667,\"lng\":127.056685848,\"r\":4}]}\t";
        ScannerPresenceRequestParam requestObject= JsonUtils.readValue(json, ScannerPresenceRequestParam.class);

        requestParam.put("body", JsonUtils.writeValue(new ScannerPresenceRequestParam()));
        externalInterface.requestAsync(requestParam, null);
        Thread.sleep(1000);
    }

    @Test
    public void locationConvertToPosition() {
        // 도면의 좌측 하단 위치
        GeoLocation floorBL = new GeoLocation();
        floorBL.setLng(127.056571);
        floorBL.setLat(37.510829);

        // 도면의 우측 상단 위치
        GeoLocation floorTR = new GeoLocation();
        floorTR.setLng(127.05701);
        floorTR.setLat(37.511046);

        // 현재 위치
        GeoLocation current = new GeoLocation();
        current.setLng(127.056771);
        current.setLat(37.510929);

        //도면의 GeoLocation 폭 및 높이 계산
        Double floorGeoWidth = floorTR.getLng() - floorBL.getLng();
        Double floorGeoHeight = floorTR.getLat() - floorBL.getLat();

        // 변환될 좌표의 폭과 높이
        Double convertWidth = 1000.0;
        Double convertHeight = 500.0;

        // X 좌표 계산
        Double posX = (current.getLng() - floorBL.getLng()) / floorGeoWidth * convertWidth;
        Double posY = (current.getLat() - floorBL.getLat()) / floorGeoHeight * convertHeight;

        logger.info("{} / {}", posX, posY);

    }

}