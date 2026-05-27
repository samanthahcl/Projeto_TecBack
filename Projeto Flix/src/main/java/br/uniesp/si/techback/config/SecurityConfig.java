package br.uniesp.si.techback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Usuário administrador para cumprir RF11
    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Permite abrir o H2 no navegador
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

                .authorizeHttpRequests(auth -> auth

                        // Swagger liberado
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // H2 liberado
                        .requestMatchers("/h2/**").permitAll()

                        // Login liberado
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()

                        // Cadastro de usuário liberado
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios").permitAll()

                        // RF11 - Apenas ADMIN pode cadastrar, editar ou apagar conteúdos
                        .requestMatchers(HttpMethod.POST, "/api/v1/conteudos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/conteudos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/conteudos/**").hasRole("ADMIN")

                        // Se ainda usar /filmes, protege também
                        .requestMatchers(HttpMethod.POST, "/filmes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/filmes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/filmes/**").hasRole("ADMIN")

                        // O restante fica liberado para não quebrar seu projeto
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}