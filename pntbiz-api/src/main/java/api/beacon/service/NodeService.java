package api.beacon.service;

import core.api.beacon.domain.Node;
import core.api.contents.domain.Contents;

import org.springframework.dao.DataAccessException;

import java.text.ParseException;
import java.util.List;

/**
 */
public interface NodeService {
	
	public Node getNode(Node node) throws DataAccessException;
    public List<?> getNodeList(String UUID, String floor) throws DataAccessException;
    public List<?> getNodeList(String UUID, String floor, String type) throws DataAccessException;
    public List<?> getNodeEdgeList(String UUID, String floor) throws DataAccessException;
    public List<?> getNodeEdgeList(String UUID, String floor, String type) throws DataAccessException;
    public List<?> getNodeContentsList(String UUID, String floor) throws DataAccessException, ParseException;
    public List<?> getNodeContentsList(String UUID, String floor, String type) throws DataAccessException, ParseException;
    public List<?> bindNodeContentsList(List<?> list) throws ParseException;
    public Contents bindNodeContentsInfo(Contents contents) throws ParseException;
    public List<?> getNodeCategoryList() throws DataAccessException;
    
    public void deleteNode(String UUID, Long nodeNum);
    public void deleteNodePair(String UUID, Integer nodeID);

}
