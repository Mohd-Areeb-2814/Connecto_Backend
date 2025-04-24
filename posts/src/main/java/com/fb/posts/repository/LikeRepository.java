package com.fb.posts.repository;

import com.fb.posts.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Like findByPostIdAndUsername(Long postId, String username);

    Long countByPostId(Long id);


    List<Like> getLikeByPostId(Long id);
}