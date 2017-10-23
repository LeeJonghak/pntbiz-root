package wms.map.service;

import wms.component.auth.LoginDetail;
import core.wms.map.domain.Node;
import core.wms.map.domain.NodePair;

import java.util.HashMap;
import java.util.List;

/**
 */
public interface NodeService {

    public int generationNodeId(Integer comNum);

    public Node getNode(LoginDetail loginDetail, Long nodeNum);

    public List<?> getNodeAll(LoginDetail loginDetail, String floor, String type);

    public void registerNode(LoginDetail loginDetail, Node node);

    public void modifyNode(LoginDetail loginDetail, Node node);

    public void deleteNode(LoginDetail loginDetail, Long nodeNum);

    public NodePair registerNodePair(LoginDetail loginDetail, Integer startPoint, Integer endPoint, String floor, String type) throws Exception;

    public void deleteNodePair(LoginDetail loginDetail, Integer nodeID);

    public void deleteNodePair(LoginDetail loginDetail, Integer startPoint, Integer endPoint);

    List<?> getPairAll(LoginDetail loginDetail, String floor, String type);

    public HashMap<String, String> getPOICateList();
}
