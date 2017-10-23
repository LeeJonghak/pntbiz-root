package wms.component.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import core.wms.admin.company.dao.CompanyDao;
import core.wms.auth.dao.LoginDao;
import core.wms.auth.dao.LoginRoleAuthoritiesDao;
import core.wms.auth.domain.Login;

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

		Login loginInfo = loginDao.getLogin(userId);

		if(loginInfo==null) {
			throw new UsernameNotFoundException("User Not Found!!!");

		} else {
			LoginDetail loginDetail = new LoginDetail(loginRoleAuthoritiesDao, companyDao, loginInfo);

			return loginDetail;
		}

	}

}
