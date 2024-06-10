package com.woka.mysqlproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long authorId;
  private String content;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public Long getId() {
    return this.id;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdateAt() {
    return this.updatedAt;
  }

  public Long getAuthorId() {
    return this.authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getContent() {
    return this.content;
  }
}
