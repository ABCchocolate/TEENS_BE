package kusuri12.teens_be.global.error.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        // 💡 1. 403 에러 발생 시 로그 출력
        log.error("Access Denied: {}", accessDeniedException.getMessage(), accessDeniedException);

        // 💡 2. 클라이언트에게 403 응답 전달 (Spring Security가 기본적으로 하지만 명확하게)
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

        // JSON 응답 본문을 보내고 싶다면 여기서 처리 (선택 사항)
        // response.setContentType("application/json;charset=UTF-8");
        // response.getWriter().write("{ \"error\": \"Access Denied\", \"message\": \"" + accessDeniedException.getMessage() + "\" }");
    }
}