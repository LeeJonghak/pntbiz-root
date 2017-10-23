package framework.web.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.StdSerializerProvider;

public class CustomObjectMapper extends ObjectMapper{
	public CustomObjectMapper(){
		StdSerializerProvider sp = new StdSerializerProvider();
		sp.setNullValueSerializer(new NullSerializer());
		this.setSerializerProvider(sp);
	}
}