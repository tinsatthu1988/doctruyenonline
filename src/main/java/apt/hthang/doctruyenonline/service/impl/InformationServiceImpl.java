package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Information;
import apt.hthang.doctruyenonline.repository.InformationRepository;
import apt.hthang.doctruyenonline.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
public class InformationServiceImpl implements InformationService {

    private final InformationRepository informationRepository;

    @Autowired
    public InformationServiceImpl(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    /**
     * Lấy Thông Tin Web
     *
     * @return Information- Nếu tồn tại Information/ null - nếu không tồn tại Information
     */
    @Override
    public Information getWebInfomation() {
        Optional<Information> information = informationRepository.findFirstByOrderByIdDesc();
        return information.orElse(null);
    }
}
