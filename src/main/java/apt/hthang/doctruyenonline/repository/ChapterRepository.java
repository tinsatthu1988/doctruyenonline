package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import apt.hthang.doctruyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Repository
public interface ChapterRepository extends JpaRepository< Chapter, Long > {
    
    /**
     * Lấy Chapter Đầu Tiên của truyện
     *
     * @param storyId
     * @param listStatus
     * @return Optional<ChapterSummary>
     */
    @Query(value = ConstantsQueryUtils.CHAPTER_HEAD,
            nativeQuery = true)
    Optional< ChapterSummary > findChapterHead(@Param("storyId") Long storyId,
                                               @Param("chapterStatus") List< Integer > listStatus);
    
    /**
     * Lấy Chapter Mới Nhất của truyện
     *
     * @param storyId
     * @param chapterStatus
     * @return Optional<ChapterSummary>
     */
    @Query(value = ConstantsQueryUtils.CHAPTER_NEW,
            nativeQuery = true)
    Optional< ChapterSummary > findChapterNew(@Param("storyId")Long storyId,
                                              @Param("chapterStatus") List< Integer > chapterStatus);
    
    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @param pageable
     * @return Page<ChapterOfStory>
     */
    Page< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus,
                                                                      Pageable pageable);
    
    /**
     * Lấy Chapter Theo Truyện
     *
     * @param storyId
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    List< ChapterOfStory > findByStory_IdAndStatusInOrderBySerialDesc(Long storyId,
                                                                      List< Integer > listChapterStatus);
}
