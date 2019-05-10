package apt.hthang.doctruyenonline.utils;

import apt.hthang.doctruyenonline.component.MyComponent;

/**
 * @author Linh
 * @project doctruyenonline
 */
public class TestDate {
    
    public static void main(String[] args) {
        System.out.println(WebUtils.maskString("a", '*'));
        System.out.println(WebUtils.maskString("ab", '*'));
        System.out.println(WebUtils.maskString("abc", '*'));
        System.out.println(WebUtils.maskString("abcd", '*'));
        System.out.println(WebUtils.maskString("abcde", '*'));
        System.out.println(WebUtils.maskString("abcdef", '*'));
//        System.out.println(DateUtils.convertStringToDate("17-05-2019"));
//        System.out.println(DateUtils.getCurrentDate());
//        System.out.println(DateUtils.getFirstDayOfMonth());
//        System.out.println(DateUtils.getLastDayOfMonth());
    }
}
