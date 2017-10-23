package framework.util;

import org.apache.commons.codec.CharEncoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by jhlee on 2017-08-29.
 *
 * AES128 encrypt시 key값은 16byte를 사용하여 encrypt 해야 함.
 */
public class EncryptionUtil {

    private static String byteArrayToHex(byte[] encrypted) {
        if(encrypted == null || encrypted.length ==0){
            return null;
        }
        StringBuffer sb = new StringBuffer(encrypted.length * 2);
        String hexNumber;
        for(int x=0; x<encrypted.length; x++){
            hexNumber = "0" + Integer.toHexString(0xff & encrypted[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    private static byte[] hexToByteArray(String hex) {
        if(hex == null || hex.length() == 0){
            return null;
        }
        //16진수 문자열을 byte로 변환
        byte[] byteArray = new byte[hex.length() /2 ];
        for(int i=0; i<byteArray.length; i++){
            byteArray[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2*i+2), 16);
        }
        return byteArray;
    }

    public static class AES128 {

        public static Key getKey(String key) {
            String iv;
            Key keySpec;
            try {
                iv = key.substring(0, 16);
                byte[] keyBytes = new byte[16];
                byte[] b = new byte[0];
                try {
                    b = key.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int len = b.length;
                if (len > keyBytes.length) {
                    len = keyBytes.length;
                }
                System.arraycopy(b, 0, keyBytes, 0, len);
                keySpec = new SecretKeySpec(keyBytes, "AES");
                return keySpec;
            } catch(Exception e) {
                return null;
            }
        }

        public static IvParameterSpec getKeyData(String key) throws UnsupportedEncodingException {
            byte[] keyData = key.getBytes(CharEncoding.UTF_8);
            IvParameterSpec keyDataSpec = new IvParameterSpec(keyData);
            return keyDataSpec;
        }

        public static String encrypt(String str, String key) throws InvalidKeyException,
                NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
            Key keySpec = getKey(key);
            IvParameterSpec keyDataSpec = getKeyData(key);
            if(keySpec != null || keyDataSpec != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, keyDataSpec);
                byte[] encrypted = cipher.doFinal(str.getBytes());
                return byteArrayToHex(encrypted);
            } else {
                return null;
            }
        }

        public static String decrypt(String str, String key) throws NoSuchPaddingException,
                NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
            Key keySpec = getKey(key);
            IvParameterSpec keyDataSpec = getKeyData(key);
            if(keySpec != null) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, keySpec, keyDataSpec);
                byte[] original = cipher.doFinal(hexToByteArray(str));
                String originalStr = new String(original);
                return originalStr;
            } else {
                return null;
            }
        }
    }


}
