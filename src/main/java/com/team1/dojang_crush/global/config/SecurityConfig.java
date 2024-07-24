package com.team1.dojang_crush.global.config;

import com.team1.dojang_crush.domain.auth.jwt.JWTExceptionFilter;
import com.team1.dojang_crush.domain.auth.jwt.JWTFilter;
import com.team1.dojang_crush.domain.auth.jwt.JWTAuthenticationEntryPoint;
import com.team1.dojang_crush.domain.member.repository.MemberRepository;
import com.team1.dojang_crush.global.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtils jwtUtils;
    private final MemberRepository memberRepository;

    //swagger 필터 안타게
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> {
            web.ignoring()
                    .requestMatchers("/swagger-ui/**S",
                            //"/books/**", "/login", "/goods/**" , "/review/**","/records/**" , "/search**",
                            "/swagger-resources/**",
                            "/v3/api-docs/**"
                    );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                .csrf((csrf)->csrf
                        .disable())
                .cors((auth)->auth.configurationSource(corsConfiguration()))
                .httpBasic((basic)->basic.disable())
                .authorizeHttpRequests( //TODO 권한 허용해야하는 주소 지정
                        authorize -> authorize
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/oauth2/kakao").permitAll()
//                                .requestMatchers("/accounts/join").permitAll()
//                                .requestMatchers("/books/**").permitAll()
//                                .requestMatchers("/goods/**").permitAll()
//                                .requestMatchers("/records/**").permitAll()
//                                .requestMatchers("/review/**").permitAll()
//                                .requestMatchers("/search**").permitAll()
                                .anyRequest().authenticated()
                )
/*
                .authorizeHttpRequests((request)->request.requestMatchers("/accounts/join","/login","/oauth2/kakao","/test",
                                "/books/**","/goods/**" , "/records/**" , "/review/**","/records/**" , "/search**").permitAll()
                        .anyRequest().permitAll())
 */
                .exceptionHandling((ex)->ex
                        .authenticationEntryPoint(jwtAuthenticationEntry()))
                .addFilterBefore(new JWTFilter(jwtUtils, memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTExceptionFilter(), JWTFilter.class)
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration(){
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource=new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration=new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("https://localhost:3000");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        corsConfiguration.addAllowedMethod("PATCH");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("OPTIONS");
        corsConfiguration.addAllowedHeader("*");

        // 헤더에 authorization항목이 있으므로 credential을 true로 설정합니다.
        corsConfiguration.setAllowCredentials(true);

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntry(){
        return new JWTAuthenticationEntryPoint();
    }
}