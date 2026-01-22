package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.UserConverter;
import com.gabrielhenrique.small_business_inventory.dto.login.LoginRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.login.TokenResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.TokenBlacklist;
import com.gabrielhenrique.small_business_inventory.repository.TokenBlacklistRepository;
import com.gabrielhenrique.small_business_inventory.security.TokenService;
import com.gabrielhenrique.small_business_inventory.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserConverter userConverter;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, UserConverter userConverter, TokenBlacklistRepository tokenBlacklistRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userConverter = userConverter;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = tokenService.generateToken(userDetails);

        return userConverter.toTokenDTO(userDetails, token);
    }

    public void logout(String token) {
        tokenBlacklistRepository.save(new TokenBlacklist(token));
    }
}
