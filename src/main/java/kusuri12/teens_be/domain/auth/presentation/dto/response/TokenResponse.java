package kusuri12.teens_be.domain.auth.presentation.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
