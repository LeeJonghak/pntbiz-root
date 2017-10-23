package api.presence.bo.service;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.domain.ScannerPresenceRequestParam;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.geofencing.domain.Geofencing;
import core.common.config.domain.InterfaceConfig;
import core.common.enums.InterfaceBindingType;
import core.common.enums.PresenceDataType;
import core.common.enums.InterfaceCommandType;
import core.common.enums.ZoneType;
import framework.exception.ExceptionType;
import framework.exception.PresenceException;
import api.presence.bo.external.ExternalInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ucjung on 2017-06-23.
 */

@Service
public class RestApiRequestServiceImpl implements RestApiRequestService {

    @Autowired
    @Qualifier("restApiInterfaceImpl")
    private ExternalInterface api;

    @Override
    public void send(InterfaceCommandType command, PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        Map<String, InterfaceConfig> interfaceConfigMap = presenceVo.getTypeData(PresenceDataType.INTERFACE_CONFIG);
        List<InterfaceConfig>  interfaceConfigs = getInterfaces(command, zoneInOutState, interfaceConfigMap);

        for (InterfaceConfig config : interfaceConfigs) {
            if (config == null) continue;
            Map<String, Object> bodyParms = makeCommandBody(command, presenceVo, zoneInOutState);
            api.requestAsync(bodyParms, config);
        }
    }

    @Override
    public void send(InterfaceCommandType command, PresenceDataPackage presenceVo) {
        send(command, presenceVo, null);
    }

    private List<InterfaceConfig> getInterfaces(InterfaceCommandType command, ZoneInOutState zoneInOutState, Map<String, InterfaceConfig> interfaceConfigMap) {
        List<InterfaceConfig> invokeInterfaceConfig = new ArrayList<>();
        switch (command) {
            case LOCATION_CHANGE:
                invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.LOCATION_COMMON, command, interfaceConfigMap));
                break;
            case FLOOR_IN:
            case FLOOR_STAY :
            case FLOOR_OUT :
                invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.FLOOR_COMMON, command, interfaceConfigMap));
                if (zoneInOutState.getZoneId() != null)
                invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.FLOOR, command, zoneInOutState.getZoneId(), interfaceConfigMap));
                break;
            case GEOFENCE_IN :
            case GEOFENCE_STAY :
            case GEOFENCE_OUT :
                invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.GEOFENCE_COMMON, command, interfaceConfigMap));
                if (zoneInOutState.getFcGroupNum() != null)
                    invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.GEOFENCE_GROUP, command, zoneInOutState.getFcGroupNum().toString(), interfaceConfigMap));
                break;
            case RESTRICTION_IN:
            case RESTRICTION_STAY:
            case RESTRICTION_OUT:
                if (zoneInOutState.getZoneType() == ZoneType.FLOOR) {
                    invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.FLOOR_COMMON, command, interfaceConfigMap));
                    if (zoneInOutState.getZoneId() != null)
                    invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.FLOOR, command, zoneInOutState.getZoneId(), interfaceConfigMap));
                }
                if (zoneInOutState.getZoneType() == ZoneType.GEOFENCE) {
                    invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.GEOFENCE_COMMON, command, interfaceConfigMap));
                    if (zoneInOutState.getFcGroupNum() != null)
                        invokeInterfaceConfig.add(getInterfaceConfig(InterfaceBindingType.GEOFENCE_GROUP, command, zoneInOutState.getFcGroupNum().toString(), interfaceConfigMap));
                }
        }

        return invokeInterfaceConfig;
    }

    private InterfaceConfig getInterfaceConfig(InterfaceBindingType bindingType, InterfaceCommandType commandType, String zoneId, Map<String, InterfaceConfig> interfaceConfigMap) {
        return interfaceConfigMap.get(bindingType.getValue() + "_" + commandType.getValue() + "_" + zoneId);
    }

    private InterfaceConfig getInterfaceConfig(InterfaceBindingType bindingType, InterfaceCommandType commandType, Map<String, InterfaceConfig> interfaceConfigMap) {
        return interfaceConfigMap.get(bindingType.getValue() + "_" + commandType.getValue());
    }

    private Map<String, Object> makeCommandBody(InterfaceCommandType command, PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        switch (command) {
            case LOCATION_CHANGE:
                return makeLocationChangeBody(presenceVo, zoneInOutState);
            case FLOOR_IN:
            case FLOOR_STAY :
            case FLOOR_OUT :
            case GEOFENCE_IN :
            case GEOFENCE_STAY :
            case GEOFENCE_OUT :
                return makeZoneStateBody(presenceVo, zoneInOutState);
            case RESTRICTION_IN :
                return makeRestrictionInBody(presenceVo, zoneInOutState);
            case RESTRICTION_OUT :
            default:
                throw new PresenceException(ExceptionType.EXTERNAL_UNKNOWN_COMMAND);
        }
    }

    private Map<String, Object> makeRestrictionInBody(PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        return makeZoneStateBody(presenceVo, zoneInOutState);
    }

    private Map<String, Object> makeZoneStateBody(PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("SUUID", requestParam.getSUUID());
        bodyParams.put("UUID", requestParam.getUUID());
        bodyParams.put("majorVer", requestParam.getMajorVer());
        bodyParams.put("minorVer", requestParam.getMinorVer());
        bodyParams.put("zoneInOutState", zoneInOutState);

        return bodyParams;
    }

    private Map<String, Object> makeLocationChangeBody(PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        ScannerPresenceRequestParam requestParam = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("SUUID", requestParam.getSUUID());
        bodyParams.put("UUID", requestParam.getUUID());
        bodyParams.put("majorVer", requestParam.getMajorVer());
        bodyParams.put("minorVer", requestParam.getMinorVer());
        bodyParams.put("floor", requestParam.getFloor());
        bodyParams.put("lat", requestParam.getLat());
        bodyParams.put("lng", requestParam.getLng());
        bodyParams.put("dateTime", presenceVo.getTypeData(PresenceDataType.DATE_CREATION));

        List<Geofencing> inbounds = presenceVo.getTypeData(PresenceDataType.INBOUND_GEOFENCES);

        List<Map<String, Object>> geofences = new ArrayList<>();
        for ( Geofencing fence : inbounds ) {
            Map<String, Object> inboudInfo = new HashMap<>();
            inboudInfo.put("fcNum", fence.getFcNum());
            inboudInfo.put("fcName", fence.getFcName());
            inboudInfo.put("fcGroupNum", fence.getFcGroupNum());
            inboudInfo.put("fcGroupName", fence.getFcGroupName());
            geofences.add(inboudInfo);
        }

        bodyParams.put("geofences", geofences);

        return bodyParams;
    }
}
