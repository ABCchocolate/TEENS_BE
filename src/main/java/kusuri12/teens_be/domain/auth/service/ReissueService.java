package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.RefreshTokenRequest;
import kusuri12.teens_be.domain.auth.presentation.dto.response.TokenResponse;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.jwt.exception.InvalidJwtException;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse reissue(RefreshTokenRequest request) {

        String submittedRefreshToken = request.refreshToken();

        // 1. Refresh Token 유효성 검증 및 정보 추출
        if (!jwtTokenProvider.validateToken(submittedRefreshToken)) {
            throw InvalidJwtException.EXCEPTION;
        }

        // 2. Refresh Token에서 사용자 ID (Username) 추출
        String username = jwtTokenProvider.getUsername(submittedRefreshToken);

        // 3. Redis에 저장된 Refresh Token 조회
        String storedRefreshToken = jwtTokenProvider.getRefreshToken(username);

        // 4. 토큰 일치 여부 검증
        // 클라이언트가 보낸 토큰이 Redis에 저장된 토큰과 다르면, 토큰 탈취로 간주하고 모두 무효화
        if (!submittedRefreshToken.equals(storedRefreshToken)) {
            jwtTokenProvider.deleteRefreshToken(username);
            throw InvalidJwtException.EXCEPTION;
        }

        String newAccessToken = jwtTokenProvider.reissueAccessToken(request.refreshToken());
        String newRefreshToken = jwtTokenProvider.reissueRefreshToken(request.refreshToken());
        jwtTokenProvider.saveRefreshToken(username, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
