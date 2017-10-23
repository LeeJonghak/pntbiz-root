package custom.Sample.common.service;

import api.common.service.CommonServiceImpl;
import core.api.common.dao.*;
import core.api.common.domain.Company;
import core.api.oauth.dao.OauthClientDao;
import core.common.code.dao.CodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SampleCommonServiceImpl extends CommonServiceImpl {

	@Autowired
	private CodeDao codeDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private SyncDao syncDao;

	@Autowired
	private CalibrationDao calibrationDao;

	@Autowired
	private SmsSendLogDao smsSendLogDao;

	@Autowired
	private OauthClientDao oauthClientDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Company getCompanyInfoByUUID(Company company) throws DataAccessException{
		Company companyInfo = null;
		companyInfo = companyDao.getCompanyInfoByUUID(company);
		companyInfo.setComName(companyInfo.getComName() + "-Sample Custom");
		logger.info("companyInfoByUUID {}", companyInfo);
		return companyInfo;
	}

}
