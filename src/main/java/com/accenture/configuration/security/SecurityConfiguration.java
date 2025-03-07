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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                        .requestMatchers(HttpMethod.GET, "/clients/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/clients/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/clients/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/clients/**").permitAll()
                        .requestMatchers("/administrateurs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vehicules/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/vehicules/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/vehicules/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/vehicules/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/voitures/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/voitures/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/voitures/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/voitures/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/velos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/velos/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/velos/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/velos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/utilitaires/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/utilitaires/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/utilitaires/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/utilitaires/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/motos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/motos/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/motos/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/motos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/administrateurs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/administrateurs/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/administrateurs/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/administrateurs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/locations/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/locations/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/locations/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("Requête non autorisée - {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autorisé");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Accès refusé - {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Interdit");
                        })
                )
                .addFilterBefore(new CustomLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
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
