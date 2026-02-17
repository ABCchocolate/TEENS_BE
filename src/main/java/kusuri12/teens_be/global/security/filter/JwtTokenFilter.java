package kusuri12.teens_be.global.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusuri12.teens_be.domain.auth.exception.AuthErrorCode;
import kusuri12.teens_be.domain.auth.repository.BlackListRepository;
import kusuri12.teens_be.global.security.userdetails.AuthDetails;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.security.jwt.JwtTokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kusuri12.teens_be.global.security.config.SecurityConfig.PERMITTED_AUTH;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 상속받은 클래스가 해당 필터를 한 번 실행할 수 있도록 함
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlackListRepository blackListRepository;
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

        String jwt = jwtTokenProvider.getToken(request);
        String path = request.getServletPath();

        if (jwt == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (blackListRepository.existsById(jwt)) {
                if (matcher.match("/auth/sign-out", path)) {
                    // 이미 로그아웃된 경우 필터에서 200 OK로 조기 종료
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"message\": \"Already logged out\"}");
                    return;
                }
                throw new TeensException(AuthErrorCode.TOKEN_THEFT_DETECTED);
            }

            Claims claims = jwtTokenProvider.parse(jwt);
            String username = claims.getSubject();
            Long userId = claims.get(JwtTokenProvider.CLAIM_USER_ID, Long.class);
            String authoritiesStr = claims.get(JwtTokenProvider.CLAIM_AUTHORITIES, String.class);
            String tokenType = claims.get(JwtTokenProvider.CLAIM_TOKEN_TYPE, String.class);

            if ("ACCESS".equals(tokenType) && username != null && userId != null && authoritiesStr != null) {

                // 권한 파싱
                List<GrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // 인증 객체 생성
                UserDetails userDetails = new AuthDetails(userId, username, authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 인증 객체 등록
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            if (matcher.match("/auth/reissue", path)) {
                chain.doFilter(request, response);
                return;
            }
            throw new TeensException(GlobalErrorCode.EXPIRED_JWT);
        } catch (TeensException e) {
            throw e;
        } catch (Exception e) {
            throw new TeensException(GlobalErrorCode.INVALID_JWT);
        }
    }
}