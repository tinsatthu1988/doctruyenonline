package apt.hthang.doctruyenonline.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Đời Không Như Là Mơ
 */
@Entity
@Table(name = "pay", schema = "")
@Data
public class Pay implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @JoinColumn(name = "userSend", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userSend;
    @JoinColumn(name = "userReceived", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userReceived;
    @Column(name = "money", precision = 22, scale = 0)
    private Double money;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createDate", length = 19)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapterId", referencedColumnName = "id")
    private Chapter chapter;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyId", referencedColumnName = "id")
    private Story story;
    @Column(name = "vote")
    private Integer vote;
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "status")
    private Integer status;
    
    
    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = DateUtils.getCurrentDate();
        }
        if (status == null) {
            status = ConstantsStatusUtils.PAY_COMPLETED;
        }
        if (vote == null) {
            vote = (Integer) 0;
        }
    }
}
