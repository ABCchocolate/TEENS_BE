package kusuri12.teens_be.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import kusuri12.teens_be.domain.auth.domain.RefreshToken;
import kusuri12.teens_be.domain.auth.domain.repository.RefreshTokenRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.domain.repository.UserRepository;
import kusuri12.teens_be.domain.user.exception.UserNotFoundException;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            RedisService redisService,
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.redisService = redisService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
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
            throw InvalidJwtException.EXCEPTION;
        }
    }

    public String reissueAccessToken(String refreshToken) {
        String username = getUsername(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken);

        if (!storedToken.getUsername().equals(username)) {
            throw InvalidJwtException.EXCEPTION;
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        AuthDetails authDetails = new AuthDetails(user);

        return generateAccessToken(authDetails);
    }

    public String reissueRefreshToken(String oldRefreshToken) {
        String username = getUsername(oldRefreshToken);

        // Redis에서 기존 토큰 확인
        RefreshToken storedToken = refreshTokenRepository.findByToken(oldRefreshToken);

        // username 일치 확인
        if (!storedToken.getUsername().equals(username)) {
            throw InvalidJwtException.EXCEPTION;
        }

        // 기존 Refresh Token 삭제
        refreshTokenRepository.delete(storedToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        AuthDetails authDetails = new AuthDetails(user);

        // 새로운 Refresh Token 발급
        return generateRefreshToken(authDetails);
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
