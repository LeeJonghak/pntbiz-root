package core.api.common.dao;

import core.api.common.domain.Company;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class CompanyDao extends BaseDao {	
	
	public Company getCompanyInfo(Company company) throws DataAccessException {
		return (Company) select("getCompanyInfo", company);
	}
	
	public Company getCompanyInfoByUUID(Company company) throws DataAccessException {
		return (Company) select("getCompanyInfoByUUID", company);
	}
}