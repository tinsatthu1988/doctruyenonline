package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.ChapterRepository;
import apt.hthang.doctruyenonline.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Huy Thang
 */
@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {

    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }
}
