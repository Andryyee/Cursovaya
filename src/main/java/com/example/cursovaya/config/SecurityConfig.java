package com.example.cursovaya.config;

import com.example.cursovaya.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/login", "/register").permitAll()  // Разрешаем доступ к этим страницам для всех
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Только для админов
                .requestMatchers("/user/**").authenticated()  // Только для пользователей
                .anyRequest().permitAll()  // Все другие запросы требуют аутентификации
                .and()
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")  // Путь к странице входа
                                .permitAll()  // Доступ открыт для всех
                                .successHandler(authenticationSuccessHandler())  // Редирект после успешного входа
                                .failureUrl("/login?error=true")  // В случае ошибки входа
                )
                .logout(logout ->
                        logout.permitAll()  // Логин и выход доступны для всех
                )
                .userDetailsService(customUserDetailService);  // Используем собственный UserDetailsService

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Используем BCrypt для шифрования паролей
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect("/home");  // Редирект для админа
            } else if ("ROLE_USER".equals(role)) {
                response.sendRedirect("/home");  // Редирект для пользователя
            } else {
                response.sendRedirect("/");  // На случай неопределенной роли
            }
        };
    }
}
