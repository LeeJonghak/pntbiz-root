package api.map.dao;

import core.api.map.dao.FloorAreaDao;
import core.api.map.domain.FloorAreaConfig;
import core.api.map.domain.FloorAreaInfo;
import com.google.gson.Gson;
import common.JUnitTestBase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * 층 구역 정보 데이타 처리 테스트
 *
 * Created by ucjung on 2017-04-16.
 */
public class FloorAreaDaoTest extends JUnitTestBase{

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    FloorAreaDao floorAreaDao;

    private Gson gson ;

    @Before
    public void setUp() {
        gson = new Gson();

        // 처음 구동 시 레디스 데이타를 삭제한 후 시작한다.
        redisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "FLUSHED";
            }
        });
    }

    @Test
    public void getFloorAreaConfigList() {
        List<FloorAreaConfig> floorAreaConfigs = floorAreaDao.getFloorAreaConfigList(1);
        List<FloorAreaConfig> floorAreaConfigs2 = floorAreaDao.getFloorAreaConfigList(1);

        assertThat(gson.toJson(floorAreaConfigs), is(gson.toJson(floorAreaConfigs2)));
    }

    @Test
    public void getFloorAreaInfoList() {
        FloorAreaConfig floorAreaConfig = new FloorAreaConfig();
        floorAreaConfig.setComNum(1);
        floorAreaConfig.setFloorNum(5);

        List<FloorAreaInfo> floorAreaInfos = floorAreaDao.getFloorAresInfoList(floorAreaConfig);
        List<FloorAreaInfo> floorAreaInfos2 = floorAreaDao.getFloorAresInfoList(floorAreaConfig);

        assertThat(gson.toJson(floorAreaInfos), is(gson.toJson(floorAreaInfos2)));
    }

}