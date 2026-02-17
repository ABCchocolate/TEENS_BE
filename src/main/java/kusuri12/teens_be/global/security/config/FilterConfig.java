package kusuri12.teens_be.global.security.config;

import kusuri12.teens_be.domain.auth.repository.BlackListRepository;
import kusuri12.teens_be.global.error.exception.ResponseWithErrorCode;
import kusuri12.teens_be.global.security.filter.GlobalExceptionFilter;
import kusuri12.teens_be.global.security.filter.JwtTokenFilter;
import kusuri12.teens_be.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlackListRepository blackListRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final ResponseWithErrorCode responseWithErrorCode;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, blackListRepository, handlerExceptionResolver);
    }

    @Bean
    public GlobalExceptionFilter globalExceptionFilter() {
        return new GlobalExceptionFilter(responseWithErrorCode);
    }

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterRegistration(JwtTokenFilter filter) {
        FilterRegistrationBean<JwtTokenFilter> registration = new FilterRegistrationBean<>(filter);
        // 서블릿 컨테이너의 자동 등록 제어
        registration.setEnabled(false);
        return registration;
    }
}