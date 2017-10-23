package wms.map.service;

import core.wms.map.domain.Area;
import core.wms.map.domain.AreaLatlng;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

/**
 */
public interface AreaService {

    public List<?> getAreaList(Map param) throws DataAccessException;
    public List<?> getAreaCenterList(Map<String, Object> param) throws DataAccessException;
    public Integer getAreaCount(Map param) throws DataAccessException;
    public Area getAreaInfo(Map param) throws DataAccessException;

    public void registerArea(Area area, List<AreaLatlng> areaLatlngs) throws DataAccessException;
    public void modifyArea(Area area, List<AreaLatlng> areaLatlngs) throws DataAccessException;
    public void removeArea(Map param) throws DataAccessException;

    public void updateNodeUpdate(List<Long> nodeNumArray, Integer areaNum, String areaName) throws DataAccessException;
}
