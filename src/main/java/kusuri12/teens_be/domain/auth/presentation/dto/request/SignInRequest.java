package kusuri12.teens_be.domain.auth.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import kusuri12.teens_be.domain.user.domain.User;

public record SignInRequest(
        @NotBlank
        String username,

        @NotBlank
        String password
) {
        public static SignInRequest from(User user) {
                return new SignInRequest(user.getUsername(), user.getPassword());
        }
}
