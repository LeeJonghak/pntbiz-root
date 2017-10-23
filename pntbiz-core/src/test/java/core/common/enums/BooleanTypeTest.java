package core.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.util.JsonUtils;
import framework.web.util.JsonUtil;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ucjung on 2017-09-08.
 */
public class BooleanTypeTest {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {


        TestClass testClass = new TestClass();

        String json = objectMapper.writeValueAsString(testClass);
        logger.error(json);

        TestClass testClass1 = objectMapper.readValue(json, TestClass.class);

    }

    static public class TestClass {
        private BooleanType temp = BooleanType.TRUE;

        public BooleanType getTemp() {
            return temp;
        }

        public void setTemp(BooleanType temp) {
            this.temp = temp;
        }

    }

    public static class BooleanTypeDeserializer extends JsonDeserializer<BooleanType> {

        @Override
        public BooleanType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return null;
        }
    }

}