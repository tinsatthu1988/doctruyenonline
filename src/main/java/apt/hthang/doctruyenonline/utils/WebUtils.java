package apt.hthang.doctruyenonline.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

public class WebUtils {
    
    /**
     * Kiểm tra string có phải Long number
     *
     * @param number
     * @return true - nếu không phải / false - nếu đúng
     */
    public static boolean checkLongNumber(String number) {
        try {
            Long.parseLong(number);
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    
    /**
     * Kiểm tra string có phải Float number và là số dương
     *
     * @param number
     * @return true - nếu sai / false - nếu đúng
     */
    public static boolean checkFloatNumber(String number) {
        try {
            Float result = Float.parseFloat(number);
            if (result < 0) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }
    
    /**
     * Kiểm tra string có phải Integer number
     *
     * @param number
     * @return true - nếu sai / false - nếu đúng
     */
    public static boolean checkIntNumber(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Random một kết quả bất kỳ
     *
     * @return int
     */
    public static int getRandomNumber() {
        int randomInt;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(ConstantsUtils.CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
    
    /**
     * Tạo một chuỗi bất kỳ
     *
     * @return string
     */
    public static String randomPassword() {
        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < ConstantsUtils.RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = ConstantsUtils.CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }
    
    
    public static Integer checkPageNumber(String page) {
        int pagenumber = 1;
        
        // Kiểm tra page != null
        // Kiểm tra page có phải kiểu int
        // Kiểm tra page > 0
        if (page != null && WebUtils.checkIntNumber(page) && Integer.parseInt(page) > 0) {
            pagenumber = Integer.parseInt(page);
        }
        return pagenumber;
    }
    
    /**
     * Kiểm tra đuôi file có đúng định dạng
     *
     * @param fileExtension
     * @return
     */
    public static boolean checkExtension(String fileExtension) {
        return !fileExtension.equalsIgnoreCase("jpg") && !fileExtension.equalsIgnoreCase("jpeg")
                && !fileExtension.equalsIgnoreCase("png");
    }
    
    public static String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";
        
        //Kiểm Tra HttpServletRequest có null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        
        return remoteAddr;
    }
    
    public static String encrypString(String text) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(text);
    }
    
    public static boolean equalsPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
