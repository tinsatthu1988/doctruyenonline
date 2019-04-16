package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.projections.StorySlide;
import apt.hthang.doctruyenonline.projections.StorySummary;
import apt.hthang.doctruyenonline.projections.StoryTop;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
 */
public interface StoryService {
    
    /**
     * Lấy List Truyện Mới Cập Nhật theo Category
     *
     * @param cID
     * @param page
     * @param size
     * @param chapterStatus
     * @param storyStatus
     * @return Page<StoryUpdate>
     */
    Page< StoryUpdate > findStoryNewUpdateByCategoryId(Integer cID,
                                                       int page, int size,
                                                       List< Integer > storyStatus, List< Integer > chapterStatus);
    
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
    Page< StoryTop > findStoryTopViewByCategoryId(Integer categoryId, Integer favoritesStatus,
                                                  List< Integer > listStatus,
                                                  Date startDate, Date endDate,
                                                  int page, int size);
    
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
    Page< StoryTop > findStoryTopVoteByCategoryId(Integer categoryID, List< Integer > storyStatus,
                                                  Integer payType, Integer payStatus,
                                                  Date startDate, Date endDate,
                                                  int page, int size);
    
    /**
     * Tìm Kiếm List Truyện Theo SearchKey
     *
     * @param searchKey
     * @param pagenumber
     * @param pageSize
     * @param listChapterStatus
     * @param listStoryStatus
     * @return
     */
    Page< StoryUpdate > findStoryBySearchKey(String searchKey,
                                             List< Integer > listChapterStatus, List< Integer > listStoryStatus,
                                             int pagenumber, Integer pageSize);
    
    /**
     * Tìm Truyện Theo StoryID và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return StorySummar - nếu tồn tại truyện thỏa mãn điều kiện
     * @throws Exception - nếu không tồn tại truyện thỏa mãn điều kiện
     */
    StorySummary findStoryByStoryIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception;
    
    /**
     * Lấy Danh sách truyện mới đăng của Converter
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    List< StorySlide > findStoryOfConverter(Long userId, List< Integer > listStoryDisplay);
    
    /**
     * Tìm Truyện Theo Id và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return Story - nếu tồn tại truyện thỏa mãn điều kiện
     * @throws Exception - nếu không tồn tại truyện thỏa mãn điều kiện
     */
    Story findStoryByIdAndStatus(Long storyId, List< Integer > listStoryStatus) throws Exception;
    
    /**
     * Lấy số lượng truyện đăng bởi User
     *
     * @param userId
     * @param listStoryDisplay
     * @return Long
     */
    Long countStoryByUser(Long userId, List< Integer> listStoryDisplay);
}
