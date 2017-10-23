package core.wms.tracking.dao;

import java.util.List;

import org.influxdb.dto.Point;
import org.springframework.stereotype.Repository;

import core.api.log.domain.PresenceLog;
import core.wms.tracking.domain.PresenceLogSearchParam;
import core.wms.tracking.domain.PresenceTargetSearchParam;
import framework.db.dao.InfluxDBBaseDao;
import framework.web.util.DateUtil;

@Repository
public class InfluxdbPresenceDao extends InfluxDBBaseDao {

	public List<?> getPresenceTargetLogList(PresenceTargetSearchParam param){
		param.setBeaconNum(null);
		influxQueryParam queryParam = new influxQueryParam();
		queryParam.setTableName("TB_LOG_PRESENCE");
		queryParam.setSearchParam(param);

		String query = this.selectQuery(queryParam);
		return selectList(query);
	}

	public List<?> getPresenceLogListByLimit(PresenceLogSearchParam param) {
		influxQueryParam queryParam = new influxQueryParam();
		queryParam.setTableName("TB_LOG_PRESENCE");
		queryParam.setSearchParam(param);
		queryParam.setAddSarchParam("time > now() - 10d");
		queryParam.setOrderBy("time DESC");
		queryParam.setLimit(30);

		String query = this.selectQuery(queryParam);
		return selectList(query);
	}
}