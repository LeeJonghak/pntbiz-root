package wms.map.service;

import core.wms.map.dao.AreaDao;
import core.wms.map.dao.AreaLatlngDao;
import core.wms.map.dao.NodeDao;
import core.wms.map.domain.Area;
import core.wms.map.domain.AreaCenter;
import core.wms.map.domain.AreaLatlng;
import core.wms.map.domain.Node;
import framework.web.util.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private AreaLatlngDao areaLatlngDao;

    @Autowired
    private NodeDao nodeDao;

    @Override
    public List<Area> getAreaList(@SuppressWarnings("rawtypes") Map param) throws DataAccessException {

        List<?> list = areaDao.getAreaList(param);
        List<Area> listArea = new ArrayList<Area>();
        for(Object areaObject:list) {
            Area areaInfo = (Area)areaObject;
            @SuppressWarnings("unchecked")
			List<AreaLatlng> latlngs = (List<AreaLatlng>) areaLatlngDao.getAreaLatlngList(QueryParam.create("areaNum", areaInfo.getAreaNum()).build());
            areaInfo.setAreaLatlngs(latlngs);
            listArea.add(areaInfo);
        }

        return listArea;
    }
    
    @Override
    public List<?> getAreaCenterList(Map<String, Object> param) {

        List<?> list = areaDao.getAreaCenterList(param);
        List<?> latlngList = areaDao.getAreaCenterLatlngList(param);
        Map<Integer, List<AreaLatlng>> latlngMap = new HashMap<Integer, List<AreaLatlng>>();

        List<AreaCenter> result = new ArrayList<AreaCenter>();
        for(Object obj: latlngList) {
            AreaLatlng areaLatlngInfo = (AreaLatlng) obj;
            if(!latlngMap.containsKey(areaLatlngInfo.getAreaNum())) {
                latlngMap.put(areaLatlngInfo.getAreaNum(), new ArrayList<AreaLatlng>());
            }

            latlngMap.get(areaLatlngInfo.getAreaNum()).add(areaLatlngInfo);
        }

        for(Object obj: list) {
        	AreaCenter areaInfo = (AreaCenter) obj;
            List<AreaLatlng> latlngs = latlngMap.get(areaInfo.getAreaNum());
            areaInfo.setLatlngs(latlngs);

            result.add(areaInfo);
        }

        return result;
    }

    @Override
    public Integer getAreaCount(Map param) throws DataAccessException {
        return areaDao.getAreaCount(param);
    }

    @Override
    public Area getAreaInfo(Map param) throws DataAccessException {
        return areaDao.getAreaInfo(param);
    }

    @Override
    @Transactional
    public void registerArea(Area area, List<AreaLatlng> areaLatlngs) throws DataAccessException {
        areaDao.insertArea(area);

        for(AreaLatlng latlng: areaLatlngs) {
            latlng.setAreaNum(area.getAreaNum());
            areaLatlngDao.insertAreaLatlng(latlng);
        }

    }

    @Override
    @Transactional
    public void modifyArea(Area area, List<AreaLatlng> areaLatlngs) throws DataAccessException {
        areaDao.modifyArea(area);

        areaLatlngDao.deleteAreaLatlng(QueryParam.create("areaNum", area.getAreaNum()).build());
        for(AreaLatlng latlng: areaLatlngs) {
            latlng.setAreaNum(area.getAreaNum());
            areaLatlngDao.insertAreaLatlng(latlng);
        }
    }

    @Override
    @Transactional
    public void removeArea(Map param) throws DataAccessException {
        areaLatlngDao.deleteAreaLatlng(param);
        areaDao.deleteArea(param);
    }

    @Override
    @Transactional
    public void updateNodeUpdate(List<Long> nodeNumArray, Integer areaNum, String areaName) throws DataAccessException {
        Node nodeParam = new Node();
        nodeParam.setNodeNumArray(nodeNumArray);
        nodeParam.setAreaName(areaName);
        nodeParam.setAreaNum(areaNum);
        if(areaNum==null && StringUtils.isBlank(areaName)) {
            nodeDao.updateNodeAreaDel(nodeParam);
        } else {
            nodeDao.updateNode(nodeParam);
        }
    }

}
