package framework.util;

import framework.exception.ExceptionType;
import framework.exception.PresenceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by ucjung on 2017-06-05.
 */
public class JsonUtils {
    static final private ObjectMapper objectMapper = new ObjectMapper();

    static public <T> T readValue(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new PresenceException(ExceptionType.JSON_PARSE_EXCEPTION);
        }
    }

    static public <T> String writeValue(T object) {
        try {

            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new PresenceException(ExceptionType.JSON_PARSE_EXCEPTION);
        }
    }

    static public <T> T convertValue(Object object, Class<T> valueType) {
        return objectMapper.convertValue(object, valueType);
    }
}
