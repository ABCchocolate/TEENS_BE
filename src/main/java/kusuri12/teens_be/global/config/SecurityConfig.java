package kusuri12.teens_be.global.config;

import kusuri12.teens_be.global.error.filter.GlobalExceptionFilter;
import kusuri12.teens_be.global.error.exception.ResponseWithErrorCode;
import kusuri12.teens_be.global.error.handler.CustomAccessDeniedHandler;
import kusuri12.teens_be.global.error.handler.CustomAuthenticationEntryPoint;
import kusuri12.teens_be.global.error.filter.JwtTokenFilter;
import kusuri12.teens_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseWithErrorCode responseWithErrorCode;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public static final String[] PERMITTED_AUTH = {
            "/auth/sign-up/**",
            "/auth/sign-in/**",
            "/auth/check-id/**",
            "/auth/reissue",

            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PERMITTED_AUTH).permitAll()

                        .requestMatchers(
                                "/auth/sign-out",
                                "/auth/quit"
                                ).authenticated()

                        .requestMatchers(HttpMethod.GET, "/forum/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/information/**").permitAll()
                        .requestMatchers("/forum/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/information/**").hasRole("ADMIN")
                        .anyRequest().hasRole("USER"))

                .addFilterBefore(globalExceptionFilter(), CorsFilter.class)
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // 인증 처리를 위한 AuthenticationManager를 Bean 등록
        // 주로 로그인 시도 시 사용자 인증 로직에 사용됨
        return config.getAuthenticationManager();
    }

    @Bean
    public GlobalExceptionFilter globalExceptionFilter() {
        return new GlobalExceptionFilter(responseWithErrorCode);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtTokenProvider);
    }
}
