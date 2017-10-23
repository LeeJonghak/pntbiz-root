package core.admin.company.dao;

import core.admin.company.domain.CompanyAllowRole;
import core.admin.company.domain.CompanySearchParam;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

import java.util.List;

/**
 * 업체에서 계정생성시 지정가능한 역할 관리
 * 2014-12-04 nohsoo
 */
@Repository
public class CompanyAllowRoleDao extends BaseDao {

    public Integer getCompanyAllowRoleCount(CompanySearchParam param) throws DataAccessException {
        return (Integer) select("getCompanyAllowRoleCount", param);
    }

    @SuppressWarnings("rawtypes")
    public List<?> getCompanyAllowRoleList(CompanySearchParam param) throws DataAccessException {
        return (List) list("getCompanyAllowRoleList", param);
    }

    public CompanyAllowRole getCompanyAllowRoleInfo(CompanyAllowRole companyAllowRole) throws DataAccessException {
        return (CompanyAllowRole) select("getCompanyAllowRoleInfo", companyAllowRole);
    }

    public void insertCompanyAllowRole(CompanyAllowRole companyAllowRole) throws DataAccessException {
        insert("insertCompanyAllowRole", companyAllowRole);
    }

    public void updateCompanyAllowRole(CompanyAllowRole companyAllowRole) throws DataAccessException {
        update("updateCompanyAllowRole", companyAllowRole);
    }

    public void deleteCompanyAllowRole(CompanyAllowRole companyAllowRole) throws DataAccessException {
        delete("deleteCompanyAllowRole", companyAllowRole);
    }

    public void deleteCompanyAllowRoleAll(CompanyAllowRole companyAllowRole) throws DataAccessException {
        delete("deleteCompanyAllowRoleAll", companyAllowRole);
    }

    public List<?> getCompanyAllowRoleListAll(CompanyAllowRole companyAllowRole) throws DataAccessException {
        return (List<?>) list("getCompanyAllowRoleListAll", companyAllowRole);
    }
}