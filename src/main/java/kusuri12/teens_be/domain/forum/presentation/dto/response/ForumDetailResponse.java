package kusuri12.teens_be.domain.forum.presentation.dto.response;

import kusuri12.teens_be.domain.forum.domain.Forum;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.global.validation.util.FieldUtil;

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
                FieldUtil.notNull(forum.getUser(), UserErrorCode.USER_NOT_FOUND).getNickname(),
                forum.getCreatedAt()
        );
    }
}