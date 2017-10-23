package api.common.service;

import core.api.common.domain.*;
import core.common.code.domain.Code;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommonService {
	
	public Company getCompanyInfo(Company company) throws DataAccessException;
	public Company getCompanyInfoByUUID(Company company) throws DataAccessException;
	
	public List<?> getCodeListByCD(Code code) throws DataAccessException;
	public Code getCodeInfo(Code code) throws DataAccessException;
	public Integer getCodeCheck(Code code) throws DataAccessException;
	
	public Sync getSyncInfo(Sync sync) throws DataAccessException;
	public List<?> getSyncList(Sync sync) throws DataAccessException; 
	public void updateSync(Sync sync) throws DataAccessException;
	
	public Calibration getCalibrationInfo(Calibration calibration) throws DataAccessException;
    public List<?> getCalibrationList(Calibration calibration) throws DataAccessException;
	public Calibration getCalibrationInfoByNum(Calibration calibration) throws DataAccessException;
	public void registerCalibration(Calibration calibration) throws DataAccessException;
	public void modifyCalibration(Calibration calibration) throws DataAccessException;
	public Object checkAuthorized(String UUID) throws DataAccessException, BadCredentialsException;

	@Transactional
	void insertSmsSendLog(SmsSendLog vo);

	@Transactional
	int updateSmsSendLog(SmsSendLog vo);

	SmsSendLog getSmsSendLogInfo(String transid);
}