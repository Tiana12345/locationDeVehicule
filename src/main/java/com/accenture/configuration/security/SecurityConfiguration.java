package com.accenture.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**","/swagger-ui.html").permitAll()
                        .requestMatchers("/utilisateurs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/clients/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/clients/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/clients/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/clients/**").permitAll()
                        .requestMatchers("/administrateurs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/voitures/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/voitures/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/voitures/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/voitures/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/velos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/velos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/velos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/velos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/utilitaires/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/utilitaires/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/utilitaires/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/utilitaires/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/motos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/administrateurs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/administrateurs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/administrateurs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/administrateurs/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Unauthorized request - {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Access denied - {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        })
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select mail,password,1 from utilisateurs where mail = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select mail, role from utilisateurs where mail = ?");
        return jdbcUserDetailsManager;
    }
}