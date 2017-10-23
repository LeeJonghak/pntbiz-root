package api.log.service;

import core.api.log.domain.*;
import core.common.log.domain.NotificationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.api.log.dao.InfluxdbPresenceDao;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import core.api.log.dao.LogDao;
import framework.web.util.StringUtil;

@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	@Autowired
	private InfluxdbPresenceDao influxdbPresenceDao;
	private @Value("#{config['log.db.type']}") String _log_db_type;

	//private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void registerPresenceLog(PresenceLog presenceLog) throws DataAccessException {
		if(StringUtil.NVL(_log_db_type).equals("influxDB"))
			 influxdbPresenceDao.insertPresenceLog(presenceLog);
		else
			logDao.insertPresenceLog(presenceLog);
	}
	@Override
	public void registerPresenceBeaconLog(PresenceBeaconLog presenceBeaconLog) throws DataAccessException {
		logDao.insertPresenceBeaconLog(presenceBeaconLog);
	}
	@Override
	public void registerPresenceExhibitionLog(PresenceExhibitionLog presenceExhibitionLog) 	throws DataAccessException {
		logDao.insertPresenceExhibitionLog(presenceExhibitionLog);
	}
	@Override
	public void registerPresenceInteractionLog(PresenceInteractionLog presenceInteractionLog) 	throws DataAccessException {
		logDao.insertPresenceInteractionLog(presenceInteractionLog);
	}
	@Override
	public void registerContentsInteractionLog(ContentsInteractionLog contentsInteractionLog) throws DataAccessException {
		logDao.insertContentsInteractionLog(contentsInteractionLog);
	}
	@Override
	public void registerBeaconSensorLog(BeaconSensorLog beaconSensorLog) throws DataAccessException {
		logDao.insertBeaconSensorLog(beaconSensorLog);
	}
	@Override
	public Integer registerGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException {
		return logDao.insertGeofenceLog(geofenceLog);
	}
	@Override
	public void modifyGeofenceLog(GeofenceLog geofenceLog) throws DataAccessException {
		logDao.updateGeofenceLog(geofenceLog);
	}

	@Override
	public Long registerInterfaceLog(InterfaceLog interfaceLog) throws DataAccessException {
		return logDao.insertInterfaceLog(interfaceLog);
	}

	@Override
	public Integer registerFloorLog(FloorLog floorLog) {
		return logDao.insertFloorLog(floorLog);
	}

	@Override
	public void modifyFloorLog(FloorLog floorLog) {
		logDao.updateFloorLog(floorLog);
	}

	@Override
	public Integer registerNotificationLog(NotificationLog notificationLog) {
		return logDao.insertNotificationLog(notificationLog);
	}

}
