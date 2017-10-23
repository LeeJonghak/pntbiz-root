package core.wms.scanner.dao;

import java.util.List;

import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class ScannerDao extends BaseDao {
	
	public Integer checkScannerDuplication(Scanner scanner) throws DataAccessException {
		return (Integer) select("checkScannerDuplication", scanner);
	}
	
	public Scanner getScannerInfo(Scanner scanner) throws DataAccessException {
		return (Scanner) select("getScannerInfo", scanner);
	}	

	public Scanner getScannerInfoByNum(Scanner scanner) throws DataAccessException {
		return (Scanner) select("getScannerInfoByNum", scanner);
	}

	public Integer getScannerCount(ScannerSearchParam param) throws DataAccessException {
		return (Integer) select("getScannerCount", param);
	}

	public List<?> getScannerList(ScannerSearchParam param) throws DataAccessException {
		return (List<?>) list("getScannerList", param);
	}	

	public void insertScanner(Scanner scanner) throws DataAccessException {
		insert("insertScanner", scanner);
	}

	public void updateScanner(Scanner scanner) throws DataAccessException {
		update("updateScanner", scanner);
	}	
	
	public void batchScanner(Scanner scanner) throws DataAccessException {
		update("batchScanner", scanner);
	}
	
	public void deleteScanner(Scanner scanner) throws DataAccessException {
		delete("deleteScanner", scanner);
	}	
	
}