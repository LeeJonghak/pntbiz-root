package core.wms.map.dao;

import core.wms.map.domain.Group;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 그룹 DAO
 * 2015-05-07 nohsoo
 */
@Repository
public class GroupDao extends BaseDao {

    public Group getGroup(HashMap<String, Object> param) {
        Group group = (Group)this.select("getGroupInfo", param);
        return group;
    }

    public List<?> getGroupList(HashMap<String, Object> param) {
        List<?> list = this.list("getGroupList", param);
        return list;
    }

    public Integer getGroupCount(HashMap<String, Object> param) {
        Integer count = (Integer)this.select("getGroupCount", param);
        return count;
    }

    public int insertGroup(Group group) {
        this.insert("insertGroup", group);
        return group.getGroupNum();
    }

    public void modifyGroup(Group vo) {
        this.update("modifyGroup", vo);
    }

    public void deleteGroup(Group vo) {
        this.delete("deleteGroup", vo);
    }

}
