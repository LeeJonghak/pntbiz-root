package api.common.service;

import core.api.common.dao.*;
import core.api.common.domain.*;
import core.api.oauth.dao.OauthClientDao;
import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {

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
	public Company getCompanyInfo(Company company) throws DataAccessException{
		Company companyInfo = null;
		companyInfo = companyDao.getCompanyInfo(company);
		logger.info("companyInfo {}", companyInfo);
		return companyInfo;
	}

	@Override
	public Company getCompanyInfoByUUID(Company company) throws DataAccessException{
		Company companyInfo = null;
		companyInfo = companyDao.getCompanyInfoByUUID(company);
		logger.info("companyInfoByUUID {}", companyInfo);
		return companyInfo;
	}

	@Override
	public List<?> getCodeListByCD(Code code) throws DataAccessException {
		List<?> codeList = null;
		codeList = codeDao.getCodeListByCD(code);
		logger.info("getCodeListByCD {}", codeList.size());
		return codeList;
	}

	@Override
	public Code getCodeInfo(Code code) throws DataAccessException {
		Code codeInfo = null;
		codeInfo = codeDao.getCodeInfo(code);
		logger.info("codeInfo {}", codeInfo);
		return codeInfo;
	}

	@Override
	public Integer getCodeCheck(Code code) throws DataAccessException {
		Integer cnt = 0;
		cnt = codeDao.getCodeCheck(code);
		logger.info("getCodeCheck {}", cnt);
		return cnt;
	}

	@Override
	@Transactional
	public Sync getSyncInfo(Sync sync) throws DataAccessException {
		Sync syncInfo = null;
		syncInfo = syncDao.getSyncInfo(sync);
		// 추가
		if(syncInfo == null) {
			syncDao.insertSync(sync);
			syncInfo = syncDao.getSyncInfo(sync);
		}
		logger.info("syncInfo {}", syncInfo);
		return syncInfo;
	}

	@Override
	public List<?> getSyncList(Sync sync) throws DataAccessException {
		List<?> syncList = syncDao.getSyncList(sync);
		logger.debug("syncList info:", syncList);
		return syncList;
	}

	@Override
	@Transactional
	public void updateSync(Sync sync) throws DataAccessException {
		Sync syncInfo = null;
		syncInfo = syncDao.getSyncInfo(sync);
		// 추가
		if(syncInfo == null) {
			syncDao.insertSync(sync);
		}
		syncDao.updateSync(sync);
	}

	@Override
	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException {
		Calibration calibrationInfo = null;
		calibrationInfo = calibrationDao.getCalibrationInfo(calibration);
		logger.info("calibrationInfo {}", calibrationInfo);
		return calibrationInfo;
	}

	@Override
	public List<?> getCalibrationList(Calibration calibration) throws DataAccessException {
		List<?> list = calibrationDao.getCalibrationList(calibration);
		logger.info("calibrationList {}", list);
		return list;
	}

	@Override
	public Calibration getCalibrationInfoByNum(Calibration calibration) throws DataAccessException {
		Calibration calibrationInfo = null;
		calibrationInfo = calibrationDao.getCalibrationInfoByNum(calibration);
		logger.info("calibrationInfo {}", calibrationInfo);
		return calibrationInfo;
	}

	@Override
	@Transactional
	public void registerCalibration(Calibration calibration) throws DataAccessException {
		calibrationDao.insertCalibration(calibration);
	}

	@Override
	@Transactional
	public void modifyCalibration(Calibration calibration) throws DataAccessException {
		calibrationDao.updateCalibration(calibration);
	}


	public Object checkAuthorized(String UUID) throws DataAccessException, BadCredentialsException {

		Company comParam = new Company();
		comParam.setUUID(UUID);
		Company company = companyDao.getCompanyInfoByUUID(comParam);
		if(StringUtils.equals("1", company.getOauthEnabled())) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication==null) {
				throw new BadCredentialsException("unauthorized");
			}
			else {
				String principal = (String)authentication.getPrincipal();
				if(StringUtils.equals("anonymousUser", principal)) {
					if(OAuth2AuthenticationDetails.class.equals(authentication.getDetails().getClass())) {
						throw new BadCredentialsException("oauth2 auth error");
					} else {
						throw new BadCredentialsException("missing access token");
					}
				} else {
					throw new BadCredentialsException("missing access token");
				}
			}

		} else {
			return true;
		}

		//throw new BadCredentialsException("unauthorized");
	}

	@Override
	@Transactional
	public void insertSmsSendLog(SmsSendLog vo) {
		smsSendLogDao.insertSmsSendLog(vo);
	}

	@Override
	@Transactional
	public int updateSmsSendLog(SmsSendLog vo) {
		return smsSendLogDao.updateSmsSendLog(vo);
	}

	@Override
	public SmsSendLog getSmsSendLogInfo(String transid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("transid", transid);
		SmsSendLog result = smsSendLogDao.getSmsSendLogInfo(param);
		return result;
	}

}
