package kusuri12.teens_be.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import kusuri12.teens_be.domain.auth.repository.RefreshTokenRepository;
import kusuri12.teens_be.domain.user.domain.User;
import kusuri12.teens_be.domain.user.repository.UserRepository;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.jwt.exception.ExpiredTokenException;
import kusuri12.teens_be.global.jwt.exception.InvalidTokenException;
import kusuri12.teens_be.global.redis.RedisService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final RedisService redisService;
    private final UserRepository userRepository;

    public JwtTokenProvider(
            JwtProperties jwtProperties,
            RedisService redisService,
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.redisService = redisService;
        this.userRepository = userRepository;
    }

    // 토큰 쌍 생성
    public JwtTokens generateToken(AuthDetails authDetails) {
        Instant now = Instant.now();

        Date issuedAt = Date.from(now);
        Date accessExp = Date.from(now.plusSeconds(jwtProperties.getAccessTokenExpiration()));
        Date refreshExp = Date.from(now.plusSeconds(jwtProperties.getRefreshTokenExpiration()));

        String authorities = authDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .subject(authDetails.getUsername())
                .claim("authorities", authorities)
                .claim("tokenType", "ACCESS")
                .issuedAt(issuedAt)
                .expiration(accessExp)
                .signWith(key, Jwts.SIG.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .subject(authDetails.getUsername())
                .claim("tokenType", "REFRESH")
                .issuedAt(issuedAt)
                .expiration(refreshExp)
                .signWith(key, Jwts.SIG.HS512)
                .compact();

        String key = "RT:" + authDetails.getUsername();
        redisService.set(
                key,
                refreshToken,
                jwtProperties.getRefreshTokenExpiration()
        );

        return JwtTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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
        } catch (ExpiredTokenException e){
            throw ExpiredTokenException.EXCEPTION;
        } catch (JwtException | IllegalArgumentException e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // access 토큰이 만료되었을 때
    public JwtTokens reissueToken(String refreshToken) {
        String username = getUsername(refreshToken);

        // Redis에서 기존 토큰 확인
        Object storedToken = redisService.get("RT:" + username);

        // null 확인
        if (storedToken == null) {
            throw InvalidTokenException.EXCEPTION;
        }

        // 기존 Refresh Token 삭제
        redisService.delete("RT:" + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        AuthDetails authDetails = new AuthDetails(user);

        return generateToken(authDetails);
    }

    public String getUsername(String token) {
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
            throw InvalidTokenException.EXCEPTION;
        }
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

    // Jwt 추출 메서드
    public String getJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return null;
        }

        return bearerToken.substring(7);
    }

    // redis 관련

    public boolean isBlackList(String accessToken) {
        return redisService.get("BlackList:" + accessToken) != null;
    }

    public void addToBlackList(String accessToken, String username, long expiration) {
        String blackListKey = "BlackList:" + accessToken;
        redisService.set(blackListKey, username, expiration);
    }

    public void deleteRefreshToken(String username) {
        // 키 생성 규칙(RT:) 및 Redis 접근 로직을 Provider가 캡슐화
        redisService.delete("RT:" + username);
    }
}
