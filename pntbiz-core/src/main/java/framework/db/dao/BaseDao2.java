package framework.db.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BaseDao2 {
	
	public SqlSessionTemplate sqlSessionTemplate2;
    
    @Autowired
    public void setSqlSessionTemplate2(SqlSessionTemplate sqlSessionTemplate2) {
    	this.sqlSessionTemplate2 = sqlSessionTemplate2;    	
    }
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 상세조회
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public Object select(String sqlId, Object param) {
		logger.debug("select {}", sqlId);
		return sqlSessionTemplate2.selectOne(sqlId, param);
	}

	public Object select(String sqlId) {
		return select(sqlId, null);
	}
	
	/**
	 * 목록조회
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public List<?> list(String sqlId, Object param) {
		return sqlSessionTemplate2.selectList(sqlId, param);
	}

	public List<?> list(String sqlId) {
		return list(sqlId, null);
	}
	
	/**
	 * 등록
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public Object insert(String sqlId, Object param) {
		return sqlSessionTemplate2.insert(sqlId, param);
	}

	public Object insert(String sqlId) {
		return insert(sqlId, null);
	}
	
	/**
	 * 수정
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public int update(String sqlId, Object param) {
		return sqlSessionTemplate2.update(sqlId, param);
	}

	public int update(String sqlId) {
		return update(sqlId, null);
	}
	
	/**
	 * 삭제
	 * @param sqlId
	 * @param param
	 * @return
	 */
	public int delete(String sqlId, Object param) {
		return sqlSessionTemplate2.delete(sqlId, param);
	}

	public int delete(String sqlId) {
		return delete(sqlId, null);
	}
	
}
