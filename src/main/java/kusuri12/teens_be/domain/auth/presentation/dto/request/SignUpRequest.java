package kusuri12.teens_be.domain.auth.presentation.dto.request;

public record SignUpRequest(
        String username,
        String email,
        String password
) {
}