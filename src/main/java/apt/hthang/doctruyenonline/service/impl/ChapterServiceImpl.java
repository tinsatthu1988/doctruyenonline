package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import apt.hthang.doctruyenonline.repository.ChapterRepository;
import apt.hthang.doctruyenonline.repository.StoryRepository;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    
    private final static Logger logger = LoggerFactory.getLogger(ChapterServiceImpl.class);
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private StoryRepository storyRepository;
    
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
    
    /**
     * Đếm Số Chương Của Truyện
     *
     * @param id
     * @return
     */
    @Override
    public Long countChapterByStory(Long id) {
        return chapterRepository.countChapterByStory_Id(id);
    }
    
    @Override
    public boolean saveNewChapter(Chapter chapter) {
        chapter.setWordCount(WebUtils.countWords(chapter.getContent()));
        chapter.setContent(chapter.getContent().replaceAll("\n", "<br />"));
        Chapter newChapter = chapterRepository.save(chapter);
        if (newChapter.getId() != null) {
            Story story = storyRepository.findById(newChapter.getStory().getId()).get();
            story.setUpdateDate(newChapter.getCreateDate());
            storyRepository.save(story);
            return true;
        }
        return false;
    }
    
    
    /**
     * Tìm Kiếm Chapter theo
     *
     * @param storyId
     * @param userId
     * @param type
     * @return
     */
    @Override
    public Page< ChapterOfStory > findByStoryIdAndUserId(Long storyId, Long userId, Integer type, Integer pagenumber) {
        Pageable pageable;
        if (type == 1)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("serial"));
        else if (type == 2)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("createDate").descending());
        else if (type == 3)
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("createDate"));
        else
            pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_DEFAULT, Sort.by("serial").descending());
        return chapterRepository.findByUser_IdAndStory_Id(userId, storyId, pageable);
    }
    
    /**
     * @param chapterId
     * @param storyId
     * @param number
     * @return
     */
    @Override
    public boolean checkChapterBySerialAndId(long chapterId, Long storyId, float number) {
        return chapterRepository.existsByIdNotAndStory_IdAndSerial(chapterId, storyId, number);
    }
    
    /**
     * @param id
     * @param number
     * @return
     */
    @Override
    public boolean checkChapterBySerial(Long id, float number) {
        return chapterRepository.existsByStory_IdAndSerial(id, number);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Chapter findChapterById(Long id) {
        return chapterRepository.findById(id).orElse(null);
    }
    
    /**
     * @param chapter
     * @return
     */
    @Override
    public boolean updateChapter(Chapter chapter) {
        try {
            Chapter editChapter = chapterRepository.findById(chapter.getId()).get();
            editChapter.setSerial(chapter.getSerial());
            editChapter.setStatus(chapter.getStatus());
            editChapter.setName(chapter.getName());
            editChapter.setChapterNumber(chapter.getChapterNumber());
            editChapter.setContent(chapter.getContent());
            chapterRepository.save(editChapter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     *
     */
    @Override
    public void updateStatusChapterVip() {
        chapterRepository.updateStatusChapterVip(ConstantsStatusUtils.CHAPTER_ACTIVED,
                ConstantsStatusUtils.CHAPTER_VIP_ACTIVED);
    }
    
    /**
     * @param date
     * @return
     */
    @Override
    public Long countNewChapterInDate(Date date) {
        return chapterRepository.countByCreateDateGreaterThanEqual(date);
    }
}
