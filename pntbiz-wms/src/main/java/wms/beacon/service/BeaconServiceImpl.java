package wms.beacon.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.common.beacon.dao.BeaconExternalLogDao;
import core.common.beacon.dao.BeaconRestrictedZoneLogDao;
import core.common.beacon.domain.BeaconExternalLog;
import core.common.beacon.domain.BeaconRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZoneLog;
import core.common.enums.AssignUnassignType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.common.code.domain.Code;
import core.wms.beacon.dao.BeaconDao;
import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconGroup;
import core.wms.beacon.domain.BeaconGroupMapping;
import core.wms.beacon.domain.BeaconGroupSearchParam;
import core.wms.beacon.domain.BeaconSearchParam;
import core.wms.beacon.domain.BeaconState;
import core.wms.beacon.domain.BeaconStateSearchParam;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.file.FileUtil;
import framework.web.ftp.SFTPClient;
import framework.web.util.DateUtil;
import framework.web.util.StringUtil;
import core.wms.info.dao.CodeActionDao;
import core.wms.info.domain.CodeActionMapping;
import core.wms.map.dao.MyContentsDao;
import core.wms.map.domain.ContentsMapping;

/**
 * 비콘 서비스
 */
@Service
public class BeaconServiceImpl implements BeaconService {

    @Autowired
    private BeaconDao beaconDao;

	@Autowired
	private BeaconRestrictedZoneLogDao beaconRestrictedZoneLogDao;

	@Autowired
	private BeaconExternalLogDao beaconExternalLogDao;

    @Autowired
    private CodeActionDao codeActionDao;

    @Autowired
    private MyContentsDao myContentsDao;

    @Value("#{config['contents.ftp.host']}")
	private String ftpHost;
	@Value("#{config['contents.ftp.port']}")
	private Integer ftpPort;
	@Value("#{config['contents.ftp.id']}")
	private String ftpID;
	@Value("#{config['contents.ftp.passwd']}")
	private String ftpPW;
	@Value("#{config['contents.ftp.privatekey']}")
	private String ftpPrivateKey;
	@Value("#{config['contents.root.path']}")
	private String contentsRootPath;
	@Value("#{config['beacon.image.src']}")
	private String beaconImageSrc;
	@Value("#{config['contentsURL']}")
	private String contentsURL;

    /**
     * 비콘 등록 처리
     *
     * @param beacon
     */
    @Transactional
    @Override
    public void registerBeacon(LoginDetail loginDetail, Beacon beacon) {
        beacon.setComNum(loginDetail.getCompanyNumber());

        beaconDao.insertBeacon(beacon);

		if(StringUtils.isNotBlank(beacon.getExternalId())) {
			BeaconExternalLog logVo = new BeaconExternalLog();
			logVo.setBeaconNum(beacon.getBeaconNum());
			logVo.setType(AssignUnassignType.CREATE);
			beaconExternalLogDao.insertBeaconExternalLog(logVo);
		}
    }

    @Override
    public List<?> getBeaconList(BeaconSearchParam paramVo) {
    	return beaconDao.getBeaconList(paramVo);
    }

    @Override
	public List<?> bindBeaconList(List<?> list, List<?> beaconTypeCD) throws ParseException {
		List<Beacon> blist = new ArrayList<Beacon>();

		for(int i=0; i<list.size(); i++) {
			Beacon beacon = (Beacon) list.get(i);
			for(int j=0; j<beaconTypeCD.size(); j++) {
				Code code = (Code) beaconTypeCD.get(j);
				if(beacon.getBeaconType().equals(code.getsCD())) {
					beacon.setBeaconTypeLangCode(code.getLangCode());
				}
			}
			blist.add(beacon);
		}
		return blist;
	}

    @Override
    public Integer getBeaconCount(BeaconSearchParam paramVo) {
        return beaconDao.getBeaconCount(paramVo);
    }


    @Override
    public Beacon getBeacon(Long beaconNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("beaconNum", beaconNum);
        Beacon beacon = beaconDao.getBeacon(param);

        if(!"".equals(StringUtil.NVL(beacon.getImgSrc(), ""))) {
        	String fileDate = StringUtil.getSubString(beacon.getImgSrc(), 0, 6);

            String beaconImageURL = contentsURL + "/" + beacon.getComNum() + beaconImageSrc + "/" + fileDate + "/";
            String imgUrl = beaconImageURL + beacon.getImgSrc();
            beacon.setImgUrl(imgUrl);
        }


        return beacon;

    }

    @Override
    public void modifyBeacon(Beacon vo) {

		Beacon orgBeaconInfo = getBeacon(vo.getBeaconNum());

        beaconDao.modifyBeacon(vo);

		if(!StringUtils.equals(orgBeaconInfo.getBarcode()+"|"+orgBeaconInfo.getExternalId()
				,vo.getBarcode()+"|"+vo.getExternalId())) {

			BeaconExternalLog logVo = new BeaconExternalLog();
			logVo.setBeaconNum(vo.getBeaconNum());

			if(StringUtils.isBlank(orgBeaconInfo.getExternalId()) && StringUtils.isBlank(orgBeaconInfo.getBarcode())) {
				logVo.setType(AssignUnassignType.CREATE);
			}
			else if(StringUtils.isBlank(vo.getExternalId()) && StringUtils.isBlank(vo.getBarcode())) {
				logVo.setType(AssignUnassignType.DELETE);
			} else {
				logVo.setType(AssignUnassignType.MODIFY);
			}

			beaconExternalLogDao.insertBeaconExternalLog(logVo);
		}
    }

    @Override
    public void deleteBeacon(Long beaconNum) {
		Beacon orgBeaconInfo = getBeacon(beaconNum);
		if(StringUtils.isNoneBlank(orgBeaconInfo.getExternalId())) {
			BeaconExternalLog logVo = new BeaconExternalLog();
			logVo.setBeaconNum(beaconNum);
			logVo.setType(AssignUnassignType.DELETE);
			beaconExternalLogDao.insertBeaconExternalLog(logVo);
		}

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("beaconNum", beaconNum);

        beaconDao.deleteBeacon(param);
    }

    @Override
    public void setBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer codeNum) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("refNum", beaconNum);
        param.put("refType", "BC");
        codeActionDao.deleteCodeActionMapping(param);

        if(codeNum!=null) {
            CodeActionMapping vo = new CodeActionMapping();
            vo.setCodeNum(codeNum);
            vo.setRefNum(beaconNum);
            vo.setRefType("BC");
            vo.setRefSubType("");
            codeActionDao.insertCodeActionMapping(vo);
        }
    }

    @Override
    public Integer getBeaconStateCount(BeaconStateSearchParam param)  throws Exception {
    	Integer cnt = beaconDao.getBeaconStateCount(param);
		return cnt;
    }

    @Override
    public List<?> getBeaconStateList(BeaconStateSearchParam param) throws Exception {
    	List<?> list = beaconDao.getBeaconStateList(param);
		return list;
    }

	@Override
	public List<BeaconState> getLogBeaconStateList(HashMap<String, Object> param) throws Exception {
		List<BeaconState> list = beaconDao.getLogBeaconStateList(param);
		return list;
	}

	@Override
	public List<BeaconState> getChartLogLossBeaconStateList(HashMap<String, Object> param) throws DataAccessException {
		List<BeaconState> list = beaconDao.getChartLogLossBeaconStateList(param);
		return list;
	}

	@Override
	public List<Beacon> getLogLossBeaconStateList(HashMap<String, Object> param) throws DataAccessException {
		List<Beacon> list = beaconDao.getLogLossBeaconStateList(param);
		return list;
	}

	@Override
	@Transactional
	public void modifyBeaconState(BeaconState beaconState) {
		beaconDao.modifyBeaconState(beaconState);

	}

	@SuppressWarnings("static-access")
	@Override
	public Map<String, String> uploadBeaconImage(MultipartRequest multi, Beacon beacon) throws IOException {
		String keyPath = Security.getLocalPrivateKeyPath() + "/";
		SFTPClient sftp = null;
		String imagePath = "";

		if(!"".equals(ftpPrivateKey) && ftpPrivateKey!=null) {
			sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, keyPath + ftpPrivateKey);
		} else {
			sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, "");
		}
		try {
			sftp.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String comNum = String.valueOf(beacon.getComNum());
		String rootPath = contentsRootPath+"/"+comNum;

		try {
			boolean isDirFlag = sftp.isDir(contentsRootPath, comNum);

			if(!isDirFlag){
				// 필수 디렉토리 생성
				sftp.mkdirList(rootPath);
			}
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 날짜 폴더 체크 후 없으면 생성
		try {
			imagePath = sftp.mkdirDate(rootPath + beaconImageSrc);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sftp.cd(imagePath);

		// 업로드 순서 수동처리
		FileUtil fu = new FileUtil();
		Map<String, String> fileList = new HashMap<String,String>();
		Enumeration<?> files = multi.getFileNames();
		fileList.put("imgSrc", "");
		boolean flag = false;
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			String fileName = multi.getFilesystemName(name);
			String fileName2 = "";
			if(fileName != null) {
				if("imgSrc".equals(name)) {
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc", fileName2);
					if(!("".equals(beacon.getImgSrc()) || beacon.getImgSrc() == null)) {
						try {
							String fileDate = StringUtil.getSubString(beacon.getImgSrc(), 0, 6);
							sftp.delete(rootPath + beaconImageSrc  + "/" + fileDate + "/" + beacon.getImgSrc());

						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if(flag == true) {
					fu.rename(Security.getLocalImagePath() + "/" + fileName, Security.getLocalImagePath() + "/" + fileName2);
					sftp.upload(Security.getLocalImagePath() + "/" + fileName2);
					fu.delete(fileName2);
					File file = new File(Security.getLocalImagePath() + "/" + fileName2);
					if(file.exists()) {
						file.delete();
					}
				}
			}
		}
		sftp.disconnect();
		return fileList;

	}

	@Override
	public String deleteBeaconImage(Beacon beacon) throws IOException, SftpException {
		String filename = beacon.getImgSrc();
		String fileDate = StringUtil.getSubString(filename, 0, 6);

		String status = "1";

		if(!("".equals(filename) || filename == null)) {
			try {
				String keyPath = Security.getLocalPrivateKeyPath() + "/";
				SFTPClient sftp = null;
				if(!"".equals(ftpPrivateKey) && ftpPrivateKey!=null) {
					sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, keyPath + ftpPrivateKey);
				} else {
					sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, "");
				}
				try {
					sftp.connect();
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				String path = contentsRootPath + "/" + beacon.getComNum() + beaconImageSrc + "/" + fileDate;

				sftp.cd(path);
				sftp.delete(filename);
				sftp.disconnect();
			} catch(SftpException se) {
				se.printStackTrace();
				status = "2";
			}

			updateBeaconBlankImage(beacon);

		}
		return status;
	}

	@Override
	public void updateBeaconBlankImage(Beacon beacon) throws DataAccessException {
		beacon.setImgSrc("");
		beaconDao.updateBeaconBlankImage(beacon);
	}

	@Override
	@Transactional
	public void modifyBeaconImage(MultipartRequest multi, Beacon beacon) throws DataAccessException, IOException, SftpException{

		Map<String, String> fileList = uploadBeaconImage(multi, beacon);

		beacon.setImgSrc(("".equals(fileList.get("imgSrc")) ? "" : fileList.get("imgSrc")));
		beaconDao.modifyBeacon(beacon);
	}

	@Override
	@Transactional
    public void setBeaconEvent(LoginDetail loginDetail, Long beaconNum, String refSubType, Long conNum, Integer evtNum) throws Exception {

        ContentsMapping vo = new ContentsMapping();
        vo.setConNum(conNum);
        vo.setRefSubType(refSubType);
        vo.setRefType("BC");
        vo.setRefNum(beaconNum);
        if(evtNum==null) {
            vo.setEvtNum(0);
        } else {
            vo.setEvtNum(evtNum);
        }
        this.myContentsDao.modifyContentMapping(vo);
    }

	@Override
	public Integer getBeaconGroupCount(BeaconGroupSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = beaconDao.getBeaconGroupCount(param);
		return cnt;
	}

	@Override
	public List<?> getBeaconGroupList(BeaconGroupSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = beaconDao.getBeaconGroupList(param);
		return list;
	}

	@Override
	public BeaconGroup getBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
		return beaconDao.getBeaconGroup(beaconGroup);
	}

	@Override
	@Transactional
	public void registerBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
		beaconDao.insertBeaconGroup(beaconGroup);
	}

	@Override
	@Transactional
	public void modifyBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
		beaconDao.updateBeaconGroup(beaconGroup);
	}

	@Override
	@Transactional
	public void deleteBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
		beaconDao.deleteBeaconGroup(beaconGroup);
	}

	@Override
	@Transactional
	public void registerBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
		beaconDao.insertBeaconGroupMapping(beaconGroupMapping);
	}

	@Override
	@Transactional
	public void modifyGeofencingGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
		beaconDao.updateBeaconGroupMapping(beaconGroupMapping);
	}

	@Override
	@Transactional
	public void deleteBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
		beaconDao.deleteBeaconGroupMapping(beaconGroupMapping);
	}


	@Override
	public Integer getBeaconRestrictedZoneCount(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		Integer cnt = 0;
		cnt = beaconDao.getBeaconRestrictedZoneCount(beaconRestrictedZone);
		return cnt;
	}

	@Override
	public List<BeaconRestrictedZone> getBeaconRestrictedZoneList(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		List<BeaconRestrictedZone> restrictedZoneList= null;
		restrictedZoneList = beaconDao.getBeaconRestrictedZoneList(beaconRestrictedZone);
		return restrictedZoneList;
	}

	@Override
	@Transactional
	public void registerBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		beaconDao.insertBeaconRestrictedZone(beaconRestrictedZone);

		BeaconRestrictedZoneLog logVo = new BeaconRestrictedZoneLog();
		logVo.setType(AssignUnassignType.CREATE);
		logVo.setBeaconNum(beaconRestrictedZone.getBeaconNum());
		logVo.setZoneType(beaconRestrictedZone.getZoneType());
		logVo.setZoneId(beaconRestrictedZone.getZoneId());
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(logVo);
	}

	@Override
	public BeaconRestrictedZone getBeaconRestrictedZoneInfo(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		beaconRestrictedZone = beaconDao.getBeaconRestrictedZoneInfo(beaconRestrictedZone);
		return beaconRestrictedZone;
	}

	@Override
	@Transactional
	public void modifyBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		beaconDao.updateBeaconRestrictedZone(beaconRestrictedZone);

		BeaconRestrictedZoneLog logVo = new BeaconRestrictedZoneLog();
		logVo.setType(AssignUnassignType.MODIFY);
		logVo.setBeaconNum(beaconRestrictedZone.getBeaconNum());
		logVo.setZoneType(beaconRestrictedZone.getZoneType());
		logVo.setZoneId(beaconRestrictedZone.getZoneId());
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(logVo);
	}

	@Override
	@Transactional
	public void deleteBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		BeaconRestrictedZoneLog logVo = new BeaconRestrictedZoneLog();
		logVo.setType(AssignUnassignType.DELETE);
		logVo.setBeaconNum(beaconRestrictedZone.getBeaconNum());
		logVo.setZoneType(beaconRestrictedZone.getZoneType());
		logVo.setZoneId(beaconRestrictedZone.getZoneId());
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(logVo);

		beaconDao.deleteBeaconRestrictedZone(beaconRestrictedZone);
	}

	@Override
	@Transactional
	public void modifyAllBeaonRestrictedZoneForPermitted(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		beaconDao.updateAllBeaconRestrictedZoneForPermitted(beaconRestrictedZone);

		BeaconRestrictedZoneLog logVo = new BeaconRestrictedZoneLog();
		logVo.setType(AssignUnassignType.MODIFY);
		logVo.setBeaconNum(beaconRestrictedZone.getBeaconNum());
		logVo.setZoneType(beaconRestrictedZone.getZoneType());
		logVo.setZoneId(beaconRestrictedZone.getZoneId());
		beaconRestrictedZoneLogDao.insertBeaconRestrictedZoneLog(logVo);
	}

	/*
	 * 사용안함
	 *
    @Override
    public List<?> getBeaconAll(LoginDetail loginDetail, String floor) {
        HashMap<String, PresenceRequestParam> param = new HashMap<String, PresenceRequestParam>();
        param.put("floor", floor);
        param.put("comNum", loginDetail.getCompanyNumber());
        return beaconDao.getBeaconList(param);
    }

    @Override
    public List<?> getBeaconAll(HashMap<String, PresenceRequestParam> param) {
        return beaconDao.getBeaconList(param);
    }
    */
}
