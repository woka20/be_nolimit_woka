package com.woka.mysqlproject.service;

import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public UserEntity register(UserEntity user) {
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new CustomUserDetails(user); // Return a CustomUserDetails object
    }
}

