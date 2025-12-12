package kusuri12.teens_be.domain.user.presentation.dto.request;

public record PasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
