package apt.hthang.doctruyenonline.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Đời Không Như Là Mơ
 */
@Embeddable
@Data
public class UserFollowPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "userId", nullable = false)
    private long userId;
    @Column(name = "storyId", nullable = false)
    private long storyId;

}
