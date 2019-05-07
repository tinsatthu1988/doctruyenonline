package apt.hthang.doctruyenonline.utils;

import apt.hthang.doctruyenonline.component.MyComponent;

/**
 * @author Linh
 * @project doctruyenonline
 */
public class TestDate {
    
    public static void main(String[] args) {
        System.out.println(DateUtils.convertStringToDate("17-05-2019"));
        System.out.println(DateUtils.getCurrentDate());
        System.out.println(DateUtils.getFirstDayOfMonth());
        System.out.println(DateUtils.getLastDayOfMonth());
    }
}
