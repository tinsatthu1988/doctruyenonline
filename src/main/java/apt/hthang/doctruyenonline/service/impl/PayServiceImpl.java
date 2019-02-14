package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.PayRepository;
import apt.hthang.doctruyenonline.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 */
@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private PayRepository payRepository;
}