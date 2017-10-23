package api.presence.bo.service;

import api.beacon.service.BeaconService;
import api.log.service.LogService;
import api.presence.bo.domain.*;
import core.api.beacon.domain.Beacon;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.enums.InterfaceCommandType;
import core.common.enums.PresenceDataType;
import api.presence.bo.external.ExternalInterface;
import core.common.enums.SocketCommandType;
import core.common.log.domain.NotificationLog;
import core.common.presence.bo.domain.ScannerPresenceRedis;
import core.common.presence.bo.domain.ZoneInOutState;
import framework.util.JsonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.api.scanner.domain.ScannerPostion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ucjung on 2017-06-23.
 */

@Service
public class SocketRequestServiceImpl implements SocketRequestService {
    @Value("#{config['presence.socket.url']}")
    private String url;

    @Autowired
    @Qualifier("socketIoInterfaceImpl")
    private ExternalInterface socket;

    @Autowired
    private LogService logService;

    @Autowired
    private BeaconService beaconService;

    @Override
    public void send(SocketCommandType command, PresenceDataPackage presenceVo) {
        Map<String, Object> params = new HashMap<>();

        params.put("url", url);
        params.put("command", command.getCommand());

        SocketUpdateMarkerReqeustParam requestBodyParam = new SocketUpdateMarkerReqeustParam();

        if (presenceVo.getMode().equals("S")) {

            ScannerPresenceRequestParam requestParams = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);

            requestBodyParam.setUUID(requestParams.getSUUID());
            requestBodyParam.setId(requestParams.getUUID() + "_" + requestParams.getMajorVer() + "_" + requestParams.getMinorVer());
            requestBodyParam.setFloor(requestParams.getFloor());
            requestBodyParam.setLat(requestParams.getLat());
            requestBodyParam.setLng(requestParams.getLng());
            requestBodyParam.setMajorVer(requestParams.getMajorVer());
            requestBodyParam.setMinorVer(requestParams.getMinorVer());
            requestBodyParam.setScannerPos(requestParams.getScannerPos());
            requestBodyParam.setSos(requestParams.getSos());
            requestBodyParam.setShowMaker(1);
            requestBodyParam.setCreationDate((String) presenceVo.getTypeData(PresenceDataType.DATE_CREATION));

            BeaconExternalWidthRestrictedZone beaconExternal = presenceVo.getTypeData(PresenceDataType.BEACON_EXTERTNAL);
            if (beaconExternal != null) {
                requestBodyParam.setBarcode(beaconExternal.getBarcode());
                requestBodyParam.setExternalId(beaconExternal.getExternalId());
                requestBodyParam.setExternamAttribute(beaconExternal.getExternalAttribute());
            }
        } else {
            BeaconPresenceRequestParam requestParams = presenceVo.getTypeData(PresenceDataType.REQUEST_PARAM);
            requestBodyParam.setUUID(requestParams.getUUID());
            requestBodyParam.setId("");
            requestBodyParam.setFloor(requestParams.getFloor());
            requestBodyParam.setLat(requestParams.getLat());
            requestBodyParam.setLng(requestParams.getLng());
            requestBodyParam.setMajorVer(0);
            requestBodyParam.setMinorVer(0);
            requestBodyParam.setShowMaker(1);
            requestBodyParam.setCreationDate((String) presenceVo.getTypeData(PresenceDataType.DATE_CREATION));
        }

        params.put("body", JsonUtils.writeValue(requestBodyParam));
        socket.requestAsync(params, null);
    }


    @Override
    public void send(SocketCommandType command, InterfaceCommandType interfaceCommandType, PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState) {
        ScannerPresenceRedis presenceRedis = presenceVo.getTypeData(PresenceDataType.REDIS_CURRENT_PRESENCE);

        // 메시지 바디 정보 설정
        SocketNotificationRequestParam bodyParams = new SocketNotificationRequestParam();

        bodyParams.setInterfaceCommandType(interfaceCommandType);
        bodyParams.setBeaconInfo(presenceRedis);
        bodyParams.setEventZoneInOutState(zoneInOutState);

        // 메시지 헤더 정보 설정
        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        params.put("command", command.getCommand());
        params.put("body", JsonUtils.writeValue(bodyParams));

        socket.requestAsync(params, null);

        NotificationLog notificationLog = new NotificationLog();

        Beacon beacon = presenceVo.getTypeData(PresenceDataType.BEACON_INFO);
        if (beacon == null) {
            beacon = new Beacon();
            beacon.setUUID(bodyParams.getBeaconInfo().getUUID());
            beacon.setMajorVer(bodyParams.getBeaconInfo().getMajorVer());
            beacon.setMinorVer(bodyParams.getBeaconInfo().getMinorVer());
            beacon = beaconService.getBeaconInfo(beacon);
        }

        notificationLog.setComNum(beacon.getComNum());
        notificationLog.setBeaconNum(beacon.getBeaconNum());
        notificationLog.setInterfaceCommandType(interfaceCommandType);
        notificationLog.setBeaconInfo(presenceRedis);
        notificationLog.setEventZoneInOutState(zoneInOutState);

        logService.registerNotificationLog(notificationLog);

    }

    @Override
    public void send(SocketCommandType command, InterfaceCommandType interfaceCommandType, PresenceDataPackage presenceVo) {
        send(command, interfaceCommandType, presenceVo, null);
    }

    // Socket 연동을 위한 Vo Class
    private class SocketUpdateMarkerReqeustParam {
        @JsonProperty("UUID")
        private String UUID;
        private String id;
        private String floor;
        private Double lat = 0.0;
        private Double lng = 0.0;
        private Integer majorVer = 0;
        private Integer minorVer = 0;
        private List<ScannerPostion> scannerPos = new ArrayList<>();
        private Integer showMaker = 1;
        private Integer sos = 0;
        private String creationDate;
        private String barcode;
        private String externalId;
        private List<Map<String, Object>> externamAttribute;

        @JsonProperty("UUID")
        public String getUUID() {
            return UUID;
        }
        public void setUUID(String UUID) {
            this.UUID = UUID;
        }
        public String getFloor() {
            return floor;
        }
        public void setFloor(String floor) {
            this.floor = floor;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
        public Integer getMajorVer() {
            return majorVer;
        }
        public void setMajorVer(Integer majorVer) {
            this.majorVer = majorVer;
        }
        public Integer getMinorVer() {
            return minorVer;
        }
        public void setMinorVer(Integer minorVer) {
            this.minorVer = minorVer;
        }
        public List<ScannerPostion> getScannerPos() {
            return scannerPos;
        }
        public void setScannerPos(List<ScannerPostion> scannerPos) {
            this.scannerPos = scannerPos;
        }
        public Integer getShowMaker() {
            return showMaker;
        }
        public void setShowMaker(Integer showMaker) {
            this.showMaker = showMaker;
        }
        public Integer getSos() {
            return sos;
        }
        public void setSos(Integer sos) {
            this.sos = sos;
        }
        public String getCreationDate() {
            return creationDate;
        }
        public String getBarcode() {
            return barcode;
        }
        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }
        public String getExternalId() {
            return externalId;
        }
        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }
        public List<Map<String, Object>> getExternamAttribute() {
            return externamAttribute;
        }
        public void setExternamAttribute(List<Map<String, Object>> externamAttribute) {
            this.externamAttribute = externamAttribute;
        }
    }

    private class SocketNotificationRequestParam {
        private InterfaceCommandType interfaceCommandType;
        private ScannerPresenceRedis beaconInfo;
        private ZoneInOutState eventZoneInOutState;

        public InterfaceCommandType getInterfaceCommandType() {
            return interfaceCommandType;
        }

        public void setInterfaceCommandType(InterfaceCommandType interfaceCommandType) {
            this.interfaceCommandType = interfaceCommandType;
        }

        public ScannerPresenceRedis getBeaconInfo() {
            return beaconInfo;
        }

        public void setBeaconInfo(ScannerPresenceRedis beaconInfo) {
            this.beaconInfo = beaconInfo;
        }

        public ZoneInOutState getEventZoneInOutState() {
            return eventZoneInOutState;
        }

        public void setEventZoneInOutState(ZoneInOutState eventZoneInOutState) {
            this.eventZoneInOutState = eventZoneInOutState;
        }
    }
}
