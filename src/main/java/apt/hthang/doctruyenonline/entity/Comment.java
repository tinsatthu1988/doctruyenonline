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
@Table(name = "comment")
@Data
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "storyId", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Story story;
    @JoinColumn(name = "userPosted", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
