package com.fb.posts.service.impl;

import com.fb.posts.dto.CommentDTO;
import com.fb.posts.entity.Comment;
import com.fb.posts.entity.Post;
import com.fb.posts.exception.ResourceNotFoundException;
import com.fb.posts.repository.CommentRepository;
import com.fb.posts.repository.PostRepository;
import com.fb.posts.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired

    private CommentRepository commentRepository;

    @Autowired

    private PostRepository postRepository;

    public Comment addComment(Long postId, String username, String text) throws ResourceNotFoundException {

        // Check if the post exists

        Post post = postRepository.findById(postId)

                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

// }

        Comment comment = new Comment();

        comment.setPost(post);

        comment.setUsername(username);

        comment.setText(text);

        comment.setCommentTimeStamp(LocalDateTime.now());

        return commentRepository.save(comment);

    }

    public String deleteCommentByIdAndPostIdAndUsername(Long commentId, Long postId, String username) throws ResourceNotFoundException {

        Optional<Comment> comment = commentRepository.findByIdAndPostIdAndUsername(commentId, postId, username);

        if (!comment.isPresent()) {

            throw new ResourceNotFoundException("Comment not found for the given ID, post ID, and username");

        }

        commentRepository.delete(comment.get());

        return "comment deleted successfully";

    }

    public CommentDTO convertToDTO(Comment comment) {

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());

        commentDTO.setUsername(comment.getUsername());

        commentDTO.setPostId(comment.getPost().getId());

        commentDTO.setText(comment.getText());

        commentDTO.setCommentTimeStamp(comment.getCommentTimeStamp());

        return commentDTO;

    }

    public Comment convertToEntity(CommentDTO commentDTO) {

        Comment comment = new Comment();

        comment.setId(commentDTO.getId());

        comment.setUsername(commentDTO.getUsername());

        comment.setText(commentDTO.getText());

        Post post = new Post();

        post.setId(commentDTO.getPostId());

        comment.setPost(post);

        return comment;

    }

    @Override

    public List<Comment> getCommentsByPostId(Long id) {

        return commentRepository.getCommentByPostId(id);

    }

}

