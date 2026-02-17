package kusuri12.teens_be.domain.forum.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
}
