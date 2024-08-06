package com.team1.dojang_crush.global.config;

import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.global.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import com.team1.dojang_crush.global.oauth.CustomOAuth2UserService;
import com.team1.dojang_crush.global.oauth.OAuth2SuccessHandler;
import com.team1.dojang_crush.global.oauth.jwt.JWTAuthenticationEntryPoint;
import com.team1.dojang_crush.global.oauth.jwt.JWTExceptionFilter;
import com.team1.dojang_crush.global.oauth.jwt.JWTFilter;
import com.team1.dojang_crush.global.utils.JWTUtils;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JWTUtils jwtUtils;
    private final MemberRepository memberRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:5174");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedHeader("https://api.dojang-crush.p-e.kr");
        configuration.addAllowedOrigin("https://api.dojang-crush.p-e.kr");
        configuration.addAllowedHeader("https://dojang-crush-client.vercel.app");
        configuration.addAllowedOrigin("https://dojang-crush-client.vercel.app");

        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedHeader("*");
        // 헤더에 authorization항목이 있으므로 credential을 true로 설정합니다.
        configuration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login/oauth2/**","/oauth2/**","/comments/test").permitAll() // 모든 요청에 대해 인증 없이 허용
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .anyRequest().permitAll() // 모든 요청에 대해 인증 필요 없음
                )
                .oauth2Login(oauth2->
                        oauth2.userInfoEndpoint(auth-> auth.userService(customOAuth2UserService))
                                .successHandler(oAuth2SuccessHandler)
                )
                .exceptionHandling((ex)->ex
                        .authenticationEntryPoint(jwtAuthenticationEntry()))
                .addFilterBefore(new JWTFilter(jwtUtils,memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTExceptionFilter(), JWTFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntry(){
        return new JWTAuthenticationEntryPoint();
    }

}

