package com.teste.vm_tecnologia.config.security;

import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UsuarioNaoExisteException;
import com.teste.vm_tecnologia.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/swagger-resources/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuario/salvar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuario/buscar", "/api/usuario/buscar/{id}").authenticated()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        try {
            var usuario = usuarioRepository.findByEmail(email);
            if(usuario.isPresent() && usuario.get().getEmail() != null) {
                return User.builder()
                    .username(usuario.get().getEmail())
                    .password(usuario.get().getSenha())
                    .roles("USER")
                    .build();
            } else {
                throw new UsuarioNaoExisteException(MessageEnum.USUARIO_NAO_ENCONTRADO.getMessage());
            }
        } catch (UsuarioNaoExisteException e) {
            throw new RuntimeException(e);
        }
    }
}