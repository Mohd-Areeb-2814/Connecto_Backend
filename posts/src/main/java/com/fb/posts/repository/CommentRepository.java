package com.fb.posts.repository;

import com.fb.posts.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    Comment findByPostIdAndUsernameAndText(Long postId, String username, String text);

    Optional<Comment> findByIdAndPostIdAndUsername(Long id, Long postId, String username);

    List<Comment> getCommentByPostId(Long id);

}