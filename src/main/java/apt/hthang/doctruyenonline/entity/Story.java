package apt.hthang.doctruyenonline.entity;

import apt.hthang.doctruyenonline.annotations.CheckUpload;
import apt.hthang.doctruyenonline.annotations.ExtensionUpload;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Đời Không Như Là Mơ
 */
@Entity
@Table(name = "story")
@Data
public class Story implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @NotEmpty(message = "{hthang.truyenonline.story.vnName.empty.message}")
    @Column(name = "vnName", nullable = false)
    private String vnName;
    @Column(name = "cnName")
    @NotEmpty(message = "{hthang.truyenonline.story.cnName.empty.message}")
    private String cnName;
    @Column(name = "cnLink")
    @NotEmpty(message = "{hthang.truyenonline.story.cnLink.empty.message}")
    private String cnLink;
    @Column(name = "images", nullable = false, length = 150)
    private String images;
    @Column(name = "author", nullable = false)
    @NotEmpty(message = "{hthang.truyenonline.story.author.empty.message}")
    private String author;
    @NotEmpty(message = "{hthang.truyenonline.story.infomation.empty.message}")
    @Column(name = "infomation", columnDefinition = "TEXT")
    private String infomation;
    @Column(name = "countAppoint")
    private Integer countAppoint;
    @Column(name = "countView")
    private Integer countView;
    @Column(name = "rating", precision = 12, scale = 0)
    private Float rating;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @Column(name = "price", precision = 22, scale = 0)
    private Double price;
    @Column(name = "timeDeal")
    private Integer timeDeal;
    @Column(name = "dealStatus")
    private Integer dealStatus;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "updateDate", length = 19)
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userPosted", referencedColumnName = "id", nullable = false)
    private User user;
    @NotEmpty(message = "{hthang.truyenonline.story.category.empty.message}")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "story_category",
            joinColumns = {@JoinColumn(name = "storyId", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "categoryId", nullable = false)})
    private List< Category > categoryList;
    @CheckUpload
    @Transient
    private MultipartFile uploadfile;
    
    @ExtensionUpload
    @Transient
    private MultipartFile editfile;
    
    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsStatusUtils.STORY_STATUS_GOING_ON;
        }
        if (rating == null) {
            rating = (float) 0;
        }
        if (countAppoint == null) {
            countAppoint = 0;
        }
        if (countView == null) {
            countView = 0;
        }
        if (updateDate == null) {
            updateDate = DateUtils.getCurrentDate();
        }
        if (dealStatus == null) {
            dealStatus = 0;
        }
        if (timeDeal == null) {
            timeDeal = 0;
        }
        if (price == null) {
            price = (double) 0;
        }
    }
}
