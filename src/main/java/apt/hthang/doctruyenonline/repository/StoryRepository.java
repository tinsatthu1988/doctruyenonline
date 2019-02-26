package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Thang
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
     * @return Page<TopStory>
     */
    @Query(value = ConstantsQueryUtils.STORY_NEW_UPDATE_BY_CATEGORY,
            countQuery = ConstantsQueryUtils.COUNT_STORY_NEW_UPDATE_BY_CATEGORY,
            nativeQuery = true)
    Page< StoryUpdate > findStoryNewByCategory(@Param("categoryId") Integer cID,
                                               @Param("storyStatus") List< Integer > listStatus,
                                               @Param("chapterStatus") List< Integer > listChStatus,
                                               Pageable pageable);
}
