package framework.influxdb;

/**
 * Created by ucjung on 2017-07-26.
 */
@InfluxdbConfig(
        database="log",
        measurement = "influxdb_test_domain"
)
public class InfluxdbTestDomain {
    @InfluxdbField(isTag = true)
    private String id;

    @InfluxdbField
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
