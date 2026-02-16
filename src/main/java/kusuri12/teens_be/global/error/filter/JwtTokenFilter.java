package kusuri12.teens_be.global.error.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusuri12.teens_be.global.auth.AuthDetails;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import kusuri12.teens_be.global.redis.RedisService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kusuri12.teens_be.global.config.SecurityConfig.PERMITTED_AUTH;

@RequiredArgsConstructor
// OncePerRequestFilter: 상속받은 클래스가 해당 필터를 한 번 실행할 수 있도록 함
public class JwtTokenFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher matcher = new AntPathMatcher(); // url, 파일 경로가 일치하는 지 확인하는 Matcher

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        if ("OPTIONS".equals(method)) {
            return true;
        }

        return Arrays.stream(PERMITTED_AUTH)
                .anyMatch(permit -> matcher.match(permit, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String jwt = jwtTokenProvider.getJwt(request);

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtTokenProvider.parse(jwt);
            String tokenType = claims.get("tokenType", String.class);
            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            String authoritiesStr = claims.get("authorities", String.class);

            if ("ACCESS".equals(tokenType) && username != null && userId != null) {
                if (jwtTokenProvider.isBlackList(jwt)) {
                    throw InvalidTokenException.EXCEPTION;
                }

                List<GrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UserDetails userDetails = new AuthDetails(userId, username, authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (InvalidTokenException e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }
}