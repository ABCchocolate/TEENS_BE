package kusuri12.teens_be.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordRequest(
        @NotBlank(message = "{validation.password.blank}")
        String currentPassword,

        @NotBlank(message = "{validation.password.blank}")
        @Size(min = 8, max = 60, message = "{validation.password.length}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9._-]{8,60}$",
                message = "{validation.password.pattern}")
        String newPassword,

        @NotBlank(message = "{validation.password.blank}")
        String confirmPassword
) {
}