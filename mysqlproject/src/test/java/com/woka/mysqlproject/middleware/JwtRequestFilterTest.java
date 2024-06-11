package com.woka.mysqlproject.middleware;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.woka.mysqlproject.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

class JwtRequestFilterTest {

  @Mock private HandlerExceptionResolver handlerExceptionResolver;

  @Mock private JwtService jwtService;

  @Mock private UserDetailsService userDetailsService;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @InjectMocks private JwtRequestFilter jwtRequestFilter;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.clearContext();
  }

    @Test
    public void doFilterInternalValidTokenSetsAuthentication() throws ServletException,
   IOException {
      when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
      when(jwtService.extractEmail("valid-token")).thenReturn("user@example.com");
      UserDetails userDetails = mock(UserDetails.class);
      when(userDetails.getUsername()).thenReturn("user@example.com");
      when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());
      when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
      when(jwtService.validateToken("valid-token", "user@example.com")).thenReturn(true);

      jwtRequestFilter.doFilterInternal(request, response, filterChain);

      SecurityContext securityContext = SecurityContextHolder.getContext();
      assertNotNull(securityContext.getAuthentication());
      assertEquals(
          "user@example.com",
          ((UserDetails) securityContext.getAuthentication().getPrincipal()).getUsername());
      verify(filterChain).doFilter(request, response);
    }


  @Test
  public void doFilterInternalNoTokenDoesNotSetAuthentication()
      throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn(null);

    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    SecurityContext securityContext = SecurityContextHolder.getContext();
    assertNull(securityContext.getAuthentication());
    verify(filterChain).doFilter(request, response);
  }


  @Test
  public void doFilterInternalExpiredTokenWithRefreshTokenSetsAuthentication()
      throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer expired-token");
    when(request.getHeader("isRefreshToken")).thenReturn("true");
    when(request.getRequestURL())
        .thenReturn(new StringBuffer("http://localhost:8080/refreshtoken"));
    ExpiredJwtException expiredJwtException = new ExpiredJwtException(null, null, "Token expired");
    when(jwtService.extractEmail("expired-token")).thenThrow(expiredJwtException);

    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    SecurityContext securityContext = SecurityContextHolder.getContext();
    assertNotNull(securityContext.getAuthentication());
    assertNull(securityContext.getAuthentication().getPrincipal());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  public void doFilterInternalInvalidTokenDoesNotSetAuthentication() throws ServletException, IOException {
    when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");

    jwtRequestFilter.doFilterInternal(request, response, filterChain);

    SecurityContext securityContext = SecurityContextHolder.getContext();
    assertNull(securityContext.getAuthentication());
    verify(filterChain).doFilter(request, response);
  }
}
