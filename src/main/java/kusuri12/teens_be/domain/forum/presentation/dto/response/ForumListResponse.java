package kusuri12.teens_be.domain.forum.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ForumListResponse {
    private Long id;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    private Long commentCount;
}