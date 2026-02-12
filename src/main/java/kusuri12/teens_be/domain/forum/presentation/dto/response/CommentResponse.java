package kusuri12.teens_be.domain.forum.presentation.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
}
