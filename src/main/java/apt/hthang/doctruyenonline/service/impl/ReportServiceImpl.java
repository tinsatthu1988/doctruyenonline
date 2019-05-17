package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.ReportRepository;
import apt.hthang.doctruyenonline.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportRepository reportRepository;
}
