package core.api.log.dao;

import core.api.log.domain.*;
import core.common.log.domain.NotificationLog;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao extends BaseDao {

	public void insertPresenceLog(PresenceLog presenceLog) throws DataAccessException {
		insert("insertPresenceLog", presenceLog);
	}
	public void insertPresenceBeaconLog(PresenceBeaconLog presenceBeaconLog) throws DataAccessException {
		insert("insertPresenceBeaconLog", presenceBeaconLog);
	}
	public void insertPresenceExhibitionLog(PresenceExhibitionLog presenceExhibitionLog) throws DataAccessException {
		insert("insertPresenceExhibitionLog", presenceExhibitionLog);
	}
	public void insertPresenceInteractionLog(PresenceInteractionLog presenceInteractionLog) throws DataAccessException {
		insert("insertPresenceInteractionLog", presenceInteractionLog);
	}
	public void insertContentsInteractionLog(ContentsInteractionLog contentsInteractionLog) throws DataAccessException {
		insert("insertContentsInteractionLog", contentsInteractionLog);
	}
	public void insertBeaconSensorLog(BeaconSensorLog beaconSensorLog) throws DataAccessException {
		insert("insertBeaconSensorLog", beaconSensorLog);
	}
	public Integer insertGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException {
		insert("insertGeofenceLog", geofenceLog);
		return geofenceLog.getLogNum();
	}
	public void updateGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException {
		update("updateGeofenceLog", geofenceLog);
	}

	public Long insertInterfaceLog(InterfaceLog interfaceLog) throws DataAccessException {
		insert("insertInterfaceLog", interfaceLog);
		return interfaceLog.getLogNum();
	}

	public Integer insertFloorLog(FloorLog floorLog) throws DataAccessException {
		insert("insertFloorLog", floorLog);
		return floorLog.getLogNum();
	}
	public void updateFloorLog(FloorLog floorLog) throws DataAccessException {
		update("updateFloorLog", floorLog);
	}

	public Integer insertNotificationLog(NotificationLog notificationLog) {
		insert("insertNotificationLog", notificationLog);
		return notificationLog.getLogNum();
	}
}