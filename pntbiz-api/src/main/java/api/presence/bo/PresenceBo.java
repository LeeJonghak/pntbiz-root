package api.presence.bo;

import api.presence.bo.domain.BeaconPresenceRequestParam;
import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import api.presence.bo.factory.PresenceTaskExecutorChainFactory;
import api.presence.bo.service.InterfaceConfigService;
import api.presence.bo.service.PresenceServiceDelegator;
import core.common.enums.PresenceDataType;
import framework.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Presence Request Data 처리 Business Object
 *
 * Created by ucjung on 2017-06-05.
 */
@Component
public class PresenceBo {
    @Autowired
    private InterfaceConfigService interfaceConfigService;

    @Autowired
    private PresenceServiceDelegator presenceServiceDelegator;

    @Autowired
    private PresenceTaskExecutorChainFactory presenceTaskExecutorChainFactory;

    @Autowired
    public PresenceBo(PresenceServiceDelegator presenceServiceDelegator) {
        this.presenceServiceDelegator = presenceServiceDelegator;
    }

    public PresenceDataPackage execute(String presenceBody) {
        return execute(JsonUtils.readValue(presenceBody, Map.class));
    }

    public PresenceDataPackage execute(Map<String, Object> requestData) {
        PresenceDataPackage dataPackage = new PresenceDataPackage();

        initData(dataPackage, requestData);
        doProcess(dataPackage);
        return dataPackage;
    }

    private void initData(PresenceDataPackage dataPackage, Map<String, Object> requestData) {

        if (requestData.containsKey("mode")) {
            BeaconPresenceRequestParam requestParam = JsonUtils.convertValue(requestData, BeaconPresenceRequestParam.class);
            dataPackage.setTypeData(PresenceDataType.REQUEST_PARAM, requestParam);
            dataPackage.setMode(requestData.get("mode").toString());
        } else {
            ScannerPresenceRequestParam requestParam = JsonUtils.convertValue(requestData, ScannerPresenceRequestParam.class);
            dataPackage.setTypeData(PresenceDataType.REQUEST_PARAM, requestParam);
            dataPackage.setMode("S");
            dataPackage.setTypeData(PresenceDataType.INTERFACE_CONFIG,interfaceConfigService.gets(requestParam.getSUUID()));
        }
    }

    private void doProcess(PresenceDataPackage dataPackage) {
        PresenceTaskExecutor executorChain = presenceTaskExecutorChainFactory.getTaskExecutorChain(dataPackage);
        executorChain.excute(dataPackage, presenceServiceDelegator);
    }
}
