package apt.hthang.doctruyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Huy Thang
 */
@Entity
@Table(name = "user_follow")
@Data
@NoArgsConstructor
public class UserFollow implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false)),
            @AttributeOverride(name = "storyId", column = @Column(name = "storyId", nullable = false))})
    private UserFollowPK userFollowPK;
    @JoinColumn(name = "storyId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Story story;
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    
}
