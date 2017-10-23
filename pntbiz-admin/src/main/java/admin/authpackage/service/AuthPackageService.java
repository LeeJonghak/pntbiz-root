package admin.authpackage.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.admin.authpackage.dao.domain.AuthPackage;
import core.admin.authpackage.dao.domain.AuthPackageSearchParam;

public interface AuthPackageService {
	
	public boolean checkAuthPackage(AuthPackage authPackage) throws DataAccessException;
	
	public AuthPackage getAuthPackageInfo(AuthPackage authPackage) throws DataAccessException;
	
	public Integer getAuthPackageCount(AuthPackageSearchParam param) throws DataAccessException;
	public List<?> getAuthPackageList(AuthPackageSearchParam param) throws DataAccessException;
	
	public Integer getPackageNameCheck(AuthPackage authPackage) throws DataAccessException;
	
	// Code Transaction
	public void registerAuthPackage(AuthPackage authPackage) throws DataAccessException;
	public void modifyAuthPackage(AuthPackage authPackage) throws DataAccessException;
	public void removeAuthPackage(AuthPackage authPackage) throws DataAccessException;
	
}
