package core.admin.authpackage.dao.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.admin.authpackage.dao.domain.AuthPackage;
import core.admin.authpackage.dao.domain.AuthPackageSearchParam;
import framework.db.dao.BaseDao2;

@Repository
public class AuthPackageDao extends BaseDao2 {
	
	public Integer checkAuthPackage(AuthPackage authPackage) throws DataAccessException {		
		return (Integer) select("checkAuthPackage", authPackage);
	}
	
	public AuthPackage getAuthPackageInfo(AuthPackage authPackage) throws DataAccessException {
		return (AuthPackage) select("getAuthPackageInfo", authPackage);
	}

	public Integer getAuthPackageCount(AuthPackageSearchParam param) throws DataAccessException {
		return (Integer) select("getAuthPackageCount", param);
	}

	public List<?> getAuthPackageList(AuthPackageSearchParam param) 	throws DataAccessException {
		return (List<?>) list("getAuthPackageList", param);
	}

	public Integer getPackageNameCheck(AuthPackage authPackage) throws DataAccessException {		
		return (Integer) select("getAuthPackageCheck", authPackage);		
	}

	public void insertAuthPackage(AuthPackage authPackage) throws DataAccessException {		
		insert("insertAuthPackage", authPackage);		
	}
	
	public void updateAuthPackage(AuthPackage authPackage) throws DataAccessException {		
		update("updateAuthPackage", authPackage);
	}
	
	public void deleteAuthPackage(AuthPackage authPackage) throws DataAccessException {
		delete("deleteAuthPackage", authPackage);		
	}
	
}