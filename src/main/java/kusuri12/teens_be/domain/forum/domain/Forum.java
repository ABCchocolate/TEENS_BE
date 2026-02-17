package kusuri12.teens_be.domain.forum.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.common.BaseTimeEntity;
import kusuri12.teens_be.domain.user.domain.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forum")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Forum extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Forum(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 수정 메서드
    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
    }
}