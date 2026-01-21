package com.gabrielhenrique.small_business_inventory.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gabrielhenrique.small_business_inventory.exception.InvalidTokenException;
import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.TokenBlacklistRepository;
import com.gabrielhenrique.small_business_inventory.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock(lenient = true)
    private TokenBlacklistRepository tokenBlacklistRepository;

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private final String secret = "test_secret";
    private final String issuer = "test-issuer";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secret", secret);
        ReflectionTestUtils.setField(tokenService, "issuer", issuer);
        ReflectionTestUtils.setField(tokenService, "expirationHours", 2L);

        user = new User();
        user.setEmail("test@example.com");
        user.setRole(Role.USER);

        // Mock the findByToken method to always return empty, meaning no token is blacklisted
        when(tokenBlacklistRepository.findByToken(anyString())).thenReturn(Optional.empty());
    }

    @Test
    void testGenerateToken() {
        String token = tokenService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testValidateToken_Success() {
        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);
        assertEquals(user.getEmail(), subject);
    }

    @Test
    void testValidateToken_InvalidToken() {
        String token = "invalid.token.string";
        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_WrongSecret() {
        String token = tokenService.generateToken(user);
        ReflectionTestUtils.setField(tokenService, "secret", "wrong_secret"); // Temporarily change for this test
        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(token));
        ReflectionTestUtils.setField(tokenService, "secret", secret); // Restore original secret
    }

    @Test
    void testValidateToken_Expired() {
        ReflectionTestUtils.setField(tokenService, "expirationHours", -1L); // Make token expire immediately
        String token = tokenService.generateToken(user);
        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(token));
        ReflectionTestUtils.setField(tokenService, "expirationHours", 2L); // Restore original expiration
    }

    @Test
    void testValidateToken_WrongIssuer() {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer("wrong-issuer")
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign(algorithm);

        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_BlacklistedToken() {
        String blacklistedToken = tokenService.generateToken(user);
        when(tokenBlacklistRepository.findByToken(blacklistedToken)).thenReturn(Optional.of(new com.gabrielhenrique.small_business_inventory.model.TokenBlacklist(blacklistedToken)));

        assertThrows(InvalidTokenException.class, () -> tokenService.validateToken(blacklistedToken));
    }
}