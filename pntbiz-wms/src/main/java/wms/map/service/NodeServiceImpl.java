package wms.map.service;

import wms.component.auth.LoginDetail;
import core.wms.map.dao.NodeDao;
import core.wms.map.dao.NodePairDao;
import core.wms.map.domain.Node;
import core.wms.map.domain.NodePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.common.code.domain.Code;

import java.util.HashMap;
import java.util.List;

/**
 */
@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    private NodeDao nodeDao;

    @Autowired
    private NodePairDao nodePairDao;

    @Override
    public List<?> getNodeAll(LoginDetail loginDetail, String floor, String type) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("floor", floor);
        param.put("type", type);
        return nodeDao.getNodeAll(param);
    }


    /**
     * 2015-11-18 nohsoo 사용하지 않음, 제거 예정
     */
    @Override
    @Deprecated
    public int generationNodeId(Integer comNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", comNum);
        int nodeID = nodeDao.getNodeID(param);
        return nodeID;
    }

    @Override
    public Node getNode(LoginDetail loginDetail, Long nodeNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("nodeNum", nodeNum);
        Node node = nodeDao.getNode(param);
        return node;
    }

    @Override
    @Transactional
    public void registerNode(LoginDetail loginDetail, Node node) {
        node.setComNum(loginDetail.getCompanyNumber());
        nodeDao.insertNode(node);
    }

    @Override
    @Transactional
    public void modifyNode(LoginDetail loginDetail, Node node) {
        nodeDao.updateNode(node);
    }

    @Override
    @Transactional
    public void deleteNode(LoginDetail loginDetail, Long nodeNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("nodeNum", nodeNum);
        nodeDao.deleteNode(param);
    }

    @Override
    @Transactional
    public NodePair registerNodePair(LoginDetail loginDetail, Integer startPoint, Integer endPoint, String floor, String type) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("startPoint", startPoint);
        param.put("endPoint", endPoint);
        param.put("floor", floor);
        param.put("type", type);
        //NodePair nodePair = nodePairDao.getNodePair(param);
        int cnt = nodePairDao.countNodePair(param);
        if(cnt>0) {
            throw new Exception("이미 추가된 노드연결입니다.");
        }

        NodePair vo = new NodePair();
        vo.setFloor(floor);
        vo.setComNum(loginDetail.getCompanyNumber());
        vo.setStartPoint(startPoint);
        vo.setEndPoint(endPoint);
        vo.setType(type);
        nodePairDao.insertNodePair(vo);

        return vo;
    }

    @Override
    @Transactional
    public void deleteNodePair(LoginDetail loginDetail, Integer nodeID) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("nodeID", nodeID);
        nodePairDao.deleteNodePair(param);
    }

    @Override
    @Transactional
    public void deleteNodePair(LoginDetail loginDetail, Integer startPoint, Integer endPoint) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("startPoint", startPoint);
        param.put("endPoint", endPoint);
        nodePairDao.deleteNodePair(param);
    }

    @Override
    public List<?> getPairAll(LoginDetail loginDetail, String floor, String type) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("floor", floor);
        param.put("type", type);
        List<?> list = nodePairDao.getNodePairAll(param);
        return list;
    }

    public HashMap<String, String> getPOICateList() {
        List<?> list = nodeDao.getPOICateList();
        HashMap<String, String> poiCategory = new HashMap<String, String>();
        for(Object obj: list) {
            Code code = (Code)obj;
            poiCategory.put(code.getsCD(), code.getsName());
        }

        return poiCategory;
    }

}
