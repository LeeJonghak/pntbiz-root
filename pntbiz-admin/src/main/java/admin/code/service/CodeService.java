package admin.code.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.common.code.domain.Code;
import core.common.code.domain.CodeSearchParam;

public interface CodeService {
	public Integer getCodeCount(CodeSearchParam code) throws DataAccessException;
	public List<?> getCodeList(CodeSearchParam code) throws DataAccessException;	
	public List<?> getCodeListByCD(Code code) throws DataAccessException;	
	public Integer getCodeCheck(Code code) throws DataAccessException;
	public Code getCodeInfo(Code code) throws DataAccessException;
	
	// Code Transaction
	public void registerCode(Code code) throws DataAccessException;
	public void modifyCode(Code code) throws DataAccessException;
	public void removeCode(Code code) throws DataAccessException;
}