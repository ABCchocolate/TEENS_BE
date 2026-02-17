package kusuri12.teens_be.domain.auth.presentation.dto.response;

import kusuri12.teens_be.global.security.userdetails.AuthDetails;

public record SignInResponse(
        String accessToken,
        String refreshToken,
        AuthDetails authDetails
) {
}