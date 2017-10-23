package api.map.service;

import core.api.map.dao.FloorCodeDao;
import core.api.map.domain.FloorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FloorCodeServiceImpl implements FloorCodeService {

    @Autowired
    private FloorCodeDao dao;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public FloorCode getFloorCodeInfo(FloorCode floorCode)   throws DataAccessException {
        return dao.getFloorCodeInfo(floorCode);
    }

    @Override
    public List<FloorCode> getFloorCodeList(FloorCode floorCode) throws DataAccessException {
        return dao.getFloorCodeList(floorCode);
    }

    @Override
    @Cacheable(value = "CACHE_FLOOR_CODE_NODEID_LIST", key="#floorCode.nodeId.toString()")
    public List<String> getFloorCodeNodeIDList(FloorCode floorCode) throws DataAccessException {
        logger.debug("floorCode {}", floorCode);
        List<String> floorCodeNodeIDList = new ArrayList<String>();
        List<String> floorCodeNodeReverseIDList = new ArrayList<String>();
        FloorCode floorCodeInfo = getFloorCodeInfo(floorCode);
        floorCodeNodeReverseIDList.add(0, floorCodeInfo.getNodeId());
        int level = floorCodeInfo.getLevelNo();
        for(int i=0; i < level ; i++) {
            FloorCode floorCodeUpper = new FloorCode();
            floorCodeUpper.setUUID(floorCode.getUUID());
            floorCodeUpper.setNodeId(floorCodeInfo.getUpperNodeId());
            floorCodeInfo = getFloorCodeInfo(floorCodeUpper);
            floorCodeNodeReverseIDList.add(floorCodeInfo.getNodeId());
        }
        int idx = 0;
        for(int j=level;  j >= 0; j--) {
            floorCodeNodeIDList.add(idx, floorCodeNodeReverseIDList.get(j));
            idx++;
        }
        return floorCodeNodeIDList;
    }

    @Override
    public List<FloorCode> getFloorCodeLevelNoDescList(FloorCode floorCode) {
        List<FloorCode> floorCodeList = dao.getFloorCodeLevelNoDescList(floorCode);
        return floorCodeList;
    }

    @Override
    public List<FloorCode> getFloorCodeLevelNoAscList(FloorCode floorCode) {
        List<FloorCode> floorCodeList = dao.getFloorCodeLevelNoAscList(floorCode);
        return floorCodeList;
    }

    @Override
    public List<FloorCode> getFloorCodeHierarchyList(FloorCode floorCode) {
        List<FloorCode> floorCodeList = getFloorCodeHierarchy(floorCode, getFloorCodeLevelNoDescList(floorCode));
        return floorCodeList;
    }

    /**
     * 1차원 층코드를 Hierarchy 구조로 변경
     * order by levelNo desc 로 floorCodeList 데이터 셋팅 필요
     * @param floorCode
     * @return
     */
    private List<FloorCode> getFloorCodeHierarchy(FloorCode floorCode, List<FloorCode> floorCodeList) {
        Iterator<FloorCode> floorCodeDestIterator = floorCodeList.iterator();
        int maxLevel = floorCodeList.get(0).getLevelNo();
        int limitLevel = (floorCode.getLevelNo() != null) ? floorCode.getLevelNo() : 0;
        while(floorCodeDestIterator.hasNext()) {
            FloorCode floorCodeInfo = floorCodeDestIterator.next();
            int level = floorCodeInfo.getLevelNo();
            // 마지막 depth를 제외하고 처리
            if(maxLevel > level) {
                Iterator<FloorCode> floorCodeICompareIterator = floorCodeList.iterator();
                List<FloorCode> floorCodeChild = new ArrayList<FloorCode>();
                while(floorCodeICompareIterator.hasNext()) {
                    FloorCode floorCodeCompareInfo = floorCodeICompareIterator.next();
                    String upperNodeId = floorCodeCompareInfo.getUpperNodeId();
                    String nodeId = floorCodeInfo.getNodeId();
                    // 정보가 null일때 처리 안하도록
                    if(upperNodeId != null && nodeId != null && upperNodeId.equals(nodeId)) {
//                    if(floorCodeCompareInfo.getUpperNodeId().equals(floorCodeInfo.getNodeId())) {
                        floorCodeChild.add(floorCodeCompareInfo);
//                        floorCodeICompareIterator.remove();
                    }
                }
                if(!floorCodeChild.equals(null)) {
                    floorCodeInfo.setChildNode(floorCodeChild);
                }
            }
        }
        // level 0인 것만 남기도록
        Iterator<FloorCode> floorCodeIterator = floorCodeList.iterator();
        while(floorCodeIterator.hasNext()) {
            FloorCode floorCodeInfo = floorCodeIterator.next();
            int level = floorCodeInfo.getLevelNo();
            if(level > limitLevel) {
                floorCodeIterator.remove();
            }
        }
        return floorCodeList;
    }

    @Override
    public List<FloorCode> getFloorCodeChildNodeList(FloorCode floorCode, List<FloorCode> floorCodeDepthList) {
        List<FloorCode> floorCodeHierarchyList = getFloorCodeHierarchyList(floorCode);
        List<FloorCode> floorCodeChildNodeList = getFloorCodeChildNode(floorCodeDepthList, floorCodeHierarchyList);
        return floorCodeChildNodeList;
    }

    /**
     * FloorCode Hierarchy 전체에서 floorCodeDepthList에 있는 값(nodeId) 하위의 tree배열만 리턴
     * @param floorCodeDepthList
     * @param floorCodeHierarchyList
     * @return
     */
    @Override
    public List<FloorCode> getFloorCodeChildNode(List<FloorCode> floorCodeDepthList, List<FloorCode> floorCodeHierarchyList) {
        Iterator<FloorCode> floorCodeIterator = floorCodeHierarchyList.iterator();
        List<FloorCode> floorCodeChildList = null;
        int matchCnt = 0;
        while (floorCodeIterator.hasNext()) {
            FloorCode floorCodeInfo = floorCodeIterator.next();
            Iterator<FloorCode> floorCodeDepthIterator = floorCodeDepthList.iterator();
            if(floorCodeDepthIterator.hasNext()) {
                FloorCode floorCodeDepthInfo = floorCodeDepthIterator.next();
                if (floorCodeInfo.getLevelNo().equals(floorCodeDepthInfo.getLevelNo()) && floorCodeInfo.getNodeId().equals(floorCodeDepthInfo.getNodeId())) {
                    floorCodeChildList = floorCodeInfo.getChildNode();
                    matchCnt++;
                    floorCodeDepthIterator.remove();
                } else {
                    floorCodeIterator.remove();
                }
                if (matchCnt > 0) {
                    return getFloorCodeChildNode(floorCodeDepthList, floorCodeChildList);
                }
            } else {
                break;
            }
        }
        return floorCodeHierarchyList;
    }

    /**
     * FloorCode Hierarchy 전체 또는 일부에서 마지막 level의 노드들을 1차원 배열로 리턴
     * @param floorCodeList
     * @return
     */
    public List<FloorCode> getFloorCodeLastChildNodeList(List<FloorCode> floorCodeList) {
        Iterator<FloorCode> floorCodeIterator = floorCodeList.iterator();
        List<FloorCode> floorCodeChildList = new ArrayList<FloorCode>();
        int childCnt = 0;
        while (floorCodeIterator.hasNext()) {
            FloorCode floorCodeInfo = floorCodeIterator.next();
            int level = floorCodeInfo.getLevelNo();
            boolean isChild = !(floorCodeInfo.getChildNode() == null || floorCodeInfo.getChildNode().size() == 0);
            if(isChild == true) {
                for(FloorCode floorCode : floorCodeInfo.getChildNode()) {
                    floorCodeChildList.add(floorCode);
                    childCnt++;
                }
            }
        }
        if(childCnt == 0) {
            return floorCodeList;
        } else {
            return getFloorCodeLastChildNodeList(floorCodeChildList);
        }
    }


}
