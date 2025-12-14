package kusuri12.teens_be.domain.information.domain;

import jakarta.persistence.*;
import kusuri12.teens_be.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Information {

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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean pinned = false;

    @Column
    private String imageUrl;

    // 수정 메서드 추가
    public void updateTitleAndContent(String title, String content, boolean pinned, String imageUrl) {
        this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.imageUrl = imageUrl;
    }
}