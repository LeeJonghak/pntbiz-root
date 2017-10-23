package framework.util;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by jhlee on 2017-08-29.
 */
public class EncryptionUtilTest {
    @Test
    public void getAESKeyNull() {
        // 해당 키가 16byte 이하일때
        Key keySpec = EncryptionUtil.AES128.getKey("123456");
        assertThat(keySpec, is(nullValue()));
    }

    @Test
    public void getAESKey() {
        Key keySpec = EncryptionUtil.AES128.getKey("1234567890123456");
        assertThat(keySpec, not(nullValue()));
    }

    @Test
    public void encryptAES128() throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        String message = "encrypt message!";
        String key = "1234567890123456";
        String encryptMessage = EncryptionUtil.AES128.encrypt(message, key);
        String decryptMessage = EncryptionUtil.AES128.decrypt(encryptMessage, key);
        assertThat(decryptMessage, is(message));
    }
}
