package api.scanner.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.api.scanner.domain.Scanner;

public interface ScannerService {
	
	public List<?> getScannerList(Scanner scanner) throws DataAccessException;	
		
}