package wms.info.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import framework.web.util.StringUtil;
import core.wms.info.dao.AdvertCompanyDao;
import core.wms.info.domain.AdvertCompany;
import core.wms.info.domain.AdvertCompanySearchParam;

@Service
public class AdvertCompanyServiceImpl implements AdvertCompanyService {

	@Autowired
	private AdvertCompanyDao advertCompanyDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Integer getAdvertCompanyCount(AdvertCompanySearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = advertCompanyDao.getAdvertCompanyCount(param);
		logger.info("getAdvertCompanyCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getAdvertCompanyList(AdvertCompanySearchParam param) throws DataAccessException {
		List<?> advertCompanyList = null;
		advertCompanyList = advertCompanyDao.getAdvertCompanyList(param);
		logger.info("getAdvertCompanyList {}", advertCompanyList.size());
		return advertCompanyList;
	}
	
	@Override
	public List<?> getAdvertCompanyListAll(AdvertCompanySearchParam param) throws DataAccessException {
		List<?> advertCompanyList = null;
		advertCompanyList = advertCompanyDao.getAdvertCompanyListByAll(param);
		logger.info("getAdvertCompanyListAll {}", advertCompanyList.size());
		return advertCompanyList;
	}
	
	@Override
	public List<?> bindAdertCompanyList(List<?> list) throws DataAccessException, ParseException {
		List<AdvertCompany> alist = new ArrayList<AdvertCompany>();		
		for(int i=0; i<list.size(); i++) {
			AdvertCompany advertCompany = (AdvertCompany) list.get(i);
			alist.add(advertCompany);
		}
		return alist;
	}

	@Override
	public AdvertCompany getAdvertCompanyInfo(AdvertCompany advertCompany) 	throws DataAccessException {
		AdvertCompany advertCompanyInfo = null;
		advertCompanyInfo = advertCompanyDao.getAdvertCompanyInfo(advertCompany);
		logger.info("advertCompanyInfo {}", advertCompanyInfo);	
		return advertCompanyInfo;
	}
	
	@Override
	@Transactional
	public void registerAdvertCompany(HttpServletRequest request, AdvertCompany advertCompany 
			) throws DataAccessException {
		String acName = StringUtil.NVL(request.getParameter("acName"), "");
		advertCompany.setAcName(acName);
		advertCompanyDao.insertAdvertCompany(advertCompany);
	}
	
	@Override
	@Transactional
	public void modifyAdvertCompany(HttpServletRequest request, AdvertCompany advertCompany
			) throws DataAccessException {
		String acName = StringUtil.NVL(request.getParameter("acName"), "");
		advertCompany.setAcName(acName);
		advertCompanyDao.updateAdvertCompany(advertCompany);
	}
	
	@Override
	@Transactional
	public void removeAdvertCompany(AdvertCompany advertCompany) throws DataAccessException {
		advertCompanyDao.deleteAdvertCompany(advertCompany);
	}
}
