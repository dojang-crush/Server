package com.team1.dojang_crush.global.config;

import com.team1.dojang_crush.domain.auth.oauth2.MyAuthenticationFailureHandler;
import com.team1.dojang_crush.domain.auth.oauth2.MyAuthenticationSuccessHandler;
import com.team1.dojang_crush.domain.auth.service.CustomOAuth2UserService;
import com.team1.dojang_crush.domain.jwt.JwtAuthFilter;
import com.team1.dojang_crush.domain.jwt.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // HTTP 기본 인증을 비활성화
                .cors().and() // CORS 활성화
                .csrf().disable() // CSRF 보호 기능 비활성화
                .formLogin().disable() // form 로그인 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션관리 정책을 STATELESS(세션이 있으면 쓰지도 않고, 없으면 만들지도 않는다)
                .and()
                .authorizeRequests() // 요청에 대한 인증 설정
                .antMatchers("/token/**").permitAll() // 토큰 발급을 위한 경로는 모두 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증이 필요하다.
                .and()
                .oauth2Login() // OAuth2 로그인 설정시작
                .userInfoEndpoint().userService(customOAuth2UserService) // OAuth2 로그인시 사용자 정보를 가져오는 엔드포인트와 사용자 서비스를 설정
                .and()
                .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                .successHandler(oAuth2LoginSuccessHandler); // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.


        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가한다.
        return http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

    //    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.addAllowedOrigin("http://localhost:3000");
//        configuration.addAllowedOrigin("http://localhost:5173");
//        configuration.addAllowedOrigin("http://localhost:5174");
//        configuration.addAllowedOrigin("http://localhost:8080");
//
//        configuration.addAllowedMethod("*"); //모든 Method 허용(POST, GET, ...)
//        configuration.addAllowedHeader("*"); //모든 Header 허용
//        configuration.setAllowCredentials(true); //CORS 요청에서 자격 증명(쿠키, HTTP 헤더) 허용
//
//        configuration.addExposedHeader("Authorization"); // 클라이언트가 특정 헤더값에 접근 가능하도록 하기
//        configuration.addExposedHeader("Authorization-Refresh");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", configuration); //위에서 설정한 Configuration 적용
//        return source;
//    }
}