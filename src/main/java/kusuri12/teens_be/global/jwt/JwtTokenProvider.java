package kusuri12.teens_be.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.jwt.exception.ExpiredJwtException;
import kusuri12.teens_be.global.jwt.exception.InvalidJwtException;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final RedisService redisService;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RedisService redisService) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisService = redisService;
    }

    public String generateToken(AuthDetails authDetails, String type, Long ext) {
        LocalDateTime now = LocalDateTime.now();

        Date expiresAt = Date.from(now.plusSeconds(ext)
                .atZone(ZoneId.systemDefault()).toInstant());

        String authorities = authDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authDetails.getUsername())
                .claim("userId", authDetails.getId())
                .claim("authorities", authorities)
                .claim("tokenType", type)
                .issuedAt(new Date())
                .expiration(expiresAt)
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String generateAccessToken(AuthDetails authDetails) {
        return generateToken(authDetails, "ACCESS", accessTokenExpiration);
    }

    public String generateRefreshToken(AuthDetails authDetails) {
        String refreshToken = generateToken(authDetails, "REFRESH", refreshTokenExpiration);

        String key = "RT:" + authDetails.getUsername();
        redisService.set(
                key,
                refreshToken,
                refreshTokenExpiration
        );

        return refreshToken;
    }

    // Token Body를 얻는 메서드
    public Claims parse(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e){
            throw ExpiredJwtException.EXCEPTION;
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            // 2. 그 외 모든 유효성 예외(서명, 형식 오류, null 등)는 Invalid로 처리합니다.
            throw InvalidJwtException.EXCEPTION;
        }
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

    public String getRefreshToken(String username) {
        Object token = redisService.get("RT:" + username);
        return (token != null) ? token.toString() : null;
    }

    public long getExpiration(String token) {
        Claims claims = parse(token);
        Date expiration = claims.getExpiration();
        long nowMillis = Instant.now().toEpochMilli();

        long remainTimeMillis = expiration.getTime() - nowMillis;

        if (remainTimeMillis > 0) {
            return remainTimeMillis / 1000;
        }
        return 0;
    }
}
