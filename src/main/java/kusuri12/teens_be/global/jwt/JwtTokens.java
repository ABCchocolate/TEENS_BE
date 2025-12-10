package kusuri12.teens_be.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JwtTokens {

    private final String accessToken;
    private final String refreshToken;
    private final LocalDateTime accessTokenExpiresAt;
    private final LocalDateTime refreshTokenExpiresAt;
}
