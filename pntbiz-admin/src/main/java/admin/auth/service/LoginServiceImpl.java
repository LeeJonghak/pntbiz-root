package admin.auth.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.admin.user.domain.User;
import core.admin.company.dao.CompanyDao;
import core.admin.company.domain.Company;
import core.admin.auth.dao.LoginDao;
import core.admin.auth.domain.Login;
import core.admin.auth.domain.LoginRole;
import framework.web.util.DateUtil;
import framework.web.util.PagingParam;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private CompanyDao companyDao;

	private Logger logger = LoggerFactory.getLogger(getClass());


	@Override
	public User getUser(String userID, String userPW) {
		return (User)loginDao.getUser(userID, userPW);
	}


	@Override
	public Integer getLoginCount(PagingParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = loginDao.getLoginCount(param);
		logger.info("getLoginCount {}", cnt);
		return cnt;
	}

	@Override
	public List<?> getLoginList(PagingParam param) throws DataAccessException {
		List<?> loginList = null;
		loginList = loginDao.getLoginList(param);
		logger.info("getLoginList {}", loginList.size());
		return loginList;
	}

	@Override
	public List<?> bindLoginList(List<?> list) throws ParseException {
		List<Login> clist = new ArrayList<Login>();
		for(int i=0; i<list.size(); i++) {
			Login login = (Login) list.get(i);
			String date = DateUtil.timestamp2str(Long.parseLong(login.getRegDate()));
			date = DateUtil.changeStrDateFormat(date, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
			login.setRegDate(date);
			clist.add(login);
		}
		return clist;
	}

	/*
	@Override
	public Login getLoginInfo(Login login) throws DataAccessException {
		Login loginInfo = null;
		loginInfo = loginDao.getLogin(login.getUserID());
		logger.info("getLoginInfo {}", loginInfo);
		return loginInfo;
	}
	*/

	@Override
	public Integer getLoginRoleCount(LoginRole loginRole)  throws DataAccessException {
		Integer cnt = 0;
		cnt = loginDao.getLoginRoleCount(loginRole);
		logger.info("getLoginRoleCount {}", cnt);
		return cnt;
	}

	@Override
	@Transactional
	public void registerLogin(Login login) throws Exception {

		/**
		 * 업체정보 확인
		 */
		if(login.getComNum()>0) {
			Company param = new Company();
			param.setComNum(login.getComNum());
			Company company = companyDao.getCompanyInfo(param);
			if(company==null) throw new Exception("not found company");
			login.setComName(company.getComName());
		}

		/**
		 * 비밀번호 암호화 (SHA-256)
		 * 2014-11-18 nohsoo
		 */
		String passwordPlainText = login.getUserPW();
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
		String encPassword = shaPasswordEncoder.encodePassword(passwordPlainText,null);
		login.setUserPW(encPassword);
		/** end **/

		loginDao.insertLogin(login);
	}

	/**
	 * 로그인계정 정보업데이트
	 * 비밀번호도 함께 수정
	 *
	 * @param login
	 * @throws DataAccessException
	 */
	@Override
	@Transactional
	public void modifyLogin(Login login) throws Exception {
		/**
		 * 업체정보 확인
		 */
		if(login.getComNum()>0) {
			Company param = new Company();
			param.setComNum(login.getComNum());
			Company company = companyDao.getCompanyInfo(param);
			if(company==null) throw new Exception("not found company");
			login.setComName(company.getComName());
		}
		/**
		 * 비밀번호 암호화 (SHA-256)
		 * 2014-11-18 nohsoo
		 */
		String passwordPlainText = login.getUserPW();

		if(!StringUtils.isEmpty(passwordPlainText)){
			ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
			String encPassword = shaPasswordEncoder.encodePassword(passwordPlainText,null);
			login.setUserPW(encPassword);
		}
		/** end **/

		loginDao.updateLogin(login);
	}

	@Override
	@Transactional
	public void removeLogin(Login login) throws DataAccessException {
		loginDao.deleteLogin(login);
	}

	@Override
	public Login getLoginInfo(Login login) throws DataAccessException {
		return loginDao.getLogin(login.getUserID());
	}

}
