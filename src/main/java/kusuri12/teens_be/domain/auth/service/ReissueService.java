package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.presentation.dto.request.ReissueRequest;
import kusuri12.teens_be.domain.auth.repository.RefreshTokenRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.exception.UserErrorCode;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.jwt.JwtProperties;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.jwt.JwtTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokens execute(String expiredAccessToken, ReissueRequest request) {

        String requestRefreshToken = request.refreshToken();

        // 1. Refresh Token 유효성 검증 및 정보 추출
        String username = jwtTokenProvider.parse(requestRefreshToken).getSubject();
        validateTokenPair(expiredAccessToken, username);

        // 2. Redis에 저장된 Refresh Token 조회
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new TeensException(AuthErrorCode.TOKEN_NOT_FOUND));

        // 3. 토큰 일치 여부 검증
        // 클라이언트가 보낸 토큰이 Redis에 저장된 토큰과 다르면, 토큰 탈취로 간주하고 모두 무효화
        validateTokenTheft(storedRefreshToken, requestRefreshToken);

        // 4. 토큰 발급
        return rotateTokens(username);
    }

    private void validateTokenPair(String expiredAccessToken, String usernameFromRt) {
        String usernameFromAt = jwtTokenProvider.getUsernameFromExpiredToken(expiredAccessToken);
        if (!usernameFromAt.equals(usernameFromRt)) {
            throw new TeensException(AuthErrorCode.INVALID_TOKEN_PAIR);
        }
    }

    private void validateTokenTheft(RefreshToken storedRt, String requestRt) {
        if (!Objects.equals(storedRt.getToken(), requestRt)) {
            throw new TeensException(AuthErrorCode.TOKEN_THEFT_DETECTED);
        }
    }

    private JwtTokens rotateTokens(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TeensException(UserErrorCode.USER_NOT_FOUND));
        AuthDetails authDetails = new AuthDetails(user);

        JwtTokens tokens = jwtTokenProvider.generateToken(authDetails);

        refreshTokenRepository.save(new RefreshToken(
                username,
                tokens.refreshToken(),
                jwtProperties.getRefreshTokenExpiration()));

        return tokens;
    }
}