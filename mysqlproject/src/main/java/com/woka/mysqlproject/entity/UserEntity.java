package com.woka.mysqlproject.entity;

import jakarta.persistence.*;

@Entity
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

    private String name;
    private String email;
    private String password;

    // getters and setters
}