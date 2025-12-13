package kusuri12.teens_be.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameRequest(
        @NotBlank(message = "{validation.nickname.blank}")
        @Size(min = 2, max = 20, message = "{validation.nickname.length}")
        String nickname
) {
}
