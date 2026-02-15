package kusuri12.teens_be.domain.forum.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.common.BaseTimeEntity;
import kusuri12.teens_be.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Builder
    public Comment(String content, Forum forum, User user) {
        this.content = content;
        this.forum = forum;
        this.user = user;
    }

    // 수정 메서드 추가
    public void updateContent(String content) {
        this.content = content;
    }
}