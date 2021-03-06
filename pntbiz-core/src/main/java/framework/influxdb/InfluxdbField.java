package framework.influxdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * Created by ucjung on 2017-07-26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InfluxdbField {
    public String name() default  "";
    public boolean isTag() default false;
    public boolean isField() default true;
}
