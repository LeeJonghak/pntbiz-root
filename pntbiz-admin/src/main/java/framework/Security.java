package framework;

import framework.auth.LoginDetail;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.MessageDigest;
import java.util.Properties;

/**
 * Created by ucjung on 2017-05-12.
 */
public class Security {

    public static LoginDetail getLoginDetail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (LoginDetail) principal;
    }

    public static String getLocalImagePath() {
        Properties properties = ApplicationContextProvider.getConfigProperties();

        if(System.getProperty("os.name").toLowerCase().matches(".*window.*") == true) {
            return properties.getProperty("local.image.window.path");
        } else {
            return properties.getProperty("local.image.path");
        }
    }

    public static String getLocalPrivateKeyPath() {
        Properties properties = ApplicationContextProvider.getConfigProperties();

        if(System.getProperty("os.name").toLowerCase().matches(".*window.*") == true) {
            return properties.getProperty("local.privatekey.window.path");
        } else {
            return properties.getProperty("local.privatekey.path");
        }
    }

    /**
     * sha256
     */
    public static String encrypt(String planText) {
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
    }
}
