package wms.scanner.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.scanner.dao.ScannerServerDao;
import core.wms.scanner.domain.ScannerServer;

@Service
public class ScannerServerServiceImpl implements ScannerServerService {

	@Autowired
	private ScannerServerDao scannerServerDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ScannerServer getScannerServerInfo(ScannerServer scannerServer) throws DataAccessException {		
		ScannerServer scannerServerInfo = null;
		scannerServerInfo = scannerServerDao.getScannerServerInfo(scannerServer);
		logger.info("getScannerServerInfo {}", scannerServerInfo);
		return scannerServerInfo;
	}
	
	@Override
	public ScannerServer getScannerServerInfoByNum(ScannerServer scannerServer) throws DataAccessException {
		ScannerServer scannerServerInfo = null;
		scannerServerInfo = scannerServerDao.getScannerServerInfoByNum(scannerServer);
		logger.info("getScannerServerInfo {}", scannerServerInfo);
		return scannerServerInfo;
	}
	@Override
	public List<?> getScannerServerList(ScannerServer scannerServer) throws DataAccessException {
		List<?> scannerServerList = null;
		scannerServerList = scannerServerDao.getScannerServerList(scannerServer);		
		logger.info("getScannerServerList {}", scannerServerList.size());		
		return scannerServerList;
	}	
	
	@Override
	@Transactional
	public void registerScannerServer(ScannerServer scannerServer) throws DataAccessException {
		scannerServerDao.insertScannerServer(scannerServer);		
	}
	
	@Override
	@Transactional
	public void modifyScannerServer(ScannerServer scannerServer) throws DataAccessException {
		scannerServerDao.updateScannerServer(scannerServer);		
	}
	
	@Transactional
	public void removeScannerServer(ScannerServer scannerServer) throws DataAccessException {
		scannerServerDao.deleteScannerServer(scannerServer);		
	}

}
