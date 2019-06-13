package apt.hthang.doctruyenonline.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Đời Không Như Là Mơ
 */
@Entity
@Table(name = "information")
@Data
@NoArgsConstructor
public class Information implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name = "introduce", columnDefinition = "TEXT")
    private String introduce;
    @Column(name = "email", nullable = false, length = 150)
    private String email;
    @Column(name = "phone", length = 13)
    private String phone;
    @Column(name = "skype", length = 50)
    private String skype;
    @Column(name = "logo", nullable = false, length = 150)
    private String logo;
    @Column(name = "favicon", nullable = false, length = 150)
    private String favicon;

}
