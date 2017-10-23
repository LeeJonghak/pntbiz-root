package core.common.code.dao;
import java.util.List;

import core.common.code.domain.Code;
import core.common.code.domain.CodeSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class CodeDao extends BaseDao {

	public Integer getCodeCount(CodeSearchParam param) throws DataAccessException {
		return (Integer) select("getCodeCount", param);
	}
	public List<Code> getCodeList(CodeSearchParam param) throws DataAccessException {
		return (List<Code>) list("getCodeList", param);
	}
	public List<Code> getCodeListByCD(Code code) throws DataAccessException {
		return (List<Code>) list("getCodeListByCD", code);
	}
	public Integer getCodeCheck(Code code) throws DataAccessException {
		return (Integer) select("getCodeCheck", code);
	}
	public Code getCodeInfo(Code code) throws DataAccessException {
		return (Code) select("getCodeInfo", code);
	}
	public void insertCode(Code code) throws DataAccessException {
		insert("insertCode", code);
	}
	public void updateCode(Code code) throws DataAccessException {
		update("updateCode", code);
	}
	public void deleteCode(Code code) throws DataAccessException {
		delete("deleteCode", code);
	}

}