package core.common.config.dao;

import core.common.config.domain.BodyMetaData;
import core.common.config.domain.InterfaceConfig;
import core.common.enums.InterfaceBindingType;
import core.common.enums.InterfaceCommandType;
import core.common.enums.PresenceDataType;
import framework.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ucjung on 2017-08-29.
 */
public class InterfaceConfigDaoTest extends JUnitTestBase {
    @Autowired
    private InterfaceConfigDao interfaceConfigDao;

    private InterfaceConfig interfaceConfig;

    @Before
    public void init() {
        interfaceConfig = new InterfaceConfig();
        interfaceConfig.setComNum(100003);
        interfaceConfig.setInterfaceBindingType(InterfaceBindingType.FLOOR_COMMON);
        interfaceConfig.setBindingZoneId("3");
        interfaceConfig.setInterfaceCommandType(InterfaceCommandType.FLOOR_IN);

        Map<String, String> targetInfo = new HashMap<>();
        targetInfo.put("protocol", "http");
        targetInfo.put("host", "localhost");
        targetInfo.put("port", "8002");
        targetInfo.put("uri", "mock/floor/in");

        interfaceConfig.setTargetInfo(JsonUtils.writeValue(targetInfo));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        interfaceConfig.setHeaders(JsonUtils.writeValue(headers));

        Map<String, BodyMetaData> bodyMetaData = new HashMap<>();

        bodyMetaData.put("beaconId", setBodyMetaData("beaconId", PresenceDataType.REQUEST_PARAM, "id"));
        bodyMetaData.put("lng", setBodyMetaData("lng", PresenceDataType.REQUEST_PARAM, "lng"));
        bodyMetaData.put("lng", setBodyMetaData("lng", PresenceDataType.REQUEST_PARAM, "lat"));

        interfaceConfig.setBodyMetaData(JsonUtils.writeValue(bodyMetaData));
    }

    private BodyMetaData setBodyMetaData(String fieldName, PresenceDataType dataType, String dataFieldName) {
        BodyMetaData metaData = new BodyMetaData();
        metaData.setFieldName(fieldName);
        metaData.setDataType(dataType);
        metaData.setDataFiledName(dataFieldName);

        return metaData;
    }

    @Test
    public void addGetUpdateDelete() {
        Integer result = interfaceConfigDao.insert(interfaceConfig);
        assertThat(result, is(1));

        InterfaceConfig interfaceConfigResult = interfaceConfigDao.get(interfaceConfig);
        compareInterfaceConfig(interfaceConfigResult, interfaceConfig);

        interfaceConfig.setBindingZoneId("4");
        result = interfaceConfigDao.update(interfaceConfig);
        assertThat(result, is(1));

        interfaceConfigResult = interfaceConfigDao.get(interfaceConfig);
        compareInterfaceConfig(interfaceConfigResult, interfaceConfig);

        //result = interfaceConfigDao.delete(interfaceConfig.getInterfaceNum());
        //assertThat(result, is(1));
    }

    private void compareInterfaceConfig(InterfaceConfig source, InterfaceConfig dest) {
        assertThat(source.getInterfaceNum(), is (dest.getInterfaceNum()));
        assertThat(source.getComNum(), is (dest.getComNum()));
        assertThat(source.getInterfaceBindingType(), is (dest.getInterfaceBindingType()));
        assertThat(source.getBindingZoneId(), is (dest.getBindingZoneId()));
        assertThat(source.getInterfaceCommandType(), is (dest.getInterfaceCommandType()));
    }



}