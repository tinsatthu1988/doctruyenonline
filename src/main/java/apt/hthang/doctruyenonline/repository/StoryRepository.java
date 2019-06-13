package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.projections.*;
import apt.hthang.doctruyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */

@Repository
public interface StoryRepository extends JpaRepository< Story, Long > {
    
    /**
     * Lấy Danh sách Truyện Mới Theo Thể Loại
     *
     * @param listChStatus
     * @param cID
     * @param listStatus
     * @param pageable
     * @return Page<StoryUpdate>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_CATEGORY,
            nativeQuery = true)
    Page< StoryUpdate > findStoryNewByCategory(@Param("categoryId") Integer cID,
                                               @Param("storyStatus") List< Integer > listStatus,
                                               @Param("chapterStatus") List< Integer > listChStatus,
                                               Pageable pageable);
    
    /**
     * Lấy Danh sách Truyện Top  View Theo Category
     *
     * @param cID
     * @param historyStatus
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW_BY_CATEGORY,
            nativeQuery = true)
    Page< StoryTop > findTopViewByCategory(@Param("categoryID") Integer cID,
                                           @Param("historyStatus") Integer historyStatus,
                                           @Param("storyStatus") List< Integer > listStatus,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate, Pageable pageable);
    
    /**
     * Lấy Danh sách Truyện Top  Đề Cử Theo Category
     *
     * @param cID
     * @param listStatus
     * @param payType
     * @param payStatus
     * @param startDate
     * @param endDate
     * @param pageable
     * @return Page<StoryTop>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VOTE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VOTE_BY_CATEGORY,
            nativeQuery = true)
    Page< StoryTop > findTopVoteByCategory(@Param("categoryID") Integer cID,
                                           @Param("storyStatus") List< Integer > listStatus,
                                           @Param("payType") Integer payType, @Param("payStatus") Integer payStatus,
                                           @Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                           Pageable pageable);
    
    
    /**
     * Lấy danh sách truyện theo searchkey
     *
     * @param listChStatus
     * @param search
     * @param listStatus
     * @param pageable
     * @return Page<StoryUpdate>
     */
    @Query(value = ConstantsQueryUtils.SEARCH_STORY,
            countQuery = ConstantsQueryUtils.COUNT_SEARCH_STORY,
            nativeQuery = true)
    Page< StoryUpdate > findStoryBySearchKey(@Param("chapterStatus") List< Integer > listChStatus,
                                             @Param("search") String search,
                                             @Param("storyStatus") List< Integer > listStatus,
                                             Pageable pageable);
    
    /**
     * Tìm truyện theo id và status thỏa mãn
     *
     * @param storyId
     * @param listStoryStatus
     * @return Optional<StorySummary>
     */
    Optional< StorySummary > findByIdAndStatusIn(Long storyId, List< Integer > listStoryStatus);
    
    /**
     * Tìm truyện theo id và status thỏa mãn
     *
     * @param storyId
     * @param listStoryStatus
     * @return Optional<Story>
     */
    Optional< Story > findStoryByIdAndStatusIn(Long storyId, List< Integer > listStoryStatus);
    
    /**
     * Lấy Danh sách Top 5 truyện mới đăng theo user và status
     *
     * @param userId
     * @param listStoryDisplay
     * @return List<StorySlide>
     */
    List< StorySlide > findTop5ByUser_IdAndStatusInOrderByCreateDateDesc(Long userId, List< Integer > listStoryDisplay);
    
    /**
     * Lấy số lượng truyện đã đăng thành công
     *
     * @param userId
     * @param listStoryDisplay
     * @return Long
     */
    Long countByUser_IdAndStatusIn(Long userId, List< Integer > listStoryDisplay);
    
    /**
     * Lấy Danh sách Truyện Mới Cập Nhật Theo Status
     *
     * @param listChapterStatus - Danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái Story
     * @param pageable          - biến phân trang
     * @return Page<NewStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_STATUS,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_STATUS,
            nativeQuery = true)
    Page< StoryUpdate > getPageStoryComplete(@Param("chapterStatus") List< Integer > listChapterStatus,
                                             @Param("storyStatus") List< Integer > listStoryStatus,
                                             Pageable pageable);
    
    /**
     * Lấy Danh sách Truyện Top
     *
     * @param startDate
     * @param endDate
     * @param listStatus
     * @param historyStatus
     * @param pageable
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_VIEW_BY_STATUS,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_VIEW_BY_STATUS,
            nativeQuery = true)
    Page< StoryTop > findStoryTopViewByStatus(@Param("storyStatus") List< Integer > listStatus,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("historyStatus") Integer historyStatus,
                                              Pageable pageable);
    
    /**
     * Lấy Danh sách Truyện Vip mới cập nhật
     *
     * @param listChapterStatus - danh sách trạng thái chapter
     * @param pageable          - biến page
     * @param listStoryStatus   - danh sách trạng thái truyện
     * @param sDealStatus       - trạng thái truyện trả tiền
     * @return Page<StoryUpdate>
     */
    @Query(value = ConstantsQueryUtils.VIP_STORY_NEW_UPDATE,
            countQuery = ConstantsQueryUtils.COUNT_VIP_STORY_NEW_UPDATE,
            nativeQuery = true)
    Page< StoryUpdate > findVipStoryNew(@Param("chapterStatus") List< Integer > listChapterStatus,
                                        @Param("storyStatus") List< Integer > listStoryStatus,
                                        @Param("storyDealStatus") Integer sDealStatus,
                                        Pageable pageable);
    
    /**
     * Lấy Page truyện theo id và status
     *
     * @param userId
     * @param listStatus
     * @param pageable
     * @return
     */
    Page< StoryMember > findByUser_IdAndStatusInOrderByCreateDateDesc(Long userId,
                                                                      List< Integer > listStatus,
                                                                      Pageable pageable);
    
    /**
     * Lấy danh sách truyện theo user_id và status
     *
     * @param userId
     * @param listStatus
     * @return
     */
    List< StoryMember > findAllByUser_IdAndStatusInOrderByCreateDateDesc(Long userId,
                                                                         List< Integer > listStatus);
    
    /**
     * Lấy danh sách truyện theo
     *
     * @param searchText
     * @param listStatus
     * @return
     */
    List< StorySlide > findTop10ByVnNameContainingAndStatusInOrderByVnNameAsc(String searchText, List< Integer > listStatus);
    
    /**
     * Lấy danh sách Truyện theo Id người đăng và Status
     *
     * @param id
     * @param status
     * @param pageable
     * @return
     */
    Page< StoryUser > findByUser_IdAndStatusOrderByUpdateDateDesc(Long id, Integer status, Pageable pageable);
    
    /**
     * Lấy Danh sách Truyện Top Đề Cử
     *
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param payType
     * @param payStatus
     * @param pageable
     * @return
     */
    @Query(value = ConstantsQueryUtils.STORY_TOP_APPOIND,
            countQuery = ConstantsQueryUtils.COUNT_STORY_TOP_APPOIND,
            nativeQuery = true)
    Page< StoryTop > findTopStoryAppoind(@Param("storyStatus") List< Integer > listStatus,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate,
                                         @Param("payType") Integer payType, @Param("payStatus") Integer payStatus,
                                         Pageable pageable);
    
    /**
     * @param date
     * @return
     */
    Long countByCreateDateGreaterThanEqual(Date date);
    
    /**
     * Đếm số truyện đăng bởi User có Id và Trạng Thái Truyện
     *
     * @param id
     * @param status
     * @return
     */
    Long countByUser_IdAndStatus(Long id, Integer status);
    
    Page< StoryAdmin > findByOrderByIdDesc(Pageable pageable);
    
    Page< StoryAdmin > findByVnNameContainingOrderByIdDesc(String search, Pageable pageable);
    
    Page< StoryAdmin > findByDealStatusOrderByIdDesc(Integer stauts, Pageable pageable);
    
    Page< StoryAdmin > findByDealStatusAndVnNameContainingOrderByIdDesc(Integer status, String search, Pageable pageable);
    
    Page< StoryAdmin > findByVnNameContainingAndStatusOrderByIdDesc(String search, Integer status, Pageable pageable);
    
    Page< StoryAdmin > findByStatusOrderByIdDesc(Integer status, Pageable pageable);
}
