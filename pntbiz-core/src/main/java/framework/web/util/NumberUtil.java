package framework.web.util;

import java.util.regex.Pattern;

public class NumberUtil {
	
	public static boolean isNumeric(String num) {
		Pattern pattern = Pattern.compile("[+-]?\\d+"); 
	    return pattern.matcher(num).matches(); 
	}
	
	public static double getDouble(Number num, double defaultValue) {
		if (num == null) {
			return defaultValue;
		}
		return num.doubleValue();
	}
	public static double getDouble(Number num) {
		return getDouble(num, 0);
	}
	public static double getDouble(String value) {
		return Double.parseDouble(value);
	}
	
	public static long getLong(Number num, long defaultValue) {
		if (num == null) {
			return defaultValue;
		}
		return num.longValue();
	}
	public static long getLong(Number num) {
		return getLong(num, 0);
	}
	public static long getLong(String value) {
		return Long.parseLong(value);
	}
	
	public static int getInt(Number num, int defaultValue) {
		if (num == null) {
			return defaultValue;
		}
		return num.intValue();
	}
	public static int getInt(Number num) {
		return getInt(num, 0);
	}
	public static int getInt(String value) {
		return Integer.parseInt(value);
	}	
	public static float getFloat(Number num, float defaultValue) {
		if (num == null) {
			return defaultValue;
		}
		return num.floatValue();
	}
	public static float getFloat(Number num) {
		return getFloat(num, 0);
	}
	public static float getFloat(String value) {
		return Float.parseFloat(value);
	}
}
