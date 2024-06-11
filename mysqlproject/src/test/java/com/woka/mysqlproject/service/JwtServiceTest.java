package com.woka.mysqlproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class JwtServiceTest {

  @InjectMocks private JwtService jwtService;

  @Value("${security.jwt.secret-key}")
  private String secretKey = "mysecretkey"; // Set a default value for testing

  @Value("${security.jwt.expiration-token}")
  private long jwtExpirationToken = 1000000; // Set a default value for testing

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
    ReflectionTestUtils.setField(jwtService, "jwtExpirationToken", jwtExpirationToken);
  }

  @Test
  public void extractEmailValidTokenReturnsEmail() {
    String token = createTestToken("test@example.com");

    String email = jwtService.extractEmail(token);

    assertEquals("test@example.com", email);
  }

  @Test
  public void extractExpirationValidTokenReturnsExpirationDate() {
    String token = createTestToken("test@example.com");

    Date expiration = jwtService.extractExpiration(token);

    assertNotNull(expiration);
  }

  @Test
  public void getExpirationTokenReturnsExpirationToken() {
    long expirationToken = jwtService.getExpirationToken();

    assertEquals(jwtExpirationToken, expirationToken);
  }

  @Test
  public void generateTokenValidUsernameReturnsToken() {
    String token = jwtService.generateToken("test@example.com");

    assertNotNull(token);
  }

  @Test
  public void validateTokenValidTokenReturnsTrue() {
    String token = createTestToken("test@example.com");

    Boolean isValid = jwtService.validateToken(token, "test@example.com");

    assertTrue(isValid);
  }

  @Test
  public void validateTokenInvalidTokenReturnsFalse() {
    String token = createTestToken("test@example.com");

    Boolean isValid = jwtService.validateToken(token, "wrong@example.com");

    assertFalse(isValid);
  }

  private String createTestToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationToken))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }
}
