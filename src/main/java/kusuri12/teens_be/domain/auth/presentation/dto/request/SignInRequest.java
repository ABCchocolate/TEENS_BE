package kusuri12.teens_be.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) { }
