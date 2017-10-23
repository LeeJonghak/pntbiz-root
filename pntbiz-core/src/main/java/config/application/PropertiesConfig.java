package config.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Properties Loader Javaconfig
 *
 * 특정 Class Path의 Properties를 로딩한다.
 *
 * Created by ucjung on 2017-04-18.
 */
@Configuration
@Component
public class PropertiesConfig {
    static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);
    static final String propertyConfigPath = "classpath:config-*.xml";
    static final String databasePropertyConfigPrefix = "config-properties";

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Value("${DatabaseSelection:}")
    private String databaseSelection;

    @PostConstruct
    public void loadProperites() throws IOException {
        logger.info("#============================================> Database Selection : {}", databaseSelection);

        Resource[] locations = getResources(propertyConfigPath);

        String properitesFileName = databasePropertyConfigPrefix + (databaseSelection.equals("") ? "" : "-" + databaseSelection) + ".xml";

        logger.info("#============================================> [START] Properties Load ");
        for (Resource resource : locations) {
            String resourceFileName = resource.getFilename();

            // Database Selection 과 일치 하지 않은 속성은 제외함
            /*
            if (resourceFileName.startsWith(databasePropertyConfigPrefix) && !resourceFileName.startsWith(databasePropertyConfigPrefix + databaseSelection)) {
                continue;
            }
            */
            if (!resourceFileName.startsWith(properitesFileName))
                continue;

            Properties properties = new Properties();
            properties.loadFromXML(resource.getInputStream());

            applicationContext
                    .getEnvironment()
                    .getPropertySources()
                    .addLast(
                            new PropertiesPropertySource(
                                    getNameForResource(resource), properties));

            logger.info(getNameForResource(resource) + " / " + String.valueOf(resource.getURI()));
        }
        logger.info("#============================================> [END  ] Properties Load ");
    }

    private Resource[] getResources(String path) throws IOException {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] locations = patternResolver.getResources(path);
        Resource[] locationsResult = new Resource[locations.length];

        int idx = 0;

        for (Resource resource : locations) {
            String resourceFileName = resource.getFilename();

            // Database Selection 과 일치 하지 않은 속성은 제외함
            if (resourceFileName.startsWith(databasePropertyConfigPrefix) && !resourceFileName.startsWith(databasePropertyConfigPrefix + databaseSelection)) {
                continue;
            }

            locationsResult[idx] = resource;
            idx ++;
        }
        return Arrays.copyOfRange(locationsResult,0,idx);
    }


    private String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@"
                    + System.identityHashCode(resource);
        }
        return name;
    }

    @Bean
    PropertiesFactoryBean config() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocations(getResources(propertyConfigPath));
        return propertiesFactoryBean;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
