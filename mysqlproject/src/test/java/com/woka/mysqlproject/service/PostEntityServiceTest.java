package com.woka.mysqlproject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.woka.mysqlproject.entity.PostEntity;
import com.woka.mysqlproject.repository.PostEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PostEntityServiceTest {

  @Mock private PostEntityRepository postRepository;

  @InjectMocks private PostEntityService postEntityService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void postContentValidArticleReturnsSavedArticle() {
    PostEntity article = new PostEntity();
    article.setContent("Test content");

    when(postRepository.save(any(PostEntity.class))).thenReturn(article);

    PostEntity result = postEntityService.postContent(article);

    assertNotNull(result);
    assertEquals("Test content", result.getContent());
    verify(postRepository).save(article);
  }

  @Test
  public void getAllReturnsListOfArticles() {

    PostEntity article1 = new PostEntity();
    article1.setContent("Content 1");

    PostEntity article2 = new PostEntity();
    article2.setContent("Content 2");

    List<PostEntity> articles = Arrays.asList(article1, article2);

    when(postRepository.findAll()).thenReturn(articles);

    List<PostEntity> result = postEntityService.getAll();

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(postRepository).findAll();
  }

  @Test
  public void getByIdValidIdReturnsArticle() {
    PostEntity article = new PostEntity();
    article.setContent("Test content");

    when(postRepository.findById(1L)).thenReturn(Optional.of(article));

    PostEntity result = postEntityService.getById(1L);

    assertNotNull(result);
    assertEquals("Test content", result.getContent());
    verify(postRepository).findById(1L);
  }

  @Test
  public void getByIdInvalidIdThrowsNullPointerException() {
    when(postRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NullPointerException.class, () -> postEntityService.getById(1L));
  }

  @Test
  public void deleteByIdValidIdDeletesArticle() {
    postEntityService.deleteById(1L);

    verify(postRepository).deleteById(1L);
  }

  @Test
  public void getAllByIdValidAuthorIdReturnsArticles() {
    PostEntity article1 = new PostEntity();
    article1.setContent("Content 1");
    article1.setAuthorId(1L);

    PostEntity article2 = new PostEntity();
    article2.setContent("Content 2");
    article2.setAuthorId(1L);

    List<PostEntity> articles = Arrays.asList(article1, article2);

    when(postRepository.findAllByAuthorId(1L)).thenReturn(articles);

    List<PostEntity> result = postEntityService.getAllById(1L);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(postRepository).findAllByAuthorId(1L);
  }
}
