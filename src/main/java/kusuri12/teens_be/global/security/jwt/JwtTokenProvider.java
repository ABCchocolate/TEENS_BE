package kusuri12.teens_be.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import kusuri12.teens_be.global.security.userdetails.AuthDetails;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_TOKEN_TYPE = "tokenType";

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtTokenProvider(
            JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    // 토큰 쌍 생성
    public JwtTokens generateToken(AuthDetails authDetails) {

        // 현재 시간
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);

        // exp 설정
        Date accessExp = Date.from(now.plusSeconds(jwtProperties.getAccessTokenExpiration()));
        Date refreshExp = Date.from(now.plusSeconds(jwtProperties.getRefreshTokenExpiration()));

        // 권한 설정
        String authorities = authDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 액세스 토큰
        String accessToken = Jwts.builder()
                .subject(authDetails.getUsername())
                .claim(CLAIM_USER_ID, authDetails.getId())
                .claim(CLAIM_AUTHORITIES, authorities)
                .claim(CLAIM_TOKEN_TYPE, "ACCESS")
                .issuedAt(issuedAt)
                .expiration(accessExp)
                .signWith(key)
                .compact();

        // 리프레시 토큰
        String refreshToken = Jwts.builder()
                .subject(authDetails.getUsername())
                .claim(CLAIM_TOKEN_TYPE, "REFRESH")
                .issuedAt(issuedAt)
                .expiration(refreshExp)
                .signWith(key)
                .compact();

        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(new RefreshToken(authDetails.getUsername(), refreshToken, jwtProperties.getRefreshTokenExpiration()))
                .build();
    }

    // Token Body를 얻는 메서드
    public Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (Exception e){
            throw new TeensException(GlobalErrorCode.INVALID_JWT);
        }
    }

    public String getUsernameFromExpiredToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new TeensException(GlobalErrorCode.INVALID_JWT);
        }
    }

    // 토큰의 남은 시간
    public long getRemainTime(String token) {
        Claims claims = parse(token);
        Date expiration = claims.getExpiration();
        long nowMillis = Instant.now().toEpochMilli();

        long remainTimeMillis = expiration.getTime() - nowMillis;

        if (remainTimeMillis > 0) {
            return remainTimeMillis / 1000;
        }
        return 0;
    }

    // Jwt 추출 메서드
    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return bearerToken.substring(7);
    }
}
