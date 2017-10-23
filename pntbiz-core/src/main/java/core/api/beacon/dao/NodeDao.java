package core.api.beacon.dao;

import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import core.api.beacon.domain.Node;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class NodeDao extends BaseDao {
	
	public Node getNode(Node node) {
		Node nodeInfo = (Node) this.select("getNode", node);
        return nodeInfo;
    }

    public List<?> getNodeList(HashMap<String, Object> param) {
        List<?> list = this.list("getNodeList", param);
        return list;
    }

    public List<?> getNodeEdgeList(HashMap<String, Object> param) {
        List<?> list = this.list("getNodeEdgeList", param);
        return list;
    }

    public List<?> getNodeContentsList(HashMap<String, Object> param) {
        return this.list("getNodeContentsList", param);
    }

    public List<?> getNodeCategoryList() {
        return this.list("getNodeCategoryList");
    }
    
    public void deleteNode(HashMap<String, Object> param) {
        this.delete("deleteNode", param);
    }
    
    public void deleteNodePair(HashMap<String, Object> param) {
        this.delete("deleteNodePair", param);
    }

}
