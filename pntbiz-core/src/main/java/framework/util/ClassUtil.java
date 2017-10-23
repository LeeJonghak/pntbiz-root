package framework.util;

import framework.exception.BaseException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassUtil {

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(orig, dest);
        } catch (Exception e) {
            throw new BaseException("500", "Internal Error copyProperties");
        }
    }

    public static Map<String, Method> getDeclaredMethods(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> result = new HashMap<>();
        for (Method method :
                methods) {
            result.put(method.getName(), method);
        }
        return result;
    }

    public static Map<String, Method> getDeclaredMethodForName(Class clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, Method> result = new HashMap<>();
        for (Method method :
                methods) {
            if (method.getName().equals(methodName))
                result.put(method.getName(), method);
        }
        return result;
    }
}