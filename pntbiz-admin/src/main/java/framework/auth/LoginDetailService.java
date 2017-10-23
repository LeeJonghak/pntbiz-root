package framework.auth;

import core.admin.company.dao.CompanyDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import core.admin.auth.dao.LoginDao;
import core.admin.auth.dao.LoginRoleAuthoritiesDao;
import core.admin.auth.domain.Login;

@Repository
public class LoginDetailService implements UserDetailsService  {

	private LoginDao loginDao;

	private LoginRoleAuthoritiesDao loginRoleAuthoritiesDao;

	private CompanyDao companyDao;

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	public void setLoginRoleAuthoritiesDao(
			LoginRoleAuthoritiesDao loginRoleAuthoritiesDao) {
		this.loginRoleAuthoritiesDao = loginRoleAuthoritiesDao;
	}

	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	@Override
	public UserDetails loadUserByUsername(String userId)
			throws UsernameNotFoundException {

		System.out.println(userId);

		Login loginInfo = null;

		try {
			loginInfo = loginDao.getLogin(userId);
		}catch(Exception e) {
			e.printStackTrace();
		}

		/**
		 * 2015-10-19 nohsoo 최고관리자만 로그인할 수 있게 수정
		 */
		if(loginInfo==null ) {
			throw new UsernameNotFoundException("User Not Found!!!");

		} else {
			LoginDetail loginDetail = new LoginDetail(loginRoleAuthoritiesDao, companyDao, loginInfo);

			return loginDetail;
		}

	}

}
