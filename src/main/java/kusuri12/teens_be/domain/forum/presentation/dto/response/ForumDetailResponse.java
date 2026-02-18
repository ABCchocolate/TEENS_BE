package kusuri12.teens_be.domain.forum.presentation.dto.response;

import kusuri12.teens_be.domain.forum.domain.Forum;

import java.time.LocalDateTime;

public record ForumDetailResponse(
        Long id,
        String title,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
    public static ForumDetailResponse from(Forum forum) {
        return new ForumDetailResponse(
                forum.getId(),
                forum.getTitle(),
                forum.getContent(),
                forum.getUser().getNickname(),
                forum.getCreatedAt()
        );
    }
}