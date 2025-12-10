package kusuri12.teens_be.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kusuri12.teens_be.global.auth.AuthDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    // JwtTokens 객체 생성 메서드, accessToken과 refreshToken 쌍 생성
    public JwtTokens generateToken(Authentication authentication) {
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        LocalDateTime now = LocalDateTime.now();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date accessTokenExpiresAt = Date.from(now.plusSeconds(accessTokenExpiration/1000)
                .atZone(ZoneId.systemDefault()).toInstant());

        Date refreshTokenExpiresAt = Date.from(now.plusSeconds(refreshTokenExpiration/1000)
                .atZone(ZoneId.systemDefault()).toInstant());

        String accessToken = Jwts.builder()
                .setSubject(authDetails.getUsername())
                .claim("userId", authDetails.getId())
                .claim("authorities", authorities)
                .claim("tokenType", "ACCESS")
                .issuedAt(new Date())
                .expiration(accessTokenExpiresAt)
                .signWith(key, Jwts.SIG.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authDetails.getUsername())
                .claim("userId", authDetails.getId())
                .claim("authorities", authorities)
                .claim("tokenType", "REFRESH")
                .issuedAt(new Date())
                .expiration(refreshTokenExpiresAt)
                .signWith(key, Jwts.SIG.HS512)
                .compact();

        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresAt(LocalDateTime.ofInstant(accessTokenExpiresAt.toInstant(), ZoneId.systemDefault()))
                .refreshTokenExpiresAt(LocalDateTime.ofInstant(refreshTokenExpiresAt.toInstant(), ZoneId.systemDefault()))
                .build();
    }

    // TODO: RefreshToken 객체만을 생성하는 메서드 만들기

    // Token Body를 얻는 메서드
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return parse(token).getSubject();
    }

    public Long getUserId(String token) {
        return parse(token).get("userId", Long.class);
    }

    public String getTokenType(String token) {
        return parse(token).get("tokenType", String.class);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 만료 검사
    public boolean isTokenExpired(String token) {
        return parse(token).getExpiration().before(new Date());
    }
}
