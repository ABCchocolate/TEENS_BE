package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final JwtTokenProvider jwtTokenProvider;

    // 로그아웃
    // 액세스 토큰 블랙리스트에 등록
    // 리프레시 토큰 레디스에서 삭제
    public void execute(String accessToken, String username) {
        jwtTokenProvider.deleteRefreshToken(username);

        long expiration = jwtTokenProvider.getExpiration(accessToken);

        if (expiration > 0) {
            jwtTokenProvider.addToBlackList(accessToken, username, expiration);
        }
    }
}
