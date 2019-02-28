package apt.hthang.doctruyenonline.service;

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
}
