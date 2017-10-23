package admin.user.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import core.common.code.dao.CodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import core.common.code.domain.Code;
import framework.web.util.DateUtil;
import core.admin.user.dao.UserDao;
import core.admin.user.domain.User;
import core.admin.user.domain.UserSearchParam;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeDao codeDao;
	
	@Override
	public Integer getUserCount(UserSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = userDao.getUserCount(param);
		logger.info("getUserCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getUserList(UserSearchParam param) throws DataAccessException {
		List<?> userList = null;
		userList = userDao.getUserList(param);		
		logger.info("getUserList {}", userList.size());		
		return userList;
	}
	
	@Override
	public User getUserInfo(User user) throws DataAccessException {
		User userInfo = null;
		userInfo = userDao.getUserInfo(user);
		logger.info("userInfo {}", userInfo);
		return userInfo;
	}
	
	@Override
	public User getUserInfoByID(User user) throws DataAccessException {
		User userInfo = null;
		userInfo = userDao.getUserInfoByID(user);
		logger.info("userInfo {}", userInfo);
		return userInfo;
	}
	
	@Override
	public List<?> bindUserList(List<?> list) throws ParseException {
		List<User> ulist = new ArrayList<User>();
		Code code = new Code();
		code.setgCD("GENDER");
		List<?> genderCD = codeDao.getCodeListByCD(code);
		code.setgCD("AGREE");
		List<?> agreeCD = codeDao.getCodeListByCD(code);
		
		for(int i=0; i<list.size(); i++) {
			User user = (User) list.get(i);
			String birth = user.getBirth();
			String anniversary = user.getAnniversary();
			birth = DateUtil.changeStrDateFormat(birth, "yyyyMMdd", "yyyy-MM-dd"); 
			anniversary = DateUtil.changeStrDateFormat(anniversary, "yyyyMMdd", "yyyy-MM-dd");
			user.setBirth(birth);
			user.setAnniversary(anniversary);			
			for(int j=0; j<genderCD.size(); j++) {
				Code gender = (Code) genderCD.get(j);
				if(user.getGender().equals(gender.getsCD())) {
					user.setGenderText(gender.getsName());
				}
			}	
			for(int j=0; j<agreeCD.size(); j++) {
				Code agree = (Code) agreeCD.get(j);
				if(user.getAgreeContents().equals(agree.getsCD())) {
					user.setAgreeContentsText(agree.getsName());
				}
				if(user.getAgreeLocation().equals(agree.getsCD())) {
					user.setAgreeLocationText(agree.getsName());
				}
			}	
			ulist.add(user);
		}
		return ulist;
	}	
}
