package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Đời Không Như Là Mơ on 20/11/2018
 * @project truyenonline
 */
public interface StorySummary {

    Long getId();

    String getVnName();

    String getCnName();

    String getCnLink();

    String getImages();

    String getAuthor();

    //Lấy Thông Tin
    String getInfomation();

    //Lấy điểm đề cử
    Long getCountAppoint();

    //Lấy Điểm đánh giá
    Float getRating();

    //Lấy ID Converter
    @Value("#{target.user.id}")
    Long getUserId();

    //Lấy Chapter Đầu Tiên
    @Value("#{@myComponent.getChapterHead(target.id)}")
    ChapterSummary getChapterHead();

    //Lấy Chapter Mới Nhất
    @Value("#{@myComponent.getNewChapter(target.id)}")
    ChapterSummary getChapterNew();

    List< CategorySummary > getCategoryList();

    Integer getStatus();
}
