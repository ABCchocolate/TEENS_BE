package kusuri12.teens_be.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusuri12.teens_be.global.error.exception.GlobalErrorCode;
import kusuri12.teens_be.global.error.exception.ResponseWithErrorCode;
import kusuri12.teens_be.global.error.exception.TeensException;
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
        } catch (TeensException e) {
            log.error("Handled TeensException : ", e);
            responseWithErrorCode.response(response, e.getErrorCode());
        } catch (Exception e) {
            log.error("Unhandled Exception : ", e);
            responseWithErrorCode.response(response, GlobalErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}