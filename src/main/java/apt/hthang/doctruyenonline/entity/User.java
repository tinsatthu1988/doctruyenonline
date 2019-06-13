package apt.hthang.doctruyenonline.entity;

import apt.hthang.doctruyenonline.annotations.EqualFields;
import apt.hthang.doctruyenonline.annotations.Unique;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import apt.hthang.doctruyenonline.validator.OnUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "displayName"),
        @UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "username")})
@Data
@NoArgsConstructor
@EqualFields(baseField = "passwordRegister", matchField = "passwordRegisterConfirm",
        message = "{hthang.truyenonline.user.password.EqualFields.message}")
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @NotEmpty(message = "{hthang.truyenonline.user.username.empty.message}")
    @Unique(service = UserService.class, fieldName = "username",
            message = "{hthang.truyenonline.user.username.unique.message}", groups = OnUpdate.class)
    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username;
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @Column(name = "displayName", unique = true)
    private String displayName;
    @Column(name = "email", unique = true, nullable = false, length = 150)
    @NotEmpty(message = "{hthang.truyenonline.user.email.empty.message}")
    @Email(message = "{hthang.truyenonline.user.email.email.message}")
    @Unique(service = UserService.class, fieldName = "email",
            message = "{hthang.truyenonline.user.email.unique.message}", groups = OnUpdate.class)
    private String email;
    @Column(name = "notification")
    private String notification;
    @Min(value = 0)
    @Column(name = "gold", precision = 22, scale = 0)
    private Double gold;
    @Column(name = "avatar")
    private String avatar;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "createDate", length = 19)
    private Date createDate;
    @Column(name = "status")
    private Integer status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", catalog = "truyendb", joinColumns = {
            @JoinColumn(name = "userId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "roleId", nullable = false, updatable = false)})
    private Collection< Role > roleList;
    @Transient
    @Size(min = 6, max = 13, message = "{hthang.truyenonline.user.passwordRegister.size.message}")
    private String passwordRegister;
    
    @Transient
    @Size(min = 6, max = 13, message = "{hthang.truyenonline.user.passwordRegister.size.message}")
    private String passwordRegisterConfirm;
    
    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsStatusUtils.USER_ACTIVED;
        }
        if (gold == null) {
            gold = (double) 0;
        }
    }
}
