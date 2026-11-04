package psiconecta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import psiconecta.security.JwtAuthenticationFilter;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        JwtAuthenticationFilter jwtAuthenticationFilter)
                        throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth

                                                // =========================
                                                // ROTAS PÚBLICAS
                                                // =========================

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/pacientes")
                                                .permitAll()

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/estudantes")
                                                .permitAll()

                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/auth/login")
                                                .permitAll()

                                                // =========================
                                                // ADMINISTRADOR
                                                // =========================

                                                .requestMatchers(
                                                                "/api/admin/**")
                                                .hasRole("ADMIN")

                                                // =========================
                                                // DEMAIS ROTAS
                                                // =========================

                                                .requestMatchers(
                                                                "/api/estudante/**")
                                                .hasRole("ESTUDANTE")

                                                .requestMatchers(
                                                                "/api/paciente/**")
                                                .hasRole("PACIENTE")

                                                .anyRequest().authenticated())

                                .exceptionHandling(exception -> exception

                                                .authenticationEntryPoint(
                                                                new HttpStatusEntryPoint(
                                                                                HttpStatus.UNAUTHORIZED))

                                                .accessDeniedHandler(
                                                                (request, response, accessDeniedException) -> response
                                                                                .setStatus(
                                                                                                HttpStatus.FORBIDDEN
                                                                                                                .value())))

                                .formLogin(form -> form.disable())

                                .httpBasic(httpBasic -> httpBasic.disable())

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                                List.of(
                                                "http://localhost:5173"));

                configuration.setAllowedMethods(
                                List.of(
                                                "GET",
                                                "POST",
                                                "PUT",
                                                "DELETE",
                                                "OPTIONS"));

                configuration.setAllowedHeaders(
                                List.of(
                                                "Authorization",
                                                "Content-Type"));

                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration(
                                "/**",
                                configuration);

                return source;
        }

}