package com.woka.mysqlproject.service;


import com.woka.mysqlproject.entity.PostEntity;
import com.woka.mysqlproject.entity.UserEntity;
import com.woka.mysqlproject.repository.PostEntityRepository;
import com.woka.mysqlproject.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class PostEntityService {

    @Autowired
    private PostEntityRepository postRepository;


    public PostEntity postContent(PostEntity article) {
        return postRepository.save(article);
    }

    public List<PostEntity> getAll() {
        return postRepository.findAll();
    }

    public PostEntity getById(Long id) {
        return postRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public List<PostEntity> getAllById(Long id) {
        return postRepository.findAllByAuthorId(id);
    }
}
