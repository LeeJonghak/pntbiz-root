package admin.authpackage.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.admin.authpackage.dao.dao.AuthPackageDao;
import core.admin.authpackage.dao.domain.AuthPackage;
import core.admin.authpackage.dao.domain.AuthPackageSearchParam;

@Service
public class AuthPackageServiceImpl implements AuthPackageService {

	@Autowired
	private AuthPackageDao authPackageDao;
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Override
	public boolean checkAuthPackage(AuthPackage authPackage) throws DataAccessException {
		Integer cnt = 0;
		cnt = authPackageDao.checkAuthPackage(authPackage);
		logger.info("checkAuthPackage {}", cnt);	
		if(cnt > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public AuthPackage getAuthPackageInfo(AuthPackage authPackage) throws DataAccessException {
		AuthPackage authPackageInfo = null;
		authPackageInfo = authPackageDao.getAuthPackageInfo(authPackage);
		logger.info("authPackageInfo", authPackageInfo);		
		return authPackageInfo;
	}

	@Override
	public Integer getAuthPackageCount(AuthPackageSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = authPackageDao.getAuthPackageCount(param);
		logger.info("getAuthPackageCount {}", cnt);		
		return cnt;
	}

	@Override
	public List<?> getAuthPackageList(AuthPackageSearchParam param) throws DataAccessException {
		List<?> authPackageList = authPackageDao.getAuthPackageList(param);
		logger.info("getAuthPackageList {}", authPackageList.size());		
		return authPackageList;
	}

	@Override
	@Transactional("transactionManager2")
	public void registerAuthPackage(AuthPackage authPackage) throws DataAccessException {
		authPackageDao.insertAuthPackage(authPackage);
	}

	@Override
	@Transactional("transactionManager2")
	public void modifyAuthPackage(AuthPackage authPackage) throws DataAccessException {
		authPackageDao.updateAuthPackage(authPackage);		
	}

	@Override
	@Transactional("transactionManager2")
	public void removeAuthPackage(AuthPackage authPackage) throws DataAccessException {
		authPackageDao.deleteAuthPackage(authPackage);
	}

	@Override
	public Integer getPackageNameCheck(AuthPackage authPackage) throws DataAccessException {
		return authPackageDao.getPackageNameCheck(authPackage);
	}

}