package apt.hthang.doctruyenonline.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * @author Đời Không Như Là Mơ
 */
public class CryptographyUtils {

    private static TextEncryptor encryptors = Encryptors.queryableText("truyenonline", "5c0744940b5c369b");

    public static String encrypText(String text) {
        return encryptors.encrypt(text);
    }

    public static String decryptText(String text) {
        return encryptors.decrypt(text);
    }
}
