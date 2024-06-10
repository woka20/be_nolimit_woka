package com.woka.mysqlproject.service;

import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.repository.UserEntityRepository;
import com.woka.mysqlproject.request.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Objects;

@Service
public class AuthService {

  private final UserEntityRepository userEntityRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public AuthService(
      UserEntityRepository userEntityRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager) {
    this.userEntityRepository = userEntityRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  public UserEntity signup(UserEntity input) {
    UserEntity user = new UserEntity();
    user.setName(input.getName());
    user.setEmail(input.getEmail());
    user.setPassword(passwordEncoder.encode(input.getPassword()));
    if (!Objects.isNull(userEntityRepository.findByEmail(user.getEmail()))) {
      throw new KeyAlreadyExistsException();
    }
    return userEntityRepository.save(user);
  }

  public UserEntity authenticate(LoginRequest input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

    return userEntityRepository.findByEmail(input.getUsername());
  }
}
