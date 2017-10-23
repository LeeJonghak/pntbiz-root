package framework.influxdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.util.ClassUtil;
import framework.util.StringUtil;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ucjung on 2017-07-12.
 */
public class InfluxdbTemplate<T> implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private InfluxdbConnectionFactory connectionFactory;
    private InfluxdbConnectionProperites properites;

    public InfluxdbTemplate(InfluxdbConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.properites = connectionFactory.getProperites();
        if(!isExistDatabase()) createDatabase(null);
    }

    public void write(T t) {
        Class clazz = t.getClass();
        InfluxdbConfig influxdbConfig = (InfluxdbConfig) clazz.getAnnotation(InfluxdbConfig.class);

        String database = properites.getDatabase();
        String measurement = StringUtil.convertCamelToSeperatedString(clazz.getSimpleName());

        if (influxdbConfig != null) {
            if (!influxdbConfig.database().equals("")) database = influxdbConfig.database();
            if (!influxdbConfig.measurement().equals("")) measurement = influxdbConfig.measurement() ;
        }

        Point.Builder builder = Point.measurement(measurement);

        Field[] fields = clazz.getDeclaredFields();
        Map<String, Method> methods = ClassUtil.getDeclaredMethods(clazz);

        for (Field field:fields) {
            String fieldName = field.getName();
            String keyName = StringUtil.convertCamelToSeperatedString(fieldName);
            String keyValue = "";
            try {
                keyValue = methods.get("get" + StringUtils.capitalize(fieldName)).invoke(t).toString();
            } catch (Exception e) {
                logger.warn("Can not get property value : {}", fieldName);
                continue;
            }

            InfluxdbField influxdbField = field.getAnnotation(InfluxdbField.class);

            if (!influxdbField.name().equals("")) {
                keyName = influxdbField.name();
            }

            if (influxdbField.isTag() == true) {
                builder.tag(keyName, keyValue);
            }

            if (influxdbField.isField() == true) {
                builder.addField(keyName, keyValue);
            }
        }
        this.write(builder.build(), database);
    }

    public void write(InfluxdbWriteParam params) {
        Point point = Point.measurement(params.getMesearment())
                .tag(params.getTags())
                .fields(params.getFields())
                .build();

        if (params.getDatabase() == null) {
            write(point);
        } else {
            write(point, params.getDatabase());
        }
    }

    public void write(Point point) {
        write(point, properites.getDatabase());
    }

    public void write(Point point, String database) {
        write(point, database, getDefaultRetentionPolicy());
    }

    public void write(Point point, String database, String retentionPolicy) {
        try {
            connectionFactory.getConnection().write(
                    database,
                    retentionPolicy,
                    point
            );
        } catch (Exception ex) {
            if (isDatabaseNotFound(ex.getMessage().toString()) == true) {
                createDatabase(database);
                write(point, database, retentionPolicy);
                logger.info("create database {}", database);
            } else {
                logger.error(ex.getMessage());
            }
        }
    }

    private boolean isDatabaseNotFound(String message) {
        HashMap<String,Object> result;
        try {
            if ((new ObjectMapper().readValue(message, HashMap.class)).get("error").toString().startsWith("database not found") == true) {
                return true;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    public void write(List<Point> points) {
        final BatchPoints.Builder batchPointsBuilder = BatchPoints
                .database(properites.getDatabase())
                .retentionPolicy(getDefaultRetentionPolicy())
                .consistency(InfluxDB.ConsistencyLevel.ALL);

        writeBatch(points, batchPointsBuilder);
    }

    public void write(List<Point> points, String database) {
        final BatchPoints.Builder batchPointsBuilder = BatchPoints
                .database(database)
                .retentionPolicy(getDefaultRetentionPolicy())
                .consistency(InfluxDB.ConsistencyLevel.ALL);

        writeBatch(points, batchPointsBuilder);
    }

    public void write(List<Point> points, String database, String retentionPolicy) {
        final BatchPoints.Builder batchPointsBuilder = BatchPoints
                .database(database)
                .retentionPolicy(retentionPolicy)
                .consistency(InfluxDB.ConsistencyLevel.ALL);

        writeBatch(points, batchPointsBuilder);
    }

    private void writeBatch(List<Point> points, BatchPoints.Builder batchPointsBuilder) {
        for (Point point:points) {
            batchPointsBuilder.point(point);
        }
        connectionFactory.getConnection().write(batchPointsBuilder.build());
    }

    public QueryResult query(final String query)
    {
        return query(query, properites.getDatabase());
    }

    public QueryResult query(final String query, final String database)
    {
        return connectionFactory.getConnection().query(new Query(query, database));
    }

    public QueryResult query(final String query, final TimeUnit timeUnit)
    {
        return query(query, properites.getDatabase(), timeUnit);
    }

    public QueryResult query(final String query, String database, final TimeUnit timeUnit)
    {
        return connectionFactory.getConnection().query(new Query(query, database), timeUnit);
    }

    public List<T> query(final String query, Class returnType) {
        return query(query, properites.getDatabase(), returnType);
    }

    public List<T> query(final String query, String database, Class returnType) {
        QueryResult queryResult = query(query, database);
        return null;
    }

    public List<T> query(final String query, final TimeUnit timeUnit, Class returnType)
    {
        return query(query,properites.getDatabase(),timeUnit,returnType);
    }

    public List<T> query(final String query, final String database, final TimeUnit timeUnit, Class returnType)
    {
        QueryResult queryResult = query(query, database, timeUnit);
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    private boolean isExistDatabase() {
        Query query = new Query("SHOW DATABASES", properites.getDatabase());
        QueryResult queryResult = connectionFactory.getConnection().query(query);
        boolean isExist = false;

        //result.getResults()
        if (queryResult.getResults().size() > 0) {
            QueryResult.Result results = queryResult.getResults().get(0);

            if (results.getSeries().size() > 0) {
                QueryResult.Series serise = results.getSeries().get(0);
                List<List<Object>> values = serise.getValues();

                for (List<Object> value : values) {
                    if (value.get(0).equals(properites.getDatabase())) {
                        isExist = true;
                        break;
                    }
                }
            }
        }

        return isExist;
    }

    private void createDatabase(String database) {
        if (database == null)
            connectionFactory.getConnection().createDatabase(properites.getDatabase());
        else
            connectionFactory.getConnection().createDatabase(database);
    }

    private String getDefaultRetentionPolicy() {

        if (connectionFactory.getConnection().version().startsWith("0.") ) {
            return "default";
        } else {
            return "autogen";
        }
    }
}
