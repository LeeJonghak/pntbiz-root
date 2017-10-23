package core.wms.scanner.dao;

import java.util.List;

import core.wms.scanner.domain.ScannerDeploy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.admin.company.domain.Company;
import core.wms.scanner.domain.ScannerGeofenceLatlng;
import framework.db.dao.BaseDao;

@Repository
public class ScannerDeployDao extends BaseDao {
	
	public List<?> getScannerDeployList(ScannerDeploy scannerDeploy) throws DataAccessException {
		return (List<?>) list("getScannerDeployList", scannerDeploy);
	}	
	
	public ScannerDeploy getScannerDeployInfo(ScannerDeploy scannerDeploy) throws DataAccessException {
		return (ScannerDeploy) select("getScannerDeployInfo", scannerDeploy);
	}	
	
	public ScannerDeploy getScannerDeployInfoByNum(ScannerDeploy scannerDeploy) throws DataAccessException {
		return (ScannerDeploy) select("getScannerDeployInfoByNum", scannerDeploy);
	}	
	
	public void insertScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		insert("insertScannerDeploy", scannerDeploy);
	}

	public void updateScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		update("updateScannerDeploy", scannerDeploy);
	}	
	
	public void deleteScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		delete("deleteScannerDeploy", scannerDeploy);
	}	
	
	public List<?> getScannerPermission() throws DataAccessException {
		return (List<?>) list("getScannerPermission");
	}
	
	public List<?> getScannerPosList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerPosList", company);
	}	
	
	public List<?> getScannerNodeList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerNodeList", company);
	}	
	
	public List<?> getScannerEdgeList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerEdgeList", company);
	}

	public List<?> getScannerEdgeList2(Company company) throws DataAccessException {
		return (List<?>) list("getScannerEdgeList2", company);
	}
	
	public List<?> getScannerBeaconPosList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerBeaconPosList", company);
	}	
	
	public List<?> getScannerBeaconNodeList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerBeaconNodeList", company);
	}	
	
	public List<?> getScannerBeaconEdgeList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerBeaconEdgeList", company);
	}
	
	public List<?> getScannerMaterialsList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerMaterialsList", company);
	}	
	
	public List<?> getScannerContentsList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerContentsList", company);
	}	
	
	public List<?> getScannerGeofenceList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerGeofenceList", company);
	}	
	
	public List<?> getScannerGeofenceLatlngList(ScannerGeofenceLatlng scannerGeofenceLatlng) throws DataAccessException {
		return (List<?>) list("getScannerGeofenceLatlngList", scannerGeofenceLatlng);
	}	
	
	public List<?> getScannerGateList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerGateList", company);
	}	
	
	public List<?> getScannerFloorList(Company company) throws DataAccessException {
		return (List<?>) list("getScannerFloorList", company);
	}	
}