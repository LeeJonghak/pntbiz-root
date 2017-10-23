package wms.info.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import framework.web.util.StringUtil;
import core.wms.info.dao.CodeActionDao;
import core.wms.info.domain.CodeAction;
import core.wms.info.domain.CodeActionSearchParam;

@Service
public class CodeActionServiceImpl implements CodeActionService {

	@Autowired
	private CodeActionDao codeActionDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CodeDao codeDao;
	
	@Override
	public Integer getCodeActionCount(CodeActionSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = codeActionDao.getCodeActionCount(param);
		logger.info("getCodeActionCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getCodeActionList(CodeActionSearchParam param) throws DataAccessException {
		List<?> codeActionList = null;
		codeActionList = codeActionDao.getCodeActionList(param);
		logger.info("getCodeActionList {}", codeActionList.size());
		return codeActionList;
	}
	
	@Override
	public List<?> getCodeActionListAll(CodeActionSearchParam param) throws DataAccessException {
		List<?> codeActionList = null;
		codeActionList = codeActionDao.getCodeActionListByAll(param);
		logger.info("getCodeActionListAll {}", codeActionList.size());
		return codeActionList;
	}
	
	@Override
	public List<?> bindCodeActionList(List<?> list) throws DataAccessException, ParseException {
		List<CodeAction> clist = new ArrayList<CodeAction>();
		Code code = new Code();
		code.setgCD("CODEACTTYPE");
		List<?> codeActCD = codeDao.getCodeListByCD(code);
		
		for(int i=0; i<list.size(); i++) {
			CodeAction codeAction = (CodeAction) list.get(i);		
			for(int j=0; j<codeActCD.size(); j++) {
				Code codeAct = (Code) codeActCD.get(j);
				if(codeAction.getCodeType().equals(codeAct.getsCD())) {
					codeAction.setCodeTypeText(codeAct.getsName());
				}
			}
			clist.add(codeAction);
		}
		return clist;
		
	}

	@Override
	public CodeAction getCodeActionInfo(CodeAction codeAction) 	throws DataAccessException {
		CodeAction codeActionInfo = null;
		codeActionInfo = codeActionDao.getCodeActionInfo(codeAction);
		logger.info("codeActionInfo {}", codeActionInfo);	
		return codeActionInfo;
	}
	
	@Override
	@Transactional
	public void registerCodeAction(HttpServletRequest request, CodeAction codeAction 
			) throws DataAccessException {
		codeAction.setCodeType(StringUtil.NVL(request.getParameter("codeType"), ""));
		codeAction.setCode(StringUtil.NVL(request.getParameter("code"), ""));
		codeAction.setCodeName(StringUtil.NVL(request.getParameter("codeName"), ""));
		codeActionDao.insertCodeAction(codeAction);
	}
	
	@Override
	@Transactional
	public void modifyCodeAction(HttpServletRequest request, CodeAction codeAction
			) throws DataAccessException {
		codeAction.setCodeType(StringUtil.NVL(request.getParameter("codeType"), ""));
		codeAction.setCode(StringUtil.NVL(request.getParameter("code"), ""));
		codeAction.setCodeName(StringUtil.NVL(request.getParameter("codeName"), ""));
		codeActionDao.updateCodeAction(codeAction);
	}
	
	@Override
	@Transactional
	public void removeCodeAction(CodeAction codeAction) throws DataAccessException {
		codeActionDao.deleteCodeAction(codeAction);
	}
}
