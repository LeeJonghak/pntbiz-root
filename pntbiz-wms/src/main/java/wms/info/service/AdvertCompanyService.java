package wms.info.service;

import core.wms.info.domain.AdvertCompany;
import core.wms.info.domain.AdvertCompanySearchParam;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

public interface AdvertCompanyService {
	public Integer getAdvertCompanyCount(AdvertCompanySearchParam param) throws DataAccessException;
	public List<?> getAdvertCompanyList(AdvertCompanySearchParam param) throws DataAccessException;
	public List<?> getAdvertCompanyListAll(AdvertCompanySearchParam param) throws DataAccessException;
	public List<?> bindAdertCompanyList(List<?> list) throws DataAccessException, ParseException;
	
	public AdvertCompany getAdvertCompanyInfo(AdvertCompany advertCompany) throws DataAccessException;
	
	public void registerAdvertCompany(HttpServletRequest request, AdvertCompany advertCompany) throws DataAccessException;
	public void modifyAdvertCompany(HttpServletRequest request, AdvertCompany advertCompany) throws DataAccessException;
	public void removeAdvertCompany(AdvertCompany advertCompany) throws DataAccessException;	
}