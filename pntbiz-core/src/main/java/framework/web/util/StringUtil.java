package framework.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringUtil {
	/**
	 * 생성자 
	 */
	public StringUtil() {
	}


	public static String toKSC5601(String str) throws UnsupportedEncodingException 
	{
		if (str == null) 
		{
			return null;
		}
		else if(str.equals(""))
		{
			return "";	
		}
		return (new String(str.getBytes("8859_1"), "KSC5601"));
	}

	public static String toUTF8(String str) throws UnsupportedEncodingException 
	{
		if (str == null) 
		{
			return null;
		}
		else if(str.equals(""))
		{
			return "";	
		}
		return (new String(str.getBytes("8859_1"), "UTF-8"));
	}

	
	/**
	 * static variables for encoding/decoding
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String KOR_CHARSET = "EUC-KR";
	public static final String ENG_CHARSET = "ISO-8859-1";

	/**
	 * 영문을 한글로 Conversion해주는 프로그램.
	 * (8859_1 --> KSC5601)
	 * @param english 한글로 바꾸어질 영문 String
	 * @return 한글로 바꾸어진 String
	 */
	public static String E2K(String english) {
		if (english == null) {
			return null;
		}

		String korean = null;
		try {
			korean = new String(english.getBytes(ENG_CHARSET), KOR_CHARSET);
		} catch (UnsupportedEncodingException e) {
			korean = new String(english);
		}

		return korean;
	}

	/**
	 * 한글을 영문으로 Conversion해주는 프로그램.
	 * @param korean 영문으로 바꾸어질 한글 String
	 * @return 영문로 바꾸어진 String
	 */
	public static String K2E(String korean) {
		if (korean == null) {
			return null;
		}

		String english = null;
		try {
			english = new String(korean.getBytes(KOR_CHARSET), ENG_CHARSET);
		} catch (UnsupportedEncodingException e) {
			english = new String(korean);
		}

		return english;
	}

	/**
	 * URL encoding.
	 *
	 * @param s
	 * @return String
	 */
	public static String encodeURL(String s) {
		try {
			return URLEncoder.encode(s, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	/**
	 * URL decoding.
	 *
	 * @param s
	 * @return String
	 */
	public static String decodeURL(String s) {
		try {
			return URLDecoder.decode(s, DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	/**
	 * String을 읽어 알파벳과 숫자만 있는지 check ('_', '-', ' ' 포함)
	 * 
	 * @param s source String
	 */
	public static boolean isAlphaNum(String s) {
		int i = s.length();
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')
					&& (c < '0' || c > '9') && c != '_' && c != '-' && c != ' ')
				return false;
		}

		return true;
	}

	/**
	 * Alphabet 문자인지 체크
	 * 
	 * @param ch 체크할 문자
	 * @return Alphabet 문자이면 true, 그렇지 않으면 false
	 */
	public static boolean isAlpha(char ch) {
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z')
			return true;
		else
			return false;
	}

	/**
	 * 숫자 문자인지 체크
	 * 
	 * @param ch 체크할 문자
	 * @return 숫자 문자이면 true, 그렇지 않으면 false
	 */
	public static boolean isNumeric(char ch) {
		if (ch >= '0' && ch <= '9')
			return true;
		else
			return false;
	}

	/**
	 * obj 가 null 이면 true 리턴
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}

	/**
	 * str 가 null or empty 이면 true 리턴
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * String s에서 연속되는 space들을 하나로 압축한 String으로 return
	 * 
	 * @param s source String
	 */
	public static String normalizeWhitespace(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = s.length();
		boolean flag = false;
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			switch (c) {
			case 9: // '\t'
			case 10: // '\n'
			case 13: // '\r'
			case 32: // ' '
				if (!flag) {
					stringbuffer.append(' ');
					flag = true;
				}
				break;

			default:
				stringbuffer.append(c);
				flag = false;
				break;
			}
		}

		return stringbuffer.toString();
	}

	/**
	 * String s에서 character c가 몇 개가 있는지 return
	 * 
	 * @param s source String
	 * @param c 찾을 character
	 */
	public static int numOccurrences(String s, char c) {
		int i = 0;
		int j = 0;
		int l;
		for (int k = s.length(); j < k; j = l + 1) {
			l = s.indexOf(c, j);
			if (l < 0)
				break;
			i++;
		}

		return i;
	}

	/**
	 * String s에서 String s1에 포함되는 모든 char를 제거한 String으로 return
	 * 
	 * @param s source String
	 * @param s1 삭제시킬 sub String
	 */
	public static String removeCharacters(String s, String s1) {
		int i = s.length();
		StringBuffer stringbuffer = new StringBuffer(i);
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (s1.indexOf(c) == -1)
				stringbuffer.append(c);
		}

		return stringbuffer.toString();
	}

	/**
	 * String s에서 존재하는 space들을 모두 제거한 String으로 return
	 * 
	 * @param s source String
	 */
	public static String removeWhiteSpace(String s) {
		int i = s.length();
		StringBuffer stringbuffer = new StringBuffer(i);
		for (int j = 0; j < i; j++) {
			char c = s.charAt(j);
			if (!Character.isWhitespace(c))
				stringbuffer.append(c);
		}

		return stringbuffer.toString();
	}

	/**
	 * String target의 arguments[0],arguments[1]..부분을
	 * replacements[0],replacements[1]..으로 바꾸어 return
	 * 
	 * @param target source String
	 * @param arguments 바뀌어질 대상의 String 배열
	 * @param replacements 대체될 String 배열
	 */
	public static String replace(String target, String[] arguments,
			String[] replacements) {
		if (target == null || arguments == null || replacements == null)
			return target;

		for (int index = 0; index < arguments.length; index++) {
			target = replace(target, arguments[index], replacements[index]);
		}

		return target;
	}

	/**
	 * String target에 포함되어 있는 argument을 replacement로 바꾸어 return
	 * 
	 * @param target source String
	 * @param argument old String
	 * @param replacement new String
	 */
	public static String replace(String target, String argument,
			String replacement) {
		if (target == null || argument == null || replacement == null)
			return target;

		int i = target.indexOf(argument);

		if (i == -1)
			return target;

		StringBuffer targetSB = new StringBuffer(target);
		while (i != -1) {
			targetSB.delete(i, i + argument.length());
			targetSB.insert(i, replacement);
			//check for any more
			i = targetSB.toString().indexOf(argument, i + replacement.length());
		}

		return targetSB.toString();
	}

	/**
	 * String s에 있는 character c를 이용하여 String을 분리한다.
	 * 
	 * @param s source String
	 * @param c String s를 분리할 character
	 * @return 분리된 String 배열
	 */
	public static String[] splitStringAtCharacter(String s, char c) {
		String as[] = new String[numOccurrences(s, c) + 1];
		splitStringAtCharacter(s, c, as, 0);
		return as;
	}

	protected static int splitStringAtCharacter(String s, char c, String as[],
			int i) {
		int j = 0;
		int k = i;
		int l = 0;
		int j1;
		for (int i1 = s.length(); l <= i1 && k < as.length; l = j1 + 1) {
			j1 = s.indexOf(c, l);
			if (j1 < 0)
				j1 = i1;
			as[k] = s.substring(l, j1);
			j++;
			k++;
		}

		return j;
	}
	
	/**
	 * subString
	 * @param str
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static String getSubString(String str, int beginIdx, int endIdx) {
		String subStr = "";
		if(str.length() > (endIdx-1)) {
			subStr = str.substring(beginIdx, endIdx);
		}
		
		return subStr;
	}

	/**
	 * Convert a String to a boolean <p>대소문자 상관없이
	 * "true","yes","ok","okay","on","1"인 경우 true를 return한다.
	 * 
	 * @param data the thing to convert
	 * @return the converted data
	 */
	public static boolean S2B(String data) {
		if (data == null)
			return false;
		if (data.equalsIgnoreCase("true"))
			return true;
		if (data.equalsIgnoreCase("yes"))
			return true;
		if (data.equalsIgnoreCase("ok"))
			return true;
		if (data.equalsIgnoreCase("okay"))
			return true;
		if (data.equalsIgnoreCase("on"))
			return true;
		if (data.equalsIgnoreCase("1"))
			return true;

		return false;
	}

	/**
	 * Convert a String to an int
	 * 
	 * @param data the thing to convert
	 * @return the converted data
	 */
	public static int S2I(String data) {
		try {
			return Integer.parseInt(data);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	/**
	 * Convert a String to an int
	 * 
	 * @param data the thing to convert
	 * @return the converted data
	 */
	public static int S2I(String data, int base) {
		try {
			return Integer.parseInt(data);
		} catch (NumberFormatException ex) {
			return base;
		}
	}

	/**
	 * Convert a String to a Hashtable <p>"key1=value1 key2=value2 .... " 구조의
	 * string을 Hashtable로 변환
	 * 
	 * @param data the thing to convert
	 * @return the converted data
	 */
	public static Map<String, String> string2Hashtable(String data) {
		Map<String, String> commands = new HashMap<String, String>();

		data = normalizeWhitespace(data);
		String[] data_arr = splitStringAtCharacter(data, ' ');

		for (int i = 0; i < data_arr.length; i++) {
			int equ_pos = data_arr[i].indexOf('=');
			String key = data_arr[i].substring(0, equ_pos);
			String value = data_arr[i].substring(equ_pos + 1);

			commands.put(key, value);
		}

		return commands;
	}

	/**
	 * Convert a Hashtable to a Sting <p>"key1=value1 key2=value2 .... " 구조의
	 * string으로 변환
	 * 
	 * @param commands the thing to convert
	 * @return the converted data
	 */
	public static String hashtable2String(Map<?, ?> commands) {
		Iterator<?> it = commands.keySet().iterator();
		StringBuffer retcode = new StringBuffer();

		while (it.hasNext()) {
			String key = "";
			String value = "";

			try {
				key = (String)it.next();
				value = (String)commands.get(key);

				retcode.append(key);
				retcode.append("=");
				retcode.append(value);
				retcode.append(" ");
			} catch (ClassCastException ex) {}
		}

		return retcode.toString().trim();
	}

	/**
	 * String s에 있는 alphabet을 모두 소문자로 바꾸어 return
	 * 
	 * @param s source String
	 */
	public static String toLowerCase(String s) {
		int i;
		int j;
		char c;
		label0: {
			i = s.length();
			for (j = 0; j < i; j++) {
				char c1 = s.charAt(j);
				c = Character.toLowerCase(c1);
				if (c1 != c)
					break label0;
			}

			return s;
		}
		char ac[] = new char[i];
		int k;
		for (k = 0; k < j; k++)
			ac[k] = s.charAt(k);

		ac[k++] = c;
		for (; k < i; k++)
			ac[k] = Character.toLowerCase(s.charAt(k));

		String s1 = new String(ac, 0, i);
		return s1;
	}

	/**
	 * String s에 있는 alphabet을 모두 대문자로 바꾸어 return
	 * 
	 * @param s source String
	 */
	public static String toUpperCase(String s) {
		int i;
		int j;
		char c;
		label0: {
			i = s.length();
			for (j = 0; j < i; j++) {
				char c1 = s.charAt(j);
				c = Character.toUpperCase(c1);
				if (c1 != c)
					break label0;
			}

			return s;
		}
		char ac[] = new char[i];
		int k;
		for (k = 0; k < j; k++)
			ac[k] = s.charAt(k);

		ac[k++] = c;
		for (; k < i; k++)
			ac[k] = Character.toUpperCase(s.charAt(k));

		return new String(ac, 0, i);
	}

	/**
	 * String s에 있는 sub string s1을 이용하여 String을 분리한다.
	 * 
	 * @param s source String
	 * @param s1 String s를 분리할 sub string
	 * @return 분리된 string의 벡터
	 */
	public static Vector<String> tokenizer(String s, String s1) {
		if (s == null)
			return null;
		Vector<String> vector = null;
		for (StringTokenizer stringtokenizer = new StringTokenizer(s, s1); stringtokenizer
				.hasMoreTokens(); vector.addElement(stringtokenizer.nextToken()
				.trim()))
			if (vector == null)
				vector = new Vector<String>();

		return vector;
	}

	/**
	 * html tag가 포함된 string을 출력할때 사용한다.
	 * @param s
	 * @return
	 */
	public static String html2text(String s) {
		String s1 = s;
		if (s1 == null)
			return "";
		if (s1.indexOf(38, 0) != -1)
			s1 = replace(s1, "&", "&amp;");
		if (s1.indexOf(60, 0) != -1)
			s1 = replace(s1, "<", "&lt;");
		if (s1.indexOf(62, 0) != -1)
			s1 = replace(s1, ">", "&gt;");
		if (s1.indexOf(34, 0) != -1)
			s1 = replace(s1, "\"", "&quot;");
		if (s1.indexOf(32, 0) != -1)
			s1 = replace(s1, " ", "&nbsp;");
		if (s1.indexOf(10, 0) != -1)
			s1 = replace(s1, "\n", "<br>");
		return s1;
	}

	/**
	 * \\n 을 &ltbr&gt 로 변경해 준다.
	 * @param s
	 * @return
	 */
	public static String nl2br(String s) {
		if (s == null)
			return "";
		String s1 = s;
		if (s1.indexOf(10, 0) != -1) {
			s1 = replace(s1, "\n", "<br>");
		}
		return s1;
	}

	/**
	 * &, <, >, "를 &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot; 로 대체한 string으로 바꾸어
	 * 줌
	 * 
	 * @param s source String
	 */
	public static String escapeHtmlString(String s) {
		String s1 = s;
		if (s1 == null)
			return null;
		if (s1.indexOf(38, 0) != -1)
			s1 = replace(s1, "&", "&amp;");
		if (s1.indexOf(60, 0) != -1)
			s1 = replace(s1, "<", "&lt;");
		if (s1.indexOf(62, 0) != -1)
			s1 = replace(s1, ">", "&gt;");
		if (s1.indexOf(34, 0) != -1)
			s1 = replace(s1, "\"", "&quot;");
		return s1;
	}

	/**
	 * &amp;amp;, &amp;lt;, &amp;gt;, &amp;quot;를 
	 * &, <, >, " 로 대체한 string으로 바꾸어 줌
	 * 
	 * @param s source String
	 */
	public static String restoreHtmlString(String s) {
		String s1 = s;
		if (s1 == null)
			return null;
		String[] arguments = { "&amp;", "&lt;", "&gt;", "&quot;" };
		String[] replacements = { "&", "<", ">", "\"" };
		return replace(s1, arguments, replacements);
	}

	/**
	 * character c로 length만큼 채워진 String을 return
	 * 
	 * @param c string으로 채워질 character
	 * @param length 원하는 character 갯수
	 * @return charracter c로 length 갯수 만큼 채워진 string
	 */
	public static String fill(char c, int length) {
		if (length <= 0)
			return "";

		char[] ca = new char[length];
		for (int index = 0; index < length; index++) {
			ca[index] = c;
		}

		return new String(ca);
	}

	/**
	 * 주어진 length를 유지하기 위해 String s에 character c를 오른쪽으로 덧댄다. <p>
	 * 
	 * <pre>
	 *     StringUtil.padRight(&quot;hahahaha&quot;, '.', 14);
	 *     StringUtil.padRight(&quot;hihihi&quot;, '.', 14);
	 *     StringUtil.padRight(&quot;hohohohoho&quot;, '.', 14);
	 *    
	 *     은 다음과 같은 결과를 보여줄 것이다.
	 *    
	 *     hahahaha.....
	 *     hihihi.......
	 *     hohohohoho...
	 * </pre>
	 * 
	 * 위와 같이 일정한 사이즈로 문단을 구성하고자 할 때 유용할 것 임
	 * 
	 * @param s source String
	 * @param c String s에 덧대질 character
	 * @param length return될 String의 length
	 */
	public static String padRight(String s, char c, int length) {
		return s + fill(c, length - s.length());
	}

	/**
	 * 주어진 length를 유지하기 위해 String s에 character c를 왼쪽으로 덧댄다. <p>
	 * 
	 * <pre>
	 * 
	 *  
	 *   
	 *    
	 *     StringUtil.padRight(&quot;hahahaha&quot;, '.', 14);
	 *     StringUtil.padRight(&quot;hihihi&quot;, '.', 14);
	 *     StringUtil.padRight(&quot;hohohohoho&quot;, '.', 14);
	 *    
	 *     은 다음과 같은 결과를 보여줄 것이다.
	 *    
	 *     .....hahahaha
	 *     .......hihihi
	 *     ...hohohohoho
	 *     
	 *    
	 *   
	 *  
	 * </pre>
	 * 
	 * 위와 같이 일정한 사이즈로 문단을 구성하고자 할 때 유용할 것 임
	 * 
	 * @param s source String
	 * @param c String s에 덧대질 character
	 * @param length return될 String의 length
	 */
	public static String padLeft(String s, char c, int length) {
		return fill(c, length - s.length()) + s;
	}

	/**
	 * 전달된 문자열의 길이를 넘겨온 길이에 맞게 잘라서 넘겨준다. 
	 * 잘린 String에 Attach String을 덧붙여서 리턴한다.
	 * 
	 * @param src 변환할 문자열
	 * @param str_length 자를 문자 길이
	 * @param att_str 잘린 문자를 대체할 문자
	 * @return String 잘린 문자열
	 */
	public static String cutString(String src, int str_length, String att_str) {
		int ret_str_length = 0;

		String ret_str = new String("");

		if (src == null) {
			return ret_str;
		}

		// 현재 환경의 Character length를 구한다.
		String tempMulLanChar = new String("가");
		int lanCharLength = tempMulLanChar.length();

		// Character가 중간에 잘리지 않게 하기위해 넣은 변수
		int multiLanCharIndex = 0;
		int nonMultiLanCharIndex = 0;

		for (int i = 0; i < src.length(); i++) {
			ret_str += src.charAt(i);

			if (src.charAt(i) > '~') {
				ret_str_length = ret_str_length + 2 / lanCharLength;
				multiLanCharIndex++;
			} else {
				ret_str_length = ret_str_length + 1;
				nonMultiLanCharIndex++;
			}
			if (ret_str_length >= str_length
					&& (multiLanCharIndex % lanCharLength) == 0) {
				if (src.length() != multiLanCharIndex + nonMultiLanCharIndex)
					ret_str += NVL(att_str);
				break;
			}
		}

		return ret_str;
	}

	/**
	 * comma 구분자를 가지고 Array를 String으로 변환한다. 
	 * <p>예를들면 <br>{"aaa","bbbb","cc"}
	 * ---> "aaa,bbbb,cc"
	 */
	public static String toString(Object[] args) {
		return toString(args, ",");
	}

	/**
	 * separator 구분자를 가지고 Array를 String으로 변환
	 */
	public static String toString(Object[] args, String separator) {
		if (args == null)
			return null;

		StringBuffer buf = new StringBuffer();

		for (int index = 0; index < args.length; index++) {
			if (index > 0)
				buf.append(separator);

			if (args[index] == null)
				buf.append("null");
			else
				buf.append(args[index].toString());
		}

		return buf.toString();
	}

	/**
	 * separator 구분자를 가지고 List를 String으로 변환
	 */
	public static String toString(List<?> list, String separator) {
		StringBuffer buf = new StringBuffer();
		for (int index = 0; index < list.size(); index++) {
			if (index > 0)
				buf.append(separator);
			buf.append(list.get(index).toString());
		}
		return buf.toString();
	}

	/**
	 * 전달된 문자열을 src_enc 방식에서 dest_enc 방식으로 변환한다.
	 * 
	 * @param str 변환시킬 문자열
	 * @param src_enc 원래 문자의 encoding방식
	 * @param dest_enc 변환시킬 encoding방식.
	 * @return String desc_enc 방식으로 변환된 문자열
	 * @throws UnsupportedEncodingException : Encoding이 지원되지 않는 문자열 변환시
	 */
	public static String toConvert(String str, String src_enc, String dest_enc)
			throws java.io.UnsupportedEncodingException {
		if (str == null)
			return "";
		else
			return new String(str.getBytes(src_enc), dest_enc);
	}

	/**
	 * Null String을 "" String으로 바꿔준다.
	 * 
	 * @param str Null 문자열
	 * @return "" 문자열(null이 아닐 경우는 변환할 문자열이 그대로 리턴)
	 */
	public static String NVL(String str) {
		if (str == null)
			return "";
		else
			return str;
	}
		
	/**
	 * 문자열이 null인경우 replace_str을 Return한다. 사용 예) 테이블의 <td>str</td> 에서 
	 * str이 null인 경우 replate_str이 &nbsp;로 지정한다.
	 * 
	 * @param str Null 문자열
	 * @param replace_str 변환할 문자열
	 * @return 변환할 문자열
	 */
	public static String NVL(String str, String replace_str) {
		if (str == null || str.length() <= 0)
			return replace_str;
		else
			return str;
	}
	
	/**
	 * Integer가 null 인 경우 0을 반환
	 * @param num
	 * @return
	 */
	public static Integer NVL(Integer num) {
		num = (num == null) ? new Integer(0) : num;
		return num;
	}
	
	/**
	 * Integer가 null 인 경우 변환할 숫자
	 * @param num
	 * @return
	 */
	public static Integer NVL(Integer num, Integer replace_num) {
		num = (num == null) ? new Integer(replace_num) : num;
		return num;
	}
	
	/**
	 * Double이 null 인 경우 0을 반환
	 * @param num
	 * @return
	 */
	public static Double NVL(Double num) {
		num = (num == null) ? new Double(0) : num;
		return num;
	}
	
	/**
	 * Double이 null 인 경우 변환할 숫자
	 * @param num
	 * @return
	 */
	public static Double NVL(Double num, Double replace_num) {
		num = (num == null) ? new Double(replace_num) : num;
		return num;
	}
	
	/**
	 * Float이 null 인 경우 0을 반환
	 * @param num
	 * @return
	 */
	public static Float NVL(Float num) {
		num = (num == null) ? new Float(0) : num;
		return num;
	}
	
	/**
	 * Float이 null 인 경우 변환할 숫자
	 * @param num
	 * @return
	 */
	public static Float NVL(Float num, Float replace_num) {
		num = (num == null) ? new Float(replace_num) : num;
		return num;
	}

	/**
	 * Long이 null 인 경우 0을 반환
	 * @param num
	 * @return
	 */
	public static Long NVL(Long num) {
		num = (num == null) ? new Long(0) : num;
		return num;
	}

	/**
	 * Long이 null 인 경우 변환할 숫자
	 * @param num
	 * @return
	 */
	public static Long NVL(Long num, Long replace_num) {
		num = (num == null) ? new Long(replace_num) : num;
		return num;
	}

	public static boolean isValidJumin(String first, String second) {
		try {
			if (first == null || second == null) {
				return false;
			}

			if (first.length() != 6 || second.length() != 7) {
				return false;
			}

			int hap = 0;
			for (int i = 0; i < 6; i++) {
				hap += Integer.parseInt(first.substring(i, i + 1)) * (i + 2);
			}
			int[] mag = new int[] { 8, 9, 2, 3, 4, 5 };
			for (int i = 0; i < 6; i++) {
				hap += Integer.parseInt(second.substring(i, i + 1)) * mag[i];
			}
			hap %= 11;
			hap = 11 - hap;
			hap %= 10;
			if (hap != Integer.parseInt(second.substring(6, 7))) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * String 문자열을 MD5로 변환 32자리로 반환
	 * 
	 * @param str Null MD5로 변환할 문자열
	 * @return 변환된 문자열
	 */
	public static String getMD5(String str) {		
		String md5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for(int i =0; i<byteData.length; i++) {
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			md5 = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			md5 = null;
		}
		return md5;
	}
	
	/**
	 * 파일명의 확장자만 추출
	 * 
	 * @param file_name Null 파일명
	 * @return 확장자명
	 */
	public static String getExtname(String file_name) {
		String ext = "";
        int index = file_name.lastIndexOf(".");
        if (index != -1) {
            ext  = file_name.substring(index + 1);
        }
        return ext;
	}	
	 
	/**
	 * 파일명의 파일만 추출
	 * 
	 * @param file_name Null 파일명
	 * @return 파일명
	 */
	public static String getFilename(String file_name) {
		String name = "";
        int index = file_name.lastIndexOf(".");
        if (index != -1) {
            name = file_name.substring(0, index);
        }
        return name;
	}
	
	public static String getCharset(String str) throws UnsupportedEncodingException {
		String charset[] = {"euc-kr", "ksc5601", "iso-8859-1", "8859_1", "ascii", "UTF-8"};
		String charStr = "";
		for(int i=0; i<charset.length ; i++){
			for(int j=0 ; j<charset.length ; j++){
				if(i==j) continue;
				charStr = charset[i];
				charStr += " : " + charset[j];
				charStr += " :" + new String(str.getBytes(charset[i]),charset[j]);
			}
        }
		return charStr;
    }

}
