package admin.company.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.admin.company.domain.Company;
import core.admin.company.domain.CompanySearchParam;

public interface CompanyService {
	public Integer getCompanyCount(CompanySearchParam param) throws DataAccessException;
	public List<?> getCompanyList(CompanySearchParam param) throws DataAccessException;
	public List<?> bindCompanyList(List<?> list) throws ParseException;
	public void deleteCompanyImage(String imgName, String comNum) throws IOException, SftpException;
	public void uploadCompanyImage(MultipartRequest multi, Company company) throws IOException, SftpException;

	public Company getCompanyInfo(Company company) throws DataAccessException;

	// Company Transaction
	public void registerCompany(Company company) throws DataAccessException;
	public void registerCompany(Company company, int[] roleNums) throws DataAccessException;
	public void modifyCompany(Company company) throws DataAccessException;
	public void modifyCompany(Company company, int[] roleNums) throws DataAccessException;
	public void modifyCompanyInfo(MultipartRequest multi, Company company) throws DataAccessException, IOException, SftpException;
	public void removeCompany(Company company) throws DataAccessException;

	/**
	 * 업체에 대한 허용 역할 목록 반환
	 * 2014-12-04 nohsoo
	 *
	 * @param company comNum 값 필요
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> getCompanyAllowRoles(Company company) throws DataAccessException;
}