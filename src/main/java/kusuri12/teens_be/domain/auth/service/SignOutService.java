package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.domain.BlackList;
import kusuri12.teens_be.domain.auth.repository.BlackListRepository;
import kusuri12.teens_be.domain.auth.repository.RefreshTokenRepository;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignOutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;

    // 로그아웃
    // 액세스 토큰 블랙리스트에 등록
    // 리프레시 토큰 레디스에서 삭제
    // username 반환
    public String execute(String accessToken) {
        String username = jwtTokenProvider.parse(accessToken).getSubject();

        refreshTokenRepository.deleteById(username);

        long expiration = jwtTokenProvider.getRemainTime(accessToken);

        if (expiration > 0) {
            blackListRepository.save(new BlackList(accessToken, username, expiration));
        }

        return username;
    }
}