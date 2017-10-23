package core.api.scanner.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.api.scanner.domain.Scanner;
import framework.db.dao.BaseDao;

@Repository
public class ScannerDao extends BaseDao {
	
	public List<?> getScannerList(Scanner scanner) throws DataAccessException {
		return (List<?>) list("getScannerList", scanner);
	}	
	
}