package core.wms.map.dao;

import core.wms.map.domain.ChaosArea;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 혼돈지역 DAO
 * @author nohsoo 2015-04-24
 */
@Repository
public class ChaosAreaDao extends BaseDao {

    public List<?> getChaosAreaListAll(ChaosArea param) {
        List<?> list = this.list("getChaosAreaListAll", param);
        return list;
    }

    public void insertChaosArea(ChaosArea chaosArea) {
        this.insert("insertChaosArea", chaosArea);
    }

    public void modifyChaosArea(ChaosArea chaosArea) {
        this.update("modifyChaosArea", chaosArea);
    }

    public void deleteChaosArea(ChaosArea param) {
        this.delete("deleteChaosArea", param);
    }

}
