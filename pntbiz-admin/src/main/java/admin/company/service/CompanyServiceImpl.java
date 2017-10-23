package admin.company.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import core.admin.company.dao.CompanyAllowRoleDao;
import core.admin.company.domain.CompanyAllowRole;
import core.admin.auth.dao.LoginRoleDao;
import core.admin.auth.domain.LoginRole;

import framework.Security;
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

import framework.web.file.FileUtil;
import framework.web.ftp.SFTPClient;
import framework.web.util.DateUtil;
import framework.web.util.StringUtil;
import core.admin.company.dao.CompanyDao;
import core.admin.company.domain.Company;
import core.admin.company.domain.CompanySearchParam;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private CompanyAllowRoleDao companyAllowRoleDao;

	@Autowired
	private LoginRoleDao loginRoleDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

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
	@Value("#{config['companyImagePath']}")
	private String companyImagePath;

	@Override
	public Integer getCompanyCount(CompanySearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = companyDao.getCompanyCount(param);
		logger.info("getCompanyCount {}", cnt);
		return cnt;
	}

	@Override
	public List<?> getCompanyList(CompanySearchParam param) throws DataAccessException {
		List<?> companyList = null;
		companyList = companyDao.getCompanyList(param);
		logger.info("getCompanyList {}", companyList.size());
		return companyList;
	}

	@Override
	public List<?> bindCompanyList(List<?> list) throws ParseException {
		List<Company> clist = new ArrayList<Company>();
		for(int i=0; i<list.size(); i++) {
			Company company = (Company) list.get(i);
			String date = DateUtil.timestamp2str(Long.parseLong(company.getRegDate()));
			date = DateUtil.changeStrDateFormat(date, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
			company.setRegDate(date);
			clist.add(company);
		}
		return clist;
	}

	@Override
	public Company getCompanyInfo(Company company) throws DataAccessException{
		Company companyInfo = null;
		companyInfo = companyDao.getCompanyInfo(company);
		logger.info("companyInfo {}", companyInfo);
		return companyInfo;
	}

	@Override
	@Transactional
	public void registerCompany(Company company) throws DataAccessException {
		companyDao.insertCompany(company);
	}

	/**
	 * 업체 추가시 선택한 허용 역할도 함께 추가
	 * 2014-12-04 nohsoo
	 *
	 * @param company
	 * @param roleNums 선택한 허용 역할
	 * @throws DataAccessException
	 */
	@Override
	@Transactional
	public void registerCompany(Company company, int[] roleNums) throws DataAccessException {
		int insertId = companyDao.insertCompany(company);

		/**
		 * 업체 추가시 선택된 허용 역할 추가
		 * 2014-12-04 nohsoo
		 */
		for(int i=0; i<roleNums.length; i++) {
			int roleNum = roleNums[i];
			LoginRole roleParam = new LoginRole();
			roleParam.setRoleNum(roleNum);
			LoginRole loginRoleInfo = loginRoleDao.getLoginRoleInfo(roleParam);

			CompanyAllowRole newCompanyAllowRole = new CompanyAllowRole();
			newCompanyAllowRole.setComNum(insertId);
			newCompanyAllowRole.setRoleNum(loginRoleInfo.getRoleNum());
			companyAllowRoleDao.insertCompanyAllowRole(newCompanyAllowRole);
		}

	}

	@Override
	@Transactional
	public void modifyCompany(Company company) throws DataAccessException{
		companyDao.updateCompany(company);
	}

	/**
	 * 업체 수정시 선택한 허용 역할도 수정
	 * 2014-12-04 nohsoo
	 *
	 * @param company
	 * @param roleNums 선택한 허용 역할
	 * @throws DataAccessException
	 */
	@Override
	@Transactional
	public void modifyCompany(Company company, int[] roleNums) throws DataAccessException {
		companyDao.updateCompany(company);

		/**
		 * 기존 허용 역할 모두 삭제하고 선택된 허용 역할 추가
		 * 2014-12-04 nohsoo
		 */
		CompanyAllowRole delParam = new CompanyAllowRole();
		delParam.setComNum(company.getComNum());
		companyAllowRoleDao.deleteCompanyAllowRoleAll(delParam);

		for(int i=0; i<roleNums.length; i++) {
			int roleNum = roleNums[i];
			LoginRole roleParam = new LoginRole();
			roleParam.setRoleNum(roleNum);
			LoginRole loginRoleInfo = loginRoleDao.getLoginRoleInfo(roleParam);

			CompanyAllowRole newCompanyAllowRole = new CompanyAllowRole();
			newCompanyAllowRole.setComNum(company.getComNum());
			newCompanyAllowRole.setRoleNum(loginRoleInfo.getRoleNum());
			companyAllowRoleDao.insertCompanyAllowRole(newCompanyAllowRole);
		}
	}

	@Override
	@Transactional
	public void modifyCompanyInfo(MultipartRequest multi, Company company) throws DataAccessException, IOException, SftpException {
		company.setComNum(Integer.parseInt((StringUtil.NVL(multi.getParameter("comNum"), "0"))));
		company.setComName(StringUtil.NVL(multi.getParameter("comName"), ""));
		company.setComBizNum(StringUtil.NVL(multi.getParameter("comBizNum"), ""));
		company.setLat(StringUtil.NVL(multi.getParameter("lat"), ""));
		company.setLng(StringUtil.NVL(multi.getParameter("lng"), ""));
		uploadCompanyImage(multi, company);
		companyDao.updateCompany(company);
	}

	@Override
	@Transactional
	public void removeCompany(Company company) throws DataAccessException {
		companyDao.deleteCompany(company);
	}

	@SuppressWarnings("static-access")
	@Override
	public void uploadCompanyImage(MultipartRequest multi, Company company) throws IOException, SftpException {
		String keyPath = Security.getLocalPrivateKeyPath() + "/";
		SFTPClient sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, keyPath + ftpPrivateKey);
		try {
			sftp.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String imgpath = companyImagePath;
		String dirname = Integer.toString(company.getComNum());
		String path = imgpath + "/" + dirname;
		if(sftp.isDir(imgpath, dirname) == false) {
			sftp.mkdir(path);
		}
		sftp.cd(path);

		// 업로드 순서 수동처리
		FileUtil fu = new FileUtil();
		Enumeration<?> files = multi.getFileNames();
		boolean flag = false;
		while (files.hasMoreElements()) {
			String name = (String) files.nextElement();
			String fileName = multi.getFilesystemName(name);
			//System.out.println(fileName);
			if(fileName != null) {
				flag = true;
				boolean fileFlag = sftp.isFile(path, fileName);
				if(fileFlag == true){
					sftp.delete(path + "/" + fileName);
				}
				if(flag == true) {
					sftp.upload(Security.getLocalImagePath() + "/" + fileName);
					fu.delete(fileName);
					File file = new File(Security.getLocalImagePath() + "/" + fileName);
					if(file.exists()) {
						file.delete();
					}
				}
			}
		}
		sftp.disconnect();
	}

	@Override
	public void deleteCompanyImage(String imgName, String comNum) throws IOException, SftpException {
		if(!"".equals(imgName)) {
			try {
				System.out.println(imgName);
				String keyPath = Security.getLocalPrivateKeyPath() + "/";
				SFTPClient sftp = new SFTPClient(ftpHost, ftpPort, ftpID, ftpPW, keyPath + ftpPrivateKey);
				try {
					sftp.connect();
				} catch (JSchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sftp.cd(companyImagePath + "/" + comNum);
				sftp.delete(imgName);
				sftp.disconnect();
			} catch(SftpException se) {
				se.printStackTrace();
			}
		}
	}

	/**
	 * 업체에 대한 허용 역할 목록 반환
	 * 2014-12-04 nohsoo
	 *
	 * @param company comNum 값 필요
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public List<?> getCompanyAllowRoles(Company company) throws DataAccessException {
		CompanyAllowRole param = new CompanyAllowRole();
		param.setComNum(company.getComNum());
		return companyAllowRoleDao.getCompanyAllowRoleListAll(param);
	}
}
