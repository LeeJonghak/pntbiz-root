package wms.admin.code.service;

import java.util.List;

import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CodeServiceImpl implements CodeService {
	
	@Autowired
    protected CodeDao codeDao;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public List<?> getCodeListByCD(Code code) throws DataAccessException {
		List<?> codeList = null;
		codeList = codeDao.getCodeListByCD(code);
		logger.info("getCodeListByCD {}", codeList.size());		
		return codeList;
	}
	
	@Override
	public Integer getCodeCheck(Code code) throws DataAccessException {
		Integer cnt = 0;
		cnt = codeDao.getCodeCheck(code);
		logger.info("getCodeCheck {}", cnt);
		return cnt;
	}
	
	@Override
	public Code getCodeInfo(Code code) throws DataAccessException {
		Code codeInfo = null;
		codeInfo = codeDao.getCodeInfo(code);
		logger.info("codeInfo {}", codeInfo);
		return codeInfo;
	}

}
