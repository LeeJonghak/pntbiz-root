package framework.influxdb;

import config.application.ApplicationConfig;
import core.api.presence.domain.PresenceRequestParam;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.influxdb.dto.QueryResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * InfluxDb Connection Factory Test Case
 * Created by ucjung on 2017-07-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-influxdb-config.xml")
public class InfluxdbTest<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private InfluxdbTemplate template;

    @Before
    public void before() {

    }

    @Test
    public void writeLocationLog() throws InterruptedException {

        Random random = new Random();

        List<InfluxdbWriteParam> points = new ArrayList<>();
        for (int i = 0 ; i < 200 ; i ++) {
            Integer floor = random.nextInt(5);
            Integer id = random.nextInt(10);

            InfluxdbWriteParam param = new InfluxdbWriteParam();

            param.setMesearment("location_log");
            param.setTag("uuid", id.toString());
            param.setTag("floor", floor.toString());
            param.setField("beaconName", id.toString());
            param.setField("floorName", floor.toString() + "층");
            param.setField("lat", 0.01);
            param.setField("lng", 0.02);
            param.setField("distance",1.2);
            param.setField("battyLevel",20);

            template.write(param);
            Thread.sleep(300);
        }
    }

    @Test
    public void writeFloorLog() throws InterruptedException {

        Random random = new Random();

        List<InfluxdbWriteParam> points = new ArrayList<>();
        for (int i = 0 ; i < 200 ; i ++) {
            Integer floor = random.nextInt(5);
            Integer id = random.nextInt(10);

            InfluxdbWriteParam param = new InfluxdbWriteParam();

            param.setDatabase("indoor_statistics_222222");
            param.setMesearment("floor_log");
            param.setTag("uuid", id.toString());
            param.setTag("floor", floor.toString());
            param.setField("beaconName", id.toString());
            param.setField("floorName", floor.toString() + "층");
            param.setField("lat", 0.01);
            param.setField("lng", 0.02);
            param.setField("distance",1.2);
            param.setField("battyLevel",20);

            template.write(param);
            Thread.sleep(10);
        }
    }


    @Test
    public void query() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT * FROM /beacon*/ GROUP BY SUUID,id");
        QueryResult queryResult = template.query(stringBuilder.toString());

        List<Map<String, Object>> result1 = (List<Map<String, Object>>) influxdbResultConvert(queryResult.getResults(), Map.class);
        List<PresenceRequestParam> result2 = (List<PresenceRequestParam>) influxdbResultConvert(queryResult.getResults(), PresenceRequestParam.class);
    }

    private List<T> influxdbResultConvert(List<QueryResult.Result> results, Class rowType) {
        List<Map<String, Object>> converResult = new ArrayList<>();
        for (QueryResult.Result result : results) {
            List<QueryResult.Series> serieses = result.getSeries();
            if (serieses == null) continue;
            influxdbSeriseParsing(result.getSeries(), (List<T>) converResult, rowType);
            result.getSeries();
        }

        return (List<T>) converResult;
    }

    private void influxdbSeriseParsing(List<QueryResult.Series> serieses, List<T> converResult, Class rowType) {
        for (QueryResult.Series series : serieses) {
            influxdbDataParsing(series.getColumns(), series.getValues(), converResult, rowType);
        }
    }

    private void influxdbDataParsing(List<String> columns, List<List<Object>> rows, List<T> converResult, Class rowType) {
        for( int rowsIndex = 0 ; rowsIndex < rows.size() ; rowsIndex ++) {

            if (rowType.isAssignableFrom(Map.class)) {
                Map<String, Object> row = new HashMap<>();

                for( int columnIndex = 0 ; columnIndex < columns.size() ; columnIndex ++) {
                    row.put(columns.get(columnIndex), rows.get(rowsIndex).get(columnIndex));
                    logger.info("{} : {}", columns.get(columnIndex), rows.get(rowsIndex).get(columnIndex));
                }
                converResult.add((T) row);
            } else {

                try {
                    Object row = rowType.getConstructor().newInstance();
                    Method[] methods = rowType.getDeclaredMethods();

                    for( int columnIndex = 0 ; columnIndex < columns.size() ; columnIndex ++) {
                        String columnName = columns.get(columnIndex);
                        Object value = rows.get(rowsIndex).get(columnIndex);
                        for(Method method : methods) {
                            if (method.getName().equals("set" + StringUtils.capitalize(columnName))) {
                                method.invoke(row, value);
                            }
                        }
                    }
                    converResult.add((T) row);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}