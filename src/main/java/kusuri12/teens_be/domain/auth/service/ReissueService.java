package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.RefreshTokenRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.TokenResponse;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse reissue(RefreshTokenRequest request) {
        String newAccessToken = jwtTokenProvider.reissueAccessToken(request.refreshToken());
        String newRefreshToken = jwtTokenProvider.reissueRefreshToken(request.refreshToken());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
