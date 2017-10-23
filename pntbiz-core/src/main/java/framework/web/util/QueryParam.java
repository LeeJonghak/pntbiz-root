package framework.web.util;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class QueryParam {

    private Map<String, Object> param = new HashMap<String, Object>();

    static public QueryParam create() {
        return new QueryParam();
    }

    static public QueryParam create(String column, Object value) {
        return QueryParam.create().put(column, value);
    }

    public QueryParam put(String column, Object value) {
        this.param.put(column, value);
        return this;
    }

    public Map<String, Object> build() {
        return this.param;

    }

}
