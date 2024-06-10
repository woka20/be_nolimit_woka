package com.woka.mysqlproject.repository;

import com.woka.mysqlproject.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
  PostEntity findByAuthorId(Long id);
  List<PostEntity> findAllByAuthorId(Long id);
}
