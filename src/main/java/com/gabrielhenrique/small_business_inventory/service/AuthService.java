package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.UserConverter;
import com.gabrielhenrique.small_business_inventory.dto.login.LoginRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.login.TokenResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService; 
    @Autowired private UserConverter userConverter;

    public TokenResponseDTO login(LoginRequestDTO dto) {
        // O Spring Security valida as credenciais aqui
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        // Se passar, gera o token para o usuário
        User user = (User) auth.getPrincipal();
        String token = String.valueOf(tokenService.allocateToken(String.valueOf(user)));

        return userConverter.toTokenDTO(user, token);
    }
}
