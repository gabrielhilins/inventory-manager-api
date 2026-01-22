package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.UserConverter;
import com.gabrielhenrique.small_business_inventory.dto.login.RegisterRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.user.UserResponseDTO;
import com.gabrielhenrique.small_business_inventory.exception.ResourceNotFoundException;
import com.gabrielhenrique.small_business_inventory.exception.UnauthorizedException;
import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userConverter = userConverter;
    }

    public List<UserResponseDTO> findAll() {
        return userConverter.toResponseDTO(userRepository.findAll());
    }

    public UserResponseDTO findById(Long id) {
        User user = findEntityById(id);
        return userConverter.toResponseDTO(user);
    }

    public UserResponseDTO findByEmail(String email) {
        User user = findEntityByEmail(email);
        return userConverter.toResponseDTO(user);
    }

    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public void create(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    
        Role role = Role.USER;
        if (dto.getRole() != null) {
            if (Role.valueOf(dto.getRole().toUpperCase()) == Role.ADMIN && !isAdmin) {
                throw new UnauthorizedException("Only admins can create other admins.");
            }
            if (isAdmin) {
                role = Role.valueOf(dto.getRole().toUpperCase());
            }
        }
    
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
    
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, RegisterRequestDTO dto) {
        User user = findEntityById(id);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.valueOf(dto.getRole()));

        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userConverter.toResponseDTO(updatedUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

