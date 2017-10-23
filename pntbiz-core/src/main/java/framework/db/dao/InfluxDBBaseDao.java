package framework.db.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import framework.web.util.StringUtil;

public class InfluxDBBaseDao {

	private @Value("#{config['influxdb.url']}") String _db_con_info;
	private @Value("#{config['influxdb.name']}") String _db_name;
	private InfluxDB connection;
    private Logger logger = LoggerFactory.getLogger(getClass());

	public InfluxDB getConnection(){
	    if (connection == null){
			connection = InfluxDBFactory.connect(_db_con_info);
	    }
	    return connection;
	}

	private Query setQuery(String query){
		return new Query(query, _db_name);
	}

	protected List<?> selectList(String query){
		QueryResult queryResult = getConnection().query(setQuery(query));
		return makeResult(queryResult);
	}

	protected void insert(Map<String, Object> fields, String tableName){
		Builder builder = Point.measurement(tableName);
		builder.fields(fields);
		Point point = builder.build();
		logger.debug(point.toString());

		getConnection().write(_db_name, "autogen", point);
	}

	protected String selectQuery(influxQueryParam queryParam){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM ");
		sb.append(queryParam.getTableName());

		StringBuffer searchSb = new StringBuffer();

		if(queryParam.getSearchParam() != null){
			Map<Object, Object> searchParam = obj2map(queryParam.getSearchParam());

			int idx = 0;
			Iterator it = searchParam.entrySet().iterator();
			String key = "";
			String searchOpt = "", searchKey = "";

			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();

				key = StringUtil.NVL((String)pair.getKey());


				if(pair.getValue() == null)	continue;

				if(key.equals("keyword")){
					searchKey = (String) pair.getValue();
					continue;
				}
				if(key.equals("opt")){
					searchOpt = (String) pair.getValue();
					continue;
				}
				searchSb.append((idx == 0)? " WHERE ": " AND ");
				if(key.equals("sDate"))
					searchSb.append("time >= '").append(pair.getValue()).append("'");
				else if(key.equals("eDate"))
					searchSb.append("time <= '").append(pair.getValue()).append("'");
				else{
					if(pair.getValue() instanceof String)
						searchSb.append(key).append("='").append(pair.getValue()).append("'");
					else
						searchSb.append(key).append("=").append(pair.getValue());
				}

				++idx;
			}
			if(!searchKey.equals("") && !searchOpt.equals("")){
				searchSb.append((searchSb.toString().equals(""))? " WHERE ": " AND ");
				searchSb.append(searchOpt).append(" = '").append(searchKey).append("'");
			}
		}

		if(queryParam.getAddSarchParam() != null){
			searchSb.append((searchSb.toString().equals(""))? " WHERE ": " AND ");
			searchSb.append(queryParam.getAddSarchParam());
		}

		if(queryParam.getOrderBy() != null){
			searchSb.append(" ORDER BY ");
			searchSb.append(queryParam.getOrderBy());
		}

		if(queryParam.getLimit() > 0){
			searchSb.append(" LIMIT ");
			searchSb.append(queryParam.getLimit());
		}

		sb.append(searchSb.toString());
		System.out.println(sb.toString());
		return sb.toString();
	}

	private Map<Object, Object> obj2map(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<Object, Object> map = new HashMap<Object, Object>();

        for(int i=0; i<fields.length; i++) {
            fields[i].setAccessible(true);
            try {
            	map.put(fields[i].getName(), fields[i].get(obj));
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

	private List<?> makeResult(QueryResult queryResult){
		List<Map<String, Object>> rtnList = null ;
		if(queryResult.getResults() != null && queryResult.getResults().size() >0
				&& queryResult.getResults().get(0).getSeries() != null
				&& queryResult.getResults().get(0).getSeries().size() >0){

			Series series = queryResult.getResults().get(0).getSeries().get(0);
			List<String> columns = series.getColumns();
			if(columns != null && columns.size() > 0){
				rtnList	= new ArrayList<Map<String, Object>>();
				Map<String, Object> valueMap = null;
				int columnSize = columns.size();

				for(List<Object> value: series.getValues()){
					valueMap = new HashMap<String, Object>();
					for(int idx = 0; idx < columnSize; idx++){
						valueMap.put(columns.get(idx), value.get(idx));
					}

					rtnList.add(valueMap);
				}
			}
		}
		return rtnList;
	}

	public static class influxQueryParam {
	    private String tableName;
	    private Object searchParam;
	    private String addSarchParam;
	    private String orderBy;
	    private int limit;

		public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public Object getSearchParam() {
			return searchParam;
		}
		public void setSearchParam(Object searchParam) {
			this.searchParam = searchParam;
		}
		public String getAddSarchParam() {
			return addSarchParam;
		}
		public void setAddSarchParam(String addSarchParam) {
			this.addSarchParam = addSarchParam;
		}
		public String getOrderBy() {
			return orderBy;
		}
		public void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}
		public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
	}
}
