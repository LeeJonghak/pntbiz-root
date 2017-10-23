package core.common.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * Created by ucjung on 2017-09-08.
 */
public class BooleanTypeDeserializer extends  JsonDeserializer<BooleanType> {

    @Override
    public BooleanType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.readValueAsTree();
        return (jsonNode.toString().equals("true")) ? BooleanType.TRUE : BooleanType.FALSE;
    }
}
