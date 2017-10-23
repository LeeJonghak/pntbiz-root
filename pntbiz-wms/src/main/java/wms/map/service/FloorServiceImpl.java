package wms.map.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.wms.map.dao.FloorCodeDao;
import core.wms.map.domain.FloorCode;
import framework.Security;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.wms.admin.company.domain.Company;
import framework.web.file.FileUtil;
import framework.web.ftp.SFTPClient;
import framework.web.util.DateUtil;
import framework.web.util.StringUtil;
import core.wms.map.dao.FloorDao;
import core.wms.map.domain.Floor;

@Service
public class FloorServiceImpl implements FloorService {

	@Autowired
	private FloorDao floorDao;
	@Autowired
	private FloorCodeDao floorCodeDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("#{config['contentsURL']}")
	private String contentsURL;
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
	@Value("#{config['floor.image.path']}")
	private String floorImagePath;
	@Value("#{config['floor.image.src']}")
	private String floorImageSrc;
	@Value("#{config['contents.root.path']}")
	private String contentsRootPath;

	@Override
	public Integer getFloorCount(Floor floor) throws DataAccessException {
		Integer cnt = 0;
		cnt = floorDao.getFloorCount(floor);
		logger.info("getFloorCount {}", cnt);
		return cnt;
	}

	@Override
	public boolean checkFloorDuplication(Floor floor) throws DataAccessException {
		Integer cnt = 0;
		cnt = floorDao.checkFloorDuplication(floor);
		logger.info("res {}", cnt);
		if(cnt > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Floor getFloorInfo(Floor floor) throws DataAccessException {
		Floor floorInfo = null;
		floorInfo = floorDao.getFloorInfo(floor);
		logger.info("getFloorInfo {}", floorInfo);
		return floorInfo;
	}

	@SuppressWarnings("static-access")
	@Override
	public Map<String, String> uploadFloorImage(MultipartRequest multi, Floor floor) throws IOException {
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

		// 2016.07.27, duwlsh
		// 폴더 경로변경
		String comNum = String.valueOf(floor.getComNum());
		String rootPath = contentsRootPath+"/"+comNum;

		try {
			boolean isDirFlag = sftp.isDir(contentsRootPath+"/", comNum);

			if(!isDirFlag){
				// 필수 디렉토리 생성
				sftp.mkdirList(rootPath);
			}
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getCause());
			e.printStackTrace();
		}

		//sftp.cd(floorImagePath);
		sftp.cd(rootPath + floorImageSrc);		// '/data/contents' 이동 후 폴더 생성여부 확인

		// 2016.07.27, END


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
					if(!("".equals(floor.getImgSrc()) || floor.getImgSrc() == null)) {
						try {
							//sftp.delete(floorImagePath + "/" + floor.getImgSrc());
							// 2016.07.27, duwlsh
							// 폴더 경로변경
							sftp.delete(rootPath + floorImageSrc + "/" + floor.getImgSrc());

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

	@SuppressWarnings("static-access")
	@Override
	public Map<String, String> uploadFloorImageLocal(MultipartRequest multi, Floor floor) throws IOException {


		// 폴더 경로변경
		String comNum = String.valueOf(floor.getComNum());
		String rootPath = contentsRootPath+"/"+comNum;


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
					if(!("".equals(floor.getImgSrc()) || floor.getImgSrc() == null)) {
						//try {
							//sftp.delete(floorImagePath + "/" + floor.getImgSrc());
							// 2016.07.27, duwlsh
							// 폴더 경로변경
							//sftp.delete(rootPath + floorImageSrc + "/" + floor.getImgSrc());
							File dfile = new File(rootPath + floorImageSrc + "/" + floor.getImgSrc());
							if(dfile.exists()) {
								dfile.delete();
							}

						//} catch(SftpException se) {
							//se.printStackTrace();
						//}
					}
				}
				if(flag == true) {
					//fu.rename(CommonUtil.getLocalImagePath() + "/" + fileName, CommonUtil.getLocalImagePath() + "/" + fileName2);

					File orgFile = new File(Security.getLocalImagePath() + "/" + fileName);
					File desFile = new File(rootPath + floorImageSrc + "/" + fileName2);
					logger.debug("copyFile "+ orgFile.getPath()+" "+desFile.getPath());
					FileUtils.copyFile(orgFile, desFile);


					//sftp.upload(CommonUtil.getLocalImagePath() + "/" + fileName2);

					fu.delete(fileName2);
					File file = new File(Security.getLocalImagePath() + "/" + fileName2);
					if(file.exists()) {
						file.delete();
					}
				}
			}
		}
		return fileList;

	}

	@Override
	public String deleteFloorImage(Floor floor, String num, String type) throws IOException, SftpException {
		String filename = "";
		String status = "1";
		if("1".equals(num)) {
			filename = floor.getImgSrc();
		}
		if(!("".equals(filename) || filename == null)) {
			try {
				System.out.println(filename);
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
				//sftp.cd(floorImagePath);
				// 2016.08.01, duwlsh
				// 경로 변경
				//sftp.cd(floorImagePath);
				String rootPath = contentsRootPath+"/"+floor.getComNum();
				sftp.cd(rootPath + floorImageSrc);
				sftp.delete(filename);
				sftp.disconnect();
			} catch(SftpException se) {
				se.printStackTrace();
				status = "2";
			}
			// 수정시 이미지 삭제일 경우만 DB업데이트
			if(type.equals("update")) {
				updateFloorBlankImage(floor, num);
			}
		}
		return status;
	}

	@Override
	public String deleteFloorImageLocal(Floor floor, String num, String type) throws IOException, SftpException {
		String filename = "";
		String status = "1";
		if("1".equals(num)) {
			filename = floor.getImgSrc();
		}
		if(!("".equals(filename) || filename == null)) {

			String rootPath = contentsRootPath+"/"+floor.getComNum();
			File dfile = new File(rootPath + floorImageSrc + "/" + filename);
			if(dfile.exists()) {
				dfile.delete();
			}

			/*try {
				System.out.println(filename);
				String keyPath = CommonUtil.getLocalPrivateKeyPath() + "/";
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
				//sftp.cd(floorImagePath);
				// 2016.08.01, duwlsh
				// 경로 변경
				//sftp.cd(floorImagePath);
				String rootPath = contentsRootPath+"/"+floor.getComNum();
				sftp.cd(rootPath + floorImageSrc);
				sftp.delete(filename);
				sftp.disconnect();
			} catch(SftpException se) {
				se.printStackTrace();
				status = "2";
			}*/
			// 수정시 이미지 삭제일 경우만 DB업데이트
			if(type.equals("update")) {
				updateFloorBlankImage(floor, num);
			}
		}
		return status;
	}

	@Override
	public void updateFloorBlankImage(Floor floor, String num) throws DataAccessException {
		if("1".equals(num)) {
			floor.setImgSrc("");
		}
		floorDao.updateFloorBlankImage(floor);
	}

	@Override
	@Transactional
	public void registerFloor(MultipartRequest multi, Floor floor) throws DataAccessException, IOException, SftpException{
		// 층별정보설정
		//floor.setFloor(Integer.parseInt((StringUtil.NVL(multi.getParameter("floor"), "1"))));

		String floorName = StringUtil.NVL(multi.getParameter("floorName"), "");
		if(StringUtils.isBlank(floorName)) {
			FloorCode floorCodeParam = new FloorCode();
			floorCodeParam.setComNum(floor.getComNum());
			floorCodeParam.setNodeId(floor.getFloor());
			FloorCode floorCodeInfo = floorCodeDao.getFloorCodeInfo(floorCodeParam);
			if(floorCodeInfo!=null) {
				floor.setFloorName(floorCodeInfo.getNodeId());
			}

		} else {
			floor.setFloorName(floorName);
		}

		floor.setSwLat(Double.parseDouble(StringUtil.NVL(multi.getParameter("swLat"), "0")));
		floor.setSwLng(Double.parseDouble(StringUtil.NVL(multi.getParameter("swLng"), "0")));
		floor.setNeLat(Double.parseDouble(StringUtil.NVL(multi.getParameter("neLat"), "0")));
		floor.setNeLng(Double.parseDouble(StringUtil.NVL(multi.getParameter("neLng"), "0")));
		floor.setDeg(getDegree(Float.parseFloat((StringUtil.NVL(multi.getParameter("deg"), "0")))));
		Map<String, String> fileList = uploadFloorImage(multi, floor);
		floor.setImgSrc(("".equals(fileList.get("imgSrc")) ? "" : fileList.get("imgSrc")));
		floorDao.insertFloor(floor);
	}

	@Override
	@Transactional
	public void modifyFloor(MultipartRequest multi, Floor floor
			) throws DataAccessException, IOException, SftpException{
		// 층별정보설정
		floor.setFloorName(StringUtil.NVL(multi.getParameter("floorName"), ""));
		floor.setSwLat(Double.parseDouble(StringUtil.NVL(multi.getParameter("swLat"), "0")));
		floor.setSwLng(Double.parseDouble(StringUtil.NVL(multi.getParameter("swLng"), "0")));
		floor.setNeLat(Double.parseDouble(StringUtil.NVL(multi.getParameter("neLat"), "0")));
		floor.setNeLng(Double.parseDouble(StringUtil.NVL(multi.getParameter("neLng"), "0")));
		floor.setDeg(getDegree(Float.parseFloat((StringUtil.NVL(multi.getParameter("deg"), "0")))));

		// 이미지 업로드
		Map<String, String> fileList = uploadFloorImage(multi, floor);
		// 이미지정보 수정
		floor.setImgSrc(("".equals(fileList.get("imgSrc")) ? null : fileList.get("imgSrc")));
		floorDao.updateFloor(floor);
	}

	public float getDegree(double degree) {
		int mok = (int) degree / 360;
		if(degree >= 0) {
			 if(mok > 0) {
				 degree = degree - (int) (mok * 360);
			 }
		} else {
			if(Math.abs(mok) > 0) {
				degree = (degree + (int) (Math.abs(mok) * 360));
			 }
		}
		degree = Float.parseFloat(String.format("%3.1f%n", degree));
		return (float) degree;
	}

	@Override
	@Transactional
	public void removeFloor(Floor floor) throws DataAccessException, IOException, SftpException {
		floorDao.deleteFloor(floor);
	}

	@Override
	public List<?> getFloorList(Floor floor) throws DataAccessException {
		List<?> floorList = null;
		floorList = floorDao.getFloorList(floor);
		logger.info("getFloorList {}", floorList.size());
		return floorList;
	}

	@Override
	public List<Floor> bindFloorList(List<Floor> list) throws ParseException {
	    Floor floor = null;
		for(int i=0; i<list.size(); i++) {
			floor = (Floor) list.get(i);
			list.set(i, this.bindFloor(floor));
		}
		return list;
	}

	@Override
	public Floor bindFloor(Floor floor) throws ParseException {
		//String imgUrl = ("".equals(StringUtil.NVL(floor.getImgSrc(), ""))) ? "" : contentsURL + floorImageSrc + "/" + floor.getImgSrc();

		// 2016.07.29, duwlsh
		// 경로 변경 (getComNum 추가)
		String imgUrl = ("".equals(StringUtil.NVL(floor.getImgSrc(), ""))) ? "" : contentsURL + "/" + floor.getComNum() + floorImageSrc + "/" + floor.getImgSrc();
		// 2016.07.29, END

/*
		if(!StringUtil.NVL(floor.getFloor()).equals("") && !StringUtil.NVL(floor.getLocationIdLevelValue()).equals("")){
		    FloorRtlLocation fVo = new FloorRtlLocation();
		    fVo.setSafetyWorkLocationId(floor.getFloor());
            fVo.setLocationIdLevelValue(floor.getLocationIdLevelValue());
            fVo.setHighSafetyWorkLocationId(floor.getHighSafetyWorkLocationId());

            ArrayList<FloorRtlLocation> locArr = new ArrayList<FloorRtlLocation>();
            locArr.add(fVo);


            int level = Integer.parseInt(StringUtil.NVL(floor.getLocationIdLevelValue()));
		    if(level > 1){
		        for(int idx = 0; idx<level-1; idx++ ){
		            fVo = floorDao.getFloorLocationInfo(fVo.getHighSafetyWorkLocationId());
		            locArr.add(0, fVo);
		        }
		    }

		    String regionCode = fVo.getHighSafetyWorkLocationId();

		    //최종 region 정보 add
		    fVo = new FloorRtlLocation();
		    fVo.setSafetyWorkLocationId(regionCode);
            fVo.setLocationIdLevelValue("0");
            fVo.setHighSafetyWorkLocationId("0");
		    locArr.add(0, fVo);

		    floor.setRtlLocationArr(locArr);
		}*/

		floor.setImgURL(imgUrl);
		return floor;
	}

	@Override
	public List<?> getFloorGroup(Floor floor) throws DataAccessException {
		List<?> floorGroup = null;
		floorGroup = floorDao.getFloorGroup(floor);
		logger.info("getFloorGroup {}", floorGroup.size());
		return floorGroup;
	}

    @Override
    public List<?> getFloorCodeList(Company floor) throws DataAccessException {
        return floorDao.getFloorCodeList(floor);
    }
}
