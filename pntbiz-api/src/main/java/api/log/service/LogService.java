package api.log.service;

import core.api.log.domain.*;
import core.common.log.domain.NotificationLog;
import org.springframework.dao.DataAccessException;

public interface LogService {

	// 스캐너모델 비콘로그
	public void registerPresenceLog(PresenceLog param) throws DataAccessException;
	// 비콘모델 모바일로그
	public void registerPresenceBeaconLog(PresenceBeaconLog presenceBeaconLog) throws DataAccessException;

	public void registerPresenceExhibitionLog(PresenceExhibitionLog presenceExhibitionLog) throws DataAccessException;
	public void registerPresenceInteractionLog(PresenceInteractionLog presenceInteractionLog) throws DataAccessException;

	public void registerContentsInteractionLog(ContentsInteractionLog contentsInteractionLog) throws DataAccessException;

	public void registerBeaconSensorLog(BeaconSensorLog beaconSensorLog) throws DataAccessException;

	public Integer registerGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException;
	public void modifyGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException;

	public Long registerInterfaceLog(InterfaceLog interfaceLog) throws DataAccessException;

	public Integer registerFloorLog(FloorLog floorLog);
	public void modifyFloorLog(FloorLog floorLog);

    public Integer registerNotificationLog(NotificationLog notificationLog);
}