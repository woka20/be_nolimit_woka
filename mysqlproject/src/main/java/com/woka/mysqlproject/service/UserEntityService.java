package com.woka.mysqlproject.service;

import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserEntityService{

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

}

