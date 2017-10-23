package framework.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Autowire Candidate Resolver 설정
 * 여러 구현체중 어떤 구현체를 Autowired 할지 결정할 Autowire Candidate Resolver를 설정한다.
 *
 * Created by ucjung on 2017-05-15.
 */
public class AutowireCandidateResolverConfigurer implements BeanFactoryPostProcessor {

    static final private Logger logger = LoggerFactory.getLogger(AutowireCandidateResolverConfigurer.class);

    private String siteShortName;
    private CustomAutowiredCandidateResolver autowireCandidateResolver;

    public AutowireCandidateResolverConfigurer(String siteShortName, CustomAutowiredCandidateResolver autowireCandidateResolver) {
        this.siteShortName = siteShortName;
        this.autowireCandidateResolver = autowireCandidateResolver;
        autowireCandidateResolver.setSiteShortName(siteShortName);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        defaultListableBeanFactory.setAutowireCandidateResolver(autowireCandidateResolver);
    }

}
