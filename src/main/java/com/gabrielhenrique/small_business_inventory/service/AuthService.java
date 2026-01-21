package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.UserConverter;
import com.gabrielhenrique.small_business_inventory.dto.login.LoginRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.login.TokenResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.TokenBlacklist;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.TokenBlacklistRepository;
import com.gabrielhenrique.small_business_inventory.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired private UserConverter userConverter;
    @Autowired private TokenBlacklistRepository tokenBlacklistRepository;

    public TokenResponseDTO login(LoginRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        User user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return userConverter.toTokenDTO(user, token);
    }

    public void logout(String token) {
        tokenBlacklistRepository.save(new TokenBlacklist(token));
    }
}
