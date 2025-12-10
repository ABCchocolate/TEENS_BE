package kusuri12.teens_be.domain.user.presentation.dto.response;

import lombok.Builder;

@Builder
public record UserMeResponse(
        String username,
        String nickname,
        String email,
        int forumCount,
        int commentCount,
        String profileImg
) {
}
