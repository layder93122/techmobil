package com.example.celulares.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 🔓 Permitir H2 Console
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // 🔧 Permitir iframe (H2)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )

                // 🔐 Permisos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/h2-console/**",
                                "/css/**",
                                "/js/**",
                                "/imagenes/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // 🔑 Login personalizado
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/admin/dashboard", true) // ✅ AQUÍ VA
                        .permitAll()
                )

                // 🚪 Logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}