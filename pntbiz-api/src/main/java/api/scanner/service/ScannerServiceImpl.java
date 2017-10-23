package api.scanner.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.api.scanner.dao.ScannerDao;
import core.api.scanner.domain.Scanner;

@Service
public class ScannerServiceImpl implements ScannerService {
	
	@Autowired
	private ScannerDao scannerDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public List<?> getScannerList(Scanner scanner) throws DataAccessException {
		List<?> scannerList = null;
		scannerList = scannerDao.getScannerList(scanner);		
		logger.info("getScannerList {}", scannerList.size());		
		return scannerList;
	}		
}
