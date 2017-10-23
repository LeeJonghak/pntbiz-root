package core.api.log.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import core.api.log.domain.PresenceLog;
import framework.db.dao.InfluxDBBaseDao;
import framework.web.util.DateUtil;

@Repository
public class InfluxdbPresenceDao extends InfluxDBBaseDao {

	public void insertPresenceLog(PresenceLog vo) {
		String regDate = Long.toString(DateUtil.str2timestamp(DateUtil.getDate("yyyyMMddHHmmss")));
		Map<String, Object> fields = new HashMap<String, Object>();

		fields.put("SUUID", vo.getSUUID());
		fields.put("UUID", vo.getUUID());
		fields.put("majorVer", vo.getMajorVer());
		fields.put("minorVer", vo.getMinorVer());
		if(vo.getLat() != null) 		fields.put("lat", vo.getLat());
		if(vo.getLng() != null) 		fields.put("lng", vo.getLng());
		if(vo.getFloor() != null) 		fields.put("floor", vo.getFloor());
		if(vo.getBeaconName() != null) fields.put("beaconName", vo.getBeaconName());
		if(vo.getTargetName() != null) fields.put("targetName", vo.getTargetName());
		if(vo.getLogDesc() != null) 	fields.put("logDesc", vo.getLogDesc());
		if(vo.getFloorCode() != null) 	fields.put("floorCode", vo.getFloorCode());
		if(vo.getColumnCode() != null) fields.put("columnCode", vo.getColumnCode());
		fields.put("regDate", regDate);

		this.insert(fields, "TB_LOG_PRESENCE1");
	}
}