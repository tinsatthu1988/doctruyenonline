package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface FavoritesRepository extends JpaRepository< Favorites, Long > {
}
