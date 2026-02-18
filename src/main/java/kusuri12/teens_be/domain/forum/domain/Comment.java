package kusuri12.teens_be.domain.forum.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.common.BaseTimeEntity;
import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.global.validation.util.FieldUtil;
import lombok.*;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id")
    private Forum forum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Comment(String content, Forum forum, User user) {
        this.content = content;
        this.forum = forum;
        this.user = user;
    }

    public static Comment of(String content, Forum forum, User user) {
        return new Comment(
                FieldUtil.hasText(content, ForumErrorCode.CONTENT_EMPTY),
                FieldUtil.notNull(forum, ForumErrorCode.FORUM_NOT_FOUND),
                FieldUtil.notNull(user, UserErrorCode.USER_NOT_FOUND)
        );
    }

    // 수정 메서드 추가
    public void updateContent(String content) {
        this.content = content;
    }
}