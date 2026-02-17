package kusuri12.teens_be.domain.forum.presentation.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ForumListResponse(
        Long id,
        String title,
        String authorName,
        LocalDateTime createdAt,
        Long commentCount
) {
}