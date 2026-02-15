package kusuri12.teens_be.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kusuri12.teens_be.global.validation.anotation.Ban;

public record NicknameRequest(
        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]+$")
        @Ban
        String nickname
) {
}
