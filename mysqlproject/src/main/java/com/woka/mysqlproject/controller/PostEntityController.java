package com.woka.mysqlproject.controller;

import com.woka.mysqlproject.entity.PostEntity;
import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.response.ErrorResponse;
import com.woka.mysqlproject.service.PostEntityService;
import com.woka.mysqlproject.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class PostEntityController {

  @Autowired private PostEntityService postEntityService;

  @Autowired private UserEntityService userEntityService;

  @PostMapping
  public ResponseEntity<PostEntity> postNew(@RequestBody PostEntity article) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();

      // Fetch the user by ID, you can also fetch by username if needed
      UserEntity user = userEntityService.findByUsername(username);
      article.setAuthorId(user.getId());
    }
    postEntityService.postContent(article);
    return ResponseEntity.ok(article);
  }

  @GetMapping("/all-content")
  public ResponseEntity<List<PostEntity>> getAllPosts() {
    return ResponseEntity.ok(postEntityService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPostById(@PathVariable Long id) {

    try {
      PostEntity post = postEntityService.getById(id);
      return ResponseEntity.ok(post);
    } catch (Exception e) {
      ErrorResponse errorResponse =
          new ErrorResponse("Post Not Found ", HttpStatus.NOT_FOUND.value());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updatePostById(
      @PathVariable Long id, @RequestBody PostEntity updateUserRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();

      // Fetch the user by ID, you can also fetch by username if needed
      UserEntity user = userEntityService.findByUsername(username);
      try {
        PostEntity post = postEntityService.getById(id);
        if (user != null && user.getId().equals(post.getAuthorId())) {
          post.setContent(updateUserRequest.getContent());
          postEntityService.postContent(post);
          return ResponseEntity.ok(post);
        } else {
          ErrorResponse errorResponse =
              new ErrorResponse("You Don't have Access to this post", HttpStatus.NOT_FOUND.value());
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
      } catch (Exception e) {
        ErrorResponse errorResponse =
            new ErrorResponse("ID Not Found", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePostById(@PathVariable Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String username = userDetails.getUsername();

      // Fetch the user by ID, you can also fetch by username if needed
      UserEntity user = userEntityService.findByUsername(username);
      try {
        PostEntity post = postEntityService.getById(id);
        if (user != null && user.getId().equals(post.getAuthorId())) {
          postEntityService.deleteById(id);
          ErrorResponse errorResponse = new ErrorResponse("Delete Success", HttpStatus.OK.value());
          return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
        }
        ErrorResponse errorResponse =
            new ErrorResponse("You dont have access to this post", HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
      } catch (Exception e) {
        ErrorResponse errorResponse =
            new ErrorResponse("ID Not Found", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
