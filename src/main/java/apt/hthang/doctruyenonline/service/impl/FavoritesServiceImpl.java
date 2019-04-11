package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Favorites;
import apt.hthang.doctruyenonline.projections.ChapterSummary;
import apt.hthang.doctruyenonline.repository.FavoritesRepository;
import apt.hthang.doctruyenonline.service.FavoritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Huy Thang
 */

@Service
public class FavoritesServiceImpl implements FavoritesService {
    
    private final FavoritesRepository favoritesRepository;
    
    @Autowired
    public FavoritesServiceImpl(FavoritesRepository favoritesRepository) {
        this.favoritesRepository = favoritesRepository;
    }
    
    /**
     * Lấy Chapter Mới đọc
     *
     * @param userId
     * @param storyId
     * @return ChapterSummary
     */
    @Override
    public Chapter findChapterReadByUser(Long userId, Long storyId) {
        Optional< Favorites > ufavorites = favoritesRepository
                .findTopByUser_IdAndChapter_Story_IdOrderByDateViewDesc(userId, storyId);
        return ufavorites.map(Favorites::getChapter).orElse(null);
    }
}
