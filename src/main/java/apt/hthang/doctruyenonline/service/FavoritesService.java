package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Chapter;

/**
 * @author Huy Thang
 */
public interface FavoritesService {
    
    /**
     * Lấy Chapter Mới đọc
     *
     * @param userId
     * @param storyId
     * @return Chapter
     */
    Chapter findChapterReadByUser(Long userId, Long storyId);
}

