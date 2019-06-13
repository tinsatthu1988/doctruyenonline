package apt.hthang.doctruyenonline.entity;

import apt.hthang.doctruyenonline.annotations.Unique;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import apt.hthang.doctruyenonline.validator.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Đời Không Như Là Mơ on 28/01/2019
 * @project truyenonline-multi-model
 */
@Entity
@Table(name = "category", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Data
@NoArgsConstructor
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Integer id;
    @NotEmpty(message = "Tên Thể Loại Không Được để trống")
    @Unique(service = CategoryService.class, fieldName = "name",
            message = "Đã có thể loại tên này", groups = OnUpdate.class)
    @Column(name = "name", unique = true, nullable = false, length = 150)
    private String name;
    @Column(name = "metatitle", nullable = false, length = 150)
    private String metatitle;
//    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @Column(name = "createBy", length = 150)
    private String createBy;
    @Column(name = "status")
    private Integer status;
    
    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsStatusUtils.CATEGORY_ACTIVED;
        }
    }
}
