package apt.hthang.doctruyenonline.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Huy Thang
 */
public class DateUtils {
    
    /**
     * Chuyển đổi String sang Date
     *
     * @param dateStr Chuỗi String cần đổi
     * @return Date - nếu không có lỗi / null nếu có lỗi xảy ra
     */
    public static Date convertStringToDate(String dateStr) {
        try {
            TemporalAccessor temporal = DateTimeFormatter
                    .ofPattern("dd-MM-yyyy")
                    .parse(dateStr); // use parse(date, LocalDateTime::from) to get LocalDateTime
            System.out.println(temporal.toString());
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate localDate = LocalDate.parse(temporal.toString());
//            System.out.println(localDate.toString());
//            java.util.Date newDate = new java.util.Date();
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            return dateFormat.format();
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    //Lấy ngày đầu tiên của tuần hiện tại
    public static Date getFirstDayOfWeek() {
        LocalDate monday = LocalDate.now();
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        return java.sql.Date.valueOf(monday.toString());
    }
    
    //Lấy ngày cuối của tuần hiện tại
    public static Date getLastDayOfWeek() {
        LocalDate sunday = LocalDate.now();
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.plusDays(1);
        }
        return java.sql.Date.valueOf(sunday.toString());
    }
    
    //Lấy ngày đầu tiên của tháng hiện tại
    public static Date getFirstDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstday = today.withDayOfMonth(1);
        return java.sql.Date.valueOf(firstday.toString());
    }
    
    //Lấy ngày cuối cùng của tháng hiện tại
    public static Date getLastDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastday = today.withDayOfMonth(today.lengthOfMonth());
        return java.sql.Date.valueOf(lastday.toString());
    }
    
    //Lấy thời gian hiện tại
    public static Date getCurrentDate() {
        LocalDate today = LocalDate.now();
        return java.sql.Date.valueOf(today.toString());
    }
    
    public static String betweenTwoDays(Date createDate) {
        LocalDateTime startDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate startLocalDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDate endLocalDate = LocalDate.now();
        long differentInSeconds = Duration.between(startDate, endDate).getSeconds();
        long differentInMinutes = Duration.between(startDate, endDate).toMinutes();
        long differentInHours = Duration.between(startDate, endDate).toHours();
        long differentInDays = Period.between(startLocalDate, endLocalDate).getDays();
        long differentInMonths = Period.between(startLocalDate, endLocalDate).getMonths();
        long differentInYears = Period.between(startLocalDate, endLocalDate).getYears();
        if (differentInYears > 0)
            return differentInYears + " năm trước";
        if (differentInMonths > 0)
            return differentInMonths + " tháng trước";
        if (differentInDays > 0)
            return differentInDays + " ngày trước";
        if (differentInHours > 0)
            return differentInHours + " giờ trước";
        if (differentInMinutes > 0)
            return differentInMinutes + " phút trước";
        return differentInSeconds + " giây trước";
    }
    
    
    public static String betweenHours(Date createDate) {
        LocalDateTime startDate = createDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDate = getCurrentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        long differentInSeconds = Duration.between(startDate, endDate).getSeconds();
        long differentInMinutes = Duration.between(startDate, endDate).toMinutes();
        if (differentInMinutes > 0)
            return differentInMinutes + " phút";
        return differentInSeconds + " giây";
    }
    
    public static Date getHoursAgo(Date now, long hours) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.minusHours(hours);
        return java.sql.Timestamp.valueOf(result);
    }
    
    public static Date getMinutesAgo(Date now, long minutes) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.minusMinutes(minutes);
        return java.sql.Timestamp.valueOf(result);
    }
    
    public static Date getDateDeal(Integer count) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(getCurrentDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime result = localDateTime.plusDays(count);
        return java.sql.Timestamp.valueOf(result);
    }
    
}
