package wms.info.service;

import core.wms.info.domain.CodeAction;
import core.wms.info.domain.CodeActionSearchParam;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;

public interface CodeActionService {
	public Integer getCodeActionCount(CodeActionSearchParam param) throws DataAccessException;
	public List<?> getCodeActionList(CodeActionSearchParam param) throws DataAccessException;
	public List<?> getCodeActionListAll(CodeActionSearchParam param) throws DataAccessException;
	public List<?> bindCodeActionList(List<?> list) throws DataAccessException, ParseException;
	
	public CodeAction getCodeActionInfo(CodeAction codeAction) throws DataAccessException;
	
	public void registerCodeAction(HttpServletRequest request, CodeAction codeAction) throws DataAccessException;
	public void modifyCodeAction(HttpServletRequest request, CodeAction codeAction) throws DataAccessException;
	public void removeCodeAction(CodeAction codeAction) throws DataAccessException;	
}