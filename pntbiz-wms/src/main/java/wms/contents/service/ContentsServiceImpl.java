package wms.contents.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconGroup;
import core.wms.beacon.domain.BeaconGroupSearchParam;
import core.wms.beacon.domain.BeaconSearchParam;
import wms.beacon.service.BeaconService;
import wms.component.auth.LoginDetail;
import core.wms.contents.dao.ContentsDao;
import core.wms.contents.domain.Contents;
import core.wms.contents.domain.ContentsMapping;
import core.wms.contents.domain.ContentsMappingSearchParam;
import core.wms.contents.domain.ContentsSearchParam;
import core.wms.contents.domain.ContentsType;
import core.wms.contents.domain.ContentsTypeComponent;
import core.wms.contents.domain.ContentsTypeSearchParam;
import framework.Security;
import framework.web.file.FileUtil;
import framework.web.ftp.SFTPClient;
import framework.web.util.DateUtil;
import framework.web.util.StringUtil;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroup;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import wms.geofencing.service.GeofencingService;

@Service
public class ContentsServiceImpl implements ContentsService {
	@Autowired
	private ContentsDao contentsDao;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeDao codeDao;

	@Autowired
	private BeaconService beaconService;

	@Autowired
	private GeofencingService geofencingService;

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
	@Value("#{config['contents.image.path']}")
	private String contentsImagePath;
	@Value("#{config['contents.image.src']}")
	private String contentsImageSrc;
	@Value("#{config['contents.sound.path']}")
	private String contentsSoundPath;
	@Value("#{config['contents.sound.src']}")
	private String contentsSoundSrc;
	@Value("#{config['contents.root.path']}")
	private String contentsRootPath;

	@Override
	public Integer getContentsCount(ContentsSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = contentsDao.getContentsCount(param);
		logger.info("getContentsCount {}", cnt);
		return cnt;
	}

	@Override
	public List<?> getContentsList(ContentsSearchParam param) throws DataAccessException {
		List<?> contentsList = null;
		contentsList = contentsDao.getContentsList(param);
		logger.info("getContentsList {}", contentsList.size());
		return contentsList;
	}

	@Override
	public List<?> bindContentsList(List<?> list, List<?> conCD) throws ParseException {
		List<Contents> clist = new ArrayList<Contents>();

		for(int i=0; i<list.size(); i++) {
			Contents contents = (Contents) list.get(i);
			for(int j=0; j<conCD.size(); j++) {
				Code code = (Code) conCD.get(j);
				if(contents.getConType().equals(code.getsCD())) {
					contents.setConTypeText(code.getsName());
					contents.setLangCode(code.getLangCode());
				}
			}

			String contentsImageURL = contentsURL + "/" + contents.getComNum() + contentsImageSrc + "/";
			String contentsSoundURL = contentsURL + "/" + contents.getComNum() + contentsSoundSrc + "/";

			String sDateText = (0 == contents.getsDate()) ? "" : DateUtil.timestamp2str(contents.getsDate(), "yyyy-MM-dd HH:mm:ss");
			String eDateText = (0 == contents.geteDate()) ? "" : DateUtil.timestamp2str(contents.geteDate(), "yyyy-MM-dd HH:mm:ss");
			contents.setsDateText(sDateText);
			contents.seteDateText(eDateText);
			String imgURL1 = ("".equals(StringUtil.NVL(contents.getImgSrc1(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc1(), 0, 6) + "/" + contents.getImgSrc1();
			String imgURL2 = ("".equals(StringUtil.NVL(contents.getImgSrc2(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc2(), 0, 6) + "/" + contents.getImgSrc2();
			String imgURL3 = ("".equals(StringUtil.NVL(contents.getImgSrc3(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc3(), 0, 6) + "/" + contents.getImgSrc3();
			String imgURL4 = ("".equals(StringUtil.NVL(contents.getImgSrc4(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc4(), 0, 6) + "/" + contents.getImgSrc4();
			String imgURL5 = ("".equals(StringUtil.NVL(contents.getImgSrc5(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc5(), 0, 6) + "/" + contents.getImgSrc5();
			String soundURL1 = ("".equals(StringUtil.NVL(contents.getSoundSrc1(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc1(), 0, 6) + "/" + contents.getSoundSrc1();
			String soundURL2 = ("".equals(StringUtil.NVL(contents.getSoundSrc2(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc2(), 0, 6) + "/" + contents.getSoundSrc2();
			String soundURL3 = ("".equals(StringUtil.NVL(contents.getSoundSrc3(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc3(), 0, 6) + "/" + contents.getSoundSrc3();
			contents.setImgURL1(imgURL1);
			contents.setImgURL2(imgURL2);
			contents.setImgURL3(imgURL3);
			contents.setImgURL4(imgURL4);
			contents.setImgURL5(imgURL5);
			contents.setSoundURL1(soundURL1);
			contents.setSoundURL2(soundURL2);
			contents.setSoundURL3(soundURL3);
			clist.add(contents);
		}
		return clist;
	}

	@Override
	public Contents bindContents(Contents contents) throws ParseException {
		Code conType = new Code();
		conType.setgCD("CONTYPE");
		List<?> codeCD = codeDao.getCodeListByCD(conType);

		String contentsImageURL = contentsURL + "/" + contents.getComNum() + contentsImageSrc + "/";
		String contentsSoundURL = contentsURL + "/" + contents.getComNum() + contentsSoundSrc + "/";

		for(int j=0; j<codeCD.size(); j++) {
			Code code = (Code) codeCD.get(j);
			if(contents.getConType().equals(code.getsCD())) {
				contents.setConTypeText(code.getsName());
				contents.setLangCode(code.getLangCode());
			}
		}
		String sDateText = (0 == contents.getsDate()) ? "" : DateUtil.timestamp2str(contents.getsDate(), "yyyy-MM-dd HH:mm:ss");
		String eDateText = (0 == contents.geteDate()) ? "" : DateUtil.timestamp2str(contents.geteDate(), "yyyy-MM-dd HH:mm:ss");
		contents.setsDateText(sDateText);
		contents.seteDateText(eDateText);
		String imgURL1 = ("".equals(StringUtil.NVL(contents.getImgSrc1(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc1(), 0, 6) + "/" + contents.getImgSrc1();
		String imgURL2 = ("".equals(StringUtil.NVL(contents.getImgSrc2(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc2(), 0, 6) + "/" + contents.getImgSrc2();
		String imgURL3 = ("".equals(StringUtil.NVL(contents.getImgSrc3(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc3(), 0, 6) + "/" + contents.getImgSrc3();
		String imgURL4 = ("".equals(StringUtil.NVL(contents.getImgSrc4(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc4(), 0, 6) + "/" + contents.getImgSrc4();
		String imgURL5 = ("".equals(StringUtil.NVL(contents.getImgSrc5(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc5(), 0, 6) + "/" + contents.getImgSrc5();
		String soundURL1 = ("".equals(StringUtil.NVL(contents.getSoundSrc1(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc1(), 0, 6) + "/" + contents.getSoundSrc1();
		String soundURL2 = ("".equals(StringUtil.NVL(contents.getSoundSrc2(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc2(), 0, 6) + "/" + contents.getSoundSrc2();
		String soundURL3 = ("".equals(StringUtil.NVL(contents.getSoundSrc3(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc3(), 0, 6) + "/" + contents.getSoundSrc3();
		contents.setImgURL1(imgURL1);
		contents.setImgURL2(imgURL2);
		contents.setImgURL3(imgURL3);
		contents.setImgURL4(imgURL4);
		contents.setImgURL5(imgURL5);
		contents.setSoundURL1(soundURL1);
		contents.setSoundURL2(soundURL2);
		contents.setSoundURL3(soundURL3);
		return contents;
	}

	@Override
	public Contents getContentsInfo(Contents contents) throws DataAccessException {
		Contents contentsInfo = null;
		contentsInfo = contentsDao.getContentsInfo(contents);
		logger.info("getContentsInfo {}", contentsInfo);
		return contentsInfo;
	}

	@SuppressWarnings("static-access")
	@Override
	public Map<String, String> uploadContentsFile(MultipartRequest multi, Contents contents) throws IOException {
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

		// 업로드 순서 수동처리
		FileUtil fu = new FileUtil();
		Map<String, String> fileList = new HashMap<String,String>();
		Enumeration<?> files = multi.getFileNames();
		fileList.put("imgSrc1", "");
		fileList.put("imgSrc2", "");
		fileList.put("imgSrc3", "");
		fileList.put("imgSrc4", "");
		fileList.put("imgSrc5", "");
		fileList.put("soundSrc1", "");
		fileList.put("soundSrc2", "");
		fileList.put("soundSrc3", "");

		boolean flag = false;
		String imagePath = "";
		String soundPath = "";
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			String fileName = multi.getFilesystemName(name);
			String fileName2 = "";

			// 2016.07.27, duwlsh
			// 폴더 경로 변경
			String comNum = String.valueOf(contents.getComNum());
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
			// // 2016.07.27, duwlsh

			if(fileName != null) {
				if("imgSrc1".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						imagePath = sftp.mkdirDate(rootPath + contentsImageSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsImagePath);
					sftp.cd(imagePath);
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc1", fileName2);
					if(!("".equals(contents.getImgSrc1()) || contents.getImgSrc1() == null)) {
						try {
							//sftp.delete(contentsImagePath + "/" + contents.getImgSrc1());
							// delete하려는 파일의 폴더 위치
							String fileDate = StringUtil.getSubString(contents.getImgSrc1(), 0, 6);
							sftp.delete(rootPath + contentsImageSrc  + "/" + fileDate + "/" + contents.getImgSrc1());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("imgSrc2".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						imagePath = sftp.mkdirDate(rootPath + contentsImageSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsImagePath);
					sftp.cd(imagePath);

					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc2", fileName2);
					if(!("".equals(contents.getImgSrc2()) || contents.getImgSrc2() == null)) {
						try {
							// delete하려는 파일의 폴더 위치
							String fileDate = StringUtil.getSubString(contents.getImgSrc2(), 0, 6);
							sftp.delete(rootPath + contentsImageSrc  + "/" + fileDate + "/" + contents.getImgSrc2());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("imgSrc3".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						imagePath = sftp.mkdirDate(rootPath + contentsImageSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsImagePath);
					sftp.cd(imagePath);

					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc3", fileName2);
					if(!("".equals(contents.getImgSrc3()) || contents.getImgSrc3() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getImgSrc3(), 0, 6);
							sftp.delete(rootPath + contentsImageSrc  + "/" + fileDate + "/" + contents.getImgSrc3());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("imgSrc4".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						imagePath = sftp.mkdirDate(rootPath + contentsImageSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// sftp.cd(contentsImagePath);
					sftp.cd(imagePath);

					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc4", fileName2);
					if(!("".equals(contents.getImgSrc4()) || contents.getImgSrc4() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getImgSrc4(), 0, 6);
							sftp.delete(rootPath + contentsImageSrc  + "/" + fileDate + "/" + contents.getImgSrc4());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("imgSrc5".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						imagePath = sftp.mkdirDate(rootPath + contentsImageSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// sftp.cd(contentsImagePath);
					sftp.cd(imagePath);
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("imgSrc5", fileName2);
					if(!("".equals(contents.getImgSrc5()) || contents.getImgSrc5() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getImgSrc5(), 0, 6);
							sftp.delete(rootPath + contentsImageSrc  + "/" + fileDate + "/" + contents.getImgSrc5());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("soundSrc1".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						soundPath = sftp.mkdirDate(rootPath + contentsSoundSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsSoundPath);
					sftp.cd(soundPath);
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("soundSrc1", fileName2);
					if(!("".equals(contents.getSoundSrc1()) || contents.getSoundSrc1() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getSoundSrc1(), 0, 6);
							sftp.delete(rootPath + contentsSoundSrc  + "/" + fileDate + "/" + contents.getSoundSrc1());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("soundSrc2".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						soundPath = sftp.mkdirDate(rootPath + contentsSoundSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsSoundPath);
					sftp.cd(soundPath);
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("soundSrc2", fileName2);
					if(!("".equals(contents.getSoundSrc2()) || contents.getSoundSrc2() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getSoundSrc2(), 0, 6);
							sftp.delete(rootPath + contentsSoundSrc  + "/" + fileDate + "/" + contents.getSoundSrc2());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				if("soundSrc3".equals(name)) {
					// 날짜 폴더 체크 후 없으면 생성
					try {
						soundPath = sftp.mkdirDate(rootPath + contentsSoundSrc);
					} catch (SftpException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					//sftp.cd(contentsSoundPath);
					sftp.cd(soundPath);
					flag = true;
					fileName2 = DateUtil.getMicrotime() + fu.getFileExt(fileName);
					fileList.put("soundSrc3", fileName2);
					if(!("".equals(contents.getSoundSrc3()) || contents.getSoundSrc3() == null)) {
						try {
							String fileDate = StringUtil.getSubString(contents.getSoundSrc3(), 0, 6);
							sftp.delete(rootPath + contentsSoundSrc  + "/" + fileDate + "/" + contents.getSoundSrc3());
						} catch(SftpException se) {
							se.printStackTrace();
						}
					}
				}
				// 업로드
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
	public String deleteContentsFile(Contents contents, String fileType, String num, String type) throws IOException, SftpException {
		String filename = "";
		String status = "1";
		String path = "";
		String fileDate = "";
		if("img".equals(fileType)) {
			if("1".equals(num)) filename = contents.getImgSrc1();
			if("2".equals(num)) filename = contents.getImgSrc2();
			if("3".equals(num)) filename = contents.getImgSrc3();
			if("4".equals(num)) filename = contents.getImgSrc4();
			if("5".equals(num)) filename = contents.getImgSrc5();

			System.out.println(filename);

			if(!("".equals(filename) || filename == null)) {
    			fileDate = StringUtil.getSubString(filename, 0, 6);
    			path = contentsRootPath + "/" + contents.getComNum() + contentsImageSrc + "/" + fileDate;
			}
		}
		if("sound".equals(fileType)) {
			if("1".equals(num)) filename = contents.getSoundSrc1();
			if("2".equals(num)) filename = contents.getSoundSrc2();
			if("3".equals(num)) filename = contents.getSoundSrc3();

			if(!("".equals(filename) || filename == null)) {
    			fileDate = StringUtil.getSubString(filename, 0, 6);
    			path = contentsRootPath + "/" + contents.getComNum() + contentsSoundSrc + "/" + fileDate;
			}
		}
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
				sftp.cd(path);
				sftp.delete(filename);
				sftp.disconnect();
			} catch(SftpException se) {
				se.printStackTrace();
				status = "2";
			}
			// 수정시 삭제일 경우만 DB업데이트
			if(type.equals("update")) {
				updateContentsBlankFile(contents, fileType, num);
			}
		}
		return status;
	}

	@Override
	public void updateContentsBlankFile(Contents contents, String fileType, String num) throws DataAccessException {
		if("img".equals(fileType)) {
			if("1".equals(num)) contents.setImgSrc1("");
			if("2".equals(num)) contents.setImgSrc2("");
			if("3".equals(num)) contents.setImgSrc3("");
			if("4".equals(num)) contents.setImgSrc4("");
			if("5".equals(num)) contents.setImgSrc5("");
		}
		if("sound".equals(fileType)) {
			if("1".equals(num)) contents.setSoundSrc1("");
			if("2".equals(num)) contents.setSoundSrc2("");
			if("3".equals(num)) contents.setSoundSrc3("");
		}
		contentsDao.updateContentsBlankFile(contents);
	}

	@Override
	public Integer getContentsFileCount(MultipartRequest multi) {
		int cnt = 0;
		Enumeration<?> files = multi.getFileNames();
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			String fileName = multi.getFilesystemName(name);
			if(fileName != null) {
				cnt++;
			}
		}
		return cnt;
	}

	@Override
	@Transactional
	public void registerContents(MultipartRequest multi, Contents contents
			) throws DataAccessException, IOException, SftpException{
		contents.setConType(StringUtil.NVL(multi.getParameter("conType"), ""));
		contents.setConName((StringUtil.NVL(multi.getParameter("conName"), "")));
		contents.setAcNum(Integer.parseInt(StringUtil.NVL(multi.getParameter("acNum"), "0")));
		contents.setExpFlag((StringUtil.NVL(multi.getParameter("expFlag"), "Y")));
		contents.setRssi(Integer.parseInt(StringUtil.NVL(multi.getParameter("rssi"), "-60")));
		contents.setConDesc((StringUtil.NVL(multi.getParameter("conDesc"), "")));
		String sDate = StringUtil.NVL(multi.getParameter("sDate"), "0");
		String eDate = StringUtil.NVL(multi.getParameter("eDate"), "0");
		if(!"0".equals(sDate)) contents.setsDate(DateUtil.str2timestamp(sDate, "yyyy-MM-dd HH:mm:ss"));
		else contents.setsDate(Long.parseLong(sDate));
		if(!"0".equals(eDate)) contents.seteDate(DateUtil.str2timestamp(eDate, "yyyy-MM-dd HH:mm:ss"));
		else contents.seteDate(Long.parseLong(eDate));
		contents.setText1((StringUtil.NVL(multi.getParameter("text1"), "")));
		contents.setText2((StringUtil.NVL(multi.getParameter("text2"), "")));
		contents.setText3((StringUtil.NVL(multi.getParameter("text3"), "")));
		contents.setText4((StringUtil.NVL(multi.getParameter("text4"), "")));
		contents.setText5((StringUtil.NVL(multi.getParameter("text5"), "")));
		contents.setUrl1((StringUtil.NVL(multi.getParameter("url1"), "")));
		contents.setUrl2((StringUtil.NVL(multi.getParameter("url2"), "")));
		contents.setUrl3((StringUtil.NVL(multi.getParameter("url3"), "")));

		Map<String, String> fileList = uploadContentsFile(multi, contents);
		contents.setImgSrc1(("".equals(fileList.get("imgSrc1")) ? "" : fileList.get("imgSrc1")));
		contents.setImgSrc2(("".equals(fileList.get("imgSrc2")) ? "" : fileList.get("imgSrc2")));
		contents.setImgSrc3(("".equals(fileList.get("imgSrc3")) ? "" : fileList.get("imgSrc3")));
		contents.setImgSrc4(("".equals(fileList.get("imgSrc4")) ? "" : fileList.get("imgSrc4")));
		contents.setImgSrc5(("".equals(fileList.get("imgSrc5")) ? "" : fileList.get("imgSrc5")));
		contents.setSoundSrc1(("".equals(fileList.get("soundSrc1")) ? "" : fileList.get("soundSrc1")));
		contents.setSoundSrc2(("".equals(fileList.get("soundSrc2")) ? "" : fileList.get("soundSrc2")));
		contents.setSoundSrc3(("".equals(fileList.get("soundSrc3")) ? "" : fileList.get("soundSrc3")));

		Integer conNum = contentsDao.insertContents(contents);
		contents.setConNum(conNum);
		contentsDao.insertContentsDetail(contents);
	}

	@Override
	@Transactional
	public void modifyContents(MultipartRequest multi, Contents contents
			) throws DataAccessException, IOException, SftpException{
		contents.setConName((StringUtil.NVL(multi.getParameter("conName"), "")));
		contents.setAcNum(Integer.parseInt(StringUtil.NVL(multi.getParameter("acNum"), "0")));
		contents.setExpFlag((StringUtil.NVL(multi.getParameter("expFlag"), "Y")));
		contents.setRssi(Integer.parseInt(StringUtil.NVL(multi.getParameter("rssi"), "-60")));
		contents.setConDesc((StringUtil.NVL(multi.getParameter("conDesc"), "")));
		String sDate = StringUtil.NVL(multi.getParameter("sDate"), "0");
		String eDate = StringUtil.NVL(multi.getParameter("eDate"), "0");
		if(!"0".equals(sDate)) contents.setsDate(DateUtil.str2timestamp(sDate, "yyyy-MM-dd HH:mm:ss"));
		else contents.setsDate(Long.parseLong(sDate));
		if(!"0".equals(eDate)) contents.seteDate(DateUtil.str2timestamp(eDate, "yyyy-MM-dd HH:mm:ss"));
		else contents.seteDate(Long.parseLong(eDate));
		contents.setText1((StringUtil.NVL(multi.getParameter("text1"), "")));
		contents.setText2((StringUtil.NVL(multi.getParameter("text2"), "")));
		contents.setText3((StringUtil.NVL(multi.getParameter("text3"), "")));
		contents.setText4((StringUtil.NVL(multi.getParameter("text4"), "")));
		contents.setText5((StringUtil.NVL(multi.getParameter("text5"), "")));
		contents.setUrl1((StringUtil.NVL(multi.getParameter("url1"), "")));
		contents.setUrl2((StringUtil.NVL(multi.getParameter("url2"), "")));
		contents.setUrl3((StringUtil.NVL(multi.getParameter("url3"), "")));

		Map<String, String> fileList = uploadContentsFile(multi, contents);
		contents.setImgSrc1(("".equals(fileList.get("imgSrc1")) ? "" : fileList.get("imgSrc1")));
		contents.setImgSrc2(("".equals(fileList.get("imgSrc2")) ? "" : fileList.get("imgSrc2")));
		contents.setImgSrc3(("".equals(fileList.get("imgSrc3")) ? "" : fileList.get("imgSrc3")));
		contents.setImgSrc4(("".equals(fileList.get("imgSrc4")) ? "" : fileList.get("imgSrc4")));
		contents.setImgSrc5(("".equals(fileList.get("imgSrc5")) ? "" : fileList.get("imgSrc5")));
		contents.setSoundSrc1(("".equals(fileList.get("soundSrc1")) ? "" : fileList.get("soundSrc1")));
		contents.setSoundSrc2(("".equals(fileList.get("soundSrc2")) ? "" : fileList.get("soundSrc2")));
		contents.setSoundSrc3(("".equals(fileList.get("soundSrc3")) ? "" : fileList.get("soundSrc3")));

		contentsDao.updateContents(contents);
		if(getContentsFileCount(multi) > 0 || (contents.getText1() !=  null) || (contents.getText2() != null)
				|| (contents.getText3() !=  null) || (contents.getUrl1() !=  null) || (contents.getUrl1() !=  null)
				|| (contents.getUrl1() !=  null)) {
			contentsDao.updateContentsDetail(contents);
		}
	}

	@Override
	@Transactional
	public void removeContents(Contents contents) throws DataAccessException, IOException, SftpException {
		contentsDao.deleteContents(contents);
	}

	@Override
	public Integer getContentsMappingCount(ContentsMappingSearchParam param) 	throws DataAccessException {
		Integer cnt = 0;
		cnt = contentsDao.getContentsMappingCount(param);
		logger.info("getContentsMappingCount {}", cnt);
		return cnt;
	}

	@Override
	public List<?> getContentsMappingList(ContentsMappingSearchParam param) throws DataAccessException {
		List<?> contentsMappingList = null;
		contentsMappingList = contentsDao.getContentsMappingList(param);
		logger.info("getContentsMappingList {}", contentsMappingList.size());
		return contentsMappingList;
	}

	@Override
	public List<?> bindContentsMappingList(List<?> list, List<?> refCD, List<?> refCD2, List<?> refSubCD, List<?> conCD) throws ParseException {
		List<ContentsMapping> cmlist = new ArrayList<ContentsMapping>();
		for(int i=0; i<list.size(); i++) {
			ContentsMapping contentsMapping = (ContentsMapping) list.get(i);
			for(int j=0; j<refCD.size(); j++) {
				Code code = (Code) refCD.get(j);
				if(contentsMapping.getRefType().equals(code.getsCD())) {
					contentsMapping.setRefTypeText(code.getsName());
					contentsMapping.setRefTypeLangCode(code.getLangCode());
				}
			}
			for(int j=0; j<refCD2.size(); j++) {
				Code code = (Code) refCD2.get(j);
				if(contentsMapping.getRefType().equals(code.getsCD())) {
					contentsMapping.setRefTypeText(code.getsName());
					contentsMapping.setRefTypeLangCode(code.getLangCode());
				}
			}
			for(int j=0; j<refSubCD.size(); j++) {
				Code code = (Code) refSubCD.get(j);
				if(contentsMapping.getRefSubType().equals(code.getsCD())) {
					contentsMapping.setRefSubTypeText(code.getsName());
					contentsMapping.setRefSubTypeLangCode(code.getLangCode());
				}
			}
			for(int j=0; j<conCD.size(); j++) {
				Code code = (Code) conCD.get(j);
				if(contentsMapping.getConType().equals(code.getsCD())) {
					contentsMapping.setConTypeText(code.getsName());
					contentsMapping.setConTypeLangCode(code.getLangCode());
				}
			}
			String regDateText = (0 == contentsMapping.getRegDate()) ? "" : DateUtil.timestamp2str(contentsMapping.getRegDate(), "yyyy-MM-dd HH:mm:ss");
			contentsMapping.setRegDateText(regDateText);
			cmlist.add(contentsMapping);
		}
		return cmlist;
	}

	/**
	 * 콘텐츠 일괄 매핑
	 * 매핑 타입별로 Data 세팅
	 */
	@Override
	public List<ContentsMapping> bindMappingDataList(Integer comNum, String mappingType) throws DataAccessException {
		List<ContentsMapping> mappingList = new ArrayList<ContentsMapping>();

		if (mappingType.equals("BC")) {
			BeaconSearchParam param = new BeaconSearchParam();
			param.setPageSizeZero();
			param.setComNum(comNum);
			param.setBeaconType("F");
			List<?> list = beaconService.getBeaconList(param);

			for (int i = 0; i < list.size(); i++) {
				ContentsMapping contentsMapping = new ContentsMapping();
				Beacon beacon = (Beacon) list.get(i);
				contentsMapping.setRefNum(beacon.getBeaconNum().intValue());
				contentsMapping.setRefName(beacon.getBeaconName());

				mappingList.add(contentsMapping);
			}
		} else if (mappingType.equals("BCG")) {
			BeaconGroupSearchParam param = new BeaconGroupSearchParam();
			param.setComNum(comNum);
			List<?> list = beaconService.getBeaconGroupList(param);

			for (int i = 0; i < list.size(); i++) {
				ContentsMapping contentsMapping = new ContentsMapping();
				BeaconGroup beaconGroup = (BeaconGroup) list.get(i);
				contentsMapping.setRefNum(beaconGroup.getBeaconGroupNum().intValue());
				contentsMapping.setRefName(beaconGroup.getBeaconGroupName());

				mappingList.add(contentsMapping);
			}
		} else if (mappingType.equals("GF")) {
			GeofencingGroupSearchParam gParam = new GeofencingGroupSearchParam();
	        gParam.setComNum(comNum);
	        gParam.setPageSizeZero();
			List<?> list = geofencingService.getGeofencingList(gParam);

			for (int i = 0; i < list.size(); i++) {
				ContentsMapping contentsMapping = new ContentsMapping();
				Geofencing geofencing = (Geofencing) list.get(i);
				contentsMapping.setRefNum(geofencing.getFcNum().intValue());
				contentsMapping.setRefName(geofencing.getFcName());

				mappingList.add(contentsMapping);
			}
		} else if (mappingType.equals("GFG")) {
			GeofencingGroupSearchParam param = new GeofencingGroupSearchParam();
			param.setComNum(comNum);
			List<?> list = geofencingService.getGeofencingGroupList(param);

			for (int i = 0; i < list.size(); i++) {
				ContentsMapping contentsMapping = new ContentsMapping();
				GeofencingGroup geofencingGroup = (GeofencingGroup) list.get(i);
				contentsMapping.setRefNum(geofencingGroup.getFcGroupNum());
				contentsMapping.setRefName(geofencingGroup.getFcGroupName());

				mappingList.add(contentsMapping);
			}
		}

		return mappingList;
	}

	/**
	 * 콘텐츠 일괄 매핑
	 * 매핑타입별로 분기
	 */
	@Override
	public void assignContentsReference(LoginDetail loginDetail, ContentsMapping contentsMapping) throws Exception {
		String refType = contentsMapping.getRefType();
		String refSubType = contentsMapping.getRefSubType();
		Long[] refNums = contentsMapping.getRefNums();
		Long[] conNums = contentsMapping.getConNums();
		Integer evtNum = contentsMapping.getEvtNum();

		for(int i=0; i<refNums.length; i++) {
    		for(int z=0; z<conNums.length; z++) {
    			ContentsMapping vo = new ContentsMapping();
                vo.setConNum(conNums[z].intValue());
                vo.setRefType(refType);
                vo.setRefSubType(refSubType);
                vo.setRefNum(refNums[i].intValue());
                vo.setEvtNum(evtNum);
                contentsDao.insertContentsMapping(vo);
    		}
    	}
	}

	@Override
	public String unassignContentsReference(String mappingInfo, String delType) throws Exception {
		String data[] = mappingInfo.split("\\|");

		String conNum = data[0];
		String refType = data[1];
		String refNum = data[2];
		String refSubNum = data[3];

	    ContentsMapping vo = new ContentsMapping();
	    vo.setConNum(Integer.valueOf(conNum));
	    vo.setRefType(refType);
	    vo.setRefNum(Integer.valueOf(refNum));
	    vo.setRefSubType(refSubNum);

	    if(delType.equals("mapping")) {
	        contentsDao.deleteContentsMapping(vo);
	    }else if(delType.equals("event")){
	    	vo.setEvtNum(0);
	        contentsDao.modifyContentsMapping(vo);
	    }
	    return refType;
	}

	@Override
	public ContentsType getContentsTypeInfo(ContentsType contentsType) throws DataAccessException {
		ContentsType contentsTypeInfo = null;
		contentsTypeInfo = contentsDao.getContentsTypeInfo(contentsType);
		logger.info("getContentsTypeInfo {}", contentsTypeInfo);
		return contentsTypeInfo;
	}

	@Override
	public Integer getContentsTypeCount(ContentsTypeSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = contentsDao.getContentsTypeCount(param);
		logger.info("getContentsTypeCount {}", cnt);
		return cnt;
	}

	@Override
	public List<?> getContentsTypeList(ContentsTypeSearchParam param) throws DataAccessException {
		List<?> contentsList = null;
		contentsList = contentsDao.getContentsTypeList(param);
		logger.info("getContentsTypeList {}", contentsList.size());
		return contentsList;
	}

	@Override
	@Transactional
	public void registerContentsType(ContentsType contentsType, List<ContentsTypeComponent> contentsTypeComponentList) throws DataAccessException {
		Integer typeNum = contentsDao.insertContentsType(contentsType);
		for(int i=0; i<contentsTypeComponentList.size(); i++) {
			ContentsTypeComponent ContentsTypeComponentInfo = contentsTypeComponentList.get(i);
			ContentsTypeComponentInfo.setTypeNum(typeNum);
			contentsDao.insertContentsTypeComponent(ContentsTypeComponentInfo);
		}
	}

	@Override
	@Transactional
	public void modifyContentsType(ContentsType contentsType, List<ContentsTypeComponent> contentsTypeComponentList) throws DataAccessException {
		contentsDao.updateContentsType(contentsType);
		for(int i=0; i<contentsTypeComponentList.size(); i++) {
			ContentsTypeComponent ContentsTypeComponentInfo = contentsTypeComponentList.get(i);
			contentsDao.updateContentsTypeComponent(ContentsTypeComponentInfo);
		}
	}

	@Override
	@Transactional
	public void removeContentsType(ContentsType contentsType) throws DataAccessException {
		ContentsTypeComponent contentsTypeComponent = new ContentsTypeComponent();
		contentsTypeComponent.setTypeNum(contentsType.getTypeNum());
		contentsDao.deleteContentsTypeComponentAll(contentsTypeComponent);
		contentsDao.deleteContentsType(contentsType);
	}

	@Override
	public List<?> getContentsTypeComponentList(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
		List<?> contentsTypeComponentList = null;
		contentsTypeComponentList = contentsDao.getContentsTypeComponentList(contentsTypeComponent);
		logger.info("getContentsTypeComponentList {}", contentsTypeComponentList.size());
		return contentsTypeComponentList;
	}

}
