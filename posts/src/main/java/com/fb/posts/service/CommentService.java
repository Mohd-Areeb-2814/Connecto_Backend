package com.fb.posts.service;

import com.fb.posts.dto.CommentDTO;
import com.fb.posts.entity.Comment;
import com.fb.posts.exception.ResourceNotFoundException;

import java.util.List;

public interface CommentService {


    Comment addComment(Long postId, String username, String text) throws ResourceNotFoundException;

    CommentDTO convertToDTO(Comment comment);

    String deleteCommentByIdAndPostIdAndUsername(Long commentId, Long postId, String username) throws ResourceNotFoundException;

    List<Comment> getCommentsByPostId(Long id);

}