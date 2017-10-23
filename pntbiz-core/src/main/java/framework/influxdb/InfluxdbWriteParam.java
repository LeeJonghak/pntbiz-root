package framework.influxdb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-07-12.
 */
public class InfluxdbWriteParam {
    private String database;
    private String mesearment;
    private Map<String, String> tags = new HashMap<>();
    private Map<String, Object> fields = new HashMap<>();

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getMesearment() {
        return mesearment;
    }

    public void setMesearment(String mesearment) {
        this.mesearment = mesearment;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public void setTag(String name, String value) {
        tags.put(name, value);
    }

    public void removeTag(String name) {
        tags.remove(name);
    }

    public void setField(String name, Object value) {
        fields.put(name, value);
    }

    public void  removeField(String name) {
        fields.remove(name);
    }
}
