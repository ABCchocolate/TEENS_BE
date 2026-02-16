package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.ReissueRequest;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.jwt.JwtProperties;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.jwt.JwtTokens;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final RedisService redisService;
    private final UserRepository userRepository;

    public static final String REFRESH_TOKEN_KEY = "RefreshToken:";

    public JwtTokens execute(String ExpiredAccessToken, ReissueRequest request) {

        String refreshToken = request.refreshToken();

        // 1. Refresh Token 유효성 검증 및 정보 추출
        String username = jwtTokenProvider.parse(refreshToken).getSubject();

        // 2. Redis에 저장된 Refresh Token 조회
        Object storedRefreshToken = redisService.get(REFRESH_TOKEN_KEY + username);

        // 3. 토큰 일치 여부 검증
        // 클라이언트가 보낸 토큰이 Redis에 저장된 토큰과 다르면, 토큰 탈취로 간주하고 모두 무효화
        if (!refreshToken.equals(storedRefreshToken)) {
            redisService.delete(REFRESH_TOKEN_KEY + username);
            throw new TeensException(GlobalErrorCode.INVALID_JWT);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));

        // 4. 토큰 발급
        JwtTokens Tokens = jwtTokenProvider.generateToken(user);

        // 5. 레디스에 저장
        String key = REFRESH_TOKEN_KEY + username;
        redisService.set(
                key,
                Tokens.refreshToken(),
                jwtProperties.getRefreshTokenExpiration()
        );

        return Tokens;
    }
}