package wms.scanner.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.scanner.domain.ScannerServer;

public interface ScannerServerService  {
	
	// scanner server info
	public ScannerServer getScannerServerInfo(ScannerServer scannerServer) throws DataAccessException;
	public ScannerServer getScannerServerInfoByNum(ScannerServer scannerServer) throws DataAccessException;
	public List<?> getScannerServerList(ScannerServer scannerServer) throws DataAccessException;	
	
	// scannerServer transaction
	public void registerScannerServer(ScannerServer scannerServer) throws DataAccessException;
	public void modifyScannerServer(ScannerServer scannerServer) throws DataAccessException;
	public void removeScannerServer(ScannerServer scannerServer) throws DataAccessException;	
}
