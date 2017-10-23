package core.wms.map.dao;

import core.wms.map.domain.Node;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class NodeDao extends BaseDao {

    public int getNodeID(HashMap<String, Object> param) {
        int nodeID = (Integer)this.select("getNodeID", param);
        return nodeID;
    }

    public List<?> getNodeAll(HashMap<String, Object> param) {
        return this.list("getNodeAll", param);
    }

    public Node getNode(HashMap<String, Object> param) {
        return (Node)this.select("getNode", param);
    }

    public long insertNode(Node node) {
        this.update("insertNode", node);
        return node.getNodeNum();
    }

    public void updateNode(Node node) {
        this.update("updateNode", node);
    }

	public void updateNodeAreaDel(Node node) {
		this.update("updateNodeAreaDel", node);
	}

    public void deleteNode(HashMap<String, Object> param) {
        this.delete("deleteNode", param);
    }

    public List<?> getPOICateList() {
        List<?> list = this.list("getPOICateList");
        return list;
    }

}
