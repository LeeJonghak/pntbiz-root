package core.admin.company.dao;
import java.util.List;

import core.admin.company.domain.CompanySearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;
import core.admin.company.domain.Company;

@Repository
public class CompanyDao extends BaseDao {

	public Integer getCompanyCount(CompanySearchParam param) throws DataAccessException {
		return (Integer) select("getCompanyCount", param);
	}

	public List<?> getCompanyList(CompanySearchParam param) throws DataAccessException {
		return (List<?>) list("getCompanyList", param);
	}

	public Company getCompanyInfo(Company company) throws DataAccessException {
		return (Company) select("getCompanyInfo", company);
	}

	public Integer insertCompany(Company company) throws DataAccessException {
		insert("insertCompany", company);
		return company.getComNum();
	}

	public void updateCompany(Company company) throws DataAccessException {
		update("updateCompany", company);
	}

	public void deleteCompany(Company company) throws DataAccessException {
		delete("deleteCompany", company);
	}
}