package wms.info.service;

import core.wms.info.domain.LocationInquirySearchParam;
import core.wms.info.domain.LocationRecord;
import core.wms.info.domain.LocationRecordSearchParam;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

public interface LocationInquiryService {
	public Integer getLocationInquiryCount(LocationInquirySearchParam param) throws DataAccessException;
	public List<?> getLocationInquiryList(LocationInquirySearchParam param) throws DataAccessException;
	public List<?> bindLocationInquiryList(List<?> list, List<?> osCD, List<?> serviceCD) throws DataAccessException, ParseException;
	
	public Integer getLocationRecordCount(LocationRecordSearchParam param) throws DataAccessException;
	public List<?> getLocationRecordList(LocationRecordSearchParam param) throws DataAccessException;
	public void registerLocationRecord(LocationRecord locationRecord) throws DataAccessException;
}