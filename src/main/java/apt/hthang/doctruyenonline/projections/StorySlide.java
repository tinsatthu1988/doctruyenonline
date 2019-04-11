package apt.hthang.doctruyenonline.projections;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface StorySlide {
    
    Long getId();
    
    String getVnName();
    
    String getCnName();
    
    String getCnLink();
    
    String getImages();
    
    String getAuthor();
    
    //Lấy Thông Tin
    String getInfomation();
}
