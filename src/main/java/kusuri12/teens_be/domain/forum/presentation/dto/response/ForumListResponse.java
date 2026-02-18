package kusuri12.teens_be.domain.forum.presentation.dto.response;

import kusuri12.teens_be.domain.forum.domain.Forum;
import java.time.LocalDateTime;

public record ForumListResponse(
        Long id,
        String title,
        String authorName,
        LocalDateTime createdAt,
        Long commentCount
) {
    public static ForumListResponse of(Forum forum, Long commentCount) {
        return new ForumListResponse(
                forum.getId(),
                forum.getTitle(),
                forum.getUser().getNickname(),
                forum.getCreatedAt(),
                commentCount
        );
    }
}