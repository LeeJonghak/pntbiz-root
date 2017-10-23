package core.wms.info.dao;

import core.wms.info.domain.AdvertCompany;
import core.wms.info.domain.AdvertCompanySearchParam;
import framework.db.dao.BaseDao;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdvertCompanyDao extends BaseDao {
	
	public Integer getAdvertCompanyCount(AdvertCompanySearchParam param) {
		return (Integer) select("getAdvertCompanyCount", param);
    }
	
	public List<?> getAdvertCompanyList(AdvertCompanySearchParam param) {
        List<?> list = this.list("getAdvertCompanyList", param);
        return list;
    }
	
	public List<?> getAdvertCompanyListByAll(AdvertCompanySearchParam param) {
        List<?> list = this.list("getAdvertCompanyListByAll", param);
        return list;
    }
	
	public AdvertCompany getAdvertCompanyInfo(AdvertCompany advertCompany) throws DataAccessException {
		return (AdvertCompany) select("getAdvertCompanyInfo", advertCompany);
	}
	
	public void insertAdvertCompany(AdvertCompany advertCompany) throws DataAccessException {
		insert("insertAdvertCompany", advertCompany);
	}
	
	public void updateAdvertCompany(AdvertCompany advertCompany) throws DataAccessException {
		update("updateAdvertCompany", advertCompany);
	}
	
	public void deleteAdvertCompany(AdvertCompany advertCompany) throws DataAccessException {
		delete("deleteAdvertCompany", advertCompany);
	}
	
}
