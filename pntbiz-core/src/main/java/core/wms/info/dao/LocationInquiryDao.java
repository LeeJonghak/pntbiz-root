package core.wms.info.dao;

import core.wms.info.domain.LocationInquirySearchParam;
import core.wms.info.domain.LocationRecord;
import core.wms.info.domain.LocationRecordSearchParam;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class LocationInquiryDao extends BaseDao {
	
	public Integer getLocationInquiryCount(LocationInquirySearchParam param) {
		return (Integer) select("getLocationInquiryCount", param);
    }
	
	public List<?> getLocationInquiryList(LocationInquirySearchParam param) {
        List<?> list = this.list("getLocationInquiryList", param);
        return list;
    }
	
	public Integer getLocationRecordCount(LocationRecordSearchParam param) {
		return (Integer) select("getLocationRecordCount", param);
    }
	
	public List<?> getLocationRecordList(LocationRecordSearchParam param) {
        List<?> list = this.list("getLocationRecordList", param);
        return list;
    }
	
	public void insertLocationRecord(LocationRecord locationRecord) throws DataAccessException {
		insert("insertLocationRecord", locationRecord);
	}
	
}
