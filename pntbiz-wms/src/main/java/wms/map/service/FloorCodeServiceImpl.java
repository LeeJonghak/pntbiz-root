package wms.map.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.map.dao.FloorCodeDao;
import core.wms.map.domain.FloorCode;

@Service
public class FloorCodeServiceImpl implements FloorCodeService {

    @Autowired
    private FloorCodeDao dao;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Integer getFloorCodeCount(FloorCode param) throws DataAccessException {
        return dao.getFloorCodeCount(param);
    }

    @Override
    public List<?> getFloorCodeList(FloorCode param) throws DataAccessException {
        List<?> paramList = null;
        paramList = dao.getFloorCodeList(param);
        logger.info("getFloorCodeList {}", paramList.size());
        return paramList;
    }

    @Override
    public FloorCode getFloorCodeInfo(FloorCode param)   throws DataAccessException {
        return dao.getFloorCodeInfo(param);
    }

    @Override
    @Transactional
    public void registerFloorCode(FloorCode param) throws DataAccessException {
        dao.insertFloorCode(param);
    }

    @Override
    @Transactional
    public void modifyFloorCode(FloorCode param) throws DataAccessException {
        dao.updateFloorCode(param);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void removeFloorCode(FloorCode param) throws DataAccessException {
        System.out.println();

        FloorCode paramVo = new FloorCode();
        //

        List<String> finalArray = new ArrayList<String>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<String> upperArray = new ArrayList<String>();
        upperArray.add(param.getNodeId());
        paramMap.put("comNum", param.getComNum());
        paramMap.put("array", upperArray);

        for(int idx = 0; idx < 10; idx++){
            finalArray.addAll((Collection<? extends String>) paramMap.get("array"));
            upperArray = (List<String>) dao.getFloorCodeIdList(paramMap);

            if(upperArray == null || upperArray.size() <= 0)
                break;
            paramMap.put("array", upperArray);
        }

        for(String id: finalArray){
            System.out.println(id);
        }

        paramMap.put("array", finalArray);
        if(finalArray.size() > 0){
            dao.deleteFloorCodeList(paramMap);
        }
    }

    /*private List<String> test(Map<String, PresenceRequestParam> paramMap){
        List<String> rtn = new ArrayList<String>();

        List<String> test = new ArrayList<String>();
        test = (List<String>) dao.getFloorCodeIdList(paramMap);
        if(rtn != null && rtn.size() > 0){
            paramMap.put("array", test);


        }
        return rtn;
    }*/

}
