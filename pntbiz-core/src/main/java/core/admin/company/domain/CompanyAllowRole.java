package core.admin.company.domain;

/**
 * 업체에서 계정 생성시 설정가능한 역할
 * 2014-12-04 nohsoo
 */
public class CompanyAllowRole {

    /**
     * 허용역할 번호
     */
    private int comAllowRoleNum = 0;

    /**
     * 업체 번호
     */
    private int comNum = 0;

    /**
     * 역할 번호
     */
    private int roleNum = 0;

    /**
     * 역할 이름
     */
    private String roleName = "";

    /**
     * 등록시간
     */
    private String regDate;

    public int getComAllowRoleNum() {
        return comAllowRoleNum;
    }

    public void setComAllowRoleNum(int comAllowRoleNum) {
        this.comAllowRoleNum = comAllowRoleNum;
    }

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public int getRoleNum() {
        return roleNum;
    }

    public void setRoleNum(int roleNum) {
        this.roleNum = roleNum;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
