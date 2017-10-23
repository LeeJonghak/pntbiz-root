package wms.component.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import core.wms.admin.company.dao.CompanyDao;
import core.wms.admin.company.domain.Company;
import core.wms.auth.dao.LoginRoleAuthoritiesDao;
import core.wms.auth.domain.Login;
import core.wms.auth.domain.LoginRoleAuthorities;
import framework.web.util.DateUtil;

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
    private Double lat = 0.0;

    /**
     * 업체 경도
     */
    private Double lng = 0.0;

	/**
	 * 업체 UUID
	 */
	private String uuid = "";

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
			this.uuid = company.getUUID();
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

		/**
         * 비밀번호 5회 이상 오류 계정 잠금 처리
         */
        if(login.getLoginFailCnt() >= 5) {
            this.accountNonLocked = false;
        }

        /**
         * 계정 잠금 체크
         */
        if(login.getUserStatus() >= 9) {
            this.accountNonLocked = false;
        }

        /**
         * 마지막 로그인 90일 체크
         */
        String sDate = DateUtil.timestamp2str(Long.parseLong(login.getLoginDate()), "yyyyMMdd");
        String eDate = DateUtil.getDate("yyyyMMdd");
        if(DateUtil.getBetweenDayCount(sDate, eDate) >= 90) {
            this.accountNonExpired = false;
        }

        /**
         * 마지막 비밀번호 변경일 90일 체크
         */
        String sDate2 = DateUtil.timestamp2str(Long.parseLong(login.getPwModDate()), "yyyyMMdd");
        String eDate2 = DateUtil.getDate("yyyyMMdd");
        if(DateUtil.getBetweenDayCount(sDate2, eDate2) >= 90) {
            this.accountNonExpired = false;
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

	public void setUsername(String username) {
		this.username = username;
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
