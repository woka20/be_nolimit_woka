package com.woka.mysqlproject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.request.LoginRequest;
import com.woka.mysqlproject.response.LoginResponse;
import com.woka.mysqlproject.service.AuthService;
import com.woka.mysqlproject.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class LoginControllerTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final AuthService authenticationService = mock(AuthService.class);
    private final LoginController loginController =
            new LoginController(jwtService, authenticationService);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void registerValidUserReturnsRegisteredUser() throws Exception {
        // Arrange
        UserEntity registerUserDto = new UserEntity();
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setPassword("password");
        when(authenticationService.signup(any(UserEntity.class))).thenReturn(registerUserDto);

        // Act
        ResponseEntity<?> response =
                loginController.register(registerUserDto);

        // Assert
        verify(authenticationService).signup(registerUserDto);
        assert response.getStatusCode().equals(HttpStatus.OK);
    }

    @Test
    public void registerExistingUserReturnsErrorResponse() throws Exception {
        // Arrange
        UserEntity registerUserDto = new UserEntity();
        registerUserDto.setEmail("existing@example.com");
        registerUserDto.setPassword("password");
        when(authenticationService.signup(any(UserEntity.class))).thenThrow(UsernameNotFoundException.class);

        // Act
        ResponseEntity<?> response =
                loginController.register(registerUserDto);

        // Assert
        assert response.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void authenticateValidLoginRequestReturnsJwtToken() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test@example.com");
        loginRequest.setPassword("password");
        UserEntity authenticatedUser = new UserEntity();
        authenticatedUser.setEmail("test@example.com");
        when(authenticationService.authenticate(any(LoginRequest.class))).thenReturn(authenticatedUser);
        when(jwtService.generateToken(anyString())).thenReturn("jwtToken");

        // Act
        ResponseEntity<LoginResponse> response =
                loginController.authenticate(loginRequest);

        // Assert
        assert response.getStatusCode().equals(HttpStatus.OK);
        assert response.getBody() != null;
        assert response.getBody().getJwt().equals("jwtToken");
    }
}

