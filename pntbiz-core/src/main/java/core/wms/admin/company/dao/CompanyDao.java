package core.wms.admin.company.dao;

import core.wms.admin.company.domain.Company;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

import java.util.List;

@Repository
public class CompanyDao extends BaseDao {

	public List<Company> getCompanyListAll() throws DataAccessException {
		return (List<Company>) list("getCompanyListAll");
	}
	
	public Company getCompanyInfo(Company company) throws DataAccessException {
		return (Company) select("getCompanyInfo", company);
	}
	
	public void updateCompany(Company company) throws DataAccessException {
		update("updateCompany", company);
	}
}