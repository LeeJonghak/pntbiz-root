package config.application;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by ucjung on 2017-04-18.
 */
@Configuration
@EnableTransactionManagement
@Component
public class DatabaseConfig {

    static final private Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${DatabaseSelection:mysql}")
    private String databaseSelection;

    private @Value("${db.driver}") String driverClassName;
    private @Value("${db.url}") String url;
    private @Value("${db.id}") String userName;
    private @Value("${db.passwd}") String passWord;
    private @Value("${db.maxactive}") int maxActive;
    private @Value("${db.maxidle}") int maxIdle;
    private @Value("${db.maxwait}") int maxWait;
    private @Value("${db.autocommit}") boolean defaultAutoCommit;

    final private PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);

        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setMapperLocations(getMapperResources(pathMatchingResourcePatternResolver, "src/main/resources/mybatis/mapper/api/%s/**/*.xml"));
		sqlSessionFactoryBean.setMapperLocations(getMapperResources(pathMatchingResourcePatternResolver, "src/main/resources/mybatis/mapper/common/%s/**/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(getConfigLocation());

        return sqlSessionFactoryBean;
    }

    private Resource getConfigLocation() throws IOException {
        logger.info("#============================================> [START] Database Mybatis Config Resource Load ");

        Resource resource = pathMatchingResourcePatternResolver.getResource("classpath:mybatis/mybatis-config.xml");
        logger.info(resource.getURI() + " / " + resource.getFilename());

        logger.info("#============================================> [END  ] Database Mybatis Config Resource Load ");

        return resource;
    }

    private Resource[] getMapperResources(PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver, String path) throws IOException {

        logger.info("#============================================> [START] Database Mybatis Mapper Resource Load {}");
        Resource[] locations = pathMatchingResourcePatternResolver.getResources(String.format("classpath:"+path, databaseSelection));

        for (Resource resource : locations) {
            logger.info(resource.getURI() + " / " + resource.getFilename());
        }

        logger.info("#============================================> [END  ] Database Mybatis Mapper Resource Load ");

        return locations;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
