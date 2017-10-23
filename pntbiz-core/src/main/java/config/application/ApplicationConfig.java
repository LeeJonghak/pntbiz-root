package config.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ucjung on 2017-04-18.
 */
@Configuration
@Import({
        PropertiesConfig.class,
        DatabaseConfig.class,
        RedisConfig.class,
        MailConfig.class
})
@ComponentScan(basePackages = {
        "framework.*",
        "core.api",
		"core.common.beacon*"
})
public class ApplicationConfig {

}