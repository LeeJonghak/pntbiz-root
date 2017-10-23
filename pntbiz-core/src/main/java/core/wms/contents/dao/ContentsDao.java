package core.wms.contents.dao;

import java.util.List;

import core.wms.contents.domain.Contents;
import core.wms.contents.domain.ContentsMapping;
import core.wms.contents.domain.ContentsSearchParam;
import core.wms.contents.domain.ContentsTypeSearchParam;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.contents.domain.ContentsMappingSearchParam;
import core.wms.contents.domain.ContentsType;
import core.wms.contents.domain.ContentsTypeComponent;
import framework.db.dao.BaseDao;

@Repository
public class ContentsDao extends BaseDao {
	
	public Integer getContentsCount(ContentsSearchParam param) throws DataAccessException {
		return (Integer) select("getContentsCount", param);
	}
	
	public List<?> getContentsList(ContentsSearchParam param) throws DataAccessException {
		return (List<?>) list("getContentsList", param);
	}
	
	public Contents getContentsInfo(Contents contents) throws DataAccessException {
		return (Contents) select("getContentsInfo", contents);
	}
	
	public Integer insertContents(Contents contents) throws DataAccessException {
		insert("insertContents", contents);
		return contents.getConNum();
	}
	
	public void insertContentsDetail(Contents contents) throws DataAccessException {
		insert("insertContentsDetail", contents);
	}
	
	public void updateContents(Contents contents) throws DataAccessException {
		update("updateContents", contents);
	}
	
	public void updateContentsDetail(Contents contents) throws DataAccessException {
		update("updateContentsDetail", contents);
	}
	
	public void updateContentsBlankFile(Contents contents) throws DataAccessException {
		update("updateContentsBlankFile", contents);
	}
	
	public void deleteContents(Contents contents) throws DataAccessException {
		delete("deleteContents", contents);
	}
	
	public Integer getContentsMappingCount(ContentsMappingSearchParam param) throws DataAccessException {
		return (Integer) select("getContentsMappingCount", param);
	}
	
	public List<?> getContentsMappingList(ContentsMappingSearchParam param) throws DataAccessException {
		return (List<?>) list("getContentsMappingList", param);
	}
	
    public void insertContentsMapping(ContentsMapping vo) throws DataAccessException {
    	insert("insertContentsMapping", vo);
    }
    
    public void deleteContentsMapping(ContentsMapping vo) throws DataAccessException {
    	delete("deleteContentsMapping", vo);
    }
    
    public void modifyContentsMapping(ContentsMapping vo) throws DataAccessException {
    	update("modifyContentsMapping", vo);
    }
    
    public ContentsType getContentsTypeInfo(ContentsType contentsType) throws DataAccessException {
		return (ContentsType) select("getContentsTypeInfo", contentsType);
	}
    
    public Integer getContentsTypeCount(ContentsTypeSearchParam param) throws DataAccessException {
		return (Integer) select("getContentsTypeCount", param);
	}
	
	public List<?> getContentsTypeList(ContentsTypeSearchParam param) throws DataAccessException {
		return (List<?>) list("getContentsTypeList", param);
	}
    
    public Integer insertContentsType(ContentsType contentsType) throws DataAccessException {
    	insert("insertContentsType", contentsType);
        return contentsType.getTypeNum();
    }
    
    public void updateContentsType(ContentsType contentsType) throws DataAccessException {
		update("updateContentsType", contentsType);
	}
    
    public void deleteContentsType(ContentsType contentsType) throws DataAccessException {
		delete("deleteContentsType", contentsType);
	}
    
    public List<?> getContentsTypeComponentList(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
		return (List<?>) list("getContentsTypeComponentList", contentsTypeComponent);
	}
    
    public void insertContentsTypeComponent(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
    	insert("insertContentsTypeComponent", contentsTypeComponent);
    }
    
    public void updateContentsTypeComponent(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
    	update("updateContentsTypeComponent", contentsTypeComponent);
    }
    
    public void deleteContentsTypeComponent(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
    	delete("deleteContentsTypeComponent", contentsTypeComponent);
    }
    
    public void deleteContentsTypeComponentAll(ContentsTypeComponent contentsTypeComponent) throws DataAccessException {
    	delete("deleteContentsTypeComponentAll", contentsTypeComponent);
    }

}
