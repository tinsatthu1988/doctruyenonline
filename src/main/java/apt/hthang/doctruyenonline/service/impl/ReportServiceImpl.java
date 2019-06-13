package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Report;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.repository.ReportRepository;
import apt.hthang.doctruyenonline.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportRepository reportRepository;
    
    @Override
    public Report saveReport(Chapter chapter, User user, String content) {
        Report report = new Report();
        report.setChapter(chapter);
        report.setContent(content);
        report.setUser(user);
        return reportRepository.save(report);
    }
    
    /**
     * @param date
     * @return
     */
    @Override
    public Long countNewReportInDay(Date date) {
        return reportRepository.countByCreateDateGreaterThanEqual(date);
    }
}
