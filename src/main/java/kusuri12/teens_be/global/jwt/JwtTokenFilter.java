package kusuri12.teens_be.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusuri12.teens_be.global.auth.AuthDetailService;
import kusuri12.teens_be.global.jwt.exception.InvalidJwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter: 상속받은 클래스가 해당 필터를 한 번 실행할 수 있도록 함
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthDetailService authDetailService;
    private final AntPathMatcher matcher = new AntPathMatcher(); // url, 파일 경로가 일치하는 지 확인하는 Matcher

    // TODO: 안에 들어갈 end point 명시하기, 귀찮아서 미룸
    private static final String[] PERMITTED_AUTH = {
            "/auth/**"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // OPTIONS 요청은 CORS preflight 이므로 필터링 제외, 라는데 공부가 더 필요할 거 같다.
        if ("OPTIONS".equals(method)) {
            return true;
        }

        return Arrays.stream(PERMITTED_AUTH)
                .anyMatch(permit -> matcher.match(permit, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, // HTTP request를 담고 있는 클래스
                                    @NonNull HttpServletResponse response, // HTTP response를 담는 클래스
                                    @NonNull FilterChain chain // Spring의 Filter들을 체인처럼 연결해 놓은 클래스
                                    ) throws ServletException, IOException {
        String jwt = getJwt(request);

        if (!jwtTokenProvider.validateToken(jwt)) throw InvalidJwtException.EXCEPTION;

        String username = jwtTokenProvider.getUsername(jwt);
        String tokenType = jwtTokenProvider.getTokenType(jwt);

        if ("ACCESS".equals(tokenType)) {
            UserDetails userDetails = authDetailService.loadUserByUsername(username);
            if (userDetails == null) {
                throw InvalidJwtException.EXCEPTION;
            }
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }  // 진짜 너무 어렵다. 나중에 제대로 공부해야겠다.

    // Jwt 추출 메서드
    private String getJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw InvalidJwtException.EXCEPTION;
        }

        return bearerToken.substring(7);
    }
}
