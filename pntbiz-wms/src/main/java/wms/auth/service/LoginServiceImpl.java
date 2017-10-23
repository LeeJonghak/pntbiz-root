package wms.auth.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.wms.admin.company.dao.CompanyDao;
import core.wms.admin.company.domain.Company;
import core.wms.auth.dao.LoginDao;
import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginOtp;
import framework.Security;
import framework.web.mail.MailUtil;
import framework.web.util.DateUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Value("#{config['mail.from']}")
	private String mailFrom;

	@Autowired
	private LoginDao loginDao;

	@Autowired
	private CompanyDao companyDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Login getLoginInfo(Login login) throws DataAccessException {
		return loginDao.getLogin(login.getUserID());
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
//		String passwordPlainText = login.getUserPW();
//		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
//		shaPasswordEncoder.setEncodeHashAsBase64(true);
//		String encPassword = shaPasswordEncoder.encodePassword(passwordPlainText,null);
//		login.setUserPW(encPassword);
		login.setUserPW(Security.encrypt(login.getUserPW()));
		/** end **/

		loginDao.updateLogin(login);
	}

	/**
	 * 기존 비밀번호를 그대로 사용
	 * 비밀번호를 수정하지 않고 로그인계정정보를 변경할때 사용
	 *
	 * @param login
	 * @param originalPassword
	 * @throws DataAccessException
	 */
	@Override
	@Transactional
	public void modifyLoginExclusionPassword(Login login, String originalPassword) throws Exception {
		login.setUserPW(originalPassword);

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

		loginDao.updateLogin(login);
	}

	@Override
	public LoginOtp getOtpNum(LoginOtp loginOtp) throws DataAccessException {
		logger.debug("loginOtp", loginOtp);
		return loginDao.getLoginOtp(loginOtp);
	}

	@Override
	@Transactional
	public void sendOtpNum(LoginOtp loginOtp) throws DataAccessException {
		// otpNum 생성
		String otpNum = "";
		Random rn = new Random();
		for(int i=0; i<6; i++) {
			int ri = rn.nextInt(9);
			otpNum = otpNum + Integer.toString(ri);
		}
		loginOtp.setOtpNum(otpNum);
		LoginOtp loginOtpInfo = loginDao.getLoginOtp(loginOtp);
		Login login = loginDao.getLogin(loginOtp.getUserID());

		if(loginOtpInfo == null) {
			loginDao.insertLoginOtp(loginOtp);
		} else {
			loginDao.updateLoginOtp(loginOtp);
		}
		// otpNum이 생성된 후 email / sms 발송처리
		String subject = "("+ login.getUserID() +") OTP Authentication Code";
		String text = "("+ login.getUserID() +") OTP Authentication Code : " + otpNum;
		System.out.println(login.getUserEmail());
		MailUtil.sendMail(mailFrom, login.getUserEmail(), subject, text);
	}

	@Override
	public boolean checkOtpNum(LoginOtp loginOtp) throws DataAccessException {
		LoginOtp loginOtpInfo = loginDao.getLoginOtp(loginOtp);
		String sDate = DateUtil.timestamp2str(Long.valueOf(loginOtpInfo.getRegDate()));
		String eDate = DateUtil.getDate("yyyyMMddHHmmss");

		// otp 번호 일치, 등록일로 부터 otpExpire(sec) 이내
		if(loginOtp.getOtpNum().equals(loginOtpInfo.getOtpNum()) && loginOtp.getUserID().equals(loginOtpInfo.getUserID())
				&& DateUtil.getBetweenTimeCount(sDate, eDate) < 300) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void modifyOtpNum(LoginOtp loginOtp) throws Exception {
		loginDao.updateLoginOtp(loginOtp);
	}

}
