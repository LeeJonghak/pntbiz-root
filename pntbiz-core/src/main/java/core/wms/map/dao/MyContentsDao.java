package core.wms.map.dao;

import core.wms.map.domain.ContentsMapping;
import framework.db.dao.BaseDao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 */
@Repository
public class MyContentsDao extends BaseDao {

    public void insertContentMapping(ContentsMapping vo) throws DataAccessException {
    	this.insert("insertContentMapping", vo);
    }

    /**
     * 컨텐츠 맵핑 정보 수정
     * create: nohsoo 2015-04-22 evtNum 값을 업데이트하기 위해서 만듬.
     *
     * @param vo
     */
    public void modifyContentMapping(ContentsMapping vo) {
        this.update("modifyContentMapping", vo);
    }

    public void deleteContentMapping(ContentsMapping vo) {
        this.delete("deleteContentMapping", vo);
    }

    public List<?> getMyBeaconContentsList(HashMap<String, Object> param) {

        List<?> list = this.list("getMyBeaconContentsList", param);
        return list;
    }

    public List<?> getMyNodeContentsList(HashMap<String, Object> param) {
        List<?> list = this.list("getMyNodeContentsList", param);
        return list;
    }

    public List<?> getMyGeofencingContentsList(HashMap<String, Object> param) {
        List<?> list = this.list("getMyGeofencingContentsList", param);
        return list;
    }


    public List<?> getContentsList(HashMap<String, Object> param) {
        List<?> list = this.list("getMapContentsList", param);
        return list;

    }

    public int countContentsList(HashMap<String, Object> param) {
        int cnt = (Integer)this.select("countMapContentsList", param);
        return cnt;
    }


}
