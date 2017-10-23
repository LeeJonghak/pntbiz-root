package framework.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by ucjung on 2017-07-12.
 */
public class InfluxdbConnectionFactory implements InitializingBean {
    private InfluxDB connection;

    private InfluxdbConnectionProperites properites;

    public InfluxdbConnectionFactory(InfluxdbConnectionProperites properites) {
        this.properites = properites;
    }

    public InfluxDB getConnection() {
        if (this.connection == null) {
            if (properites.getUsername() == null || properites.getPassword() == null)
                connection = InfluxDBFactory.connect(properites.getUrl());
            else
                connection = InfluxDBFactory.connect(
                        properites.getUrl(),
                        properites.getUsername(),
                        properites.getPassword()
                );
        }

        return connection;
    }

    public InfluxdbConnectionProperites getProperites() {
        return properites;
    }

    public void setProperites(InfluxdbConnectionProperites properites) {
        this.properites = properites;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
