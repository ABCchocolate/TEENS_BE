package kusuri12.teens_be.global.config;

import kusuri12.teens_be.domain.auth.repository.BlackListRepository;
import kusuri12.teens_be.global.error.filter.JwtTokenFilter;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final BlackListRepository blackListRepository;

    public FilterConfig(JwtTokenProvider jwtTokenProvider, BlackListRepository blackListRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.blackListRepository = blackListRepository;
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider, blackListRepository);
    }

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterRegistration(JwtTokenFilter filter) {
        FilterRegistrationBean<JwtTokenFilter> registration = new FilterRegistrationBean<>(filter);
        // 서블릿 컨테이너의 자동 등록 제어
        registration.setEnabled(false);
        return registration;
    }
}