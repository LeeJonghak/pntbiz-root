package framework.web.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class NullKeySerializer extends JsonSerializer<Object>{
	@Override
	public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) 
							throws IOException, JsonProcessingException{
		jsonGenerator.writeString("");
	}
}
