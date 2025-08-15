package com.suke.czx.authentication;

import com.suke.czx.authentication.handler.*;
import com.suke.czx.authentication.provider.CustomAuthorizationManager;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.config.AuthIgnoreConfig;
import com.suke.czx.interceptor.AuthenticationTokenFilter;
import com.suke.czx.interceptor.ValidateCodeFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @Description 安全配置
 * @Date 20:53
 * @Author yzcheng90@qq.com
 **/
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private AuthIgnoreConfig authIgnoreConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        List<String> permitAll = authIgnoreConfig.getIgnoreUrls();
        permitAll.add("/error");
        permitAll.add("/v3/**");
        permitAll.add("/swagger-ui/**");
        permitAll.add("/swagger-resources/**");
        permitAll.add(Constant.TOKEN_ENTRY_POINT_URL);
        permitAll.add(Constant.TOKEN_LOGOUT_URL);
        String[] urls = permitAll.stream().distinct().toArray(String[]::new);

        // 基于 token，不需要 csrf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // 跨域配置
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // 基于 token，不需要 session
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 权限
        httpSecurity.authorizeHttpRequests(it ->
                it.requestMatchers(urls).permitAll()  //设置登录路径所有人都可以访问
                        .anyRequest().access(authorizationManager())  //其他路径都要进行拦截
        );

        // 设置登录URL
        httpSecurity.formLogin(formLogin -> formLogin
                .loginProcessingUrl(Constant.TOKEN_ENTRY_POINT_URL)
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler()));

        // 设置退出URL
        httpSecurity.logout(logout -> logout
                .logoutUrl(Constant.TOKEN_LOGOUT_URL).logoutUrl(Constant.TOKEN_LOGOUT_URL)
                .logoutSuccessHandler(logoutSuccessHandler()).clearAuthentication(true));
        // 如果不用验证码，注释这个过滤器即可
        httpSecurity.addFilterBefore(new ValidateCodeFilter(redisTemplate, authenticationFailureHandler()), UsernamePasswordAuthenticationFilter.class);
        // token 验证过滤器
        httpSecurity.addFilterBefore(new AuthenticationTokenFilter(authIgnoreConfig, redisTemplate), UsernamePasswordAuthenticationFilter.class);
        // 认证异常处理
        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler());
            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new TokenAuthenticationFailHandler());
        });
        return httpSecurity.build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
        return new CustomAuthorizationManager();
    }

    // 解决跨域
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 开放静态资源权限
        return web -> web.ignoring().requestMatchers("/actuator/**", "/css/**", "/error");
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}