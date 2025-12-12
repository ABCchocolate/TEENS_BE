package kusuri12.teens_be.domain.auth.presentation.dto.request;

public record SignInRequest(
        String username,
        String password
) {
}
