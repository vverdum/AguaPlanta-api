package com.farm.AguaPlanta_api.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/bancadas/**").permitAll() // Permite acesso às rotas das bancadas sem autenticação
                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable) // Desativa HTTP Basic Authentication
                .formLogin(AbstractHttpConfigurer::disable); // Desativa formulário de login

        return http.build();
    }
}
