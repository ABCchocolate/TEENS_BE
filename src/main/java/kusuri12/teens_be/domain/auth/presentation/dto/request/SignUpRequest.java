package kusuri12.teens_be.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "{validation.username.blank}")
        @Size(min = 2, max = 20, message = "{validation.username.length}")
        String username,

        @NotBlank(message = "{validation.email.blank}")
        String email,

        @NotBlank
        @Size(min = 8, max = 60)
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W)(?=\\S+$).+$")
        String password,

        @NotBlank
        String confirmPassword
) {
}