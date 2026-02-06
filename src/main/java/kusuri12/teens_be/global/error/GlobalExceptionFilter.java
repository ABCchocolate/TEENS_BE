package kusuri12.teens_be.global.error;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusuri12.teens_be.global.error.exception.ErrorCode;
import kusuri12.teens_be.global.error.exception.ResponseWithErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
import kusuri12.teens_be.global.jwt.exception.ExpiredTokenException;
import kusuri12.teens_be.global.jwt.exception.InvalidTokenException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionFilter extends OncePerRequestFilter {

    private final ResponseWithErrorCode responseWithErrorCode;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (ExpiredTokenException e) {
            log.error("ExpiredJwtException catch : {}", e.getMessage());
            responseWithErrorCode.response(response, ErrorCode.EXPIRED_JWT);
        } catch (InvalidTokenException e) {
            log.error("InvalidJwtException catch : {}", e.getMessage());
            responseWithErrorCode.response(response, ErrorCode.INVALID_JWT);
        } catch (TeensException e) {
            log.error("Handled TeensException : ", e);
            responseWithErrorCode.response(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("Unhandled Exception : ", e);
            responseWithErrorCode.response(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}