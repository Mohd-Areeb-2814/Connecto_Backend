package com.fb.posts.repository;

import com.fb.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> getPostByUsername(String username);
    Optional<Post> findByUsernameAndId(String username, Long id);
}

