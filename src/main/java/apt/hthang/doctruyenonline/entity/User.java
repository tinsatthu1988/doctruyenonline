package apt.hthang.doctruyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "displayName"),
        @UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "username")})
@Data
@NoArgsConstructor
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username;
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @Column(name = "displayName", unique = true)
    private String displayName;
    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;
    @Column(name = "notification")
    private String notification;
    @Column(name = "gold", precision = 22)
    private Double gold;
    @Column(name = "avatar")
    private String avatar;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "modifiedDate", length = 19)
    private Date modifiedDate;
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
    @Size(min = 6, max = 13, message = "{hthang.truyenmvc.user.passwordRegister.size.message}")
    private String passwordRegister;
    
    @Transient
    @Size(min = 6, max = 13, message = "{hthang.truyenmvc.user.passwordRegister.size.message}")
    private String passwordRegisterConfirm;
    
}
