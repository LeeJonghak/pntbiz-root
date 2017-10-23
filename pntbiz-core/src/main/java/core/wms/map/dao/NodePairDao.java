package core.wms.map.dao;

import core.wms.map.domain.NodePair;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class NodePairDao extends BaseDao {

    public List<?> getNodePairAll(HashMap<String, Object> param) {
        return this.list("getNodePairAll", param);
    }
    public NodePair getNodePair(HashMap<String, Object> param) {
        return (NodePair)this.select("getNodePair", param);
    }

    public int countNodePair(HashMap<String, Object> param) {
        return (Integer)this.select("countNodePair", param);
    }

    public long insertNodePair(NodePair nodePair) {
        this.insert("insertNodePair", nodePair);
        return nodePair.getPairNum();
    }

    public void updateNodePair(NodePair nodePair) {
        this.update("updateNodePair", nodePair);
    }

    public void deleteNodePair(HashMap<String, Object> param) {
        this.delete("deleteNodePair", param);
    }

}
