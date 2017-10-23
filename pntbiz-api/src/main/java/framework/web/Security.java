package framework.web;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * Created by ucjung on 2017-05-12.
 */
public class Security {
    /**
    * sha256
    */
    public static String encrypt(String planText) {
        ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
        String encText = shaPasswordEncoder.encodePassword(planText,null);
        return encText;
    }

    public static String encrypt(String planText, boolean base64) {
        ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
        shaPasswordEncoder.setEncodeHashAsBase64(base64);
        String encText = shaPasswordEncoder.encodePassword(planText,null);
        return encText;
        /*
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(planText.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
        */
    }
}
