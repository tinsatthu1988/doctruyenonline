package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import apt.hthang.doctruyenonline.repository.ChapterRepository;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

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
    
    /**
     * Lấy Chapter ID Chương Đầu
     *
     * @param storyId
     * @param listStatus
     * @return Long
     */
    @Override
    public ChapterSummary findChapterHeadOfStory(Long storyId, List< Integer > listStatus) {
        return chapterRepository.findChapterHead(storyId, listStatus).orElse(null);
    }
    
    /**
     * Tìm Chapter Mới Nhất Của Truyện
     *
     * @param storyId
     * @param listStatus
     * @return ChapterSummary - nếu tìm thấy Chapter / null - nếu không tìm thấy
     */
    @Override
    public ChapterSummary findChapterNewOfStory(Long storyId, List< Integer > listStatus) {
        return chapterRepository.findChapterNew(storyId, listStatus).orElse(null);
    }
    
    /**
     * Lấy danh sách Chapter của Truyện Theo
     *
     * @param storyId
     * @param pagenumber
     * @param listChapterStatus
     * @return Page<ChapterOfStory>
     */
    @Override
    public Page< ChapterOfStory > getListChapterOfStory(Long storyId,
                                                        Integer pagenumber,
                                                        List< Integer > listChapterStatus,
                                                        Integer type) {
        Page< ChapterOfStory > chapterOfStoryPage;
        if (type == 1) {
            Pageable pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_CHAPTER_OF_STORY);
            chapterOfStoryPage = chapterRepository
                    .findByStory_IdAndStatusInOrderBySerialDesc(storyId, listChapterStatus, pageable);
        } else {
            List< ChapterOfStory > chapterOfStoryList = chapterRepository
                    .findByStory_IdAndStatusInOrderBySerialDesc(storyId, listChapterStatus);
            chapterOfStoryPage = new PageImpl<>(chapterOfStoryList);
        }
        return chapterOfStoryPage;
    }
    
    /**
     * Lấy số lượng chương đã đăng thành công của User
     *
     * @param userId
     * @param listChapterDisplay
     * @return long
     */
    @Override
    public Long countChapterByUser(Long userId, List< Integer > listChapterDisplay) {
        return chapterRepository.countByUser_IdAndStatusIn(userId, listChapterDisplay);
    }
    
    /**
     * Tìm Chapter Theo Story ID và Chapter ID
     *
     * @param storyId
     * @param listStatusStory
     * @param chapterId
     * @param listStatusChapter
     * @return Chapter
     * @throws Exception
     */
    @Override
    public Chapter findChapterByStoryIdAndChapterID(Long storyId, List< Integer > listStatusStory, Long chapterId, List< Integer > listStatusChapter) throws Exception {
        return chapterRepository
                .findByStory_IdAndStory_StatusInAndIdAndStatusIn(storyId, listStatusStory,
                        chapterId, listStatusChapter)
                .orElseThrow(() -> new HttpMyException("Chương không tồn tại hoặc đã bị xóa!"));
    }
    
    /**
     * Cập Nhật Lượt Xem Của Chapter
     *
     * @param chapter
     * @throws Exception
     */
    @Override
    public void updateViewChapter(Chapter chapter) throws Exception {
        Chapter updateChapter = findChapterByStoryIdAndChapterID(chapter.getStory().getId(), ConstantsListUtils.LIST_STORY_DISPLAY,
                chapter.getId(), ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        updateChapter.setCountView(updateChapter.getCountView() + 1);
        Story story = updateChapter.getStory();
        story.setCountView(story.getCountView() + 1);
        updateChapter.setStory(story);
        chapterRepository.save(updateChapter);
    }
    
    /**
     * Lấy Chapter ID Trước
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    @Override
    public Long findPreviousChapterID(Float serial, Long storyId) {
        Optional< Long > previousID = chapterRepository
                .findPreviousChapter(serial, storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return previousID.orElseGet(() -> valueOf(0));
    }
    
    /**
     * Lấy Chapter ID Tiếp Theo
     *
     * @param serial
     * @param storyId
     * @return Long
     */
    @Override
    public Long findNextChapterID(Float serial, Long storyId) {
        Optional< Long > nextId = chapterRepository
                .findNextChapter(serial, storyId, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        return nextId.orElseGet(() -> valueOf(0));
    }
    
    /**
     * Tìm kiếm Chapter theo
     *
     * @param chapterId  - ID của chapter
     * @param listStatus -  List các Trạng Thái của Chapter
     * @return chapter - nếu có dữ liệu thỏa mãn điều kiện / null - nếu không có dữ liệu thỏa mãn điều kiện
     */
    @Override
    public Chapter findChapterByIdAndStatus(Long chapterId, List< Integer > listStatus) {
        return chapterRepository.findChapterByIdAndStatusIn(chapterId, listStatus)
                .orElse(null);
    }
}
