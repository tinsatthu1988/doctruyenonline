package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Huy Thang
 */
public interface ChapterService {
    
    /**
     * Tìm Chapter Đầu Tiên Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    ChapterSummary findChapterHeadOfStory(Long storyId,
                                          List< Integer > listStatus);
    
    /**
     * Tìm Chapter Mới Nhất Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    ChapterSummary findChapterNewOfStory(Long storyId,
                                         List< Integer > listStatus);
    
    /**
     * Lấy danh sách Chapter của Truyện Theo
     *
     * @param storyId
     * @param pagenumber
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    Page< ChapterOfStory > getListChapterOfStory(Long storyId,
                                                 Integer pagenumber,
                                                 List< Integer > listChapterStatus,
                                                 Integer type);
}
