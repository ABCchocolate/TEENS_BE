package kusuri12.teens_be.domain.information.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.common.BaseTimeEntity;
import kusuri12.teens_be.domain.forum.exception.ForumErrorCode;
import kusuri12.teens_be.domain.information.exception.InfoErrorCode;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import lombok.*;
import org.springframework.util.Assert;

@Entity
@Table(name = "information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Information extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private boolean pinned = false;

    @Column
    private String imageUrl;

    private Information(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Information of(String title, String content, User user) {
        Assert.hasText(title, InfoErrorCode.TITLE_EMPTY.getMessage());
        Assert.hasText(content, InfoErrorCode.CONTENT_EMPTY.getMessage());
        Assert.notNull(user, UserErrorCode.USER_NOT_FOUND.getMessage());

        return new Information(title, content, user);
    }

    // 수정 메서드 추가
    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}