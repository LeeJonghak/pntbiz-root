package wms.scanner.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.scanner.dao.ScannerDao;
import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;

@Service
public class ScannerServiceImpl implements ScannerService {
	
	@Autowired
	private ScannerDao scannerDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Scanner getScannerInfo(Scanner scanner) throws DataAccessException {		
		Scanner scannerInfo = null;
		scannerInfo = scannerDao.getScannerInfo(scanner);
		logger.info("getScannerInfo {}", scannerInfo);
		return scannerInfo;
	}
	
	@Override
	public Scanner getScannerInfoByNum(Scanner scanner) throws DataAccessException {
		Scanner scannerInfo = null;
		scannerInfo = scannerDao.getScannerInfoByNum(scanner);
		logger.info("getScannerInfo {}", scannerInfo);
		return scannerInfo;
	}
	
	@Override
	public Integer getScannerCount(ScannerSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = scannerDao.getScannerCount(param);
		logger.info("getScannerCount {}", cnt);	
		return cnt;
	}	
	
	@Override
	public List<?> getScannerList(ScannerSearchParam param) throws DataAccessException {
		List<?> scannerList = null;
		scannerList = scannerDao.getScannerList(param);		
		logger.info("getScannerList {}", scannerList.size());		
		return scannerList;
	}	
	
	@Override
	public boolean checkScannerDuplication(Scanner scanner) throws DataAccessException {
		Integer cnt = 0;
		cnt = scannerDao.checkScannerDuplication(scanner);
		logger.info("res {}", cnt);	
		if(cnt > 0) {
			return true;
		} else {
			return false;
		}
	}		
	
	@Override
	@Transactional
	public void registerScanner(Scanner scanner) throws DataAccessException {
		scannerDao.insertScanner(scanner);		
	}
	
	@Override
	@Transactional
	public void modifyScanner(Scanner scanner) throws DataAccessException {
		scannerDao.updateScanner(scanner);		
	}
	
	@Override
	@Transactional
	public void batchScanner(Scanner scanner) throws DataAccessException {
		scannerDao.batchScanner(scanner);		
	}
	
	@Override
	@Transactional
	public void removeScanner(Scanner scanner) throws DataAccessException {
		scannerDao.deleteScanner(scanner);		
	}
	
}
