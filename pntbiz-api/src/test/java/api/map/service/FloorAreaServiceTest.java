package api.map.service;
import core.api.log.domain.PresenceLog;
import core.api.map.domain.FloorAreaInfo;
import core.api.presence.domain.PresenceRequestParam;
import com.google.gson.Gson;
import common.JUnitTestBase;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 측위정보로 구역정보를 처리하는 기능 테스트
 *
 * Created by ucjung on 2017-04-16.
 */
public class FloorAreaServiceTest extends JUnitTestBase{

    @Autowired FloorAreaService floorAreaService;

    private Gson gson = new Gson();

    @Test
    public void checkFloorArea() throws IOException, JSONException {
        String presenceRequestData = "{\n" +
                "    \"UUID\": \"6B62736D-632D-706E-7462-697A2E636F6D\",\n" +
                "    \"id\": \"6B62736D-632D-706E-7462-697A2E636F6D_40031_100\",\n" +
                "    \"lat\": 37.511773,\n" +
                "    \"lng\": 127.056460,\n" +
                "    \"sid\": \"G1\",\n" +
                "    \"floor\": \"126\",\n" +
                "    \"battLevel\": 30,\n" +
                "    \"sos\": 1,\n" +
                "    \"nodeIndex\": 10000,\n" +
                "    \"scannerPos\": [\n" +
                "        {\"lat\": 37.5111111, \"lng\": 127.5111222, \"r\": 3},\n" +
                "        {\"lat\": 37.5111211, \"lng\": 127.5111322, \"r\": 4},\n" +
                "        {\"lat\": 37.5111311, \"lng\": 127.5111422, \"r\": 5}\n" +
                "    ]\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        PresenceRequestParam presenceRequestParam = new ObjectMapper().readValue(presenceRequestData, PresenceRequestParam.class);

        PresenceLog presenceLog = new PresenceLog();
        presenceLog.setUUID(presenceRequestParam.getUUID());
        presenceLog.setLat(presenceRequestParam.getLat());
        presenceLog.setLng(presenceRequestParam.getLng());
        presenceLog.setFloor(presenceRequestParam.getFloor());

        try {
            FloorAreaInfo floorAreaInfo = floorAreaService.getAreaInfo(presenceLog);
            logger.info(gson.toJson(floorAreaInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}