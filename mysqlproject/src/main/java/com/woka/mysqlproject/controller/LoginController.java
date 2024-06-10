package com.woka.mysqlproject.controller;

import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.request.LoginRequest;
import com.woka.mysqlproject.response.ErrorResponse;
import com.woka.mysqlproject.response.LoginResponse;
import com.woka.mysqlproject.service.AuthService;
import com.woka.mysqlproject.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private final JwtService jwtService;

  private final AuthService authenticationService;

  public LoginController(JwtService jwtService, AuthService authenticationService) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/public/signup")
  public ResponseEntity<?> register(@RequestBody UserEntity registerUserDto) {
    try {
      UserEntity registeredUser = authenticationService.signup(registerUserDto);
      return ResponseEntity.ok(registeredUser);
    } catch (Exception e) {
      ErrorResponse errorResponse =
          new ErrorResponse("This Email Has Been Existing", HttpStatus.BAD_REQUEST.value());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
  }

  @PostMapping("/public/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest loginRequest) {
    UserEntity authenticatedUser = authenticationService.authenticate(loginRequest);

    String jwtToken = jwtService.generateToken(authenticatedUser.getUsername());

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setJwt(jwtToken);
    loginResponse.setExpiresIn(jwtService.getExpirationToken());

    return ResponseEntity.ok(loginResponse);
  }
}
