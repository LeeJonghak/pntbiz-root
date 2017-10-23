package framework.web.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

public class CommonUtil {
	
	public static String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		String bs = "";
		if(header.contains("MSIE")) {
			bs = "MSIE";
		} else if(header.contains("Chrome")) {
			bs = "Chrome";
		} else if(header.contains("Opera")) {
			bs = "Opera";
		} else {
			bs = "Firefox";
		}
		return bs;
	}

	public static String dump(Object object) {

        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append(object.getClass().getSimpleName()).append('{');

        boolean firstRound = true;

        for (Field field : fields) {
            if (!firstRound) {
                sb.append(", ");
            }
            firstRound = false;
            field.setAccessible(true);
            try {
                final Object fieldObj = field.get(object);
                final String value;
                if (null == fieldObj) {
                    value = "null";
                } else {
                    value = fieldObj.toString();
                }
                sb.append(field.getName()).append('=').append('\'')
                        .append(value).append('\'');
            } catch (IllegalAccessException ignore) {
                //this should never happen
            }

        }

        sb.append('}');
        return sb.toString();
    }
}
