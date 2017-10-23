package core.common.enums;

/**
 * PresenceDataPackage 저장 Data Key Type
 *
 * 해당 Key Type을 이용하여 PresenceDataPackage에 객체를 set/get 한다.
 *
 * Created by ucjung on 2017-06-05.
 */
public enum PresenceDataType {
    REQUEST_PARAM(0),                   // 요청 데이타
    PRESENCE_SCANNER_LOG(1),            // SCANNER LOG
    PRESENCE_BEACON_LOG(2),             // BEACON LOG
    INTERFACE_CONFIG(3),                // 연동 설정 정보
    REDIS_PREVRIVOUS_PRESENCE(10),      // 이전 BEACON REDIS 저장 정보
    REDIS_CURRENT_PRESENCE(11),         // 현재 BEACON REDIS 저장 정보
    DATE_CREATION(20),                  // 생성일자
    FLOOR_GEOFENCES(30),                // 해당 층 Geofence 정보
    INBOUND_GEOFENCES(31),              // Beacon Inbound된 Geofence 정보
    BEACON_INFO(40),               // Beacon 외부 정보 (연동 대상 정보 및 인허가 지역 정보)
    BEACON_EXTERTNAL(41);               // Beacon 외부 정보 (연동 대상 정보 및 인허가 지역 정보)

    private Integer value;

    PresenceDataType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
