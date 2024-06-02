package com.teste.vm_tecnologia.utils;

import com.teste.vm_tecnologia.model.enums.MessageEnum;
import com.teste.vm_tecnologia.model.exceptions.UnauthorizedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SecurityUtils {

    private static UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityUtils(UserDetailsService userDetailsService) {
        SecurityUtils.userDetailsService = userDetailsService;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    private String[] extractCredentialsFromHeader(String authHeader) throws UnauthorizedException {
        if(authHeader == null || !authHeader.startsWith("Basic ")) {
            throw new UnauthorizedException(MessageEnum.USUARIO_NAO_AUTORIZADO.getMessage());
        }

        String base64Credentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCreds = new String(decodedBytes, StandardCharsets.UTF_8);
        return decodedCreds.split(":", 2);
    }

    public void checkCreds(String authorizationHeader) throws UnauthorizedException {
        String[] credentials = extractCredentialsFromHeader(authorizationHeader);
        String email = credentials[0];
        String senha = credentials[1];
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if(!passwordEncoder.matches(senha, userDetails.getPassword())) {
            throw new UnauthorizedException("senha inválido.");
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
