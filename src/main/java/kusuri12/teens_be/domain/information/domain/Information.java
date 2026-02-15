package kusuri12.teens_be.domain.information.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.common.BaseTimeEntity;
import kusuri12.teens_be.domain.user.domain.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Builder
    public Information(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
    // 수정 메서드 추가
    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}