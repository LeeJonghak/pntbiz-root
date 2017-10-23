package framework.gabiasms;

import javax.xml.bind.DatatypeConverter;

public class Base64Utils {

	public static String encode(byte[] encodeBytes) {
		return DatatypeConverter.printBase64Binary(encodeBytes).trim();
	}

	public static byte[] decode(String strDecode) {
		return DatatypeConverter.parseBase64Binary(strDecode);
	}
}
