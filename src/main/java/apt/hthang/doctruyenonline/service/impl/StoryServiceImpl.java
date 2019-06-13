package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.*;
import apt.hthang.doctruyenonline.repository.StoryRepository;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsPayTypeUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
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
     * @param historyStatus
     * @param listStatus
     * @param startDate
     * @param endDate
     * @param page
     * @param size
     * @return Page<StoryTop>
     */
    @Override
    public Page< StoryTop > findStoryTopViewByCategoryId(Integer categoryId, Integer historyStatus,
                                                         List< Integer > listStatus,
                                                         Date startDate, Date endDate,
                                                         int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopViewByCategory(categoryId, historyStatus,
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
    
    /**
     * Tìm Truyện Theo Id và ListStatus
     *
     * @param storyId
     * @param listStoryStatus
     * @return Story - nếu tồn tại truyện thỏa mãn điều kiện
     */
    @Override
    public Story findStoryByIdAndStatus(Long storyId, List< Integer > listStoryStatus) {
        return storyRepository
                .findStoryByIdAndStatusIn(storyId, listStoryStatus)
                .orElse(null);
    }
    
    /**
     * Lấy số lượng truyện đăng bởi User
     *
     * @param userId
     * @param listStoryDisplay
     * @return Long
     */
    @Override
    public Long countStoryByUser(Long userId, List< Integer > listStoryDisplay) {
        return storyRepository.countByUser_IdAndStatusIn(userId, listStoryDisplay);
    }
    
    /**
     * Lấy Page Truyện theo Status
     *
     * @param listChapterStatus -  danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái Story
     * @param page              - số trang
     * @param size              - độ dài trang
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryUpdateByStatus(List< Integer > listChapterStatus,
                                                       List< Integer > listStoryStatus,
                                                       int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .getPageStoryComplete(listChapterStatus, listStoryStatus, pageable);
    }
    
    /**
     * Lấy danh sách truyên top view trong khoảng theo status
     *
     * @param listStatus    - Danh sách trạng thái Story
     * @param startDate     - Ngày Bắt đầu
     * @param endDate       - Ngày kết thúc
     * @param historyStatus - Status history
     * @param page          - số trang
     * @param size          - độ dài trang
     * @return
     */
    @Override
    public Page< StoryTop > findStoryTopViewByStatuss(List< Integer > listStatus,
                                                      Date startDate, Date endDate,
                                                      Integer historyStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository.findStoryTopViewByStatus(listStatus, startDate, endDate, historyStatus, pageable);
    }
    
    /**
     * Lấy Danh sách Truyện Vip mới cập nhật
     *
     * @param listChapterStatus - danh sách trạng thái chapter
     * @param listStoryStatus   - danh sách trạng thái truyện
     * @param sDealStatus       - trạng thái truyện trả tiền
     * @param pagenumber        - biến số trang
     * @param size              - biến size
     * @return Page<StoryUpdate>
     */
    @Override
    public Page< StoryUpdate > findStoryVipUpdateByStatus(List< Integer > listChapterStatus, List< Integer > listStoryStatus, Integer sDealStatus, int pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findVipStoryNew(listChapterStatus, listStoryStatus, sDealStatus, pageable);
    }
    
    /**
     * Lấy danh sách truyện đã đăng của User
     *
     * @param userId
     * @param pagenumber
     * @param type
     * @param size
     * @param listStatus
     * @return
     */
    @Override
    public Page< StoryMember > findStoryByUserId(Long userId, List< Integer > listStatus,
                                                 int pagenumber, int type, Integer size) {
        Page< StoryMember > storyMembers;
        if (type == 1) {
            Pageable pageable = PageRequest.of(pagenumber - 1, ConstantsUtils.PAGE_SIZE_CHAPTER_OF_STORY);
            storyMembers = storyRepository.findByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStatus, pageable);
        } else {
            List< StoryMember > storyMemberList = storyRepository
                    .findAllByUser_IdAndStatusInOrderByCreateDateDesc(userId, listStatus);
            storyMembers = new PageImpl<>(storyMemberList);
        }
        return storyMembers;
    }
    
    /**
     * Lấy List Truyện Theo searchText
     *
     * @param searchText
     * @param listStatus
     * @return
     */
    @Override
    public List< StorySlide > findListStoryBySearchKey(String searchText, List< Integer > listStatus) {
        return storyRepository
                .findTop10ByVnNameContainingAndStatusInOrderByVnNameAsc(searchText, listStatus);
    }
    
    /**
     * Lấy List Truyện đăng bởi User
     *
     * @param id         - id của User đăng
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @param status     - Trạng Thái Truyện
     * @return
     */
    @Override
    public Page< StoryUser > findPageStoryByUser(Long id, int pagenumber, Integer size, Integer status) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return storyRepository.findByUser_IdAndStatusOrderByUpdateDateDesc(id, status, pageable);
    }
    
    /**
     * Tìm Truyện Theo id
     *
     * @param id
     * @return
     */
    @Override
    public Story findStoryById(Long id) {
        return storyRepository.findById(id).orElse(null);
    }
    
    /**
     * Xóa truyện Theo Id
     *
     * @param id
     */
    @Override
    public void deleteStoryById(Long id) {
        storyRepository.deleteById(id);
    }
    
    /**
     * Đăng Truyện mới
     *
     * @param story
     * @return
     */
    @Override
    public boolean newStory(Story story) {
        Story storyRes = storyRepository.save(story);
        return storyRes.getId() != null;
    }
    
    /**
     * Cập Nhật Truyện
     *
     * @param story
     * @return
     */
    @Override
    public boolean updateStory(Story story) {
        Optional< Story > storyOptional = storyRepository.findById(story.getId());
        Story storyEdit = storyOptional.get();
        storyEdit.setAuthor(story.getAuthor());
        storyEdit.setVnName(story.getVnName());
        storyEdit.setCnName(story.getCnName());
        storyEdit.setCnLink(story.getCnLink());
        storyEdit.setInfomation(story.getInfomation());
        storyEdit.setCategoryList(story.getCategoryList());
        storyEdit.setStatus(story.getStatus());
        if(story.getDealStatus() !=null){
            storyEdit.setDealStatus(story.getDealStatus());
            if(story.getDealStatus() == ConstantsStatusUtils.STORY_VIP){
                if(story.getPrice() != null){
                    storyEdit.setPrice(story.getPrice());
                }
                if(story.getTimeDeal() != null){
                    storyEdit.setTimeDeal(story.getTimeDeal());
                }
            }
        }
        if (story.getImages() != null && !story.getImages().isEmpty()) {
            storyEdit.setImages(story.getImages());
        }
        storyRepository.save(storyEdit);
        return true;
    }
    
    /**
     * Lấy List Truyện Top Đề cử Trong Khoảng
     *
     * @param page
     * @param size
     * @param startDate
     * @param endDate
     * @return Page<TopStory>
     */
    @Override
    public Page< StoryTop > getTopStoryAppoind(int page, int size, Date startDate, Date endDate) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return storyRepository
                .findTopStoryAppoind(ConstantsListUtils.LIST_STORY_DISPLAY, startDate, endDate, ConstantsPayTypeUtils.PAY_APPOINT_TYPE, ConstantsStatusUtils.PAY_COMPLETED, pageable);
    }
    
    /**
     * @param date
     * @return
     */
    @Override
    public Long countNewUserInDate(Date date) {
        return storyRepository.countByCreateDateGreaterThanEqual(date);
    }
    
    @Override
    public Page< StoryAdmin > findStoryInAdmin(Integer pagenumber, Integer size, Integer type, String search) {
        Pageable pageable = PageRequest.of(pagenumber-1, size);
        if (type == -1) {
            if (search.trim().isEmpty()) {
                return storyRepository.findByOrderByIdDesc(pageable);
            }
            return storyRepository.findByVnNameContainingOrderByIdDesc(search, pageable);
        } else if (type == 3) {
            if (search.trim().isEmpty()) {
                return storyRepository.findByDealStatusOrderByIdDesc(ConstantsStatusUtils.STORY_STATUS_GOING_ON, pageable);
            }
            return storyRepository.findByDealStatusAndVnNameContainingOrderByIdDesc(ConstantsStatusUtils.STORY_STATUS_GOING_ON, search, pageable);
        } else {
            if (search.trim().isEmpty()) {
                return storyRepository.findByStatusOrderByIdDesc(type, pageable);
            }
            return storyRepository.findByVnNameContainingAndStatusOrderByIdDesc(search, type, pageable);
        }
    }
    
    /**
     * Lấy số lượng truyện đăng bởi User
     *
     * @param userId
     * @param status
     * @return Long
     */
    public Long countStoryByUserWithStatus(Long userId, Integer status) {
        return storyRepository.countByUser_IdAndStatus(userId, status);
    }
    
    ;
}