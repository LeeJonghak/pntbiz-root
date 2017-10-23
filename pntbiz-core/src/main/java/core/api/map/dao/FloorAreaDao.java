package core.api.map.dao;

import core.api.map.domain.FloorAreaConfig;
import core.api.map.domain.FloorAreaInfo;
import framework.db.dao.BaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FloorAreaDao extends BaseDao {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Cacheable(value = "CACHE_FLOOR_AREA_CONFIG", key="#comNum.toString()")
	public List<FloorAreaConfig> getFloorAreaConfigList(Integer comNum) throws DataAccessException {
		return (List<FloorAreaConfig>) list("getFloorAreaConfigList", comNum);
	}

	@Cacheable(value = "CACHE_FLOOR_AREA_INFO", key="#params.comNum.toString().concat('-').concat(#params.floorNum.toString())")
	public List<FloorAreaInfo> getFloorAresInfoList(FloorAreaConfig params) {
		return (List<FloorAreaInfo>) list("getFloorAreaInfoList", params);
	}
}
