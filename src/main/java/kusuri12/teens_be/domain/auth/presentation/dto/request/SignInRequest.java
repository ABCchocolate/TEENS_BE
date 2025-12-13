package kusuri12.teens_be.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "{validation.username.blank}")
        String username,

        @NotBlank(message = "{validation.password.blank}")
        String password
) {
}
