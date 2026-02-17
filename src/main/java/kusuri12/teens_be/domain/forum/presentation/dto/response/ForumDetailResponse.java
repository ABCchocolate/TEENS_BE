package kusuri12.teens_be.domain.forum.presentation.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ForumDetailResponse(
        Long id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt,
        List<CommentResponse> comments
) {
}