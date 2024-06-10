package com.woka.mysqlproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.repository.UserEntityRepository;
import com.woka.mysqlproject.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Optional;

public class AuthServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signupNewUserReturnsSavedUser() {
        // Arrange
        UserEntity inputUser = new UserEntity();
        inputUser.setName("John Doe");
        inputUser.setEmail("john@example.com");
        inputUser.setPassword("password");

        UserEntity savedUser = new UserEntity();
        savedUser.setName("John Doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode(inputUser.getPassword())).thenReturn("encodedPassword");
        when(userEntityRepository.findByEmail(inputUser.getEmail())).thenReturn(null);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        // Act
        UserEntity result = authService.signup(inputUser);

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(userEntityRepository).save(any(UserEntity.class));
    }

    @Test
    public void signupExistingUserThrowsKeyAlreadyExistsException() {
        // Arrange
        UserEntity inputUser = new UserEntity();
        inputUser.setName("John Doe");
        inputUser.setEmail("john@example.com");
        inputUser.setPassword("password");

        when(userEntityRepository.findByEmail(inputUser.getEmail())).thenReturn(new UserEntity());

        // Act & Assert
        assertThrows(KeyAlreadyExistsException.class, () -> authService.signup(inputUser));
    }

    @Test
    public void authenticateValidCredentialsReturnsUser() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john@example.com");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("john@example.com");

        when(userEntityRepository.findByEmail(loginRequest.getUsername())).thenReturn(userEntity);

        // Act
        UserEntity result = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void authenticateInvalidCredentialsThrowsException() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john@example.com");
        loginRequest.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
    }
}

