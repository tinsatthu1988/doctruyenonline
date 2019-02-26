package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.repository.StoryRepository;
import apt.hthang.doctruyenonline.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Huy Thang
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {
    
    private final StoryRepository storyRepository;
    
    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }
    
    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param cID
     * @param page
     * @param size
     * @param storyStatus
     * @param chapterStatus
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryNewUpdateByCategroyId(Integer cID, int page, int size, List< Integer > storyStatus, List< Integer > chapterStatus) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findStoryNewByCategory(cID, storyStatus,chapterStatus, pageable);
    }
}
