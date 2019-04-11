package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.StorySlide;
import apt.hthang.doctruyenonline.projections.StorySummary;
import apt.hthang.doctruyenonline.projections.StoryTop;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.repository.StoryRepository;
import apt.hthang.doctruyenonline.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public Page< StoryUpdate > findStoryNewUpdateByCategoryId(Integer cID,
                                                              int page, int size,
                                                              List< Integer > storyStatus, List< Integer > chapterStatus) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findStoryNewByCategory(cID, storyStatus, chapterStatus, pageable);
    }
    
    /**
     * Lấy List Truyện Top View theo Category
     *
     * @param categoryId
     * @param favoritesStatus
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    @Override
    public Page< StoryTop > findStoryTopViewByCategoryId(Integer categoryId, Integer favoritesStatus,
                                                         List< Integer > listStatus,
                                                         Date startDate, Date endDate,
                                                         int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopViewByCategory(categoryId, favoritesStatus,
                        listStatus, startDate, endDate, pageable);
    }
    
    /**
     * Lấy Danh sách Truyện Top  Đề Cử Theo Category
     *
     * @param categoryID
     * @param storyStatus
     * @param payType
     * @param payStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    @Override
    public Page< StoryTop > findStoryTopVoteByCategoryId(Integer categoryID,
                                                         List< Integer > storyStatus,
                                                         Integer payType, Integer payStatus,
                                                         Date startDate, Date endDate,
                                                         int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopVoteByCategory(categoryID, storyStatus, payType, payStatus, startDate, endDate, pageable);
    }
    
    /**
     * Tìm Kiếm List Truyện Theo SearchKey
     *
     * @param searchKey
     * @param pagenumber
     * @param pageSize
     * @param listStoryStatus
     * @param listChapterStatus
     * @return
     */
    @Override
    public Page< StoryUpdate > findStoryBySearchKey(String searchKey,
                                                    List< Integer > listChapterStatus, List< Integer > listStoryStatus,
                                                    int pagenumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pagenumber - 1, pageSize);
        return storyRepository.findStoryBySearchKey(listChapterStatus, searchKey, listStoryStatus, pageable);
    }
    
    /**
     * Tìm Truyện Theo StoryID và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return StorySummar - nếu tồn tại truyện thỏa mãn điều kiện
     * @throws Exception - nếu không tồn tại truyện thỏa mãn điều kiện
     */
    @Override
    public StorySummary findStoryByStoryIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception {
        return storyRepository
                .findByIdAndStatusIn(storyId, listStoryStatus)
                .orElseThrow(NotFoundException::new);
    }
    
    /**
     * Lấy Danh sách truyện mới đăng của Converter
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    @Override
    public List< StorySlide > findStoryOfConverter(Long userId, List< Integer > listStoryDisplay) {
        return storyRepository
                .findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStoryDisplay);
    }
}