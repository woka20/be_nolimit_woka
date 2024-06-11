package com.woka.mysqlproject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woka.mysqlproject.entity.PostEntity;
import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.service.PostEntityService;
import com.woka.mysqlproject.service.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

public class PostEntityControllerTest {

    @Mock
    private PostEntityService postEntityService;

    @Mock
    private UserEntityService userEntityService;

    @InjectMocks
    private PostEntityController postEntityController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void postNewAuthenticatedUserReturnsValidPostEntity() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");

        PostEntity postEntity = new PostEntity();
        postEntity.setContent("New post");

        when(userEntityService.findByUsername("test@example.com")).thenReturn(userEntity);
        when(postEntityService.postContent(any(PostEntity.class))).thenReturn(postEntity);

        ResponseEntity<PostEntity> response = postEntityController.postNew(postEntity);

        verify(postEntityService).postContent(postEntity);
        assert response.getStatusCode().equals(HttpStatus.OK);
        assert response.getBody() != null;
        assert response.getBody().getAuthorId().equals(1L);
    }

    @Test
    public void getAllPostsReturnsListOfPostEntities() {
        PostEntity post1 = new PostEntity();
        post1.setContent("Post 1");

        PostEntity post2 = new PostEntity();
        post2.setContent("Post 2");

        List<PostEntity> posts = Arrays.asList(post1, post2);

        when(postEntityService.getAll()).thenReturn(posts);

        ResponseEntity<List<PostEntity>> response = postEntityController.getAllPosts();

        assert response.getStatusCode().equals(HttpStatus.OK);
        assert response.getBody() != null;
        assert response.getBody().size() == 2;
    }

    @Test
    public void getPostByIdValidIdReturnsPostEntity() {
        PostEntity postEntity = new PostEntity();
        postEntity.setContent("Test post");

        when(postEntityService.getById(1L)).thenReturn(postEntity);

        ResponseEntity<?> response = postEntityController.getPostById(1L);

        assert response.getStatusCode().equals(HttpStatus.OK);
        assert response.getBody() != null;
    }

    @Test
    public void getPostByIdInvalidIdReturnsErrorResponse() {
        when(postEntityService.getById(1L)).thenThrow(new RuntimeException("Post not found"));

        ResponseEntity<?> response = postEntityController.getPostById(1L);

        assert response.getStatusCode().equals(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updatePostByIdValidIdReturnsUpdatedPostEntity() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");

        PostEntity postEntity = new PostEntity();
        postEntity.setContent("Original content");
        postEntity.setAuthorId(1L);

        PostEntity updatedPostEntity = new PostEntity();
        updatedPostEntity.setContent("Updated content");

        when(userEntityService.findByUsername("test@example.com")).thenReturn(userEntity);
        when(postEntityService.getById(1L)).thenReturn(postEntity);
        when(postEntityService.postContent(any(PostEntity.class))).thenReturn(updatedPostEntity);

        ResponseEntity<?> response = postEntityController.updatePostById(1L, updatedPostEntity);

        verify(postEntityService).postContent(postEntity);
        assert response.getStatusCode().equals(HttpStatus.OK);
        assert ((PostEntity) response.getBody()).getContent().equals("Updated content");
    }

    @Test
    public void deletePostByIdValidIdReturnsSuccessResponse() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");

        PostEntity postEntity = new PostEntity();
        postEntity.setAuthorId(1L);

        when(userEntityService.findByUsername("test@example.com")).thenReturn(userEntity);
        when(postEntityService.getById(1L)).thenReturn(postEntity);

        ResponseEntity<?> response = postEntityController.deletePostById(1L);

        verify(postEntityService).deleteById(1L);
        assert response.getStatusCode().equals(HttpStatus.OK);
    }
}

