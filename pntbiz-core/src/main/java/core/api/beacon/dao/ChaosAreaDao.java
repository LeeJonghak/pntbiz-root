package core.api.beacon.dao;

import core.api.beacon.domain.ChaosArea;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TB_CHAOS_AREA 혼돈지역
 * @author nohsoo 2015-04-23
 */
@Repository
public class ChaosAreaDao extends BaseDao {

    public List<?> getChaosAreaListAll(ChaosArea param) {
        List<?> list = this.list("getChaosAreaListAll", param);
        return list;
    }

}
