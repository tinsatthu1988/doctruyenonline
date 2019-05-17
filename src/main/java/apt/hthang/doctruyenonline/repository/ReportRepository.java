package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Repository
public interface ReportRepository extends JpaRepository< Report, Long > {
}
