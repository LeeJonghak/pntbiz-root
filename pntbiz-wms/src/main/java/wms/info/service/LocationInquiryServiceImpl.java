package wms.info.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.common.code.domain.Code;
import core.wms.info.dao.LocationInquiryDao;
import core.wms.info.domain.LocationInquiry;
import core.wms.info.domain.LocationInquirySearchParam;
import core.wms.info.domain.LocationRecord;
import core.wms.info.domain.LocationRecordSearchParam;

@Service
public class LocationInquiryServiceImpl implements LocationInquiryService {

	@Autowired
	private LocationInquiryDao locationInquiryDao;
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Override
	public Integer getLocationInquiryCount(LocationInquirySearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = locationInquiryDao.getLocationInquiryCount(param);
		logger.info("getLocationInquiryCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getLocationInquiryList(LocationInquirySearchParam param) throws DataAccessException {
		List<?> locationInquiryList = null;
		locationInquiryList = locationInquiryDao.getLocationInquiryList(param);
		logger.info("getLocationInquiryList {}", locationInquiryList.size());
		return locationInquiryList;
	}

	@Override
	public List<?> bindLocationInquiryList(List<?> list, List<?> osCD, List<?> serviceCD) throws DataAccessException, ParseException {
		List<LocationInquiry> llist = new ArrayList<LocationInquiry>();		
		for(int i=0; i<list.size(); i++) {
			LocationInquiry locationInquiry = (LocationInquiry) list.get(i);
			for(int j=0; j<osCD.size(); j++) {
				Code code = (Code) osCD.get(j);
				if(locationInquiry.getOs().equals(code.getsCD())) {
					locationInquiry.setOs(code.getsName());
				}
			}
			for(int j=0; j<serviceCD.size(); j++) {
				Code code = (Code) serviceCD.get(j);
				if(locationInquiry.getService().equals(code.getsCD())) {
					locationInquiry.setService(code.getsName());
				}
			}
			llist.add(locationInquiry);
		}
		return llist;
	}
	
	@Override
	public Integer getLocationRecordCount(LocationRecordSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = locationInquiryDao.getLocationRecordCount(param);
		logger.info("getLocationRecordCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getLocationRecordList(LocationRecordSearchParam param) throws DataAccessException {
		List<?> locationRecordList = null;
		locationRecordList = locationInquiryDao.getLocationRecordList(param);
		logger.info("getLocationRecordList {}", locationRecordList.size());
		return locationRecordList;
	}

	@Override
//	@Transactional
	public void registerLocationRecord(LocationRecord locationRecord) throws DataAccessException {
		locationInquiryDao.insertLocationRecord(locationRecord);		
	}
}
