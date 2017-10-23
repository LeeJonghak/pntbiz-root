package wms.admin.company.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.admin.company.dao.CompanyDao;
import core.wms.admin.company.domain.Company;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	private CompanyDao companyDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<?> getCompanyListAll() throws DataAccessException{
		List<?> companyList = companyDao.getCompanyListAll();
		return companyList;
	}

	@Override
	public Company getCompanyInfo(Company company) throws DataAccessException{
		Company companyInfo = null;
		companyInfo = companyDao.getCompanyInfo(company);
		logger.info("companyInfo {}", companyInfo);
		return companyInfo;
	}

	@Override
	@Transactional
	public void modifyCompany(Company company) throws DataAccessException{
		companyDao.updateCompany(company);
	}
}
