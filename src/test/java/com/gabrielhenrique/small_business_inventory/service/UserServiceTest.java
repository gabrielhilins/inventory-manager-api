package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.UserConverter;
import com.gabrielhenrique.small_business_inventory.dto.login.RegisterRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.user.UserResponseDTO;
import com.gabrielhenrique.small_business_inventory.exception.ResourceNotFoundException;
import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private UserService userService;

    private User user;
    private RegisterRequestDTO registerRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);

        registerRequestDTO = new RegisterRequestDTO("Test User", "test@example.com", "password", "USER");
        userResponseDTO = new UserResponseDTO(1L, "Test User", "test@example.com", Role.USER);
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userConverter.toResponseDTO(anyList())).thenReturn(Collections.singletonList(userResponseDTO));

        List<UserResponseDTO> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userConverter.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.create(registerRequestDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreate_EmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(registerRequestDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userConverter.toResponseDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.update(1L, registerRequestDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDelete_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L));
        verify(userRepository, never()).deleteById(1L);
    }
}
