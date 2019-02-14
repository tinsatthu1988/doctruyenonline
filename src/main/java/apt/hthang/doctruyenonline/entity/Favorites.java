package apt.hthang.doctruyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "locationIP", nullable = false, length = 50)
    private String locationIP;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "dateView", length = 19)
    private Date dateView;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "chapterId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Chapter chapter;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
