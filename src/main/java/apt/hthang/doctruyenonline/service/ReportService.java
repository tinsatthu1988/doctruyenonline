package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Report;
import apt.hthang.doctruyenonline.entity.User;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
public interface ReportService {
    /**
     *
     * @param chapter
     * @param user
     * @param content
     * @return
     */
    Report saveReport(Chapter chapter, User user, String content);
    
    /**
     *
     * @param date
     * @return
     */
    Long countNewReportInDay(Date date);
    
}
