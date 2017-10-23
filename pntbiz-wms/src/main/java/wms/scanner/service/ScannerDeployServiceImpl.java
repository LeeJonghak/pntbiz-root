package wms.scanner.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import framework.Security;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.admin.company.domain.Company;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import framework.web.file.FileUtil;
import framework.web.ftp.SFTPClient;
import framework.web.util.StringUtil;
import core.wms.scanner.dao.ScannerDeployDao;
import core.wms.scanner.domain.ScannerBeaconEdge;
import core.wms.scanner.domain.ScannerBeaconNode;
import core.wms.scanner.domain.ScannerBeaconPos;
import core.wms.scanner.domain.ScannerContents;
import core.wms.scanner.domain.ScannerDeploy;
import core.wms.scanner.domain.ScannerEdge;
import core.wms.scanner.domain.ScannerFloor;
import core.wms.scanner.domain.ScannerGeofence;
import core.wms.scanner.domain.ScannerGeofenceLatlng;
import core.wms.scanner.domain.ScannerMaterials;
import core.wms.scanner.domain.ScannerNode;
import core.wms.scanner.domain.ScannerPos;
import core.wms.scanner.domain.ScannerServer;

@Service
public class ScannerDeployServiceImpl implements ScannerDeployService {

	@Autowired
	private ScannerDeployDao scannerDeployDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("#{config['contentsURL']}")
	private String contentsURL;
	@Value("#{config['contents.image.path']}")
	private String contentsImagePath;
	@Value("#{config['contents.image.src']}")
	private String contentsImageSrc;	
	@Value("#{config['contents.sound.path']}")
	private String contentsSoundPath;
	@Value("#{config['contents.sound.src']}")
	private String contentsSoundSrc;	
	@Value("#{config['floor.image.path']}")
	private String floorImagePath;
	@Value("#{config['floor.image.src']}")
	private String floorImageSrc;

	@Override
	public ScannerDeploy getScannerDeployInfo(ScannerDeploy scannerDeploy) throws DataAccessException {		
		ScannerDeploy scannerDeployInfo = null;
		scannerDeployInfo = scannerDeployDao.getScannerDeployInfo(scannerDeploy);
		logger.info("getScannerDeployInfo {}", scannerDeployInfo);
		return scannerDeployInfo;
	}
	
	@Override
	public ScannerDeploy getScannerDeployInfoByNum(ScannerDeploy scannerDeploy) throws DataAccessException {
		ScannerDeploy scannerDeployInfo = null;
		scannerDeployInfo = scannerDeployDao.getScannerDeployInfoByNum(scannerDeploy);
		logger.info("getScannerDeployInfo {}", scannerDeployInfo);
		return scannerDeployInfo;
	}
	
	@Override
	public List<?> getScannerDeployList(ScannerDeploy scannerDeploy) throws DataAccessException {
		List<?> scannerDeployList = null;
		scannerDeployList = scannerDeployDao.getScannerDeployList(scannerDeploy);		
		logger.info("getScannerDeployList {}", scannerDeployList.size());		
		return scannerDeployList;
	}	
	
	@Override
	@Transactional
	public void registerScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		scannerDeployDao.insertScannerDeploy(scannerDeploy);		
	}
	
	@Override
	@Transactional
	public void modifyScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		scannerDeployDao.updateScannerDeploy(scannerDeploy);		
	}
	
	@Transactional
	public void removeScannerDeploy(ScannerDeploy scannerDeploy) throws DataAccessException {
		scannerDeployDao.deleteScannerDeploy(scannerDeploy);		
	}
	
	@Override
	public List<?> getScannerPermission() throws DataAccessException {		
		List<?> list = null;
		list = scannerDeployDao.getScannerPermission();		
		logger.info("getScannerPermission {}", list.size());		
		return list;
	}	
		
	@Override
	public boolean deployFile(ScannerServer scannerServer, ScannerDeploy scannerDeploy) throws SftpException, IOException {
		
		String keyPath = Security.getLocalPrivateKeyPath() + "/";
		SFTPClient sftp = null;
		if(!"".equals(scannerServer.getFtpPrivateKey()) && scannerServer.getFtpPrivateKey()!=null) {
			sftp = new SFTPClient(scannerServer.getFtpHost(),
					scannerServer.getFtpPort(), scannerServer.getFtpID(), scannerServer.getFtpPW(), keyPath + scannerServer.getFtpPrivateKey());
		} else {
			sftp = new SFTPClient(scannerServer.getFtpHost(),
					scannerServer.getFtpPort(), scannerServer.getFtpID(), scannerServer.getFtpPW(), "");
		}		
//		SFTPClient sftp = new SFTPClient(scannerServer.getFtpHost(), 
//				scannerServer.getFtpPort(), scannerServer.getFtpID(), scannerServer.getFtpPW(), "");
		try {
			StringBuffer data = scannerDeploy.getDepJson();
			String filePath = Security.getLocalImagePath() + "/" +
						FileUtil.getFileName(scannerDeploy.getDepFile()) + "_" + 
						scannerServer.getComNum() + FileUtil.getFileExt(scannerDeploy.getDepFile());
			BufferedWriter js = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
			js.write(data.toString());
			js.close();			
			sftp.connect();
			sftp.cd(scannerDeploy.getDepPath());
			sftp.upload(filePath);
			sftp.disconnect();			
			File file = new File(filePath);
			if(file.exists()) {
				file.delete();
			}			
			return true;
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deployFileLocal(ScannerServer scannerServer, ScannerDeploy scannerDeploy) throws SftpException, IOException {


		StringBuffer data = scannerDeploy.getDepJson();
		String filePath = Security.getLocalImagePath() + "/" +
				FileUtil.getFileName(scannerDeploy.getDepFile()) + "_" +
				scannerServer.getComNum() + FileUtil.getFileExt(scannerDeploy.getDepFile());
		BufferedWriter js = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
		js.write(data.toString());
		js.close();


		File orgFile = new File(filePath);
		File desFile = new File(scannerDeploy.getDepPath() + "/" + orgFile.getName());
		File desDirectory = new File(scannerDeploy.getDepPath());
		if(!desDirectory.exists()) {
			desDirectory.mkdirs();
		}
		FileUtils.copyFile(orgFile, desFile);

		if(orgFile.exists()) {
			orgFile.delete();
		}

		return true;
	}
	
	@Override
	public StringBuffer getScannerPosList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		list = scannerDeployDao.getScannerPosList(company);
		int cnt = 0;
		cnt = list.size();	
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var scannerPos = {\n");
		}
		for(int i=0; i<cnt; i++) {
			ScannerPos row = (ScannerPos) list.get(i);
			StringBuffer majorData = new StringBuffer(5000);
			String[] majorList = new String(row.getMajorVer()).split(",");
			int mcnt = 0;
			for(String major : majorList) {
				if(mcnt > 0) { majorData.append(", "); }
				majorData.append("\"major" + mcnt + "\": " + major);				
				mcnt++;
			}
			data.append("\"" + StringUtil.NVL(row.getMacAddr(), "11:22:33:44:55:66") + "\": ");
			data.append("{");				
			data.append("\"majorVer\": {" + majorData + "}");
			data.append(", ");
			data.append("\"scannerName\": \"" + StringUtil.NVL(row.getScannerName(), "") + "\"");
			data.append(", ");
			data.append("\"sid\": \"" + StringUtil.NVL(row.getSid(), "") + "\"");
			data.append(", ");
			data.append("\"x\": " + StringUtil.NVL(Double.toString(row.getLat()), "0"));
			data.append(", ");
			data.append("\"y\": " + StringUtil.NVL(Double.toString(row.getLng()), "0"));
			data.append(", ");
			data.append("\"rssi\": " + StringUtil.NVL(Float.toString(row.getRssi()), "0"));
			data.append(", ");
			data.append("\"srssi\": " + StringUtil.NVL(Float.toString(row.getSrssi()), "0"));
			data.append(", ");
			data.append("\"mrssi\": " + StringUtil.NVL(Float.toString(row.getMrssi()), "0"));
			data.append(", ");
			data.append("\"drssi\": " + StringUtil.NVL(Float.toString(row.getDrssi()), "0"));
			data.append(", ");
			data.append("\"exMeter\": " + StringUtil.NVL(Float.toString(row.getExMeter()), "0"));
			data.append(", ");
			data.append("\"calPoint\": " + StringUtil.NVL(Float.toString(row.getCalPoint()), "0"));
			data.append(", ");
			data.append("\"maxSig\": " + StringUtil.NVL(Float.toString(row.getMaxSig()), "0"));
			data.append(", ");
			data.append("\"maxBuf\": " + StringUtil.NVL(Float.toString(row.getMaxBuf()), "0"));
			data.append(", ");
			data.append("\"fwVer\": \"" + StringUtil.NVL(row.getFwVer(), "0") + "\"");
			data.append(", ");
			data.append("\"floor\": \"" + StringUtil.NVL(row.getFloor(), "") + "\"");
			data.append(", ");
			data.append("\"UUID\": \"" + StringUtil.NVL(row.getUUID(), "") + "\"");
			data.append("}");
			if(i < cnt-1) data.append(",");
			data.append("\n");
		}
		data.append("}");
		return data;
	}

	/**
	 * 제거 예정
	 * getScannerNodeList2 로 변경
	 */
	@Override
	@Deprecated
	public StringBuffer getScannerNodeList(Company company, String depFormat) throws DataAccessException {		
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		int cnt = 0;
		
		list = scannerDeployDao.getScannerNodeList(company);	
		cnt = list.size();
		
		// 스캐너 노드
		if(depFormat.equals("json")) {
			data.append("{\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : {\n");
			data.append("\"node\": [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerNode node = (ScannerNode) list.get(j);
				data.append("{");	
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("],");
			data.append("\n");	
		} else {
			data.append("var scannerNode = {\n");			
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerNode node = (ScannerNode) list.get(j);
				data.append("{");	
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");		
			data.append("}");	
			data.append("\n\n");	
		}
		
		list = scannerDeployDao.getScannerEdgeList(company);	
		cnt = list.size();
		
		// 스캐너 엣지
		if(depFormat.equals("json")) {
			data.append("\"edge\": [\n");
			for(int j=0; j< cnt; j++)
			{
				ScannerEdge edge = (ScannerEdge) list.get(j);
				data.append("{");	
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEdgeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getComNum(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}	
			data.append("]");	
			data.append("}\n}");
		} else {
			data.append("var scannerEdge = {\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
			for(int j=0; j< cnt; j++)
			{
				ScannerEdge edge = (ScannerEdge) list.get(j);
				data.append("{");	
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}	
			data.append("]");	
			data.append("}");
		}
		return data;
	}

	@Override
	public StringBuffer getScannerNodeList2(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);
		List<?> list = null;
		int cnt = 0;

		list = scannerDeployDao.getScannerNodeList(company);
		cnt = list.size();

		// 스캐너 노드
		if(depFormat.equals("json")) {
			data.append("{\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : {\n");
			data.append("\"node\": [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerNode node = (ScannerNode) list.get(j);
				data.append("{");
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("],");
			data.append("\n");
		} else {
			data.append("var scannerNode = {\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerNode node = (ScannerNode) list.get(j);
				data.append("{");
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");
			data.append("}");
			data.append("\n\n");
		}

		list = scannerDeployDao.getScannerEdgeList2(company);
		cnt = list.size();

		// 스캐너 엣지
		if(depFormat.equals("json")) {
			data.append("\"edge\": [\n");
			for(int j=0; j< cnt; j++)
			{
				ScannerEdge edge = (ScannerEdge) list.get(j);
				data.append("{");
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEdgeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getComNum(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");
			data.append("}\n}");
		} else {
			data.append("var scannerEdge = {\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
			for(int j=0; j< cnt; j++)
			{
				ScannerEdge edge = (ScannerEdge) list.get(j);
				data.append("{");
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");
			data.append("}");
		}
		return data;
	}

	@Override
	public StringBuffer getScannerBeaconPosList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		list = scannerDeployDao.getScannerBeaconPosList(company);
		int cnt = 0;
		cnt = list.size();	
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var beaconPos = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");	
		for(int i=0; i<cnt; i++) {
			ScannerBeaconPos row = (ScannerBeaconPos) list.get(i);			
			data.append("{");		
			data.append("\"UUID\": \"" + StringUtil.NVL(row.getUUID(), "") + "\"");
			data.append(", ");
			data.append("\"majorVer\": " + StringUtil.NVL(Integer.toString(row.getMajorVer()), "0"));
			data.append(", ");
			data.append("\"minorVer\": " + StringUtil.NVL(Integer.toString(row.getMinorVer()), "0"));
			data.append(", ");
			data.append("\"macAddr\": \"" + StringUtil.NVL(row.getMacAddr(), "") + "\"");
			data.append(", ");
			data.append("\"txPower\": " + StringUtil.NVL(Float.toString(row.getTxPower()), "0"));
			data.append(", ");
			data.append("\"battery\": " + StringUtil.NVL(Integer.toString(row.getBattery()), "0"));
			data.append(", ");
			data.append("\"beaconName\": \"" + StringUtil.NVL(row.getBeaconName(), "") + "\"");
			data.append(", ");
			data.append("\"floor\": \"" + StringUtil.NVL(row.getFloor(), "") + "\"");
			data.append(", ");
			data.append("\"lat\": " + StringUtil.NVL(Double.toString(row.getLat()), "0"));
			data.append(", ");
			data.append("\"lng\": " + StringUtil.NVL(Double.toString(row.getLng()), "0"));
			data.append("}");
			if(i < cnt-1) data.append(",");
			data.append("\n");
		}
		data.append("]");
		data.append("}");
		return data;
	}	
	
	@Override
	public StringBuffer getScannerBeaconNodeList(Company company, String depFormat) throws DataAccessException {		
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		int cnt = 0;
		
		list = scannerDeployDao.getScannerBeaconNodeList(company);	
		cnt = list.size();
		
		// 비콘 노드
		if(depFormat.equals("json")) {
			data.append("{\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : {\n");
			data.append("\"node\": [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerBeaconNode node = (ScannerBeaconNode) list.get(j);
				data.append("{");	
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("],");
			data.append("\n");	
		} else {
			data.append("var scannerNode = {\n");			
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
			for(int j=0; j<cnt; j++)
			{
				ScannerBeaconNode node = (ScannerBeaconNode) list.get(j);
				data.append("{");	
				data.append("\"nodeNum\": " + StringUtil.NVL(node.getNodeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(node.getComNum(), 0) + ", ");
				data.append("\"nodeID\": " + StringUtil.NVL(node.getNodeID(), 0) + ", ");
				data.append("\"nodeName\": \"" + StringUtil.NVL(node.getNodeName(), "") + "\", ");
				data.append("\"floor\": \"" +StringUtil.NVL(node.getFloor(), "") + "\", ");
				data.append("\"lat\": " + StringUtil.NVL(node.getLat(), 0.00) + ", ");
				data.append("\"lng\": " + StringUtil.NVL(node.getLng(), 0.00));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");		
			data.append("}");	
			data.append("\n\n");	
		}
		
		list = scannerDeployDao.getScannerBeaconEdgeList(company);	
		cnt = list.size();
		
		// 비콘 엣지
		if(depFormat.equals("json")) {
			data.append("\"edge\": [\n");
			for(int j=0; j< cnt; j++)
			{
				ScannerBeaconEdge edge = (ScannerBeaconEdge) list.get(j);
				data.append("{");	
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEdgeNum(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getComNum(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}	
			data.append("]");	
			data.append("}\n}");
		} else {
			data.append("var scannerEdge = {\n");
			data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");				
			for(int j=0; j< cnt; j++)
			{
				ScannerBeaconEdge edge = (ScannerBeaconEdge) list.get(j);
				data.append("{");	
				data.append("\"edgeNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"comNum\": " + StringUtil.NVL(edge.getEndPoint(), 0) + ", ");
				data.append("\"floor\": \"" + StringUtil.NVL(edge.getFloor(), "") + "\", ");
				data.append("\"startPoint\": " + StringUtil.NVL(edge.getStartPoint(), 0) + ", ");
				data.append("\"endPoint\": " + StringUtil.NVL(edge.getEndPoint(), 0));
				data.append("}");
				if(j < cnt-1) data.append(",");
				data.append("\n");
			}	
			data.append("]");	
			data.append("}");
		}
		return data;
	}	
	
	@Override
	public StringBuffer getScannerMaterialsList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		int cnt = 0;
		list = scannerDeployDao.getScannerMaterialsList(company);	
		cnt = list.size();
		
		// 자재정보
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var materialsList = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : {\n");
		for(int j=0; j<cnt; j++)
		{	
			ScannerMaterials materials = (ScannerMaterials) list.get(j);
			data.append("\"" + StringUtil.NVL(materials.getUUID(), "") + "_" + 
			StringUtil.NVL(materials.getMajorVer(), 0) + "_" + 
			StringUtil.NVL(materials.getMinorVer(), 0) + "\": {");	
			data.append("\"UUID\": \"" + StringUtil.NVL(materials.getUUID(), "") + "\", ");
			data.append("\"majorVer\": " + StringUtil.NVL(materials.getMajorVer(), 0) + ", ");
			data.append("\"minorVer\": " + StringUtil.NVL(materials.getMinorVer(), 0) + ", ");
			data.append("\"beaconName\": \"" + StringUtil.NVL(materials.getBeaconName(), "") + "\", ");
			data.append("\"conName\": \"" +StringUtil.NVL(materials.getConName(), "") + "\"");
			data.append("}");
			if(j < cnt-1) data.append(",");
			data.append("\n");
		}
		data.append("}");			
		data.append("}");	
		data.append("\n\n");			
		return data;		
	}	
	
	@Override
	public StringBuffer getScannerContentsList(Company company, String depFormat) 	throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		int cnt = 0;
		list = scannerDeployDao.getScannerContentsList(company);	
		cnt = list.size();		

		String contentsImageURL = "";
		String contentsSoundURL = "";
		
		// 콘텐츠 정보
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var contentsList = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : {\n");
		for(int j=0; j<cnt; j++)
		{	
			ScannerContents contents = (ScannerContents) list.get(j);
			
			contentsImageURL = contentsURL + "/" + company.getComNum() + contentsImageSrc + "/";
			contentsSoundURL = contentsURL + "/" + company.getComNum() + contentsSoundSrc + "/";
			
			contents.setImgURL1(("".equals(StringUtil.NVL(contents.getImgSrc1(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc1(), 0, 6) + "/" + contents.getImgSrc1());
			contents.setImgURL2(("".equals(StringUtil.NVL(contents.getImgSrc2(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc2(), 0, 6) + "/" + contents.getImgSrc2());
			contents.setImgURL3(("".equals(StringUtil.NVL(contents.getImgSrc3(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc3(), 0, 6) + "/" + contents.getImgSrc3());
			contents.setImgURL4(("".equals(StringUtil.NVL(contents.getImgSrc4(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc4(), 0, 6) + "/" + contents.getImgSrc4());
			contents.setImgURL5(("".equals(StringUtil.NVL(contents.getImgSrc5(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc5(), 0, 6) + "/" + contents.getImgSrc5());			
			contents.setSoundURL1(("".equals(StringUtil.NVL(contents.getSoundURL1(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundURL1(), 0, 6) + "/" + contents.getSoundURL1());
			contents.setSoundURL2(("".equals(StringUtil.NVL(contents.getSoundURL2(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundURL2(), 0, 6) + "/" + contents.getSoundURL2());
			contents.setSoundURL3(("".equals(StringUtil.NVL(contents.getSoundURL3(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundURL3(), 0, 6) + "/" + contents.getSoundURL3());			
			data.append("\"" + StringUtil.NVL(contents.getUUID(), "") + "_" + 
			StringUtil.NVL(contents.getMajorVer(), 0) + "_" + 
			StringUtil.NVL(contents.getMinorVer(), 0) + "\": {");	
			data.append("\"UUID\": \"" + StringUtil.NVL(contents.getUUID(), "") + "\", ");
			data.append("\"majorVer\": " + StringUtil.NVL(contents.getMajorVer(), 0) + ", ");
			data.append("\"minorVer\": " + StringUtil.NVL(contents.getMinorVer(), 0) + ", ");
			data.append("\"beaconName\": \"" + StringUtil.NVL(contents.getBeaconName(), "") + "\", ");
			data.append("\"conType\": \"" +StringUtil.NVL(contents.getConType(), "") + "\", ");
			data.append("\"conName\": \"" +StringUtil.NVL(contents.getConName(), "") + "\", ");			
			data.append("\"refType\": \"" +StringUtil.NVL(contents.getRefType(), "") + "\", ");
			data.append("\"refSubType\": \"" +StringUtil.NVL(contents.getRefSubType(), "") + "\", "); 
			data.append("\"imgSrc1\": \"" +StringUtil.NVL(contents.getImgSrc1(), "") + "\", ");
			data.append("\"imgSrc2\": \"" +StringUtil.NVL(contents.getImgSrc2(), "") + "\", ");
			data.append("\"imgSrc3\": \"" +StringUtil.NVL(contents.getImgSrc3(), "") + "\", ");
			data.append("\"imgSrc4\": \"" +StringUtil.NVL(contents.getImgSrc4(), "") + "\", ");
			data.append("\"imgSrc5\": \"" +StringUtil.NVL(contents.getImgSrc5(), "") + "\", ");			
			data.append("\"imgURL1\": \"" + contents.getImgURL1() + "\", ");
			data.append("\"imgURL2\": \"" + contents.getImgURL2() + "\", ");
			data.append("\"imgURL3\": \"" + contents.getImgURL3() + "\", ");
			data.append("\"imgURL4\": \"" + contents.getImgURL4() + "\", ");
			data.append("\"imgURL5\": \"" + contents.getImgURL5() + "\", ");			
			data.append("\"soundURL1\": \"" + contents.getSoundURL1() + "\", ");
			data.append("\"soundURL2\": \"" + contents.getSoundURL2() + "\", ");
			data.append("\"soundURL3\": \"" + contents.getSoundURL3() + "\", ");
			data.append("\"text1\": \"" +StringUtil.NVL(contents.getText1(), "") + "\", ");
			data.append("\"text2\": \"" +StringUtil.NVL(contents.getText2(), "") + "\", ");
			data.append("\"text3\": \"" +StringUtil.NVL(contents.getText3(), "") + "\", ");
			data.append("\"text4\": \"" +StringUtil.NVL(contents.getText4(), "") + "\", ");
			data.append("\"text5\": \"" +StringUtil.NVL(contents.getText5(), "") + "\", ");
			data.append("\"soundSrc1\": \"" +StringUtil.NVL(contents.getSoundSrc1(), "") + "\", ");
			data.append("\"soundSrc2\": \"" +StringUtil.NVL(contents.getSoundSrc2(), "") + "\", ");
			data.append("\"soundSrc3\": \"" +StringUtil.NVL(contents.getSoundSrc3(), "") + "\", ");
			data.append("\"url1\": \"" +StringUtil.NVL(contents.getUrl1(), "") + "\", ");
			data.append("\"url2\": \"" +StringUtil.NVL(contents.getUrl2(), "") + "\", ");
			data.append("\"url3\": \"" +StringUtil.NVL(contents.getUrl3(), "") + "\"");
			data.append("}");
			if(j < cnt-1) data.append(",");
			data.append("\n");	
		}
		data.append("}");			
		data.append("}");	
		data.append("\n\n");			
		return data;		
	}
	
	@Override
	public StringBuffer getScannerGeofenceList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		List<?> glist = null;
		int cnt = 0;
		ScannerGeofenceLatlng gfLatlng = new ScannerGeofenceLatlng();
		
		list = scannerDeployDao.getScannerGeofenceList(company);
		cnt = list.size();
		
		// 지오펜스정보
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var geofenceList = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
		
		for(int j=0; j<cnt; j++)
		{	
			ScannerGeofence row = (ScannerGeofence) list.get(j);
			data.append("{");	
			data.append("\"fcNum\": " + StringUtil.NVL(row.getFcNum(), 0) + ", ");
			data.append("\"fcType\": \"" + StringUtil.NVL(row.getFcType(), "0") + "\", ");
			data.append("\"fcShape\": \"" + StringUtil.NVL(row.getFcShape(), "0") + "\", ");
			data.append("\"fcName\": \"" + StringUtil.NVL(row.getFcName(), "") + "\", ");
			data.append("\"floor\": \"" + StringUtil.NVL(row.getFloor(), "") + "\", ");
			data.append("\"evtEnter\": " + StringUtil.NVL(row.getEvtEnter(), 0) + ", ");
			data.append("\"evtLeave\": " + StringUtil.NVL(row.getEvtLeave(), 0) + ", ");
			data.append("\"evtStay\": " + StringUtil.NVL(row.getEvtStay(), 0) + ", ");
			data.append("\"numEnter\": " + StringUtil.NVL(row.getNumEnter(), 0) + ", ");
			data.append("\"numLeave\": " + StringUtil.NVL(row.getNumLeave(), 0) + ", ");
			data.append("\"numStay\": " + StringUtil.NVL(row.getNumStay(), 0) + ", ");			
			data.append("\"field1\": \"" + StringUtil.NVL(row.getField1(), "") + "\", ");
			data.append("\"field2\": \"" + StringUtil.NVL(row.getField2(), "") + "\", ");
			data.append("\"field3\": \"" + StringUtil.NVL(row.getField3(), "") + "\", ");
			data.append("\"isNodeEnable\": \"" + StringUtil.NVL(row.getIsNodeEnable(), "") + "\", ");
			gfLatlng.setFcNum(row.getFcNum());					
			glist = scannerDeployDao.getScannerGeofenceLatlngList(gfLatlng);					
			data.append("\"latlng\": [");		
			int gcnt = glist.size();
			for(int k=0; k<glist.size(); k++) {						
				ScannerGeofenceLatlng latlng = (ScannerGeofenceLatlng) glist.get(k);
				data.append("{");	
				data.append("\"lat\": " + StringUtil.NVL(Double.toString(latlng.getLat()), "0") + ", ");
				data.append("\"lng\": " + StringUtil.NVL(Double.toString(latlng.getLng()), "0") + ", ");
//						data.append("orderSeq: " + StringUtil.NVL(latlng.getOrderSeq()), "0") + ", ");
				data.append("\"orderSeq\": " + k + ", ");
				data.append("\"radius\": " + StringUtil.NVL(latlng.getRadius(), 0) + "");
				data.append("}");						
				if(k < gcnt-1) data.append(",");
				data.append("\n");
			}
			data.append("]");
			data.append("}");
			if(j < cnt-1) data.append(",");
			data.append("\n");
		}
		data.append("]");			
		data.append("}");	
		data.append("\n\n");
		return data;
	}	
	
	@Override
	public StringBuffer getScannerGateList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);
//		List<?> list = null;
//		list = scannerDeployDao.getScannerGateList(company);		
//		int cnt = list.size();		
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var gateList = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
		data.append("]");
		data.append("}");
		data.append("\n\n");		
		return data;
	}
	
	@Override
	public StringBuffer getScannerFloorList(Company company, String depFormat) throws DataAccessException {
		StringBuffer data = new StringBuffer(50000);		
		List<?> list = null;
		int cnt = 0;		
		list = scannerDeployDao.getScannerFloorList(company);
		cnt = list.size();		
		String contentsFloorURL = contentsURL + "/" + company.getComNum() + floorImageSrc + "/";		
		// 층 정보
		if(depFormat.equals("json")) {
			data.append("{\n");
		} else {
			data.append("var floorList = {\n");
		}
		data.append("\"" + StringUtil.NVL(company.getUUID(), "") + "\" : [\n");
		for(int j=0; j<cnt; j++)
		{	
			ScannerFloor row = (ScannerFloor) list.get(j);			
			row.setImgURL(("".equals(StringUtil.NVL(row.getImgSrc(), ""))) ? "" : contentsFloorURL + row.getImgSrc());		
			data.append("{");	
			data.append("\"floorNum\": " + StringUtil.NVL(row.getFloorNum(), 0) + ", ");
			data.append("\"floor\": \"" + StringUtil.NVL(row.getFloor(), "0") + "\", ");
			data.append("\"floorName\": \"" + StringUtil.NVL(row.getFloorName(), "0") + "\", ");			
			data.append("\"swLat\": " + StringUtil.NVL(Double.toString(row.getSwLat()), "0") + ", ");
			data.append("\"swLng\": " + StringUtil.NVL(Double.toString(row.getSwLng()), "0") + ", ");
			data.append("\"neLat\": " + StringUtil.NVL(Double.toString(row.getNeLat()), "0") + ", ");
			data.append("\"neLng\": " + StringUtil.NVL(Double.toString(row.getNeLng()), "0") + ", ");			
			data.append("\"deg\": " + StringUtil.NVL(Float.toString(row.getDeg()), "0") + ", ");
			data.append("\"imgSrc\": \"" + StringUtil.NVL(row.getImgSrc(), "") + "\", ");			
			data.append("\"imgURL\": \"" + StringUtil.NVL(row.getImgURL(), "") + "\"");
			data.append("}");	
			if(j < cnt-1) data.append(",");
			data.append("\n");
		}
		data.append("]");	
		data.append("}");	
		data.append("\n\n");
		return data;
	}	

}
