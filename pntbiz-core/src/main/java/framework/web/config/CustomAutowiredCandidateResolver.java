package framework.web.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * 다양한 사이트 요구사항 수용을 위하여 Custom Autowire Candidate Resolver 작성
 * 사이트 약어에 따라 Autowire할 대상을 변경한다.
 *
 * customizing Class Package Path : custom.[siteShortName].[common Class Path].[siteShortName + common Class Name]
 * [ex] scanner.service.ScannerService에 대하여 siteShortName = 'Test'인 경우
 * ㄴ custom.Test.scanner.service.TestScannerServiceImpl 생성
 *
 * [ex] scanner.dao.ScannerDao에 대하여 siteShortName = 'Test' 인 경우
 * ㄴ custom.Test.ScannerDao 생성
 * ㄴ scanner.dao.ScannerDao를 상속하도록 설정한다.
 *
 * Created by ucjung on 2017-05-15.
 */
public class CustomAutowiredCandidateResolver extends ContextAnnotationAutowireCandidateResolver implements ApplicationContextAware {
    static final private Logger logger = LoggerFactory.getLogger(CustomAutowiredCandidateResolver.class);

    private ApplicationContext applicationContext = null;

    private @Value("#{config['site.shortName']?:''}") String siteShortName;

    static private String serviceImplementSurfix = "Impl";
    private CandidateReturnType returnType;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private enum CandidateReturnType  {
        IS_CANDIDATED(1),       // 후보선정
        ISNOT_CANDIDATED(2),    // 후보아님
        DELEGATE_SUPER(3);      // Super Class의 Method로 위임

        private int value;

        private CandidateReturnType(int value) {
            this.value = value;
        }

        public Object getValue() {
            switch (value) {
                case 1 : return true;
                case 2 : return false;
                default: return null;
            }
        }
    }

    @Override
    public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
        returnType = CandidateReturnType.DELEGATE_SUPER;
        Field descriptorField = descriptor.getField();

        if (isValidAutowiredDescriporField(descriptorField)) {
            returnType = isCandidate(bdHolder, descriptor, descriptorField);
        }

        return (returnType.getValue() != null) ? (boolean) returnType.getValue() : super.isAutowireCandidate(bdHolder, descriptor);
    }

    private CandidateReturnType isCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor, Field descriptorField) {
        Class fieldType = descriptor.getField().getType();
        Class clazz = getClassByName(bdHolder.getBeanDefinition().getBeanClassName());

        if (clazz.getAnnotation(Service.class) != null) {
            String fieldTypeName = fieldType.getSimpleName();
            String commonImplementName = fieldTypeName + serviceImplementSurfix;
            String siteImplementName = siteShortName + commonImplementName;
            String currentClassName = clazz.getSimpleName();

            returnType = candidateAurowiredAnnotaion(fieldTypeName, commonImplementName, siteImplementName, currentClassName);
        } else if (clazz.getAnnotation(Repository.class) != null) {
            String fieldTypeName = fieldType.getSimpleName();
            String commonImplementName = fieldTypeName;
            String siteImplementName = siteShortName + commonImplementName;
            String currentClassName = clazz.getSimpleName();

            returnType = candidateAurowiredAnnotaion(fieldTypeName, commonImplementName, siteImplementName, currentClassName);
        }

        return returnType;
    }

    /**
     * Annotation Class Candidate
     *
     * currentClassName에 대하여 FieldTypeName에 대한 Autowired Injection 설정여부에 대한 처리를 한다.
     *
     * @param fieldTypeName         Autowired Annotation이 붙은 필드의 Class Type
     * @param commonImplementName   fieldTypeName에 대응하는 공통 Implement Class Name
     * @param siteImplementName     fieldTypeName에 대응하는 사이트 Implement Class Name
     * @param currentClassName      현재 처리 중인 Implement Class Name
     * @return
     */
    private CandidateReturnType candidateAurowiredAnnotaion(String fieldTypeName, String commonImplementName, String siteImplementName, String currentClassName) {
        if ( siteShortName.equals("") ) {
            if (currentClassName.equals(commonImplementName)) {
                logger.debug("===============================> {} is AutowireCandidated : {}", fieldTypeName, currentClassName);
                returnType = CandidateReturnType.IS_CANDIDATED;
            } else {
                logger.debug("===============================> {} is not AutowireCandidated : {}", fieldTypeName, currentClassName);
                returnType = CandidateReturnType.ISNOT_CANDIDATED;
            }
        } else {
            if (currentClassName.equals(commonImplementName)) {
                if (getBeanByName(siteImplementName) != null) {
                    logger.debug("===============================> {} is not AutowireCandidated : {}", fieldTypeName, currentClassName);
                    returnType = CandidateReturnType.ISNOT_CANDIDATED;
                }
            } else if (currentClassName.equals(siteImplementName)) {
                logger.debug("===============================> {} is AutowireCandidated : {}", fieldTypeName, currentClassName);
                returnType = CandidateReturnType.IS_CANDIDATED;
            } else {
                returnType = CandidateReturnType.ISNOT_CANDIDATED;
            }
        }
        return returnType;
    }

    private boolean isValidAutowiredDescriporField(Field descriptorField) {
        boolean result = true;
        if (descriptorField ==  null) {
            return false;
        } else if (descriptorField.getAnnotation(Autowired.class) == null){
            return false;
        }
        return result;
    }

    private Class getClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Object getBeanByName(String className) {
        Object object = null;
        try{
            object = applicationContext.getBean(StringUtils.uncapitalize(className));
        } catch (NoSuchBeanDefinitionException ex) {
        }

        try{
            if (object == null)
                object = applicationContext.getBean(className);
        } catch (NoSuchBeanDefinitionException ex) {
        }

        return object;
    }

    public void setSiteShortName(String siteShortName) {
        this.siteShortName = (siteShortName == null) ? "" : siteShortName;
    }

}
