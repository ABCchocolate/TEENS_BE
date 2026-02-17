package kusuri12.teens_be.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kusuri12.teens_be.global.validation.anotation.Ban;

public record SignUpRequest(

        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]+$")
        @Ban
        String username,

        @NotBlank
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$")
        String email,

        @NotBlank
        @Size(min = 8, max = 60)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W)(?=\\S+$).+$")
        String password,

        @NotBlank
        String confirmPassword
) {
}