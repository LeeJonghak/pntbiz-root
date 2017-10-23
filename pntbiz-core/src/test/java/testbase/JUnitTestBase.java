package testbase;

import config.application.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ApplicationConfig.class
})
public class JUnitTestBase {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void Test() {
    }
}
