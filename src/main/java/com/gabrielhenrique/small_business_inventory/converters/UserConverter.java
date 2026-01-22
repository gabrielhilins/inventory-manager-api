package com.gabrielhenrique.small_business_inventory.converters;

import com.gabrielhenrique.small_business_inventory.dto.login.RegisterRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.login.TokenResponseDTO;
import com.gabrielhenrique.small_business_inventory.dto.user.UserResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public User toEntity(RegisterRequestDTO dto, String encodedPassword) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(Role.valueOf(dto.getRole()));
        return user;
    }

    public TokenResponseDTO toTokenDTO(UserDetailsImpl userDetails, String token) {
        TokenResponseDTO dto = new TokenResponseDTO();
        dto.setToken(token);
        dto.setType("Bearer");
        dto.setEmail(userDetails.getEmail());
        dto.setRole(userDetails.getRole().name());
        return dto;
    }

    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserResponseDTO> toResponseDTO(List<User> users) {
        return users.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }
}
