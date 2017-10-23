package wms.admin.company.service;

import org.springframework.dao.DataAccessException;

import core.wms.admin.company.domain.Company;

import java.util.List;

public interface CompanyService {

	public List<?> getCompanyListAll() throws DataAccessException;
	public Company getCompanyInfo(Company company) throws DataAccessException;

	// Company Transaction
	public void modifyCompany(Company company) throws DataAccessException;
}