package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Repository
public interface ReportRepository extends JpaRepository< Report, Long > {
    
    /**
     *
     * @param date
     * @return
     */
    Long countByCreateDateGreaterThanEqual(Date date);
}
