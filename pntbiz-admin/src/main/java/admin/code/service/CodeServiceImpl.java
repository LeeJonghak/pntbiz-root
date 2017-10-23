package admin.code.service;

import java.util.List;

import core.common.code.dao.CodeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.common.code.domain.Code;
import core.common.code.domain.CodeSearchParam;

@Service
public class CodeServiceImpl implements CodeService {
	
	@Autowired
	private CodeDao codeDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Integer getCodeCount(CodeSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = codeDao.getCodeCount(param);
		logger.info("getCodeCount {}", cnt);		
		return cnt;
	}
	
	@Override
	public List<?> getCodeList(CodeSearchParam param) throws DataAccessException {
		List<?> codeList = null;
		codeList = codeDao.getCodeList(param);
		logger.info("getCodeList {}", codeList.size());		
		return codeList;
	}
	
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

	@Override
	@Transactional
	public void registerCode(Code code) throws DataAccessException {
		codeDao.insertCode(code);
	}

	@Override
	@Transactional
	public void modifyCode(Code code) throws DataAccessException {
		codeDao.updateCode(code);
	}

	@Override
	@Transactional
	public void removeCode(Code code) throws DataAccessException {
		codeDao.deleteCode(code);
	}

}
