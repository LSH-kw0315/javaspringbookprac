package org.zerock.jwtprac.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.jwtprac.security.APIUserDetailsService;
import org.zerock.jwtprac.security.filter.APILoginFilter;
import org.zerock.jwtprac.security.filter.RefreshTokenFilter;
import org.zerock.jwtprac.security.filter.TokenCheckFilter;
import org.zerock.jwtprac.security.handler.APILoginSucessHandler;
import org.zerock.jwtprac.util.JWTUtil;

import java.util.Arrays;


@Configuration
@Log4j2
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final APIUserDetailsService apiUserDetailsService;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("----web configure----");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
        //정적 자원에 대한 필터를 무시
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception{
        log.info("----configure----");

        AuthenticationManagerBuilder authenticationManagerBuilder=
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(apiUserDetailsService)
                        .passwordEncoder(passwordEncoder());
        //인증 매니저 빌더
        AuthenticationManager authenticationManager=authenticationManagerBuilder.build();
        //인증 매니저 생성(인증 매니저 빌더로 생성)
        http.authenticationManager(authenticationManager);
        //http에 인증매니저 설정
        APILoginFilter apiLoginFilter=new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        APILoginSucessHandler apiLoginSucessHandler=new APILoginSucessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(apiLoginSucessHandler);

        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
        //apiLoginFilter(jwt필터) 위치 조정. 두번째 인자에 해당하는 필터의 before에 필터를 설치.

        http.addFilterBefore(tokenCheckFilter(jwtUtil,apiUserDetailsService),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(refresTokenFilter(jwtUtil,"/refreshToken"),
                TokenCheckFilter.class);

        http.csrf(config->config.disable());
        http.sessionManagement(cofig->cofig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(config->config.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil,APIUserDetailsService apiUserDetailsService){
        return new TokenCheckFilter(jwtUtil,apiUserDetailsService);
    }

    private RefreshTokenFilter refresTokenFilter(JWTUtil jwtUtil, String path){
        return new RefreshTokenFilter(jwtUtil,path);
    }

    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("localhost","http://localhost","localhost:80","http://localhost/*","localhost:80/*","localhost/*"));//보안을 위해서는 와일드카드를 쓰면 안됨.
        corsConfiguration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",corsConfiguration);

        return source;
    }
}
