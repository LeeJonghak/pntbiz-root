package core.wms.info.dao;

import core.wms.info.domain.CodeAction;
import framework.db.dao.BaseDao;
import core.wms.info.domain.CodeActionMapping;
import core.wms.info.domain.CodeActionSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CodeActionDao extends BaseDao {
	
	public Integer getCodeActionCount(CodeActionSearchParam param) {
		return (Integer) select("getCodeActionCount", param);
    }
	
	public List<?> getCodeActionList(CodeActionSearchParam param) {
        List<?> list = this.list("getCodeActionList", param);
        return list;
    }
	
	public List<?> getCodeActionListByAll(CodeActionSearchParam param) {
        List<?> list = this.list("getCodeActionListByAll", param);
        return list;
    }
	
	public CodeAction getCodeActionInfo(CodeAction codeAction) throws DataAccessException {
		return (CodeAction) select("getCodeActionInfo", codeAction);
	}
	
	public void insertCodeAction(CodeAction codeAction) throws DataAccessException {
		insert("insertCodeAction", codeAction);
	}
	
	public void updateCodeAction(CodeAction codeAction) throws DataAccessException {
		update("updateCodeAction", codeAction);
	}
	
	public void deleteCodeAction(CodeAction codeAction) throws DataAccessException {
		delete("deleteCodeAction", codeAction);
	}

    public List<?> getCodeActionAll(HashMap<String, Object> param) throws DataAccessException {
        List<?> list = this.list("getCodeActionAll", param);
        return list;
    }

    public void insertCodeActionMapping(CodeActionMapping codeActionMapping) throws DataAccessException {
        this.insert("insertCodeActionMapping", codeActionMapping);

    }

    public void deleteCodeActionMapping(HashMap<String, Object> param) throws DataAccessException {
        this.delete("deleteCodeActionMapping", param);
    }

    public List<?> getMyGeofencingCodeActionList(HashMap<String, Object> param) throws DataAccessException {
        List<?> list = this.list("getMyGeofencingCodeActionList", param);
        return list;
    }

    public List<?> getMyBeaconCodeActionList(HashMap<String, Object> param) throws DataAccessException {
        List<?> list = this.list("getMyBeaconCodeActionList", param);
        return list;
    }
}
