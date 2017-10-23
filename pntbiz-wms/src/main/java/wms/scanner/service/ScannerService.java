package wms.scanner.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;

public interface ScannerService {
	
	// scanner info
	public Scanner getScannerInfo(Scanner scanner) throws DataAccessException;
	public Scanner getScannerInfoByNum(Scanner scanner) throws DataAccessException;
	public Integer getScannerCount(ScannerSearchParam param) throws DataAccessException;
	public List<?> getScannerList(ScannerSearchParam param) throws DataAccessException;	
	
	// scanner Check
	public boolean checkScannerDuplication(Scanner scanner) throws DataAccessException;	
	
	// scanner transaction
	public void registerScanner(Scanner scanner) throws DataAccessException;
	public void modifyScanner(Scanner scanner) throws DataAccessException;
	public void batchScanner(Scanner scanner) throws DataAccessException;
	public void removeScanner(Scanner scanner) throws DataAccessException;	
		
}