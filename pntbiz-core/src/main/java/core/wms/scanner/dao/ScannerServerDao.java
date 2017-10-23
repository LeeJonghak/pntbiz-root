package core.wms.scanner.dao;

import java.util.List;

import core.wms.scanner.domain.ScannerServer;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class ScannerServerDao extends BaseDao {
	
	public List<?> getScannerServerList(ScannerServer scannerServer) throws DataAccessException {
		return (List<?>) list("getScannerServerList", scannerServer);
	}	
	
	public ScannerServer getScannerServerInfo(ScannerServer scannerServer) throws DataAccessException {
		return (ScannerServer) select("getScannerServerInfo", scannerServer);
	}	
	
	public ScannerServer getScannerServerInfoByNum(ScannerServer scannerServer) throws DataAccessException {
		return (ScannerServer) select("getScannerServerInfoByNum", scannerServer);
	}	
	
	public void insertScannerServer(ScannerServer scannerServer) throws DataAccessException {
		insert("insertScannerServer", scannerServer);
	}

	public void updateScannerServer(ScannerServer scannerServer) throws DataAccessException {
		update("updateScannerServer", scannerServer);
	}	
	
	public void deleteScannerServer(ScannerServer scannerServer) throws DataAccessException {
		delete("deleteScannerServer", scannerServer);
	}	
	
}