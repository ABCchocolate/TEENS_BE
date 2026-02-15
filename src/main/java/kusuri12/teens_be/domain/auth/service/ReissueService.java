package kusuri12.teens_be.domain.auth.service;

import kusuri12.teens_be.domain.auth.presentation.dto.request.ReissueRequest;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.jwt.JwtTokens;
import kusuri12.teens_be.global.jwt.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokens execute(ReissueRequest request) {

        String refreshToken = request.refreshToken();

        // 1. Refresh Token 유효성 검증 및 정보 추출
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw InvalidTokenException.EXCEPTION;
        }

        // 2. Refresh Token에서 사용자 ID (Username) 추출
        String username = jwtTokenProvider.getUsername(refreshToken);

        // 3. Redis에 저장된 Refresh Token 조회
        String storedRefreshToken = jwtTokenProvider.getRefreshToken(username);

        // 4. 토큰 일치 여부 검증
        // 클라이언트가 보낸 토큰이 Redis에 저장된 토큰과 다르면, 토큰 탈취로 간주하고 모두 무효화
        if (!refreshToken.equals(storedRefreshToken)) {
            jwtTokenProvider.deleteRefreshToken(username);
            throw InvalidTokenException.EXCEPTION;
        }

        return jwtTokenProvider.reissueToken(refreshToken);
    }
}