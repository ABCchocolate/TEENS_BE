package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public static final String REFRESH_TOKEN_KEY = "RefreshToken:";

    // 로그아웃
    // 액세스 토큰 블랙리스트에 등록
    // 리프레시 토큰 레디스에서 삭제
    public void execute(String accessToken, String username) {
        redisService.delete(REFRESH_TOKEN_KEY + username);

        long expiration = jwtTokenProvider.getRemainTime(accessToken);

        if (expiration > 0) {
            redisService.addToBlackList(accessToken, username, expiration);
        }
    }
}
