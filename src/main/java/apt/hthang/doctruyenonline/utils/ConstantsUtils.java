package apt.hthang.doctruyenonline.utils;

import static java.lang.Long.valueOf;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public class ConstantsUtils {
    
    //Chuỗi random
    public static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    
    //Độ dài chuỗi random
    public static final int RANDOM_STRING_LENGTH = 6;
    
    // Page Default
    public static Integer PAGE_DEFAULT = 1;
    
    // Page size default
    public static Integer PAGE_SIZE_DEFAULT = 20;
    
    //Page size list comment default
    public static Integer PAGE_SIZE_COMMENT_DEFAULT = 10;
    
    // Page size Top View
    public static Integer RANK_SIZE = 10;
    
    //Page size Chapter list default
    public static Integer PAGE_SIZE_CHAPTER_OF_STORY = 45;
    
    //Thời Gian Một Ngày Theo Giờ
    public static Long TIME_DAY = valueOf(24);
    
    //Thời gian nửa tiếng theo phút
    public static long HALF_HOUR = valueOf(30);
    
    //Link avartar default
    public static final String AVATAR_DEFAULT = "https://res.cloudinary.com/thang1988/image/upload/v1541176548/truyenmvc/logo.png";
}
