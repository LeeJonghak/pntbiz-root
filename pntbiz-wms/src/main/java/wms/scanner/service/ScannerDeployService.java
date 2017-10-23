package wms.scanner.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.admin.company.domain.Company;

import com.jcraft.jsch.SftpException;

import core.wms.scanner.domain.ScannerDeploy;
import core.wms.scanner.domain.ScannerServer;

public interface ScannerDeployService  {
	
	// scannerDeployr info
	public ScannerDeploy getScannerDeployInfo(ScannerDeploy scannerDeploy) throws DataAccessException;
	public ScannerDeploy getScannerDeployInfoByNum(ScannerDeploy scannerDeploy) throws DataAccessException;
	public List<?> getScannerDeployList(ScannerDeploy scannerDeploy) throws DataAccessException;	
	
	// scannerDeploy transaction
	public void registerScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException;
	public void modifyScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException;
	public void removeScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException;	
	
	// scanner permission
	public List<?> getScannerPermission() throws DataAccessException;
	
	// deploy
	public boolean deployFile(ScannerServer scannerServer, ScannerDeploy scannerDeploy) throws SftpException, IOException;
	public boolean deployFileLocal(ScannerServer scannerServer, ScannerDeploy scannerDeploy) throws SftpException, IOException;
	
	// scanner
	public StringBuffer getScannerPosList(Company company, String depFormat) throws DataAccessException;
	public StringBuffer getScannerNodeList(Company company, String depFormat) throws DataAccessException;

	public StringBuffer getScannerNodeList2(Company company, String depFormat) throws DataAccessException;
	
	// beacon
	public StringBuffer getScannerBeaconPosList(Company company, String depFormat) throws DataAccessException;
	public StringBuffer getScannerBeaconNodeList(Company company, String depFormat) throws DataAccessException;
	
	// materials
	public StringBuffer getScannerMaterialsList(Company company, String depFormat) throws DataAccessException;
	
	// contents
	public StringBuffer getScannerContentsList(Company company, String depFormat) throws DataAccessException;
	
	// geofence
	public StringBuffer getScannerGeofenceList(Company company, String depFormat) throws DataAccessException;
	
	// gate
	public StringBuffer getScannerGateList(Company company, String depFormat) throws DataAccessException;	
	
	// floor
	public StringBuffer getScannerFloorList(Company company, String depFormat) throws DataAccessException;	
	
}
