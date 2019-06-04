package apt.hthang.doctruyenonline.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

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
     * Kiểm tra string có phải Float number và là số dương
     *
     * @param number
     * @return true - nếu sai / false - nếu đúng
     */
    public static boolean checkDoubleNumber(String number) {
        try {
            Double result = Double.parseDouble(number);
            return true;
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * Kiểm tra string có phải Double number và là số dương
     *
     * @param number
     * @return true - nếu sai / false - nếu đúng
     */
    public static boolean checkMoney(String number) {
        try {
            Double result = Double.parseDouble(number);
            if (result <= 0)
                return true;
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
    
    public static String maskString(String strText, char maskChar) {
        if (strText == null || strText.equals(""))
            return "";
        int start;
        int end = strText.length();
        int length = strText.length();
        if (length <= 3)
            start = 1;
        else if (length <= 5)
            start = 2;
        else {
            start = 2;
            end = length - 2;
        }
        int maskLength = end - start;
        
        if (maskLength == 0)
            return strText;
        
        StringBuilder sbMaskString = new StringBuilder(maskLength);
        
        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append(maskChar);
        }
        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(end);
    }
    
    public static String convertStringToMetaTitle(String name) {
        try {
            name = name.replaceAll("[+.^:,^$|?*+()!]", "");
            String temp = Normalizer.normalize(name, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            return " ";
        }
    }
    
    public static Integer countWords(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }
        int wordCount = 0;
        boolean isWord = false;
        int endOfLine = word.length() - 1;
        char[] characters = word.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(characters[i]) && i != endOfLine) {
                isWord = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(characters[i]) && isWord) {
                wordCount++;
                isWord = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(characters[i]) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
