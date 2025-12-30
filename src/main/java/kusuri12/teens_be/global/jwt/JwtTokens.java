package kusuri12.teens_be.global.jwt;

import lombok.Builder;

@Builder
public record JwtTokens(
        String accessToken,
        String refreshToken
) {}