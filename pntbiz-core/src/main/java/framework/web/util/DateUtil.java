package framework.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 현재날짜
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String nDate = sdf.format(date);
		return nDate;
	}
	
	/**
	 * 현재날짜(포멧입력)
	 * @param format
	 * @return
	 */
	public static String getDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat (format);
		Date date = new Date();
		String nDate = sdf.format(date);
		return nDate;
	}
	
	/**
	 * string to timestamp default 'yyyyMMddHHmmss'
	 * @param str
	 * @return
	 */
	public static long str2timestamp(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date;
		long timestamp = 0;
		try {
			date = sdf.parse(str);
			timestamp = (long) date.getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
			timestamp = 0;
		}
		return timestamp;
	}
	
	/**
	 * string to timestamp
	 * @param str
	 * @param dateFormat
	 * @return
	 */
	public static long str2timestamp(String str, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date;
		long timestamp = 0;
		try {
			date = sdf.parse(str);
			timestamp = (long) date.getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
			timestamp = 0;
		}
		return timestamp;
	}
	
	/**
	 * 타임스탬프>스트링
	 * @param timestamp
	 * @return
	 */
	public static String timestamp2str(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(timestamp*1000);
		return str;
	}
	
	/**
	 * 타임스탬프>스트링(포멧입력)
	 * @param timestamp
	 * @param dateFormat
	 * @return
	 */
	public static String timestamp2str(long timestamp, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String str = sdf.format(timestamp*1000);
		return str;
	}
	
	/**
	 * String으로 된 데이트포멧을 변경(String포멧)할때 
	 * @param str
	 * @param format1
	 * @param format2
	 * @return
	 * @throws ParseException
	 * @ex changeStrDateFormat("20140429102521", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss") > "2014-04-29 10:25:21" 
	 */
	public static String changeStrDateFormat(String str, String format1, String format2) throws ParseException {
		// 원래
		SimpleDateFormat sdf1 = new SimpleDateFormat(format1);
		SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
		Date date = sdf1.parse(str);
		str = sdf2.format(date);
		return str;
	}
	
	/**
	 * date +- 날짜
	 * @param date
	 * @param day
	 * @return
	 */
	public static String getAddDate(String date, Integer day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date addDate= null;
		try {
			addDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(addDate);
		cal.add(Calendar.DATE, day);
		date = sdf.format(cal.getTime());
		return date;
	}
	
	/**
	 * date +- 날짜
	 * @param date
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getAddDate(String date, Integer day, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date addDate= null;
		try {
			addDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(addDate);
		cal.add(Calendar.DATE, day);
		date = sdf.format(cal.getTime());
		return date;
	}
	
	/**
	 * 두 날짜 사이의 날수 계산
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static int getBetweenDayCount(String sDate, String eDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return (int) ((sdf.parse(eDate).getTime() - sdf.parse(sDate)
					.getTime()) / 1000 / 60 / 60 / 24);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 두 날짜 사이의 날수 계산
	 * @param sDate
	 * @param eDate
	 * @return
	 */
	public static int getBetweenTimeCount(String sDate, String eDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			return (int) ((sdf.parse(eDate).getTime() - sdf.parse(sDate)
					.getTime()) / 1000);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 마이크로 타임 
	 * @return
	 */
	public static String getMicrotime() {
		long time = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		String mctime = sdf.format(new Date(time));
		return mctime;
	}
	
}