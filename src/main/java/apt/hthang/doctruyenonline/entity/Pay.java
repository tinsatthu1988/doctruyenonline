package apt.hthang.doctruyenonline.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Huy Thang
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
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "status")
    private Integer status;

}
