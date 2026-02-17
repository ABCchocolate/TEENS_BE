package kusuri12.teens_be.global.security.jwt;

import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import lombok.Builder;

@Builder
public record JwtTokens(
        String accessToken,
        RefreshToken refreshToken
) {}