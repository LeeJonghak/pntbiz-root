package wms.admin.code.service;

import java.util.List;

import core.common.code.domain.Code;
import org.springframework.dao.DataAccessException;

public interface CodeService {
	public List<?> getCodeListByCD(Code code) throws DataAccessException;
	public Integer getCodeCheck(Code code) throws DataAccessException;
	public Code getCodeInfo(Code code) throws DataAccessException;

}