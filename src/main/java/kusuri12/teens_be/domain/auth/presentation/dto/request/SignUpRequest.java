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

        @NotBlank(message = "{validation.password.blank}")
        @Size(min = 8, max = 60, message = "{validation.password.length}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9._-]{8,60}$",
                message = "{validation.password.pattern}")
        String password,

        @NotBlank(message = "{validation.password.blank}")
        String confirmPassword
) {
}