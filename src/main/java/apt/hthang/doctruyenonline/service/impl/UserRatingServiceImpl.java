package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.UserRatingRepository;
import apt.hthang.doctruyenonline.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 */
@Service
public class UserRatingServiceImpl implements UserRatingService {

    private final UserRatingRepository userRatingRepository;

    @Autowired
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository) {
        this.userRatingRepository = userRatingRepository;
    }
}
