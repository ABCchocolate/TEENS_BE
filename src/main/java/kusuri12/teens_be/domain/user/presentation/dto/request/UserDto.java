package kusuri12.teens_be.domain.user.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserMeResponse {
        private Long id;
        private String username;
        private String nickname;
        private String email;
        private String role;
        private Long forumCount;
        private Long commentCount;
    }
}