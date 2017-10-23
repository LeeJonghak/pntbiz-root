package framework.auth;

import core.admin.company.dao.CompanyDao;
import core.admin.company.domain.Company;
import core.admin.auth.dao.LoginRoleAuthoritiesDao;
import core.admin.auth.domain.Login;
import core.admin.auth.domain.LoginRoleAuthorities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginDetail implements UserDetails {

	private static final long serialVersionUID = -21795631197417579L;

	/**
	 * 로그인 아이디
	 */
	private String username = "";

	/**
	 * 비밀번호
	 */
	private String password = "";

	/**
	 * 표시 이름
	 */
	private String displayName = "";

	/**
	 * 업체번호
	 */
	private int companyNumber = 0;

	/**
	 * 업체명
	 */
	private String companyName = "";

	/**
	 * 매장번호
	 */
	private int groupNumber = 0;

	/**
	 * 매장이름
	 */
	private String groupName = "";

    /**
     * 업체 위도
     */
    private String lat = "";

    /**
     * 업체 경도
     */
    private String lng = "";

	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	/**
	 * 계정 잠김 설정
	 */
	private boolean accountNonLocked = true;
	private boolean accountNonExpired = true;

	public LoginDetail(LoginRoleAuthoritiesDao loginAuthoritiesDao, CompanyDao companyDao, Login login) {
		this.username = login.getUserID();
		this.password = login.getUserPW();
		this.displayName = login.getUserName();

		/**
		 * 업체정보 확인
		 */
		if(login.getComNum()>0) {
			Company param = new Company();
			param.setComNum(login.getComNum());
			Company company = companyDao.getCompanyInfo(param);
			this.companyNumber = company.getComNum();
			this.companyName = company.getComName();
            this.lat = company.getLat();
            this.lng = company.getLng();
		}

		/**
		 * Role에 대한 권한 확인
 		 */

		LoginRoleAuthorities loginRoleAuthorities = new LoginRoleAuthorities();
		loginRoleAuthorities.setRoleNum(login.getRoleNum());
		List<?> authList = loginAuthoritiesDao.getLoginRoleAuthorities(loginRoleAuthorities);

		for(int i=0; i<authList.size(); i++) {
			LoginRoleAuthorities auth = (LoginRoleAuthorities)authList.get(i);
			this.authorities.add(new SimpleGrantedAuthority(auth.getAuthCode()));
		}

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
//		return true;
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(int companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
